(ns server.main
  (:require
   ["express" :as express]
   ["serve-static" :as serve-static]
   ["node:fs" :as fs]
   ["node:http" :as http]
    ; ["node:fs/readFileSync" :as slurp] 
    ; ["node:process/stdout" :as stdout] 
    ; ["node:stream/pipeline" :as pipeline]
   ["react-dom/server" :as rdc :refer [renderToPipeableStream]]
   [ui.html :as ui-html]
   [ui.hiccup-to-react :refer [create-elements]]))

(defonce server (atom nil))



(def serve (serve-static "build", #js {:index false}))

(defn reload! []
  (println "Code updated."))



(defn request-handler [^js req ^js res next]
  (let [did-error (atom false)
        stream (atom nil)]
    (if (not (= (.-url req) "/"))
      (do
        (js/console.log (.-url req))
        (serve req res next))
      (reset!
       stream
       (renderToPipeableStream
        (create-elements ui-html/index)
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
    (.listen @server 8000 (fn [] (println "Server running on port 8000")))
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



