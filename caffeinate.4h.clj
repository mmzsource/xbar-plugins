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
;; Make sure the script has a name like caffeinate.4h.clj where the .4h. is important
;; because that's an interface to XBAR telling it it needs to run every 4 hours.
;;

;; CONSTANTS

(def sleepy-icon "🥱")
(def alert-icon "☕️")

;; temp-dir is needed, because writing in the script directory (which is the plugin
;; directory) would lead to an XBAR error at next startup because XBAR would then try
;; to evaluate the edn file as a plugin. (been there)

(def state-file-path (str (babashka.fs/temp-dir) "/caffeinate-state.edn"))

(def default-state {:state :sleepy})

;; INTERFACE TO XBAR
;;
;; I'm telling XBAR to:
;;
;; - show a certain icon
;; - call this script (which is exactly the contents of *file*)
;; - with a keyword parameter
;;
;; See: https://github.com/matryer/xbar-plugins/blob/main/CONTRIBUTING.md

(defn sleepy-cmd []
  (format "%s | bash=\"%s\" param1=\"%s\" terminal=false refresh=true" sleepy-icon *file* :sleepy))

(defn alert-cmd []
  (format "%s | bash=\"%s\" param1=\"%s\" terminal=false refresh=true" alert-icon *file* :alert))

(defn render [{:keys [state]}]
  (cond
    (= state :sleepy) (println (str sleepy-icon "\n---\n" (alert-cmd)))
    (= state :alert)  (println (str alert-icon  "\n---\n" (sleepy-cmd)))))

;; STATE-HANDLING

(defn state-stored? []
  (babashka.fs/exists? state-file-path))

(defn read-old-state []
  (clojure.edn/read-string (slurp state-file-path)))

;; XBAR promises to pass the arguments configured as param1, param2, etc
;; They can be found in order in *command-line-args*
;; This script is only interested in param1 (state-cmd)
(defn read-new-state []
  (let [[state-cmd & _] *command-line-args*]
    (if state-cmd
      {:state (read-string state-cmd)}
      state-cmd)))

(defn write-state [state]
  (spit state-file-path state))

(defn kill-all-caffeinate []
  (babashka.process/process ["killall" "caffeinate"]))

(defn caffeinate []
  (babashka.process/process ["caffeinate" "-d"]))

(defn process [{:keys [state]}]
  (cond
    (= state :sleepy) (kill-all-caffeinate)
    (= state :alert)  (caffeinate)))

(defn superglue []
  (if (state-stored?)
    (let [old-state (read-old-state)
          new-state (read-new-state)]
      (if new-state
        (do
          (write-state new-state)
          (process new-state)
          (render new-state))
        (render old-state)))
    (do
      (write-state default-state)
      (process default-state)
      (render default-state))))

(superglue)
