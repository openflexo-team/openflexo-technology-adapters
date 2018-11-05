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

package org.openflexo.technologyadapter.diagram.metamodel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.ScreenshotBuilder;
import org.openflexo.fge.ScreenshotBuilder.ScreenshotImage;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.resource.FileIODelegate;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterService;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.converter.RelativePathResourceConverter;
import org.openflexo.model.validation.Validable;
import org.openflexo.swing.ImageUtils;
import org.openflexo.swing.ImageUtils.ImageType;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.rm.DiagramPaletteResource;

@ModelEntity
@ImplementationClass(DiagramPalette.DiagramPaletteImpl.class)
@XMLElement
public interface DiagramPalette extends DiagramPaletteObject, ResourceData<DiagramPalette>, Comparable<DiagramPalette> {

	@PropertyIdentifier(type = DiagramPaletteElement.class, cardinality = Cardinality.LIST)
	public static final String PALETTE_ELEMENTS_KEY = "elements";
	@PropertyIdentifier(type = DrawingGraphicalRepresentation.class)
	public static final String GRAPHICAL_REPRESENTATION_KEY = "graphicalRepresentation";
	@PropertyIdentifier(type = String.class)
	String DESCRIPTION_KEY = "description";

	@Getter(value = DESCRIPTION_KEY)
	@XMLAttribute
	public String getDescription();

	@Setter(DESCRIPTION_KEY)
	public void setDescription(String description);

	/**
	 * Return palette element identified by its name
	 */
	public DiagramPaletteElement getPaletteElement(String elementName);

	@Getter(value = PALETTE_ELEMENTS_KEY, cardinality = Cardinality.LIST, inverse = DiagramPaletteElement.PALETTE_KEY)
	@XMLElement
	public List<DiagramPaletteElement> getElements();

	@Setter(PALETTE_ELEMENTS_KEY)
	public void setElements(List<DiagramPaletteElement> elements);

	@Adder(PALETTE_ELEMENTS_KEY)
	public void addToElements(DiagramPaletteElement obj);

	@Remover(PALETTE_ELEMENTS_KEY)
	public boolean removeFromElements(DiagramPaletteElement obj);

	@Getter(value = GRAPHICAL_REPRESENTATION_KEY)
	@XMLElement
	public DrawingGraphicalRepresentation getGraphicalRepresentation();

	@Setter(value = GRAPHICAL_REPRESENTATION_KEY)
	public void setGraphicalRepresentation(DrawingGraphicalRepresentation graphicalRepresentation);

	public String getURI();

	public int getIndex();

	public DiagramPaletteElement addPaletteElement(String name, Object graphicalRepresentation);

	/**
	 * Return screenshot of this palette, when available
	 * 
	 * @return
	 */
	public ScreenshotImage<DiagramPalette> getScreenshotImage();

	@Override
	public DiagramPaletteResource getResource();

	public abstract static class DiagramPaletteImpl extends DiagramPaletteObjectImpl implements DiagramPalette {

		static final Logger logger = Logger.getLogger(DiagramPalette.class.getPackage().getName());

		private int index;
		// private Vector<DiagramPaletteElement> _elements;
		private DiagramPaletteResource resource;
		// private DrawingGraphicalRepresentation graphicalRepresentation;

		private boolean screenshotModified = false;
		private ScreenshotImage<DiagramPalette> screenshotImage;
		private File expectedScreenshotImageFile = null;

		/*public static DiagramPaletteResource newDiagramPalette(DiagramSpecification diagramSpecification, String diagramPaletteName,
				DrawingGraphicalRepresentation graphicalRepresentation, FlexoServiceManager serviceManager) {
			DiagramPaletteResource diagramPaletteResource = DiagramPaletteResourceImpl.makeDiagramPaletteResource(
					diagramSpecification.getResource(), diagramPaletteName, serviceManager);
			DiagramPalette diagramPalette = diagramPaletteResource.getDiagramPalette();
			diagramPalette.setGraphicalRepresentation(graphicalRepresentation);
			diagramPalette.setName(diagramPaletteName);
			diagramPaletteResource.setResourceData(diagramPalette);
			diagramPalette.setResource(diagramPaletteResource);
			try {
				diagramPaletteResource.save(null);
			} catch (SaveResourceException e) {
				e.printStackTrace();
			}
			return diagramPaletteResource;
		}*/

		// private DiagramPaletteFactory factory;

		// Used during deserialization, do not use it
		/*public DiagramPaletteImpl() {
			super();
			//_elements = new Vector<DiagramPaletteElement>();
		}*/

		@Override
		public FlexoServiceManager getServiceManager() {
			return getResource().getServiceManager();
		}

		@Override
		public DiagramPalette getPalette() {
			return this;
		}

		public void init(DiagramSpecification diagramSpecification, String diagramPaletteName) {
			setName(diagramPaletteName);
			// this.diagramSpecification = diagramSpecification;
			// xmlFile = paletteFile;
			logger.info("Registering palette for DiagramSpecification " + getDiagramSpecification().getName());
			tryToLoadScreenshotImage();
			// initialized = true;
		}

