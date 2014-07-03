ARTEFACT_ROOT=$(CURDIR)
DEV_DIR=$(ARTEFACT_ROOT)/DEV
ARCHETYPE_NAME=technologyadapters-archetype
ARCHETYPE_VERSION=1.6-SNAPSHOT
ARCHETYPE_ARCHIVE=$(ARCHETYPE_NAME)-$(ARCHETYPE_VERSION).tgz

help: 
	@echo "this Makefile helps working on the TechnologyAdapter Archetype"
	@echo "   steps :"
	@echo "       "

env:
	mvn -version

archive :
	- rm -i  $(ARCHETYPE_ARCHIVE)
	tar cvzf /tmp/$(ARCHETYPE_ARCHIVE) .
	mv /tmp/$(ARCHETYPE_ARCHIVE) .
	ls -la $(ARCHETYPE_ARCHIVE)

# --------------------------------------------------------------------------------
# openflexo
# --------------------------------------------------------------------------------
OPENFLEXO_BRANCHNAME=1.6
OPENFLEXO_DEVDIR=$(DEV_DIR)/$(OPENFLEXO_BRANCHNAME)

clone_git:
	@[ -d $(DEV_DIR) ] || mkdir $(DEV_DIR)
	cd $(DEV_DIR) && git clone git://github.com/agilebirds/openflexo.git $(OPENFLEXO_BRANCHNAME)


build_openflexo :
	cd $(OPENFLEXO_DEVDIR) && mvn clean compile

package_openflexo :
	cd $(OPENFLEXO_DEVDIR) && ls -la openflexo/packaging/flexobuild/build.xml \
	&& cd  openflexo/packaging/flexobuild && mvn package -DcreatePackage=1

test_openflexo :
	cd $(OPENFLEXO_DEVDIR) && mvn test

run_openflexo :
	cd $(OPENFLEXO_DEVDIR)/packaging/exec/flexoenterprise && mvn exec:exec



# --------------------------------------------------------------------------------
# technologyadapter Archetype
# --------------------------------------------------------------------------------
#ARCHETYPE_DEV_DIR=$(DEV_DIR)/ARCHETYPE
ARCHETYPE_DEV_DIR=$(CURDIR)
ARCHETYPE_DEMO_DIR=$(ARCHETYPE_DEV_DIR)/DEMO_ARCHETYPE
ARCHETYPE_MODEL=$(OPENFLEXO_DEVDIR)/flexodesktop/technologyadapters

# those commented targets are use only one time
#   => prepare archetype from maven project
#create_archetype_from_project: $(ARCHETYPE_MODEL) 
#	cd $(ARCHETYPE_MODEL); \
#		mvn -o -B archetype:create-from-project ;\
#		mkdir -p $(ARCHETYPE_DEV_DIR); \ 
#		cp -r $(ARCHETYPE_MODEL)/target/generated-sources/archetype $(ARCHETYPE_DEV_DIR) ; \
#		find $(ARCHETYPE_DEV_DIR)
#	@echo voir http://blog.soat.fr/2011/06/maven-pour-les-nuls-les-archetypes/

ressources:
	@echo "http://maven.apache.org/guides/mini/guide-creating-archetypes.html"

# files 
archetype_parameter :
	gvim 	$(ARCHETYPE_DEV_DIR)/src/main/resources/META-INF/maven/archetype-metadata.xml

test_parameters:
	gvim 	$(ARCHETYPE_DEV_DIR)/src/test/resources/projects/basic/archetype.properties

test_reference :
	cd $(ARCHETYPE_DEV_DIR)/src/test/resources/projects/basic/reference; find .

archetype_templates :
	cd $(ARCHETYPE_DEV_DIR)/src/main/resources/archetype-resources; find .

clean clean_artifact :
	cd $(ARCHETYPE_DEV_DIR); mvn -o -B clean ; 

integ :
	cd $(ARCHETYPE_DEV_DIR); mvn -o -B clean integration-test ; 

# when they are differences
diff: 
	cd $(ARCHETYPE_DEV_DIR);  \
	echo "-----------------------------------------------------------------------------------";\
	ls -la target/test-classes/projects/basic/project  target/test-classes/projects/basic/reference ; \
	diff -r --brief	target/test-classes/projects/basic/project/csv  target/test-classes/projects/basic/reference  | awk '{ print "gvimdiff --nofork " $$3 " " $$5"\n " ;}'  | tee diffs.txt ; chmod u+x diffs.txt 
	echo "cd $(ARCHETYPE_DEV_DIR); ./diffs.txt"

diffl :
	cd $(ARCHETYPE_DEV_DIR);  \
	cd target/test-classes/projects/basic ; \
	cd project/csv; find . 2>&1 | sort | tee /tmp/found ; \
	cd ../../reference; find . 2>&1 | sort  | tee  /tmp/ref ; \
	gvimdiff --nofork /tmp/found  /tmp/ref; 


