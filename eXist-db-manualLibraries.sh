mvn install:install-file -Dfile=/opt/eXist-db/exist.jar -DgroupId=org.exist-db -DartifactId=exist -Dversion=2.1 -Dpackaging=jar
mvn install:install-file -Dfile=/opt/eXist-db/exist-optional.jar -DgroupId=org.exist-db -DartifactId=exist-optional -Dversion=2.1 -Dpackaging=jar
mvn install:install-file -Dfile=/opt/eXist-db/start.jar -DgroupId=org.exist-db -DartifactId=start -Dversion=2.1 -Dpackaging=jar
mvn install:install-file -Dfile=/opt/eXist-db/lib/core/pkg-repo.jar -DgroupId=org.exist-db -DartifactId=pkg-repo -Dversion=2.1 -Dpackaging=jar
mvn install:install-file -Dfile=/opt/eXist-db/lib/core/quartz-2.1.6.jar -DgroupId=org.quartz -DartifactId=quartz -Dversion=2.1.6 -Dpackaging=jar
mvn install:install-file -Dfile=/opt/eXist-db/lib/core/xmldb.jar -DgroupId=org.xmldb.api -DartifactId=xmldb -Dversion=from-existdb-2.1 -Dpackaging=jar
mvn install:install-file -Dfile=/opt/eXist-db/lib/core/gnu-crypto-2.0.1-min.jar -DgroupId=org.gnu -DartifactId=gnu-crypto -Dversion=2.0.1 -Dpackaging=jar -Dclassifier=min
mvn install:install-file -Dfile=/opt/eXist-db/lib/extensions/exist-index-lucene.jar -DgroupId=org.exist-db.extensions -DartifactId=exist-index-lucene -Dversion=2.1 -Dpackaging=jar
mvn install:install-file -Dfile=/opt/eXist-db/lib/extensions/exist-index-sort.jar -DgroupId=org.exist-db.extensions -DartifactId=exist-index-sort -Dversion=2.1 -Dpackaging=jar
mvn install:install-file -Dfile=/opt/eXist-db/lib/extensions/exist-index-ngram.jar -DgroupId=org.exist-db.extensions -DartifactId=exist-index-ngram -Dversion=2.1 -Dpackaging=jar
mvn install:install-file -Dfile=/opt/eXist-db/lib/extensions/exist-webdav.jar -DgroupId=org.exist-db.extensions -DartifactId=exist-webdav -Dversion=2.1 -Dpackaging=jar
mvn install:install-file -Dfile=/opt/eXist-db/lib/extensions/exist-xslt.jar -DgroupId=org.exist-db.extensions -DartifactId=exist-xslt -Dversion=2.1 -Dpackaging=jar
mvn install:install-file -Dfile=/opt/eXist-db/lib/extensions/exist-modules.jar -DgroupId=org.exist-db.extensions -DartifactId=exist-modules -Dversion=2.1 -Dpackaging=jar