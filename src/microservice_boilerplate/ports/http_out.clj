(ns microservice-boilerplate.ports.http-out
  (:require [jsonista.core :as json]
            [microservice-boilerplate.adapters :as adapters.price]
            [microservice-boilerplate.schemas.types :as schemas.types]
            [parenthesin.components.http :as components.http]
            [parenthesin.interceptors :as interceptors]
            [schema.core :as s]))

(set! *warn-on-reflection* true)

(s/defn get-btc-usd-price :- s/Num
  [http :- schemas.types/HttpComponent]
  (-> {:url "https://api.coindesk.com/v1/bpi/currentprice.json"
       :method :get}
      (as-> request (components.http/request http request))
      :body
      (json/read-value interceptors/json-mapper)
      adapters.price/wire->usd-price))
