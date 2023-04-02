(ns ui.hiccup-to-react
  (:require
   ["react" :as react]
   [clojure.string :refer [capitalize split]]))

(defn keyword->camel-case [kw]
  (let [parts (-> kw name (split #"-"))]
    (apply str (map-indexed (fn [idx s] (if (zero? idx) s (capitalize s))) parts))))

(defn create-elements
  ([hiccup] (create-elements hiccup {}))
  ([hiccup props]
   (let [el-type (first hiccup)
         el-props (if (map? (second hiccup)) (second hiccup) {})
         children (if (map? (second hiccup)) (drop 2 hiccup) (drop 1 hiccup))
         react-props (into {} (for [[k v] el-props] [(keyword->camel-case k) v]))]
      
     (apply
      react/createElement
      (if (keyword? el-type)
        (keyword->camel-case el-type)
        el-type)
      (clj->js (merge react-props props))
      (map (fn [child]
             (if (vector? child)
               (create-elements child)
               child))
           children)))))
