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

package org.openflexo.technologyadapter.freeplane;

import java.lang.reflect.Type;

import org.openflexo.foundation.technologyadapter.DeclareEditionActions;
import org.openflexo.foundation.technologyadapter.DeclareFetchRequests;
import org.openflexo.foundation.technologyadapter.DeclarePatternRole;
import org.openflexo.foundation.technologyadapter.DeclarePatternRoles;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.view.action.CreateVirtualModelInstance;
import org.openflexo.foundation.view.action.ModelSlotInstanceConfiguration;
import org.openflexo.foundation.viewpoint.FlexoRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.roles.IFreeplaneMapRole;
import org.openflexo.technologyadapter.freeplane.model.roles.IFreeplaneNodeRole;

/**
 * Implementation of the ModelSlot class for the Freeplane technology adapter<br>
 * 
 * We expect here to connect an Freeplane model conform to an FreeplaneMetaModel
 * 
 * @author eloubout
 * 
 */
@DeclarePatternRoles({ // All pattern roles available through this model slot
@DeclarePatternRole(flexoRoleClass = IFreeplaneNodeRole.class, FML = "Node"),
        @DeclarePatternRole(flexoRoleClass = IFreeplaneMapRole.class, FML = "Map") })
@DeclareEditionActions({ // All edition actions available through this model
// slot
})
@DeclareFetchRequests({ // All requests available through this model slot
})
@ModelEntity
@ImplementationClass(IFreeplaneModelSlot.FreeplaneModelSlotImpl.class)
@XMLElement
public interface IFreeplaneModelSlot extends ModelSlot<IFreeplaneMap> {

    public static abstract class FreeplaneModelSlotImpl extends ModelSlotImpl<IFreeplaneMap> implements IFreeplaneModelSlot {

        @Override
        public Class<FreeplaneTechnologyAdapter> getTechnologyAdapterClass() {
            return FreeplaneTechnologyAdapter.class;
        }

        /**
         * Instanciate a new model slot instance configuration for this model
         * slot
         */
        @Override
        public ModelSlotInstanceConfiguration<? extends ModelSlot<IFreeplaneMap>, IFreeplaneMap> createConfiguration(
                final CreateVirtualModelInstance action) {
            return new FreeplaneModelSlotInstanceConfiguration(this, action);
        }

        @Override
        public <PR extends FlexoRole<?>> String defaultFlexoRoleName(final Class<PR> patternRoleClass) {
            if (IFreeplaneNodeRole.class.isAssignableFrom(patternRoleClass)) {
                return "Node";
            }
            if (IFreeplaneMapRole.class.isAssignableFrom(patternRoleClass)) {
                return "Map";
            }
            return null;
        }

        @Override
        public Type getType() {
            return IFreeplaneMap.class;
        }

        @Override
        public FreeplaneTechnologyAdapter getTechnologyAdapter() {
            return (FreeplaneTechnologyAdapter) super.getTechnologyAdapter();
        }

    }
}
