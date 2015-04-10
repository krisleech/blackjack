(ns blackjack.compontents
  (:require [reagent.core :as r]))

;; Generic compontents for building a UI

(def button-defaults { :class 'btn' })

(defn button [] 
  (let [this (r/current-component)
        props (merge button-defaults (r/props this))]
  [:button props (:label props)]))
