(ns blackjack.debug-panel
  (:require [reagent.core :as r]))

(defn widget [] [:div { :id "debug" } (str @blackjack.core/app-state)])
