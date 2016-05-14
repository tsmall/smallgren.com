(ns wedding-site.utils
  (:import java.text.SimpleDateFormat))

(defn formatted-date
  "Convert a Date into a reader-friendly string."
  [date]
  (let [format "E, MMM d, y"
        formatter (SimpleDateFormat. format)]
    (.format formatter date)))
