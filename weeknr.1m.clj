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
;; Make sure the script has a name like weeknr.1m.clj where the .1m. is important
;; because that's an interface to XBAR telling it it needs to run every minute.
;;

;; butlast to remove the newline at the end (the newline creates a new menu-item
;; in xbar and xbar will cycle between weeknr and a newline - not what we want)
;; apply str to then change all the loose chars into one string again.

(def weeknr (apply str (butlast (:out (shell/sh "date" "+(wk%V)")))))

;; telling xbar that every line that is printed should have the monospaced font
;; 'Menlo Regular' and it should do no trimming. This in order to make the day
;; numbers, day names and weeknumbers line out properly.

(def month (clojure.string/replace (:out (shell/sh "ncal" "-w")) #"\n" " | font='Menlo Regular' trim=false\n"))

(println (str weeknr "\n---\n" month "---\n" "calendar | href=https://www.calendar-365.com"))
