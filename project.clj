(defproject ranked-choice "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2138"]
                 [compojure "1.1.6"]
                 [reagent "0.3.0"]
                 [com.facebook/react "0.8.0.1"]]

  :plugins [[lein-cljsbuild "1.0.2"]
            [lein-ring "0.8.8"]]

  :ring {:handler ranked-choice.core/handler}

  :source-paths ["src/clj", "src/cljs"]

  :cljsbuild {
    :repl-listen-port 9001
    :builds [{:id "ranked-choice"
              :source-paths ["src/cljs"]
              :compiler {
                :output-to "resources/public/js/ranked_choice.js"
                :output-dir "resources/public/out"
                :optimizations :none
                :source-map true
                :pretty-print true}}]}

  ; so people can try out the main algorithm for themselves:

  :repl-options {:init-ns ranked-choice.vote})
