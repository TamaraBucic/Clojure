(set-env!
 :source-paths #{"src/cljs"}
 :resource-paths #{"html"}

 :dependencies '[[org.clojure/clojure "1.7.0"]         ;; add CLJ
                [org.clojure/clojurescript "1.7.170"] ;; add CLJS
 				[adzerk/boot-cljs "1.7.170-3"] ;; boot-cljs to compile CLJS source code
				[pandeiro/boot-http "0.7.0"] ;; add http dependency
				[adzerk/boot-reload "0.4.2"] ;; add boot-reload
				[adzerk/boot-cljs-repl "0.3.0"] ;; add REPL
				[com.cemerick/piggieback "0.2.1"]     ;; needed by bREPL 
                [weasel "0.7.0"]                      ;; needed by bREPL
                [org.clojure/tools.nrepl "0.2.12"]    ;; needed by bREPL
				]) 
 

(require '[adzerk.boot-cljs :refer [cljs]] ;; make boot-cljs visible
 		'[pandeiro.boot-http :refer [serve]] ;; make serve task visible
 		'[adzerk.boot-reload :refer [reload]] ;; make reload visible	
 		'[adzerk.boot-cljs-repl :refer[cljs-repl start-repl]] ;; make repl visilbe
 		) 
	