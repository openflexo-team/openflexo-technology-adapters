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
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import ${package}.${technologyPrefix}TechnologyAdapter;
import ${package}.metamodel.${technologyPrefix}MetaModel;
import ${package}.model.${technologyPrefix}Model;

@ModelEntity
@ImplementationClass(${technologyPrefix}MetaModelResourceImpl.class)
@XMLElement
public interface ${technologyPrefix}MetaModelResource extends FlexoFileResource<${technologyPrefix}MetaModel>,
		FlexoMetaModelResource<${technologyPrefix}Model, ${technologyPrefix}MetaModel, ${technologyPrefix}TechnologyAdapter> {

	public static final String EXTENSION = "${technologyExtension}";

	/**
	 * Setter of extension for model files related to this MetaModel.
	 * 
	 * @return
	 */
	@Setter(EXTENSION)
	void setModelFileExtension(String modelFileExtension);

	/**
	 * Getter of extension for model files related to this MetaModel.
	 * 
	 * @return
	 */
	@Getter(EXTENSION)
	String getModelFileExtension();

	
	/**
	 * Get the MetaModel stored in the Resource..
	 * 
	 * @return
	 */
	@Override
	public ${technologyPrefix}MetaModel getMetaModelData();
}
