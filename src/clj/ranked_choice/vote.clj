(ns ranked-choice.vote
  (:use [clojure.pprint :only [pprint]])
  (:require [ranked-choice.test-data :as test-data]))

(defn candidate-sequences [ballots]
  "assumes data is a well-formed vector of
   key-value pairs where each key is the name of a candidate
   and each value is an integer ranking, such that in each vector,
   all the integer keys together consist of the unique numbers from
   1 to some number n, with no gaps"
  (map (fn [ballot]
         (map key (sort-by val ballot)))
       ballots))

(defn candidate-names [candidate-seqs]
  (distinct (apply concat candidate-seqs)))

(defn candidate-frequencies
  ([candidate-seqs]
    (let [all-names (candidate-names candidate-seqs)]
      (candidate-frequencies candidate-seqs all-names)))
  ([candidate-seqs names]
    (let [default-freqs (zipmap names (repeat 0))
          calculated-freqs (frequencies (map first candidate-seqs))]
      (into default-freqs calculated-freqs))))

(defn losing-candidates [candidate-freqs]
  (let [low-score (apply min (vals candidate-freqs))]
    (keys (filter #(#{low-score} (val %))
                  candidate-freqs))))

(defn losing-candidate [candidate-seqs]
  "If there's a tie, recursively pick from among the losers
  the one with the least votes in the next group of rankings,
  in the same version of candidate-seqs. If the tie cannot be
  resolved even after we reach the last round of the ballot,
  pick a loser at random."
  (loop [candidate-seqs candidate-seqs
         candidates-to-check (candidate-names candidate-seqs)]
    (let [freqs (candidate-frequencies candidate-seqs candidates-to-check)
          freqs-to-check (select-keys freqs candidates-to-check)
          low-scorers (losing-candidates freqs-to-check)]
      (cond
        (= (count low-scorers) 1) (first low-scorers)
        (empty? candidate-seqs) (rand-nth low-scorers)
        :otherwise (recur (remove empty? (map rest candidate-seqs))
                          low-scorers)))))

(defn winning-candidate [candidate-seqs]
  (let [majority-threshold (/ (count candidate-seqs) 2)
        candidate-freqs (candidate-frequencies candidate-seqs)
        [winner high-score] (apply max-key val candidate-freqs)]
    (when (> high-score majority-threshold)
      winner)))

(defn next-round [candidate-seqs]
  (let [loser (losing-candidate candidate-seqs)]
    (remove empty? (map (partial remove #{loser})
                        candidate-seqs))))

(defn vote [ballots & [verbose?]]
  (loop [candidate-seqs (candidate-sequences ballots)]
    (when verbose?
      (println "candidate sequences:")
      (pprint (sort-by first candidate-seqs))
      (println "candidate frequencies:")
      (pprint (reverse (sort-by val (candidate-frequencies candidate-seqs))))
      (println "winner:")
      (pprint (winning-candidate candidate-seqs))
      (println "losers:")
      (pprint (losing-candidates (candidate-frequencies candidate-seqs)))
      (println "loser:")
      (pprint (losing-candidate candidate-seqs))
      (println))
    (if-let [winner (winning-candidate candidate-seqs)]
      winner
      (recur (next-round candidate-seqs)))))

(defn demo []
  (vote test-data/regular-ballots-no-tie true))
