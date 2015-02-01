(ns cljs-devtools-sample.core
  (:require [clojure.browser.repl :as repl]
            [clojure.string :as string]
            [devtools.core :as dev]
            [devtools.debug :as debug]
            [devtools.format :as format]))

(repl/connect "http://localhost:9000/repl")
(enable-console-print!)
(debug/init!)
(dev/install-devtools!)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; some quick and dirty inline tests

(def test-number 42)
(def test-keyword :keyword)
(def test-symbol 'symbol)
(def test-vector [nil 0 0.5 "text" [1 2 3 [10 20 30 [100 200 300]]] '#{a b c} {:k1 'v1 :k2 "v2"}])
(def test-problematic-vector [(aget js/window "sampleArray") [js/window] #js {"k1" "v1" "k2" :v2} (js* "function(x) { console.log(x); }") #(.log js/console "hello")])
(def test-long-seq (range 100))
(def test-large-map {:k1 'v1 :k2 'v2 :k3 'v3 :k4 'v4 :k5 'v5 :k6 'v6 :k7 'v7 :k8 'v8 :k9 'v9})
(def test-large-set #{1 2 3 4 5 6 7 8 9 10 11 12 13 14 15})
(def test-interleaved #js {"js" true "nested" {:js false :nested #js {"js2" true "nested2" {:js2 false}}}})

(def test-atom
  (atom {:number 0
         :string "sample string"
         :keyword :keyword
         :symbol 'symbol
         :vector [0,1,2]
         :set '#{a b c}
         :map '{k1 v1 k2 v2}
         :form '[(defn greet [name] (str "Hello, " name "!"))]
         ;:lambda #(println %)
         }))

; custom formatter defined in user code
(defrecord Person [name address]
  format/IDevtoolsFormat
  (-header [_] (format/template "span" "color:white; background-color:blue; padding: 0px 4px" (str "Person: " name)))
  (-has-body [_] (exists? address))
  (-body [_] (format/standard-body-template (string/split-lines address))))

(def test-person (Person. "John Doe" "Office 33\n27 Colmore Row\nBirmingham\nEngland"))

(defn excercise! []
  (.log js/console test-number)
  (.log js/console test-keyword)
  (.log js/console test-symbol)
  (.log js/console test-long-seq)
  (.log js/console test-large-map)
  (.log js/console test-large-set)
  (.log js/console test-atom)
  (.log js/console test-problematic-vector)
  (.log js/console test-person)
  ;(.log js/console (.-nested test-interleaved))
  ;(.log js/console test-interleaved)
  )

(excercise!)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; disable support

(.log js/console "****** disable ******")

(dev/disable-devtools!)

(excercise!)

; ad-hoc debugging

(.log js/console "****** enable ******")

(dev/enable-devtools!)

;(.log js/console (dev/build-header test-vector))
