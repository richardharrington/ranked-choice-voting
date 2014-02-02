(ns ranked-choice.core)

(def majority-win-data
  [{"Bob" 1, "Sally" 2, "Maurice" 3}
   {"Bob" 1, "Sally" 3, "Maurice" 2}
   {"Bob" 2, "Sally" 1, "Maurice" 3}])

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

(defn majority-score [items]
  (/ (count items) 2))

(defn ballot-vectors [ballots]
  (map (fn [ballot]
         (map key (sort-by val ballot)))
       ballots))

(defn candidate-frequencies [ballots]
  (frequencies (map first (ballot-vectors ballots))))

(defn high-or-low-scorer [max-or-min ballots]
  "if there's a tie, pick a random one from among the ties"
  (let [candidate-freqs (candidate-frequencies ballots)
        high-or-low-score (apply max-or-min (vals candidate-freqs))
        high-or-low-scorers (keys (filter #(= (val %) high-or-low-score)
                                     candidate-freqs))]
    (rand-nth (seq high-or-low-scorers))))

(defn high-scorer [ballots]
  (high-or-low-scorer max ballots))

(defn low-scorer [ballots]
  (high-or-low-scorer min ballots))

(defn majority-winner [ballots]
  (let [candidate-freqs (candidate-frequencies ballots)
        [winner high-score] (apply max-key val candidate-freqs)]
    (when (> high-score (majority-score ballots))
      winner)))