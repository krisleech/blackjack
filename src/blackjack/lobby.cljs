(ns blackjack.lobby
  (:require [reagent.core :as r]
            [blackjack.debug-panel :as debug-panel]))

(defn header [] 
  (let
    [points (:points @blackjack.core/app-state)]
    [:div { :class "container" :id "header" }
     [:p { :class "pull-left" } [:a { :href "/#" } "CASINO"]]
     [:p { :class "pull-right" } (str "Score: " points)]]))

(defn page []
  [:div { :id "app" }
   [header]
   [:div { :id "content" :class "container" }
    [:p "Choose a game"]
    [:ol
     [:li [:a { :href "/#high-low-game" } "High Low"]]]]
   [debug-panel/widget]])
