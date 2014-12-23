/*
 * (c) Copyright 2010-2012 AgileBirds
 * (c) Copyright 2013 Openflexo
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

package org.openflexo.technologyadapter.emf;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.technologyadapter.DeclareEditionActions;
import org.openflexo.foundation.technologyadapter.DeclareFetchRequests;
import org.openflexo.foundation.technologyadapter.DeclarePatternRole;
import org.openflexo.foundation.technologyadapter.DeclarePatternRoles;
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
@DeclarePatternRoles({ // All pattern roles available through this model slot
@DeclarePatternRole(FML = "EMFClassClass", flexoRoleClass = EMFClassClassRole.class),
		@DeclarePatternRole(FML = "EMFEnumClass", flexoRoleClass = EMFEnumClassRole.class) })
@DeclareEditionActions({ // All edition actions available through this model
// slot
})
@DeclareFetchRequests({ // All requests available through this model slot
})
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
