(ns vimson.parser
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
      [string not-char number whitespace]])
  (:require [zetta.parser.seq :as s]))

(declare vimson)

(def vimson-dict
  (*>
    (s/char \{)
    (<* (<$> #(apply hash-map (flatten %))
             (sep-by (<$> (fn [a _ b] [a b])
                          (vimson)
                          (s/char \:)
                          (vimson))
                     (s/char \,)))
        (s/char \}))))

(def vimson-string
  (choice
    [(around (s/char \")
             (<$> clojure.string/join (many (not-char \"))))
     (around (s/char \')
             (<$> clojure.string/join (many (not-char \'))))]))

(def vimson-list
  (*>
    (s/char \[)
    (<* (sep-by (vimson) (s/char \,))
        (s/char \]))))

(defn- vimson []
  (around (many whitespace)
          (choice [vimson-dict number vimson-string vimson-list])))

(defn vimson->clojure [clj-str]
  (let [result (z/parse-once (vimson) clj-str)]
    [(:result result) (:remainder result)]))
