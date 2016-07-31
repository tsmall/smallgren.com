(ns wedding-site.responders.admin
  (:require [hiccup.page :as h]
            [ring.util.response :as response]
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

(defn home
  "Return the response for the admin home page."
  []
  (page
   "Admin // Home"
   [:nav
    [:ul.item-list
     [:li.item-list__item
      [:a {:href (r.admin/reception-list-path)}
       [:p.item-list__text "Manage receptions"]]]]]))

(defn reception-list
  "Return the response for the admin reception list."
  [receptions]
  (page
   "Admin // Receptions"
   (let [receptions-by-date (sort-by :day receptions)]
     [:section
      [:p
       [:a.link-button
        {:href (r.admin/new-reception-path)}
        "Add a new reception"]]
      [:ol.item-list
       (for [r receptions-by-date]
         [:li.item-list__item
          [:p.item-list__action
           [:a.link-button
            {:href (r.admin/single-reception-path
                    :day (utils/date->slug (:day r)))}
            "Edit"]]
          [:p.item-list__action
           [:a.link-button
            {:href (r.admin/rsvps-path
                    :city (:city r)
                    :state (:state r))}
            (utils/label (or (:num-rsvps r) 0) "RSVP")]]
          [:h1.item-list__heading (:city r) ", " (:state r)]
          [:h2.item-list__subhead (utils/formatted-date (:day r))]])]])))

(defn- labeled-inputs [field-specs]
  (for [{:keys [id label type value]} field-specs]
    [:div.vertical-form__field
     [:label.vertical-form__label {:for id} (str label " ")]
     [:input.vertical-form__input {:type type :id id :name id :value value}]]))

(defn- reception-form-page
  "Render the HTML for the reception form."
  [{:keys [title action-text values url]}]
  (page
   title
   [:form.vertical-form {:method "post" :action url}
    (anti-forgery-field)
    (labeled-inputs
     [{:id "city", :label "City:", :type "text", :value (:city values)}
      {:id "state", :label "State:", :type "text", :value (:state values)}
      {:id "day", :label "Day:", :type "date" :value (:day values)}])
    [:div.vertical-form__field
     [:label.vertical-form__label {:for "info"} "Info:"]
     [:textarea.vertical-form__input {:id "info" :name "info"} (:info values)]]
    [:div.vertical-form__field
     [:input.vertical-form__input {:type "submit" :value action-text}]]]
   (if (seq values)
     [:form.vertical-form {:method "post"
                           :action (r.admin/delete-reception-path
                                    :day (:day values))}
      (anti-forgery-field)
      [:div.vertical-form__field
       [:input.vertical-form__input_dangerous
        {:type "submit" :value "Delete reception"}]]])))

(defn reception-form
  "Return the response for the create or edit reception form."
  ([]
   (reception-form-page {:title "Admin // New reception"
                         :action-text "Create reception"
                         :values {}
                         :url (r.admin/new-reception-path)}))
  ([reception]
   (reception-form-page {:title "Admin // Edit reception"
                         :action-text "Update reception"
                         :values reception
                         :url (r.admin/edit-reception-path
                               :previous-day (:day reception))})))

(defn- redirect-to-reception-list
  []
  (response/redirect (r.admin/reception-list-path)
                     :see-other))

(def create-reception
  "Return the response for attempting to create a reception."
  redirect-to-reception-list)

(def update-reception
  "Return the response for attempting to update a reception."
  redirect-to-reception-list)

(def delete-reception
  "Return the response for attempting to delete a reception."
  redirect-to-reception-list)

(defn unauthenticated
  "Return the response for an unauthenticated user."
  []
  (response/redirect (r.admin/login-path)
                     :see-other))

(defn login-form
  "Returns the response for the admin login form."
  []
  (page
   "Admin // Login"
   [:form.vertical-form {:method "post" :action ""}
    (anti-forgery-field)
    (labeled-inputs
     [{:id "password", :label "Password:", :type "password", :value ""}])
    [:div.vertical-form__field
     [:input.vertical-form__input {:type "submit" :value "Log in"}]]]))

(defn login-successful
  "Returns the response for a successful login."
  []
  (-> (response/redirect (r.admin/home-path) :see-other)
      (assoc :session {:authenticated true})))

(defn- rsvp-item
  "Render the HTML for a single RSVP item."
  [rsvp]
  [:li.item-list__item
   [:h1.item-list__heading (:guest-name rsvp)]
   [:h2.item-list__subhead
    (str (utils/label (:party-size rsvp) "peep") " in party")]
   [:p.item-list__body
    [:a {:href (str "mailto:" (:guest-email rsvp))} (:guest-email rsvp)]]])

(defn rsvp-details
  "Returns the RSVP details in HTML format."
  [rsvp-info]
  (page
   "Admin // RSVPs"
   [:h2 (str (:city rsvp-info) ", " (:state rsvp-info))]
   [:table
    [:tbody
     [:tr
      [:th "RSVPs:"]
      [:td (:num-rsvps rsvp-info)]]
     [:tr
      [:th "Attendees:"]
      [:td (:num-attending rsvp-info)]]]]
   (let [split-rsvps (group-by :attending (:rsvps rsvp-info))
         yeses (sort-by :guest-name (get split-rsvps true))
         nos (sort-by :guest-name (get split-rsvps false))]
     [:section {:id "rsvp-details"}
      [:h2 "Yeses"
       [:ol.item-list
        (map rsvp-item yeses)]]
      [:h2 "Nos"
       [:ol.item-list
        (map rsvp-item nos)]]])))
