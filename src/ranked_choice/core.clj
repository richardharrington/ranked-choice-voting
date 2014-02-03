(ns ranked-choice.core
  (:use [clojure.pprint :only [pprint]])
  (:require [ranked-choice.test-data :as test-data]))


(defn candidate-sequences [ballots]
  (map (fn [ballot]
         (map key (sort-by val ballot)))
       ballots))

(defn candidate-frequencies [candidate-seqs]
  (frequencies (map first candidate-seqs)))

(defn highest-or-lowest-freq [max-or-min candidate-seqs]
  "if there's a tie, pick a random one from among the ties"
  (let [candidate-freqs (candidate-frequencies candidate-seqs)
        high-or-low-score (apply max-or-min (vals candidate-freqs))
        high-or-low-freqs (filter #(#{high-or-low-score} (val %))
                                  candidate-freqs)]
    (rand-nth high-or-low-freqs)))

(def highest-freq (partial highest-or-lowest-freq max))

(def lowest-freq (partial highest-or-lowest-freq min))

(defn majority-winner [candidate-seqs]
  (let [majority-threshold (/ (count candidate-seqs) 2)
        [winner high-score] (highest-freq candidate-seqs)]
    (when (> high-score majority-threshold)
      winner)))

(defn winning-candidate [candidate-seqs]
  (or (majority-winner candidate-seqs)
      (and (= (count (first candidate-seqs)) 1)
           (key (highest-freq candidate-seqs)))))

(defn next-round [candidate-seqs]
  (let [loser (key (lowest-freq candidate-seqs))]
    (map #(remove #{loser} %) candidate-seqs)))

(defn vote [ballots & [verbose?]]
  (loop [candidate-seqs (candidate-sequences ballots)]
    (when verbose?
      (pprint (sort-by first candidate-seqs)))
    (if-let [winner (winning-candidate candidate-seqs)]
      winner
      (recur (next-round candidate-seqs)))))

(defn demo []
  (vote test-data/majority-in-third-round true))


