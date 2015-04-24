(ns blackjack.high-low
  (:require [reagent.core :as r]
            [blackjack.layout :as layout]
            [blackjack.ui :as ui]))

;; State 
(def db-key :high-low)
(defn db-get [key] (get-in @blackjack.core/app-state [db-key key]))
(defn db-put[key value] (swap! blackjack.core/app-state assoc-in [db-key key] value))

;; Queries

(defn won? []
  (let [now  (db-get :dice)
        then (db-get :previous_dice)
        bet  (db-get :bet)]
    (or (and (> now then) (= bet "higher")) (and (< now then) (= bet "lower")))))

;; Actions

(defn calc-winner []
  (if (won?)
    (do
      (db-put :winner "You")
      (swap! blackjack.core/app-state assoc :points (+ (:points @blackjack.core/app-state) 10)))
    (do
      (db-put :winner "Me")
      (swap! blackjack.core/app-state assoc :points (- (:points @blackjack.core/app-state) 10)))))

;; Better to have an animated GIF for showing dice rolling?
(defn roll-dice []
  (do
    (db-put :previous_dice (db-get :dice))
    (let [timer (js/setInterval #(db-put :dice (+ (rand-int 6) 1)) 60)]
      (js/setTimeout #(do (js/clearInterval timer) (calc-winner)) 1000))))

(defn make-bet [choice]
  (db-put :bet choice)
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
  (let [bet       (db-get :bet)
        dice_then (db-get :previous_dice)]

  [:div.bet (if (not (nil? bet))
    [:div (str "Bet: " bet " than ")
      [dice { :tag :span :width "20px" :number dice_then }]])]))

(defn player [] (let
                  [winner    (db-get :winner)
                   dice_then (db-get :previous_dice)
                   dice_now  (db-get :dice)]

                  [:div.player
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

(defn page []
  [layout/page [player]])

;; Initalize

(defn initialize [] (do 
                      (swap! blackjack.core/app-state assoc db-key { :previous_dice nil :dice nil :bet nil :winner nil })
                      (roll-dice)))
