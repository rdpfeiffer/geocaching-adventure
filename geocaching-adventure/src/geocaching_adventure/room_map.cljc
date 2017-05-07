(ns geocaching-adventure.room-map
  (:require [advenjure.rooms :as room]
            [advenjure.utils :as utils]
            [geocaching-adventure.home     :refer [home gpsr pen]]
            [geocaching-adventure.stage-1  :refer [stage-1]]
            [geocaching-adventure.stage-2  :refer [stage-2]]
            [geocaching-adventure.stage-3a :refer [stage-3a tree-top]]
            [geocaching-adventure.stage-3b :refer [rest-area stage-3b]]
            [geocaching-adventure.stage-4  :refer [stage-4]]))

;; Some conditions to leave "rooms".

(def no-coords-response "But where would you go?")

(defn can-start? [gs]
  (cond (not ((:events gs) :smart-phone-read)) no-coords-response
        (not (contains? (:inventory gs) gpsr)) "Surely you don't want to leave without your GPSr!"
        (not (contains? (:inventory gs) pen))  "Won't you need something to write with?"
        :else :stage-1))

(defn stage-2-coords? [gs]
  (if (not ((:events gs) :stage-2-coords))
    no-coords-response
    :stage-2))

(defn stage-3a-coords? [gs]
  (if (not ((:events gs) :stage-3-coords)) 
    no-coords-response
    :stage-3a))

(defn stage-3b-coords? [gs]
  (if (not ((:events gs) :stage-3-coords)) 
    no-coords-response
    :rest-area))

(defn stage-4-coords? [gs]
  (if (not ((:events gs) :stage-4-coords)) 
    no-coords-response
    :stage-4))

;; Define the "room" map, and then set the connections between the "rooms".
(def room-map (-> {:home      home
                   :stage-1   stage-1
                   :stage-2   stage-2
                   :stage-3a  stage-3a
                   :tree-top  tree-top
                   :rest-area rest-area
                   :stage-3b  stage-3b
                   :stage-4   stage-4}
                  (room/one-way-connect :home      :north `can-start?)
                  (room/one-way-connect :stage-1   :west  `stage-2-coords?)
                  (room/one-way-connect :stage-2   :north `stage-3a-coords?)
                  (room/one-way-connect :stage-2   :west  `stage-3b-coords?)
                  (room/one-way-connect :stage-2   :east  :stage-1)
                  (room/one-way-connect :stage-3a  :south :stage-2)
                  (room/connect         :stage-3a  :up    :tree-top)
                  (room/connect         :rest-area :north :stage-3b)
                  (room/one-way-connect :rest-area :east  :stage-2)
                  (room/one-way-connect :stage-3b  :north `stage-4-coords?)
                  (room/one-way-connect :stage-4   :south :stage-3b)))
