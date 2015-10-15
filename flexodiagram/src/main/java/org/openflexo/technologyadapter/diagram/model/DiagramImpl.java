/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.technologyadapter.diagram.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.resource.CannotRenameException;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.InvalidFileNameException;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.ScreenshotBuilder;
import org.openflexo.foundation.resource.ScreenshotBuilder.ScreenshotImage;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterService;
import org.openflexo.swing.ImageUtils;
import org.openflexo.swing.ImageUtils.ImageType;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramResourceImpl;

/**
 * Default implementation for a diagram in Openflexo build-in diagram technology<br>
 * 
 * @author sylvain
 * 
 */
public abstract class DiagramImpl extends DiagramContainerElementImpl<DrawingGraphicalRepresentation>implements Diagram {

	private static final Logger logger = Logger.getLogger(DiagramImpl.class.getPackage().getName());

	private boolean screenshotModified = false;
	private ScreenshotImage<Diagram> screenshotImage;
	private File expectedScreenshotImageFile = null;

	public static DiagramResource newDiagramResource(String diagramName, String diagramTitle, String uri, File diagramFile,
			DiagramSpecification diagramSpecification, FlexoResourceCenter<?> resourceCenter, FlexoServiceManager serviceManager)
					throws InvalidFileNameException, SaveResourceException {

		DiagramResource newDiagramResource = DiagramResourceImpl.makeDiagramResource(diagramName, uri, diagramFile,
				diagramSpecification.getResource(), resourceCenter, serviceManager);

		Diagram newDiagram = newDiagramResource.getFactory().makeNewDiagram(diagramSpecification);
		newDiagramResource.setResourceData(newDiagram);
		newDiagram.setResource(newDiagramResource);
		// diagramSpecification.addToExampleDiagrams(newDiagram);

		newDiagram.setTitle(diagramTitle);

		newDiagramResource.save(null);

		return newDiagramResource;
	}

	@Override
	public DiagramSpecification getMetaModel() {
		return getDiagramSpecification();
	}

	@Override
	public DiagramTechnologyAdapter getTechnologyAdapter() {
		if (getResource() != null && getResource().getServiceManager() != null) {
			return getResource().getServiceManager().getService(TechnologyAdapterService.class)
					.getTechnologyAdapter(DiagramTechnologyAdapter.class);
		}
		return null;
	}

	private File getExpectedScreenshotImageFile() {
		if (expectedScreenshotImageFile == null && getResource().getFlexoIODelegate() instanceof FileFlexoIODelegate) {
			FileFlexoIODelegate delegate = (FileFlexoIODelegate) getResource().getFlexoIODelegate();
			expectedScreenshotImageFile = new File(delegate.getFile().getParentFile(), getName() + ".diagram.png");
		}
		return expectedScreenshotImageFile;
	}

	private ScreenshotImage<Diagram> buildAndSaveScreenshotImage() {
		if (getTechnologyAdapter().getScreenshotBuilder() != null) {
			ScreenshotBuilder<Diagram> builder = getTechnologyAdapter().getScreenshotBuilder();

			screenshotImage = builder.getImage(this);
			try {
				logger.info("Saving " + getExpectedScreenshotImageFile().getAbsolutePath());
				ImageUtils.saveImageToFile(screenshotImage.image, getExpectedScreenshotImageFile(), ImageType.PNG);
			} catch (IOException e) {
				e.printStackTrace();
				logger.warning("Could not save " + getExpectedScreenshotImageFile().getAbsolutePath());
			}
			screenshotModified = false;
			getPropertyChangeSupport().firePropertyChange("screenshotImage", null, screenshotImage);
			return screenshotImage;
		}
		return null;
	}

	private ScreenshotImage<Diagram> tryToLoadScreenshotImage() {
		// TODO
		/*if (getExpectedScreenshotImageFile() != null && getExpectedScreenshotImageFile().exists()) {
			BufferedImage bi = ImageUtils.loadImageFromFile(getExpectedScreenshotImageFile());
			if (bi != null) {
				logger.info("Read " + getExpectedScreenshotImageFile().getAbsolutePath());
				screenshotImage = ScreenshotGenerator.trimImage(bi);
				screenshotModified = false;
				return screenshotImage;
			}
		}*/
		return null;
	}

	@Override
	public void setModified(boolean modified) {
		super.setModified(modified);
		screenshotModified = true;
	}

	@Override
	public ScreenshotImage<Diagram> getScreenshotImage() {
		if (screenshotImage == null || screenshotModified) {
			if (screenshotModified) {
				logger.info("Rebuilding screenshot for " + this + " because screenshot is modified");
			}
			buildAndSaveScreenshotImage();
		}
		return screenshotImage;
	}

	@Override
	public synchronized void setIsModified() {
		super.setIsModified();
		screenshotModified = true;
	}

	@Override
	public String getName() {
		if (getResource() != null) {
			return getResource().getName();
		}
		return null;
	}

	@Override
	public void setName(String name) {
		if (requireChange(getName(), name)) {
			if (getResource() != null) {
				try {
					getResource().setName(name);
				} catch (CannotRenameException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String getURI() {
		if (getResource() != null) {
			return getResource().getURI();
		}
		return null;
	}

	@Override
	public void setURI(String uri) {
		if (requireChange(getURI(), uri)) {
			if (getResource() != null) {
				getResource().setURI(uri);
			}
		}
	}

	@Override
	public DiagramFactory getDiagramFactory() {
		return ((DiagramResource) getResource()).getFactory();
	}

	@Override
	public Diagram getDiagram() {
		return this;
	}

	@Override
	public DiagramSpecification getDiagramSpecification() {
		if (getResource() != null) {
			DiagramResource resource = (DiagramResource) getResource();
			if (resource.getMetaModelResource() != null) {
				try {
					return resource.getMetaModelResource().getResourceData(null);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ResourceLoadingCancelledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FlexoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public void setDiagramSpecification(DiagramSpecification aDiagramSpecification) {
		if (getResource() != null) {
			DiagramResource resource = (DiagramResource) getResource();
			if (aDiagramSpecification != null) {
				resource.setMetaModelResource(aDiagramSpecification.getResource());
				// TODO: i'm pretty sure there are other things to do....
			}
		}
	}

	/**
	 * Return the diagram specification URI of this diagram (might be null)
	 * 
	 * @return
	 */
	@Override
	public String getDiagramSpecificationURI() {
		if (getDiagramSpecification() != null) {
			return getDiagramSpecification().getURI();
		}
		return diagramSpecificationURI;
	}

	/**
	 * Sets the diagram specification URI of this diagram (might be null)
	 * 
	 * @param aName
	 */
	@Override
	public void setDiagramSpecificationURI(String aDiagramSpecificationURI) {
		this.diagramSpecificationURI = aDiagramSpecificationURI;
	}

	private String diagramSpecificationURI;

}
