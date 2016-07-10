(ns wedding-site.actions.home
  (:require [wedding-site.responders.home :as responders]))

(defn view-home
  "Return the site's home page resource."
  []
  (responders/home))
