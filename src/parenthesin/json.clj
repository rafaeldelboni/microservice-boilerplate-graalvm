(ns parenthesin.json
  (:require [jsonista.core :as json]))

(def mapper
  (json/object-mapper
   {:encode-key-fn name
    :decode-key-fn keyword}))

(defn encode [x] (.writeValueAsString mapper x))

(defn decode [x] (.readValue mapper ^String x ^Class Object))
