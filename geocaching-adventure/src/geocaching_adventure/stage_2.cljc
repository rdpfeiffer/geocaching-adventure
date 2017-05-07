(ns geocaching-adventure.stage-2
  (:require [advenjure.items :as item]
            [advenjure.rooms :as room]
            [advenjure.utils :as utils]))

(defn after-note-read [old-gs gs] 
  (update-in gs [:events] conj :stage-3-coords))

(def note (item/make ["note"]
                     "It has writing on it."
                     :read {:say (str "\"CONGRATULATIONS ON FINDING STAGE 2!\"\n"
                                      "The information on the note is a bit surprising. "
                                      "Instead of just one, there are two sets of coordinates. "
                                      "They are labeled \"north\" and \"west\".")
                            :post `after-note-read}))

(def tupperware (item/make ["tupperware" "tupperware container" "container" "box" "cache" "geocache"]
                           "It's a geocache."
                           :read "Official Geocache"
                           :closed true
                           :items #{note}))

(def spor (item/make ["rocks" "rock" "pile" "rock pile" "pile of rocks" 
                      "suspicious pile of rocks" "suspicious rock pile" "spor" "srp"]
                     "Hmmm, that pile of rocks sure does look mighty suspicious."
                     :move "Moving the rocks reveals a tupperware container."
                     :lift "Lifting the rocks reveals a tupperware container."
                     :items #{tupperware}))

(def bush (item/make ["lemonade berry bush" "lemonadeberry bush" "bush" "rhus integrifolia"]
                     "This bush is thriving! You notice a suspicious pile of rocks underneath."
                     :climb "You can't climb a bush!"))

(def stage-2 (-> (room/make "Stage 2"
                            "Second waypoint."
                            :initial-description 
                            (str "You head over to the trailhead and begin a nice stroll along a lovely trail "
                                 "through an extensive patch of manzanita. It's in full bloom! "
                                 "And, by the way, did you know that the berries and flowers of most species are edible? "
                                 "After a short while, you arrive at the coordinates for stage 2. "
                                 "You zero out right at a nice big specimen of Rhus integrifolia - "))
                 (room/add-item bush "A lemonade berry bush.")
                 (room/add-item spor "")))

