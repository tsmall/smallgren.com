(ns wedding-site.migration
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [wedding-site.db :as db]))

(defn- config
  "The migration configuration."
  []
  {:datastore (jdbc/sql-database db/spec)
   :migrations (jdbc/load-resources "migrations")})

(defn migrate
  "Migrate the database to the latest version."
  []
  (repl/migrate (config)))
