(ns kjv-api.validate)

(defn- wrong-book-message [book] 
  (if (= "" book)
    "Empty book name is invalid"
    (str "Book name \"" book "\" is invalid")))

(defn- wrong-verse-message [verse]
  (str "Verse number format: " verse " is invalid. Valid example: \"2:10\""))

(defn- get-wrong-book-message-or-nil [request books]
  (if-not (some #(= % (:book request)) books)
    {:error (wrong-book-message (:book request))}))

(defn- verse-format-is-valid? [verse] (re-matches #"\d+:\d+" verse)) 

(defn- get-wrong-verse-message-or-nil [request]
  (merge-with #(str %1 "\n" %2)
    (if-not (verse-format-is-valid? (:start request))
      {:error (wrong-verse-message (:start request))})
    (if-not (or (verse-format-is-valid? (:end request)) (nil? (:end request)))
      {:error (wrong-verse-message (:end request))})))

(defn- get-out-of-order-verse-message-or-nil [request]
  (if (and 
        (verse-format-is-valid? (:start request))
        (verse-format-is-valid? (:end request))
        (< 0 (compare (:start request) (:end request))))
    {:error (str "Start verse " (:start request) " is after end verse " (:end request))}))
      
(defn get-error-message-fn [books] 
  (fn [request] 
    (merge-with #(str %1 "\n" %2)
      (get-wrong-book-message-or-nil request books)
      (get-wrong-verse-message-or-nil request)
      (get-out-of-order-verse-message-or-nil request))))

(def get-error-message 
  (get-error-message-fn 
    (clojure.string/split (slurp "resources/books_list.txt") #"\s")))

