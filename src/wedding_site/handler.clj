(ns wedding-site.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [wedding-site.actions.admin :as actions.admin]
            [wedding-site.actions.home :as actions.home]
            [wedding-site.actions.wedding :as actions.wedding]
            [wedding-site.routes.home :as r.home]
            [wedding-site.routes.wedding :as r.wedding]
            [wedding-site.routes.wedding-admin :as r.admin]))

(defroutes app-routes
  ;; /*
  (GET r.home/home-template []
       (actions.home/view-home))

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

  ;; /wedding/a/*
  (GET r.admin/home-template []
       (actions.admin/view-home))
  (GET r.admin/reception-list-template []
       (actions.admin/view-receptions))
  (GET r.admin/new-reception-template []
       (actions.admin/view-new-reception-form))
  (GET r.admin/edit-reception-template [previous-day]
       (actions.admin/view-edit-reception-form previous-day))
  (POST r.admin/new-reception-template [city state day info]
        (actions.admin/create-reception city state day info))
  (POST r.admin/edit-reception-template [previous-day city state day info]
        (actions.admin/update-reception previous-day city state day info))

  (route/resources "/r")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
