(ns ranked-choice.core)

(def data
  [{"Sally" 3
    "Bob" 2
    "Maurice" 1}
   {"Sally" 1
    "Bob" 2
    "Maurice" 3}
   {"Sally" 1
    "Bob" 2
    "Maurice" 3}
   {"Sally" 1
    "Bob" 2
    "Maurice" 3}])


; initial ballots consist of a sequence
; of vectors of maps of names (strings) to preference numbers (integers).

(defn vote [ballots]
  (let [total-to-beat (/ (count ballots) 2)
        find-winner (fn [candidate-totals]
                 (if-let [winner-map-entry (some #(> (val %) total-to-beat)
                                                 candidate-totals)]
                   (key winner-map-entry)
                   nil))
        find-loser (fn [candidate-totals]
                (key (reduce (fn [acc candidate-total]
                               (if (< (val candidate-total) (val acc))
                                 candidate-total
                                 acc))
                             candidate-totals)))]
    (loop [ballot-vectors (map (fn [ballot]
                                  (map key (sort-by val ballot)))
                                ballots)]
      (let [candidate-totals (frequencies (map first ballot-vectors))]
        (or (find-winner candidate-totals)
            (let [loser (find-loser candidate-totals)]
              (recur (map (fn [ballot-vector]
                            (if (= (first ballot-vector) loser)
                              (rest ballot-vector)
                              ballot-vector))
                          ballot-vectors))))))))