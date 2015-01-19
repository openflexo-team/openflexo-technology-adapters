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

package ${package}.fml;

import java.lang.reflect.Type;

import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.ModelObjectActorReference;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceModelFactory;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import ${package}.${technologyPrefix}TechnologyAdapter;
import ${package}.model.${technologyPrefix}Model;
import ${package}.fml.${technologyPrefix}Role.${technologyPrefix}RoleImpl;

@ModelEntity
@ImplementationClass(value = ${technologyPrefix}RoleImpl.class)
@XMLElement
public interface ${technologyPrefix}Role extends FlexoRole<${technologyPrefix}Model> {

    public ${technologyPrefix}TechnologyAdapter getModelSlotTechnologyAdapter();

    public abstract static class ${technologyPrefix}RoleImpl extends FlexoRoleImpl<${technologyPrefix}Model> implements ${technologyPrefix}Role {

        public ${technologyPrefix}RoleImpl() {
            super();
        }

        /* (non-Javadoc)
         * @see org.openflexo.foundation.viewpoint.FlexoRole.FlexoRoleImpl#getType()
         */
        @Override
        public Type getType() {
            return ${technologyPrefix}Model.class;
        }

        /* (non-Javadoc)
         * @see org.openflexo.foundation.viewpoint.FlexoRole.FlexoRoleImpl#getPreciseType()
         */
        @Override
        public String getPreciseType() {
            return ${technologyPrefix}Model.class.getSimpleName();
        }

        /* (non-Javadoc)
         * @see org.openflexo.foundation.viewpoint.FlexoRole#defaultCloningStrategy()
         */
        @Override
        public RoleCloningStrategy defaultCloningStrategy() {
            return RoleCloningStrategy.Reference;
        }

        /* (non-Javadoc)
         * @see org.openflexo.foundation.viewpoint.FlexoRole.FlexoRoleImpl#defaultBehaviourIsToBeDeleted()
         */
        @Override
        public boolean defaultBehaviourIsToBeDeleted() {
            return false;
        }

        /* (non-Javadoc)
         * @see org.openflexo.foundation.viewpoint.FlexoRole.FlexoRoleImpl#makeActorReference(java.lang.Object, org.openflexo.foundation.view.FlexoConceptInstance)
         */
        @Override
        public ActorReference<${technologyPrefix}Model> makeActorReference(final ${technologyPrefix}Model object, final FlexoConceptInstance epi) {
            final VirtualModelInstanceModelFactory factory = epi.getFactory();
            final ModelObjectActorReference<${technologyPrefix}Model> returned = factory.newInstance(ModelObjectActorReference.class);
            returned.setFlexoRole(this);
            returned.setFlexoConceptInstance(epi);
            returned.setModellingElement(object);
            return returned;
        }

        /**
         * 
         * @return ${technologyPrefix} technology adapter in service manager.
         */
        @Override
        public ${technologyPrefix}TechnologyAdapter getModelSlotTechnologyAdapter() {
            return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(${technologyPrefix}TechnologyAdapter.class);
        }
    }
}
