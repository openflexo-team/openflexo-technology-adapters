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

import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.technologyadapter.FlexoModelResource;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import ${package}.${technologyPrefix}TechnologyAdapter;
import ${package}.${technologyPrefix}TechnologyContextManager;
import ${package}.metamodel.${technologyPrefix}MetaModel;
import ${package}.model.${technologyPrefix}Model;

@ModelEntity
@ImplementationClass(${technologyPrefix}ModelResourceImpl.class)
public abstract interface ${technologyPrefix}ModelResource extends FlexoResource<${technologyPrefix}Model>, FlexoModelResource<${technologyPrefix}Model, ${technologyPrefix}MetaModel, ${technologyPrefix}TechnologyAdapter, ${technologyPrefix}TechnologyAdapter>
{
  public static final String TECHNOLOGY_CONTEXT_MANAGER = "technologyContextManager";

  @Getter(value="technologyContextManager", ignoreType=true)
  public abstract ${technologyPrefix}TechnologyContextManager getTechnologyContextManager();

  @Setter("technologyContextManager")
  public abstract void setTechnologyContextManager(${technologyPrefix}TechnologyContextManager param${technologyPrefix}TechnologyContextManager);
}

