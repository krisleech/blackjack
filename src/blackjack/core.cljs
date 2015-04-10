;; always reload this file
(ns ^:figwheel-always blackjack.core
  (:require [reagent.core :as r]
            [secretary.core :as secretary :refer-macros [defroute]]
            [goog.events :as events]
            [blackjack.lobby :as lobby]
            [blackjack.high-low :as high-low]
            [goog.history.EventType :as EventType])
  (:import goog.History))

(enable-console-print!)

;; initial app state
(defonce app-state (r/atom { :name "" :points 1000 }))

;; Routes

; helper to render an entire page
(defn render-page [page] (r/render [page] (js/document.getElementById "app")))

(defroute "/" []
  (render-page lobby/page))

(defroute "/high-low-game" []
  (render-page high-low/page))

;; Dispatch routes based on URL
(let [h (History.)]
  (goog.events/listen h EventType/NAVIGATE #(println (.-token %)))
  (goog.events/listen h EventType/NAVIGATE #(secretary/dispatch! (.-token %)))
  (doto h (.setEnabled true)))

;; APP
(defn init! [] (do (high-low/initialize)
                   (render-page lobby/page)))

(init!)
