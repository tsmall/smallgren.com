(ns wedding-site.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [wedding-site.routes.core :as r.core]
            [wedding-site.routes.wedding :as r.wedding]
            [wedding-site.routes.wedding-admin :as r.admin]
            [wedding-site.view :as view]))

(defroutes app-routes
  (GET r.core/home-template [] "Hello World")
  (GET r.admin/home-template [] (view/admin-home))
  (GET r.admin/reception-list-template [] (view/admin-reception-list))
  (GET r.admin/new-reception-template [] (view/admin-new-reception))
  (POST r.admin/new-reception-template [city state day info] (view/admin-create-reception city state day info))
  (GET r.admin/single-reception-template [day] (view/admin-reception day))
  (POST r.admin/edit-reception-template [previous-day city state day info] (view/update-reception previous-day city state day info))
  (GET r.wedding/home-template [] (view/wedding-home))
  (GET r.wedding/road-trip-template [] (view/road-trip))
  (GET r.wedding/rsvp-template [day] (view/rsvp day))
  (POST r.wedding/new-rsvp-template [day name email attending plus_ones] (view/record-rsvp day name email attending plus_ones))
  (GET r.wedding/story-template [] (view/wedding-story))
  (route/resources "/r")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
