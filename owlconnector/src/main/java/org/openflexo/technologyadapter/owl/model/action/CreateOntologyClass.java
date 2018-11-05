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

package org.openflexo.technologyadapter.owl.model.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.ontology.DuplicateURIException;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;
import org.openflexo.technologyadapter.owl.model.OWLClass;
import org.openflexo.technologyadapter.owl.model.OWLConcept;
import org.openflexo.technologyadapter.owl.model.OWLObject;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.toolbox.StringUtils;

public class CreateOntologyClass extends FlexoAction<CreateOntologyClass, OWLObject, OWLConcept<?>> {

	private static final Logger logger = Logger.getLogger(CreateOntologyClass.class.getPackage().getName());

	public static FlexoActionFactory<CreateOntologyClass, OWLObject, OWLConcept<?>> actionType = new FlexoActionFactory<CreateOntologyClass, OWLObject, OWLConcept<?>>(
			"create_class", FlexoActionFactory.newMenu, FlexoActionFactory.defaultGroup, FlexoActionFactory.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateOntologyClass makeNewAction(OWLObject focusedObject, Vector<OWLConcept<?>> globalSelection, FlexoEditor editor) {
			return new CreateOntologyClass(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(OWLObject object, Vector<OWLConcept<?>> globalSelection) {
			return object != null;
		}

		@Override
		public boolean isEnabledForSelection(OWLObject object, Vector<OWLConcept<?>> globalSelection) {
			return object != null && !object.getOntology().getIsReadOnly();
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateOntologyClass.actionType, OWLOntology.class);
		FlexoObjectImpl.addActionForClass(CreateOntologyClass.actionType, OWLClass.class);
	}

	public String newOntologyClassName;
	public String description;
	public OWLClass fatherClass;

	public String validURILabel;

	private OWLClass newClass;

	private static final String VALID_URI_LABEL = "uri_is_well_formed_and_valid_regarding_its_unicity";
	private static final String INVALID_URI_LABEL = "uri_is_not_valid_please_choose_another_class_name";

	private CreateOntologyClass(OWLObject focusedObject, Vector<OWLConcept<?>> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
		newOntologyClassName = "NewClass";
		fatherClass = focusedObject instanceof OWLClass ? (OWLClass) focusedObject
				: focusedObject.getOntologyLibrary().getOWLOntology().getRootClass();
		isValid();
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getServiceManager() != null) {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(OWLTechnologyAdapter.class).getLocales();
		}
		return super.getLocales();
	}

	@Override
	protected void doAction(Object context) throws DuplicateURIException {
		logger.info("Create IFlexoOntologyClass on " + getFocusedObject());
		newClass = getOntology().createOntologyClass(newOntologyClassName, fatherClass);
	}

	public OWLClass getNewClass() {
		return newClass;
	}

	public OWLOntology getOntology() {
		return getFocusedObject().getFlexoOntology();
	}

	@Override
	public boolean isValid() {
		boolean returned = !StringUtils.isEmpty(newOntologyClassName) && getOntology().testValidURI(newOntologyClassName);
		validURILabel = returned ? getLocales().localizedForKey(VALID_URI_LABEL) : getLocales().localizedForKey(INVALID_URI_LABEL);
		return returned;
	}

}
