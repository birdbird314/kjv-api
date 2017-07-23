(ns kjv-api.retrieve-test
  (:require [clojure.test :refer :all]
            [kjv-api.retrieve :refer :all]))

(def book-content "{1:1} verse 1 {1:2} verse 2")

(deftest retrieve-verse-test
  (testing "Should return empty string for empty sequence"
    (is (= "" (retrieve-verse `() book-content))))

  (testing "Should return empty string for out of scope verse"
    (is (= "" (retrieve-verse {:start "13:13"} book-content))))

  (testing "Should retrieve single verse"
    (is (= "verse 1" (retrieve-verse {:start "1:1"} book-content))))
			
  (testing "Should retrieve multiple verses"
    (is (= "verse 1 verse 2" (retrieve-verse {:start "1:1" :end "1:2"} book-content))))

  (testing "Should return everything till end of the book if verse is out of scope"
    (is (= "verse 1 verse 2" (retrieve-verse {:start "1:1" :end "5:2"} book-content)))))


