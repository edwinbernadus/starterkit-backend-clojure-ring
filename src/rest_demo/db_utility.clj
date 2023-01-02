(ns rest-demo.db-utility
  (:require
   [compojure.core :refer :all]
   [ring.middleware.defaults :refer :all]
   [clojure.pprint :as pp]
   [honey.sql :as sql]
   [rest-demo.db-utility :as db-utility]
   [honey.sql.helpers :refer :all :as h]
   [clojure.core :as c]
   [next.jdbc :as jdbc]
   [clojure.data.json :as json]))



(defn get-db-config []
  {:dbtype "postgresql"
   :dbname "hello_dev"
   :host "localhost"
   :user "postgres"
   :password "postgres"})

;; //sql_select_get_one
(defn get-one-row []
  (let [db-config (get-db-config)]
    (let [db (jdbc/get-datasource db-config)]
      (let [sql-map1 {:select [:id :title]
                      :from [:albums]
                      :limit 1}]
        (let [row0 (jdbc/execute-one! db (sql/format sql-map1))]

          (pp/pprint "result1")
          (pp/pprint (:albums/id row0))
          (pp/pprint "result2")
          (pp/pprint (:albums/title row0))

          (let [output {:id (:albums/id row0) :title (:albums/title row0)}]
            (pp/pprint "output")
            (pp/pprint output)
            {:id (:albums/id row0) :title (:albums/title row0)}))))))



;; //sql_select_list
(defn get-list-row []
  (let [db-config (get-db-config)]
    (let [db (jdbc/get-datasource db-config)]
      (let [sql-map1 {:select [:id :title]
                      :from [:albums]}]
        (let [items2 (jdbc/execute! db (sql/format sql-map1))]

          (pp/pprint "result all")
          (pp/pprint items2)

          (let [result (map (fn [x] {:id (:albums/id x) :title (:albums/title x)}) items2)]
            (pp/pprint "result all - 2")
            (pp/pprint result)
            result))))))


;; //sql_count
(defn get-total []
  (let [db-config (get-db-config)]
    (let [db (jdbc/get-datasource db-config)]
      (let [sql-map1 {:select
                      :%count.*
                      :from [:albums]}]
        (let [total (jdbc/execute-one! db (sql/format sql-map1))]

          (pp/pprint "result total")
          (pp/pprint total)
          total
          )))))


;; =======================

;; (defn output-5 []
;;   5)

;; (defn add-10 [x]
;;   (+ x 10))


;; (defn add-10-to-output-5 []
;;   (let [output (output-5)]
;;     (+ output 10)))

(defn get-number []
  5)



(defn get-number-two []
  (let [output (get-number)]
    (+ output 11)))



;; (defn get-number-3 []
;;   (let [input (get-number-2)]
;;     input))

;; (defn get-number-three []
;;   (def row0 (get-number))
;;   (def row1 row0)
;;   row1)

(defn get-number-three []
  (let [row0 (get-number)]
    (let [row1 row0]
      row1)))


(defn add-number [number1 number2]
  (+ number1 number2))
