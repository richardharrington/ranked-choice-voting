(ns ranked-choice.core
  (:use [clojure.pprint :only [pprint]])
  (:require [ranked-choice.test-data :as test-data]))

(defn candidate-sequences [ballots]
  (map (fn [ballot]
         (map key (sort-by val ballot)))
       ballots))

(defn candidate-frequencies [candidate-seqs]
  (frequencies (map first candidate-seqs)))

(defn low-scorer [candidate-seqs]
  "if there's a tie, pick a random one from among the ties"
  (let [candidate-freqs (candidate-frequencies candidate-seqs)
        low-score (apply min (vals candidate-freqs))
        low-freqs (filter #(#{low-score} (val %))
                          candidate-freqs)]
    (key (rand-nth low-freqs))))

(defn winning-candidate [candidate-seqs]
  (let [majority-threshold (/ (count candidate-seqs) 2)
        candidate-freqs (candidate-frequencies candidate-seqs)
        [winner high-score] (apply max-key val candidate-freqs)]
    (when (> high-score majority-threshold)
      winner)))

(defn next-round [candidate-seqs]
  (let [loser (low-scorer candidate-seqs)]
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
