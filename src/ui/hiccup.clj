(ns ui.hiccup
  (:require
    [cljs.env :as env]
    [hicada.compiler]
    [edamame.core :as e :refer [parse-string]]
    [clojure.string :as str]
    [shadow.resource :as rc]))

(defn get-current-namespace []
    (-> #'get-current-namespace meta :ns))


(defn vec->str [path-vec] 
  (->> path-vec 
      (map name)
      (interpose "/")
      (apply str)))

(defn str->content [path-str]
  (parse-string (rc/slurp-resource {:ns {:name 'ui.hiccup}}  (str "./" path-str ".edn"))))

(defn select-node 
  ([v] v)  
  ([v indices] (get-in v indices)))

(defmacro hcc [path-vec & {:keys [index]}]
  (hicada.compiler/compile
    (-> path-vec vec->str str->content (select-node index))
    {:create-element 'react/createElement
     ;:transform-fn (comp)
     :array-children? false}))
