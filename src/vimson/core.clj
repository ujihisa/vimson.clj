(ns vimson.core
  (:use [vimson.parser :only (vimson->clojure)]))

(defn -main []
  (prn (vimson->clojure "{1: 2, 3: 4}")))
