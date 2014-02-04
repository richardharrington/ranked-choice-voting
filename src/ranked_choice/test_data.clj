(ns ranked-choice.test-data)

(def tie
  [{"Bob" 1, "Sally" 2}
   {"Bob" 2, "Sally" 1}])

(def majority-in-first-round
  [{"Bob" 1, "Sally" 2, "Maurice" 3}
   {"Bob" 1, "Sally" 3, "Maurice" 2}
   {"Bob" 2, "Sally" 1, "Maurice" 3}])

(def majority-in-third-round
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
   {"Sally" 1, "Bob" 2, "Maurice" 4, "Susan" 3}
   {"Sally" 1, "Bob" 2, "Maurice" 4, "Susan" 3}])


