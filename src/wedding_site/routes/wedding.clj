(ns wedding-site.routes.wedding
  (:require [clojurewerkz.route-one.core :refer [defroute]]))

(defroute home "/wedding")
(defroute registry "/wedding/registry")
(defroute road-trip "/wedding/road-trip")
(defroute rsvp "/wedding/road-trip/:day")
(defroute new-rsvp "/wedding/road-trip/:day/rsvps")
(defroute story "/wedding/story")
