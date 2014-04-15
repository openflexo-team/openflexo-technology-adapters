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
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.fge.FGEModelFactoryImpl;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.ScreenshotBuilder;
import org.openflexo.foundation.technologyadapter.DeclareModelSlot;
import org.openflexo.foundation.technologyadapter.DeclareModelSlots;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.foundation.viewpoint.VirtualModelModelFactory;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.rm.DiagramRepository;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramResourceImpl;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationRepository;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResourceImpl;

/**
 * This class defines and implements the Openflexo built-in diagram technology adapter
 * 
 * @author sylvain
 * 
 */
@DeclareModelSlots({ // ModelSlot(s) declaration
@DeclareModelSlot(FML = "TypedDiagramModelSlot", modelSlotClass = TypedDiagramModelSlot.class), // Typed diagram
		@DeclareModelSlot(FML = "FreeDiagramModelSlot", modelSlotClass = FreeDiagramModelSlot.class) // A free diagram
})
// @DeclareRepositoryType({ OWLOntologyRepository.class })
public class DiagramTechnologyAdapter extends TechnologyAdapter {

	private static final Logger logger = Logger.getLogger(DiagramTechnologyAdapter.class.getPackage().getName());

	private ScreenshotBuilder<Diagram> screenshotBuilder;
	
	private ScreenshotBuilder<DiagramShape> diagramShapeScreenshotBuilder;

	public DiagramTechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	@Override
	public String getName() {
		return "Openflexo diagram";
	}

	@Override
	public DiagramTechnologyContextManager createTechnologyContextManager(FlexoResourceCenterService service) {
		return new DiagramTechnologyContextManager(this, service);
	}

	@Override
	public DiagramTechnologyContextManager getTechnologyContextManager() {
		return (DiagramTechnologyContextManager) super.getTechnologyContextManager();
	}

	@Override
	public TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Initialize the supplied resource center with the technology<br>
	 * ResourceCenter is scanned, ResourceRepositories are created and new technology-specific resources are build and registered.
	 * 
	 * @param resourceCenter
	 */
	@Override
	public <I> void initializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {

		DiagramTechnologyContextManager technologyContextManager = (DiagramTechnologyContextManager) getTechnologyAdapterService()
				.getTechnologyContextManager(this);

		// A single DiagramSpecification Repository for all ResourceCenters

		DiagramSpecificationRepository dsRepository = resourceCenter.getRepository(DiagramSpecificationRepository.class, this);
		if (dsRepository == null) {
			dsRepository = createDiagramSpecificationRepository(resourceCenter);
		}

		DiagramRepository diagramRepository = resourceCenter.getRepository(DiagramRepository.class, this);
		if (diagramRepository == null) {
			diagramRepository = createDiagramRepository(resourceCenter);
		}

		// First pass on meta-models only
		Iterator<I> it = resourceCenter.iterator();

		while (it.hasNext()) {
			I item = it.next();
			if (item instanceof File) {
				File candidateFile = (File) item;
				DiagramSpecificationResource mmRes = tryToLookupDiagramSpecification(resourceCenter, candidateFile);
			}
		}

		// Second pass on models
		it = resourceCenter.iterator();

		while (it.hasNext()) {
			I item = it.next();
			if (item instanceof File) {
				File candidateFile = (File) item;
				DiagramResource diagramRes = tryToLookupDiagram(resourceCenter, candidateFile);
			}
		}
	}

