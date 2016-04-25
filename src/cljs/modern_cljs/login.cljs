
(ns modern-cljs.login
  (:require [domina.core :refer [append!
                                 by-class
                                 by-id
                                 destroy!
                                 prepend!
                                 value
                                 attr]]
            [domina.events :refer [listen! prevent-default]]
            [hiccups.runtime])
    (:require-macros [hiccups.core :refer [html]])
  )

(defn validate-email [email]
  (destroy! (by-class "email"))
  (if (not (re-matches (re-pattern (attr email :pattern)) (value email)))
    (do
      (prepend! (by-id "loginForm") (html [:div.help.email (attr email :title)]))
      false)
    true)
)

(defn validate-password [password]
  (destroy! (by-class "password"))
  (if (not (re-matches (re-pattern (attr password :pattern)) (value password)))
    (do
      (append! (by-id "loginForm") (html [:div.help.password (attr password :title)]))
      false)
    true)
 )
;; define the function to be attached to form submission event
;; if the value of the email or password is empty, prevent the form action from being fired, 
;; raise the alert window asking the user to enter the email and the password and finally return control to the form;
;; otherwise return true to pass control to the default action of the form.
(defn validate-form [evt]
  (let [email (by-id "email")
        password (by-id "password")
        email-val (value email)
        password-val (value password)]
    (if (or (empty? email-val) (empty? password-val))
      (do
        (destroy! (by-class "help"))
        (prevent-default evt)
        (append! (by-id "loginForm") 
                 (html [:div.help "Please complete the form"])))
      (if (and (validate-email email)
               (validate-password password))
        true
        (prevent-default evt)
      )
    )
  )
)


;; define the function to attach validate-form to onsubmit event of
(defn ^:export init []
  (if (and js/document
           (aget js/document "getElementById"))
    (let [email (by-id "email")
          password (by-id "password")]
    ;; get loginForm by element id and set its onsubmit property to
    ;; validate-form function
      (listen! (by-id "submit") :click (fn [evt] (validate-form evt)))
      (listen! email :blur (fn [evt] (validate-email email)))
      (listen! password :blur (fn [evt] (validate-password password))))
  )
)