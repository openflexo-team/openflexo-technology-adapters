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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.openflexo.fge.ScreenshotBuilder;
import org.openflexo.fge.ScreenshotBuilder.ScreenshotImage;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.resource.FileIODelegate;
import org.openflexo.swing.ImageUtils;
import org.openflexo.swing.ImageUtils.ImageType;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;

public abstract class DiagramShapeImpl extends DiagramContainerElementImpl<ShapeGraphicalRepresentation> implements DiagramShape {

	private static final Logger logger = Logger.getLogger(DiagramShapeImpl.class.getPackage().getName());

	private boolean screenshotModified = false;
	private ScreenshotImage<DiagramShape> screenshotImage;
	private File expectedScreenshotImageFile = null;

	// private String multilineText;
	// private Vector<DiagramConnector> incomingConnectors;
	// private Vector<DiagramConnector> outgoingConnectors;

	// private FlexoConceptInstance flexoConceptInstance;

	/**
	 * Constructor invoked during deserialization
	 * 
	 * @param componentDefinition
	 */
	/*public DiagramShapeImpl(DiagramBuilder builder) {
		this((Diagram) builder.vmInstance);
		initializeDeserialization(builder);
	}*/

	/**
	 * Default constructor for OEShema
	 * 
	 * @param shemaDefinition
	 */
	/*public DiagramShapeImpl(Diagram diagram) {
		super(diagram);
		incomingConnectors = new Vector<DiagramConnector>();
		outgoingConnectors = new Vector<DiagramConnector>();
	}*/

	// @Override
	/*public void setDescription(String description) {
		super.setDescription(description);
	}*/
	/**
	 * Reset graphical representation to be the one defined in related pattern property
	 */
	/*@Override
	public void resetGraphicalRepresentation() {
		getGraphicalRepresentation().setsWith(getPatternRole().getGraphicalRepresentation(), GraphicalRepresentation.TEXT,
				GraphicalRepresentation.IS_VISIBLE, GraphicalRepresentation.TRANSPARENCY, GraphicalRepresentation.ABSOLUTE_TEXT_X,
				GraphicalRepresentation.ABSOLUTE_TEXT_Y, ShapeGraphicalRepresentation.X, ShapeGraphicalRepresentation.Y,
				ShapeGraphicalRepresentation.WIDTH, ShapeGraphicalRepresentation.HEIGHT, ShapeGraphicalRepresentation.RELATIVE_TEXT_X,
				ShapeGraphicalRepresentation.RELATIVE_TEXT_Y);
		refreshGraphicalRepresentation();
	}*/
	/**
	 * Refresh graphical representation
	 */
	/*@Override
	public void refreshGraphicalRepresentation() {
		super.refreshGraphicalRepresentation();
		getGraphicalRepresentation().updateConstraints();
		getGraphicalRepresentation().notifyShapeNeedsToBeRedrawn();
		getGraphicalRepresentation().notifyObjectHasMoved();
	}*/
	/*@Override
	public boolean delete() {
		if (getParent() != null) {
			getParent().removeFromChilds(this);
		}
		for (DiagramConnector c : incomingConnectors) {
			c.delete();
		}
		for (DiagramConnector c : outgoingConnectors) {
			c.delete();
		}
		super.delete();
		deleteObservers();
		return true;
	}*/
	/*public Vector<DiagramConnector> getIncomingConnectors() {
		return incomingConnectors;
	}

	public void setIncomingConnectors(Vector<DiagramConnector> incomingConnectors) {
		this.incomingConnectors = incomingConnectors;
	}

	public void addToIncomingConnectors(DiagramConnector connector) {
		incomingConnectors.add(connector);
	}

	public void removeFromIncomingConnectors(DiagramConnector connector) {
		incomingConnectors.remove(connector);
	}

	public Vector<DiagramConnector> getOutgoingConnectors() {
		return outgoingConnectors;
	}

	public void setOutgoingConnectors(Vector<DiagramConnector> outgoingConnectors) {
		this.outgoingConnectors = outgoingConnectors;
	}

	public void addToOutgoingConnectors(DiagramConnector connector) {
		outgoingConnectors.add(connector);
	}

	public void removeFromOutgoingConnectors(DiagramConnector connector) {
		outgoingConnectors.remove(connector);
	}*/

	/*@Override
	public String getDisplayableDescription() {
		return "ShapeSpecification" + (getFlexoConcept() != null ? " representing " + getFlexoConcept() : "");
	}*/

