/*
 * (c) Copyright 2010-2011 AgileBirds
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
package org.openflexo.technologyadapter.diagram.metamodel;

import org.openflexo.foundation.DefaultPamelaResourceModelFactory;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.converter.RelativePathResourceConverter;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.EditingContext;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResource;

/**
 * Diagram specification factory<br>
 * 
 * @author sylvain
 * 
 */
public class DiagramSpecificationFactory extends DefaultPamelaResourceModelFactory<DiagramSpecificationResource> {

	public DiagramSpecificationFactory(DiagramSpecificationResource resource, EditingContext editingContext)
			throws ModelDefinitionException {
		super(resource, ModelContextLibrary.getModelContext(DiagramSpecification.class));
		setEditingContext(editingContext);
		addConverter(new RelativePathResourceConverter(resource.getFlexoIODelegate().getParentPath()));
	}

	public DiagramSpecification makeNewDiagramSpecification() {
		return newInstance(DiagramSpecification.class);
	}

}
