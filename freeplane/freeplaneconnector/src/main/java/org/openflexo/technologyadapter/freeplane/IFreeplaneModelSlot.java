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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.technologyadapter.DeclareEditionAction;
import org.openflexo.foundation.technologyadapter.DeclareEditionActions;
import org.openflexo.foundation.technologyadapter.DeclarePatternRole;
import org.openflexo.foundation.technologyadapter.DeclarePatternRoles;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.foundation.view.FreeModelSlotInstance;
import org.openflexo.foundation.view.action.CreateVirtualModelInstance;
import org.openflexo.foundation.viewpoint.FlexoRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.freeplane.IFreeplaneModelSlot.FreeplaneModelSlotImpl;
import org.openflexo.technologyadapter.freeplane.fml.IFreeplaneMapRole;
import org.openflexo.technologyadapter.freeplane.fml.IFreeplaneNodeRole;
import org.openflexo.technologyadapter.freeplane.fml.editionactions.AddChildNodeAction;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;

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
@DeclareEditionActions({ @DeclareEditionAction(editionActionClass = AddChildNodeAction.class, FML = "AddChildNode") })
@ModelEntity
@ImplementationClass(FreeplaneModelSlotImpl.class)
@XMLElement
public interface IFreeplaneModelSlot extends FreeModelSlot<IFreeplaneMap> {

	public static abstract class FreeplaneModelSlotImpl extends FreeModelSlotImpl<IFreeplaneMap> implements IFreeplaneModelSlot {

		private static final Logger LOGGER = Logger.getLogger(IFreeplaneModelSlot.class.getPackage().getName());
		private Map<String, IFreeplaneMap> uriCache;

		@Override
		public Class<FreeplaneTechnologyAdapter> getTechnologyAdapterClass() {
			return FreeplaneTechnologyAdapter.class;
		}

		/**
		 * Instanciate a new model slot instance configuration for this model
		 * slot
		 */
		@Override
		public FreeplaneModelSlotInstanceConfiguration createConfiguration(final CreateVirtualModelInstance action) {
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

		@Override
		public String getURIForObject(final FreeModelSlotInstance<IFreeplaneMap, ? extends FreeModelSlot<IFreeplaneMap>> msInstance,
				final Object o) {
			final IFreeplaneMap fpObject = (IFreeplaneMap) o;

			String builtURI = null;

			try {
				builtURI = URLEncoder.encode(fpObject.getUri(), "UTF-8");
			} catch (final UnsupportedEncodingException e) {
				LOGGER.log(Level.WARNING, "Cannot process URI - Unexpected encoding error", e);
			}

			if (builtURI != null) {
				if (uriCache.get(builtURI) == null) {
					uriCache.put(builtURI, fpObject);
				}
			}
			return builtURI.toString();
		}

		@Override
		public IFreeplaneMap retrieveObjectWithURI(
				final FreeModelSlotInstance<IFreeplaneMap, ? extends FreeModelSlot<IFreeplaneMap>> msInstance, final String objectURI) {
			return uriCache.get(objectURI);
		}

	}
}
