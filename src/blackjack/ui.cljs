(ns blackjack.ui
  (:require [reagent.core :as r]))

(def button-defaults { :disabled false :class "btn btn-primary" })

(defn button []
  (let [this (r/current-component)
        props (merge button-defaults (r/props this))]
  [:button props (:label props)]))

(def sparkline-defaults {})

;; [1 2 3 4 5 6] -> "0 1, 10 2, 20 3, 30 4, 40 5, 50 6"
(defn data-to-path [data] 
  (let [ x-points (map #(* % 10) (range (count data)))
        data-diff (-  (apply max data) (apply min data))
        axis-diff 200
        modifer (if (> data-diff axis-diff) * /)
        divider (/ axis-diff data-diff)
        modified-data (reverse (map #(modifer divider %) data))]
    (println data-diff)
    (println divider)
    (apply str (interpose "," (mapv #(str (first %) " " (last %)) (mapv vector x-points modified-data))))))

(defn sparkline []
  (let [this (r/current-component)
        props (merge sparkline-defaults (r/props this))
        data (:data props)]
    (println data)
    (println (data-to-path data))
  [:svg { :width "200px" :height "200px" :version "1.1" :xmlns "http://www.w3.org/2000/svg" }
   [:path { :d "M 0 0 L 20 10 L 40 10 L 60 70 L 80 100" :fill "transparent" :stroke "black"  }]
   [:polyline { :points (data-to-path data) :fill "none" :stroke "red"  }]
   ]))

