(ns geocaching-adventure.verbs
  (:require [advenjure.verbs :refer [make-item-handler make-say-verb]]
            [advenjure.verb-map :refer [default-map add-verb]]
            [advenjure.utils :refer [say]]
            [advenjure.gettext.core :refer [_ p_]]))

(def help (make-say-verb (clojure.string/join 
                          "\n    " 
                          [(_ (str "You're playing a text adventure game. "
                                   "You play the game by typing commands. "
                                   "Some available commands are:"))
                           (_ "LOOK: Look around.")
                           (_ "LOOK AT <item>: Look at some specific item.")
                           (_ "LOOK IN <item>: Look inside some container item.")
                           (_ "TAKE <item>: Add an item to your inventory.")
                           (_ "INVENTORY: List your inventory contents. \"I\" will work too.")
                           (_ (str "GO <direction>: Move in the given compass direction."
                                   "\n         " "For example: \"GO NORTH\". \"NORTH\" and \"N\" will work too."))
                           (_ (str "TALK TO <character>: Start a conversation with another character."
                                   "\n         " "The dialogs are very simple, you simply choose what you want to say "
                                   "\n         " "by typing numbers from a list."))
                           (_ "OPEN, CLOSE, READ, LIFT, etc. may work on some objects.")
                           (_ "Try other things that make sense and see what happens.")
                           ;(_ "SAVE: save your current progress.")
                           ;(_ "RESTORE: restore a previously saved game.")
                           ;(_ "EXIT: close the game.")
                           ;(_ "You can use the TAB key to get completion suggestions for a command and the UP/DOWN arrows to search the command history.")
                                                        ])))

;; Additional custom verbs to support new commands.

;; (def lift (make-item-handler "lift" :lift
;;                               (fn [game-state item] (say (:lift item)))))
(def lift (make-item-handler (_ "lift") :lift))

;; (def sit-on (make-item-handler "sit on" :sit-on
;;                               (fn [game-state item] (say (:sit-on item)))))
(def sit-on (make-item-handler (_ "sit-on") :sit-on))

;(def remove (make-item-handler (_ "remove") :remove))
(def pull-off (make-item-handler (_ "pull off") :pull-off))

;; (def sign (make-item-handler "sign" :sign
;;                               (fn [game-state item] (say (:sign item)))))
(def sign (make-item-handler (_ "sign") :sign))

;; Print a message, just for fun.
(def yell (make-say-verb "AAAAHHHH!!!"))

;; Define the verb map to set the regexes that match the defined verbs.
(def verb-map (-> default-map ;support all the default verbs
                  (add-verb [(_ "^help$") (_ "^info$")] help)
                  (add-verb ["^lift (?<item>.*)" "^lift$"] lift)
                  ;(add-verb ["^remove (?<item>.*)" "^remove$"] remove)
                  (add-verb ["^pull off (?<item>.*)" "^pull off$"] pull-off)
                  (add-verb ["^sign (?<item>.*)" "^sign$"] sign)
                  (add-verb [(_ "^sit on (?<item>.*)") (_ "^sit on$")] sit-on)
                  (add-verb ["^yell$" "^scream$" "^shout$"] yell))) ;a couple synonyms
