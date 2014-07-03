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

package ${package}.rm;

import org.openflexo.foundation.resource.FlexoFileResource;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import ${package}.${technologyPrefix}TechnologyAdapter;
import ${package}.${technologyPrefix}TechnologyContextManager;
import ${package}.model.${technologyPrefix}Model;

@ModelEntity
@ImplementationClass(${technologyPrefix}ResourceImpl.class)
public abstract interface ${technologyPrefix}Resource extends FlexoFileResource<${technologyPrefix}Model>, TechnologyAdapterResource<${technologyPrefix}Model, ${technologyPrefix}TechnologyAdapter>{
    
    public static final String TECHNOLOGY_CONTEXT_MANAGER = "technologyContextManager";

    @Getter(value="technologyContextManager", ignoreType=true)
    public ${technologyPrefix}TechnologyContextManager getTechnologyContextManager();

    @Setter("technologyContextManager")
    public void setTechnologyContextManager(${technologyPrefix}TechnologyContextManager param${technologyPrefix}TechnologyContextManager);
}

