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

package org.openflexo.technologyadapter.gina.fml;

import java.lang.reflect.Type;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceModelFactory;
import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.ModelObjectActorReference;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.fml.FIBComponentRole.FIBComponentRoleImpl;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;

@ModelEntity
@ImplementationClass(FIBComponentRoleImpl.class)
@XMLElement
public interface FIBComponentRole extends FlexoRole<GINAFIBComponent> {

	public GINATechnologyAdapter getModelSlotTechnologyAdapter();

	public abstract static class FIBComponentRoleImpl extends FlexoRoleImpl<GINAFIBComponent>implements FIBComponentRole {

		@Override
		public Type getType() {
			return FIBComponent.class;
		}

		@Override
		public RoleCloningStrategy defaultCloningStrategy() {
			return RoleCloningStrategy.Reference;
		}

		@Override
		public boolean defaultBehaviourIsToBeDeleted() {
			return false;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ActorReference<GINAFIBComponent> makeActorReference(final GINAFIBComponent object, final FlexoConceptInstance fci) {
			final VirtualModelInstanceModelFactory<?> factory = fci.getFactory();
			final ModelObjectActorReference<GINAFIBComponent> returned = factory.newInstance(ModelObjectActorReference.class);
			returned.setFlexoRole(this);
			returned.setFlexoConceptInstance(fci);
			returned.setModellingElement(object);
			return returned;
		}

		/**
		 * 
		 * @return GINA technology adapter in service manager.
		 */
		@Override
		public GINATechnologyAdapter getModelSlotTechnologyAdapter() {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(GINATechnologyAdapter.class);
		}

		@Override
		public Class<? extends TechnologyAdapter> getRoleTechnologyAdapterClass() {
			return GINATechnologyAdapter.class;
		}

	}
}
