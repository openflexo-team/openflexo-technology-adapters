/*
 * (c) Copyright 2013- Openflexo
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

package org.openflexo.technologyadapter.gina;

import java.lang.reflect.Type;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.gina.fml.FIBComponentRole;
import org.openflexo.technologyadapter.gina.fml.model.GINAFIBComponent;

/**
 * Implementation of the ModelSlot class for the GIN technology adapter<br>
 * We expect here to connect an GIN model conform to an GINMetaModel
 * 
 * @author Sylvain Guérin
 * 
 */
@DeclareFlexoRoles({ FIBComponentRole.class })
@DeclareEditionActions({})
@DeclareFetchRequests({})
@ModelEntity
@ImplementationClass(GINAModelSlot.GINModelSlotImpl.class)
@XMLElement
public interface GINAModelSlot extends FreeModelSlot<GINAFIBComponent> {

	@Override
	public GINATechnologyAdapter getModelSlotTechnologyAdapter();

	public static abstract class GINModelSlotImpl extends FreeModelSlotImpl<GINAFIBComponent>implements GINAModelSlot {

		@Override
		public Class<GINATechnologyAdapter> getTechnologyAdapterClass() {
			return GINATechnologyAdapter.class;
		}

		/**
		 * Instanciate a new model slot instance configuration for this model slot
		 */
		/*@Override
		public GINAModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
		    return new GINAModelSlotInstanceConfiguration(this, action);
		}*/

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (FIBComponentRole.class.isAssignableFrom(patternRoleClass)) {
				return "Object";
			}
			return "";
		}

		@Override
		public Type getType() {
			return FIBComponent.class;
		}

		@Override
		public GINATechnologyAdapter getModelSlotTechnologyAdapter() {
			return (GINATechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

	}
}
