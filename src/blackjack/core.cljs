;; always reload this file
(ns ^:figwheel-always blackjack.core
  (:require [reagent.core :as r]
            [secretary.core :as secretary :refer-macros [defroute]]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
  (:import goog.History))

(enable-console-print!)

;; initial app state
(defonce app-state (r/atom { :name "" :points 1000 :previous_dice nil :dice nil :bet nil :winner nil }))

;; Queries

(defn won? []
  (let [now  (:dice @app-state)
        then (:previous_dice @app-state)
        bet  (:bet @app-state)]
    (or (and (> now then) (= bet "higher")) (and (< now then) (= bet "lower")))))

;; Actions

(defn calc-winner []
    (if (won?)
      ((swap! app-state assoc :winner "You") 
       (swap! app-state assoc :points (+ (:points @app-state) 10)))
      ((swap! app-state assoc :winner "Me")
      (swap! app-state assoc :points (- (:points @app-state) 10)))
      ))

(defn roll-dice []
  (let [new_number (+ (rand-int 6) 1)]
    (swap! app-state assoc :previous_dice (:dice @app-state)) ; can we make two swaps atomic?
    (swap! app-state assoc :dice new_number)))

(defn bet-higher [] 
  (swap! app-state assoc :bet "higher")
  (roll-dice)
  (calc-winner))

(defn bet-lower []
  (swap! app-state assoc :bet "lower")
  (roll-dice)
  (calc-winner))

;; components

;; GENERIC

(def button-defaults { :class 'btn' })

(defn button [] 
  (let [this (r/current-component)
        props (merge button-defaults (r/props this))]
  [:button props (:label props)]))

;; DOMAIN

(def dice-defaults { :width "100px"
                     :tag   :div })

(defn dice []
  (let [this   (r/current-component)
        props  (merge dice-defaults (r/props this))
        number (:number props)
        tag    (:tag    props)
        width  (:width  props)]

  [tag [:image { :src (str "images/die" number ".svg") :style { :width width } :alt number }]]))

(defn bet []
  (let [bet       (:bet           @app-state)
        dice_then (:previous_dice @app-state)]

  [:div.bet (if (not (nil? bet))
    [:div (str "Bet: " bet " than ")
      [dice { :tag :span :width "20px" :number dice_then }]])]))

(defn player [] (let
                  [name      (:name @app-state)
                   points    (:points @app-state)
                   winner    (:winner @app-state)
                   dice_then (:previous_dice @app-state)
                   dice_now  (:dice @app-state)]

                  [:div.player
                    [:div.name (str "Name: " name)]
                    [:div.points (str "Points: " points)]
                    [bet]
                    [:div.winner (if (not (nil? winner)) (str "Winner: " winner))]
                    [dice { :number dice_now }]
                    [button {:label "Higher" :on-click #(bet-higher)}]
                    [button {:label "Lower" :on-click #(bet-lower)}]]))

(defn header []
  [:div.header 
    [:a { :href "/#" } "Lobby"]
    [:a { :href "/#high-low-game" } "High Low"]])

;; PAGES

(defn home-page [] 
  (let
    [points (:points @app-state)]
  [:div
    [:h1 "Casino"]
    [:p (str "Points: " points)]
    [:p "Choose a game"]
    [:a { :href "/#high-low-game" } "High Low"]]))

(defn high-low-game [] [:div [header] [player]])

;; Routes

; help to render an entire page
(defn render-page [page] (r/render [page] (js/document.getElementById "app")))

(defroute "/" []
  (render-page home-page))

(defroute "/high-low-game" []
  (render-page high-low-game))

;; Dispatch routes based on URL
(let [h (History.)]
  (goog.events/listen h EventType/NAVIGATE #(println (.-token %)))
  (goog.events/listen h EventType/NAVIGATE #(secretary/dispatch! (.-token %)))
  (doto h (.setEnabled true)))


;; APP
(defn init! [] (
  (roll-dice)
  (render-page home-page)))

(init!)
