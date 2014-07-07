=pod

=encoding iso-8859-1

=head1  NAME

TechnologyAdapterArchetype 

=head1 Objectif 

Permettre a des developpeurs de creer simplement et rapidement un environnement de developpement de technologyadapter. 


=head2 Pré-requis

jdk   1.6 installé
maven 3   installé


=head1 Usage

=head2 Generer un squelette de projet de développement de technologyadapter pour OpenFlexo

On veut developper un technologyadapter permettant de manipuler un format de type "monFormat"

	C<mvn archetype:generate 
                -DarchetypeGroupId=org.openflexo.archetypes \
                -DarchetypeArtifactId=technologyadapter-archetype \
                -DarchetypeVersion=1.2 \

                -DgroupId=org.openflexo.demo \
                -DartifactId=monFormat \
                -Dextension=monFormat> 


        nota : ajouter C<-DarchetypeCatalog=local> pour utiliser un catalogue déployé localement (${HOME}/.m2/archetype-catalog.xml  

	le repertoire monFormat est créé et contient un projet minimaliste de technologyadapter qui compile et dispose de l'environnement de test 
	C<cd monFormat;  mvn  test>


pour en savoir plus sur l'usage des archetypes, voir les liens ci-dessous :
- L<http://books.sonatype.com/mvnref-book/reference/archetype-sect-using.html>
- L<http://maven.apache.org/guides/introduction/introduction-to-archetypes.html>
- L<http://maven.apache.org/archetype/maven-archetype-plugin/usage.html>


=head2 Phase de Développement du technologyadapter

voir tutorial ici ....

=head2 Phase de test du technologyadapter

	C<mvn  test>

=head2 colissage du technologyadapter, pour livraison

	C<mvn  package>

=head2 publication du technologyadapter

	C<mvn  deploy>

	Il faut les droits pour pouvoir publier, 
	TODO : procedure de soumission officielle



=head1 En savoir plus sur ce qu'est un projet de développement d'archetype maven

=head2 les concepts
L<http://books.sonatype.com/mvnref-book/reference/archetype-sect-intro.html>
L<http://maven.apache.org/guides/mini/guide-creating-archetypes.html>



=head1 maintenance de l'archetype :

=head2 la gestion des version

=head2 Recuperer le projet de developpement de la version courante

C<git clone git://github.com/agilebirds/openflexo.git 1.6>

=head2 Organisation des fichiers

Un archetype contient différents types d'information

=head3 l'identification du projet archetype

C<./pom.xml>
Il identifie :
		- le projet de développment de l'archetype, 
		- sa version, 
		- le depot de référence du code source de l'archetype
		- le depot de référence pour les utilisateurs de l'archetype 

=head3 le modele de projet

Lors de la génération du projet maven correspondant à l'artefact, le contenu de C<./src/main/resources/> sera utiliser comme modèle de projet.  Cette arborescence est considérée comme un ensemble de fichiers interpretés par velocity :

=over 

	- Les propriétés à renseigner obligatoirement à chaque usage ( paramètres de génération de l'archetype)
	- Les modules maven générés et leur organisation interne

=over

	* Si le fileSet contient l'option filtered="true", les C<${propertyName}> seront remplacés par la valeur de la propriete C<propertyName> (au sens maven)
	* Lors de la génération des fichiers, le fichier dont le nom contient __propertyName__suiteDuNomDuFichier.xxx seront renommés de telle sorte que __propertyName__ soit remplacé par la valeur de la propriete C<propertyName>

=back

=back

=head4 exemple

./src/main/resources/archetype-resources/__rootArtifactId__connector-ui/src/main/java/controller/__prefix__AdapterController.java


=head3 les parametrages de l'usage et du comportement de la génération de l'archetype

Il est définit dans ./src/main/resources/META-INF/maven/archetype-metadata.xml

=head3 Les tests d'integration

	les fichiers utilisés lors de la commande C<mvn test-integration>, sont organisé comme suit,  
	avec comme exemple, une generation d'un projet nommé "basic" 

=head4 Il y a une partie parametrage

	./src/test/resources/projects/basic/archetype.properties : les valeurs de parametres de generation
	./src/test/resources/projects/basic/goal.txt		 : les cibles maven a déclencher

=head4 le code que l'on voudrait voir généré avec les parametres fournis plus haut 

	dans C<./src/test/resources/projects/basic/reference>


=head3 Mise a jour du modele de projet

les modeles de fichiers sont situés dans le répertoire ...
Ils sont au format velocity

=head3 Ajout/modification de parametres

=head3 tests d'intégration

C<mvn test-integration>

=head4 jeux de référence pour comparaison 

repertoire
goals
parametres

=head4 Comparaison à une référence

=head4 test comme utilisateur finale 

=head2 comment il a été créé

=head2 livraison d'une nouvelle version

=head3 livraison du code source

=head3 publication

=head4 mise a disposition sur le serveur

=head4 mise a jour du catalogue

http://books.sonatype.com/mvnref-book/reference/archetype-sect-publishing.html

=head1 Le catalogue

on peur le generer avec la commande C<mvn archetype:crawl>, le fichier est alors disponible dans ${HOME}/.m2/archetype-catalog.xml
C'est ce fichier qui devra est disponible sur le depot binaire d'openflexo, pour declarer l'existence de l'archetype

L<http://maven.apache.org/archetype/archetype-models/archetype-catalog/archetype-catalog.html>
L<http://maven.apache.org/archetype/maven-archetype-plugin/specification/archetype-catalog.html>

=head1 Deploiement pour usage

=head2 En local

C<mvn install>

=head2 Sur le dépot d'openflexo

C<mvn deploy>

=head2 SEE ALSO

=head3 le concept d'archetype

=head3 Quelques liens sur la création d'archetypes
L<http://maven.apache.org/archetype/maven-archetype-plugin/>
L<http://blog.soat.fr/2011/06/maven-pour-les-nuls-les-archetypes/?doing_wp_cron=1379014170.8262538909912109375000>
L<https://community.jboss.org/wiki/MavenArchetypeCreationGuidelines?_sscc=t>

=head3 Gestion de configuration
L<http://blog.sonatype.com/people/2009/09/maven-tips-and-tricks-using-github/>

=head3 exemple de documentation

L<https://github.com/tbroyer/gwt-maven-archetypes/blob/master/README.md>
