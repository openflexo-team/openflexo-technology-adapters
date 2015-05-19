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

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.ModelRepository;
import ${package}.${technologyPrefix}TechnologyAdapter;
import ${package}.metamodel.${technologyPrefix}MetaModel;
import ${package}.model.${technologyPrefix}Model;

public class ${technologyPrefix}ModelRepository extends ModelRepository<${technologyPrefix}ModelResource, ${technologyPrefix}Model, ${technologyPrefix}MetaModel, ${technologyPrefix}TechnologyAdapter, ${technologyPrefix}TechnologyAdapter> {

	public ${technologyPrefix}ModelRepository(${technologyPrefix}TechnologyAdapter adapter, FlexoResourceCenter<?> resourceCenter) {
		super(adapter, resourceCenter);
	}

	private static final String DEFAULT_BASE_URI = "http://www.openflexo.org/${technologyPrefix}TechnologyAdapter/Models";

	@Override
	public String getDefaultBaseURI() {
		return DEFAULT_BASE_URI;
	}

}
