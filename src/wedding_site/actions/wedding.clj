(ns wedding-site.actions.wedding
  (:require [wedding-site.db :as db]
            [wedding-site.responders.wedding :as responders]))

(defn view-home
  "Return the wedding home resource."
  []
  (responders/home))

(defn view-registry
  "Return the registry resource."
  []
  (responders/registry))

(defn view-road-trip
  "Return the wedding road trip resource."
  []
  (let [receptions (db/all-receptions)]
    (responders/road-trip receptions)))

(defn view-road-trip-stop
  "Return the resource for an individual road trip stop."
  [day-string user-rsvps]
  (let [reception (db/reception-by-day day-string)]
    (responders/reception reception user-rsvps)))

(defn view-story
  "Return the resource for the wedding story."
  []
  (responders/story))

(defn save-rsvp
  "Save a new RSVP and return a resource indicating the result."
  [day name email attending plus-ones user-rsvps]
  (let [is-attending (case attending
                       "yes" true
                       false)
        num-plus-ones (Integer. plus-ones)
        reception (db/reception-by-day day)]
    (db/create-rsvp (assoc reception
                           :name name
                           :email email
                           :attending is-attending
                           :plus-ones num-plus-ones))
    (responders/save-rsvp reception user-rsvps)))
