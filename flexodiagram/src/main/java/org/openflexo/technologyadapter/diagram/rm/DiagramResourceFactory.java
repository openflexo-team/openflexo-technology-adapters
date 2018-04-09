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
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.TechnologySpecificPamelaResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.toolbox.FlexoVersion;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.xml.XMLRootElementInfo;

/**
 * Implementation of PamelaResourceFactory for {@link DiagramResource}
 * 
 * @author sylvain
 *
 */
public class DiagramResourceFactory
		extends TechnologySpecificPamelaResourceFactory<DiagramResource, Diagram, DiagramTechnologyAdapter, DiagramFactory> {

	private static final Logger logger = Logger.getLogger(DiagramResourceFactory.class.getPackage().getName());

	public static final String DIAGRAM_SUFFIX = ".diagram";
	public static final FlexoVersion INITIAL_REVISION = new FlexoVersion("0.1");
	public static final FlexoVersion CURRENT_MODEL_VERSION = new FlexoVersion("1.0");

	public DiagramResourceFactory() throws ModelDefinitionException {
		super(DiagramResource.class);
	}

	@Override
	public DiagramFactory makeResourceDataFactory(DiagramResource resource,
			TechnologyContextManager<DiagramTechnologyAdapter> technologyContextManager) throws ModelDefinitionException {
		return new DiagramFactory(resource, technologyContextManager.getServiceManager().getEditingContext());
	}

	@Override
	public Diagram makeEmptyResourceData(DiagramResource resource) {
		return resource.getFactory().makeNewDiagram();
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return resourceCenter.retrieveName(serializationArtefact).endsWith(DIAGRAM_SUFFIX);
	}

	public <I> DiagramResource makeDiagramResource(String baseName, String uri, DiagramSpecificationResource diagramSpecificationResource,
			RepositoryFolder<DiagramResource, I> folder, boolean createEmptyContents)
			throws SaveResourceException, ModelDefinitionException {

		FlexoResourceCenter<I> rc = folder.getResourceRepository().getResourceCenter();

		String artefactName = baseName.endsWith(DiagramResourceFactory.DIAGRAM_SUFFIX) ? baseName
				: baseName + DiagramResourceFactory.DIAGRAM_SUFFIX;

		I serializationArtefact = rc.createEntry(artefactName, folder.getSerializationArtefact());

		DiagramResource newDiagramResource = makeResource(serializationArtefact, rc, baseName, uri, true);
		newDiagramResource.setMetaModelResource(diagramSpecificationResource);

		return newDiagramResource;
	}

	@Override
	protected <I> DiagramResource registerResource(DiagramResource resource, FlexoResourceCenter<I> resourceCenter) {
		super.registerResource(resource, resourceCenter);

		// Register the resource in the DiagramRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				getTechnologyAdapter(resourceCenter.getServiceManager()).getDiagramRepository(resourceCenter));

		return resource;
	}

	@Override
	protected <I> DiagramResource initResourceForRetrieving(I serializationArtefact, FlexoResourceCenter<I> resourceCenter)
			throws ModelDefinitionException, IOException {
		DiagramResource returned = super.initResourceForRetrieving(serializationArtefact, resourceCenter);

		DiagramInfo vmiInfo = findDiagramInfo(returned, resourceCenter);
		if (vmiInfo != null) {
			// returned.initName(vmiInfo.name);
			returned.setURI(vmiInfo.uri);
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
			if (StringUtils.isNotEmpty(vmiInfo.diagramSpecificationURI)) {
				DiagramSpecificationResource dsResource = (DiagramSpecificationResource) getTechnologyContextManager(
						resourceCenter.getServiceManager()).getResourceWithURI(vmiInfo.diagramSpecificationURI);
				if (dsResource == null) {
					// DiagramSpecificationResource not found yet, but give a chance to lookup later
					// We stored the URI of DiagramSpecification
					((DiagramResourceImpl) returned).diagramSpecificationURI = vmiInfo.diagramSpecificationURI;
				}
				else {
					returned.setMetaModelResource(dsResource);
				}
			}

		}
		else {
			logger.warning("Cannot retrieve info from " + serializationArtefact);
			returned.setVersion(INITIAL_REVISION);
			returned.setModelVersion(CURRENT_MODEL_VERSION);
		}

		return returned;

	}

	private static class DiagramInfo {
		public String uri;
		public String name;
		public String version;
		public String title;
		public String modelVersion;
		public String diagramSpecificationURI;
	}

	private static <I> DiagramInfo findDiagramInfo(DiagramResource resource, FlexoResourceCenter<I> resourceCenter) {

		DiagramInfo returned = new DiagramInfo();
		XMLRootElementInfo xmlRootElementInfo = resourceCenter
				.getXMLRootElementInfo((I) resource.getIODelegate().getSerializationArtefact());
		if (xmlRootElementInfo == null) {
			return null;
		}
		if (xmlRootElementInfo.getName().equals("Diagram")) {
			returned.name = xmlRootElementInfo.getAttribute(Diagram.NAME);
			returned.title = xmlRootElementInfo.getAttribute(Diagram.TITLE);
			returned.uri = xmlRootElementInfo.getAttribute(Diagram.URI);
			returned.diagramSpecificationURI = xmlRootElementInfo.getAttribute(Diagram.DIAGRAM_SPECIFICATION_URI);
			returned.version = xmlRootElementInfo.getAttribute("version");
			returned.modelVersion = xmlRootElementInfo.getAttribute("modelVersion");
		}
		return returned;
	}

	/*public static DiagramResource makeDiagramResource(String name, String uri, File diagramFile, FlexoResourceCenter<?> resourceCenter,
			FlexoServiceManager serviceManager) {
		return makeDiagramResource(name, uri, diagramFile, null, resourceCenter, serviceManager);
	}
	
	public static DiagramResource makeDiagramResource(String name, String uri, File diagramFile,
			DiagramSpecificationResource diagramSpecificationResource, FlexoResourceCenter<?> resourceCenter,
			FlexoServiceManager serviceManager) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, DiagramResource.class));
			DiagramResourceImpl returned = (DiagramResourceImpl) factory.newInstance(DiagramResource.class);
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(diagramFile, factory));
			DiagramFactory diagramFactory = new DiagramFactory(returned, serviceManager.getEditingContext());
			returned.setFactory(diagramFactory);
			returned.initName(name);
			returned.setURI(uri);
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(serviceManager);
			if (diagramSpecificationResource != null) {
				returned.setMetaModelResource(diagramSpecificationResource);
			}
			Diagram newDiagram = returned.getFactory().makeNewDiagram();
			newDiagram.setResource(returned);
			returned.setResourceData(newDiagram);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DiagramResource retrieveDiagramResource(File diagramFile, FlexoResourceCenter<?> resourceCenter,
			FlexoServiceManager serviceManager) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, DiagramResource.class));
			DiagramResourceImpl returned = (DiagramResourceImpl) factory.newInstance(DiagramResource.class);
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(diagramFile, factory));
			DiagramFactory diagramFactory = new DiagramFactory(returned, serviceManager.getEditingContext());
			returned.setFactory(diagramFactory);
			String baseName = diagramFile.getName().substring(0, diagramFile.getName().length() - DiagramResource.DIAGRAM_SUFFIX.length());
			returned.initName(baseName);
			DiagramInfo info = null;
			try {
				info = findDiagramInfo(new FileInputStream(diagramFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (info == null) {
				// Unable to retrieve infos, just abort
				logger.warning("Cannot retrieve info for diagram " + diagramFile);
				return null;
			}
			returned.setURI(info.uri);
			if (StringUtils.isNotEmpty(info.diagramSpecificationURI)) {
				DiagramTechnologyAdapter ta = serviceManager.getTechnologyAdapterService()
						.getTechnologyAdapter(DiagramTechnologyAdapter.class);
				DiagramSpecificationResource dsResource = (DiagramSpecificationResource) ta.getTechnologyAdapterService()
						.getServiceManager().getResourceManager().getResource(info.diagramSpecificationURI);
				if (dsResource == null) {
					// DiagramSpecificationResource not found yet, but give a chance to lookup later
					// We stored the URI of DiagramSpecification
					returned.diagramSpecificationURI = info.diagramSpecificationURI;
				}
				else {
					returned.setMetaModelResource(dsResource);
				}
			}
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(serviceManager);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DiagramResource retrieveDiagramResource(InJarResourceImpl inJarResource, FlexoResourceCenter<?> resourceCenter,
			FlexoServiceManager serviceManager) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(InJarFlexoIODelegate.class, DiagramResource.class));
			DiagramResourceImpl returned = (DiagramResourceImpl) factory.newInstance(DiagramResource.class);
			returned.setFlexoIODelegate(InJarFlexoIODelegateImpl.makeInJarFlexoIODelegate(inJarResource, factory));
			DiagramFactory diagramFactory = new DiagramFactory(returned, serviceManager.getEditingContext());
			returned.setFactory(diagramFactory);
			DiagramInfo info = findDiagramInfo(inJarResource.openInputStream());
			if (info == null) {
				// Unable to retrieve infos, just abort
				// logger.warning("Cannot retrieve info for diagram " + diagramFile);
				return null;
			}
			returned.setURI(info.uri);
			if (StringUtils.isNotEmpty(info.diagramSpecificationURI)) {
				DiagramTechnologyAdapter ta = serviceManager.getTechnologyAdapterService()
						.getTechnologyAdapter(DiagramTechnologyAdapter.class);
				DiagramSpecificationResource dsResource = (DiagramSpecificationResource) ta.getTechnologyAdapterService()
						.getServiceManager().getResourceManager().getResource(info.diagramSpecificationURI);
				if (dsResource == null) {
					// DiagramSpecificationResource not found yet, but give a chance to lookup later
					// We stored the URI of DiagramSpecification
					returned.diagramSpecificationURI = info.diagramSpecificationURI;
				}
				else {
					returned.setMetaModelResource(dsResource);
				}
			}
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(serviceManager);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}*/

}
