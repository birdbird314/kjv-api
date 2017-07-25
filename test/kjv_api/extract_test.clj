(ns kjv-api.extract-test
  (:require [clojure.test :refer :all]
            [kjv-api.extract :refer :all]))

(def book-content "{1:1} verse 1 {1:2} verse 2")

(deftest extract-verses-test
  (testing "Should return empty string for empty sequence"
    (is (= "" (extract-verses `() book-content))))

  (testing "Should return empty string for out of scope verse"
    (is (= "" (extract-verses {:start "13:13"} book-content))))

  (testing "Should extract single verse"
    (is (= "verse 1" (extract-verses {:start "1:1"} book-content))))
			
  (testing "Should extract multiple verses"
    (is (= "verse 1 verse 2" (extract-verses {:start "1:1" :end "1:2"} book-content))))

  (testing "Should return everything till end of the book if verse is out of scope"
    (is (= "verse 1 verse 2" (extract-verses {:start "1:1" :end "5:2"} book-content)))))

