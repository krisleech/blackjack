(ns blackjack.high-low
  (:require [reagent.core :as r]
            [blackjack.ui :as ui]))

(defn won? []
  (let [now  (:dice @blackjack.core/app-state)
        then (:previous_dice @blackjack.core/app-state)
        bet  (:bet @blackjack.core/app-state)]
    (or (and (> now then) (= bet "higher")) (and (< now then) (= bet "lower")))))

;; Actions

(defn calc-winner []
    (if (won?)
      (do 
        (swap! blackjack.core/app-state assoc :winner "You") 
        (swap! blackjack.core/app-state assoc :points (+ (:points @blackjack.core/app-state) 10)))
      (do 
        (swap! blackjack.core/app-state assoc :winner "Me")
        (swap! blackjack.core/app-state assoc :points (- (:points @blackjack.core/app-state) 10)))))

(defn roll-dice []
  (let [new_number (+ (rand-int 6) 1)]
    (swap! blackjack.core/app-state assoc :previous_dice (:dice @blackjack.core/app-state)) ; can we make two swaps atomic?
    (swap! blackjack.core/app-state assoc :dice new_number)))

(defn bet-higher [] 
  (swap! blackjack.core/app-state assoc :bet "higher")
  (roll-dice)
  (calc-winner))

(defn bet-lower []
  (swap! blackjack.core/app-state assoc :bet "lower")
  (roll-dice)
  (calc-winner))

;; components

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
  (let [bet       (:bet           @blackjack.core/app-state)
        dice_then (:previous_dice @blackjack.core/app-state)]

  [:div.bet (if (not (nil? bet))
    [:div (str "Bet: " bet " than ")
      [dice { :tag :span :width "20px" :number dice_then }]])]))

(defn player [] (let
                  [name      (:name @blackjack.core/app-state)
                   points    (:points @blackjack.core/app-state)
                   winner    (:winner @blackjack.core/app-state)
                   dice_then (:previous_dice @blackjack.core/app-state)
                   dice_now  (:dice @blackjack.core/app-state)]

                  [:div.player
                    [:div.name (str "Name: " name)]
                    [:div.points (str "Points: " points)]
                    [bet]
                    [:div.winner (if (not (nil? winner)) (str "Winner: " winner))]
                    [dice { :number dice_now }]
                    [ui/button {:label "Higher" :on-click #(bet-higher)}]
                    [ui/button {:label "Lower" :on-click #(bet-lower)}]]))

(defn header []
  [:div.header 
    [:a { :href "/#" } "Lobby"]
    [:a { :href "/#high-low-game" } "High Low"]])

;; PAGE

(defn page [] [:div [header] [player]])

;; Initalize

(defn initialize [] (do 
                      (swap! blackjack.core/app-state assoc :previous_dice nil)
                      (swap! blackjack.core/app-state assoc :dice nil)
                      (swap! blackjack.core/app-state assoc :bet nil)
                      (swap! blackjack.core/app-state assoc :winner nil)
                      (roll-dice)))
