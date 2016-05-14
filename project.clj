(defproject wedding-site "0.1.0-SNAPSHOT"
  :description "Run your own wedding reception website."
  :url "https://github.com/tsmall/wedding-site"
  :min-lein-version "2.0.0"
  :main ^:skip-aot wedding-site.core
  :uberjar-name "wedding-site-standalone.jar"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/java.jdbc "0.6.0-rc2"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [org.postgresql/postgresql "9.4.1208.jre7"]
                 [ragtime "0.6.0"]
                 [ring/ring-defaults "0.2.0"]
                 [ring/ring-jetty-adapter "1.4.0"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler wedding-site.handler/app
         :main wedding-site.core}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}
   :ubarjar {:aot :all}})
