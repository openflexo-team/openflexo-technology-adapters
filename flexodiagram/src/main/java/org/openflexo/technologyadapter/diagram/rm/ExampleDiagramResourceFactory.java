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

package org.openflexo.technologyadapter.diagram.rm;

import java.io.IOException;
import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.diagram.model.Diagram;

/**
 * Implementation of PamelaResourceFactory for {@link DiagramResource}
 * 
 * @author sylvain
 *
 */
public class ExampleDiagramResourceFactory extends DiagramResourceFactory {

	private static final Logger logger = Logger.getLogger(ExampleDiagramResourceFactory.class.getPackage().getName());

	public ExampleDiagramResourceFactory() throws ModelDefinitionException {
		super();
	}

	public <I> DiagramResource retrieveExampleDiagramResource(I serializationArtefact, DiagramSpecificationResource dsResource)
			throws ModelDefinitionException, IOException {

		FlexoResourceCenter<I> resourceCenter = (FlexoResourceCenter<I>) dsResource.getResourceCenter();
		String name = resourceCenter.retrieveName(serializationArtefact);

		DiagramResource returned = initResourceForRetrieving(serializationArtefact, resourceCenter);
		returned.setURI(dsResource.getURI() + "/" + name);

		dsResource.addToContents(returned);
		dsResource.notifyContentsAdded(returned);

		registerResource(returned, resourceCenter);

		return returned;
	}

	public <I> DiagramResource makeExampleDiagramResource(String name, DiagramSpecificationResource dsResource, boolean createEmptyContents)
			throws SaveResourceException, ModelDefinitionException {

		FlexoResourceCenter<I> resourceCenter = (FlexoResourceCenter<I>) dsResource.getResourceCenter();
		I serializationArtefact = resourceCenter.createEntry((name.endsWith(DIAGRAM_SUFFIX) ? name : (name + DIAGRAM_SUFFIX)),
				resourceCenter.getContainer((I) dsResource.getIODelegate().getSerializationArtefact()));

		DiagramResource returned = initResourceForCreation(serializationArtefact, resourceCenter, name,
				dsResource.getURI() + "/" + (name.endsWith(DIAGRAM_SUFFIX) ? name : (name + DIAGRAM_SUFFIX)));

		dsResource.addToContents(returned);
		dsResource.notifyContentsAdded(returned);

		registerResource(returned, resourceCenter);

		if (createEmptyContents) {
			Diagram resourceData = makeEmptyResourceData(returned);
			resourceData.setResource(returned);
			returned.setResourceData(resourceData);
			returned.setModified(true);
			returned.save();
		}

		dsResource.getDiagramSpecification().addToExampleDiagrams(returned.getDiagram());

		return returned;
	}

}
