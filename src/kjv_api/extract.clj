(ns kjv-api.extract
  (:require [clojure.string :as string]))

(defn- verse-number
  [verse]
  (clojure.string/replace 
    (re-find #"\{\d+:\d+\}" verse)
    #"\{|\}"
    ""))

(defn- clean-verse-numbers [verse]
  (clojure.string/replace verse #"\{\d+:\d+\}\s" ""))

(defn- clean-line-breaks [verse]
  (reduce
    (fn 
	  [v [regex replacement]] 
	    (clojure.string/replace v regex replacement))
    verse
     [[#"([^\r])\n([^\r])" "$1 $2"]
     [#"([^\n])\r([^\n])" "$1 $2"]
     [#"\r\n" " "]
     [#"\n\r" " "]]))
   
(defn- verse-leq? [v1 v2] 
  (<= (compare v1 v2) 0))

(defn- is-actual-verse? [text] (= \{ (first text)))

(defn split-verses [book-content]
  (filter is-actual-verse? 
    (clojure.string/split
      (clojure.string/replace book-content #"[^$]\{" ";;;;{")
      #";;;;")))

(defn extract-verses
  [verses book-content]
  (clojure.string/join " "
    (map (comp clean-verse-numbers clean-line-breaks)
      (take-while #(verse-leq? (verse-number %) (:end verses (:start verses))) 
        (drop-while #(not (= (verse-number %) (:start verses))) 
          (split-verses book-content)))))) 


