(ns vimson.tests.core
  (:use clojure.test)
  (:use vimson.parser))

(deftest vimson->clojure-test
  (is (= [123 []] (vimson->clojure "123")))
  (is (= [123 []] (vimson->clojure " 123 ")))
  (is (= ["" []] (vimson->clojure "\"\"")))
  (is (= ["abc" []] (vimson->clojure "\"abc\"")))
  (is (= ["a\"bc" []] (vimson->clojure "'a\"bc'")))
  (is (= [" " []] (vimson->clojure "\" \"")))
  (is (= [[] []] (vimson->clojure "[]")))
  (is (= [[1 23 4] []] (vimson->clojure "[1, 23, 4]")))
  (is (= [[] []] (vimson->clojure "[ ]")))
  (is (= [{} []] (vimson->clojure "{}")))
  (is (= [{1 2} []] (vimson->clojure "{1: 2}")))
  (is (= [{1 2 "a" 3} []] (vimson->clojure "{1: 2, 'a': 3}"))))

; vim: set lispwords+=deftest :
