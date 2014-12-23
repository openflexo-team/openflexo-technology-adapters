/*
 * (c) Copyright 2012-2014 Openflexo
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

import java.io.IOException;

import org.openflexo.foundation.technologyadapter.FlexoModelResource;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.XMLTechnologyContextManager;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.model.XMLModel;

@ModelEntity
@ImplementationClass(XMLResourceImpl.class)
public interface XMLResource extends TechnologyAdapterResource<XMLModel, XMLTechnologyAdapter>,
		FlexoModelResource<XMLModel, XMLMetaModel, XMLTechnologyAdapter, XMLTechnologyAdapter> {

	public static final String TECHNOLOGY_CONTEXT_MANAGER = "XMLTechnologyContextManager";
	public static final String TARGET_NAMESPACE = "targetNamespace";

	@Getter(value = TECHNOLOGY_CONTEXT_MANAGER, ignoreType = true)
	public XMLTechnologyContextManager getTechnologyContextManager();

	@Setter(TECHNOLOGY_CONTEXT_MANAGER)
	public void setTechnologyContextManager(XMLTechnologyContextManager technologyContextManager);

	@Getter(value = TARGET_NAMESPACE, ignoreType = true)
	public String getTargetNamespace() throws IOException;

	// initializes the Metamodel property of XMLModel, given the reference provided by metamodelResource property
	public void attachMetamodel();

}
