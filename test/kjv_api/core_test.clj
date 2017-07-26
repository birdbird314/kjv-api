(ns kjv-api.core-test
  (:require [clojure.test :refer :all]
            [kjv-api.core :refer :all]))

(def invalid-request {:book "Pat" :start ".d?" :end "df"})

(def genesis-request {:book "Genesis" :start "1:1"})
(def genesis-first-verse "In the beginning God created the heaven and the earth.")

(def john-request {:book "John" :start "1:1" :end "1:3"})
(def john-first-verses "In the beginning was the Word, and the Word was with God, and the Word was God. The same was in the beginning with God. All things were made by him; and without him was not any thing made that was made.")

(deftest core-test
  (testing "Should return error message for invalid request"
    (let [result (process-request invalid-request)]
      (is (not (nil? (:error result))))))

  (testing "Should return single verse properly"
    (let [result (process-request genesis-request)]
      (is (= {:passage genesis-first-verse} result)))) 

  (testing "Should return multiple verses properly"
    (let [result (process-request john-request)]
      (is (= {:passage john-first-verses} result)))))  
