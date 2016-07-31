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
      {{:keys [day name email attending party_size]} :params
       cookies :cookies}
    (actions.wedding/save-rsvp day name email attending party_size
                               (state/get-user-rsvps cookies)))

  ;; /wedding/a/*
  (GET r.admin/login-template []
    (actions.admin/view-login))
  (POST r.admin/login-template [password]
    (actions.admin/login password))
  (GET r.admin/home-template {session :session}
    (actions.admin/view-home session))
  (GET r.admin/reception-list-template {session :session}
    (actions.admin/view-receptions session))
  (GET r.admin/new-reception-template {session :session}
    (actions.admin/view-new-reception-form session))
  (GET r.admin/edit-reception-template
      {{:keys [previous-day]} :params
       session :session}
    (actions.admin/view-edit-reception-form session previous-day))
  (GET r.admin/rsvps-template
      {{:keys [state city]} :params
       session :session}
    (actions.admin/view-rsvps-for-reception session state city))
  (POST r.admin/new-reception-template
      {{:keys [city state day info]} :params
       session :session}
    (actions.admin/create-reception session city state day info))
  (POST r.admin/edit-reception-template
      {{:keys [previous-day city state day info]} :params
       session :session}
    (actions.admin/update-reception
     session previous-day city state day info))
  (POST r.admin/delete-reception-template
      {{:keys [day]} :params
       session :session}
    (actions.admin/delete-reception session day))

  (route/resources "/r")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
