(ns browser.main
  (:require
    ["react-dom/client" :as rdc :refer [hydrateRoot createRoot]]
    ["react" :as react :refer [createElement]]
    [ui.hiccup :refer [hcc]]))

(defonce container (atom nil))
(defonce root (atom nil))


(defn render-ui []
  (hydrateRoot
    @container
    (hcc [:index] :index [2 1 2])))

(defn hot-reload-ui []
  (.render @root (hcc [:index] :index [2 1 2])))

(defn init []
  (let [doc (-> js/window .-document)
        readyState (.-readyState doc)
        on-load #(do
                   (reset! container (-> "app" js/document.getElementById))
                   (reset! root (render-ui)))]
    (js/console.info "browser.main/init")
    (if (= readyState "loading")
      (.addEventListener js/window "DOMContentLoaded", #(on-load))
      (on-load))))