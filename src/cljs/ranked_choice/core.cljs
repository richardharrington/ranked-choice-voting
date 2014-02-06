(ns ranked-choice.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string]
            [cljs.core.async :refer [put! chan <!]]))

(defn by-id [id]
  (. js/document (getElementById id)))


(def app-state
  (atom
    {:contacts
     [{:first "Ben" :last "Bitdiddle" :email "benb@mit.edu"}
      {:first "Alyssa" :middle-initial "P" :last "Hacker" :email "aphacker@mit.edu"}
      {:first "Eva" :middle "Lu" :last "Ator" :email "eval@mit.edu"}
      {:first "Louis" :last "Reasoner" :email "prolog@mit.edu"}
      {:first "Cy" :middle-initial "D" :last "Effect" :email "bugs@mit.edu"}
      {:first "Lem" :middle-initial "E" :last "Tweakit" :email "morebugs@mit.edu"}]}))

(defn update-contacts! [f & args]
  (apply swap! app-state update-in [:contacts] f args))

(defn add-contact! [c]
  (update-contacts! conj c))

(defn remove-contact [c]
  (update-contacts! (fn [contacts c]
                      (vec (remove #{c} contacts)))
                    c))



(defn some-component []
  [:div
   [:h3 "I am a component!"]
   [:p.someclass
    "I have " [:strong "bold"]
    [:span {:style {:color "red"}} " and red"]
    " text."]])

(defn calling-component []
  [:div "Parent component"
   [some-component]])

(defn child [props]
  [:p "Hi, I am "
   (:name props)])

(defn childcaller []
  [child {:name "Foo"}])


(defn mount [component]
  (reagent/render-component [component]
                            (.-body js/document)))

(mount calling-component)



