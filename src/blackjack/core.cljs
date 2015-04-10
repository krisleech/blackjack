;; always reload this file
(ns ^:figwheel-always blackjack.core
  (:require [reagent.core :as r]
            [secretary.core :as secretary :refer-macros [defroute]]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [blackjack.lobby :as lobby]
            [blackjack.high-low :as high-low])
  (:import goog.History))

(enable-console-print!)

;; Routes

(defn render-page [page] (r/render [page] (js/document.getElementById "app")))
(defn render-game [game] (r/render [game] (js/document.getElementById "game")))

(defroute "/" []
  (render-page lobby/page))

(defroute "/high-low-game" []
  (render-game high-low/game))

;; Dispatch routes based on URL
(let [h (History.)]
  (goog.events/listen h EventType/NAVIGATE #(println (.-token %)))
  (goog.events/listen h EventType/NAVIGATE #(secretary/dispatch! (.-token %)))
  (doto h (.setEnabled true)))

;; APP
(defn init! [] (
  (render-page lobby/page)))

(init!)
