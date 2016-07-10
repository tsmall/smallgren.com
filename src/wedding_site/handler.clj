(ns wedding-site.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [wedding-site.actions.wedding :as actions.wedding]
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

  ;; /wedding/*
  (GET r.wedding/home-template []
       (actions.wedding/view-home))
  (GET r.wedding/road-trip-template []
       (actions.wedding/view-road-trip))
  (GET r.wedding/rsvp-template [day]
       (actions.wedding/view-road-trip-stop day))
  (GET r.wedding/story-template []
       (actions.wedding/view-story))
  (POST r.wedding/new-rsvp-template [day name email attending plus_ones]
        (actions.wedding/save-rsvp day name email attending plus_ones))

  (route/resources "/r")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
