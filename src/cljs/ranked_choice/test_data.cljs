(ns ranked-choice.test-data)

(def simple-tie
  [{"Bob" 1, "rainbows" 2}
   {"Bob" 2, "rainbows" 1}])

(def first-round-majority
  [{"Bob" 1, "rainbows" 2, "video games" 3}
   {"Bob" 1, "rainbows" 3, "video games" 2}
   {"Bob" 2, "rainbows" 1, "video games" 3}])

(def regular-ballots-no-tie
  [{"rainbows" 4, "puppies" 3, "video games" 1, "ice cream" 2}
   {"rainbows" 4, "puppies" 3, "video games" 1, "ice cream" 2}
   {"rainbows" 4, "puppies" 3, "video games" 1, "ice cream" 2}
   {"rainbows" 4, "puppies" 3, "video games" 1, "ice cream" 2}
   {"rainbows" 4, "puppies" 3, "video games" 1, "ice cream" 2}
   {"rainbows" 3, "puppies" 2, "video games" 4, "ice cream" 1}
   {"rainbows" 3, "puppies" 2, "video games" 4, "ice cream" 1}
   {"rainbows" 3, "puppies" 2, "video games" 4, "ice cream" 1}
   {"rainbows" 3, "puppies" 2, "video games" 4, "ice cream" 1}
   {"rainbows" 2, "puppies" 1, "video games" 4, "ice cream" 3}
   {"rainbows" 2, "puppies" 1, "video games" 4, "ice cream" 3}
   {"rainbows" 2, "puppies" 1, "video games" 4, "ice cream" 3}
   {"rainbows" 1, "puppies" 2, "video games" 4, "ice cream" 3}
   {"rainbows" 1, "puppies" 2, "video games" 4, "ice cream" 3}])

(def regular-ballots-tie
  [{"rainbows" 4, "puppies" 3, "video games" 1, "ice cream" 2}
   {"rainbows" 4, "puppies" 3, "video games" 1, "ice cream" 2}
   {"rainbows" 4, "puppies" 3, "video games" 1, "ice cream" 2}
   {"rainbows" 4, "puppies" 3, "video games" 1, "ice cream" 2}
   {"rainbows" 4, "puppies" 3, "video games" 1, "ice cream" 2}
   {"rainbows" 3, "puppies" 2, "video games" 4, "ice cream" 1}
   {"rainbows" 3, "puppies" 2, "video games" 4, "ice cream" 1}
   {"rainbows" 3, "puppies" 2, "video games" 4, "ice cream" 1}
   {"rainbows" 3, "puppies" 2, "video games" 4, "ice cream" 1}
   {"rainbows" 2, "puppies" 1, "video games" 4, "ice cream" 3}
   {"rainbows" 2, "puppies" 1, "video games" 4, "ice cream" 3}
   {"rainbows" 2, "puppies" 1, "video games" 4, "ice cream" 3}
   {"rainbows" 1, "puppies" 2, "video games" 4, "ice cream" 3}
   {"rainbows" 1, "puppies" 2, "video games" 4, "ice cream" 3}
   {"rainbows" 1, "puppies" 2, "video games" 4, "ice cream" 3}])

(def irregular-ballots
  [{"Twinkies" 1, "Jalapeños" 2}
   {"rainbows" 5, "puppies" 4, "video games" 2, "ice cream" 3, "Garbonzo Beans" 1}
   {"rainbows" 5, "puppies" 4, "video games" 2, "ice cream" 3, "Garbonzo Beans" 1}
   {"rainbows" 4, "puppies" 3, "video games" 1, "ice cream" 2}
   {"rainbows" 4, "puppies" 3, "video games" 1, "ice cream" 2}
   {"rainbows" 4, "puppies" 3, "video games" 1, "ice cream" 2}
   {"rainbows" 3, "puppies" 2, "video games" 4, "ice cream" 1}
   {"rainbows" 3, "puppies" 2, "video games" 4, "ice cream" 1}
   {"rainbows" 3, "puppies" 2, "video games" 4, "ice cream" 1}
   {"rainbows" 4, "puppies" 3, "video games" 5, "ice cream" 2, "Chilis" 1}
   {"rainbows" 2, "Bob" 1, "video games" 4, "ice cream" 3}
   {"rainbows" 2, "Bob" 1, "video games" 4, "ice cream" 3}
   {"rainbows" 2, "Bob" 1, "video games" 4, "ice cream" 3}
   {"rainbows" 1, "Bob" 2, "video games" 4, "ice cream" 3}
   {"rainbows" 1, "Bob" 2, "video games" 4, "ice cream" 3}
   {"rainbows" 1, "Bob" 2, "video games" 4, "ice cream" 3}])


