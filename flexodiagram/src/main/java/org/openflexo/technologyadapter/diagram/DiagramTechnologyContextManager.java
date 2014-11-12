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
package org.openflexo.technologyadapter.diagram;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResource;

public class DiagramTechnologyContextManager extends TechnologyContextManager<DiagramTechnologyAdapter> {

	/** Stores all known DiagramSpecification where key is the URI of DiagramSpecification */
	protected Map<String, DiagramSpecificationResource> diagramSpecifications = new HashMap<String, DiagramSpecificationResource>();
	/** Stores all known Diagrams where key is the URI of Diagram */
	protected Map<String, DiagramResource> diagrams = new HashMap<String, DiagramResource>();

	public DiagramTechnologyContextManager(DiagramTechnologyAdapter adapter, FlexoResourceCenterService resourceCenterService) {
		super(adapter, resourceCenterService);
	}

	@Override
	public DiagramTechnologyAdapter getTechnologyAdapter() {
		return (DiagramTechnologyAdapter) super.getTechnologyAdapter();
	}

	public DiagramSpecificationResource getDiagramSpecificationResource(Object diagramSpecification) {
		return diagramSpecifications.get(diagramSpecification);
	}

	public DiagramResource getDiagramResource(File diagramFile) {
		for(Entry<String,DiagramResource> entry :diagrams.entrySet()){
			 if(entry.getValue().getFlexoIODelegate() instanceof FileFlexoIODelegate){
				 FileFlexoIODelegate delegate = (FileFlexoIODelegate)entry.getValue().getFlexoIODelegate();
				 if(delegate.getFile().equals(diagramFile)){
					 return entry.getValue();
				 }
			 }
		}
		return diagrams.get(diagramFile);
	}

	/**
	 * Called when a new meta model was registered, notify the {@link TechnologyContextManager}
	 * 
	 * @param newModel
	 */
	public void registerDiagramSpecification(DiagramSpecificationResource newDiagramSpecificationResource) {
		registerResource(newDiagramSpecificationResource);
		diagramSpecifications.put(newDiagramSpecificationResource.getURI(), newDiagramSpecificationResource);
	}

	/**
	 * Called when a new diagram was registered, notify the {@link TechnologyContextManager}
	 * 
	 * @param newModel
	 */
	public void registerDiagram(DiagramResource newDiagramResource) {
		registerResource(newDiagramResource);
		diagrams.put(newDiagramResource.getURI(), newDiagramResource);
	}

}
