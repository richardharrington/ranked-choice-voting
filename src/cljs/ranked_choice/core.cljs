(ns ranked-choice.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string]
            [cljs.core.async :refer [put! chan <!]]))


(defn index-of [pred coll]
  (first (keep-indexed (fn [idx item]
                         (when (pred item) idx))
                        coll)))

(defn by-id [id]
  (. js/document (getElementById id)))

(defn log [msg]
  (. js/console (log msg)))

(def app-state
  (atom
    {:winner ""
     :choices [["puppies" nil]
               ["rainbows" nil]
               ["ice cream" nil]
               ["waterfalls" nil]]}))

(defn deref-choices []
  (:choices @app-state))

(defn index-of-name [name]
  (index-of #(#{name} (first %))
            (deref-choices)))

(defn index-of-rank [rank]
  (index-of #(#{rank} (second %))
            (deref-choices)))

(defn first-nonconsecutive [coll-of-numbers]
  (loop [n 1
         ordered (sort coll-of-numbers)]
    (if (not= (first ordered) n)
      n
      (recur (inc n) (rest ordered)))))

(defn next-rank []
  (first-nonconsecutive (remove nil? (map second (deref-choices)))))

(defn update-choices! [f & args]
  (apply swap! app-state update-in [:choices] f args))

(defn update-choice! [f name & args]
  (let [idx (index-of-name name)]
    (update-choices! (fn [choices & args]
                       (update-in choices [idx] f args))
                     (cons name args))))

(defn add-rank! [name rank]
  (update-choice! (fn [choice name]
                    [(first choice) rank])
                  name
                  rank))

(defn remove-rank! [name]
  (update-choice! (fn [choice name]
                    [(first choice) nil])
                  name))

(defn toggle-rank! [name]
  (let [choices (deref-choices)]
    (if (second (nth choices (index-of-name name)))
      (remove-rank! name)
      (add-rank! name (next-rank)))))

(defn choice-list-component []
  [:div
   [:p "Current leader: " [:span (:winner @app-state)]]
   [:p "Make your selections below:"]
   [:ul
    (for [choice (deref-choices)]
      [:li {:on-click #(toggle-rank! (first choice))
            :style {:cursor "pointer"}}
       (str (first choice) " " (second choice))])]])


(defn main-page []
  [:div
   [:h1 "Welcome to the Instant Runoff Voterator!"]
   [choice-list-component]])

(defn mount [component element]
  (reagent/render-component [component] element))

(mount main-page (by-id "ranked-choice-root"))
