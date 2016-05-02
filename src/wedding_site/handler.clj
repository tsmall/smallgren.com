(ns wedding-site.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

(defn run
  [options]
  (let [options (merge {:port 8080 :join? true} options)]
    (jetty/run-jetty (var app) options)))
