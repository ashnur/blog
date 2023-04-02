(ns server.test.main-test
  (:require [cljs.test :refer (deftest is)]))
;            ["pm2" :as pm2]))

; (-> pm2
;   .connect (fn [err]
;             (if err
;               (-> console .error err)
;               (-> process .exit 2))
;             (-> pm2
;               .start #js {:script "api.js"
;                           :name "api"})))


;; testing the server
;; send a request, expect a response


(deftest a-failing-test
  (is (= 1 2)))
