# ranked-choice-voting

An implementation of a [ranked-choice (or instant-runoff)](http://en.wikipedia.org/wiki/Instant-runoff_voting) voting algorithm.

## Installation

1. Install [leiningen](https://github.com/technomancy/leiningen).
2. `git clone git@github.com:richardharrington/ranked-choice-voting.git`
3. `cd ranked-choice-voting`

## Usage

1. `lein repl`
2. `(demo)`
3. (optional, see the data in test_data.clj) `(vote <name-of-data-set> <verbose?>)`

When you run `(demo)`, you should get a printout of the algorithm running on the following sample data (numbers are rankings on each ballot):

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

In the first round, here are the number of votes that each candidate gets:

     Maurice 5
     Susan 4
     Bob 3
     Sally 2

But in the end, Bob wins.

This sample data is structured to demonstrate the advantages of a ranked-choice voting system. In a typical first-past-the-post election (like a U.S. presidential election), Maurice would win with only 27% of the vote. In a normal runoff-voting system, Maurice and Susan would have an expensive runoff, and one of them would win.

But in ranked-choice voting, the election goes to Bob. He is the only person who is everyone's first or second choice. Even without getting into the math, he is clearly preferred most strongly by the most number of people.

## License

Copyright © 2014 Richard Harrington

Distributed under the Eclipse Public License, the same as Clojure.
