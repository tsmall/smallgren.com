(ns wedding-site.responders.wedding
  (:require [hiccup.page :as h]
            [ring.util.response :as response]
            [wedding-site.routes.wedding :as r.wedding]
            [wedding-site.state :as state]
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
    (h/include-css "/r/styles/main.css")
    [:link {:rel "icon"
            :type "image/png"
            :href "/r/favicon.ico"}]]
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
     [:li.nav-bar__item [:a.nav-bar__link {:href (r.wedding/road-trip-path)} "Road Trip"]]
     [:li.nav-bar__item [:a.nav-bar__link {:href (r.wedding/registry-path)} "Registry"]]
     ]])

(defn home
  "Return the response for the wedding home page."
  []
  (page
   "We got married!"
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

(defn registry
  "Return the response for the registry page."
  []
  (page
   "Honeymoon registry"
   (nav-bar)
   [:main
    [:p
     "We live in a small New York apartment and have very little room for new possessions. "
     "In lieu of objects, if you would like, we have set up a registry through Wanderable.com for you to help us go on our honeymoon this coming winter."]
    [:p
     "This will be our first trip overseas together, "
     "and the first time for either of us to go to Scandinavia. "
     "We're excited for grand adventures and building new memories together."]
    [:p
     [:a.link-button {:href "https://www.wanderable.com/hm/smallgren"} "See our registry"]]]))

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

(defn- confirmation-message
  [reception user-rsvps]
  (if (state/user-has-rsvpd? user-rsvps reception)
    [:p.flash-message
     "Thank you for RSVPing."]
    nil))

(defn- rsvp-message
  [reception user-rsvps]
  (if (state/user-has-rsvpd? user-rsvps reception)
    [:p
     [:strong "We got your RSVP. "]
     "Thanks! "
     "If you need to change your RSVP, fill out the form again."]
    [:p
     "Fill out the form below to let us know if you can join us. "
     "Please feel free to bring friends."]))

(defn reception
  "Return the response for an individual reception page."
  [reception user-rsvps]
  (let [slug-date (utils/date->slug (:day reception))]
    (page
     (str (:city reception) ", " (:state reception))
     (nav-bar)
     (confirmation-message reception user-rsvps)
     [:h2 (utils/formatted-date (:day reception))]
     [:main
      [:section#info
       [:p (:info reception)]]
      [:section#rsvp
       [:h3 "RSVP"]
       (rsvp-message reception user-rsvps)
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
         [:label.vertical-form__label {:for "party_size"}
          "How many people will be attending in total?"]
         [:input.vertical-form__input_short {:type "number"
                                             :id "party_size"
                                             :name "party_size"
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
          "They eventually met for a date at Chelsea Market where they talked about puppets, ")
     [:em "Hamilton"]
     (str ", and Ruth Bader Ginsberg. "
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
  [reception user-rsvps]
  (let [day (:day reception)
        response (response/redirect (r.wedding/rsvp-path :day day) :see-other)]
    (state/add-user-rsvp-to-response response user-rsvps reception)))
