(ns ui.hiccup
  (:require 
    [ui.html]
    [hicada.compiler :refer [compile]]))

(defmacro hcc-server [body]
  (hicada.compiler/compile 
    (eval body)
    {:create-element 'react/createElement 
     ;:transform-fn (comp)
     :array-children? false}))

(defmacro hcc-client [body]
  (hicada.compiler/compile 
    (eval body)
    {:create-element 'js/React.createElement
     ;:transform-fn (comp)
     :array-children? false}))

