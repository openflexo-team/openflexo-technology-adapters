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

package org.openflexo.technologyadapter.freeplane.fml.structural;

import java.lang.reflect.Type;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.ModelObjectActorReference;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceModelFactory;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.fml.structural.IFreeplaneMapRole.FreeplaneMapRoleImpl;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;

@ModelEntity
@ImplementationClass(value = FreeplaneMapRoleImpl.class)
@XMLElement
public interface IFreeplaneMapRole extends FlexoRole<IFreeplaneMap> {

	public FreeplaneTechnologyAdapter getFreePlaneTechnologyAdapter();

	public abstract static class FreeplaneMapRoleImpl extends FlexoRoleImpl<IFreeplaneMap> implements IFreeplaneMapRole {

		public FreeplaneMapRoleImpl() {
			super();
		}

		/* (non-Javadoc)
		 * @see org.openflexo.foundation.fml.FlexoRole.FlexoRoleImpl#getType()
		 */
		@Override
		public Type getType() {
			return IFreeplaneMap.class;
		}

		/* (non-Javadoc)
		 * @see org.openflexo.foundation.fml.FlexoRole.FlexoRoleImpl#getPreciseType()
		 */
		@Override
		public String getPreciseType() {
			return IFreeplaneMap.class.getSimpleName();
		}

		/* (non-Javadoc)
		 * @see org.openflexo.foundation.fml.FlexoRole#defaultCloningStrategy()
		 */
		@Override
		public RoleCloningStrategy defaultCloningStrategy() {
			return RoleCloningStrategy.Reference;
		}

		/* (non-Javadoc)
		 * @see org.openflexo.foundation.fml.FlexoRole.FlexoRoleImpl#defaultBehaviourIsToBeDeleted()
		 */
		@Override
		public boolean defaultBehaviourIsToBeDeleted() {
			return false;
		}

		/* (non-Javadoc)
		 * @see org.openflexo.foundation.fml.FlexoRole.FlexoRoleImpl#makeActorReference(java.lang.Object, org.openflexo.foundation.fml.rt.FlexoConceptInstance)
		 */
		@Override
		public ActorReference<IFreeplaneMap> makeActorReference(final IFreeplaneMap object, final FlexoConceptInstance epi) {
			final VirtualModelInstanceModelFactory factory = epi.getFactory();
			final ModelObjectActorReference<IFreeplaneMap> returned = factory.newInstance(ModelObjectActorReference.class);
			returned.setFlexoRole(this);
			returned.setFlexoConceptInstance(epi);
			returned.setModellingElement(object);
			return returned;
		}

		/**
		 * 
		 * @return Freeplane technology adapter in service manager.
		 */
		@Override
		public FreeplaneTechnologyAdapter getFreePlaneTechnologyAdapter() {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(FreeplaneTechnologyAdapter.class);
		}
	}
}
