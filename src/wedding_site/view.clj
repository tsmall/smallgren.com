(ns wedding-site.view
  (:require [hiccup.page :as h]
            [wedding-site.db :as db]
            [wedding-site.utils :as utils]))

(defn- page
  "Template for a full HTML page."
  [title & body]
  (h/html5
   [:head
    [:meta {:charset "utf-8"}]
    [:title title]
    (h/include-css "/r/styles/main.css")]
   [:body
    [:header
     [:h1.page-title title]]
    body]))
  []
(defn road-trip []
  (let [receptions (db/all-receptions)
        receptions-by-date (sort-by :day receptions)]
    (page
     "Road trip"
     [:section#intro
      [:p
       "We're going on a road trip right after our wedding, "
       "so we can celebrate with our family and friends. "]
      [:p
       "We'll keep this page updated with the latest information. "
       "We hope you can join us!"]]
     [:section#reception-list
      [:ol.item-list
       (for [r receptions-by-date]
         [:li.item-list__item
          [:h1.item-list__heading (:city r) ", " (:state r)]
          [:h2.item-list__subhead (utils/formatted-date (:day r))]])]])))
