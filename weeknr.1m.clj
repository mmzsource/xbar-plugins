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

(defn emoji-of [number-char]
  (let [emojis ["0️⃣" "1️⃣" "2️⃣" "3️⃣" "4️⃣" "5️⃣" "6️⃣" "7️⃣" "8️⃣" "9️⃣"]]
    (nth emojis (Character/getNumericValue number-char))))

(defn emoji-fy [number-string]
  (apply str (mapcat emoji-of number-string)))

;; butlast to remove the newline at the end (the newline creates a new menu-item
;; in xbar and xbar will cycle between weeknr and a newline - not what we want)
;; apply str to then change all the loose chars into one string again.

(def weeknr
  (let [weeknr (apply str (butlast (:out (shell/sh "date" "+%V"))))]
    (emoji-fy weeknr)))

;; telling xbar that every line that is printed should have the monospaced font
;; 'Menlo Regular' and it should do no trimming. This in order to make the day
;; numbers, day names and weeknumbers line out properly.

(def month (clojure.string/replace (:out (shell/sh "ncal" "-w")) #"\n" " | font='Menlo Bold' color=black trim=false\n"))

(defn localtime [utc-time-string]
  (-> utc-time-string
    (str/replace "+00:00" "Z") ;; I *know* this is UTC (and not some local server time) from the API docs of sunrise-sunset.org/api
    (java.time.Instant/parse)
    (.atZone (java.time.ZoneId/of "Europe/Amsterdam"))
    (.toLocalTime)))

;; lat & lng for Eindhoven, Netherlands ... Find your lat & lng and TimeZone on https://sunrise-sunset.org

(defn sunrise-sunset []
  (let [astronomical-data (-> (babashka.curl/get "https://api.sunrise-sunset.org/json?lat=51.4392648&lng=5.478633&formatted=0")
                              :body
                              (cheshire.core/parse-string true)
                              :results)
        sunrise-raw       (:sunrise astronomical-data)
        sunset-raw        (:sunset  astronomical-data)]
    (str "☼↑ " (localtime sunrise-raw) "       ☼↓ " (localtime sunset-raw) " | font='Menlo Bold' color=black")))

(println (str weeknr "\n---\n" month "---\n" (sunrise-sunset) "\n---\n" "www.calendar-365.com | font='Menlo Bold' color=black href=https://www.calendar-365.com"))
