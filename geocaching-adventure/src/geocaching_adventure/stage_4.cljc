(ns geocaching-adventure.stage-4
  (:require [advenjure.items :as item]
            [advenjure.rooms :as room]
            [advenjure.utils :as utils]
            [geocaching-adventure.support :refer [disentangle gordian-knot-3 gordian-knot-4]]))

(def trinkets (item/make ["bunch of trinkets" "trinkets" "knickknacks" "baubles" "curios"
                          "trifles" "gimcracks" "gewgaws" "bibelots" "novelties"]
                         (str "There are knickknacks, baubles, curios, trifles, gimcracks, gewgaws, bibelots, "
                              "and various other novelties.")
                         :take true))

(def dog-tag (item/make ["dog tag" "tag"]
                        "The dog tag has the customary tracking code stamped on it."
                        :read (str "\"" (disentangle gordian-knot-3) "\"")))

(def code (item/make ["tracking code" "code"]
                     "It's a tracking code."
                     :read (str "\"" (disentangle gordian-knot-3) "\"")))

(def travel-bug (item/make ["travel bug" "bug" "tb"]
                           (str "It's a special \"Online Adventurer\" travel bug! "
                                "And, of course, it has the usual dog tag attached to it.")
                           :take true))

(def plush-toy (item/make ["plush toy" "toy" "signal" "signal the frog" "frog"]
                          "It's Signal the Frog!"
                          :take true))

(def writing (item/make ["writing" "tiny writing" "small writing" "label writing" "tiny letters" "small letters"]
                        "The writing is very tiny."
                        :read "Another adventure awaits at N 34... Happy Caching!"))

(defn after-log-signed [old-gs gs] 
  (let [stage-4 (utils/current-room gs)
        new-stage-4 (-> stage-4
                        (room/add-item writing))]
    (assoc-in gs [:room-map :stage-4] new-stage-4)))

;; (defn after-log-revealed [old-gs gs] 
;;   (update-in gs [:events] conj :log-revealed))

(defn remove-label [old-gs gs] 
  (if (contains? (:events gs) :log-revealed)
    (utils/say "It has alread been removed.")
    (do
      (utils/say (str "The label comes right off. And, on the inner surface... it's the geocache log! "
                      "The log is completely blank except for the letters \"FTF\" on the top line, "
                      "awaiting its first signature."))
                 (update-in gs [:events] conj :log-revealed))))

(defn sign-log [old-gs gs] 
  (if (not (contains? (:events gs) :log-revealed))
    (utils/say "Sign what?")
    (let [stage-4 (utils/current-room gs)
          new-stage-4 (-> stage-4
                          (room/add-item writing ""))]
      (utils/say (str "As you happily sign the log in the coveted FTF spot, "
                      "you notice some tiny writing near the bottom of the logsheet."))
      (assoc-in gs [:room-map :stage-4] new-stage-4))))

(def label (item/make ["label" "can label" "beans label" "log" "logsheet" "cache log" "geocache log"]
                      (str "Upon closer inspection, the label is actually a very thin removable magnetic sheet. "
                           "It looks like it could be easily pulled off.")
                      :read (str "\"Best Baked Beans\"\n"
                                 "and handwritten underneath \"(NOT The Property of Dave Ulmer)\"")
                      ;; :pull {:say 
                      ;;        (str "The label comes right off. And, on the inner surface... it's the geocache log! "
                      ;;             "The log is completely blank except for the letters \"FTF\" on the first line, "
                      ;;             "awaiting its first signature.")
                      ;;        :post `after-log-revealed}
                      :remove   {:post `remove-label}
                      :pull     {:post `remove-label}
                      :pull-off {:post `remove-label}
                      :sign     {:post `sign-log}))

(def can (item/make ["can of beans" "can" "beans"]
                    "It's an old can of beans with a slightly frayed label."
                    :read (str "\"Best Baked Beans\"\n"
                               "and handwritten underneath \"(NOT The Property of Dave Ulmer)\"")))

(defn after-bucket-opened [oldgs gs] ;TODO
  ;(update-in gs [:events] conj :bucket-opened)
  )

(def bucket (item/make ["five gallon bucket" "five-gallon bucket" "bucket" "cache" "geocache"]
                       "It's a white five gallon bucket with a screw-on lid."
                       :closed true
                       :items #{can travel-bug plush-toy trinkets}))

(def tafoni (item/make ["tafoni"]
                       "These amazing tafoni were almost certainly created by salt weathering."
                       :look-in "It would take a long time to look in each and every one of them."))

(def tafone (item/make ["large tafone" "tafone"]
                       "It's a deep, cavernous oval pocket."
                       :items #{bucket}))

(def formation (item/make ["sandstone formation" "sandstone" "formation"]
                     (str "This formation is simply unbelievable! And it's covered with tafoni of many shapes and sizes. "
                          "There is a particularly large and interesting tafone near the base. "
                          "Who can imagine exactly how something of this subtle complexity "
                          "could have be carved by the forces of nature?!")
                     :climb "You greatly enjoy testing your climbing skills on this complex natural work-of-art."
                     :sit-on "You find a comfortable nook and settle in."))

(def stage-4 (-> (room/make "Final Stage"
                            "Final waypoint."
                            :initial-description 
                            (str "Knowing that you are headed for the final stage of this adventure, "
                                 "you are eager with anticipation! But not so much so that you "
                                 "fail to notice the unbelievable sandstone formations all around you. "
                                 "They remind you of something out of a Dr. Suess book. "
                                 "You continue on through this fantastical landscape, encountering formation after formation "
                                 "- each one more Seussian than the last. "
                                 "Ultimately, you arrive at your destination, yet another incredible sandstone formation. "
                                 "In fact, with it's towering spires, multi-varied cavities, and sinous curves, "
                                 "all of which are airbrushed to perfection in muted earthtones, "
                                 "this one is by far the most Seussian of them all!"))
                 (room/add-item formation "")
                 (room/add-item tafone "")
                 (room/add-item tafoni "")
                 (room/add-item label "")
                 (room/add-item dog-tag "")
                 (room/add-item code "")))

