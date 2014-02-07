(ns ranked-choice.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string]
            [cljs.core.async :refer [put! chan <!]]))


(defn index-of [pred coll]
  (first (keep-indexed (fn [idx item]
                         (when (pred item) idx))
                        coll)))

(defn by-id [id]
  (. js/document (getElementById id)))

(defn log [msg]
  (. js/console (log msg)))

(def app-state
  (atom
    {:winner ""
     :choices [["puppies" nil]
               ["rainbows" nil]
               ["ice cream" nil]
               ["waterfalls" nil]]}))

(defn deref-choices []
  (:choices @app-state))

(defn index-of-name [name]
  (index-of #(#{name} (first %))
            (deref-choices)))

(defn index-of-rank [rank]
  (index-of #(#{rank} (second %))
            (deref-choices)))

(defn first-nonconsecutive [coll-of-numbers]
  (loop [n 1
         ordered (sort coll-of-numbers)]
    (if (not= (first ordered) n)
      n
      (recur (inc n) (rest ordered)))))

(defn next-rank []
  (first-nonconsecutive (remove nil? (map second (deref-choices)))))

; (defn lowest-rank []
;   (apply min-key second (deref-choices)))

; (defn update-winner! []
;   (let [rank (lowest-rank)
;         choices (deref-choices)]
;     (log rank)
;     (log choices)
;     (swap! app-state assoc :winner (first (nth choices rank)))))


(defn update-choices! [f & args]
  (apply swap! app-state update-in [:choices] f args))

(defn update-choice! [f name & args]
  (let [idx (index-of-name name)]
    (update-choices! (fn [choices & args]
                       (update-in choices [idx] f args))
                     (cons name args))))

(defn add-rank! [name rank]
  (update-choice! (fn [choice name]
                    [(first choice) rank])
                  name
                  rank))

(defn remove-rank! [name]
  (update-choice! (fn [choice name]
                    [(first choice) nil])
                  name))

(defn toggle-rank! [name]
  (let [choices (deref-choices)]
    (if (second (nth choices (index-of-name name)))
      (remove-rank! name)
      (add-rank! name (next-rank)))
    ; (update-winner!)
    ))

(defn choice-list-component []
  [:div
   [:p "Current leader: " [:span (:winner @app-state)]]
   [:p "Make your selections below:"]
   [:ul
    (for [choice (deref-choices)]
      [:li {:on-click #(toggle-rank! (first choice))
            :style {:cursor "pointer"}}
       (str (first choice) " " (second choice))])]])


(defn main-page []
  [:div
   [:h1 "Welcome to the Instant Runoff Voterator!"]
   [choice-list-component]])

(defn mount [component element]
  (reagent/render-component [component] element))

(mount main-page (by-id "ranked-choice-root"))







; (def app-state
;   (atom
;     {:contacts
;      [{:first "Ben" :last "Bitdiddle" :email "benb@mit.edu"}
;       {:first "Alyssa" :middle-initial "P" :last "Hacker" :email "aphacker@mit.edu"}
;       {:first "Eva" :middle "Lu" :last "Ator" :email "eval@mit.edu"}
;       {:first "Louis" :last "Reasoner" :email "prolog@mit.edu"}
;       {:first "Cy" :middle-initial "D" :last "Effect" :email "bugs@mit.edu"}
;       {:first "Lem" :middle-initial "E" :last "Tweakit" :email "morebugs@mit.edu"}]}))

; (defn update-contacts! [f & args]
;   (apply swap! app-state update-in [:contacts] f args))

; (defn add-contact! [c]
;   (update-contacts! conj c))

; (defn remove-contact! [c]
;   (update-contacts! (fn [contacts c]
;                       (vec (remove #{c} contacts)))
;                     c))

; (defn parse-contact [contact-str]
;   (let [[first middle last :as parts] (string/split contact-str #"\s+")
;         [first last middle] (if (nil? last) [first middle] [first last middle])
;         middle (when middle (string/replace middle "." ""))
;         c (if middle (count middle) 0)]
;     (when (>= (reduce + (map #(if % 1 0) parts)) 2)
;       (cond-> {:first first :last last}
;         (= c 1)  (assoc :middle-initial middle)
;         (> c 1) (assoc :middle middle)))))

; (defn middle-name [{:keys [middle middle-initial]}]
;   (cond
;     middle (str " " middle)
;     middle-initial (str " " middle-initial ".")))

; (defn display-name [{:keys [first last] :as contact}]
;   (str last ", " first (middle-name contact)))



; (defn contact [c]
;   [:li
;    [:span (display-name c)]
;    [:button {:on-click #(remove-contact! c)}
;     "Delete"]])

; (def new-contact
;   (let [valu (reagent/atom "")]
;     (fn []
;       [:div
;        [:input {:type "text"
;                 :placeholder "Contact Name"
;                 :value @valu
;                 :on-change #(reset! valu (-> % .-target .-value))}]
;        [:button {:on-click #(when-let [c (parse-contact @valu)]
;                               (add-contact! c)
;                               (reset! val ""))}
;         "Add"]])))

; (defn contact-list []
;   [:div
;    [:h1 "Contact list"]
;    [:ul
;     (for [c (:contacts @app-state)]
;       [contact c])]
;    [new-contact]])



; ;------------------------------------------------------

; (defn main-page []
;   [:div
;    [:h1 "Reagent playground"]
;    [contact-list]])

; (defn mount [component element]
;   (reagent/render-component [component] element))

; (mount main-page (by-id "reagent-tut"))


