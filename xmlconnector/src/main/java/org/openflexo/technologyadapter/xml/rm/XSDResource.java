/*
 * (c) Copyright 2014-  Openflexo
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

package org.openflexo.technologyadapter.xml.rm;

import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.XMLTechnologyContextManager;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.model.XMLModel;

@ModelEntity
// @ImplementationClass(XSDResourceImpl.class)
public interface XSDResource extends  TechnologyAdapterResource<XMLMetaModel, XMLTechnologyAdapter>, FlexoMetaModelResource<XMLModel, XMLMetaModel, XMLTechnologyAdapter> {

	public static final String TECHNOLOGY_CONTEXT_MANAGER = "XMLTechnologyContextManager";

	@Getter(value = TECHNOLOGY_CONTEXT_MANAGER, ignoreType = true)
	public XMLTechnologyContextManager getTechnologyContextManager();

	@Setter(TECHNOLOGY_CONTEXT_MANAGER)
	public void setTechnologyContextManager(XMLTechnologyContextManager technologyContextManager);
}
