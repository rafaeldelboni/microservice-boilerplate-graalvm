(ns unit.parenthesin.components.interoute-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [matcher-combinators.test :refer [match?]]
            [parenthesin.interoute :as interoute]
            [ring.mock.request :as mock]
            [schema.test :as schema.test]))

(use-fixtures :once schema.test/validate-schemas)

(def route-handlers
  (interoute/routes->handler
   [{:path "/wallet/deposit"
     :method :post
     :handler (fn [_] {:status 201 :body "post"})}
    {:path "/wallet/list"
     :method :get
     :handler (fn [_] {:status 200 :body "get"})}]))

(deftest parse-query-test
  (testing "parse-query enter should parse query-string into map"
    (is (match? {:status 201 :body "post"}
                (->> (mock/request :post "/wallet/deposit")
                     route-handlers)))
    (is (match? {:status 200 :body "get"}
                (->> (mock/request :get "/wallet/list")
                     route-handlers)))))
