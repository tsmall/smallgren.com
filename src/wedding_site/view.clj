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
    [:input {:type "submit" :value "Create reception"}]]))

(defn admin-create-reception [city state day]
  (db/create-reception city state day)
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
      [:input {:type "submit" :value "Save Changes"}]])))

(defn update-reception [original-day city state new-day]
  (db/update-reception original-day city state new-day)
  (response/redirect (r.admin/reception-list-path) :see-other))

(defn- wedding-nav-bar
  "Navigation bar for the wedding pages."
  []
  [:nav
    [:ul.nav-bar
     [:li.nav-bar__item [:a.nav-bar__link {:href (r.wedding/home-path)} "Intro"]]
     [:li.nav-bar__item [:a.nav-bar__link {:href (r.wedding/story-path)} "Story"]]
     [:li.nav-bar__item [:a.nav-bar__link {:href (r.wedding/road-trip-path)} "Road Trip"]]]])

(defn wedding-home []
  (page
   ;; TODO (TS 2016-05-30) Change to "We got married!" after Oct 8, 2016.
   "We're getting married!"
   (wedding-nav-bar)
   [:img.full-bleed {:src "/r/photos/card.jpg"
                     :alt "Cartoon drawing of Teresa and Tom"}]
   [:section#intro
    [:p
     "Welcome to our wedding website. "
     "You can find out more information about "
     [:a {:href (r.wedding/road-trip-path)} "our road trip"]
     ". And, if you're curious, you can read "
     [:a {:href (r.wedding/story-path)} "our story"]
     "."]]))

(defn wedding-story []
  (page
   "Our story"
   (wedding-nav-bar)
   [:main
    [:img.full-bleed {:src "/r/photos/cafe-lalo.jpg"
                      :alt "Photo of Tom and Teresa outside Cafe Lalo"}]
    [:p
     (str "Tom and Teresa met each other the way all great couples since Tom Hanks and Meg Ryan meet: "
          "online, in New York, and having very likely crossed paths in the Upper West Side. "
          "Despite living only five blocks apart in 2011 and 2012, fate gave them another chance in early 2015. "
          "Tom reached out on eHarmony just in time; Teresa had nearly given up on the idea of dating in The City. "
          "He impressed her, having met Glen Close and sung with the New Kids on the Block. "
          "She surprised him, asking about the Winter Olympics instead of a monster flick. "
          "They eventually met for a date at Chelsea Market where they talked about puppets, Hamilton, and Ruth Bader Ginsberg. "
          "They have been nearly inseparable ever since.")]
    [:img.full-bleed {:src "/r/photos/niagara-falls.jpg"
                      :alt "Photo of Tom and Teresa in front of Niagara Falls"}]
    [:p
     (str "They moved together to a cozy apartment in Fort Greene, Brooklyn, in September. "
          "That same day they acquired a cat, Traila, who runs (around) the house. "
          "After months of morning coffee and crossword rituals, art openings, trips Upstate and out West, "
          "after seeing countless plays and musicals, Teresa came to the only natural conclusion: "
          "that she wanted to ask Tom to marry her.")]
    [:p
     "So she did."]
    [:p
     (str "On January 17, Tom surprised Teresa with a day full of adventures around Manhattan. "
          "But she surprised him, too, over drinks that night. "
          "He said yes.")]
    [:img.full-bleed {:src "/r/photos/he-said-yes.jpg"
                      :alt "Photo of Tom and Teresa. Tom is holding his hand up to show his ring."}]
    [:p
     "And here we are."]]))

(defn road-trip []
  (let [receptions (db/all-receptions)
        receptions-by-date (sort-by :day receptions)]
    (page
     "Road trip"
     (wedding-nav-bar)
     [:section#intro
      [:p
       "We're going on a road trip right after our wedding, "
       "so we can celebrate with our family and friends. "]
      [:p
       "We'll keep this page updated with the latest information. "
       "We hope you can join us!"]]
     [:section#reception-list
      [:ol.item-list
       (for [r receptions-by-date]
         [:li.item-list__item
          [:h1.item-list__heading (:city r) ", " (:state r)]
          [:h2.item-list__subhead (utils/formatted-date (:day r))]])]])))
