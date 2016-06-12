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
        ["SELECT DISTINCT city , state , day FROM reception WHERE day = ?"
         sql-date])))))

(defn update-reception
  "Change one or more pieces of data for an existing reception."
  [original-day city state new-day]
  (let [sql-date (slug->sql-date new-day)
        original-sql-date (slug->sql-date original-day)]
   (sql/with-db-connection [db spec]
     (sql/update! db :reception
                  {:city city, :state state, :day sql-date}
                  ["day = ?" original-sql-date]))))
