/*
 * (c) Copyright 2012-2014 Openflexo
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
package org.openflexo.technologyadapter.xml.fml.editionaction;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.XMLModelSlot;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLModel;

@FIBPanel("Fib/GetXMLDocumentRoot.fib")
@ModelEntity
@ImplementationClass(GetXMLDocumentRoot.GetXMLDocumentRootImpl.class)
@XMLElement
public interface GetXMLDocumentRoot extends XMLAction<XMLModelSlot, XMLIndividual> {

	public static abstract class GetXMLDocumentRootImpl extends TechnologySpecificActionImpl<XMLModelSlot, XMLIndividual> implements
			GetXMLDocumentRoot {

		private static final Logger logger = Logger.getLogger(GetXMLDocumentRoot.class.getPackage().getName());

		public GetXMLDocumentRootImpl() {
			super();
		}

		@Override
		public XMLIndividual execute(FlexoBehaviourAction action) {

			ModelSlotInstance<XMLModelSlot, XMLModel> modelSlotInstance = (ModelSlotInstance<XMLModelSlot, XMLModel>) getModelSlotInstance(action);
			XMLModel model = modelSlotInstance.getAccessedResourceData();

			XMLIndividual rootIndiv = model.getRoot();

			return rootIndiv;
		}

		@Override
		public Type getAssignableType() {
			return Object.class;
		}

	}
}