	/**
	 * Creates and return a diagram repository for current {@link TechnologyAdapter} and supplied {@link FlexoResourceCenter}
	 */
	public DiagramRepository createDiagramRepository(FlexoResourceCenter<?> resourceCenter) {
		DiagramRepository returned = new DiagramRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, DiagramRepository.class, this);
		return returned;
	}

	/**
	 * Creates and return a diagram specification repository for current {@link TechnologyAdapter} and supplied {@link FlexoResourceCenter}
	 */
	public DiagramSpecificationRepository createDiagramSpecificationRepository(FlexoResourceCenter<?> resourceCenter) {
		DiagramSpecificationRepository returned = new DiagramSpecificationRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, DiagramSpecificationRepository.class, this);
		return returned;
	}

	protected DiagramSpecificationResource tryToLookupDiagramSpecification(FlexoResourceCenter<?> resourceCenter, File candidateFile) {
		if (isValidDiagramSpecificationFile(candidateFile)) {
			DiagramSpecificationRepository dsRepo = resourceCenter.getRepository(DiagramSpecificationRepository.class, this);
			if (dsRepo != null) {
				DiagramSpecificationResource dsRes = null;
				RepositoryFolder<DiagramSpecificationResource> folder;
				try {
					folder = dsRepo.getRepositoryFolder(candidateFile, true);
					dsRes = retrieveDiagramSpecificationResource(candidateFile, folder);
					dsRepo.registerResource(dsRes, folder);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				referenceResource(dsRes, resourceCenter);
				return dsRes;
			}
		}
		return null;
	}

	private boolean isValidDiagramSpecificationFile(File candidateFile) {
		if (candidateFile.exists() && candidateFile.isDirectory()
				&& candidateFile.getName().endsWith(DiagramSpecificationResource.DIAGRAM_SPECIFICATION_SUFFIX)) {
			System.out.println("Found valid candidate for DiagramSpecification: " + candidateFile);
			return true;
		}
		return false;
	}

	private boolean isValidDiagramFile(File candidateFile) {
		return candidateFile.exists() && candidateFile.getName().endsWith(DiagramResource.DIAGRAM_SUFFIX);
	}

	/**
	 * Instantiate new model resource stored in supplied model file, given the conformant metamodel<br>
	 * We assert here that model resource is conform to supplied metamodel, ie we will not try to lookup the metamodel but take the one
	 * which was supplied
	 * 
	 */
	private DiagramSpecificationResource retrieveDiagramSpecificationResource(File diagramSpecificationDirectory, RepositoryFolder<?> folder) {
		DiagramSpecificationResource returned = getTechnologyContextManager()
				.getDiagramSpecificationResource(diagramSpecificationDirectory);

		if (returned == null) {
			returned = DiagramSpecificationResourceImpl.retrieveDiagramSpecificationResource(diagramSpecificationDirectory, folder,
					getTechnologyAdapterService().getServiceManager());
			if (returned != null) {
				getTechnologyContextManager().registerDiagramSpecification(returned);
			} else {
				logger.warning("Cannot retrieve DiagramSpecificationResource for " + diagramSpecificationDirectory);
			}
		}

		return returned;
	}

	/**
	 * Instantiate new diagram resource stored in supplied diagram file
	 * 
	 */
	private DiagramResource retrieveDiagramResource(File aDiagramFile) {
		DiagramResource returned = getTechnologyContextManager().getDiagramResource(aDiagramFile);

		if (returned == null) {
			returned = DiagramResourceImpl.retrieveDiagramResource(aDiagramFile, getTechnologyAdapterService().getServiceManager());
			if (returned != null) {
				getTechnologyContextManager().registerDiagram(returned);
			} else {
				logger.warning("Cannot retrieve DiagramResource for " + aDiagramFile);
			}
		}

		return returned;
	}

	protected DiagramResource tryToLookupDiagram(FlexoResourceCenter<?> resourceCenter, File candidateFile) {
		DiagramTechnologyContextManager technologyContextManager = getTechnologyContextManager();
		// DiagramSpecificationRepository dsRepository; // = resourceCenter.getRepository(DiagramSpecificationRepository.class, this);
		DiagramRepository diagramRepository = resourceCenter.getRepository(DiagramRepository.class, this);

		List<FlexoResourceCenter> rscCenters = technologyContextManager.getResourceCenterService().getResourceCenters();

		// for (FlexoResourceCenter<?> rscCenter : rscCenters) {
		// dsRepository = rscCenter.getRepository(DiagramSpecificationRepository.class, this);
		// if (dsRepository != null) {
		// for (DiagramSpecificationResource dsRes : dsRepository.getAllResources()) {
		if (isValidDiagramFile(candidateFile)) {
			DiagramResource diagramResource = retrieveDiagramResource(candidateFile);
			if (diagramResource != null) {
				RepositoryFolder<DiagramResource> folder;
				try {
					folder = diagramRepository.getRepositoryFolder(candidateFile, true);
					diagramRepository.registerResource(diagramResource, folder);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				referenceResource(diagramResource, resourceCenter);
				return diagramResource;
			}
		}
		// }
		// }
		// }
		return null;
	}

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}

	@Override
	public <I> void contentsAdded(FlexoResourceCenter<I> resourceCenter, I contents) {
		if (contents instanceof File) {
			System.out.println("DiagramTechnologyAdapter: File ADDED " + ((File) contents).getName() + " in "
					+ ((File) contents).getParentFile().getAbsolutePath());
			File candidateFile = (File) contents;
			if (tryToLookupDiagramSpecification(resourceCenter, candidateFile) != null) {
				// This is a meta-model, this one has just been registered
			} else {
				tryToLookupDiagram(resourceCenter, candidateFile);
			}
		}
	}

	@Override
	public <I> void contentsDeleted(FlexoResourceCenter<I> resourceCenter, I contents) {
		if (contents instanceof File) {
			System.out.println("DiagramTechnologyAdapter: File DELETED " + ((File) contents).getName() + " in "
					+ ((File) contents).getParentFile().getAbsolutePath());
		}
	}

	public DiagramResource createNewDiagram(FlexoProject project, String filename, String diagramUri,
			DiagramSpecificationResource diagramSpecificationResource) throws SaveResourceException {
		File diagramFile = new File(getProjectSpecificDiagramsDirectory(project), filename);
		return createNewDiagram(diagramFile.getName(), diagramUri, diagramFile, diagramSpecificationResource);
	}

	public DiagramResource createNewDiagram(FileSystemBasedResourceCenter resourceCenter, String relativePath, String filename,
			String diagramUri, DiagramSpecificationResource diagramSpecificationResource) throws SaveResourceException {
		File diagramDirectory = new File(resourceCenter.getRootDirectory(), relativePath);
		File diagramFile = new File(diagramDirectory, filename);
		return createNewDiagram(diagramFile.getName(), diagramUri, diagramFile, diagramSpecificationResource);
	}

	public DiagramResource createNewDiagram(String diagramName, String diagramlUri, File diagramFile,
			DiagramSpecificationResource diagramSpecificationResource) throws SaveResourceException {

		DiagramResource diagramResource = DiagramResourceImpl.makeDiagramResource(diagramName, diagramlUri, diagramFile,
				diagramSpecificationResource, getTechnologyAdapterService().getServiceManager());

		diagramResource.save(null);

		return diagramResource;
	}

	public static File getProjectSpecificDiagramsDirectory(FlexoProject project) {
		File returned = new File(project.getProjectDirectory(), "Diagrams");
		returned.mkdirs();
		return returned;
	}

	public ScreenshotBuilder<Diagram> getScreenshotBuilder() {
		return screenshotBuilder;
	}

	public void setScreenshotBuilder(ScreenshotBuilder<Diagram> screenshotBuilder) {
		this.screenshotBuilder = screenshotBuilder;
	}
	
	public ScreenshotBuilder<DiagramShape> getDiagramShapeScreenshotBuilder() {
		return diagramShapeScreenshotBuilder;
	}

	public void setDiagramShapeScreenshotBuilder(ScreenshotBuilder<DiagramShape> screenshotBuilder) {
		this.diagramShapeScreenshotBuilder = screenshotBuilder;
	}

	// Override when required
	@Override
	public void initVirtualModelFactory(VirtualModelModelFactory virtualModelModelFactory) {
		try {
			FGEModelFactoryImpl.installImplementingClasses(virtualModelModelFactory);
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
	}

}
