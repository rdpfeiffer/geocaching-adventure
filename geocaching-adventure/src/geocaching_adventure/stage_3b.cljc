(ns geocaching-adventure.stage-3b
  (:require [clojure.string :as string]
            [advenjure.items :as item]
            [advenjure.rooms :as room]
            [advenjure.utils :as utils]
            [geocaching-adventure.support :refer [disentangle gordian-knot-2]]
            [advenjure.ui.input :refer [prompt-value]]
            #?(:cljs [advenjure.dialogs :refer [event? not-event? set-event item?]]
               :clj [advenjure.dialogs :refer [event? not-event? set-event item? dialog conditional optional random]])
            #?(:clj [advenjure.async :refer [alet]]))
  #?(:cljs (:require-macros [advenjure.dialogs :refer [dialog conditional optional random]]
                            [advenjure.async :refer [alet]])))


;;; The rest area.

(def ring (item/make ["shiny thing" "shiny object" "thing" "ring"]
                      "An old ring. It doesn't look valuable."
                      :take true))

(def hole (item/make ["hole"]
                      "A small hole."
                      :items #{ring}))

(def bench-1 (item/make ["bench"]
                      (str "It's a well-worn comfortable little bench with a great view. "
                           "It's clearly seen a lot of use by weary hikers over the years. "
                           "It's quite scratched up, could definitely use a fresh coat of paint, and there's a little hole, "
                           "probably made by a small burrowing animal, by one of its legs.")
                      :sit-on "It's just as comfortable as you expected."))

(def rest-area (-> (room/make "Resting area"
                              "Bench area."
                              :initial-description 
                              (str "The trail now begins a nice easy climb. "
                                   "You ascend up through an old growth forest just teeming with nature. "
                                   "The gentle breeze refreshes your spirit. "
                                   "You hear nothing but the faint rustling of the leaves, some amazing birdsong, "
                                   "and your own solitary footsteps. "
                                   "By and by, you arrive in a nice little area made for resting. "
                                   "The trail turns and continues northward from here."))
                   (room/add-item bench-1)
                   (room/add-item hole "")))


;;; The waterfall area.

(defn read-booklet []
  (advenjure.dialogs/set-event :stage-4-coords))

(defn after-booklet-read [old-gs gs] 
  (update-in gs [:events] conj :stage-4-coords))

(def booklet (item/make ["large booklet" "booklet" "large book" "book"]
                        "It's entitled \"Wonderful Waterfalls of the World\"."
                        :read {:say (str "The booklet contains images of some of the most breathtaking waterfalls "
                                         "from all around the world - icons of the earth in all of its splendor!\n"
                                         "Inside the front cover there is a note which says:\n"
                                         "\"CONGRATULATIONS ON FINDING THIS PENULTIMATE STAGE!\"\n"
                                         "The coordinates for the final stage are included.")
                               :post `after-booklet-read}))

(defn enter-combination
  [gs]
  ;; using the alet macro to hide the async nature of user input in ClojureScript
  (alet [combo (prompt-value "Enter combination: ") ;TODO figure out why this broke
  ;(let [combo (prompt-value "Enter combination: ")
        combo (string/trim combo)
        responses ["No luck." "That wasn't it." "Nope."]]
    (if (= combo (disentangle gordian-knot-2))
      (do (utils/say "It works! The lock is open.") true)
      (get responses (rand-int (count responses))))))

(defn open-lock
  [old-gs new-gs]
  (let [lock (utils/find-first new-gs "padlock")
        new-lock (assoc lock :use "The padlock was already open.")
        ammo-box (utils/find-first new-gs "ammo box")
        new-ammo-box (merge ammo-box {:closed false
                                      :close "Better left open."})] ;TODO change this?
    (-> new-gs
        (utils/replace-item lock new-lock)
        (utils/replace-item ammo-box new-ammo-box))))

(def lock-conditions {:pre `enter-combination :post `open-lock})

(def padlock (item/make ["lock" "padlock"]
                        "It's a heavy-duty combination lock with five rotating wheels. The wheels have letters on them."
                        :use lock-conditions))

(def ammo-box (item/make ["ammo box" "ammo-box" "ammo can" "ammo-can" "box" "can" "cache" "geocache"]
                           "It's an ammo box alright."
                           :read "Official Geocache"
                           :closed true
                           :items #{booklet}
                           :open lock-conditions
                           :unlock "I had to USE the padlock to open the ammo box."
                           :locked false
                           :lock "It's already locked."))

(defn look-in-log [oldgs gs] 
  (if (first (utils/find-item gs "muggles"))
    (utils/say "You can't do any searching with those muggles here.")
    (let [log (first (utils/find-item gs "log"))]
      (utils/say (str "Deep inside there's an ammo box tethered by a thick chain "
                      "to a stake driven through the bottom of the heavy log and into the ground. "
                      "The box is locked with a very sturdy padlock. "
                      "Obviously, the CO didn't want a cache placed in such a popular location to be muggled.")))))

(def log (item/make ["hollow log" "log"]
                     "The log is quite massive - it's certainly not going anywhere!"
                     :look-in {:post `look-in-log}
                     :sit-on "You pick out a nice spot with a great view and take a seat."))


;;; The muggles dialog.

(def greet-muggles (conditional (event? :knows-muggles)
                                ("YOU" "Hello again.")
                                ("YOU" "Hi, folks!")))

;; Randomly select one of the given dialog lines.
(def muggles-say-hi (random ("MUGGLES" "Hello.")
                            ("MUGGLES" "Hi.")
                            ("MUGGLES" "Hi there.")))

(defn muggles-move
  "Ring item is removed from inventory, MUGGLES character(s) removed from the 'room',player describes MUGGLES leaving."
  [gs]
  (let [ring    (first (utils/find-item gs "ring"))
        muggles (first (utils/find-item gs "muggles"))
        log     (first (utils/find-item gs "log"))]
    (utils/say (str "The muggles take the ring and offer you a small reward, which you refuse. "
                    "They thank you again and depart down the trail, singing a song from a Gilbert & Sullivan "
                    "operetta in perfect harmony."))
    (-> gs
        (utils/remove-item ring)
        (utils/remove-item muggles)
        (utils/replace-item log (assoc log :items #{ammo-box})))))

(def muggles-dialog-options
  (optional
   ("Isn't that a breathtaking waterfall?"
    (dialog ("YOU" "Isn't that a breathtaking waterfall?")
            ("MUGGLES" "It sure is!")))
   ("I've never seen a double rainbow before!"
    (dialog ("YOU" "I've never seen a double rainbow before!")
            ("MUGGLES" "None of us had either. It's stunning!")))
   ("Have you been here long?"
    (dialog ("YOU" "Have you been here long?")
            ("MUGGLES" (str "Yes. And we're actually ready to leave, but we can't, because we're looking "
                            "for a ring of great sentimental value that one of us has lost."))
            (advenjure.dialogs/set-event :knows-about-ring)))
   ("I haven't found any rings, but I'll be on the lookout and let you know if I find it."
    (dialog ("YOU" "I haven't seen a ring, but I'll be on the lookout and let you know if I find it.")
            ("MUGGLES" "Thank you."))
    :show-if #(and (not ((advenjure.dialogs/item? "ring") %)) ((advenjure.dialogs/event? :knows-about-ring) %))
    :sticky) 
   ("Is this the ring you're looking for?"
    (dialog ("YOU" "Is this the ring you're looking for?")
            ("MUGGLES" "It is! Thanks a million!")
            muggles-move)
    :show-if #(and ((advenjure.dialogs/item? "ring") %) ((advenjure.dialogs/event? :knows-about-ring) %))
    :go-back)
   ("Goodbye." (dialog ("YOU" "Goodbye.")
                       ("MUGGLES" "Bye."))
    :sticky
    :go-back)))

(def muggles-dialog
  (dialog greet-muggles 
          muggles-say-hi 
          muggles-dialog-options   
          (advenjure.dialogs/set-event :knows-muggles)))

(def muggles (item/make ["group of muggles" "muggles" "muggle"]
                        "The muggles were discussing something intently and looking around on the ground."
                        :dialog `muggles-dialog))


;;; The "room".

(def stage-3b (-> (room/make "Stage 3"
                             "A magnificent waterfall."
                             :initial-description 
                             (str "You continue your ascent. "
                                  "As you arrive at the top, you are treated to a view "
                                  "of one of the most magnificent waterfalls that you have ever seen! "
                                  "And, even more amazingly, the precise angle of the sun hitting the mist "
                                  "from the falls conspires to treat you to that rarest of sights - a double rainbow!! "
                                  "You could pull up a seat on a hugh old log to rest and take in the marvelous view."))
                  (room/add-item log "")
                  (room/add-item muggles)))
