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

package org.openflexo.technologyadapter.odt;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.rt.action.AbstractCreateVirtualModelInstance;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.odt.fml.ODTParagraphRole;
import org.openflexo.technologyadapter.odt.fml.action.AddODTParagraph;
import org.openflexo.technologyadapter.odt.model.ODTDocument;

/**
 * Implementation of the ModelSlot class for the ODT technology adapter<br>
 * We expect here to connect an .odt document
 * 
 * @author sylvain
 * 
 */
@DeclareFlexoRoles({ ODTParagraphRole.class })
@DeclareEditionActions({ AddODTParagraph.class })
@ModelEntity
@ImplementationClass(ODTModelSlot.ODTModelSlotImpl.class)
@XMLElement
public interface ODTModelSlot extends FreeModelSlot<ODTDocument> {

	@Override
	public ODTTechnologyAdapter getModelSlotTechnologyAdapter();

	public static abstract class ODTModelSlotImpl extends FreeModelSlotImpl<ODTDocument> implements ODTModelSlot {

		private static final Logger logger = Logger.getLogger(ODTModelSlot.class.getPackage().getName());

		@Override
		public Class<ODTTechnologyAdapter> getTechnologyAdapterClass() {
			return ODTTechnologyAdapter.class;
		}

		/**
		 * Instanciate a new model slot instance configuration for this model slot
		 */
		@Override
		public ODTModelSlotInstanceConfiguration createConfiguration(AbstractCreateVirtualModelInstance action) {
			return new ODTModelSlotInstanceConfiguration(this, action);
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (ODTParagraphRole.class.isAssignableFrom(patternRoleClass)) {
				return "document";
			}
			return null;
		}

		@Override
		public Type getType() {
			return ODTDocument.class;
		}

		@Override
		public ODTTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (ODTTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

	}
}
