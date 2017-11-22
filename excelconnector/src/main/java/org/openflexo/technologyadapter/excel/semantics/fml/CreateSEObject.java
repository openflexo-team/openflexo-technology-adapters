/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Flexo-foundation, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel.semantics.fml;

import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.fml.rt.editionaction.AbstractAddFlexoConceptInstance;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.semantics.model.SEFlexoConceptInstance;
import org.openflexo.technologyadapter.excel.semantics.model.SEVirtualModelInstance;

/**
 * Create a new object as a new row in excel workbook
 * 
 * @author sylvain
 */
@ModelEntity
@ImplementationClass(CreateSEObject.CreateSEObjectImpl.class)
@XMLElement
public interface CreateSEObject extends AbstractAddFlexoConceptInstance<SEFlexoConceptInstance, SEVirtualModelInstance> {

	public static abstract class CreateSEObjectImpl
			extends AbstractAddFlexoConceptInstanceImpl<SEFlexoConceptInstance, SEVirtualModelInstance> implements CreateSEObject {

		private static final Logger logger = Logger.getLogger(CreateSEObject.class.getPackage().getName());

		@Override
		public SEFlexoConceptInstance execute(RunTimeEvaluationContext evaluationContext) throws FlexoException {
			SEVirtualModelInstance vmi = getVirtualModelInstance(evaluationContext);

			System.out.println("CreateSEObject for receiver " + getReceiver() + " = " + vmi + " concept=" + getFlexoConceptType());

			SEFlexoConceptInstance returned = super.execute(evaluationContext);

			return returned;

		}

		@Override
		protected SEFlexoConceptInstance makeNewFlexoConceptInstance(RunTimeEvaluationContext evaluationContext) throws FlexoException {
			FlexoConceptInstance container = null;
			SEVirtualModelInstance vmi = getVirtualModelInstance(evaluationContext);

			if (getFlexoConceptType().getContainerFlexoConcept() != null) {
				container = getContainer(evaluationContext);
				if (container == null) {
					logger.warning("null container while creating new concept " + getFlexoConceptType());
					return null;
				}
			}

			SEFlexoConceptInstance returned = vmi.makeNewFlexoConceptInstance(getFlexoConceptType(), container);

			return returned;
		}

		@Override
		public TechnologyAdapter getModelSlotTechnologyAdapter() {
			if (getServiceManager() != null) {
				return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(ExcelTechnologyAdapter.class);
			}
			return super.getModelSlotTechnologyAdapter();
		}

		@Override
		public Class<SEVirtualModelInstance> getVirtualModelInstanceClass() {
			return SEVirtualModelInstance.class;
		}
	}

}
