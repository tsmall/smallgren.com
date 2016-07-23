(ns wedding-site.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [wedding-site.actions.admin :as actions.admin]
            [wedding-site.actions.home :as actions.home]
            [wedding-site.actions.wedding :as actions.wedding]
            [wedding-site.routes.home :as r.home]
            [wedding-site.routes.wedding :as r.wedding]
            [wedding-site.routes.wedding-admin :as r.admin]
            [wedding-site.state :as state]))

(defroutes app-routes
  ;; /*
  (GET r.home/home-template []
       (actions.home/view-home))

  ;; /wedding/*
  (GET r.wedding/home-template []
       (actions.wedding/view-home))
  (GET r.wedding/registry-template []
       (actions.wedding/view-registry))
  (GET r.wedding/road-trip-template []
       (actions.wedding/view-road-trip))
  (GET r.wedding/rsvp-template {{day :day} :params cookies :cookies}
       (actions.wedding/view-road-trip-stop day (state/get-user-rsvps cookies)))
  (GET r.wedding/story-template []
       (actions.wedding/view-story))
  (POST r.wedding/new-rsvp-template
        {{:keys [day name email attending plus_ones]} :params
         cookies :cookies}
        (actions.wedding/save-rsvp day name email attending plus_ones
                                   (state/get-user-rsvps cookies)))

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
  (POST r.admin/delete-reception-template [day]
        (actions.admin/delete-reception day))

  (route/resources "/r")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
