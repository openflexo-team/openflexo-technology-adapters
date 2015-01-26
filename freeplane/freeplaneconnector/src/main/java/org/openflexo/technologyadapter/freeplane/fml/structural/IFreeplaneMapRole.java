/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Freeplane, a component of the software infrastructure 
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


package org.openflexo.technologyadapter.freeplane.fml.structural;

import java.lang.reflect.Type;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.FML;
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
@FML("FreeplaneMapRole")
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
