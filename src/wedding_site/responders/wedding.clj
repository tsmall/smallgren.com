(ns wedding-site.responders.wedding
  (:require [hiccup.page :as h]
            [ring.util.response :as response]
            [wedding-site.routes.wedding :as r.wedding]
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

(defn- nav-bar
  "Navigation bar for the wedding pages."
  []
  [:nav
    [:ul.nav-bar
     [:li.nav-bar__item [:a.nav-bar__link {:href (r.wedding/home-path)} "Intro"]]
     [:li.nav-bar__item [:a.nav-bar__link {:href (r.wedding/story-path)} "Story"]]
     [:li.nav-bar__item [:a.nav-bar__link {:href (r.wedding/road-trip-path)} "Road Trip"]]]])

(defn home
  "Return the response for the wedding home page."
  []
  (page
   ;; TODO (TS 2016-05-30) Change to "We got married!" after Oct 8, 2016.
   "We're getting married!"
   (nav-bar)
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

(defn road-trip
  "Return the response for the road trip page."
  [receptions]
  (let [receptions-by-date (sort-by :day receptions)]
    (page
     "Road trip"
     (nav-bar)
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
         (let [slug-date (utils/date->slug (:day r))]
           [:li.item-list__item
            [:p.item-list__action
             [:a.link-button {:href (r.wedding/rsvp-path :day slug-date)} "RSVP"]]
            [:h1.item-list__heading (:city r) ", " (:state r)]
            [:h2.item-list__subhead (utils/formatted-date (:day r))]]))]])))

(defn reception
  "Return the response for an individual reception page."
  [reception]
  (let [slug-date (utils/date->slug (:day reception))]
    (page
     (str (:city reception) ", " (:state reception))
     (nav-bar)
     [:h2 (utils/formatted-date (:day reception))]
     [:main
      [:section#info
       [:p (:info reception)]]
      [:section#rsvp
       [:h3 "RSVP"]
       [:p
        "Fill out the form below to let us know if you can join us. "
        "Please feel free to bring friends."]
       [:form.vertical-form {:method "post" :action (r.wedding/new-rsvp-path :day slug-date)}
        (anti-forgery-field)
        [:div.vertical-form__field
         [:label.vertical-form__label {:for "name"} "Your name"]
         [:input.vertical-form__input {:type "text"
                                       :id "name"
                                       :name "name"
                                       :placeholder "John Doe"
                                       :autocomplete "name"
                                       :required "true"}]]
        [:div.vertical-form__field
         [:label.vertical-form__label {:for "email"} "Your email address"]
         [:input.vertical-form__input {:type "email"
                                       :id "email"
                                       :name "email"
                                       :placeholder "john@example.com"
                                       :autocomplete "email"
                                       :inputmode "email"
                                       :required "true"}]]
        [:div.vertical-form__field
         [:label.vertical-form__label {:for "attending"} "Can you come?"]
         [:select {:id "attending" :name "attending"}
          [:option {:value "yes"} "Yes!"]
          [:option {:value "no"} "Unfortunately, no."]]]
        [:div.vertical-form__field
         [:label.vertical-form__label {:for "plus_ones"} "How many people are you bringing?"]
         [:input.vertical-form__input_short {:type "number"
                                             :id "plus_ones"
                                             :name "plus_ones"
                                             :inputmode "numeric"
                                             :min "0"
                                             :max "10"
                                             :value "0"
                                             :required "true"}]]
        [:div.vertical-form__field
         [:input.vertical-form__input {:type "submit"
                                       :value "Send my RSVP"}]]]]])))

(defn story
  "Return the response for the wedding story page."
  []
  (page
   "Our story"
   (nav-bar)
   [:main
    [:img.full-bleed {:src "/r/photos/cafe-lalo.jpg"
                      :alt "Photo of Tom and Teresa outside Cafe Lalo"}]
    [:p
     (str "Tom and Teresa met each other the way all great couples since Tom Hanks and Meg Ryan meet: "
          "online, in New York, and having very likely crossed paths in the Upper West Side. "
          "Despite living only five blocks apart in 2011 and 2012, fate gave them another chance in early 2015. ")]
    [:p
     (str "Tom reached out on eHarmony just in time; Teresa had nearly given up on the idea of dating in The City. "
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
    [:p
     "Then they got a dog."]
    [:p
     "And here we are."]
    [:img.full-bleed {:src "/r/photos/he-said-yes.jpg"
                      :alt "Photo of Tom and Teresa. Tom is holding his hand up to show his ring."}]]))

(defn save-rsvp
  "Return the response for attempting to save an RSVP."
  [day]
  (response/redirect (r.wedding/rsvp-path :day day)
                     :see-other))
