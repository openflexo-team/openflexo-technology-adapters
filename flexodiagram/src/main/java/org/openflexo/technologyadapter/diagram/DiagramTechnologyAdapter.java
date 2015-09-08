/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.technologyadapter.diagram;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;
import org.openflexo.fge.FGEModelFactoryImpl;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareTechnologySpecificTypes;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.ScreenshotBuilder;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.rm.InJarResourceImpl;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.DiagramType;
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
@DeclareModelSlots({ TypedDiagramModelSlot.class, FreeDiagramModelSlot.class })
@DeclareTechnologySpecificTypes({ DiagramType.class })
public class DiagramTechnologyAdapter extends TechnologyAdapter {

	private static final Logger logger = Logger.getLogger(DiagramTechnologyAdapter.class.getPackage().getName());

	private ScreenshotBuilder<Diagram> screenshotBuilder;

	private ScreenshotBuilder<DiagramPalette> diagramPaletteScreenshotBuilder;

	private ScreenshotBuilder<DiagramShape> diagramShapeScreenshotBuilder;

	private ScreenshotBuilder<DiagramElement<?>> fmlDiagramElementScreenshotBuilder;

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
			DiagramSpecificationResource mmRes = tryToLookupDiagramSpecification(resourceCenter, item);
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

		// Call it to update the current repositories
		getPropertyChangeSupport().firePropertyChange("getAllRepositories()", null, resourceCenter);
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

	/**
	 * Check if it correspond to a diagram specification an thus create a diagram specification resource
	 * 
	 * @param resourceCenter
	 * @param candidateElement
	 * @return
	 */
	protected DiagramSpecificationResource tryToLookupDiagramSpecification(FlexoResourceCenter<?> resourceCenter, Object candidateElement) {
		if (isValidDiagramSpecification(candidateElement)) {
			DiagramSpecificationRepository dsRepo = resourceCenter.getRepository(DiagramSpecificationRepository.class, this);
			if (dsRepo != null) {
				DiagramSpecificationResource dsRes = null;
				RepositoryFolder<DiagramSpecificationResource> folder;
				try {
					folder = dsRepo.getRepositoryFolder(candidateElement, true);
					dsRes = retrieveDiagramSpecificationResource(candidateElement, folder);
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

	/**
	 * A valid diagram specification is a directory with the extension .diagramspecification in a file or jar
	 * 
	 * @param candidateElement
	 * @return
	 */
	private boolean isValidDiagramSpecification(Object candidateElement) {
		if (candidateElement instanceof File && ((File) candidateElement).exists() && ((File) candidateElement).isDirectory()
				&& ((File) candidateElement).getName().endsWith(DiagramSpecificationResource.DIAGRAM_SPECIFICATION_SUFFIX)) {
			// System.out.println("Found valid candidate for DiagramSpecification: " + ((File)candidateElement));
			return true;
		}
		if (candidateElement instanceof InJarResourceImpl && ((InJarResourceImpl) candidateElement).getRelativePath().endsWith(".xml")
				&& ((InJarResourceImpl) candidateElement).getRelativePath()
						.endsWith(FilenameUtils.getBaseName(((InJarResourceImpl) candidateElement).getRelativePath())
								+ DiagramSpecificationResource.DIAGRAM_SPECIFICATION_SUFFIX + "/"
								+ FilenameUtils.getBaseName(((InJarResourceImpl) candidateElement).getRelativePath()) + ".xml")) {
			// System.out.println("Found valid candidate for DiagramSpecification: " + ((InJarResourceImpl)candidateElement));
			return true;
		}
		return false;
	}

	private boolean isValidDiagramFile(File candidateFile) {
		return candidateFile.exists() && candidateFile.getName().endsWith(DiagramResource.DIAGRAM_SUFFIX);
	}

	/**
	 * Instantiate new diagram specification resource stored in supplied model file or in jar<br>
	 */
	private DiagramSpecificationResource retrieveDiagramSpecificationResource(Object diagramSpecification, RepositoryFolder<?> folder) {
		DiagramSpecificationResource returned = getTechnologyContextManager().getDiagramSpecificationResource(diagramSpecification);
		if (returned == null) {
			if (diagramSpecification instanceof File) {
				returned = DiagramSpecificationResourceImpl.retrieveDiagramSpecificationResource((File) diagramSpecification, folder,
						getTechnologyAdapterService().getServiceManager());
			}
			else if (diagramSpecification instanceof InJarResourceImpl) {
				returned = DiagramSpecificationResourceImpl.retrieveDiagramSpecificationResource((InJarResourceImpl) diagramSpecification,
						getTechnologyAdapterService().getServiceManager());
			}
			if (returned != null) {
				getTechnologyContextManager().registerDiagramSpecification(returned);
			}
			else {
				logger.warning("Cannot retrieve DiagramSpecificationResource for " + diagramSpecification);
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
			}
			else {
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
			}
			else {
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
		DiagramResource returned = createNewDiagram(diagramFile.getName(), diagramUri, diagramFile, diagramSpecificationResource);
		DiagramRepository diagramRepository = project.getRepository(DiagramRepository.class, this);
		diagramRepository.registerResource(returned);
		return returned;
	}

	public DiagramResource createNewDiagram(FileSystemBasedResourceCenter resourceCenter, String relativePath, String filename,
			String diagramUri, DiagramSpecificationResource diagramSpecificationResource) throws SaveResourceException {
		File diagramDirectory = new File(resourceCenter.getRootDirectory(), relativePath);
		File diagramFile = new File(diagramDirectory, filename);
		DiagramResource returned = createNewDiagram(diagramFile.getName(), diagramUri, diagramFile, diagramSpecificationResource);
		DiagramRepository diagramRepository = resourceCenter.getRepository(DiagramRepository.class, this);
		diagramRepository.registerResource(returned);
		return returned;
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

	public ScreenshotBuilder<DiagramPalette> getDiagramPaletteScreenshotBuilder() {
		return diagramPaletteScreenshotBuilder;
	}

	public void setDiagramPaletteScreenshotBuilder(ScreenshotBuilder<DiagramPalette> screenshotBuilder) {
		this.diagramPaletteScreenshotBuilder = screenshotBuilder;
	}

	public ScreenshotBuilder<DiagramShape> getDiagramShapeScreenshotBuilder() {
		return diagramShapeScreenshotBuilder;
	}

	public void setDiagramShapeScreenshotBuilder(ScreenshotBuilder<DiagramShape> screenshotBuilder) {
		this.diagramShapeScreenshotBuilder = screenshotBuilder;
	}

	public ScreenshotBuilder<DiagramElement<?>> getFMLControlledDiagramElementScreenshotBuilder() {
		return fmlDiagramElementScreenshotBuilder;
	}

	public void setFMLControlledDiagramScreenshotBuilder(ScreenshotBuilder<DiagramElement<?>> diagramElement) {
		this.fmlDiagramElementScreenshotBuilder = diagramElement;
	}

	// Override when required
	@Override
	public void initFMLModelFactory(FMLModelFactory fMLModelFactory) {
		try {
			FGEModelFactoryImpl.installImplementingClasses(fMLModelFactory);
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getIdentifier() {
		return "DIAGRAM";
	}

}
