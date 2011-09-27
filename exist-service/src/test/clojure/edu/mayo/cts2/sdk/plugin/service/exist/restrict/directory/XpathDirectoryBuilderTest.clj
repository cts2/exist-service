(ns edu.mayo.cts2.sdk.plugin.service.exist.restrict.directory.XpathDirectoryBuilderTest
  (:require clojure.contrib.mock)
  (:use clojure.test)
  (:use clojure.test.junit)
  (:import [edu.mayo.cts2.sdk.filter.directory AbstractStateBuildingDirectoryBuilder$Callback])
  (:import [edu.mayo.cts2.sdk.plugin.service.exist.restrict.directory XpathDirectoryBuilder]))

(defn new-callback [] ( proxy 
                        [AbstractStateBuildingDirectoryBuilder$Callback] []
                        (execute [state start maxtoreturn] nil)
                        (executeCount [state]  1)))

(defn new-builder [] (XpathDirectoryBuilder. nil (new-callback) nil nil ))


(deftest test-not-null
  (is (not= nil (new-builder))))

(deftest count
  (is (= 1 (.count(new-builder)))))

(run-all-tests)