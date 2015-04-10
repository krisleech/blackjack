(ns blackjack.lobby
  (:require [reagent.core :as r]))

(defn page []
  (let
    [points (:points @blackjack.core/app-state)]
  [:div
    [:h1 "Casino"]
    [:p (str "Points: " points)]
    [:p "Choose a game"]
    [:a { :href "/#high-low-game" } "High Low"]]))

