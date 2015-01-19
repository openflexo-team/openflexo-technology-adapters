#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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

package ${package};

import java.lang.reflect.Type;

import org.openflexo.foundation.technologyadapter.DeclareEditionActions;
import org.openflexo.foundation.technologyadapter.DeclareFetchRequests;
import org.openflexo.foundation.technologyadapter.DeclarePatternRole;
import org.openflexo.foundation.technologyadapter.DeclarePatternRoles;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import ${package}.${technologyPrefix}TechnologyAdapter;
import ${package}.model.${technologyPrefix}Model;
import ${package}.fml.${technologyPrefix}Role;

/**
 * Implementation of the ModelSlot class for the ${technologyPrefix} technology adapter<br>
 * We expect here to connect an ${technologyPrefix} model conform to an ${technologyPrefix}MetaModel
 * 
 * @author ${author}
 * 
 */
@DeclareFlexoRoles({ // All pattern roles available through this model slot
        @DeclareFlexoRole(flexoRoleClass = ${technologyPrefix}Role.class, FML = "Object"),
    })
@DeclareEditionActions({ // All edition actions available through this model slot
    })
@DeclareFetchRequests({ // All requests available through this model slot
    })
@ModelEntity
@ImplementationClass(${technologyPrefix}ModelSlot.${technologyPrefix}ModelSlotImpl.class)
@XMLElement
public interface ${technologyPrefix}ModelSlot extends FreeModelSlot<${technologyPrefix}Model> {

    @Override
    public ${technologyPrefix}TechnologyAdapter getTechnologyAdapter();

    public static abstract class ${technologyPrefix}ModelSlotImpl extends FreeModelSlotImpl<${technologyPrefix}Model> implements ${technologyPrefix}ModelSlot {

        @Override
        public Class<${technologyPrefix}TechnologyAdapter> getTechnologyAdapterClass() {
            return ${technologyPrefix}TechnologyAdapter.class;
        }

        /**
         * Instanciate a new model slot instance configuration for this model slot
         */
        @Override
        public ${technologyPrefix}ModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
            return new ${technologyPrefix}ModelSlotInstanceConfiguration(this, action);
        }

        @Override
        public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
            if (${technologyPrefix}Role.class.isAssignableFrom(patternRoleClass)) {
                return "Object";
        	}
            return "";
        }

        @Override
        public Type getType() {
            return ${technologyPrefix}Model.class;
        }

        @Override
        public ${technologyPrefix}TechnologyAdapter getTechnologyAdapter() {
            return (${technologyPrefix}TechnologyAdapter) super.getTechnologyAdapter();
        }

    }
}
