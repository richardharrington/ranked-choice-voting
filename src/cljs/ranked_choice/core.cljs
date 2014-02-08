(ns ranked-choice.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [ranked-choice.vote :as vote]
            [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string]
            [cljs.core.async :refer [put! chan <!]]))

(def regular-ballots


[{:key 0,
  :ballot
  [{:key 0, :ballot-idx 0, :name "ice cream", :rank 4}
   {:key 1, :ballot-idx 0, :name "puppies", :rank 3}
   {:key 2, :ballot-idx 0, :name "video games", :rank 1}
   {:key 3, :ballot-idx 0, :name "rainbows", :rank 2}]}
 {:key 1,
  :ballot
  [{:key 0, :ballot-idx 1, :name "ice cream", :rank 4}
   {:key 1, :ballot-idx 1, :name "puppies", :rank 3}
   {:key 2, :ballot-idx 1, :name "video games", :rank 1}
   {:key 3, :ballot-idx 1, :name "rainbows", :rank 2}]}
 {:key 2,
  :ballot
  [{:key 0, :ballot-idx 2, :name "ice cream", :rank 4}
   {:key 1, :ballot-idx 2, :name "puppies", :rank 3}
   {:key 2, :ballot-idx 2, :name "video games", :rank 1}
   {:key 3, :ballot-idx 2, :name "rainbows", :rank 2}]}
 {:key 3,
  :ballot
  [{:key 0, :ballot-idx 3, :name "ice cream", :rank 4}
   {:key 1, :ballot-idx 3, :name "puppies", :rank 3}
   {:key 2, :ballot-idx 3, :name "video games", :rank 1}
   {:key 3, :ballot-idx 3, :name "rainbows", :rank 2}]}
 {:key 4,
  :ballot
  [{:key 0, :ballot-idx 4, :name "ice cream", :rank 4}
   {:key 1, :ballot-idx 4, :name "puppies", :rank 3}
   {:key 2, :ballot-idx 4, :name "video games", :rank 1}
   {:key 3, :ballot-idx 4, :name "rainbows", :rank 2}]}
 {:key 5,
  :ballot
  [{:key 0, :ballot-idx 5, :name "ice cream", :rank 3}
   {:key 1, :ballot-idx 5, :name "puppies", :rank 2}
   {:key 2, :ballot-idx 5, :name "video games", :rank 4}
   {:key 3, :ballot-idx 5, :name "rainbows", :rank 1}]}
 {:key 6,
  :ballot
  [{:key 0, :ballot-idx 6, :name "ice cream", :rank 3}
   {:key 1, :ballot-idx 6, :name "puppies", :rank 2}
   {:key 2, :ballot-idx 6, :name "video games", :rank 4}
   {:key 3, :ballot-idx 6, :name "rainbows", :rank 1}]}
 {:key 7,
  :ballot
  [{:key 0, :ballot-idx 7, :name "ice cream", :rank 3}
   {:key 1, :ballot-idx 7, :name "puppies", :rank 2}
   {:key 2, :ballot-idx 7, :name "video games", :rank 4}
   {:key 3, :ballot-idx 7, :name "rainbows", :rank 1}]}
 {:key 8,
  :ballot
  [{:key 0, :ballot-idx 8, :name "ice cream", :rank 3}
   {:key 1, :ballot-idx 8, :name "puppies", :rank 2}
   {:key 2, :ballot-idx 8, :name "video games", :rank 4}
   {:key 3, :ballot-idx 8, :name "rainbows", :rank 1}]}
 {:key 9,
  :ballot
  [{:key 0, :ballot-idx 9, :name "ice cream", :rank 2}
   {:key 1, :ballot-idx 9, :name "puppies", :rank 1}
   {:key 2, :ballot-idx 9, :name "video games", :rank 4}
   {:key 3, :ballot-idx 9, :name "rainbows", :rank 3}]}
 {:key 10,
  :ballot
  [{:key 0, :ballot-idx 10, :name "ice cream", :rank 2}
   {:key 1, :ballot-idx 10, :name "puppies", :rank 1}
   {:key 2, :ballot-idx 10, :name "video games", :rank 4}
   {:key 3, :ballot-idx 10, :name "rainbows", :rank 3}]}
 {:key 11,
  :ballot
  [{:key 0, :ballot-idx 11, :name "ice cream", :rank 2}
   {:key 1, :ballot-idx 11, :name "puppies", :rank 1}
   {:key 2, :ballot-idx 11, :name "video games", :rank 4}
   {:key 3, :ballot-idx 11, :name "rainbows", :rank 3}]}
 {:key 12,
  :ballot
  [{:key 0, :ballot-idx 12, :name "ice cream", :rank 1}
   {:key 1, :ballot-idx 12, :name "puppies", :rank 2}
   {:key 2, :ballot-idx 12, :name "video games", :rank 4}
   {:key 3, :ballot-idx 12, :name "rainbows", :rank 3}]}
 {:key 13,
  :ballot
  [{:key 0, :ballot-idx 13, :name "ice cream", :rank 1}
   {:key 1, :ballot-idx 13, :name "puppies", :rank 2}
   {:key 2, :ballot-idx 13, :name "video games", :rank 4}
   {:key 3, :ballot-idx 13, :name "rainbows", :rank 3}]}])

(defn process-ballots [ballots]
  (vec (map (fn [ballot]
              (into {} (map (fn [choice]
                              {(:name choice) (:rank choice)})
                            (remove #(nil? (:rank %))
                                    (:ballot ballot)))))
            ballots)))





; (defn process-ballots [ballots]
;   (vec (map-indexed (fn [ballot-idx ballot]
;                       {:key ballot-idx
;                        :ballot (vec (map (fn [key-idx ranking]
;                                            {:key key-idx
;                                             :ballot-idx ballot-idx
;                                             :name (key ranking)
;                                             :rank (val ranking)})
;                                          (range)
;                                          ballot))})
;                     ballots)))

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

(defn update-winner! []
  (let [processed-ballots (process-ballots (deref-choices))
        winner (vote/vote processed-ballots)]
    (swap! app-state assoc :winner winner)))


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
                    (assoc choice :rank rank))
                  ballot-idx
                  name
                  rank))

(defn remove-rank! [ballot-idx name]
  (update-choice! (fn [choice name]
                    (assoc choice :rank nil))
                  ballot-idx
                  name))

(defn toggle-rank! [ballot-idx name]
  (let [ballot (deref-ballot ballot-idx)]
    (if (:rank (nth ballot (index-by-name ballot-idx name)))
      (remove-rank! ballot-idx name)
      (add-rank! ballot-idx name (next-rank ballot-idx)))
    (update-winner!)))


(defn choice-component [choice]
  [:li {:on-click #(let [c choice
                         bic (:ballot-idx choice)
                         nc (:name choice)]
                         (toggle-rank! (:ballot-idx choice) (:name choice)))}
   [:span.rank (:rank choice)]
   [:span.name (:name choice)]])

(defn ballot-component [ballot]
  [:ul.ballot
   (for [choice (:ballot ballot)]
     [choice-component choice])])

(defn choice-lists-component []
  [:div
   [:p "Current leader: " [:span.leader (:winner @app-state)]]
   [:p "Make your selections below:"]
   [:div
    (for [ballot (deref-choices)]
      [ballot-component ballot])]])


(defn main-page []
  [:div
   [:h1 "Welcome to the Instant Runoff Voterator!"]
   [choice-lists-component]])

(swap! app-state assoc :choices regular-ballots)

(defn mount [component element]
  (reagent/render-component [component] element))

(mount main-page (by-id "ranked-choice-root"))
(update-winner!)