	/*public static class DropAndLinkScheme {
		public DropAndLinkScheme(DropScheme dropScheme, LinkScheme linkScheme) {
			super();
			this.dropScheme = dropScheme;
			this.linkScheme = linkScheme;
		}

		public DropScheme dropScheme;
		public LinkScheme linkScheme;

	}

	public Vector<DropAndLinkScheme> getAvailableDropAndLinkSchemeFromThisShape(FlexoConcept targetFlexoConcept) {
		if (getFlexoConcept() == null) {
			return null;
		}

		Vector<DropAndLinkScheme> availableLinkSchemeFromThisShape = null;

		ViewPoint viewPoint = getDiagram().getViewPoint();
		if (viewPoint == null) {
			return null;
		}

		availableLinkSchemeFromThisShape = new Vector<DropAndLinkScheme>();

		for (FlexoConcept ep1 : getDiagramSpecification().getFlexoConcepts()) {
			for (DropScheme ds : ep1.getDropSchemes()) {
				if (ds.getTargetFlexoConcept() == targetFlexoConcept || ds.getTopTarget() && targetFlexoConcept == null) {
					for (FlexoConcept ep2 : getDiagramSpecification().getFlexoConcepts()) {
						for (LinkScheme ls : ep2.getLinkSchemes()) {
							// Let's directly reuse the code that exists in the LinkScheme instead of re-writing it here.
							if (ls.isValidTarget(ep2, ds.getFlexoConcept()) && ls.getIsAvailableWithFloatingPalette()) {
								// This candidate is acceptable
								availableLinkSchemeFromThisShape.add(new DropAndLinkScheme(ds, ls));
							}
						}
					}
				}
			}
		}

		return availableLinkSchemeFromThisShape;
	}

	public Vector<LinkScheme> getAvailableLinkSchemeFromThisShape() {
		if (getFlexoConcept() == null) {
			return null;
		}

		Vector<LinkScheme> availableLinkSchemeFromThisShape = null;

		ViewPoint calc = getDiagram().getViewPoint();
		if (calc == null) {
			return null;
		}

		availableLinkSchemeFromThisShape = new Vector<LinkScheme>();

		for (FlexoConcept ep : getDiagramSpecification().getFlexoConcepts()) {
			for (LinkScheme ls : ep.getLinkSchemes()) {
				if (ls.getFromTargetFlexoConcept() != null && ls.getFromTargetFlexoConcept().isAssignableFrom(getFlexoConcept())
						&& ls.getIsAvailableWithFloatingPalette()) {
					// This candidate is acceptable
					availableLinkSchemeFromThisShape.add(ls);
				}
			}
		}

		return availableLinkSchemeFromThisShape;
	}

	@Override
	public ShapeRole getPatternRole() {
		return (ShapeRole) super.getPatternRole();
	}*/

	@Override
	public ShapeRole getPatternRole(VirtualModelInstance vmInstance) {
		return (ShapeRole) super.getPatternRole(vmInstance);
	}

	private File getExpectedScreenshotImageFile() {
		if (expectedScreenshotImageFile == null && getDiagram().getResource().getIODelegate() instanceof FileIODelegate) {
			FileIODelegate delegate = (FileIODelegate) (getDiagram().getResource()).getIODelegate();
			expectedScreenshotImageFile = new File(delegate.getFile().getParentFile(), getName() + ".diagram_container_element.png");
		}
		return expectedScreenshotImageFile;
	}

	private ScreenshotImage<DiagramShape> buildAndSaveScreenshotImage() {
		if (getTechnologyAdapter().getScreenshotBuilder() != null) {
			ScreenshotBuilder<DiagramShape> builder = getTechnologyAdapter().getDiagramShapeScreenshotBuilder();

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

	private ScreenshotImage<DiagramShape> tryToLoadScreenshotImage() {
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
	public ScreenshotImage<DiagramShape> getScreenshotImage() {
		if (screenshotImage == null || screenshotModified) {
			if (screenshotModified) {
				logger.info("Rebuilding screenshot for " + this + " because screenshot is modified");
			}
			buildAndSaveScreenshotImage();
		}
		return screenshotImage;
	}

	// @Override
	@Override
	public boolean delete(Object... context) {
		// A list of connectors that may be deleted if a shape is connected to it
		List<DiagramConnector> dependingConnectors = new ArrayList<DiagramConnector>();
		dependingConnectors.addAll(getStartConnectors());
		dependingConnectors.addAll(getEndConnectors());
		for (Iterator<DiagramConnector> connectors = dependingConnectors.iterator(); connectors.hasNext();) {
			DiagramConnector connector = connectors.next();
			if (!connector.isDeleted()) {
				logger.info("Try to delete undeleted DiagramConnector " + connector);
				connector.delete();
				logger.info("DiagramConnector " + connector + " has been successfully deleted");
			} else {
				logger.info("DiagramConnector " + connector + " has already been successfully deleted");
			}
		}
		return super.delete(context);
	}
}
