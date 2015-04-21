(ns blackjack.high-low
  (:require [reagent.core :as r]
            [blackjack.ui :as ui]))

(defn won? []
  (let [now  (get-in @blackjack.core/app-state [:high-low :dice])
        then (get-in @blackjack.core/app-state [:high-low :previous_dice ])
        bet  (get-in @blackjack.core/app-state [:high-low :bet])]
    (or (and (> now then) (= bet "higher")) (and (< now then) (= bet "lower")))))

;; Actions

(defn calc-winner []
    (if (won?)
      (do 
        (swap! blackjack.core/app-state assoc-in [:high-low :winner] "You") 
        (swap! blackjack.core/app-state assoc :points (+ (:points @blackjack.core/app-state) 10)))
      (do 
        (swap! blackjack.core/app-state assoc-in [:high-low :winner] "Me")
        (swap! blackjack.core/app-state assoc :points (- (:points @blackjack.core/app-state) 10)))))

(defn roll-dice []
  (let [new_number (+ (rand-int 6) 1)]
    (swap! blackjack.core/app-state assoc-in [:high-low :previous_dice] (get-in @blackjack.core/app-state [:high-low :dice])) ; can we make two swaps atomic?
    (swap! blackjack.core/app-state assoc-in [:high-low :dice] new_number)))

(defn bet-higher [] 
  (swap! blackjack.core/app-state assoc-in [:high-low :bet] "higher")
  (roll-dice)
  (calc-winner))

(defn bet-lower []
  (swap! blackjack.core/app-state assoc-in [:high-low :bet] "lower")
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
  (let [bet       (get-in @blackjack.core/app-state [:high-low :bet])
        dice_then (get-in @blackjack.core/app-state [:high-low :previous_dice])]

  [:div.bet (if (not (nil? bet))
    [:div (str "Bet: " bet " than ")
      [dice { :tag :span :width "20px" :number dice_then }]])]))

(defn player [] (let
                  [name      (get-in @blackjack.core/app-state [:high-low :name])
                   points    (:points @blackjack.core/app-state)
                   winner    (get-in @blackjack.core/app-state [:high-low :winner])
                   dice_then (get-in @blackjack.core/app-state [:high-low :previous_dice])
                   dice_now  (get-in @blackjack.core/app-state [:high-low :dice])]

                  [:div.player
                    [:div.name (str "Name: " name)]
                    [:div.points (str "Points: " points)]
                    [bet]
                    [:div.winner (if (not (nil? winner)) (str "Winner: " winner))]
                    [dice { :number dice_now }]
                    [:p { :class "bet-buttons" }
                      [ui/button {:label "Higher" :on-click #(bet-higher)}]
                      [ui/button {:label "Lower" :on-click #(bet-lower)}]]]))

(defn header []
  [:div.header 
    [:a { :href "/#" } "Lobby"]])

;; PAGE

(defn page [] [:div [header] [player]])

;; Initalize

(defn initialize [] (do 
                      (swap! blackjack.core/app-state assoc :high-low { :previous_dice nil :dice nil :bet nil :winner nil })
                      (roll-dice)))
