(ns wedding-site.db
  (:require [clojure.java.jdbc :as sql]))

(def spec (or (System/getenv "DATABASE_URL")
              "postgresql://localhost:5432/wedding"))

(defn all-receptions
  "Get all of the receptions in the database."
  []
  (sql/with-db-connection [db spec]
    (sql/query db ["SELECT DISTINCT city , state , day FROM reception"])))
