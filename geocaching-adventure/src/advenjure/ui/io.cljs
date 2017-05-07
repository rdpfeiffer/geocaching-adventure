(ns advenjure.ui.io
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [sablono.core :refer [html]]
            [cljs.core.async :refer [<! >! chan close!]]))

(enable-console-print!) ;for debugging

(def input-chan (atom (chan)))

(defn figwheel-cleanup
  "Need to recreate input chan so previous game loop doesnt receive input anymore."
  []
  (println "resetting input channel for figwheel cleanup")
  (close! @input-chan)
  (reset! input-chan (chan)))

;(defonce app-state (atom {:lines ["testing..."]}))
(defonce app-state (atom {:lines []}))

(def tab-key 9)
(def enter-key 13)
(def prompt "> ")

;; For debugging
;; (defn clear-lines [data]
;;   (swap! data update-in [:lines] #(vector)))

(defn add-line [line data]
  (swap! data update-in [:lines] #(conj % (str prompt line)))
  (go (>! @input-chan line)))

;; (defn add-text [text data]
;;   (let [lines (filter #(not= % "\n") 
;;                       (map #(reduce str %) 
;;                            (partition-by #(= % "\n") text)))]
;;     (swap! data update-in [:lines] #(reduce conj % lines))))

(defn output-text [text]
  (let [lines (filter #(not= % "\n") 
                      (map #(reduce str %) 
                           (partition-by #(= % "\n") text)))]
    (swap! app-state update-in [:lines] #(reduce conj % (conj (into [] lines) "")))))

;; For debugging
;; (defn add-dummy-lines [data]
;;   (add-text "testing...\nbada bing\nbada boom!" data))

(defn on-blur [event]
  ;(println (str "on-blur, " (.. event -target))) ;for debugging
  (let [target (.. event -target)]
    (js/setTimeout #(.. target focus))))

;; For debugging
;(defn on-focus [event]
;  (println (str "on-focus, " (.. event -target))))

;; TODO - this doesn't work, it never gets called when "tab" key is pressed
(defn on-key-down [event]
  ;(println (str "on-key-down, " (.. event -key))) ;for debugging
  (when (= key tab-key)
      (.. event -preventDefault)))

(defn on-key-up [event data]
  (let [key (.. event -keyCode)
        input-text (.. event -target -value)]
    ;(println (str "on-key-up, key: " key ", text: " input-text)) ;for debugging
    (when (= key enter-key)
      (set! (.-value (js/document.getElementById "input-field")) "")
      (add-line input-text data))))

(defn display-lines [data]
  (html [:div 
         ;; [:div 
         ;;  [:a {:href "#" :on-click #(clear-lines data)} "Clear lines"]
         ;;  [:a {:href "#" :on-click #(add-dummy-lines data)} "Add lines"]]
         (mapcat #(list (vector :label %) [:br]) (:lines @data))
         [:div
         [:label {:for "input-field"} prompt]
         [:input
          {:type "text"
           :id "input-field"
           :auto-focus "on"
           :on-blur #(on-blur %)
           ;:on-focus #(on-focus %) ;for debugging
           :on-key-down #(on-key-down %)
           :on-key-up #(on-key-up % data)
           ;:class "autocomplete"
           ;:auto-complete "off"
           }]]]))

(defn render! []
  (.render js/ReactDOM
           (display-lines app-state)
           (.getElementById js/document "app"))
  (js/window.scrollTo 0 js/document.body.scrollHeight))

;; add-watch is getting called over and over again on reload. 
;; But this is OK because it is just replacing the current :on-change listener, making this top level side-effect reloadable.
(add-watch app-state :on-change (fn [_ _ _ _] (render!)))

;; This forces a re-render every time the file is loaded. 
;; This is helpful so that we will render app changes as we edit the file.
(render!)
