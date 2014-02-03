# ranked-choice-voting

An implementation of a ranked-choice (or instant-runoff) voting algorithm.

## Installation

1. Install [leiningen](https://github.com/technomancy/leiningen).
2. `git clone git@github.com:richardharrington/ranked-choice-voting.git`
3. `cd ranked-choice-voting`

## Usage

1. `lein repl`
2. `(vote data true)`

You should get a printout of the algorithm running on the sample data, which is structured to demonstrate the advantages of a ranked-choice voting system. In a typical first-past-the-post election (like a U.S. presidential election), Maurice would win with only 27% of the vote. In a normal runoff-voting system, Maurice and Susan would have an expensive runoff, and one of them would win.

But in ranked-choice voting, the election goes to Bob, who is clearly preferred most strongly by the most number of people.

## License

Copyright © 2014 Richard Harrington

Distributed under the Eclipse Public License, the same as Clojure.
