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

import java.lang.reflect.Type;

import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.csv.CSVTechnologyAdapter;
import org.openflexo.technologyadapter.csv.model.CSVModel;
import org.openflexo.technologyadapter.csv.fml.CSVRole;

/**
 * Implementation of the ModelSlot class for the CSV technology adapter<br>
 * We expect here to connect an CSV model conform to an CSVMetaModel
 * 
 * @author Jean Le Paon
 * 
 */
@DeclareFlexoRoles({CSVRole.class})
@DeclareEditionActions({})
@DeclareFetchRequests({})
@ModelEntity
@ImplementationClass(CSVModelSlot.CSVModelSlotImpl.class)
@XMLElement
public interface CSVModelSlot extends FreeModelSlot<CSVModel> {

    @Override
    public CSVTechnologyAdapter getModelSlotTechnologyAdapter();

    public static abstract class CSVModelSlotImpl extends FreeModelSlotImpl<CSVModel> implements CSVModelSlot {

        @Override
        public Class<CSVTechnologyAdapter> getTechnologyAdapterClass() {
            return CSVTechnologyAdapter.class;
        }

        /**
         * Instanciate a new model slot instance configuration for this model slot
         */
        @Override
        public CSVModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
            return new CSVModelSlotInstanceConfiguration(this, action);
        }

        @Override
        public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
            if (CSVRole.class.isAssignableFrom(patternRoleClass)) {
                return "Object";
        	}
            return "";
        }

        @Override
        public Type getType() {
            return CSVModel.class;
        }

        @Override
        public CSVTechnologyAdapter getModelSlotTechnologyAdapter() {
            return (CSVTechnologyAdapter) super.getModelSlotTechnologyAdapter();
        }

    }
}
