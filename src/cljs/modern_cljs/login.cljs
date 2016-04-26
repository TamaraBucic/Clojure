
(ns modern-cljs.login
  (:require [domina.core :refer [append!
                                 by-class
                                 by-id
                                 destroy!
                                 prepend!
                                 value
                                 attr
                                 log]]
            [domina.events :refer [listen! prevent-default]]
            [hiccups.runtime :as hiccupsrt]
            [modern-cljs.login.validators :refer [user-credential-errors]]
            [shoreleave.remotes.http-rpc :refer [remote-callback]]
    )
    (:require-macros  [hiccups.core :refer [html]]
                      [shoreleave.remotes.macros :as shore-macros])
  )

(defn validate-email-domain [email]
  (remote-callback :email-domain-errors
                   [email]
                   #(if %
                      (do
                        (prepend! (by-id "loginForm")
                                  (html [:div.help.email "The email domain doesn't exist."]))
                        false)
                      true)))

(defn validate-email [email]
  (destroy! (by-class "email"))
  (if-let [errors (:email (user-credential-errors (value email) nil))]
    (do
      (prepend! (by-id "loginForm") (html [:div.help.email(first errors)]))
      false
    )
    (validate-email-domain (value email))
  )
)

(defn validate-password [password]
  (destroy! (by-class "password"))
  (if-let [errors (:password (user-credential-errors (value password) nil))]
    (do
      (prepend! (by-id "loginForm") (html [:div.help.password(first errors)]))
      false
    )
    true
  )
 )
;; define the function to be attached to form submission event
;; if the value of the email or password is empty, prevent the form action from being fired, 
;; raise the alert window asking the user to enter the email and the password and finally return control to the form;
;; otherwise return true to pass control to the default action of the form.
(defn validate-form [evt email password]
  (if-let [{e-errs :email p-errs :password} (user-credential-errors (value email) (value password))]
    (if (or e-errs p-errs)
      (do
        (destroy! (by-class "help"))
        (prevent-default evt)
        (append! (by-id "loginForm") (html [:div.help "Please complete the form."]))
      )
      (prevent-default evt)
    )
    true
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
      (listen! (by-id "submit") :click (fn [evt] (validate-form evt email password)))
      (listen! email :blur (fn [evt] (validate-email email)))
      (listen! password :blur (fn [evt] (validate-password password))))
  )
)