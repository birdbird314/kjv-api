(ns kjv-api.core
  (:require [kjv-api.validate :refer [get-error-message]]
            [kjv-api.retrieve :refer [retrieve-book-content]]
            [kjv-api.extract :refer [extract-verses]]
  
  ))

(defn- verses-from [request] (select-keys request [:start :end]))

(defn- fetch-passage-from [request]
  (extract-verses (verses-from request)
    (retrieve-book-content (:book request))))

(defn process-request
  [request]
  (if-let [error (get-error-message request)]
    error
	{:passage (fetch-passage-from request)}))

