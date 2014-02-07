(ns ranked-choice.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string]
            [cljs.core.async :refer [put! chan <!]]))

(def default-winner "None. Vote already!")

(defn first-nonconsecutive [coll-of-numbers]
  (loop [n 1
         ordered (sort coll-of-numbers)]
    (if (not= (first ordered) n)
      n
      (recur (inc n) (rest ordered)))))

(defn index-by [pred coll]
  (first (keep-indexed (fn [idx item]
                         (when (pred item) idx))
                        coll)))

(defn index-by-key-value [key coll value]
  (index-by #(do
               (when (= key :rank)
               (= (get % key) value))
            coll))

(defn by-id [id]
  (. js/document (getElementById id)))

(defn log [msg]
  (. js/console (log msg)))

(def app-state
  (atom
    {:winner default-winner
     :choices [{:name "puppies" :rank nil}
               {:name "rainbows" :rank nil}
               {:name "ice cream" :rank nil}
               {:name "waterfalls" :rank nil}]}))

(defn deref-choices []
  (:choices @app-state))

(defn index-by-name [name]
  (index-by-key-value :name (deref-choices) name))

(defn index-by-rank [rank]
  (index-by-key-value :rank (deref-choices) rank))

(defn next-rank []
  (first-nonconsecutive (remove nil? (map :rank (deref-choices)))))

(defn lowest-rank []
  (apply min (remove nil? (map :rank (deref-choices)))))

(defn update-winner! []
  (let [winning-rank (lowest-rank)
        winner (if winning-rank
                 (:name (nth (deref-choices) (index-by-rank winning-rank)))
                 default-winner)]
    (swap! app-state assoc :winner winner)))


(defn update-choices! [f & args]
  (apply swap! app-state update-in [:choices] f args))

(defn update-choice! [f name & args]
  (let [idx (index-by-name name)]
    (update-choices! (fn [choices & args]
                       (update-in choices [idx] f args))
                     (cons name args))))

(defn add-rank! [name rank]
  (update-choice! (fn [choice name]
                    {:name (:name choice) :rank rank})
                  name
                  rank))

(defn remove-rank! [name]
  (update-choice! (fn [choice name]
                    {:name (:name choice) :rank nil})
                  name))

(defn toggle-rank! [name]
  (let [choices (deref-choices)]
    (if (:rank (nth choices (index-by-name name)))
      (remove-rank! name)
      (add-rank! name (next-rank)))
    (update-winner!)))


(defn choice-component [choice]
  [:li {:on-click #(toggle-rank! (:name choice))
        :style {:cursor "pointer"}}
   (str (:name choice) " " (:rank choice))])

(defn choice-list-component []
  [:div
   [:p "Current leader: " [:span (:winner @app-state)]]
   [:p "Make your selections below:"]
   [:ul
    (for [choice (deref-choices)]
      [choice-component choice])]])


(defn main-page []
  [:div
   [:h1 "Welcome to the Instant Runoff Voterator!"]
   [choice-list-component]])

(defn mount [component element]
  (reagent/render-component [component] element))

(mount main-page (by-id "ranked-choice-root"))
