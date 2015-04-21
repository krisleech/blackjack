(ns blackjack.high-low
  (:require [reagent.core :as r]
            [blackjack.ui :as ui]))

(defn game-state [key] (get-in @blackjack.core/app-state [:high-low key]))

(defn won? []
  (let [now  (game-state :dice)
        then (game-state :previous_dice)
        bet  (game-state :bet)]
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

;; Better to have an animated GIF for showing dice rolling?
(defn roll-dice []
  (do
    (swap! blackjack.core/app-state assoc-in [:high-low :previous_dice] (game-state :dice))
    (let [timer (js/setInterval #(swap! blackjack.core/app-state assoc-in [:high-low :dice] (+ (rand-int 6) 1)) 60)]
      (js/setTimeout #(do (js/clearInterval timer) (calc-winner)) 1000))))

(defn make-bet [choice]
  (swap! blackjack.core/app-state assoc-in [:high-low :bet] choice)
  (roll-dice))

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
  (let [bet       (game-state :bet)
        dice_then (game-state :previous_dice)]

  [:div.bet (if (not (nil? bet))
    [:div (str "Bet: " bet " than ")
      [dice { :tag :span :width "20px" :number dice_then }]])]))

(defn player [] (let
                  [name      (:name @blackjack.core/app-state)
                   points    (:points @blackjack.core/app-state)
                   winner    (game-state :winner)
                   dice_then (game-state :previous_dice)
                   dice_now  (game-state :dice)]

                  [:div.player
                    [:div.name (str "Name: " name)]
                    [:div.points (str "Points: " points)]
                    [bet]
                    [:div.winner (if (not (nil? winner)) (str "Winner: " winner))]
                    [dice { :number dice_now }]
                    [:p { :class "bet-buttons" }
                      [ui/button {:label "Higher" :on-click #(make-bet "higher")}]
                      [ui/button {:label "Lower" :on-click #(make-bet "lower")}]]]))

(defn header []
  [:div.header 
    [:a { :href "/#" } "Lobby"]])

;; PAGE

(defn page [] [:div [header] [player]])

;; Initalize

(defn initialize [] (do 
                      (swap! blackjack.core/app-state assoc :high-low { :previous_dice nil :dice nil :bet nil :winner nil })
                      (roll-dice)))
