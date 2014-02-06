(ns ranked-choice.core
  (:use compojure.core)
  (:require [ring.util.response :as response]
            [compojure.handler :as handler]
            [compojure.route :as route]))

; defroutes macro defines a function that chines individual route
; functions together. The request map is passed to each functions in
; turn, until a non-nil response is returned

(defroutes app-routes
  ; to serve document root address
  (GET "/" []
       (response/resource-response "index.html"))
  ; to serve static pages saved in resources/public directory
  (route/resources "/")
  ; if pages is not found
  (route/not-found "Page not found mofo"))

(def handler
  (handler/site app-routes))

