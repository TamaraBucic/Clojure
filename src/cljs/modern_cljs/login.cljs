(ns modern-cljs.login
  (:require [domina.core :refer [append! 
                                 by-class
                                 by-id 
                                 destroy! 
                                 set-value! 
                                 value]]
            [domina.events :refer [listen!]]))

;; define the function to be attached to form submission event
(defn validate-form []
  (if (and (> (count (value (by-id "email"))) 0)
           (> (count (value (by-id "password"))) 0))
    true
    (do (js/alert "Please, complete the form!")
        false)))

;; define the function to attach validate-form to onsubmit event of
(defn ^:export init []
  (if (and js/document
           (aget js/document "getElementById"))
    ;; get loginForm by element id and set its onsubmit property to
    ;; validate-form function
    (listen! (by-id "login") :click validate-form)))