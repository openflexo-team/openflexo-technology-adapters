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

package org.openflexo.technologyadapter.csv.rm;

import org.openflexo.foundation.resource.FlexoFileResource;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.csv.CSVTechnologyAdapter;
import org.openflexo.technologyadapter.csv.CSVTechnologyContextManager;
import org.openflexo.technologyadapter.csv.model.CSVModel;

@ModelEntity
@ImplementationClass(CSVResourceImpl.class)
public abstract interface CSVResource extends FlexoFileResource<CSVModel>, TechnologyAdapterResource<CSVModel, CSVTechnologyAdapter>{
    
    public static final String TECHNOLOGY_CONTEXT_MANAGER = "technologyContextManager";

    @Getter(value="technologyContextManager", ignoreType=true)
    public CSVTechnologyContextManager getTechnologyContextManager();

    @Setter("technologyContextManager")
    public void setTechnologyContextManager(CSVTechnologyContextManager paramCSVTechnologyContextManager);
}

