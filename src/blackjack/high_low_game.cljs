(ns blackjack.high-low
  (:require [reagent.core :as r]
            [blackjack.db :as db]
            [blackjack.compontents :as ui]))

;; High Low Dice Game

;; Queries

(defn won? []
  (let [now  (:dice @)
        then (:previous_dice @db/app-state)
        bet  (:bet @db/app-state)]
    (or (and (> now then) (= bet "higher")) (and (< now then) (= bet "lower")))))

;; Actions

(defn calc-winner []
    (if (won?)
      ((swap! @db/app-state assoc :winner "You") 
       (swap! @db/app-state assoc :points (+ (:points @db/app-state) 10)))
      ((swap! @db/app-state assoc :winner "Me")
      (swap! @db/app-state assoc :points (- (:points @db/app-state) 10)))
      ))

(defn roll-dice []
  (let [new_number (+ (rand-int 6) 1)]
    (swap! @db/app-state assoc :previous_dice (:dice @db/app-state)) ; can we make two swaps atomic?
    (swap! @db/app-state assoc :dice new_number)))

(defn bet-higher [] 
  (swap! @db/app-state assoc :bet "higher")
  (roll-dice)
  (calc-winner))

(defn bet-lower []
  (swap! @db/app-state assoc :bet "lower")
  (roll-dice)
  (calc-winner))

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
  (let [bet       (:bet           @db/app-state)
        dice_then (:previous_dice @db/app-state)]

  [:div.bet (if (not (nil? bet))
    [:div (str "Bet: " bet " than ")
      [dice { :tag :span :width "20px" :number dice_then }]])]))

(defn player [] (let
                  [name      (:name @db/app-state)
                   points    (:points @db/app-state)
                   winner    (:winner @db/app-state)
                   dice_then (:previous_dice @db/app-state)
                   dice_now  (:dice @db/app-state)]

                  [:div.player
                    [:div.name (str "Name: " name)]
                    [:div.points (str "Points: " points)]
                    [bet]
                    [:div.winner (if (not (nil? winner)) (str "Winner: " winner))]
                    [dice { :number dice_now }]
                    [ui/button {:label "Higher" :on-click #(bet-higher)}]
                    [ui/button {:label "Lower" :on-click #(bet-lower)}]]))

(defn game [] [:div [player]])
