(ns blackjack.lobby
  (:require [reagent.core :as r]
            [blackjack.layout :as layout]))

(defn page []
  [layout/page [:div
            [:div { :id "content" :class "container" }
             [:p "Choose a game"]
             [:ol
              [:li [:a { :href "/#high-low-game" } "High Low"]]]]] ])
