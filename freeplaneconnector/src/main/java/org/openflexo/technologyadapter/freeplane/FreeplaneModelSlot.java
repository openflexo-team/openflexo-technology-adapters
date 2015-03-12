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


package org.openflexo.technologyadapter.freeplane;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoBehaviours;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.freeplane.FreeplaneModelSlot.FreeplaneModelSlotImpl;
import org.openflexo.technologyadapter.freeplane.fml.editionactions.AddChildNodeAction;
import org.openflexo.technologyadapter.freeplane.fml.editionactions.AddSiblingNodeAction;
import org.openflexo.technologyadapter.freeplane.fml.editionactions.SelectAllNodes;
import org.openflexo.technologyadapter.freeplane.fml.structural.IFreeplaneMapRole;
import org.openflexo.technologyadapter.freeplane.fml.structural.IFreeplaneNodeRole;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;

/**
 * Implementation of the ModelSlot class for the Freeplane technology adapter<br>
 * <p/>
 * We expect here to connect an Freeplane model conform to an FreeplaneMetaModel
 *
 * @author eloubout
 */
@DeclareFlexoRoles({ IFreeplaneNodeRole.class, IFreeplaneMapRole.class })
@DeclareEditionActions({ AddChildNodeAction.class, AddSiblingNodeAction.class })
@DeclareFlexoBehaviours({})
@DeclareFetchRequests({ SelectAllNodes.class })
@ModelEntity
@ImplementationClass(FreeplaneModelSlotImpl.class)
@XMLElement
@FML("FreeplaneModelSlot")
public interface FreeplaneModelSlot extends FreeModelSlot<IFreeplaneMap> {

	public abstract static class FreeplaneModelSlotImpl extends FreeModelSlotImpl<IFreeplaneMap> implements FreeplaneModelSlot {

		private static final Logger LOGGER = Logger.getLogger(FreeplaneModelSlot.class.getPackage().getName());
		private final Map<String, IFreeplaneMap> uriCache = new HashMap<String, IFreeplaneMap>();

		@Override
		public Class<FreeplaneTechnologyAdapter> getTechnologyAdapterClass() {
			return FreeplaneTechnologyAdapter.class;
		}

		/**
		 * Instantiate a new model slot instance configuration for this model slot
		 */
		@Override
		public FreeplaneModelSlotInstanceConfiguration createConfiguration(final CreateVirtualModelInstance action) {
			return new FreeplaneModelSlotInstanceConfiguration(this, action);
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(final Class<PR> patternRoleClass) {
			if (IFreeplaneNodeRole.class.isAssignableFrom(patternRoleClass)) {
				return "node";
			}
			if (IFreeplaneMapRole.class.isAssignableFrom(patternRoleClass)) {
				return "map";
			}
			return null;
		}

		@Override
		public Type getType() {
			return IFreeplaneMap.class;
		}

		@Override
		public FreeplaneTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (FreeplaneTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

		@Override
		public String getURIForObject(final FreeModelSlotInstance<IFreeplaneMap, ? extends FreeModelSlot<IFreeplaneMap>> msInstance,
				final Object o) {
			final IFreeplaneMap fpObject = (IFreeplaneMap) o;

			String builtURI = "";

			try {
				builtURI = URLEncoder.encode(fpObject.getUri(), "UTF-8");
			} catch (final UnsupportedEncodingException e) {
				LOGGER.log(Level.WARNING, "Cannot process URI - Unexpected encoding error", e);
			}

			if (builtURI != null && uriCache.get(builtURI) == null) {
				uriCache.put(builtURI, fpObject);
			}
			return builtURI;
		}

		@Override
		public IFreeplaneMap retrieveObjectWithURI(
				final FreeModelSlotInstance<IFreeplaneMap, ? extends FreeModelSlot<IFreeplaneMap>> msInstance, final String objectURI) {
			return uriCache.get(objectURI);
		}

		@Override
		public TechnologyAdapterResource<IFreeplaneMap, FreeplaneTechnologyAdapter> createProjectSpecificEmptyResource(final View view,
				final String filename, final String modelUri) {
			return getModelSlotTechnologyAdapter().createNewFreeplaneMap(view.getProject(), filename);
		}
	}
}
