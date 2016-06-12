(ns wedding-site.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [wedding-site.view :as view]))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/wedding/a" [] (view/admin-home))
  (GET "/wedding/a/receptions" [] (view/admin-reception-list))
  (GET "/wedding/a/receptions/:day" [day] (view/admin-reception day))
  (POST "/wedding/a/receptions/:previous-day" [previous-day city state day] (view/update-reception previous-day city state day))
  (GET "/wedding" [] (view/wedding-home))
  (GET "/wedding/road-trip" [] (view/road-trip))
  (GET "/wedding/story" [] (view/wedding-story))
  (route/resources "/r")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
