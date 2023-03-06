(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'io.github.rutledgepaulv/newrelic-clj)
(def version "1.0.1")
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def jar-file (format "target/newrelic-clj.jar" (name lib) version))

(defn get-version [_]
  (print version))

(defn clean [_]
  (b/delete {:path "target"}))

(defn jar [_]
  (b/write-pom {:class-dir class-dir
                :lib       lib
                :version   version
                :basis     basis
                :src-dirs  ["src"]
                :scm       {:tag        (str "v" version)
                            :connection (str "scm:git:git@github.com:rutledgepaulv/" (name lib) ".git")
                            :url        (str "https://github.com/rutledgepaulv/" (name lib))}})
  (b/copy-dir {:src-dirs ["src"] :target-dir class-dir})
  (b/jar {:class-dir class-dir :jar-file jar-file}))