/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * Copyright (c) 2012, THALES SYSTEMES AEROPORTES - All Rights Reserved
 * 
 * This file is part of Emfconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.emf.fml.editionaction;

import java.lang.reflect.Type;

import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.emf.EMFModelSlot;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividualAttributeObjectPropertyValue;

/**
 * Remove an Enum value from the attribute of an object.
 * 
 * @author gbesancon
 * 
 */
@ModelEntity
@ImplementationClass(RemoveEMFObjectIndividualAttributeObjectPropertyValue.RemoveEMFObjectIndividualAttributeObjectPropertyValueImpl.class)
@XMLElement
@FML("RemoveEMFObjectIndividualAttributeObjectPropertyValue")
public interface RemoveEMFObjectIndividualAttributeObjectPropertyValue extends EMFAction<EMFObjectIndividualAttributeObjectPropertyValue> {

	public static abstract class RemoveEMFObjectIndividualAttributeObjectPropertyValueImpl extends
			TechnologySpecificActionImpl<EMFModelSlot, EMFObjectIndividualAttributeObjectPropertyValue> implements
			RemoveEMFObjectIndividualAttributeObjectPropertyValue {

		/**
		 * Follow the link.
		 * 
		 * @see org.openflexo.foundation.fml.editionaction.AssignableAction#getAssignableType()
		 */
		@Override
		public Type getAssignableType() {
			// if (value != null) {
			// return value.getClass();
			// }
			return Object.class;
		}

		/**
		 * Follow the link.
		 * 
		 * @see org.openflexo.foundation.fml.editionaction.EditionAction#execute(org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction)
		 */
		@Override
		public EMFObjectIndividualAttributeObjectPropertyValue execute(FlexoBehaviourAction action) {
			EMFObjectIndividualAttributeObjectPropertyValue result = null;
			// ModelSlotInstance<EMFModel, EMFMetaModel> modelSlotInstance = getModelSlotInstance(action);
			// EMFModel model = modelSlotInstance.getModel();
			// // Remove Attribute in EMF
			// if (attributeObjectProperty.getObject().getUpperBound() != 1) {
			// List<T> values = (List<T>) objectIndividual.getObject().eGet(attributeObjectProperty.getObject());
			// values.remove(value);
			// } else {
			// objectIndividual.getObject().eUnset(attributeObjectProperty.getObject());
			// }
			// // Instanciate Wrapper
			// result = model.getConverter().convertObjectIndividualAttributeObjectPropertyValue(model, objectIndividual.getObject(),
			// attributeObjectProperty.getObject());
			return result;
		}

	}

}
