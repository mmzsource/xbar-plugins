#!/usr/local/bin/bb

;;
;; This script has 3 prerequisites:
;;
;; 1. Running on a mac
;; 2. Babashka installed - https://github.com/babashka/babashka#installation
;; 3. XBAR installed - https://github.com/matryer/xbar
;;
;; Put this script inside the XBAR plugin folder. Most likely to be found here:
;; ~/Library/Application Support/xbar/plugins
;;
;; Make sure the script has a name like weeknr.12h.clj where the .12h. is important
;; because that's an interface to XBAR telling it it needs to run every 12 hours.
;;

;; butlast to remove the newline at the end (the newline creates a new menu-item
;; in xbar and xbar will cycle between weeknr and a newline - not what we want)
;; apply str to then change all the loose chars into one string again.
(def weeknr (apply str (butlast (:out (shell/sh "date" "+(wk%V)")))))

(println (str weeknr
              "\n---\n"
              "calendar | href=https://www.calendar-365.com"))
