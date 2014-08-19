/*
 * (c) Copyright 2010-2011 AgileBirds
 * (c) Copyright 2014 - Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.technologyadapter.xml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.ontology.DuplicateURIException;
import org.openflexo.foundation.view.TypeAwareModelSlotInstance;
import org.openflexo.foundation.view.action.FlexoBehaviourAction;
import org.openflexo.foundation.viewpoint.editionaction.AddClass;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.XSDModelSlot;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;

@ModelEntity
@ImplementationClass(AddXMLType.AddXMLTypeImpl.class)
@XMLElement
public interface AddXMLType extends AddClass<XSDModelSlot, XMLType> {

	public static abstract class AddXMLTypeImpl extends AddClassImpl<XSDModelSlot, XMLType> implements AddXMLType {

		private static final Logger logger = Logger.getLogger(AddXMLType.class.getPackage().getName());

		private final String dataPropertyURI = null;

		public AddXMLTypeImpl() {
			super();
		}

		@Override
		public XMLType getOntologyClass() {
			return (XMLType) super.getOntologyClass();
		}

		@Override
		public Class<XMLType> getOntologyClassClass() {
			return XMLType.class;
		}

		@Override
		public XMLType performAction(FlexoBehaviourAction action) {
			XMLType father = getOntologyClass();
			String newClassName = null;
			try {
				newClassName = getClassName().getBindingValue(action);
			} catch (TypeMismatchException e1) {
				e1.printStackTrace();
			} catch (NullReferenceException e1) {
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
			}
			XMLType newClass = null;
			try {
				logger.info("Adding class " + newClassName + " as " + father);
				// FIXME : Something wrong here!
				// newClass = getModelSlotInstance(action).getModel().getMetaModel().createOntologyClass(newClassName, father);
				newClass = getModelSlotInstance(action).getAccessedResourceData().getMetaModel().createOntologyClass(newClassName, father);
				logger.info("Added class " + newClass.getName() + " as " + father);
			} catch (DuplicateURIException e) {
				e.printStackTrace();
			}
			return newClass;
		}

		@Override
		public TypeAwareModelSlotInstance<XMLXSDModel, XMLMetaModel, XSDModelSlot> getModelSlotInstance(FlexoBehaviourAction action) {
			return (TypeAwareModelSlotInstance<XMLXSDModel, XMLMetaModel, XSDModelSlot>) super.getModelSlotInstance(action);
		}

	}
}
