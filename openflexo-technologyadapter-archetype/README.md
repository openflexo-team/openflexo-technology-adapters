# NAME

TechnologyAdapterArchetype 

# Objectif 

Permettre a des developpeurs de cr&eacute;er simplement et rapidement un environnement de developpement de *TechnologyAdapter* pour [OpenFlexo](http://www.openflexo.org). 



## Pr&eacute;-requis

* jdk   1.6 install&eacute;
* maven 3   install&eacute;



# Usage

## G&eacute;n&eacute;rer un squelette de projet de d&eacute;veloppement de technologyadapter pour OpenFlexo

On veut d&eacute;velopper un technologyAdapter permettant de manipuler un format de type &quot;monFormat&quot;

	mvn archetype:generate 
                -DarchetypeGroupId=org.openflexo.archetypes \
                -DarchetypeArtifactId=technologyadapter-archetype \
                -DarchetypeVersion=1.2 \

                -DgroupId=org.openflexo.demo \
                -DartifactId=monFormat \
                -Dextension=monFormat 

nota : ajouter `-DarchetypeCatalog=local` pour utiliser un catalogue d&eacute;ploy&eacute; localement (voir `${HOME}/.m2/archetype-catalog.xml`)  

R&eacute;sultat : le repertoire *monFormat* est cr&eacute;&eacute; et contient un projet minimaliste de technologyAdapter qui compile et dispose de l&#39;environnement de test :

	cd monFormat;  mvn  test

Pour en savoir plus sur l&#39;usage des archetypes, voir les liens ci-dessous :

* [http://books.sonatype.com/mvnref-book/reference/archetype-sect-using.html](http://books.sonatype.com/mvnref-book/reference/archetype-sect-using.html)

* [http://maven.apache.org/guides/introduction/introduction-to-archetypes.html](http://maven.apache.org/guides/introduction/introduction-to-archetypes.html)

* [http://maven.apache.org/archetype/maven-archetype-plugin/usage.html](http://maven.apache.org/archetype/maven-archetype-plugin/usage.html)


## Phase de D&eacute;veloppement du technologyAdapter

Voir tutorial ici (ADU)....
Respecter les r&egrave;gles de [d&eacute;veloppement](https://openflexo.org/tiki/Development+guidelines) du projet.

## Phase de test du technologyAdapter

	mvn  test

### Test unitaire

	ADU

### Test d'int&eacute;gration

	ADU


## Colissage du technologyAdapter, pour livraison

	mvn  package

## Publication du technologyAdapter

	mvn  deploy

	Il faut les droits pour pouvoir publier, 
	TODO : procedure de soumission officielle

### En savoir plus sur ce qu&#39;est un projet de d&eacute;veloppement d&#39;archetype maven

Les concepts :

* [http://books.sonatype.com/mvnref-book/reference/archetype-sect-intro.html](http://books.sonatype.com/mvnref-book/reference/archetype-sect-intro.html)

* [http://maven.apache.org/guides/mini/guide-creating-archetypes.html](http://maven.apache.org/guides/mini/guide-creating-archetypes.html)


# Maintenance de l&#39;archetype :

## La gestion des versions

## R&eacute;cup&eacute;rer le projet de d&eacute;veloppement de la version courante

git clone https://github.com/openflexo-team/openflexo-technology-adapters.git
[https://github.com/openflexo-team/openflexo-technology-adapters.git](https://github.com/openflexo-team/openflexo-technology-adapters.git)


## Organisation des fichiers

Un archetype contient diff&eacute;rents types d&#39;informations :

### l&#39;identification du projet archetype

`./pom.xml`

Il identifie :

* le projet de d&eacute;veloppment de l&#39;archetype, 
* sa version, 
* le depot de r&eacute;f&eacute;rence du code source de l&#39;archetype
* le depot de r&eacute;f&eacute;rence pour les utilisateurs de l&#39;archetype 

### Le mod&egrave;le de projet

Lors de la g&eacute;n&eacute;ration du projet maven correspondant &agrave; l&#39;artefact, le contenu de `./src/main/resources/` sera utiliser comme mod&egrave;le de projet.  Cette arborescence est consid&eacute;r&eacute;e comme un ensemble de fichiers interpret&eacute;s par velocity :

* Les propri&eacute;t&eacute;s &agrave; renseigner obligatoirement &agrave; chaque usage ( param&egrave;tres de g&eacute;n&eacute;ration de l&#39;archetype)
* Les modules maven g&eacute;n&eacute;r&eacute;s ainsi que leur organisation interne

	* Si le fileSet contient l&#39;option *filtered=&quot;true*, les `${propertyName}` seront remplac&eacute;s par la valeur de la propri&eacute;te `propertyName` (au sens maven)
	* Lors de la g&eacute;n&eacute;ration des fichiers, le fichier dont le nom contient \_\_propertyName\_\_suiteDuNomDuFichier.xxx seront renomm&eacute;s de telle sorte que \_\_propertyName\_\_ soit remplac&eacute; par la valeur de la propri&eacute;te `${propertyName}`

#### Exemple

	./src/main/resources/archetype-resources/__rootArtifactId__connector-ui/src/main/java/controller/__prefix__AdapterController.java



### Les param&eacute;trages de l&#39;usage et du comportement de la g&eacute;n&eacute;ration de l&#39;archetype

Il est d&eacute;finit dans `./src/main/resources/META-INF/maven/archetype-metadata.xml`

### Les tests d&#39;int&eacute;gration

Les fichiers utilis&eacute;s lors de la commande `mvn test-integration`, sont organis&eacute; comme suit,  
	avec comme exemple, une generation d&#39;un projet nomm&eacute; *&quot;basic&quot*; 

#### Il y a une partie param&acute;trage

	./src/test/resources/projects/basic/archetype.properties : les valeurs de paramètres de generation
	./src/test/resources/projects/basic/goal.txt		 : les cibles maven a déclencher

#### Le code que l&#39;on voudrait voir g&eacute;n&eacute;r&eacute; avec les param&egrave;tres fournis plus haut 

	./src/test/resources/projects/basic/reference

### Mise a jour du mod&egrave;le de projet

Les mod&egrave;les de fichiers sont situ&eacute;s dans le r&eacute;pertoire `./src/main/resources/archetype-resources`

Ils sont au format velocity. 


### Ajout/modification de param&egrave;tres

### tests d&#39;int&eacute;gration

`mvn test-integration`

#### jeux de r&eacute;f&eacute;rence pour comparaison 

repertoire
goals
parametres

#### Comparaison &agrave; une r&eacute;f&eacute;rence

Le code g&eacute;n&aecute;r&eacute; est dans `src/test/resources/projects/basic/reference`

#### Test comme utilisateur final 

## Comment il a &eacute;t&eacute; cr&eacute;&eacute;

## livraison d&#39;une nouvelle version

### livraison du code source

### publication

#### mise a disposition sur le serveur

#### mise a jour du catalogue

[http://books.sonatype.com/mvnref-book/reference/archetype-sect-publishing.html](http://books.sonatype.com/mvnref-book/reference/archetype-sect-publishing.htmlhttp://books.sonatype.com/mvnref-book/reference/archetype-sect-publishing.html)

# Le catalogue

On peut le g&eacute;n&aecute;r&eacute; avec la commande `mvn archetype:crawl`, le fichier est alors disponible dans `${HOME}/.m2/archetype-catalog.xml`
C&#39;est ce fichier qui devra est disponible sur le d&eacute;pot binaire d&#39;openflexo, pour d&eacute;clarer l&#39;existence de l&#39;archetype.


* [http://maven.apache.org/archetype/archetype-models/archetype-catalog/archetype-catalog.html](http://maven.apache.org/archetype/archetype-models/archetype-catalog/archetype-catalog.html)
* [http://maven.apache.org/archetype/maven-archetype-plugin/specification/archetype-catalog.html](http://maven.apache.org/archetype/maven-archetype-plugin/specification/archetype-catalog.html)

# Deploiement pour usage

## En local

	mvn install

## Sur le d&eacute;pot d&#39;OpenFlexo

	mvn deploy

## SEE ALSO

### Le concept d&#39;archetype

### Quelques liens sur la cr&eacute;ation d&#39;archetypes

* [http://maven.apache.org/archetype/maven-archetype-plugin/](http://maven.apache.org/archetype/maven-archetype-plugin/)
* [http://blog.soat.fr/2011/06/maven-pour-les-nuls-les-archetypes/?doing_wp_cron=1379014170.8262538909912109375000](http://blog.soat.fr/2011/06/maven-pour-les-nuls-les-archetypes/?doing_wp_cron=1379014170.8262538909912109375000)
* [https://community.jboss.org/wiki/MavenArchetypeCreationGuidelines?_sscc=t](https://community.jboss.org/wiki/MavenArchetypeCreationGuidelines?_sscc=t)

### Gestion de configuration

* [http://blog.sonatype.com/people/2009/09/maven-tips-and-tricks-using-github/](http://blog.sonatype.com/people/2009/09/maven-tips-and-tricks-using-github/)

### Exemple de documentation

* [https://github.com/tbroyer/gwt-maven-archetypes/blob/master/README.md](https://github.com/tbroyer/gwt-maven-archetypes/blob/master/README.md)

### Jouer avec l'encodage perldoc => markdown

* [encodage Markdown pour documentation sur github](http://fr.wikipedia.org/wiki/Markdown#Formatage)

Vous aurez besoin de : 
	sudo apt-get install libhtml-parser-perl
	sudo apt-get install libpod-markdown-perl

* *README.txt*  est &eacute;crit en pod ( perldoc )

	iconv -f utf8 -t iso-8859-1  README.txt | pod2markdown  | perl -MHTML::Entities -ne 'print encode_entities($\_)'  >  README.md

L'extension **Markdown Preview** sur *Chromium* permet de prévisualiser le rendu



