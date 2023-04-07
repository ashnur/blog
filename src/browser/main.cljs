(ns browser.main
  (:require
   ["react-dom/client" :as rdc :refer [hydrateRoot]]
   ["react" :as react :refer  [createElement]]
   [ui.html :as ui-html]
   [ui.hiccup :refer [hcc-client]]))


(defn render-ui []
  (let [html #(nth ui.html/index 2)]
    (hydrateRoot  
      (-> js/window .-document .-body) 
      (clj->js (hcc-client (html))))))

(js/console.log "asd")

(defn init []
  (let [doc (-> js/window .-document)
        readyState (.-readyState doc)]
    (if (= readyState "loading")
      (.addEventListener js/window "DOMContentLoaded", #(render-ui))
      (render-ui))))
