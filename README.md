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
     {"Sally" 1, "Bob" 2, "Maurice" 4, "Susan" 3}
     {"Sally" 1, "Bob" 2, "Maurice" 4, "Susan" 3}])

In the first round, here are the number of votes that each candidate gets:

     Maurice 5
     Susan 4
     Bob 3
     Sally 3

But in the end, Bob wins.

This sample data is structured to demonstrate the advantages of a ranked-choice voting system. In a typical first-past-the-post election (like a U.S. presidential election), Maurice would win with only 27% of the vote. In a normal runoff-voting system, Maurice and Susan would have an expensive runoff, and one of them would win.

But in ranked-choice voting, the election goes to Bob. He is the only person who is everyone's first or second choice, so he is clearly preferred most strongly by the most number of people.

Note: there is one thing I have added here that is not in the standard description of the ranked-choice algorithm: in case of a tie for loser in any given round, I have it check those candidates' votes in the next round, and then the next, recursively until it reaches every voter's last picks, in which case if there's still a tie it picks randomly.

None of this would ever be a concern in real political elections, but it becomes an issue in cases of a high voter to candidate ratio, like eight members of a book club picking a book to read from ten nominated books. It's quite common then to end up with a situation where in any given round, many candidates get the same number of votes, like zero or one.

So in that case it's helpful to be able to look into the future to the later rounds, which is something the classic ranked-choice voting method avoids, probably for the sake of simplicity and ease of comprehension. One could argue that the best way to take into account all the information on the ballots at the same time is just to average all the rankings, but no one seems to be suggesting doing that in elections. The standard version of the ranked-choice algorithm can be conducted entirely by counting and crossing names off ballots. Once you get into using a mathematical formula to determine the winner of a political election -- even a formula as simple as an average of rankings -- you've kind of gone beyond what most people feel comfortable trusting their electoral authorities with.

## License

Copyright © 2014 Richard Harrington

Distributed under the Eclipse Public License, the same as Clojure.
