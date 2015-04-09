;; always reload this file
(ns ^:figwheel-always blackjack.core
  (:require [reagent.core :as r]))

(enable-console-print!)

;; initial app state
(defonce app-state (r/atom { :name "" :points 1000 :previous_dice nil :dice nil :bet nil :winner nil }))

;; Actions

(defn calc-winner []
  (let [now (:dice @app-state)
        then (:previous_dice @app-state)
        bet (:bet @app-state)]
    (if (or (and (> now then) (= bet "higher")) (and (< now then) (= bet "lower")))
      (swap! app-state assoc :winner "You")
      (swap! app-state assoc :winner "Me")
      )
    ))

(defn roll-dice [] 
  (let [new_number (+ (rand-int 6) 1)]
    (swap! app-state assoc :previous_dice (:dice @app-state)) ; can we make two swaps atomic?
    (swap! app-state assoc :dice new_number)
    (calc-winner)))

(defn bet-higher [] 
  (swap! app-state assoc :bet "higher")
  (roll-dice))

(defn bet-lower []
  (swap! app-state assoc :bet "lower")
  (roll-dice))

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
                  points (:points @app-state)
                  winner (:winner @app-state)
                  bet (:bet @app-state)]

                  [:div.player
                   [:div.name (str "Name: " name)]
                   [:div.points (str "Points: " points)]
                   [:div.bet (str "Bet " bet)]
                   [:div.winner (if (not (nil? winner)) (str "Winner: " winner))]
                   [dice]
                   [button {:label "Higher" :on-click #(bet-higher)}]
                   [button {:label "Lower" :on-click #(bet-lower)}]
                   ]))

;; PAGES

(defn home-page [] [:div [player]])


;; APP
(defn init! [] (
  (roll-dice)
  (r/render [home-page] (js/document.getElementById "app"))
                ))

(init!)
