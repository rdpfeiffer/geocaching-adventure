(defproject geocaching-adventure "0.1.0-SNAPSHOT"
  :description ""
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-cljsbuild "1.1.4"]
            [lein-figwheel "0.5.4-7"]
            ;[lein-figwheel "0.5.9"]
            ]
  :clean-targets ^{:protect false} [:target-path "out" "resources/public/js"]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [org.clojure/core.async "0.2.391"]
                 [gettext "0.1.1"]
                 ;[jline/jline "2.8"]
                 [cljs-ajax "0.5.8"]
                 [org.clojure/data.json "0.2.6"]

                 [cljsjs/react "15.4.2-2"]
                 [cljsjs/react-dom "15.4.2-2"]
                 [sablono "0.8.0"]
                 [lock-key "1.4.1"]
                 [io.github.theasp/simple-encryption "0.1.0"]
                 ]
  :cljsbuild
    {:builds
     {:main {:source-paths ["src"]
             :compiler {:main geocaching-adventure.core
                        :asset-path "js/out"
                        :output-to "resources/public/js/main.js"
                        :optimizations :simple
                        :pretty-print false
                        :optimize-constants true
                        :static-fns true}}

      :dev {:source-paths ["src"]
            :figwheel {:load-warninged-code true
                       :before-jsload "advenjure.ui.io/figwheel-cleanup"}

            :compiler {:output-to "resources/public/js/main.js"
                       :output-dir "resources/public/js/out"
                       :main geocaching-adventure.core
                       :parallel-build true
                       :asset-path "js/out"
                       :optimizations :none
                       :source-map true
                       :pretty-print true}}}}

  :main ^:skip-aot geocaching-adventure.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
