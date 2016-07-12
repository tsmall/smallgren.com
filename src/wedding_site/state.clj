(ns wedding-site.state
  (:require [clojure.edn]))

(defn get-user-rsvps
  "Get the current user's RSVPs, or nil if they don't have any."
  [cookies]
  (let [{rsvps "rsvps"} cookies]
    (clojure.edn/read-string (:value rsvps))))

(defn add-user-rsvp-to-response
  "Record that the user has RSVPd to the given reception, in addition to
  any receptions they have already RSVPd to."
  [response user-rsvps reception]
  (let [city-state ((juxt :city :state) reception)
        new-rsvps (conj (or user-rsvps #{}) city-state)]
    (assoc response :cookies {"rsvps" {:value (pr-str new-rsvps)
                                       :path "/wedding"
                                       :expires "Sun, 01 Jan 2017 00:00:00 GMT"}})))

(defn user-has-rsvpd?
  "Return boolean indicating whether user has RSVPd to the given reception."
  [user-rsvps reception]
  (let [city-state ((juxt :city :state) reception)]
    (contains? user-rsvps city-state)))
