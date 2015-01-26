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

package org.openflexo.components.widget;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.openflexo.TestApplicationContext;
import org.openflexo.fib.controller.FIBController;
import org.openflexo.fib.editor.FIBAbstractEditor;
import org.openflexo.fib.model.FIBComponent;
import org.openflexo.logging.FlexoLoggingManager;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.OWLOntologyLibrary;
import org.openflexo.toolbox.FileResource;
import org.openflexo.view.controller.FlexoFIBController;

public class FIBOntologyEditorEDITOR {

	// To have icon, you need to choose "Test interface" in the editor (otherwise, flexo controller is not insanciated in EDIT mode)
	public static void main(String[] args) {

		try {
			FlexoLoggingManager.initialize(-1, true, null, Level.INFO, null);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TestApplicationContext testApplicationContext = new TestApplicationContext(new FileResource("TestResourceCenter"));
		OWLTechnologyAdapter owlAdapter = testApplicationContext.getTechnologyAdapterService().getTechnologyAdapter(
				OWLTechnologyAdapter.class);
		OWLOntologyLibrary ontologyLibrary = (OWLOntologyLibrary) testApplicationContext.getTechnologyAdapterService()
				.getTechnologyContextManager(owlAdapter);

		// selector.setContext(resourceCenter.retrieveBaseOntologyLibrary().getFlexoConceptOntology());
		OWLOntology o = ontologyLibrary.getOntology(
		// "http://www.thalesgroup.com/ontologies/sepel-ng/MappingSpecifications.owl");
		// "http://www.cpmf.org/ontologies/cpmfInstance");
				"http://www.agilebirds.com/openflexo/ontologies/FlexoConceptsOntology.owl");
		// "http://www.openflexo.org/test/TestInstances.owl");
		// "http://www.w3.org/2002/07/owl");
		// "http://www.w3.org/2000/01/rdf-schema");
		o.loadWhenUnloaded();
		final FIBOntologyEditor selector = new FIBOntologyEditor(o, null);
		selector.setOntology(o);
		selector.setHierarchicalMode(false); // false
		selector.setShowAnnotationProperties(true);
		selector.setShowObjectProperties(true);
		selector.setShowDataProperties(true);
		selector.setShowClasses(true);
		selector.setShowIndividuals(true);
		selector.setStrictMode(false);
		// selector.setRootClass(transformationRule);

		FIBAbstractEditor editor = new FIBAbstractEditor() {
			@Override
			public Object[] getData() {
				return makeArray(selector);
			}

			@Override
			public File getFIBFile() {
				return FIBOntologyEditor.FIB_FILE;
			}

			@Override
			public FIBController makeNewController(FIBComponent component) {
				return new FlexoFIBController(component);
			}
		};
		editor.launch();
	}

	/*public static void main2(String[] args) {
		JFrame frame = new JFrame();
		FlexoResourceCenter resourceCenter = DefaultResourceCenterService.getNewInstance().getOpenFlexoResourceCenter();
		// selector.setContext(resourceCenter.retrieveBaseOntologyLibrary().getFlexoConceptOntology());
		FlexoOntology o = resourceCenter.retrieveBaseOntologyLibrary().getOntology(
		// "http://www.thalesgroup.com/ontologies/sepel-ng/MappingSpecifications.owl");
				"http://www.w3.org/2002/07/owl");
		// "http://www.cpmf.org/ontologies/cpmfInstance");
		o.loadWhenUnloaded();
		System.out.println("ontology: " + o);
		FIBOWLOntologyEditor browser = new FIBOWLOntologyEditor(o, null);
		browser.setOntology(o);
		frame.getContentPane().add(browser);
		frame.pack();
		frame.setVisible(true);
	}*/

}
