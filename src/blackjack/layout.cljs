(ns blackjack.layout
  (:require [reagent.core :as r]))

(defn header [] 
  (let
    [points (:points @blackjack.core/app-state)]
    [:div { :class "container" :id "header" }
     [:p { :class "pull-left" } [:a { :href "/#" } "CASINO"]]
     [:p { :class "pull-right" } (str "Score: " points)]]))

(defn debug-panel [] [:div#debug (str @blackjack.core/app-state)])

(defn page []
  (let [this (r/current-component)
        children (r/children this)]
  [:div
   [header]
   (into [:div.container] children)
   [debug-panel]]))
