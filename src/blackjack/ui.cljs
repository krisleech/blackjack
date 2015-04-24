(ns blackjack.ui
  (:require [reagent.core :as r]))

(def button-defaults { :class "btn btn-primary" })

(defn button []
  (let [this (r/current-component)
        props (merge button-defaults (r/props this))]
  [:button props (:label props)]))
