(ns wedding-site.responders.home
  (:require [ring.util.response :as response]
            [wedding-site.routes.wedding :as r.wedding]))

(defn home
  "Return the response for the site's home page."
  []
  (response/redirect (r.wedding/home-path)
                     :see-other))
