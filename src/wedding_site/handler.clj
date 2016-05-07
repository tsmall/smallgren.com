(ns wedding-site.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer :all]))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

(defn run
  [options]
  (let [options (merge {:port 8080 :join? true} options)]
    (jetty/run-jetty (var app) options)))

(defn -main
  [& [port]]
  (let [port (Integer. (or port
                           (System/getenv "PORT")
                           5000))]
    (run {:port port :join? false})))
