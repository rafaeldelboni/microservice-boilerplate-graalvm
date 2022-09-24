(ns microservice-boilerplate.schemas.wire-out
  (:require [schema.core :as s]))

(set! *warn-on-reflection* true)

(s/defschema CoinDeskResponse
  {:bpi {:USD {:rate_float s/Num
               s/Any s/Any}
         s/Any s/Any}
   s/Any s/Any})
