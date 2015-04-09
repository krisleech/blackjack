;; always reload this file
(ns ^:figwheel-always blackjack.core
  (:require [reagent.core :as r]))

(enable-console-print!)

;; initial app state
(defonce app-state (atom { :name "" :points 1000 :dice nil}))

;; components

;; GENERIC

(defn button [] 
  (let [this (r/current-component)
        props (r/props this)]
  [:button { :class "btn" } (:label props)]))

;; DOMAIN

(defn dice [] 
  [:div.dice (:dice @app-state)])

(defn player [] (let
                 [name (:name @app-state)
                  points (:points @app-state)]

                  [:div.player
                   [:div.name (str "Name: " name)]
                   [:div.points (str "Points: " points)]
                   [dice]
                   [button {:label "Higher"}]
                   [button {:label "Lower"}]
                   ]))

(defn home-page [] [:div [player]])

;; Actions

(defn roll-dice [] (swap! app-state assoc :dice 4))


;; initialize app
(defn init! [] (
  (roll-dice)
  (r/render [home-page] (js/document.getElementById "app"))
                ))

(init!)
