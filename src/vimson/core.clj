(ns vimson.core
  (:refer-clojure :exclude [char])
  (:require [clojure.core :as core]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [zetta.core :as z])
  (:use [zetta.core :only [<$> <* *> >>]])
  (:use
    [zetta.combinators
      :only
      [sep-by around many many1 choice]]
    [zetta.parser.seq
      :only
      [string char not-char number whitespace]]))

(declare vimson)

(def vimson-dict
  (*>
    (char \{)
    (<* (<$> #(apply hash-map (flatten %))
             (sep-by (<$> (fn [a _ b] [a b])
                          (vimson)
                          (char \:)
                          (vimson))
                     (char \,)))
        (char \}))))

(def vimson-string
  (choice
    [(around (char \")
             (<$> clojure.string/join (many (not-char \"))))
     (around (char \')
             (<$> clojure.string/join (many (not-char \'))))]))

(def vimson-list
  (*>
    (char \[)
    (<* (sep-by (vimson) (char \,))
        (char \]))))

(defn- vimson []
  (around (many whitespace)
          (choice [vimson-dict number vimson-string vimson-list])))

(defn vimson->clojure [clj-str]
  (let [result (z/parse-once (vimson) clj-str)]
    [(:result result) (:remainder result)]))

(defn -main []
  (prn (z/parse-once (vimson) "{1: 2, 3: 4}")))
