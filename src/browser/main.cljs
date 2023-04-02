(ns browser.main
  (:require 
    ["react-dom/client" :as rdc :refer [hydrateRoot]]
    ["react" :as react]
    [ui.html :as ui-html]
    [ui.hiccup-to-react :refer [create-elements]]))

(defn render-ui []
  (hydrateRoot  (-> js/window .-document .-body) (create-elements (nth ui-html/index 3)))) 

(defn init []
  (let [doc (-> js/window .-document)
        readyState (.-readyState doc)]))
    ; (if (= readyState "loading")
    ;   (.addEventListener js/window "DOMContentLoaded", #(render-ui))
    ;   (render-ui))))
