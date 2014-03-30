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


package org.openflexo.technologyadapter.csv;

import org.openflexo.foundation.technologyadapter.TypeAwareModelSlotInstanceConfiguration;
import org.openflexo.foundation.view.action.CreateVirtualModelInstance;
import org.openflexo.technologyadapter.csv.metamodel.CSVMetaModel;
import org.openflexo.technologyadapter.csv.model.CSVModel;
import org.openflexo.technologyadapter.csv.rm.CSVMetaModelResource;

public class CSVTypeAwareModelSlotInstanceConfiguration extends TypeAwareModelSlotInstanceConfiguration<CSVModel, CSVMetaModel, CSVTypeAwareModelSlot> {

	protected CSVTypeAwareModelSlotInstanceConfiguration(CSVTypeAwareModelSlot ms, CreateVirtualModelInstance action) {
		super(ms, action);
	}

	@Override
	public void setOption(org.openflexo.foundation.view.action.ModelSlotInstanceConfiguration.ModelSlotInstanceConfigurationOption option) {
		super.setOption(option);
		// TODO : add specific options here
	}

	@Override
	public boolean isURIEditable() {
		return false;
	}

}
