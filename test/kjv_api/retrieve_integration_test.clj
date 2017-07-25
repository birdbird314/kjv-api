(ns kjv-api.retrieve-integration-test
  (:require [clojure.test :refer :all]
            [kjv-api.retrieve :refer [retrieve-book-content-fn]]))

(def retrieve-book-content (retrieve-book-content-fn slurp "resources/test/books/"))

(deftest retrieve-book-content-integration-test
  (testing "Should retrieve book content"
    (is (= "some content from book_name.txt\n" (retrieve-book-content "book_name"))))) 
