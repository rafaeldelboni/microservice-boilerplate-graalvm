(ns unit.parenthesin.components.interceptors-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [matcher-combinators.test :refer [match?]]
            [parenthesin.interceptors :as interceptors]
            [ring.mock.request :as mock]
            [schema.core :as s]
            [schema.test :as schema.test]))

(use-fixtures :once schema.test/validate-schemas)

(deftest parse-body-test
  (let [{:keys [enter leave]} interceptors/parse-body]
    (testing "parse-body enter should not parse text body bytes into map"
      (is (match? {:body "pure text"}
                  (enter {:body "pure text"}))))
    (testing "parse-body enter should parse json body bytes into map"
      (is (match? {:body {:name "human" :number 2}}
                  (enter (mock/json-body {} {:name "human" :number 2})))))
    (testing "parse-body leave should not parse string"
      (is (match? {:body "test"}
                  (leave {:body "test"}))))
    (testing "parse-body leave should parse vec into json string"
      (is (match? {:body "[{\"name\":\"human\",\"number\":2}]"}
                  (leave {:body [{:name "human" :number 2}]}))))
    (testing "parse-body leave should parse map into json string"
      (is (match? {:body "{\"name\":\"human\",\"number\":2}"}
                  (leave {:body {:name "human" :number 2}}))))))

(deftest parse-query-test
  (let [{:keys [enter]} interceptors/parse-query]
    (testing "parse-query enter should parse query-string into map"
      (is (match? {:query {:a "first" :b "2"}}
                  (enter {:query-string "a=first&b=2"}))))))

(deftest coerce-schema-test
  (let [{:keys [enter leave]} interceptors/coerce-schema]
    (testing "coerce-schema enter should not coerce inputs without defined in schema"
      (is (match? {:query {:a "first" :b 2 :k :key}
                   :params {:a "first" :b 2 :k :key}
                   :body {:name "human" :keyword :nerd :number "2"}}
                  (enter {:query {:a "first" :b 2 :k :key}
                          :params {:a "first" :b 2 :k :key}
                          :body {:name "human" :keyword :nerd :number "2"}}))))
    (testing "coerce-schema enter should coerce inputs with defined in schema"
      (is (match? {:query {:a "first" :b 2 :k :key}
                   :params {:a "first" :b 2 :k :key}
                   :body {:name "human" :keyword :nerd :number 2}}
                  (enter {:parameters {:query {:a s/Str :b s/Int :k s/Keyword}
                                       :path {:a s/Str :b s/Int :k s/Keyword}
                                       :body {:name s/Str :keyword s/Keyword :number s/Int}}
                          :query {:a "first" :b "2" :k "key"}
                          :params {:a "first" :b "2" :k "key"}
                          :body {:name "human" :keyword "nerd" :number 2}}))))
    (testing "coerce-schema leave should not coerce response without defined in schema"
      (is (match? {:status 200
                   :body {:name "human" :keyword :nerd :number "2"}}
                  (leave {:status 200
                          :body {:name "human" :keyword :nerd :number "2"}}))))
    (testing "coerce-schema leave should coerce response without defined in schema"
      (is (match? {:status 200
                   :body "test"}
                  (leave {:responses {200 {:body s/Str}}
                          :status 200
                          :body "test"}))))))
