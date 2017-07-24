(ns kjv-api.validate-test
  (:require [clojure.test :refer :all]
            [kjv-api.validate :refer :all]))

(def books ["Mat"])

(def get-error-message (get-error-message-fn books))

(defn- assert-request->message [request expected-result] 
 (is (= expected-result (get-error-message request))))

(deftest get-error-message-test
  (testing "Should return nil for valid request"
    (assert-request->message 
	  {:book "Mat" :start "1:1" :end "1:2"}
	  nil))

  (testing "Should recognize invalid book name"
    (assert-request->message 
	  {:book "Pat" :start "1:1" :end "1:2"}
	  {:error "Book name \"Pat\" is invalid"})
	(assert-request->message 
	  {:book "" :start "1:1" :end "1:2"}
	  {:error "Empty book name is invalid"}))		
  
  (testing "Should recognize invalid verse format"
    (assert-request->message 
	  {:book "Mat" :start "4%df" :end "1:2"}
	  {:error "Verse number format: 4%df is invalid. Valid example: \"2:10\""})

	(assert-request->message 
	  {:book "Mat" :start "1:1" :end "4%df"}
	  {:error "Verse number format: 4%df is invalid. Valid example: \"2:10\""}))

  (testing "Should merge error messages"
    (assert-request->message 
	  {:book "Pat" :start "4%df" :end "1:2"}
	  {:error "Book name \"Pat\" is invalid\nVerse number format: 4%df is invalid. Valid example: \"2:10\""}))

  (testing "Should recognize out of order verses"
    (assert-request->message 
	  {:book "Mat" :start "1:2" :end "1:1"}
	  {:error "Start verse 1:2 is after end verse 1:1"}))) 
