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

(def formatted-date
  "Convert a Date into a reader-friendly string."
  (partial date-to-string date-format-ui))
