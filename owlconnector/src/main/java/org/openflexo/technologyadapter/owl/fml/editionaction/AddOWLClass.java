/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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

package org.openflexo.technologyadapter.owl.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.foundation.ontology.DuplicateURIException;
import org.openflexo.foundation.ontology.fml.editionaction.AddClass;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.owl.OWLModelSlot;
import org.openflexo.technologyadapter.owl.model.OWLClass;
import org.openflexo.technologyadapter.owl.model.OWLOntology;

@ModelEntity
@ImplementationClass(AddOWLClass.AddOWLClassImpl.class)
@XMLElement
@FML("AddOWLClass")
public interface AddOWLClass extends AddClass<OWLModelSlot, OWLClass>, OWLAction<OWLClass> {

	public static abstract class AddOWLClassImpl extends AddClassImpl<OWLModelSlot, OWLClass> implements AddOWLClass {

		private static final Logger logger = Logger.getLogger(AddOWLClass.class.getPackage().getName());

		private final String dataPropertyURI = null;

		public AddOWLClassImpl() {
			super();
		}

		@Override
		public OWLClass getOntologyClass() {
			return (OWLClass) super.getOntologyClass();
		}

		@Override
		public Class<OWLClass> getOntologyClassClass() {
			return OWLClass.class;
		}

		@Override
		public OWLClass execute(FlexoBehaviourAction action) {
			OWLClass father = getOntologyClass();
			String newClassName = null;
			try {
				newClassName = getClassName().getBindingValue(action);
			} catch (TypeMismatchException e1) {
				e1.printStackTrace();
			} catch (NullReferenceException e1) {
				e1.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			OWLClass newClass = null;
			try {
				logger.info("Adding class " + newClassName + " as " + father);
				newClass = getModelSlotInstance(action).getAccessedResourceData().createOntologyClass(newClassName, father);
				logger.info("Added class " + newClass.getName() + " as " + father);
			} catch (DuplicateURIException e) {
				e.printStackTrace();
			}
			return newClass;
		}

		@Override
		public TypeAwareModelSlotInstance<OWLOntology, OWLOntology, OWLModelSlot> getModelSlotInstance(FlexoBehaviourAction action) {
			return (TypeAwareModelSlotInstance<OWLOntology, OWLOntology, OWLModelSlot>) super.getModelSlotInstance(action);
		}

	}
}
