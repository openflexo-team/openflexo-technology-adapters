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
package org.openflexo.technologyadapter.diagram.model;

import java.util.logging.Logger;

import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;

public abstract class DiagramShapeImpl extends DiagramContainerElementImpl<ShapeGraphicalRepresentation> implements DiagramShape {

	private static final Logger logger = Logger.getLogger(DiagramShapeImpl.class.getPackage().getName());

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
	 * Reset graphical representation to be the one defined in related pattern role
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

}
