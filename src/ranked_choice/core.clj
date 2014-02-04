(ns ranked-choice.core
  (:use [clojure.pprint :only [pprint]])
  (:require [ranked-choice.test-data :as test-data]))

(defn candidate-sequences [ballots]
  (map (fn [ballot]
         (map key (sort-by val ballot)))
       ballots))

(defn candidate-frequencies [candidate-seqs]
  (frequencies (map first candidate-seqs)))

(defn losing-candidates [candidate-freqs candidates-to-check]
  (let [freqs-to-check (filter #(get candidates-to-check (key %))
                               candidate-freqs)
        low-score (apply min (vals freqs-to-check))]
    (keys (filter #(#{low-score} (val %))
                  freqs-to-check))))

(defn losing-candidate [candidate-seqs]
  "if there's a tie, recursively pick from among the losers
  the one who would score the lowest in the next round. If they
  tie all the way back to the last choice on the ballot, pick one at random."
  (loop [candidate-seqs candidate-seqs
         candidates-to-check (set (first candidate-seqs))]
    (let [candidate-freqs (candidate-frequencies candidate-seqs)
          low-scorers (losing-candidates candidate-freqs candidates-to-check)]
      (cond
        (= (count low-scorers) 1)
          (first low-scorers)
        (= (count (first candidate-seqs)) 1)
          (rand-nth low-scorers)
        :otherwise
          (recur (map next candidate-seqs) (set low-scorers))))))

(defn winning-candidate [candidate-seqs]
  (let [majority-threshold (/ (count candidate-seqs) 2)
        candidate-freqs (candidate-frequencies candidate-seqs)
        [winner high-score] (apply max-key val candidate-freqs)]
    (when (> high-score majority-threshold)
      winner)))

(defn next-round [candidate-seqs]
  (let [loser (losing-candidate candidate-seqs)]
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
