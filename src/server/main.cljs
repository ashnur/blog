(ns server.main
  (:require
   [ui.hiccup :refer [hcc]]
   ["express" :as express]
   ["serve-static" :as serve-static]
   ["node:http" :as http]
   ["react" :as react :refer [createElement]]
   ["react-dom/server" :as rdc :refer [renderToPipeableStream]]))

(defonce server (atom nil))

(def serve (serve-static "build", #js {:index false}))

(defn request-handler [^js req ^js res next]
  (let [did-error (atom false)
        stream (atom nil)]

    (if (not (= (.-url req) "/"))
     (serve req res next)
     (reset!
      stream
      (renderToPipeableStream
       (hcc [:index])
       #js {:onError (fn [e]
                       (reset! did-error true)
                       (js/console.error "Error rendering" e))
            :onAllReady
            (fn []
              (js/console.log "ALL READY"))
            :onShellError
            (fn [err]
              (set! (.-statusCode res) 500)
              (.send res (str "ERROR:" err)))
            :onShellReady
            (fn []
              (set! (.-statusCode res) (if @did-error 500 200))
              (.setHeader res "Content-Type" "text/html")
              (.pipe ^js @stream res)
              (.end res))})))))

(defn start [bs]
  ^:dev/after-load
  (let [app (express)]
    (.use app request-handler)
    (reset! server (http/createServer app))
    (.listen ^js/Node.http.Server @server 8000 (fn [] (println "Server running on port 8000")))
    bs))

; (defn stop-server []
;   ^:dev/before-load
;   (when @server (.close @server)))
; (defn -dev [& args]
;   (js/console.log "starting development server")
;   (try
;     (renderToPipeableStream (hiccup/html (slurp "../ui/html.edn"))
;       (.pipe stdout))
;     (catch js/Object e 
;       (->> e
;         (.stringify js/JSON)
;         (.log js/console)))))



(defn reload! []
  (println "Code updated."))
