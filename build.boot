(set-env!
 :source-paths #{"src/clj" "src/cljs" "src/cljc"}
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
                [org.clojars.magomimmo/domina "2.0.0-SNAPSHOT"] ;; domina (DOM library written in CLJS )
                [hiccups "0.3.0"] ;; Hiccup is a library for representing HTML in Clojure
                [compojure "1.4.0"]                   ;; routing lib
                [org.clojars.magomimmo/shoreleave-remote-ring "0.3.1"]
                [org.clojars.magomimmo/shoreleave-remote "0.3.1"]
                [javax.servlet/servlet-api "2.5"]     ;; for dev only
                [org.clojars.magomimmo/valip "0.4.0-SNAPSHOT"] ;; valip dependency for server-side validation
                [enlive "1.1.6"] ;; selector based lib for clojure
                [adzerk/boot-test "1.0.7"] ;; test pre-build task 
                [crisptrutski/boot-cljs-test "0.2.1-SNAPSHOT"]
				]) 
 

(require '[adzerk.boot-cljs :refer [cljs]] ;; make boot-cljs visible
 		'[pandeiro.boot-http :refer [serve]] ;; make serve task visible
 		'[adzerk.boot-reload :refer [reload]] ;; make reload visible	
 		'[adzerk.boot-cljs-repl :refer[cljs-repl start-repl]] ;; make repl visible
 		'[adzerk.boot-test :refer [test]]
 		'[crisptrutski.boot-cljs-test :refer [test-cljs]]
 		) 

(deftask testing
	"Add test/cljc for CLJ/CLJS testing purpose"
	[]
	(set-env! :source-paths #(conj % "test/cljc"))
	identity
)

(deftask tdd
	"Launch a TDD Environment"
	[]
	(comp
		(serve 	:handler 'modern-cljs.core/app
				:resource-root "target"
				:reload true
			)
		(testing)
		(watch)
		(reload)
		(cljs-repl)
		(test-cljs	:out-file "main.js"
					:js-env :phantom 
					:namespaces '#{modern-cljs.shopping.validators-test}
					:update-fs? true)
		(test :namespaces '#{modern-cljs.shopping.validators-test})
		(target :dir #{"target"})
	)
)



(deftask dev
	"Launch Immediate Feedback Development Environment"
	[]
	(comp
		(serve :handler 'modern-cljs.core/app    ;; ring handler
			:resource-root "target" ;; resource-path
			:reload true) ;; reload server side ns
		(watch)
		(reload)
		(cljs-repl)
		(cljs)
		(target :dir #{"target"})
		)
	)
