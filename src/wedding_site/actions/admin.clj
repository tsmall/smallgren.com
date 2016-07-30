(ns wedding-site.actions.admin
  (:require [wedding-site.db :as db]
            [wedding-site.responders.admin :as responders]))

(defn- authenticated?
  "Returns true if the current session is authenticated."
  [session]
  (contains? session :authenticated))

(defn view-login
  "Returns the admin login resource."
  []
  (responders/login-form))

(defn login
  "Attempts to log in the user."
  [password]
  (let [secret-key (or (System/getenv "ADMIN_SECRET")
                       "secret")]
    (if (= password secret-key)
      (responders/login-successful)
      (responders/unauthenticated))))

(defn view-home
  "Return the admin section's home page resource."
  [session]
  (if (authenticated? session)
    (responders/home)
    (responders/unauthenticated)))

(defn view-receptions
  "Return the admin reception list resource."
  [session]
  (if (authenticated? session)
    (let [rsvp-stats (db/rsvp-stats)
          receptions (db/all-receptions)]
      (responders/reception-list
       (map
        #(merge % (db/stats-for-reception rsvp-stats
                                          :city (:city %)
                                          :state (:state %)))
        receptions)))
    (responders/unauthenticated)))

(defn view-new-reception-form
  "Return the admin form for creating a new reception."
  [session]
  (if (authenticated? session)
    (responders/reception-form)
    (responders/unauthenticated)))

(defn view-edit-reception-form
  "Return the admin form for editing a reception."
  [session day]
  (if (authenticated? session)
    (let [reception (db/reception-by-day day)]
      (responders/reception-form reception))
    (responders/unauthenticated)))

(defn create-reception
  "Create a new reception."
  [session city state day info]
  (if (authenticated? session)
    (do
      (db/create-reception city state day info)
      (responders/create-reception))
    (responders/unauthenticated)))

(defn update-reception
  "Update an existing reception."
  [session previous-day city state new-day info]
  (if (authenticated? session)
    (do
      (db/update-reception previous-day city state new-day info)
      (responders/update-reception))
    (responders/unauthenticated)))

(defn delete-reception
  "Delete a reception."
  [session day]
  (if (authenticated? session)
    (do
      (db/delete-reception day)
      (responders/delete-reception))
    (responders/unauthenticated)))
