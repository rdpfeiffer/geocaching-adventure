(ns geocaching-adventure.stage-1
  (:require [advenjure.items :as item]
            [advenjure.rooms :as room]
            [advenjure.utils :as utils]))

(def lamp-post (item/make ["lamp post" "lamppost" "post"]
                          (str "It's an ordinary lamp post with a sign stating, \"No Overnight Parking\". "
                               "There's a metal skirt at the base.")
                          :read "There's some illegible graffiti scrawled on it."
                          :move "Are you kidding?"
                          :pull "It doesn't budge."
                          :push "It doesn't budge."
                          :climb "That's too hard!"))

(def lamp-post-skirt (item/make ["skirt" "lamp post skirt" "lamppost skirt" "post skirt" "metal skirt"]
                                "The lamp post skirt looks like it's unfastened, and can be lifted."
                                :lift (str "You look underneath the lamp post skirt. "
                                           "You find nothing but some spider webs and filthy gunk. Yuck!")
                                :move (str "You look underneath the lamp post skirt. "
                                           "You find nothing but some spider webs and filthy gunk. Yuck!")))

(def sign (item/make ["sign" "lamp post sign" "lamppost sign" "post sign"]
                     "Neatly tucked up behind the sign, you notice a tiny black speck."
                     :read "No Overnight Parking"
                     :move "The sign is securely fastened in place."
                     :pull "The sign is securely fastened in place."
                     :push "The sign is securely fastened in place."))

(defn after-nano-paper-read [old-gs gs] 
  (update-in gs [:events] conj :stage-2-coords))

(def paper (item/make ["tiny rolled up slip of paper" "slip" "paper" "slip of paper" "piece of paper"]
                      "It has writing on it."
                      :read {:say (str "\"CONGRATULATIONS ON FINDING STAGE 1!\"\n"
                                       "The coordinates for stage 2, which lies along the trail to the west, are provided.")
                             :post `after-nano-paper-read}))

(def nano (item/make ["nano" "nano cache" "speck" "black speck" "cache" "geocache"]
                     "It's a nano cache!"
                     :closed true
                     :items #{paper}))

(def stage-1 (-> (room/make "Stage 1"
                            "Initial waypoint."
                            :initial-description 
                            (str "You race out the door and hop into your cachemobile. "
                                 "Obeying all traffic laws, you arrive at the first waypoint in record time. "
                                 "You exit your vehicle and walk over to the location where your GPSr zeroes out."))
                 (room/add-item lamp-post "It's a lamp post.")
                 (room/add-item lamp-post-skirt "")
                 (room/add-item sign "")
                 (room/add-item nano "")))
