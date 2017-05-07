(ns geocaching-adventure.support
  (:require [lock-key.core :refer [decrypt-from-base64]])
  )

(def meta-key "nq(ci3sxQ#WT")

(def unscramble decrypt-from-base64)

(defn disentangle [tangle]
  (letfn [(untangle [x]
            (map #(reduce str %) (apply map list (partition 2 x))))]
    (let [[y z] (untangle tangle)]
      (unscramble y (unscramble z meta-key)))))

(def gordian-knot-1 "0/N2ZR8u2XaF0mvh8D1HO72LbYhYfbNdo3mw3LTsTwCNYWfu6WGgIq+vFDVF89au+R+MwmCsMa5c+x1rYrbXQo==")

(def gordian-knot-2 "GGUS3HAA+KAJlS6u4T2dziVyE6qejZpBvmYEMoO+fhWOQcgCsY6tE1PvOvnA6zY5uwOfYa5dm9L3h6qi83Uhgw==")

(def gordian-knot-3 "GMd9yYa3KZVnw58r0ZviOkK5wMHKjTKPICfvvqK8K6QmtfRiCN213Wtc5Qqe/ZgP4zigRngW5UM8RdHr9C6iUc==")

(def gordian-knot-4 "N 34 ...")
