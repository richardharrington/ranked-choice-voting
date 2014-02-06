(ns ranked-choice.test-data)

(def simple-tie
  [{"Bob" 1, "Sally" 2}
   {"Bob" 2, "Sally" 1}])

(def first-round-majority
  [{"Bob" 1, "Sally" 2, "Maurice" 3}
   {"Bob" 1, "Sally" 3, "Maurice" 2}
   {"Bob" 2, "Sally" 1, "Maurice" 3}])

(def regular-ballots-no-tie
  [{"Sally" 4, "Bob" 3, "Maurice" 1, "Susan" 2}
   {"Sally" 4, "Bob" 3, "Maurice" 1, "Susan" 2}
   {"Sally" 4, "Bob" 3, "Maurice" 1, "Susan" 2}
   {"Sally" 4, "Bob" 3, "Maurice" 1, "Susan" 2}
   {"Sally" 4, "Bob" 3, "Maurice" 1, "Susan" 2}
   {"Sally" 3, "Bob" 2, "Maurice" 4, "Susan" 1}
   {"Sally" 3, "Bob" 2, "Maurice" 4, "Susan" 1}
   {"Sally" 3, "Bob" 2, "Maurice" 4, "Susan" 1}
   {"Sally" 3, "Bob" 2, "Maurice" 4, "Susan" 1}
   {"Sally" 2, "Bob" 1, "Maurice" 4, "Susan" 3}
   {"Sally" 2, "Bob" 1, "Maurice" 4, "Susan" 3}
   {"Sally" 2, "Bob" 1, "Maurice" 4, "Susan" 3}
   {"Sally" 1, "Bob" 2, "Maurice" 4, "Susan" 3}
   {"Sally" 1, "Bob" 2, "Maurice" 4, "Susan" 3}])

(def regular-ballots-tie
  [{"Sally" 4, "Bob" 3, "Maurice" 1, "Susan" 2}
   {"Sally" 4, "Bob" 3, "Maurice" 1, "Susan" 2}
   {"Sally" 4, "Bob" 3, "Maurice" 1, "Susan" 2}
   {"Sally" 4, "Bob" 3, "Maurice" 1, "Susan" 2}
   {"Sally" 4, "Bob" 3, "Maurice" 1, "Susan" 2}
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

(def irregular-ballots
  [{"Twinkies" 1, "Jalapeños" 2}
   {"Sally" 5, "Bob" 4, "Maurice" 2, "Susan" 3, "Garbonzo Beans" 1}
   {"Sally" 5, "Bob" 4, "Maurice" 2, "Susan" 3, "Garbonzo Beans" 1}
   {"Sally" 4, "Bob" 3, "Maurice" 1, "Susan" 2}
   {"Sally" 4, "Bob" 3, "Maurice" 1, "Susan" 2}
   {"Sally" 4, "Bob" 3, "Maurice" 1, "Susan" 2}
   {"Sally" 3, "Bob" 2, "Maurice" 4, "Susan" 1}
   {"Sally" 3, "Bob" 2, "Maurice" 4, "Susan" 1}
   {"Sally" 3, "Bob" 2, "Maurice" 4, "Susan" 1}
   {"Sally" 4, "Bob" 3, "Maurice" 5, "Susan" 2, "Chilis" 1}
   {"Sally" 2, "Bob" 1, "Maurice" 4, "Susan" 3}
   {"Sally" 2, "Bob" 1, "Maurice" 4, "Susan" 3}
   {"Sally" 2, "Bob" 1, "Maurice" 4, "Susan" 3}
   {"Sally" 1, "Bob" 2, "Maurice" 4, "Susan" 3}
   {"Sally" 1, "Bob" 2, "Maurice" 4, "Susan" 3}
   {"Sally" 1, "Bob" 2, "Maurice" 4, "Susan" 3}])


