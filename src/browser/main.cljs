(ns browser.main
  (:require
   ["react-dom/client" :as rdc :refer [hydrateRoot]]
   ["react" :as react :refer  [createElement]]
   [ui.hiccup :refer [hcc]]))


(defn render-ui []
  (hydrateRoot  
    (-> js/window .-document .-body) 
    (hcc [:index] [2])))


(defn init []
  (let [doc (-> js/window .-document)
        readyState (.-readyState doc)]
    (if (= readyState "loading")
      (.addEventListener js/window "DOMContentLoaded", #(render-ui))
      (render-ui))))
