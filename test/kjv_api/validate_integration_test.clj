(ns kjv-api.validate-integration-test
  (:require [clojure.test :refer :all]
            [kjv-api.validate :refer :all]))

(defn- assert-request->message [request expected-result] 
 (is (= expected-result (get-error-message request))))

(deftest get-error-message-test
  (testing "Should return nil for valid request"
    (assert-request->message 
      {:book "Psalms" :start "1:1" :end "1:2"}
      nil))
  
  (testing "Should recognize invalid book name"
    (assert-request->message 
      {:book "Plms" :start "1:1" :end "1:2"}
      {:error "Book name \"Plms\" is invalid"})
	  (assert-request->message 
      {:book "" :start "1:1" :end "1:2"}
      {:error "Empty book name is invalid"})))



