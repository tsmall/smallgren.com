(ns wedding-site.utils
  (:import java.text.SimpleDateFormat))

(def ^{:private true} date-format-slug "y-M-d")
(def ^{:private true} date-format-ui "E, MMM d, y")

(defn- date-to-string
  "Convert a Date into a string, using the given format string."
  [format date]
  (let [formatter (SimpleDateFormat. format)]
    (.format formatter date)))

(def date->slug
  "Convert a Date into a URL-friendly string."
  (partial date-to-string date-format-slug))

(defn slug->date
  "Convert a URL-friendly date into an actual date."
  [string]
  (let [formatter (SimpleDateFormat. date-format-slug)]
    (.parse formatter string)))

(def formatted-date
  "Convert a Date into a reader-friendly string."
  (partial date-to-string date-format-ui))

(defn pluralize
  "Adds an 's' to string if quantity is plural."
  [string quantity]
  (str string (when-not (= 1 quantity) "s")))

(defn label
  "Returns string labeling quantity with label, using simple
  pluralization rules."
  [quantity label]
  (str quantity " " (pluralize "RSVP" quantity)))
