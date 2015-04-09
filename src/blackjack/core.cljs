;; always reload this file
(ns ^:figwheel-always blackjack.core
  (:require [reagent.core :as r]))

(enable-console-print!)

;; initial app state
(defonce app-state (atom { :name "" :points 1000 }))

;; components

(defn dice [] 
  [:div.dice "Dice"])

(defn player [] (let
                 [name (:name @app-state)
                  points (:points @app-state)]

                  [:div.player
                   [:div.name (str "Name: " name)]
                   [:div.points (str "Points: " points)]
                   [dice]
                   [:button "Roll"]]))

(defn home-page [] [:div [player]])


;; initialize app
(r/render [home-page] (js/document.getElementById "app"))
