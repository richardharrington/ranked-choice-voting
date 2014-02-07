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

(defn index-by-key-value [k coll value]
  (index-by #(= (k %) value)
            coll))

(defn by-id [id]
  (. js/document (getElementById id)))

(defn log [msg]
  (. js/console (log msg)))

(def app-state
  (atom
    {:winner default-winner
     :choices [{:key 0
                :ballot [{:key 0 :ballot-idx 0 :name "puppies" :rank nil}
                         {:key 1 :ballot-idx 0 :name "rainbows" :rank nil}
                         {:key 2 :ballot-idx 0 :name "ice cream" :rank nil}
                         {:key 3 :ballot-idx 0 :name "waterfalls" :rank nil}]}

               {:key 1
                :ballot [{:key 0 :ballot-idx 1 :name "puppies" :rank nil}
                         {:key 1 :ballot-idx 1 :name "rainbows" :rank nil}
                         {:key 2 :ballot-idx 1 :name "ice cream" :rank nil}
                         {:key 3 :ballot-idx 1 :name "waterfalls" :rank nil}]}

               {:key 2
                :ballot [{:key 0 :ballot-idx 2 :name "puppies" :rank nil}
                         {:key 1 :ballot-idx 2 :name "rainbows" :rank nil}
                         {:key 2 :ballot-idx 2 :name "ice cream" :rank nil}
                         {:key 3 :ballot-idx 2 :name "waterfalls" :rank nil}]}

               {:key 3
                :ballot [{:key 0 :ballot-idx 3 :name "puppies" :rank nil}
                         {:key 1 :ballot-idx 3 :name "rainbows" :rank nil}
                         {:key 2 :ballot-idx 3 :name "ice cream" :rank nil}
                         {:key 3 :ballot-idx 3 :name "waterfalls" :rank nil}]}

               {:key 4
                :ballot [{:key 0 :ballot-idx 4 :name "puppies" :rank nil}
                         {:key 1 :ballot-idx 4 :name "rainbows" :rank nil}
                         {:key 2 :ballot-idx 4 :name "ice cream" :rank nil}
                         {:key 3 :ballot-idx 4 :name "waterfalls" :rank nil}]}]}))

(defn deref-choices []
  (:choices @app-state))

(defn deref-ballot [idx]
  (:ballot (nth (deref-choices) idx)))

(defn index-by-name [ballot-idx name]
  (index-by-key-value :name (deref-ballot ballot-idx) name))

(defn index-by-rank [ballot-idx rank]
  (index-by-key-value :rank (deref-ballot ballot-idx) rank))

(defn next-rank [ballot-idx]
  (first-nonconsecutive (remove nil? (map :rank (deref-ballot ballot-idx)))))

; (defn lowest-rank []
;   (apply min (remove nil? (map :rank (deref-choices)))))

; (defn update-winner! []
;   (let [winning-rank (lowest-rank)
;         winner (if winning-rank
;                  (:name (nth (deref-choices) (index-by-rank winning-rank)))
;                  default-winner)]
;     (swap! app-state assoc :winner winner)))


(defn update-choices! [f & args]
  (apply swap! app-state update-in [:choices] f args))

(defn update-ballot! [f ballot-idx & args]
  (update-choices! (fn [choices & args]
                     (update-in choices [ballot-idx :ballot] f args))
                   args))

(defn update-choice! [f ballot-idx name & args]
  (let [choice-idx (index-by-name ballot-idx name)]
    (apply update-ballot! (fn [ballot & args]
                              (update-in ballot [choice-idx] f args))
                          ballot-idx
                          name
                          args)))


(defn add-rank! [ballot-idx name rank]
  (update-choice! (fn [choice name]
                    {:name (:name choice) :rank rank})
                  ballot-idx
                  name
                  rank))

(defn remove-rank! [ballot-idx name]
  (update-choice! (fn [choice name]
                    {:name (:name choice) :rank nil})
                  ballot-idx
                  name))

(defn toggle-rank! [ballot-idx name]
  (let [ballot (deref-ballot ballot-idx)]
    (if (:rank (nth ballot (index-by-name ballot-idx name)))
      (remove-rank! ballot-idx name)
      (do (add-rank! ballot-idx name (next-rank ballot-idx))
        (log "hi")))
    ; (update-winner!)

    ))


(defn choice-component [choice]
  [:li {:on-click #(toggle-rank! (:ballot-idx choice) (:name choice))
        :style {:cursor "pointer"}}
   (str (:name choice) " " (:rank choice))])

(defn choice-list-component []
  [:div
   [:p "Current leader: " [:span (:winner @app-state)]]
   [:p "Make your selections below:"]
   [:ul
    (for [choice (deref-ballot 0)]
      [choice-component choice])]])


(defn main-page []
  [:div
   [:h1 "Welcome to the Instant Runoff Voterator!"]
   [choice-list-component]])

(defn mount [component element]
  (reagent/render-component [component] element))

(mount main-page (by-id "ranked-choice-root"))
