(ns blackjack.db
  (:require [reagent.core :as r]))

;; initial app state
(defonce app-state (r/atom { :name "" :points 1000 :previous_dice nil :dice nil :bet nil :winner nil }))
