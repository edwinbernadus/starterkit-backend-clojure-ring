(ns rest-demo.core
  ;; (refer-clojure :exclude '[filter for group-by into partition-by set update])
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [honey.sql :as sql]
            [rest-demo.db-utility :as db-utility]
            ;; [next.jdbc.result-set :as rs]
            [honey.sql.helpers :refer :all :as h]
            [clojure.core :as c]
            ;; [honey.sql :as sql]
            [next.jdbc :as jdbc]
            ;; [honeysql.helpers :refer :all :as helpers]
            [clojure.data.json :as json])
  (:gen-class))
;; (def ds-opts (jdbc/with-options ds {:builder-fn rs/as-unqualified-lower-maps}))


(defn print_get_number []
  (pp/pprint "get-number-two")
  (pp/pprint (db-utility/get-number-two))
;; (pp/pprint (db-utility/add-10-to-output-5))


  (pp/pprint "get-number-three")
  (pp/pprint (db-utility/get-number-three)))
;; (print_get_number)


;; (logic-db)

(defn print_get_db_util []
  (pp/pprint "----")
  (pp/pprint "test3 get-one-row")
  (def test3 (db-utility/get-one-row))
  (pp/pprint "result from function - test3")
  (pp/pprint test3)
  (pp/pprint "test3 get-one-row [end]")


  (pp/pprint "----")
  (pp/pprint "test4 get-list-row")
  (def test4 (db-utility/get-list-row))
  (pp/pprint "result from function - test4")
  (pp/pprint test4)
  (pp/pprint "test4 get-list-row [end]"))
;; (print_get_db_util)

; Simple Body Page
(defn simple-body-page [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Hello World"})



(defn db-count-item [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (->>
             (let [item (db-utility/get-total)]
               (if item
                 (str (json/write-str (:count item)))
                 (str "no result"))))})

(defn db-get-one [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (->>
             (let [item (db-utility/get-one-row)]
               (if item
                 (str (json/write-str item))
                 (str "no result"))))})

(defn db-get-all [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (->>
             (let [item (db-utility/get-list-row)]
               (if item
                 (str (json/write-str item))
                 (str "no result"))))})

;; //get
; request-example
(defn request-example [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (->>
             (pp/pprint req)
             (str "Request Object: " req))})


;; //get_header
(defn get-auth-header [req]
  (let [headers (:headers req)]
    (pp/pprint "authorization1")
    (pp/pprint headers)
    (pp/pprint "authorization1a")
    (pp/pprint (get headers :authorization))
    (pp/pprint (get headers "authorization"))
    (let [authorization_result (get headers "authorization")]
      (pp/pprint "authorization2")
      (pp/pprint authorization_result)
      (if authorization_result
        {:body authorization_result}
        {:body "no authorization header"}))))

(def x2 {:a 1 :b 2})
(defn test-body-handler [req]
  {:status  200
   :body    (->
             (pp/pprint req)
             (str (json/write-str x2)))})


;; //post
(defn test-body-handler2 [req]
  (pp/pprint "body-handler-1")
  (pp/pprint req)
  (let [body (slurp (:body req))]
    (if body
      {:status  201
       :body (str body)}
      {:body "no content body"})))


;; {
;;     "amount" : 12421.3213,
;;     "requestRate" : 11.24141251251,
;;     "name" : "hello world",
;;     "number1" : 789
;; }

;; //post_body_to_model
(defn test-body-handler3 [req]
  (let [body2 (slurp (:body req))]
    (let [json-string (str body2)]
      (let [json-object (json/read-str json-string)]
        (let [item1 (get json-object "name")]
          (let [item2 (get json-object "number1")]
            (let [item3 (str item1 " - " item2)]
              (if body2
                {:status  201
                 :body item3}
                {:body "no content body"}))))))))
;; //routing_id
(defn hello-name [req] ;(3)
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (->
             (pp/pprint req)
             (str "Hello " (:name (:params req))))})

; my people-collection mutable collection vector
(def people-collection (atom []))

;Collection Helper functions to add a new person
(defn addperson [firstname surname]
  (swap! people-collection conj {:firstname (str/capitalize firstname) :surname (str/capitalize surname)}))
; Example JSON objects
(addperson "Functional" "Human")
(addperson "Micky" "Mouse")

; Get the parameter specified by pname from :params object in req
(defn getparameter [req pname] (get (:params req) pname))

; Return List of People
(defn people-handler [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
  ;;  //json_convert
   :body    (str (json/write-str @people-collection))})



(def item-one (atom []))
(swap! item-one conj {:a 1 :b 2})
(swap! item-one conj {:a 1 :b 2})
(swap! item-one conj {:a 1 :b 2})


(defn test-handler [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (-> (str (json/write-str x2)))})

; Add a new person into the people-collection
(defn addperson-handler [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (-> (let [p (partial getparameter req)]
                  (str (json/write-str (addperson (p :firstname) (p :surname))))))})

(defroutes app-routes
  (GET "/" [] simple-body-page)
  (GET "/request" [] request-example)
  (GET "/hello" [] hello-name)
  (GET "/test" [] test-handler)
  (GET "/db_get_one" [] db-get-one)
  (GET "/db_get_all" [] db-get-all)
  (GET "/db_total" [] db-count-item)
  (POST "/test-post" [] test-body-handler)
  (POST "/test-post2" [] test-body-handler2)
  (POST "/test-post3" [] test-body-handler3)
  (GET "/people" [] people-handler)
  (GET "/people/add" [] addperson-handler)
  (GET "/test-header" [] get-auth-header)

  (route/not-found "Error, page not found!"))


(defn test_number []
  (let [numbers [5 4 1 3 9 8 6 7 2 0]
        nums-plus-one (map inc numbers)]
    (pp/pprint "nums-plus-one")
    (pp/pprint nums-plus-one))

  (let [numbers [5 4 1 3 9 8 6 7 2 0]
        nums-plus-one (map (fn [x] (db-utility/add-number x 11)) numbers)]
    (pp/pprint "nums-plus-one test2")
    (pp/pprint nums-plus-one)))
;; (test_number)


;; (defn -main
;;   "I don't do a whole lot ... yet."
;;   [& args]
;;   (println "Hello, World!"))
(defn -main
  "This is our main entry point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    ; Run the server with Ring.defaults middleware

    (server/run-server (wrap-defaults #'app-routes api-defaults) {:port port})
    ;; (server/run-server (wrap-defaults #'app-routes site-defaults) {:port port})
    ; Run the server without ring defaults
    ;; (server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))