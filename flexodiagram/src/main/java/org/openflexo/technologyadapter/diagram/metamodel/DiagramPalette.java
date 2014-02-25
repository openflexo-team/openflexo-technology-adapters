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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JComponent;

import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.resource.FlexoFileResource;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.ScreenshotBuilder;
import org.openflexo.foundation.resource.ScreenshotBuilder.ScreenshotImage;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterService;
import org.openflexo.foundation.validation.Validable;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.converter.RelativePathFileConverter;
import org.openflexo.swing.ImageUtils;
import org.openflexo.swing.ImageUtils.ImageType;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.rm.DiagramPaletteResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramPaletteResourceImpl;

@ModelEntity
@ImplementationClass(DiagramPalette.DiagramPaletteImpl.class)
@XMLElement
public interface DiagramPalette extends DiagramPaletteObject, ResourceData<DiagramPalette>, Comparable<DiagramPalette> {

	@PropertyIdentifier(type = DiagramPaletteElement.class, cardinality = Cardinality.LIST)
	public static final String PALETTE_ELEMENTS_KEY = "elements";
	@PropertyIdentifier(type = DrawingGraphicalRepresentation.class)
	public static final String GRAPHICAL_REPRESENTATION_KEY = "graphicalRepresentation";

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

	public abstract static class DiagramPaletteImpl extends DiagramPaletteObjectImpl implements DiagramPalette {

		static final Logger logger = Logger.getLogger(DiagramPalette.class.getPackage().getName());

		private int index;
		// private Vector<DiagramPaletteElement> _elements;
		private DiagramPaletteResource resource;
		// private DrawingGraphicalRepresentation graphicalRepresentation;

		private boolean screenshotModified = false;
		private ScreenshotImage<DiagramPalette> screenshotImage;
		private File expectedScreenshotImageFile = null;

		public static DiagramPaletteResource newDiagramPalette(DiagramSpecification diagramSpecification, String diagramPaletteName,
				DrawingGraphicalRepresentation graphicalRepresentation, FlexoServiceManager serviceManager) {
			DiagramPaletteResource diagramPaletteResource = DiagramPaletteResourceImpl.makeDiagramPaletteResource(
					diagramSpecification.getResource(), diagramPaletteName, serviceManager);
			DiagramPalette diagramPalette = diagramPaletteResource.getFactory().makeNewDiagramPalette();
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
		}

		// private DiagramPaletteFactory factory;

		// Used during deserialization, do not use it
		/*public DiagramPaletteImpl() {
			super();
			//_elements = new Vector<DiagramPaletteElement>();
		}*/

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
			return resource.getContainer().getDiagramSpecification();
		}

		@Override
		public boolean delete() {
			if (getDiagramSpecification() != null) {
				getDiagramSpecification().removeFromPalettes(this);
			}
			super.delete();
			deleteObservers();
			return true;
		}

		@Override
		public String getURI() {
			return resource.getURI();
		}

		@Override
		public Collection<? extends Validable> getEmbeddedValidableObjects() {
			return getElements();
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
				return getResource().getName();
			}
			return null;
		}

		@Override
		public void setName(String name) {
			if (requireChange(getName(), name)) {
				if (getResource() != null) {
					getResource().setName(name);
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

		public RelativePathFileConverter getRelativePathFileConverter() {
			return getResource().getContainer().getRelativePathFileConverter();
		}

		@Override
		public DiagramPaletteElement addPaletteElement(String name, Object graphicalRepresentation) {
			DiagramPaletteElement newElement = getResource().getFactory().makeDiagramPaletteElement();
			newElement.setName(name);
			newElement.setGraphicalRepresentation((ShapeGraphicalRepresentation) graphicalRepresentation);
			addToElements(newElement);
			return newElement;
		}

		@Override
		public int compareTo(DiagramPalette o) {
			return index - o.getIndex();
		}

		public DiagramTechnologyAdapter getTechnologyAdapter() {
			return getResource().getServiceManager().getService(TechnologyAdapterService.class)
					.getTechnologyAdapter(DiagramTechnologyAdapter.class);
		}

		private File getExpectedScreenshotImageFile() {
			if (expectedScreenshotImageFile == null && getResource() instanceof FlexoFileResource) {
				expectedScreenshotImageFile = new File(((FlexoFileResource<DiagramPalette>) getResource()).getFile().getParentFile(),
						getName() + ".diagram.png");
			}
			return expectedScreenshotImageFile;
		}

		private ScreenshotImage<DiagramPalette> buildAndSaveScreenshotImage() {
			ScreenshotBuilder<DiagramPalette> builder = new ScreenshotBuilder<DiagramPalette>() {
				@Override
				public String getScreenshotName(DiagramPalette o) {
					return o.getName();
				}

				@Override
				public JComponent getScreenshotComponent(DiagramPalette object) {
					/*ExternalVPMModule vpmModule = null;
					try {
						IModuleLoader moduleLoader = getViewPointLibrary().getServiceManager().getService(IModuleLoader.class);
						if (moduleLoader != null) {
							vpmModule = moduleLoader.getVPMModuleInstance();
						}
					} catch (ModuleLoadingException e) {
						logger.warning("cannot load VPM module (and so can't create screenshoot." + e.getMessage());
						e.printStackTrace();
					}

					if (vpmModule == null) {
						return null;
					}

					logger.info("Building " + getExpectedScreenshotImageFile().getAbsolutePath());

					JComponent c = vpmModule.createScreenshotForExampleDiagram(this);
					c.setOpaque(true);
					c.setBackground(Color.WHITE);
					JFrame frame = new JFrame();
					frame.setBackground(Color.WHITE);
					frame.setUndecorated(true);
					frame.getContentPane().add(c);
					frame.pack();
					c.validate();*/
					return null;
				}
			};

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

		private ScreenshotImage<DiagramPalette> tryToLoadScreenshotImage() {
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
