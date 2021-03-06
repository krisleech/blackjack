(ns blackjack.layout
  (:require [reagent.core :as r]))

(defn header [] 
  (let
    [points (:points @blackjack.core/app-state)]
    [:div#header.container
     [:div { :class "pull-left" } [:a { :href "/#" } "CASINO"]]
     [:div { :class "pull-right" }
      [:div (str "Score: " points)]
      [:div#points-sparkline]]]))

(defn debug-panel [] 
  (let [closed (r/atom false)]
    (fn [] 
      [:div#debug 
       (when-not @closed [:div#app-state (str @blackjack.core/app-state)])
       [:a {:on-click #(swap! closed not)} (if @closed "Open" "Close")]])))

(defn page []
  (let [this (r/current-component)
        children (r/children this)]
    [:div#content
     [header]
     (into [:div.container] children)
     [debug-panel]]))
