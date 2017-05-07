(ns geocaching-adventure.stage-3a
  (:require [advenjure.items :as item]
            [advenjure.rooms :as room]
            [advenjure.utils :as utils]
            [advenjure.change-rooms :refer [change-rooms]]
            [geocaching-adventure.support :refer [disentangle gordian-knot-1]]))

(def card (item/make ["card"]
                      "It has writing on it."
                      :read (str "\"" (disentangle gordian-knot-1) "\"")))

(def egg (item/make ["egg"]
                    "It's a geocache!"
                    :closed true
                    :items #{card}))

(def nest (item/make ["bird's nest" "birds nest" "nest"]
                     "It's a very clean bird's nest that looks rather new."
                     :items #{egg}))

(defn go-tree-top  [old-gs newgs] (change-rooms newgs :tree-top))
(defn go-tree-base [old-gs newgs] (change-rooms newgs :stage-3a))

(def tree (item/make ["tree" "oak" "oak tree"]
                     "It sure is a mighty oak! And it looks like it would be an easy climb."
                     :use {:post `go-tree-top}
                     :climb-up :tree-top))

(def stage-3a (-> (room/make "Base of oak tree"
                             "An ancient oak tree."
                             :initial-description 
                             (str "You head off once again. "
                                  "The path winds its way through a wonderful meadow blooming with wildflowers. "
                                  "You see a vast field of yellow, red, purple, orange, pink and white, all intermingled. "
                                  "The fragrance is intoxicating! "
                                  "You almost want to stop and just enjoy the beautiful surroundings. "
                                  "It sure would be a lovely spot for a picnic. "
                                  "But there's business to attend to, so you keep moving. "
                                  "Eventually, you arrive at the coordinates. "
                                  "This time you zero out at a majestic oak tree."))
                  (room/add-item tree "")))

(def tree-bough (item/make ["tree bough"]
                           "It sure is a mighty oak! And it looks like it would be an easy climb."
                           :use {:post `go-tree-base}
                           :climb-down :stage-3a))

(def tree-top (-> (room/make "Oak tree top"
                             "The top of the oak tree."
                             :initial-description 
                             (str "The sturdy limbs are perfectly arranged. "
                                  "It seems like this ancient oak was just made for climbing! "
                                  "You go up and up, until you are as high as you can get. "
                                  "There's a bird's nest here."))
                  (room/add-item nest "")
                  (room/add-item tree-bough "")))

