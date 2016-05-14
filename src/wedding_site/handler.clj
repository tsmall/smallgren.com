(ns wedding-site.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [wedding-site.view :as view]))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/receptions" [] (view/reception-list))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
