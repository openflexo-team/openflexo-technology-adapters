/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
 * 
 * This file is part of Owlconnector, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.technologyadapter.owl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.OWLOntologyLibrary;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestPizza extends OpenflexoTestCase {

	private static OWLTechnologyAdapter owlAdapter;
	private static OWLOntologyLibrary ontologyLibrary;

	public static final String FLEXO_CONCEPT_ONTOLOGY_URI = "http://www.agilebirds.com/openflexo/ontologies/FlexoConceptsOntology.owl";

	/**
	 * Instanciate test ResourceCenter
	 */
	@Test
	@TestOrder(1)
	public void test0LoadTestResourceCenter() {

		log("test0LoadTestResourceCenter()");
		instanciateTestServiceManager(OWLTechnologyAdapter.class);
		owlAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(OWLTechnologyAdapter.class);
		ontologyLibrary = (OWLOntologyLibrary) serviceManager.getTechnologyAdapterService()
				.getTechnologyContextManager(owlAdapter);

		for (FlexoResourceCenter<?> rc : serviceManager.getResourceCenterService().getResourceCenters()) {
			System.out.println("> rc: " + rc.getDefaultBaseURI() + " " + rc.getBaseArtefact());
			for (FlexoResource<?> r : rc.getAllResources(null)) {
				System.out.println(" >>> " + r.getURI());
			}
		}

	}

	/**
	 * Load an ontology
	 */
	@Test
	@TestOrder(2)
	public void test1LoadTestResourceCenter() {

		// Resource myOntology =
		// ResourceLocator.locateResource("TestResourceCenter/Ontologies/Tests/PizzaOntology.owl");
		// File myOntologyFile = ((FileResourceImpl) myOntology).getFile();

		OWLOntology hop = ontologyLibrary
				.getOntology("http://www.denali.be/flexo/ontologies/PizzaOntology/PizzaOntology.owl");

		System.out.println("Found: " + hop);

		Object myOntologyFile = hop.getResource().getIODelegate().getSerializationArtefact();

		System.out.println("myOntologyFile: " + myOntologyFile);

		hop.loadWhenUnloaded();

		hop.describe();

		// FlexoProject project;
		// project.getURI();

		File createdFile = null;

		try {
			createdFile = File.createTempFile("MyPizza", ".owl");
			// createdFile = new File(myOntologyFile.getParent(), f.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Try to create ontology: " + createdFile);

		String URI = "http://my-pizza.com";

		Model base = ModelFactory.createDefaultModel();
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, ontologyLibrary, base);
		ontModel.createOntology(URI);
		ontModel.setDynamicImports(true);
		OntClass cacaClass = ontModel.createClass(URI + "#" + "caca");
		OntClass pipiClass = ontModel.createClass(URI + "#" + "pipi");
		pipiClass.addSuperClass(cacaClass);

		OWLOntology flexoConceptsOntology = ontologyLibrary.getOntology(FLEXO_CONCEPT_ONTOLOGY_URI);

		ontModel.getDocumentManager().loadImport(flexoConceptsOntology.getOntModel(), FLEXO_CONCEPT_ONTOLOGY_URI);
		ontModel.getDocumentManager().addModel(FLEXO_CONCEPT_ONTOLOGY_URI, flexoConceptsOntology.getOntModel(), true);
		ontModel.loadImports();
		ontModel.getDocumentManager().loadImports(ontModel);

		OntClass flexoConceptClass = flexoConceptsOntology.getOntModel()
				.createClass(FLEXO_CONCEPT_ONTOLOGY_URI + "#" + "FlexoConcept");

		for (Iterator i = flexoConceptClass.listSuperClasses(); i.hasNext();) {
			OntClass unParent = (OntClass) i.next();
			System.out.println("FlexoConcept, comme parent j'ai: " + unParent);
		}
		pipiClass.addSuperClass(flexoConceptClass);

		System.out.println("Dynamic imports= " + ontModel.getDynamicImports());

		for (Object o : ontModel.listImportedOntologyURIs()) {
			System.out.println("J'importe " + (String) o);
		}

		for (Iterator i = ontModel.listSubModels(true); i.hasNext();) {
			OntModel unModele = (OntModel) i.next();
			System.out.println("Comme sub-model j'ai: " + unModele);
		}

		for (Iterator i = ontModel.getDocumentManager().listDocuments(); i.hasNext();) {
			System.out.println("Comme document j'ai: " + i.next());
		}

		String ONTOLOGY_C = URI;
		String ONTOLOGY_A = FLEXO_CONCEPT_ONTOLOGY_URI;
		String ONTOLOGY_B = "http://www.openflexo.org/test/Family.owl";
		String ONTOLOGY_D = "http://www.agilebirds.com/openflexo/ViewPoints/BasicOntology.owl";

		String SOURCE = "@prefix rdf:         <http://www.w3.org/1999/02/22-rdf-syntax-ns#>.\n"
				+ "@prefix rdfs:        <http://www.w3.org/2000/01/rdf-schema#>.\n"
				+ "@prefix owl:         <http://www.w3.org/2002/07/owl#>.\n" + "<" + ONTOLOGY_C + "> a owl:Ontology \n"
				+ "   ; owl:imports <" + ONTOLOGY_A + ">\n" + "   ; owl:imports <" + ONTOLOGY_B + ">.\n";

		System.out.println("About to load source ontology:");
		System.out.println(SOURCE);

		// create an ont model spec that uses a custom document manager to
		// look for imports in the database
		/*
		 * OntModelSpec oms = getMaker(); oms.setDocumentManager( new
		 * DbAwareDocumentManager( m_maker ) );
		 */

		// create the ontology model
		/*
		 * Model base = m_maker.createModel( ONTOLOGY_C ); OntModel om =
		 * ModelFactory.createOntologyModel( oms, base );
		 */

		// read in some content which does importing
		ontModel.read(new StringReader(SOURCE), ONTOLOGY_C, "N3");

		// as a test, write everything
		// System.out.println( "Combined model contents:" );
		// ontModel.writeAll( System.out, "N3", null );

		// Model base =
		// importedOntologyLibrary.createModel("http://my-pizza.com");
		/*
		 * Model base = null; //new
		 * ModelCom(importedOntologyLibrary.getGraphMaker().createGraph(
		 * "http://my-pizza.com"));
		 * 
		 * 
		 * OntModel ontModel =
		 * ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM,
		 * importedOntologyLibrary, base);
		 * 
		 * ontModel.createOntology("http://my-pizza.com");
		 * 
		 * //ontModel.addSubModel(importedOntologyLibrary.getOntology(
		 * ImportedOntologyLibrary.FLEXO_CONCEPT_ONTOLOGY_URI).getOntModel());
		 * 
		 * //ontModel.read(importedOntologyLibrary.FLEXO_CONCEPT_ONTOLOGY_URI);
		 * 
		 * //ontModel.getDocumentManager().addModel(importedOntologyLibrary.
		 * FLEXO_CONCEPT_ONTOLOGY_URI,
		 * importedOntologyLibrary.getOntology(ImportedOntologyLibrary.
		 * FLEXO_CONCEPT_ONTOLOGY_URI).getOntModel());
		 * 
		 * // URI declarations String familyUri = "http://my-pizza.com"; String
		 * relationshipUri = "http://purl.org/vocab/relationship/";
		 * 
		 * // Create an empty Model // Model model =
		 * ModelFactory.createDefaultModel();
		 * 
		 * // Create a Resource for each family member, identified by their URI
		 * OntClass adam = ontModel.createClass(familyUri+"adam"); OntClass beth
		 * = ontModel.createClass(familyUri+"beth"); OntClass chuck =
		 * ontModel.createClass(familyUri+"chuck"); OntClass dotty =
		 * ontModel.createClass(familyUri+"dotty"); // and so on for other
		 * family members
		 * 
		 * // Create properties for the different types of relationship to
		 * represent OntProperty childOf =
		 * ontModel.createOntProperty(relationshipUri+"childOf"); OntProperty
		 * parentOf = ontModel.createOntProperty(relationshipUri+"parentOf");
		 * OntProperty siblingOf =
		 * ontModel.createOntProperty(relationshipUri+"siblingOf"); OntProperty
		 * spouseOf = ontModel.createOntProperty(relationshipUri+"spouseOf");
		 * 
		 * // Add properties to adam describing relationships to other family
		 * members adam.addProperty(siblingOf,beth);
		 * adam.addProperty(spouseOf,dotty); adam.addProperty(parentOf,chuck);
		 * 
		 * // Can also create statements directly . . . Statement statement =
		 * ontModel.createStatement(adam,parentOf,dotty);
		 * 
		 * // but remember to add the created statement to the model
		 * ontModel.add(statement);
		 * 
		 * //ontModel.addLoadedImport(importedOntologyLibrary.
		 * FLEXO_CONCEPT_ONTOLOGY_URI);
		 */

		FileOutputStream out;
		try {
			out = new FileOutputStream(createdFile);
			ontModel.write(out, null/* ,"http://my-pizza.com" */);
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Wrote " + createdFile.getName());

		String SOURCE2 = "@prefix rdf:         <http://www.w3.org/1999/02/22-rdf-syntax-ns#>.\n"
				+ "@prefix rdfs:        <http://www.w3.org/2000/01/rdf-schema#>.\n"
				+ "@prefix owl:         <http://www.w3.org/2002/07/owl#>.\n" + "<" + ONTOLOGY_C + "> a owl:Ontology \n"
				+ "   ; owl:imports <" + ONTOLOGY_D + ">.\n";

		System.out.println("About to load source ontology:");
		System.out.println(SOURCE2);
		ontModel.read(new StringReader(SOURCE2), ONTOLOGY_C, "N3");

		try {
			out = new FileOutputStream(createdFile);
			ontModel.write(out, null/* ,"http://my-pizza.com" */);
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Wrote " + createdFile.getName());
	}

}
