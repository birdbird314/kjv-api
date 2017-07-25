(ns kjv-api.retrieve)

(defn retrieve-book-content-fn 
  [content-provider path-prefix]
  (fn [book-name] (content-provider (str path-prefix book-name ".txt"))))

(def retrieve-book-content 
  (retrieve-book-content-fn slurp "resources/books/"))
