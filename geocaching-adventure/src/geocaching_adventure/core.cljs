(ns geocaching-adventure.core
  (:require [advenjure.game :as game]
            [geocaching-adventure.room-map :refer [room-map]]
            [geocaching-adventure.verbs :refer [verb-map]]))

(enable-console-print!)

(let [game-state (game/make room-map :home)
      finished? #(= (:current-room %) :stage-5)] ;don't ever finish - there is no stage 5
  (game/run game-state finished? 
    :start-message (str "Welcome to the your geocaching adventure!\n"
                        "Please type 'help' if you don't know what to do.\n") 
    :verb-map verb-map))
