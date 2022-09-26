(ns microservice-boilerplate.ports.http-out
  (:require [microservice-boilerplate.adapters :as adapters.price]
            [microservice-boilerplate.schemas.types :as schemas.types]
            [parenthesin.components.http :as components.http]
            [parenthesin.json :as json]
            [schema.core :as s]))

(set! *warn-on-reflection* true)

(s/defn get-btc-usd-price :- s/Num
  [http :- schemas.types/HttpComponent]
  (-> {:url "https://api.coindesk.com/v1/bpi/currentprice.json"
       :method :get}
      (as-> request (components.http/request http request))
      :body
      json/decode
      adapters.price/wire->usd-price))