		@Override
		public DiagramSpecification getDiagramSpecification() {
			if (resource != null && resource.getContainer() != null) {
				return resource.getContainer().getDiagramSpecification();
			}
			return null;
		}

		@Override
		public boolean delete(Object... context) {
			if (getDiagramSpecification() != null) {
				getDiagramSpecification().removeFromPalettes(this);
			}
			performSuperDelete(context);
			deleteObservers();
			return true;
		}

		@Override
		public String getURI() {
			return resource.getURI();
		}

		/**
		 * Return palette element identified by its name
		 */
		@Override
		public DiagramPaletteElement getPaletteElement(String elementName) {
			for (DiagramPaletteElement e : getElements()) {
				if (elementName.equals(e.getName())) {
					return e;
				}
			}
			return null;
		}

		@Override
		public Collection<Validable> getEmbeddedValidableObjects() {
			Collection<Validable> result = new ArrayList<>();
			for (Validable v : getElements())
				result.add(v);
			return result;
		}

		@Override
		public String toString() {
			return "DiagramPalette:" + (getDiagramSpecification() != null ? getDiagramSpecification().getName() : "null");
		}

		@Override
		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		@Override
		public String getName() {
			if (getResource() != null) {
				if (getResource().getName().endsWith(".palette")) {
					return getResource().getName().substring(0, getResource().getName().indexOf(".palette"));
				}
				return getResource().getName();
			}
			return null;
		}

		@Override
		public void setName(String name) {
			if (requireChange(getName(), name)) {
				if (getResource() != null) {
					getResource().initName(name);
					// getResource().renameFileTo(name + ".palette");
				}
			}
		}

		/*	@Override
			public Vector<DiagramPaletteElement> getElements() {
				return _elements;
			}
		
			@Override
			public void setElements(Vector<DiagramPaletteElement> elements) {
				_elements = elements;
			}
		
			@Override
			public void addToElements(DiagramPaletteElement obj) {
				obj.setPalette(this);
				_elements.add(obj);
				setChanged();
				notifyObservers(new DiagramPaletteElementInserted(obj, this));
			}
		
			@Override
			public boolean removeFromElements(DiagramPaletteElement obj) {
				obj.setPalette(null);
				boolean returned = _elements.remove(obj);
				setChanged();
				notifyObservers(new DiagramPaletteElementRemoved(obj, this));
				return returned;
			}*/

		/*	@Override
			public DrawingGraphicalRepresentation getGraphicalRepresentation() {
				return graphicalRepresentation;
			}
		
			@Override
			public void setGraphicalRepresentation(DrawingGraphicalRepresentation graphicalRepresentation) {
				this.graphicalRepresentation = graphicalRepresentation;
			}*/

		public RelativePathResourceConverter getRelativePathFileConverter() {
			return getResource().getContainer().getRelativePathResourceConverter();
		}

		@Override
		public DiagramPaletteElement addPaletteElement(String name, Object graphicalRepresentation) {
			if (getResource() != null) {
				DiagramPaletteElement newElement = getResource().getFactory().makeDiagramPaletteElement();
				newElement.setName(name);
				newElement.setGraphicalRepresentation((ShapeGraphicalRepresentation) graphicalRepresentation);
				addToElements(newElement);
				return newElement;
			}
			return null;
		}

		@Override
		public int compareTo(DiagramPalette o) {
			return index - o.getIndex();
		}

		@Override
		public DiagramTechnologyAdapter getTechnologyAdapter() {
			return getResource().getServiceManager().getService(TechnologyAdapterService.class)
					.getTechnologyAdapter(DiagramTechnologyAdapter.class);
		}

		private File getExpectedScreenshotImageFile() {
			if (expectedScreenshotImageFile == null && getResource().getIODelegate() instanceof FileIODelegate) {
				FileIODelegate delegate = (FileIODelegate) getResource().getIODelegate();
				expectedScreenshotImageFile = new File(delegate.getFile().getParentFile(), getName() + ".diagram.png");
			}
			return expectedScreenshotImageFile;
		}

		private ScreenshotImage<DiagramPalette> buildAndSaveScreenshotImage() {
			if (getTechnologyAdapter().getDiagramPaletteScreenshotBuilder() != null) {
				ScreenshotBuilder<DiagramPalette> builder = getTechnologyAdapter().getDiagramPaletteScreenshotBuilder();

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

		private static ScreenshotImage<DiagramPalette> tryToLoadScreenshotImage() {
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
		public ScreenshotImage<DiagramPalette> getScreenshotImage() {
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
			if (!isModified()) {
				logger.info(">>>>>>>>>>>>>>> Palette " + this + " has been modified !!!");
			}
			screenshotModified = true;
		}

		@Override
		public DiagramPaletteResource getResource() {
			return resource;
		}

		@Override
		public void setResource(org.openflexo.foundation.resource.FlexoResource<DiagramPalette> resource) {
			this.resource = (DiagramPaletteResource) resource;
		}

	}

}
