(ns kjv-api.validate)

(defn- wrong-book-message [book] 
  (if (= "" book)
    "Empty book name is invalid"
    (str "Book name \"" book "\" is invalid")))

(defn- wrong-verse-message [verse]
  (str "Verse number format: " verse " is invalid. Valid example: \"2:10\""))

(defn- get-wrong-book-message-or-nil [request books]
  (if (some #(= % (:book request)) books)
    nil
    {:error (wrong-book-message (:book request))}))

(defn- verse-is-valid? [verse] (re-matches #"\d+:\d+" verse)) 

(defn- get-wrong-verse-message-or-nil [request]
  (merge-with #(str %1 "\n" %2)
    (if (verse-is-valid? (:start request))
      nil
      {:error (wrong-verse-message (:start request))})
	(if (or (verse-is-valid? (:end request)) (nil? (:end request)))
      nil
      {:error (wrong-verse-message (:end request))})))

(defn- get-out-of-order-verse-message-or-nil [request]
  (if 
    (and 
      (verse-is-valid? (:start request))
      (verse-is-valid? (:end request))
	  (< 0 (compare (:start request) (:end request))))
	{:error (str "Start verse " (:start request) " is after end verse " (:end request))}
	nil))
    	
(defn get-error-message-fn [books] 
  (fn [request] 
    (merge-with #(str %1 "\n" %2)
	  (get-wrong-book-message-or-nil request books)
      (get-wrong-verse-message-or-nil request)
      (get-out-of-order-verse-message-or-nil request))))

(def get-error-message 
  (get-error-message-fn 
    (clojure.string/split (slurp "resources/books_list.txt") #"\s")))

