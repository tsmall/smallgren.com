(ns aws-utils
  (:require [cognitect.aws.client.api :as aws]))

(def s3 (aws/client {:api :s3}))
(aws/validate-requests s3 true)

(defn get-objects [bucket prefix]
  (aws/invoke s3 {:op :ListObjectsV2
                  :request {:Bucket bucket
                            :Prefix prefix}}))

(defn get-object-keys [bucket prefix]
  (->> (get-objects bucket prefix)
       :Contents
       (map :Key)))

(defn make-private [bucket key]
  (aws/invoke s3 {:op :PutObjectAcl
                  :request {:Bucket bucket
                            :Key key
                            :ACL "private"}}))

(defn make-all-objects-private [bucket prefix]
  (println bucket)
  (doseq [key (get-object-keys bucket prefix)]
    (println "  " key)
    (make-private bucket key)))

(comment
  (make-all-objects-private "smallgren-stories" "2015/")
  )
