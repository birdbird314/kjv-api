(ns kjv-api.retrieve
  (:require [clojure.string :as string]))

(defn- verse-number
  [verse]
  (clojure.string/replace 
    (re-find #"\{\d+:\d+\}" verse)
	#"\{|\}"
	""))

(defn- clean-verse-numbers [verse]
  (clojure.string/replace verse #"\{\d+:\d+\}\s" ""))

(defn- verse-leq? [v1 v2] 
  (<= (compare v1 v2) 0))

(defn- split-verses [book-content]
  (clojure.string/split
	(clojure.string/replace book-content #"[^$]\{" ";{")
	#";"))

(defn retrieve-verse
  [verses book-content]
  (clojure.string/join " "
	(map clean-verse-numbers
	  (take-while #(verse-leq? (verse-number %) (:end verses (:start verses))) 
		(drop-while #(not (= (verse-number %) (:start verses))) 
		  (split-verses book-content)))))) 


