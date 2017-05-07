(ns geocaching-adventure.home
  (:require [advenjure.items :as item]
            [advenjure.rooms :as room]
            [advenjure.utils :as utils]))

(defn after-smart-phone-read [oldgs gs] 
  (update-in gs [:events] conj :smart-phone-read))

(def smart-phone (item/make ["smart phone" "phone" "notification" "phone notification"] 
                            (str "A fully-charged smart phone, ready for action. "
                                 "There's a new notification on the screen that you may wish to read.")
                            :take true
                            :read {:say "NEW NOTIFICATION: A multi-cache has just been published!"
                                   :post `after-smart-phone-read}))

(def gpsr (item/make ["GPSr" "gpsr" "Garmin" "garmin"] 
                     "It's a well used Garmin."
                     :take true
                     ;:read "It displays the current coordinates." ;TODO?
                     ))

(def pen (item/make ["pen"] 
                    "An ordinary, nondescript pen."
                     :take true))

(def table (item/make ["small table" "table"] 
                      "A small table."
                      :items #{gpsr smart-phone pen}))

(def home (-> (room/make "Home"
                         "At home."
                         :initial-description 
                         (str "You're at home just puttering around, but thinking about what your next adventure might be. "
                              "Perhaps you should get outside for some fresh air. But where would you go? "
                              "Your front door faces north."))
              (room/add-item table)))

