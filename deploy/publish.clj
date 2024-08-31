#!/usr/bin/env bb

(ns publish
  (:require [clojure.string :as str]))

(defn parse-args [args]
  (let [args (into {} (partition 2 args))
        {:strs [remote-host release-dir jail-hostname deploy-dir]} args
        defaults {"remote-host" "Default Remote Host"
                  "release-dir" "Default Release Dir"
                  "jail-hostname" "Default Jail Hostname"
                  "deploy-dir" "Default Deploy Dir"}]
    (merge defaults args)))

(defn -main [& args]
  (println args)
  (let [opts (parse-args (map str/lower-case args))]
    (println "Remote Host:" (opts "remote-host"))
    (println "Release Dir:" (opts "release-dir"))
    (println "Jail Hostname:" (opts "jail-hostname"))
    (println "Deploy Dir:" (opts "deploy-dir"))))

(defn -entry []
  (-main *command-line-args*))

