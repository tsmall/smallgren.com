(ns wedding-site.view
  (:require [hiccup.page :as h]
            [ring.util.response :as response]
            [wedding-site.db :as db]
            [wedding-site.routes.wedding :as r.wedding]
            [wedding-site.routes.wedding-admin :as r.admin]
            [wedding-site.utils :as utils])
  (use ring.util.anti-forgery))

(defn- page
  "Template for a full HTML page."
  [title & body]
  (h/html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]
    [:title title]
    (h/include-css "/r/styles/main.css")]
   [:body
    [:header
     [:h1.page-title title]]
    body]))

(defn admin-home []
  (page
   "Admin // Home"
   [:nav
    [:ul.item-list
     [:li.item-list__item
      [:a {:href (r.admin/reception-list-path)}
       [:p.item-list__text "Manage receptions"]]]]]))

(defn admin-reception-list []
  (page
   "Admin // Receptions"
   (let [receptions (db/all-receptions)
         receptions-by-date (sort-by :day receptions)]
     [:section
      [:p
       [:a {:href (r.admin/new-reception-path)} "Add a new reception"]]
      [:ol.item-list
       (for [r receptions-by-date]
         [:li.item-list__item
          [:a {:href (r.admin/single-reception-path :day (utils/date->slug (:day r)))}
           [:h1.item-list__heading (:city r) ", " (:state r)]
           [:h2.item-list__subhead (utils/formatted-date (:day r))]]])]])))

(defn- labeled-inputs [field-specs]
  (for [{:keys [id label type value]} field-specs]
    [:div
     [:label {:for id} (str label " ")]
     [:input {:type type :id id :name id :value value}]]))

(defn admin-new-reception []
  (page
   "Admin // New reception"
   [:form {:method "post" :href (r.admin/new-reception-path)}
    (anti-forgery-field)
    (labeled-inputs
     [{:id "city", :label "City:", :type "text"}
      {:id "state", :label "State:", :type "text"}
      {:id "day", :label "Day:", :type "date"}])
    [:div
     [:label {:for "info"} "Info:"]
     [:textarea {:id "info" :name "info"}]]
    [:input {:type "submit" :value "Create reception"}]]))

(defn admin-create-reception [city state day info]
  (db/create-reception city state day info)
  (response/redirect (r.admin/reception-list-path)))

(defn admin-reception [day]
  (page
   "Admin // Edit reception"
   (let [reception (db/reception-by-day day)
         slug-date (utils/date->slug (:day reception))
         post-url (r.admin/edit-reception-path :previous-day slug-date)]
     [:form {:method "post" :href post-url}
      (anti-forgery-field)
      (labeled-inputs
       [{:id "city", :label "City:", :type "text", :value (:city reception)}
        {:id "state", :label "State:", :type "text", :value (:state reception)}
        {:id "day", :label "Day:", :type "date", :value (:day reception)}])
      [:div
       [:label {:for "info"} "Info:"]
       [:textarea {:id "info" :name "info"} (:info reception)]]
      [:input {:type "submit" :value "Save Changes"}]])))

(defn update-reception [original-day city state new-day info]
  (db/update-reception original-day city state new-day info)
  (response/redirect (r.admin/reception-list-path) :see-other))

(defn- wedding-nav-bar
  "Navigation bar for the wedding pages."
  []
  [:nav
    [:ul.nav-bar
     [:li.nav-bar__item [:a.nav-bar__link {:href (r.wedding/home-path)} "Intro"]]
     [:li.nav-bar__item [:a.nav-bar__link {:href (r.wedding/story-path)} "Story"]]
     [:li.nav-bar__item [:a.nav-bar__link {:href (r.wedding/road-trip-path)} "Road Trip"]]]])
