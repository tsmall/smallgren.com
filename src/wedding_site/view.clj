(ns wedding-site.view
  (:require [hiccup.page :as h]
            [wedding-site.db :as db]
            [wedding-site.utils :as utils]))

(defn reception-list
  "Show all of the receptions."
  []
  (let [receptions (db/all-receptions)
        receptions-by-date (sort-by :day receptions)]
    (h/html5
     [:head
      [:meta {:charset "utf-8"}]
      [:title "Receptions"]]
     [:body
      [:h1 "Receptions"]
      [:table
       [:thead
        [:tr
         [:th "When"]
         [:th "Where"]]]
       [:tbody
        (for [r receptions-by-date]
          [:tr
           [:td (utils/formatted-date (:day r))]
           [:td (:city r) ", " (:state r)]])]]])))
