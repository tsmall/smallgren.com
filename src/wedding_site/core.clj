(ns wedding-site.core
  (:require [ring.adapter.jetty :as jetty]
            [wedding-site.handler :as handler]))

(defn run
  [options]
  (let [options (merge {:port 8080 :join? true} options)]
    (jetty/run-jetty (var handler/app) options)))

(defn -main
  [& [port]]
  (let [port (Integer. (or port
                           (System/getenv "PORT")
                           5000))]
    (run {:port port :join? false})))
