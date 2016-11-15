(defproject tween-clj "0.6.0-SNAPSHOT"
  :description "Tweening library for Clojure(Script)"
  :url "https://github.com/gstamp/tween-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.28"]]
  :profiles {:dev {:plugins [[cider/cider-nrepl "0.8.0-SNAPSHOT"]
                             [org.clojure/tools.trace "0.7.8"]]}}
  :global-vars {*warn-on-reflection* true
                *assert* true})
