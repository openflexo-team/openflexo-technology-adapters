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

package org.openflexo.technologyadapter.diagram.fml.action;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation.DimensionConstraints;
import org.openflexo.fge.ShapeGraphicalRepresentation.LocationConstraints;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.resource.ScreenshotBuilder.ScreenshotImage;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.rm.BasicResourceImpl.LocatorNotFoundException;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.swing.ImageUtils;
import org.openflexo.swing.ImageUtils.ImageType;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteFactory;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.JavaUtils;
import org.openflexo.toolbox.StringUtils;

public class PushToPalette extends FlexoAction<PushToPalette, DiagramShape, DiagramElement<?>> {

	private static final Logger logger = Logger.getLogger(PushToPalette.class.getPackage().getName());

	public static FlexoActionType<PushToPalette, DiagramShape, DiagramElement<?>> actionType = new FlexoActionType<PushToPalette, DiagramShape, DiagramElement<?>>(
			"push_to_palette", FlexoActionType.defaultGroup, FlexoActionType.NORMAL_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public PushToPalette makeNewAction(DiagramShape focusedObject, Vector<DiagramElement<?>> globalSelection, FlexoEditor editor) {
			return new PushToPalette(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramShape shape, Vector<DiagramElement<?>> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(DiagramShape shape, Vector<DiagramElement<?>> globalSelection) {
			return shape != null /* && shape.getVirtualModel().getPalettes().size() > 0*/;
		}

	};

	private TypedDiagramModelSlot diagramModelSlot;

	private VirtualModel virtualModel;

	private VirtualModelResource virtualModelResource;

	public GraphicalRepresentation graphicalRepresentation;
	public DiagramPalette palette;
	public int xLocation;
	public int yLocation;;
	private FlexoConcept flexoConcept;
	public DropScheme dropScheme;
	private String newElementName;
	public boolean takeScreenshotForTopLevelElement = true;
	public boolean overrideDefaultGraphicalRepresentations = false;

	private String errorMessage;

	private ScreenshotImage<DiagramShape> screenshot;
	public int imageWidth;
	public int imageHeight;

	private Image image;

	private DiagramPaletteElement _newPaletteElement;

	static {
		FlexoObjectImpl.addActionForClass(PushToPalette.actionType, DiagramShape.class);
	}

	public static enum PushToPaletteChoices {
		CONFIGURE_FML_CONTROL, FREE
	}

	public PushToPaletteChoices primaryChoice = PushToPaletteChoices.CONFIGURE_FML_CONTROL;

	protected PushToPalette(DiagramShape focusedObject, Vector<DiagramElement<?>> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
		diagramElementEntries = new Vector<DiagramElementEntry>();
		updateDrawingObjectEntries();
	}

	/**
	 * Return the model slot which encodes the access to a {@link Diagram} conform to a {@link DiagramSpecification}, in the context of a
	 * {@link VirtualModel} (which is a context where a diagram is federated with other sources of informations)
	 * 
	 * @return
	 */
	public TypedDiagramModelSlot getDiagramModelSlot() {
		if (virtualModel != null) {
			return FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(virtualModel);
		}
		return null;
	}

	/**
	 * Sets the model slot which encodes the access to a {@link Diagram} conform to a {@link DiagramSpecification}, in the context of a
	 * {@link VirtualModel} (which is a context where a diagram is federated with other sources of informations)
	 * 
	 * @return
	 */
	/*public void setDiagramModelSlot(TypedDiagramModelSlot diagramModelSlot) {
		this.diagramModelSlot = diagramModelSlot;
	}*/

	@Override
	protected void doAction(Object context) {
		logger.info("Push to palette");

		if (getFocusedObject() != null && palette != null) {

			DiagramPaletteFactory factory = palette.getFactory();

			if (takeScreenshotForTopLevelElement) {
				Resource screenshotFile = null;
				try {
					screenshotFile = new FileResourceImpl(saveScreenshot());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (LocatorNotFoundException e) {
					e.printStackTrace();
				}
				ShapeGraphicalRepresentation gr = factory.makeShapeGraphicalRepresentation(ShapeType.RECTANGLE);
				gr.setForeground(factory.makeNoneForegroundStyle());
				gr.setBackground(factory.makeImageBackground(screenshotFile));
				gr.setShadowStyle(factory.makeNoneShadowStyle());
				gr.setTextStyle(factory.makeDefaultTextStyle());
				gr.setText("");
				gr.setWidth(imageWidth);
				gr.setHeight(imageHeight);
				gr.setDimensionConstraints(DimensionConstraints.UNRESIZABLE);
				gr.setIsFloatingLabel(false);
				gr.setX(xLocation);
				gr.setY(yLocation);
				graphicalRepresentation = gr;
			} else {
				GraphicalRepresentation gr = getFocusedObject().getGraphicalRepresentation();
				if (gr instanceof ShapeGraphicalRepresentation) {
					graphicalRepresentation = factory.makeShapeGraphicalRepresentation();
					graphicalRepresentation.setsWith(gr);
					((ShapeGraphicalRepresentation) graphicalRepresentation).setX(xLocation);
					((ShapeGraphicalRepresentation) graphicalRepresentation).setY(yLocation);
				} else if (gr instanceof ConnectorGraphicalRepresentation) {
					graphicalRepresentation = factory.makeConnectorGraphicalRepresentation();
					((ConnectorGraphicalRepresentation) graphicalRepresentation).setsWith(gr);
				}
			}

			((ShapeGraphicalRepresentation) graphicalRepresentation).setLocationConstraints(LocationConstraints.FREELY_MOVABLE);
			_newPaletteElement = palette.addPaletteElement(newElementName, graphicalRepresentation);

			if (primaryChoice.equals(PushToPaletteChoices.CONFIGURE_FML_CONTROL)) {
				FMLDiagramPaletteElementBinding newBinding = getFactory().newInstance(FMLDiagramPaletteElementBinding.class);
				newBinding.setPaletteElement(_newPaletteElement);
				newBinding.setDiagramModelSlot(getDiagramModelSlot());
				newBinding.setBoundFlexoConcept(flexoConcept);
				newBinding.setDropScheme(dropScheme);
				newBinding.setBoundLabelToElementName(!takeScreenshotForTopLevelElement);

				getDiagramModelSlot().addToPaletteElementBindings(newBinding);
			}

			/*for (DrawingObjectEntry entry : diagramElementEntries) {
				if(!entry.isMainEntry()){
					if (entry.graphicalObject.getGraphicalRepresentation() instanceof ShapeGraphicalRepresentation) {
						ShapeGraphicalRepresentation subGr = new ShapeGraphicalRepresentation();
						ShapeGraphicalRepresentation test = ((ShapeGraphicalRepresentation)graphicalRepresentation);
						((ShapeGraphicalRepresentation) subGr).setsWith((ShapeGraphicalRepresentation)entry.graphicalObject.getGraphicalRepresentation());
						List grs = test.getOrderedContainedGraphicalRepresentations();
						if(grs!=null){
							grs.add(subGr);
						}
						else{
							grs=new ArrayList();
						}
						
					}
				}
			}*/

			if (overrideDefaultGraphicalRepresentations) {
				for (DiagramElementEntry entry : diagramElementEntries) {
					if (entry.getSelectThis()) {
						// TODO
						/*if (entry.graphicalObject instanceof GRShapeTemplate) {
							_newPaletteElement.addToOverridingGraphicalRepresentations(new ShapeOverridingGraphicalRepresentation(
									entry.patternRole, (ShapeGraphicalRepresentation) entry.graphicalObject.getGraphicalRepresentation()));
						} else if (entry.graphicalObject instanceof GRConnectorTemplate) {
							_newPaletteElement.addToOverridingGraphicalRepresentations(new ConnectorOverridingGraphicalRepresentation(
									entry.patternRole, (ConnectorGraphicalRepresentation) entry.graphicalObject
											.getGraphicalRepresentation()));
						}*/
					}
				}
			}

		} else {
			logger.warning("Focused property is null !");
		}
	}

	public DiagramPaletteElement getNewPaletteElement() {
		return _newPaletteElement;
	}

	public String getNewElementName() {
		return newElementName;
	}

	public void setNewElementName(String newElementName) {
		this.newElementName = newElementName;
	}

	@Override
	public boolean isValid() {
		if (!StringUtils.isNotEmpty(newElementName)) {
			setErrorMessage(noNameMessage());
			return false;
		}

		if (palette == null) {
			setErrorMessage(noPaletteSelectedMessage());
			return false;
		}

		if (primaryChoice.equals(PushToPaletteChoices.CONFIGURE_FML_CONTROL)) {
			if (flexoConcept == null) {
				setErrorMessage(noFlexoConceptSelectedMessage());
				return false;
			}

			if (dropScheme == null) {
				setErrorMessage(noDropSchemeSelectedMessage());
				return false;
			}

			if (virtualModel == null) {
				setErrorMessage(noVirtualModelSelectedMessage());
				return false;
			}
		}

		return true;
	}

	public String noNameMessage() {
		return FlexoLocalization.localizedForKey("no_palette_element_name_defined");
	}

	public String noPaletteSelectedMessage() {
		return FlexoLocalization.localizedForKey("no_palette_selected");
	}

	public String noFlexoConceptSelectedMessage() {
		return FlexoLocalization.localizedForKey("no_flexo_concept_selected");
	}

	public String noDropSchemeSelectedMessage() {
		return FlexoLocalization.localizedForKey("no_drop_scheme_selected");
	}

	public String noVirtualModelSelectedMessage() {
		return FlexoLocalization.localizedForKey("no_virtual_model_selected");
	}

	private List<DiagramElementEntry> diagramElementEntries;

	public List<DiagramElementEntry> getDiagramElementEntries() {
		return diagramElementEntries;
	}

	public void setDiagramElementEntries(List<DiagramElementEntry> diagramElementEntries) {
		this.diagramElementEntries = diagramElementEntries;
	}

	public class DiagramElementEntry {
		private boolean selectThis;
		public DiagramElement<?> graphicalObject;
		public String elementName;
		public GraphicalElementRole<?, ?> patternRole;

		public DiagramElementEntry(DiagramElement<?> graphicalObject, String elementName) {
			super();
			this.graphicalObject = graphicalObject;
			this.elementName = elementName;
			this.selectThis = isMainEntry();
			/*if (isMainEntry() && flexoConcept != null) {
				patternRole = flexoConcept.getDefaultPrimaryRepresentationRole();
			}*/
		}

		public boolean isMainEntry() {
			return graphicalObject == getFocusedObject();
		}

		public boolean getSelectThis() {
			if (isMainEntry()) {
				return true;
			}
			return selectThis;
		}

		public void setSelectThis(boolean aFlag) {
			selectThis = aFlag;
			if (patternRole == null && graphicalObject instanceof DiagramShape && flexoConcept != null) {
				GraphicalElementRole<?, ?> parentEntryPatternRole = getParentEntry().patternRole;
				for (ShapeRole r : flexoConcept.getFlexoProperties(ShapeRole.class)) {
					if (r.getParentShapeRole() == parentEntryPatternRole && patternRole == null) {
						patternRole = r;
					}
				}
			}
		}

		public DiagramElementEntry getParentEntry() {
			return getEntry(graphicalObject.getParent());
		}

		public List<? extends GraphicalElementRole<?, ?>> getAvailablePatternRoles() {
			if (graphicalObject instanceof DiagramShape) {
				return flexoConcept.getFlexoProperties(ShapeRole.class);
			} else if (graphicalObject instanceof DiagramConnector) {
				return flexoConcept.getFlexoProperties(ConnectorRole.class);
			}
			return null;
		}
	}

	public int getSelectedEntriesCount() {
		int returned = 0;
		for (DiagramElementEntry e : diagramElementEntries) {
			if (e.selectThis) {
				returned++;
			}
		}
		return returned;
	}

	public DiagramElementEntry getEntry(DiagramElement<?> o) {
		for (DiagramElementEntry e : diagramElementEntries) {
			if (e.graphicalObject == o) {
				return e;
			}
		}
		return null;
	}

	public FlexoConcept getFlexoConcept() {
		return flexoConcept;
	}

	public void setFlexoConcept(FlexoConcept flexoConcept) {
		this.flexoConcept = flexoConcept;
		updateDrawingObjectEntries();
	}

	public List<DropScheme> getAvailableDropSchemes() {
		if (getFlexoConcept() != null) {
			return getFlexoConcept().getFlexoBehaviours(DropScheme.class);
		}
		return null;
	}

	private void updateDrawingObjectEntries() {
		diagramElementEntries.clear();
		int shapeIndex = 1;
		int connectorIndex = 1;
		List<? extends DiagramElement<?>> elements = (getFocusedObject() instanceof DiagramContainerElement ? ((DiagramContainerElement<?>) getFocusedObject())
				.getDescendants() : Collections.singletonList(getFocusedObject()));

		for (DiagramElement<?> o : elements) {
			if (o instanceof DiagramShape) {
				DiagramShape shape = (DiagramShape) o;
				String shapeRoleName = StringUtils.isEmpty(shape.getName()) ? "shape" + (shapeIndex > 1 ? shapeIndex : "") : shape
						.getName();
				diagramElementEntries.add(new DiagramElementEntry(shape, shapeRoleName));
				shapeIndex++;
			}
			if (o instanceof DiagramConnector) {
				DiagramConnector connector = (DiagramConnector) o;
				String connectorRoleName = "connector" + (connectorIndex > 1 ? connectorIndex : "");
				diagramElementEntries.add(new DiagramElementEntry(connector, connectorRoleName));
				connectorIndex++;
			}
		}

	}

	public ScreenshotImage<DiagramShape> getScreenshot() {
		if (this.screenshot == null || this.screenshot != getFocusedObject().getScreenshotImage()) {
			setScreenshot(getFocusedObject().getScreenshotImage());
		}
		return this.screenshot;
	}

	public void setScreenshot(ScreenshotImage<DiagramShape> screenshot) {
		this.screenshot = screenshot;
		imageWidth = screenshot.image.getWidth(null);
		imageHeight = screenshot.image.getHeight(null);
	}

	public File saveScreenshot() {
		File imageFile = new File(ResourceLocator.retrieveResourceAsFile(getDiagramSpecification().getResource().getDirectory()),
				JavaUtils.getClassName(newElementName) + ".palette-element" + ".png");
		logger.info("Saving " + imageFile);
		try {
			ImageUtils.saveImageToFile(getScreenshot().image, imageFile, ImageType.PNG);
			return imageFile;
		} catch (IOException e) {
			e.printStackTrace();
			logger.warning("Could not save " + imageFile.getAbsolutePath());
			return null;
		}
	}

	public DiagramSpecification getDiagramSpecification() {
		return getFocusedObject().getDiagram().getDiagramSpecification();
	}

	public VirtualModel getVirtualModel() {
		return virtualModel;
	}

	public void setVirtualModel(VirtualModel virtualModel) {
		this.virtualModel = virtualModel;
	}

	public FMLModelFactory getFactory() {
		if (getVirtualModel() != null) {
			return getVirtualModel().getFMLModelFactory();
		}
		return null;
	}

	public VirtualModelResource getVirtualModelResource() {
		return virtualModelResource;
	}

	public void setVirtualModelResource(VirtualModelResource virtualModelResource) {
		this.virtualModelResource = virtualModelResource;
		setVirtualModel(virtualModelResource.getVirtualModel());
	}

	public String getErrorMessage() {
		isValid();
		// System.out.println("valid=" + isValid());
		// System.out.println("errorMessage=" + errorMessage);
		return errorMessage;
	}

	public void setErrorMessage(String message) {
		this.errorMessage = message;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}
