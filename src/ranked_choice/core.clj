(ns ranked-choice.core
  (:use [clojure.pprint :only [pprint]]))

(def majority-win-data
  [{"Bob" 1, "Sally" 2, "Maurice" 3}
   {"Bob" 1, "Sally" 3, "Maurice" 2}
   {"Bob" 2, "Sally" 1, "Maurice" 3}])

(def tie-data
  [{"Bob" 1}
   {"Sally" 1}])

(def data
  [{"Sally" 3, "Bob" 2, "Maurice" 1, "Susan" 4}
   {"Sally" 3, "Bob" 2, "Maurice" 1, "Susan" 4}
   {"Sally" 3, "Bob" 2, "Maurice" 1, "Susan" 4}
   {"Sally" 3, "Bob" 2, "Maurice" 1, "Susan" 4}
   {"Sally" 3, "Bob" 2, "Maurice" 1, "Susan" 4}
   {"Sally" 3, "Bob" 2, "Maurice" 4, "Susan" 1}
   {"Sally" 3, "Bob" 2, "Maurice" 4, "Susan" 1}
   {"Sally" 3, "Bob" 2, "Maurice" 4, "Susan" 1}
   {"Sally" 3, "Bob" 2, "Maurice" 4, "Susan" 1}
   {"Sally" 2, "Bob" 1, "Maurice" 4, "Susan" 3}
   {"Sally" 2, "Bob" 1, "Maurice" 4, "Susan" 3}
   {"Sally" 2, "Bob" 1, "Maurice" 4, "Susan" 3}
   {"Sally" 1, "Bob" 2, "Maurice" 4, "Susan" 3}
   {"Sally" 1, "Bob" 2, "Maurice" 4, "Susan" 3}])


; initial ballots consist of a sequence
; of vectors of maps of names (strings) to preference numbers (integers).

(defn candidate-sequences [ballots]
  (map (fn [ballot]
         (map key (sort-by val ballot)))
       ballots))

(defn candidate-frequencies [candidate-seqs]
  (frequencies (map first candidate-seqs)))

(defn high-or-low-scorer [max-or-min candidate-seqs]
  "if there's a tie, pick a random one from among the ties"
  (let [candidate-freqs (candidate-frequencies candidate-seqs)
        high-or-low-score (apply max-or-min (vals candidate-freqs))
        high-or-low-scorers (keys (filter #(= (val %) high-or-low-score)
                                          candidate-freqs))]
    (rand-nth high-or-low-scorers)))

(def high-scorer (partial high-or-low-scorer max))

(def low-scorer (partial high-or-low-scorer min))

(defn majority-winner [candidate-seqs]
  (let [majority-threshold (/ (count candidate-seqs) 2)
        candidate-freqs (candidate-frequencies candidate-seqs)
        [winner high-score] (apply max-key val candidate-freqs)]
    (when (> high-score majority-threshold)
      winner)))

(defn winning-candidate [candidate-seqs]
  (or (majority-winner candidate-seqs)
      (and (= (count (first candidate-seqs)) 1)
           (high-scorer candidate-seqs))))

(defn next-round [candidate-seqs]
  (let [loser (low-scorer candidate-seqs)]
    (map #(remove #{loser} %) candidate-seqs)))

(defn vote [ballots]
  (loop [candidate-seqs (candidate-sequences ballots)]
    (pprint (sort-by first candidate-seqs))
    (if-let [winner (winning-candidate candidate-seqs)]
      winner
      (recur (next-round candidate-seqs)))))


