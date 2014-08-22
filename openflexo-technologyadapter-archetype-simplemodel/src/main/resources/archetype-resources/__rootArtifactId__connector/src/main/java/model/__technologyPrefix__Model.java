#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * (c) Copyright 2013 Openflexo
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

package ${package}.model;


import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import ${package}.${technologyPrefix}TechnologyAdapter;
import ${package}.model.${technologyPrefix}ModelImpl;

@ModelEntity
@ImplementationClass(value = ${technologyPrefix}ModelImpl.class)
public interface ${technologyPrefix}Model extends TechnologyObject<${technologyPrefix}TechnologyAdapter>, ResourceData<${technologyPrefix}Model>{

    public static final String MODEL_ITEM_KEY = "${rootArtifactId}Object";

    @Getter(value = MODEL_ITEM_KEY, ignoreType = true)
    public Object getModelItem();

    @Setter(value =  MODEL_ITEM_KEY)
    public void setModelItem(Object object);

}
