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

import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.FreeModelSlotInstanceConfiguration;
import org.openflexo.technologyadapter.odt.model.ODTDocument;

public class ODTModelSlotInstanceConfiguration extends FreeModelSlotInstanceConfiguration<ODTDocument, ODTModelSlot> {

	protected ODTModelSlotInstanceConfiguration(ODTModelSlot ms, FlexoConceptInstance fci, FlexoResourceCenter<?> rc) {
		super(ms, fci, rc);
	}

	@Override
	public void setOption(
			org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration.ModelSlotInstanceConfigurationOption option) {
		super.setOption(option);
		// TODO : add specific options here
	}

}
