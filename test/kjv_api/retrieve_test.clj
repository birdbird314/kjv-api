(ns kjv-api.retrieve-test
  (:require [clojure.test :refer :all]
            [kjv-api.retrieve :refer [retrieve-book-content-fn]]))

(defn slurp-mock 
  [path]
  (cond
    (= "somePrefix/book_name.txt" path) "dummyBookContent"
	:else ""))

(def retrieve-book-content (retrieve-book-content-fn slurp-mock "somePrefix/"))

(deftest retrieve-book-content-test
  (testing "Should delegate retrieving with correct path"     
    (is (= "dummyBookContent" (retrieve-book-content "book_name"))))) 


