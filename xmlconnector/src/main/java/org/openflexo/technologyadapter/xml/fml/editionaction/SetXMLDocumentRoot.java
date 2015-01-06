/*
 * (c) Copyright 2012-2013 Openflexo
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

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.fml.editionaction.ProcedureAction;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Import;
import org.openflexo.model.annotations.Imports;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.XMLModelSlot;
import org.openflexo.technologyadapter.xml.fml.XMLActorReference;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLModel;

@ModelEntity
@ImplementationClass(SetXMLDocumentRoot.SetXMLDocumentRootImpl.class)
@XMLElement
@Imports({@Import(XMLActorReference.class),})
public interface SetXMLDocumentRoot extends ProcedureAction<XMLModelSlot, XMLIndividual> {

	public static abstract class SetXMLDocumentRootImpl extends ProcedureActionImpl<XMLModelSlot, XMLIndividual> implements
			SetXMLDocumentRoot {

		private static final Logger logger = Logger.getLogger(SetXMLDocumentRoot.class.getPackage().getName());

		public SetXMLDocumentRootImpl() {
			super();
		}

		@Override
		public XMLIndividual execute(FlexoBehaviourAction action) {

			ModelSlotInstance<XMLModelSlot, XMLModel> modelSlotInstance = (ModelSlotInstance<XMLModelSlot, XMLModel>) getModelSlotInstance(action);
			XMLModel model = modelSlotInstance.getAccessedResourceData();
			XMLModelSlot modelSlot = modelSlotInstance.getModelSlot();

			XMLIndividual rootIndiv = null;

			try {
				Object o = getParameter().getBindingValue(action);
				if (o instanceof XMLIndividual) {
					rootIndiv = (XMLIndividual) o;
				} else {
					logger.warning("Invalid value in Binding :" + getParameter().getUnparsedBinding());
				}
			} catch (TypeMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullReferenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (rootIndiv != null) {
				model.setRoot(rootIndiv);
			}

			return rootIndiv;
		}

	}
}
