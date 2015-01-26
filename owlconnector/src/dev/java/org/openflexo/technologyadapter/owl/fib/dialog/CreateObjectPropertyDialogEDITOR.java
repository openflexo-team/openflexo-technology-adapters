/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
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

package org.openflexo.technologyadapter.owl.fib.dialog;

import java.io.File;

import org.openflexo.ApplicationContext;
import org.openflexo.TestApplicationContext;
import org.openflexo.fib.editor.FIBAbstractEditor;
import org.openflexo.foundation.resource.DefaultResourceCenterService;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;
import org.openflexo.technologyadapter.owl.controller.OWLAdapterController;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.OWLOntologyLibrary;
import org.openflexo.technologyadapter.owl.model.action.CreateObjectProperty;
import org.openflexo.toolbox.FileResource;

public class CreateObjectPropertyDialogEDITOR extends FIBAbstractEditor {

	@Override
	public Object[] getData() {
		FlexoResourceCenter resourceCenter = DefaultResourceCenterService.getNewInstance().getOpenFlexoResourceCenter();

		ApplicationContext testApplicationContext = new TestApplicationContext(new FileResource("src/test/resources/Ontologies"));
		OWLTechnologyAdapter owlAdapter = testApplicationContext.getTechnologyAdapterService().getTechnologyAdapter(
				OWLTechnologyAdapter.class);
		OWLOntologyLibrary ontologyLibrary = (OWLOntologyLibrary) testApplicationContext.getTechnologyAdapterService()
				.getTechnologyContextManager(owlAdapter);

		OWLOntology ontology = ontologyLibrary
				.getOntology("http://www.agilebirds.com/openflexo/ontologies/ScopeDefinition/OrganizationalUnitScopeDefinition.owl");
		ontology.loadWhenUnloaded();
		CreateObjectProperty action = CreateObjectProperty.actionType.makeNewAction(ontology, null, null);
		return makeArray(action);
	}

	@Override
	public File getFIBFile() {
		return OWLAdapterController.CREATE_OBJECT_PROPERTY_DIALOG_FIB;
	}

	public static void main(String[] args) {
		main(CreateObjectPropertyDialogEDITOR.class);
	}

}
