(ns wedding-site.db
  (:require [clojure.java.jdbc :as sql]
            [wedding-site.utils :as utils]))

(def spec (or (System/getenv "DATABASE_URL")
              "postgresql://localhost:5432/wedding"))

(defn all-receptions
  "Get all of the receptions in the database."
  []
  (sql/with-db-connection [db spec]
    (sql/query db ["SELECT DISTINCT city , state , day FROM reception"])))

(defn- slug->sql-date
  "Convert a date string in the slug format to a SqlDate object."
  [date-slug]
  (-> date-slug
      utils/slug->date
      .getTime
      java.sql.Date.))

(defn reception-by-day
  "Get a single reception given the day it occurs on."
  [day]
  (let [sql-date (slug->sql-date day)]
    (sql/with-db-connection [db spec]
      (first
       (sql/query
        db
        ["SELECT DISTINCT city , state , day , info
          FROM reception WHERE day = ?"
         sql-date])))))

(defn create-reception
  "Create a new reception."
  [city state day info]
  (let [sql-date (slug->sql-date day)]
    (sql/with-db-connection [db spec]
      (sql/insert! db :reception
                   {:city city
                    :state state
                    :day sql-date
                    :info info}))))

(defn update-reception
  "Change one or more pieces of data for an existing reception."
  [original-day city state new-day info]
  (let [sql-date (slug->sql-date new-day)
        original-sql-date (slug->sql-date original-day)]
   (sql/with-db-connection [db spec]
     (sql/update! db :reception
                  {:city city
                   :state state
                   :day sql-date
                   :info info}
                  ["day = ?" original-sql-date]))))

(defn create-rsvp
  "Create a new RSVP record."
  [{:keys [city state name email attending plus-ones]}]
  (sql/with-db-connection [db spec]
    (sql/insert! db :rsvp
                 {:city city
                  :state state
                  :rsvp_time (now)
                  :guest_name name
                  :guest_email email
                  :attending attending
                  :plus_ones plus-ones})))

(defn- now
  "Get the current time, as a java.sql.Date object."
  []
  (-> (java.util.Date.)
      .getTime
      java.sql.Date.))
