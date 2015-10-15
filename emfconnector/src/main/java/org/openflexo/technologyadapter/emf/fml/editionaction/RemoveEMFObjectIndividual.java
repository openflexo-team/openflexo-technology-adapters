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

import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoProperty;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.DeleteAction;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividual;

/**
 * Remove an EMF Object Individual from model.
 * 
 * @author gbesancon
 */
@ModelEntity
@ImplementationClass(RemoveEMFObjectIndividual.RemoveEMFObjectIndividualImpl.class)
@XMLElement
@FML("RemoveEMFObjectIndividual")
public interface RemoveEMFObjectIndividual extends DeleteAction<EMFObjectIndividual>, EMFAction<EMFObjectIndividual> {

	public static abstract class RemoveEMFObjectIndividualImpl extends DeleteActionImpl<EMFObjectIndividual> implements
			RemoveEMFObjectIndividual {

		private static final Logger logger = Logger.getLogger(RemoveEMFObjectIndividual.class.getPackage().getName());

		/**
		 * Follow the link.
		 * 
		 * @see org.openflexo.foundation.fml.editionaction.DeleteAction#execute(RunTimeEvaluationContext)
		 */
		@Override
		public EMFObjectIndividual execute(RunTimeEvaluationContext evaluationContext) {
			// ModelSlotInstance<EMFModel, EMFMetaModel> modelSlotInstance = getModelSlotInstance(action);
			// EObject object = objectIndividual.getObject();
			// EObject container = object.eContainer();
			// EStructuralFeature containmentFeature = object.eContainmentFeature();
			// if (container != null) {
			// // Model Object not root
			// if (containmentFeature.getUpperBound() != 1) {
			// List<EObject> values = (List<EObject>) container.eGet(containmentFeature);
			// values.remove(object);
			// } else {
			// objectIndividual.getObject().eUnset(containmentFeature);
			// }
			// } else {
			// // Root Model Object
			// modelSlotInstance.getModel().getEMFResource().getContents().remove(object);
			// }
			return null;
		}

		@Override
		public FlexoProperty getAssignedFlexoProperty() {
			// TODO Auto-generated method stub
			return super.getAssignedFlexoProperty();
		}
	}
}
