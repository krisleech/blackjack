(ns blackjack.lobby
  (:require [blackjack.db :as db]))

;; Lobby

(defn header []
  [:div.header 
    [:a { :href "/#" } "Lobby"]
    [:span " /  "]
    [:a { :href "/#high-low-game" } "High Low"]])

;; PAGES

(defn page []
  (let
    [points (:points @db/app-state)]
  [:div { :id "game" }
    [:h1 "Casino"]
    [:p (str "Points: " points)]
    [:p "Choose a game"]
    [:a { :href "/#high-low-game" } "High Low"]]))
