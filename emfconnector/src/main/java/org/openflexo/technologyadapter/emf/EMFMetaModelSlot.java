/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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


package org.openflexo.technologyadapter.emf;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.technologyadapter.emf.fml.EMFClassClassRole;
import org.openflexo.technologyadapter.emf.fml.EMFEnumClassRole;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;

/**
 * Implementation of the ModelSlot class for the EMF technology adapter<br>
 * We expect here to connect an EMF meta model
 * 
 * @author sylvain
 * 
 */
@DeclareFlexoRoles({ EMFClassClassRole.class, EMFEnumClassRole.class })
@DeclareEditionActions({})
@DeclareFetchRequests({})
@FML("EMFModelSlot")
public interface EMFMetaModelSlot extends ModelSlot<EMFMetaModel> {

	public abstract static class EMFMetaModelSlotImpl extends ModelSlotImpl<EMFMetaModel> implements EMFMetaModelSlot {

		private static final Logger logger = Logger.getLogger(EMFMetaModelSlot.class.getPackage().getName());

		@Override
		public Class<EMFTechnologyAdapter> getTechnologyAdapterClass() {
			return EMFTechnologyAdapter.class;
		}

		/**
		 * Instanciate a new model slot instance configuration for this model slot
		 */
		@Override
		public EMFMetaModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
			return new EMFMetaModelSlotInstanceConfiguration(this, action);
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (EMFClassClassRole.class.isAssignableFrom(patternRoleClass)) {
				return "class";
			} else if (EMFEnumClassRole.class.isAssignableFrom(patternRoleClass)) {
				return "enum";
			}
			return null;
		}

		@Override
		public Type getType() {
			return EMFMetaModel.class;
		}

		@Override
		public EMFTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (EMFTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

		@Override
		public String getURIForObject(ModelSlotInstance<? extends ModelSlot<EMFMetaModel>, EMFMetaModel> msInstance, Object o) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object retrieveObjectWithURI(ModelSlotInstance<? extends ModelSlot<EMFMetaModel>, EMFMetaModel> msInstance, String objectURI) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
