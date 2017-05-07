(ns advenjure.ui.input
  (:require [clojure.string :as string]
            [advenjure.items :refer [all-item-names]]
            [advenjure.rooms :refer [visible-name-mappings]]
            [advenjure.utils :refer [direction-mappings current-room room-as-item]]))

(defn read-key []
  ;(str (char (.readCharacter console)))
  (read-line)
  )

(def exit #(System/exit 0))

(defn read-value
  "read a single key and eval its value. Return nil if no value entered."
  []
  (let [input (read-key)]
    (try
      (read-string input)
      (catch RuntimeException e nil))))

(defn prompt [gs]
  (let [prompt-fn (get-in gs [:configuration :prompt])
        p (prompt-fn gs)]
    (print p)
    (flush)
    (read-line)))

(defn get-input
  ([game-state]
   (let [verb-map (get-in game-state [:configuration :verb-map])
         verbs (keys verb-map)
         room (current-room game-state)
         room-names (keys (visible-name-mappings (:room-map game-state) (:current-room game-state)))
         all-items (concat (:inventory game-state) (:items room) [(room-as-item room)])
         item-names (all-item-names all-items)
         input (prompt game-state)]
     input)))

(defn read-file [file] (read-string (slurp file)))

(defn prompt-value
  "Ask the user to enter a value"
  [prompt]
  (print prompt)
  (flush)
  (read-line))
