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
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.fge.ScreenshotBuilder.ScreenshotImage;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation.DimensionConstraints;
import org.openflexo.fge.ShapeGraphicalRepresentation.LocationConstraints;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.rm.BasicResourceImpl.LocatorNotFoundException;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.swing.ImageUtils;
import org.openflexo.swing.ImageUtils.ImageType;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteFactory;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.JavaUtils;
import org.openflexo.toolbox.StringUtils;

/**
 * Abstract base implementation for an action which allows to create new palette element
 * 
 * @author sylvain
 *
 * @param <A>
 * @param <T1>
 * @param <T2>
 */
public abstract class AbstractCreatePaletteElement<A extends FlexoAction<A, T1, T2>, T1 extends FlexoObject, T2 extends FlexoObject>
		extends FlexoAction<A, T1, T2> {

	private static final Logger logger = Logger.getLogger(AbstractCreatePaletteElement.class.getPackage().getName());

	// private TypedDiagramModelSlot diagramModelSlot;

	private VirtualModel virtualModel;

	private VirtualModelResource virtualModelResource;

	private GraphicalRepresentation paletteElementGraphicalRepresentation;
	private DiagramPalette palette;
	private FlexoConcept flexoConcept;
	private DropScheme dropScheme;
	private String newElementName;
	private boolean takeScreenshotForTopLevelElement = false;
	private boolean overrideDefaultGraphicalRepresentations = false;

	private ScreenshotImage<DiagramShape> screenshot;
	private int imageWidth;
	private int imageHeight;
	private Image image;

	private DiagramPaletteElement newPaletteElement;

	private boolean configureFMLControls = true;

	protected List<GraphicalElementEntry> diagramElementEntries;

	private static final int X_OFFSET = 10;
	private static final int Y_OFFSET = 10;

	protected AbstractCreatePaletteElement(FlexoActionFactory actionType, T1 focusedObject, Vector<T2> globalSelection,
			FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
		diagramElementEntries = new Vector<>();
		updateDiagramElementsEntries();
	}

	protected void updateDiagramElementsEntries() {
		diagramElementEntries.clear();
	}

	/**
	 * Return the model slot which encodes the access to a {@link Diagram} conform to a {@link DiagramSpecification}, in the context of a
	 * {@link VirtualModel} (which is a context where a diagram is federated with other sources of informations)
	 * 
	 * @return
	 */
	public TypedDiagramModelSlot getDiagramModelSlot() {
		if (getVirtualModel() != null) {
			return FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(getVirtualModel());
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

	public boolean getConfigureFMLControls() {
		return configureFMLControls;
	}

	public void setConfigureFMLControls(boolean configureFMLControls) {
		if (configureFMLControls != this.configureFMLControls) {
			this.configureFMLControls = configureFMLControls;
			getPropertyChangeSupport().firePropertyChange("configureFMLControls", !configureFMLControls, configureFMLControls);
		}
	}

	@Override
	protected void doAction(Object context) {
		logger.info("Push to palette");

		if (getFocusedObject() != null && palette != null) {

			DiagramPaletteFactory factory = palette.getFactory();

			Point location = retrieveNextAvailableLocation();

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
				gr.setX(location.x);
				gr.setY(location.y);
				paletteElementGraphicalRepresentation = gr;
			}
			else {
				// We don't care, we take any role since only one GraphicalRepresentation can be put in DiagramPaletteElement
				GraphicalRepresentation gr = makePaletteElementGraphicalRepresentation();
				// GraphicalRepresentation gr = getFocusedObject().getGraphicalRepresentation();
				if (gr instanceof ShapeGraphicalRepresentation) {
					paletteElementGraphicalRepresentation = factory.makeShapeGraphicalRepresentation();
					paletteElementGraphicalRepresentation.setsWith(gr);
					((ShapeGraphicalRepresentation) paletteElementGraphicalRepresentation).setX(location.x);
					((ShapeGraphicalRepresentation) paletteElementGraphicalRepresentation).setY(location.y);
				}
				else if (gr instanceof ConnectorGraphicalRepresentation) {
					paletteElementGraphicalRepresentation = factory.makeConnectorGraphicalRepresentation();
					((ConnectorGraphicalRepresentation) paletteElementGraphicalRepresentation).setsWith(gr);
				}
			}

			((ShapeGraphicalRepresentation) paletteElementGraphicalRepresentation)
					.setLocationConstraints(LocationConstraints.FREELY_MOVABLE);
			newPaletteElement = palette.addPaletteElement(getNewElementName(), paletteElementGraphicalRepresentation);

			if (getConfigureFMLControls()) {
				FMLDiagramPaletteElementBinding newBinding = getFactory().newInstance(FMLDiagramPaletteElementBinding.class);
				newBinding.setPaletteElement(newPaletteElement);
				newBinding.setDiagramModelSlot(getDiagramModelSlot());
				newBinding.setBoundFlexoConcept(getFlexoConcept());
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
				for (GraphicalElementEntry entry : diagramElementEntries) {
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

		}
		else {
			logger.warning("Focused property is null !");
		}
	}

	public abstract ShapeGraphicalRepresentation makePaletteElementGraphicalRepresentation();

	protected Point retrieveNextAvailableLocation() {
		if (getPalette() != null) {
			if (getPalette().getElements().size() > 0) {
				DiagramPaletteElement lastElement = getPalette().getElements().get(getPalette().getElements().size() - 1);
				if (lastElement.getGraphicalRepresentation().getX() + lastElement.getGraphicalRepresentation().getWidth()
						+ X_OFFSET < 200) {
					return new Point((int) (lastElement.getGraphicalRepresentation().getX()
							+ lastElement.getGraphicalRepresentation().getWidth() + X_OFFSET),
							(int) lastElement.getGraphicalRepresentation().getY());
				}
				return new Point((X_OFFSET), (int) (lastElement.getGraphicalRepresentation().getY()
						+ lastElement.getGraphicalRepresentation().getHeight() + Y_OFFSET));
			}
			return new Point(X_OFFSET, Y_OFFSET);
		}
		return new Point(0, 0);
	}

	public DiagramPaletteElement getNewPaletteElement() {
		return newPaletteElement;
	}

	public String getNewElementName() {
		if (newElementName == null && getFlexoConcept() != null) {
			return JavaUtils.getClassName(getFlexoConcept().getName());
		}
		return newElementName;
	}

	public void setNewElementName(String newElementName) {
		if ((newElementName == null && this.newElementName != null)
				|| (newElementName != null && !newElementName.equals(this.newElementName))) {
			String oldValue = this.newElementName;
			this.newElementName = newElementName;
			getPropertyChangeSupport().firePropertyChange("newElementName", oldValue, newElementName);
		}
	}

	@Override
	public boolean isValid() {
		if (getPalette() == null) {
			return false;
		}

		if (getConfigureFMLControls()) {
			if (getFlexoConcept() == null) {
				return false;
			}

			if (getDropScheme() == null) {
				return false;
			}

			if (getVirtualModel() == null) {
				return false;
			}
		}

		if (StringUtils.isEmpty(getNewElementName())) {
			return false;
		}

		if (getPalette().getPaletteElement(getNewElementName()) != null) {
			return false;
		}

		return true;
	}

	public List<GraphicalElementEntry> getDiagramElementEntries() {
		return diagramElementEntries;
	}

	public void setDiagramElementEntries(List<GraphicalElementEntry> diagramElementEntries) {
		this.diagramElementEntries = diagramElementEntries;
	}

	public abstract class GraphicalElementEntry {
		private boolean selectThis;
		private String elementName;
		private GraphicalElementRole<?, ?> flexoRole;

		public GraphicalElementEntry(String elementName) {
			super();
			this.elementName = elementName;
			this.selectThis = isMainEntry();
		}

		public String getElementName() {
			return elementName;
		}

		public void setElementName(String elementName) {
			if ((elementName == null && this.elementName != null) || (elementName != null && !elementName.equals(this.elementName))) {
				String oldValue = this.elementName;
				this.elementName = elementName;
				getPropertyChangeSupport().firePropertyChange("elementName", oldValue, elementName);
			}
		}

		public GraphicalElementRole<?, ?> getFlexoRole() {
			return flexoRole;
		}

		public void setFlexoRole(GraphicalElementRole<?, ?> flexoRole) {
			if ((flexoRole == null && this.flexoRole != null) || (flexoRole != null && !flexoRole.equals(this.flexoRole))) {
				GraphicalElementRole<?, ?> oldValue = this.flexoRole;
				this.flexoRole = flexoRole;
				getPropertyChangeSupport().firePropertyChange("flexoRole", oldValue, flexoRole);
			}
		}

		public abstract boolean isMainEntry();

		public abstract boolean getSelectThis();

		public abstract void setSelectThis(boolean aFlag);

		public abstract GraphicalElementEntry getParentEntry();

		public abstract List<? extends GraphicalElementRole<?, ?>> getAvailableFlexoRoles();
	}

	public int getSelectedEntriesCount() {
		int returned = 0;
		for (GraphicalElementEntry e : diagramElementEntries) {
			if (e.selectThis) {
				returned++;
			}
		}
		return returned;
	}

	public FlexoConcept getFlexoConcept() {
		return flexoConcept;
	}

	public void setFlexoConcept(FlexoConcept flexoConcept) {
		if (flexoConcept != this.flexoConcept) {
			FlexoConcept oldValue = this.flexoConcept;
			this.flexoConcept = flexoConcept;
			updateDiagramElementsEntries();
			getPropertyChangeSupport().firePropertyChange("flexoConcept", oldValue, flexoConcept);
			getPropertyChangeSupport().firePropertyChange("elementName", null, getNewElementName());
		}
	}

	public List<DropScheme> getAvailableDropSchemes() {
		if (getFlexoConcept() != null) {
			return getFlexoConcept().getFlexoBehaviours(DropScheme.class);
		}
		return null;
	}

	public abstract ScreenshotImage<DiagramShape> makeScreenshot();

	public ScreenshotImage<DiagramShape> getScreenshot() {
		if (this.screenshot == null /*|| this.screenshot != getFocusedObject().getScreenshotImage()*/) {
			setScreenshot(makeScreenshot());
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
		if (getDiagramModelSlot() != null) {
			return getDiagramModelSlot().getDiagramSpecification();
		}
		return null;
		// return getFocusedObject().getDiagram().getDiagramSpecification();
	}

	public VirtualModel getVirtualModel() {
		return virtualModel;
	}

	public void setVirtualModel(VirtualModel virtualModel) {
		if (virtualModel != this.virtualModel) {
			VirtualModel oldValue = this.virtualModel;
			this.virtualModel = virtualModel;
			getPropertyChangeSupport().firePropertyChange("virtualModel", oldValue, virtualModel);
		}
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

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public DiagramPalette getPalette() {
		return palette;
	}

	public void setPalette(DiagramPalette palette) {
		if ((palette == null && this.palette != null) || (palette != null && !palette.equals(this.palette))) {
			DiagramPalette oldValue = this.palette;
			this.palette = palette;
			getPropertyChangeSupport().firePropertyChange("palette", oldValue, palette);
		}
	}

	public DropScheme getDropScheme() {
		return dropScheme;
	}

	public void setDropScheme(DropScheme dropScheme) {
		if (dropScheme != this.dropScheme) {
			DropScheme oldValue = this.dropScheme;
			this.dropScheme = dropScheme;
			getPropertyChangeSupport().firePropertyChange("dropScheme", oldValue, dropScheme);
		}
	}

	public boolean takeScreenshotForTopLevelElement() {
		return takeScreenshotForTopLevelElement;
	}

	public void setTakeScreenshotForTopLevelElement(boolean takeScreenshotForTopLevelElement) {
		if (takeScreenshotForTopLevelElement != this.takeScreenshotForTopLevelElement) {
			this.takeScreenshotForTopLevelElement = takeScreenshotForTopLevelElement;
			getPropertyChangeSupport().firePropertyChange("takeScreenshotForTopLevelElement", !takeScreenshotForTopLevelElement,
					takeScreenshotForTopLevelElement);
		}
	}

	public boolean overrideDefaultGraphicalRepresentations() {
		return overrideDefaultGraphicalRepresentations;
	}

	public void setOverrideDefaultGraphicalRepresentations(boolean overrideDefaultGraphicalRepresentations) {
		if (overrideDefaultGraphicalRepresentations != this.overrideDefaultGraphicalRepresentations) {
			this.overrideDefaultGraphicalRepresentations = overrideDefaultGraphicalRepresentations;
			getPropertyChangeSupport().firePropertyChange("overrideDefaultGraphicalRepresentations",
					!overrideDefaultGraphicalRepresentations, overrideDefaultGraphicalRepresentations);
		}
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}
}
