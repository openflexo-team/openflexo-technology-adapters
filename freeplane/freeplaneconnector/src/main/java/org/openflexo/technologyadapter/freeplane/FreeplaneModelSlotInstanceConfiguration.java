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

import java.util.List;

import org.openflexo.foundation.view.ModelSlotInstance;
import org.openflexo.foundation.view.View;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.view.action.CreateVirtualModelInstance;
import org.openflexo.foundation.view.action.ModelSlotInstanceConfiguration;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;

public class FreeplaneModelSlotInstanceConfiguration extends ModelSlotInstanceConfiguration<IFreeplaneModelSlot, IFreeplaneMap> {

    protected FreeplaneModelSlotInstanceConfiguration(final IFreeplaneModelSlot ms, final CreateVirtualModelInstance action) {
        super(ms, action);
    }

    @Override
    public void setOption(
            final org.openflexo.foundation.view.action.ModelSlotInstanceConfiguration.ModelSlotInstanceConfigurationOption option) {
        super.setOption(option);
        // TODO : add specific options here
    }

    @Override
    public List<org.openflexo.foundation.view.action.ModelSlotInstanceConfiguration.ModelSlotInstanceConfigurationOption> getAvailableOptions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ModelSlotInstance<IFreeplaneModelSlot, IFreeplaneMap> createModelSlotInstance(final VirtualModelInstance vmInstance,
            final View view) {
        // TODO Auto-generated method stub
        return null;
    }

}
