(ns ui.hiccup
  (:require 
    [hicada.compiler]
    [edamame.core :as e :refer [parse-string]]
    [clojure.string :as str]))

(defn get-current-namespace []
    (-> #'get-current-namespace meta :ns))

(def ns-str 
  (-> (get-current-namespace) 
      str 
      (str/split #"\.") 
      drop-last))

(defn vec->str [path-vec] 
  (->> path-vec 
      (map name)
      (cons (str/join "/" ns-str))
      (interpose "/") 
      (apply str)))

(defn str->content [path-str]
  (parse-string (slurp (str "./src/" path-str ".edn"))))

(defn select-node 
  ([v] v)  
  ([v indeces] (get-in v indeces)))

(defmacro hcc [path-vec & {:keys [index]}]
  (prn 'z (-> path-vec vec->str str->content (select-node index)))
  (hicada.compiler/compile 
    (-> path-vec vec->str str->content (select-node index))
    {:create-element 'react/createElement 
     ;:transform-fn (comp)
     :array-children? false}))
