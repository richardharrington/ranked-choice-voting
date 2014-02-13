(ns ranked-choice.core
  (:require [ranked-choice.vote :as vote]
            [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string]

; TODO: Put this data in some other file. Probably
; have it in the other format, to be transferred into the
; appropriate format to be used by this file.

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



; TODO: reinstate this to take convert ballot info in the format from
; ranked-choice.vote into ballot info suitable for the views
; in these components. (Note the generation of the keys.)

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

; TODO: Reinstate this default winner thing. Investigate the
; issue where the winner is not displayed properly if there's
; only one vote. Possibly nothing else to compare it to?
; It also doesn't deal well with the edge case of zero votes.

(def default-winner "None. Vote already!")

(defn first-nonconsecutive [coll-of-numbers]
  (loop [n 1
         ordered (sort coll-of-numbers)]
    (if (not= (first ordered) n)
      n
      (recur (inc n) (rest ordered)))))

; TODO: Look into indexing by React keys throughout this file,
; instead of indexing by array index values.

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

; TODO: rename "choices" to something else. It's a list
; of ballots now. A list of lists of choices, so to speak.
; Probably it should be called "ballots".

; TODO: General note. Do we really have to have all these data
; structures that contain pathways to themselves from the top
; of bigger data structures? I'm speaking mostly of things like choice-component,
; which have local indexes and ballot indexes and such. It's the same
; problem I had with robotwar. I must speak to John Barker about it.

(def app-state
  (atom
    {:winner default-winner
     :choices []}))

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

; These ballot-indexes are the same as keys. Get rid of ballot-indexes.
; Look into the whole "key" thing in React. What's that all about?

(defn choice-component [{:keys [ballot-idx name rank]}]
  [:li {:on-click #(toggle-rank! ballot-idx name)}
   [:span.rank rank]
   [:span.name name]])

(defn ballot-component [{ballot :ballot}]
  [:ul.ballot
   (for [choice ballot]
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

; TODO: Have it update the winner here? Really?
; Look for something more organic.

(update-winner!)
