#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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


package ${package};

import java.util.Collections;
import java.util.List;

import org.openflexo.foundation.view.ModelSlotInstance;
import org.openflexo.foundation.view.View;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.view.action.CreateVirtualModelInstance;
import org.openflexo.foundation.view.action.ModelSlotInstanceConfiguration;
import ${package}.model.${technologyPrefix}Model;

public class ${technologyPrefix}ModelSlotInstanceConfiguration extends ModelSlotInstanceConfiguration<${technologyPrefix}ModelSlot, ${technologyPrefix}Model> {

    protected ${technologyPrefix}ModelSlotInstanceConfiguration(${technologyPrefix}ModelSlot ms, CreateVirtualModelInstance action) {
        super(ms, action);
    }

    @Override
    public void setOption(ModelSlotInstanceConfigurationOption option) {
        super.setOption(option);
        // TODO : add specific options here
    }

    /**
     * @return empty list
     */
    @Override
    public List<ModelSlotInstanceConfigurationOption> getAvailableOptions() {
        return Collections.emptyList();
    }
    
    @Override
    public ModelSlotInstance<${technologyPrefix}ModelSlot, ${technologyPrefix}Model> createModelSlotInstance(final VirtualModelInstance vmInstance, final View view) {
        return null;
    }
}
