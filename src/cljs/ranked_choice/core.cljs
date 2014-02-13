(ns ranked-choice.core
  (:require [ranked-choice.vote :as vote]
            [ranked-choice.test-data :as test-data]
            [reagent.core :as reagent]
            [clojure.string :as string]))


(defn view->ballots [ballots]
  (vec (map (fn [ballot]
              (into {} (map (fn [choice]
                              {(:name choice) (:rank choice)})
                            (remove #(nil? (:rank %))
                                    (:ballot ballot)))))
            ballots)))

(defn ballots->view [ballots]
  (vec (map-indexed (fn [ballot-idx ballot]
                      {:key ballot-idx
                       :ballot (vec (map (fn [key-idx ranking]
                                           {:key key-idx
                                            :ballot-idx ballot-idx
                                            :name (key ranking)
                                            :rank (val ranking)})
                                         (range)
                                         ballot))})
                    ballots)))

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
  (reagent/atom
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
  (let [processed-ballots (view->ballots (deref-choices))
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

(defn mount [component element]
  (reagent/render-component [component] element))

(defn make-it-so []
  (let [test-ballots (ballots->view test-data/regular-ballots-no-tie)
        root-id "ranked-choice-root"]
    (swap! app-state assoc :choices test-ballots)
    (mount main-page (by-id root-id))
    (update-winner!)))

; TODO: Have it update the winner here? Really?
; Look for something more organic.

(make-it-so)
