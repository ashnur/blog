(ns browser.build-hooks.edn)

(defn preload
  {:shadow.build/stage :compile-finish}
  [build-state & args]
  (prn build-state)
  build-state)

