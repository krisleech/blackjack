;; always reload this file
(ns ^:figwheel-always blackjack.core
  (:require [reagent.core :as r]))

(enable-console-print!)

;; initial app state
(defonce app-state (r/atom { :name "" :points 1000 :dice nil}))

;; Actions

(defn roll-dice [] 
  (let [new_number (+ (rand-int 6) 1)]
    (swap! app-state assoc :dice new_number)))

;; components

;; GENERIC

(def button-defaults { :class 'btn' })

(defn button [] 
  (let [this (r/current-component)
        props (merge button-defaults (r/props this))]
  [:button props (:label props)]))

;; DOMAIN

(defn dice [] 
  (let [number (:dice @app-state)]
  [:div.dice number]))

(defn player [] (let
                 [name (:name @app-state)
                  points (:points @app-state)]

                  [:div.player
                   [:div.name (str "Name: " name)]
                   [:div.points (str "Points: " points)]
                   [dice]
                   [button {:label "Higher" :on-click #(roll-dice)}]
                   [button {:label "Lower" :on-click #(roll-dice)}]
                   ]))

;; PAGES

(defn home-page [] [:div [player]])


;; APP
(defn init! [] (
  (roll-dice)
  (r/render [home-page] (js/document.getElementById "app"))
                ))

(init!)
