(ns wedding-site.actions.admin
  (:require [wedding-site.db :as db]
            [wedding-site.responders.admin :as responders]))

(defn view-home
  "Return the admin section's home page resource."
  []
  (responders/home))

(defn view-receptions
  "Return the admin reception list resource."
  []
  (let [receptions (db/all-receptions)]
    (responders/reception-list receptions)))

(defn view-new-reception-form
  "Return the admin form for creating a new reception."
  []
  (responders/reception-form))

(defn view-edit-reception-form
  "Return the admin form for editing a reception."
  [day]
  (let [reception (db/reception-by-day day)]
    (responders/reception-form reception)))

(defn create-reception
  "Create a new reception."
  [city state day info]
  (db/create-reception city state day info)
  (responders/create-reception))

(defn update-reception
  "Update an existing reception."
  [previous-day city state new-day info]
  (db/update-reception previous-day city state new-day info)
  (responders/update-reception))
