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

import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.PamelaResourceFactory;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteFactory;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.toolbox.FlexoVersion;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.xml.XMLRootElementInfo;

/**
 * Implementation of PamelaResourceFactory for {@link DiagramPaletteResource}
 * 
 * @author sylvain
 *
 */
public class DiagramPaletteResourceFactory
		extends PamelaResourceFactory<DiagramPaletteResource, DiagramPalette, DiagramTechnologyAdapter, DiagramPaletteFactory> {

	private static final Logger logger = Logger.getLogger(DiagramPaletteResourceFactory.class.getPackage().getName());

	public static final String DIAGRAM_PALETTE_SUFFIX = ".palette";

	public static final FlexoVersion INITIAL_REVISION = new FlexoVersion("0.1");
	public static final FlexoVersion CURRENT_MODEL_VERSION = new FlexoVersion("1.0");

	public DiagramPaletteResourceFactory() throws ModelDefinitionException {
		super(DiagramPaletteResource.class);
	}

	@Override
	public DiagramPaletteFactory makeResourceDataFactory(DiagramPaletteResource resource,
			TechnologyContextManager<DiagramTechnologyAdapter> technologyContextManager) throws ModelDefinitionException {
		return new DiagramPaletteFactory(resource, technologyContextManager.getServiceManager().getEditingContext());
	}

	@Override
	public DiagramPalette makeEmptyResourceData(DiagramPaletteResource resource) {
		return resource.getFactory().makeNewDiagramPalette();
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return resourceCenter.retrieveName(serializationArtefact).endsWith(DIAGRAM_PALETTE_SUFFIX);
	}

	public <I> DiagramPaletteResource retrievePaletteResource(I serializationArtefact,
			TechnologyContextManager<DiagramTechnologyAdapter> technologyContextManager, DiagramSpecificationResource dsResource)
					throws ModelDefinitionException {

		FlexoResourceCenter<I> resourceCenter = (FlexoResourceCenter<I>) dsResource.getResourceCenter();
		String name = resourceCenter.retrieveName(serializationArtefact);

		DiagramPaletteResource returned = initResourceForRetrieving(serializationArtefact, resourceCenter, technologyContextManager);
		returned.setURI(dsResource.getURI() + "/" + name);

		dsResource.addToContents(returned);
		dsResource.notifyContentsAdded(returned);

		registerResource(returned, resourceCenter, technologyContextManager);

		return returned;
	}

	public <I> DiagramPaletteResource makeDiagramPaletteResource(String name, DiagramSpecificationResource dsResource,
			TechnologyContextManager<DiagramTechnologyAdapter> technologyContextManager, boolean createEmptyContents)
					throws SaveResourceException, ModelDefinitionException {

		FlexoResourceCenter<I> resourceCenter = (FlexoResourceCenter<I>) dsResource.getResourceCenter();
		I serializationArtefact = resourceCenter.createEntry(
				(name.endsWith(DIAGRAM_PALETTE_SUFFIX) ? name : (name + DIAGRAM_PALETTE_SUFFIX)),
				resourceCenter.getContainer((I) dsResource.getFlexoIODelegate().getSerializationArtefact()));

		DiagramPaletteResource returned = initResourceForCreation(serializationArtefact, resourceCenter, technologyContextManager,
				dsResource.getURI() + "/" + (name.endsWith(DIAGRAM_PALETTE_SUFFIX) ? name : (name + DIAGRAM_PALETTE_SUFFIX)));

		dsResource.addToContents(returned);
		dsResource.notifyContentsAdded(returned);

		registerResource(returned, resourceCenter, technologyContextManager);

		if (createEmptyContents) {
			DiagramPalette resourceData = makeEmptyResourceData(returned);
			resourceData.setResource(returned);
			returned.setResourceData(resourceData);
			returned.setModified(true);
			returned.save(null);
		}

		return returned;
	}

	@Override
	protected <I> DiagramPaletteResource registerResource(DiagramPaletteResource resource, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<DiagramTechnologyAdapter> technologyContextManager) {
		super.registerResource(resource, resourceCenter, technologyContextManager);

		// Register the resource in the DiagramRepository of supplied resource center
		// registerResourceInResourceRepository(resource,
		// technologyContextManager.getTechnologyAdapter().getDiagramRepository(resourceCenter));

		return resource;
	}

	@Override
	protected <I> DiagramPaletteResource initResourceForRetrieving(I serializationArtefact, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<DiagramTechnologyAdapter> technologyContextManager) throws ModelDefinitionException {
		DiagramPaletteResource returned = super.initResourceForRetrieving(serializationArtefact, resourceCenter, technologyContextManager);

		PaletteInfo vmiInfo = findPaletteInfo(returned, resourceCenter);
		if (vmiInfo != null) {
			// returned.initName(vmiInfo.name);
			// returned.setURI(vmiInfo.uri);
			if (StringUtils.isNotEmpty(vmiInfo.version)) {
				returned.setVersion(new FlexoVersion(vmiInfo.version));
			}
			else {
				returned.setVersion(INITIAL_REVISION);
			}
			if (StringUtils.isNotEmpty(vmiInfo.modelVersion)) {
				returned.setModelVersion(new FlexoVersion(vmiInfo.modelVersion));
			}
			else {
				returned.setModelVersion(CURRENT_MODEL_VERSION);
			}
		}
		else {
			logger.warning("Cannot retrieve info from " + serializationArtefact);
			returned.setVersion(INITIAL_REVISION);
			returned.setModelVersion(CURRENT_MODEL_VERSION);
		}

		return returned;

	}

	private <I> PaletteInfo findPaletteInfo(DiagramPaletteResource resource, FlexoResourceCenter<I> resourceCenter) {

		PaletteInfo returned = new PaletteInfo();
		XMLRootElementInfo xmlRootElementInfo = resourceCenter
				.getXMLRootElementInfo((I) resource.getFlexoIODelegate().getSerializationArtefact());
		if (xmlRootElementInfo.getName().equals("DiagramPalette")) {
			returned.name = xmlRootElementInfo.getAttribute(Diagram.NAME);
			returned.version = xmlRootElementInfo.getAttribute("version");
			returned.modelVersion = xmlRootElementInfo.getAttribute("modelVersion");
		}
		return returned;
	}

	private static class PaletteInfo {
		public String name;
		public String version;
		public String modelVersion;
	}

	/*public static DiagramPaletteResource makeDiagramPaletteResource(DiagramSpecificationResource dsResource, String diagramPaletteName,
			FlexoServiceManager serviceManager) {
		try {
			File diagramPaletteFile = new File(ResourceLocator.retrieveResourceAsFile(dsResource.getDirectory()),
					diagramPaletteName + ".palette");
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, DiagramPaletteResource.class));
			DiagramPaletteResourceImpl returned = (DiagramPaletteResourceImpl) factory.newInstance(DiagramPaletteResource.class);
			returned.initName(diagramPaletteFile.getName());
			// returned.setFile(diagramPaletteFile);
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(diagramPaletteFile, factory));
	
			returned.setURI(dsResource.getURI() + "/" + diagramPaletteFile.getName());
			returned.setResourceCenter(dsResource.getResourceCenter());
			returned.setServiceManager(serviceManager);
			returned.setFactory(new DiagramPaletteFactory(serviceManager.getEditingContext(), returned));
			dsResource.addToContents(returned);
			DiagramPalette newPalette = returned.getFactory().makeNewDiagramPalette();
			newPalette.setResource(returned);
			returned.setResourceData(newPalette);
			dsResource.getDiagramSpecification().addToPalettes(newPalette);
			dsResource.getDiagramPaletteResources().add(returned);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DiagramPaletteResource retrieveDiagramPaletteResource(DiagramSpecificationResource dsResource, File diagramPaletteFile,
			FlexoServiceManager serviceManager) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, DiagramPaletteResource.class));
			DiagramPaletteResourceImpl returned = (DiagramPaletteResourceImpl) factory.newInstance(DiagramPaletteResource.class);
			returned.initName(diagramPaletteFile.getName());
			// returned.setFile(diagramPaletteFile);
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(diagramPaletteFile, factory));
	
			PaletteInfo info = null;
			try {
				info = findPaletteInfo(new FileInputStream(diagramPaletteFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (info == null) {
				// Unable to retrieve infos, just abort
				logger.warning("Cannot retrieve info for palette " + diagramPaletteFile);
				return null;
			}
			// TODO: we already have set the name ???
			returned.initName(info.name);
	
			returned.setURI(dsResource.getURI() + "/" + diagramPaletteFile.getName());
			returned.setResourceCenter(dsResource.getResourceCenter());
			returned.setServiceManager(serviceManager);
			returned.setFactory(new DiagramPaletteFactory(serviceManager.getEditingContext(), returned));
			dsResource.addToContents(returned);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DiagramPaletteResource retrieveDiagramPaletteResource(DiagramSpecificationResource dsResource,
			InJarResourceImpl diagramPaletteInJarResource, FlexoServiceManager serviceManager) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(InJarFlexoIODelegate.class, DiagramPaletteResource.class));
			DiagramPaletteResourceImpl returned = (DiagramPaletteResourceImpl) factory.newInstance(DiagramPaletteResource.class);
			// returned.setFile(diagramPaletteFile);
			returned.setFlexoIODelegate(InJarFlexoIODelegateImpl.makeInJarFlexoIODelegate(diagramPaletteInJarResource, factory));
			PaletteInfo info = findPaletteInfo(diagramPaletteInJarResource.openInputStream());
			if (info == null) {
				return null;
			}
			returned.initName(info.name);
	
			returned.setURI(dsResource.getURI() + "/" + returned.getName() + ".palette");
			returned.setResourceCenter(dsResource.getResourceCenter());
			returned.setServiceManager(serviceManager);
			returned.setFactory(new DiagramPaletteFactory(serviceManager.getEditingContext(), returned));
			dsResource.addToContents(returned);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}*/

}
