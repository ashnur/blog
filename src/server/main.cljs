(ns server.main
  (:require
   [ui.hiccup :refer [hcc]]
   [promesa.core :as p]
   ["express" :as express]
   ["serve-static" :as serve-static]
   ["node:http" :as http]
   ["react" :as react :refer [createElement]]
   ["react-dom/server" :as rdc :refer [renderToPipeableStream renderToStaticMarkup]]
   ["nsfw" :as nsfw]))

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
              (.send res (str "ERROR:" err)))`
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
    ;(start-watcher logdir)
    (reset! server (http/createServer app))
    (.listen ^js/Node.http.Server @server 8000 (fn [] (println "Server running on port 8000")))
    bs))


(defn stop-server []
  (when @server
    (js/console.log "Shutting down gracefully...")
    (.close ^js/Node.http.Server @server
            (fn []
              (js/console.log "Server closed")
              (reset! server nil)))
    (js/process.exit 0)))

(js/process.on "SIGINT" stop-server)
(js/process.on "SIGTERM" stop-server)


(defn reload! []
  (println "Code updated."))

