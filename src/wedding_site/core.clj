(ns wedding-site.core
  (:gen-class :main true)
  (:require [ring.adapter.jetty :as jetty]
            [wedding-site.handler :as handler]
            [wedding-site.migration :as migration]))

(defn run
  [options]
  (let [options (merge {:port 8080 :join? true} options)]
    (jetty/run-jetty (var handler/app) options)))

(defn -main
  []
  (migration/migrate)
  (let [port (Integer. (or (System/getenv "PORT") "8080"))]
    (run {:port port
          :join? true})))
