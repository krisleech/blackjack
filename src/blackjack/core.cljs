;; always reload this file
(ns ^:figwheel-always blackjack.core
  (:require [reagent.core :as r]))

(enable-console-print!)

;; initial app state
(defonce app-state (atom {}))

;; components
(defn home-page [] [:div "Homepage"])

;; initialize app
(r/render [home-page] (js/document.getElementById "app"))