package_archetype: $(ARCHETYPE_DEV_DIR)
	cd $(ARCHETYPE_DEV_DIR); mvn -o -B clean package integration-test

install_archetype: 
	cd $(ARCHETYPE_DEV_DIR); mvn -o -B clean integration-test install


doc:
	cd $(ARCHETYPE_DEV_DIR); mvn -o -B site 

info:
	chromium-browser file://$(ARCHETYPE_DEV_DIR)/target/site/index.html

# see http://books.sonatype.com/mvnref-book/reference/archetype-sect-publishing.html

catalog:
	cd $(ARCHETYPE_DEV_DIR); mvn -o -B  archetype:crawl ; 
	ls $(HOME)/.m2/repository/archetype-catalog.xml;
# https://community.jboss.org/wiki/MavenArchetypeCreationGuidelines?_sscc=t	
# exemple : https://code.google.com/p/open-archetypes/source/browse/multi-javaee5-archetype/



test_archetype: 
	rm -rf $(ARCHETYPE_DEMO_DIR)
	mkdir $(ARCHETYPE_DEMO_DIR);
	cd $(ARCHETYPE_DEMO_DIR);  mvn -o -B archetype:generate \
		-DarchetypeGroupId=org.openflexo \
		-DarchetypeArtifactId=technologyadapters-archetype \
		-DarchetypeVersion=1.6-SNAPSHOT \
		-DgroupId=org.openflexo.demo \
		-DartifactId=csv \
		-Dextension=csv \
		-DarchetypeCatalog=local ; cd csv;  mvn -B compile




#flexo_foundation_deps :
#	cd $(DEV_DIR)/1.6/flexodesktop/model/flexofoundation && mvn install -Dmaven.test.skip=true


#[ERROR] 
#[ERROR] [1] [INFO] Searching for file location: /home/gilles/openFlexo/$(DEV_DIR)/1.6/flexodesktop/model/flexofoundation/package-resources.xml
#[ERROR] 
#[ERROR] [2] [INFO] File: /home/gilles/openFlexo/$(DEV_DIR)/1.6/flexodesktop/model/flexofoundation/package-resources.xml does not exist.
#[ERROR] 
#[ERROR] [3] [INFO] File: /home/gilles/openFlexo/$(DEV_DIR)/1.6/flexodesktop/model/flexofoundation/package-resources.xml does not exist.
#[ERROR] -> [Help 1]
#
# mvn install:install-file -Dfile=target/flexofoundation-1.6-SNAPSHOT.jar -DgroupId=org.openflexo -DartifactId=flexofoundation -Dpackaging=jar -Dversion=1.6-SNAPSHOT

#ajout de 
#<repositories>
#    <repository>
#      <releases>
#        <enabled>true</enabled>
#      </releases>
#      <snapshots>
#        <enabled>true</enabled>
#        <updatePolicy>always</updatePolicy>
#      </snapshots>
#      <id>openflexo</id>
#      <url>https://maven.openflexo.com/artifactory/openflexo-deps/</url>
#    </repository>
# </repositories>
#dans le pom de module

# voir src/main/resources/META-INF/maven/archetype-metadata.xml
# src/main/resources/META-INF/maven

# voir http://maven.apache.org/guides/mini/guide-creating-archetypes.html
# http://maven.apache.org/archetype/maven-archetype-plugin/
# http://maven.apache.org/archetype/maven-archetype-plugin/integration-test-mojo.html*
# http://maven.apache.org/archetype/maven-archetype-plugin/update-local-catalog-mojo.html
# http://maven.apache.org/archetype/maven-archetype-plugin/examples/create-with-property-file.html
# http://www.luckyryan.com/2013/02/15/create-maven-archetype-from-existing-project/

# dans /home/gilles/openFlexo/$(DEV_DIR)/ARCHETYPE/target/generated-sources/archetype/src/test/resources/projects/basic
# mkdir reference
# cp -r ../../../../../../../../pom.xml reference/
# cp -r ../../../../../../../../csvconnector reference/
# cp -r ../../../../../../../../csvconnector-ui reference/

#	 mvn integration-test
#	 ls
#	 cd src/test/resources/projects/basic/reference/


TODO: 
	@echo "integrer eclipse et les @TODO pour guider le developpeur"
	@echo "limiter le perimetre du pom parent"
	@echo "separer les pom de modules et les poms parent"

# recherche des imports
# find . -name "*.java" | xargs perl -n -e 'print qq{$1\n} if /import (.*);$/' | sort -u
# refactoring
# for i in `find src/ | xargs grep -l '${prefix}'`; do perl -pi -e 's/\${prefix}/\${technologyPrefix}/g' $i ; done
# for i in `find src/ | xargs grep -l '${extension}'`; do perl -pi -e 's/\${extension}/\${technologyExtension}/g' $i ; done
# format pom
#for i in `find . -name "pom.xml"`; do xmllint --format $i -o $i; done
# 

