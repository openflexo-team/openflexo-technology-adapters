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

import java.util.logging.Logger;

import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;

public abstract class DiagramConnectorImpl extends DiagramElementImpl<ConnectorGraphicalRepresentation> implements DiagramConnector {

	private static final Logger logger = Logger.getLogger(DiagramShape.class.getPackage().getName());

	// private DiagramShape startShape;
	// private DiagramShape endShape;

	/**
	 * Constructor invoked during deserialization
	 * 
	 * @param componentDefinition
	 */
	/*public DiagramConnectorImpl(DiagramBuilder builder) {
		this((Diagram) builder.vmInstance);
		initializeDeserialization(builder);
	}*/

	/**
	 * Default constructor for OEShema
	 * 
	 * @param shemaDefinition
	 */
	/*public DiagramConnectorImpl(Diagram diagram) {
		super(diagram);
	}*/

	/**
	 * Common constructor for OEShema
	 * 
	 * @param shemaDefinition
	 */
	/*public DiagramConnectorImpl(Diagram diagram, DiagramShape aStartShape, DiagramShape anEndShape) {
		super(diagram);
		setStartShape(aStartShape);
		setEndShape(anEndShape);
	}*/

	/**
	 * Reset graphical representation to be the one defined in related pattern role
	 */
	/*@Override
	public void resetGraphicalRepresentation() {
		getGraphicalRepresentation().setsWith(getPatternRole().getGraphicalRepresentation(), GraphicalRepresentation.TEXT,
				GraphicalRepresentation.IS_VISIBLE, GraphicalRepresentation.TRANSPARENCY, GraphicalRepresentation.ABSOLUTE_TEXT_X,
				GraphicalRepresentation.ABSOLUTE_TEXT_Y);
		refreshGraphicalRepresentation();
	}*/

	/**
	 * Refresh graphical representation
	 */
	/*@Override
	public void refreshGraphicalRepresentation() {
		super.refreshGraphicalRepresentation();
		getGraphicalRepresentation().notifyConnectorChanged();
	}*/

	/*@Override
	public boolean delete() {
		if (getParent() != null) {
			getParent().removeFromChilds(this);
		}
		super.delete();
		deleteObservers();
		return true;
	}*/

	/* @Override
	 public AddSchemaElementAction getEditionAction() 
	 {
	 	return getAddConnectorAction();
	 }
	 
	public AddConnector getAddConnectorAction()
	{
		if (getFlexoConcept() != null && getPatternRole() != null)
			return getFlexoConcept().getAddConnectorAction(getPatternRole());
		return null;
	}*/

	/*@Override
	public String getClassNameKey() {
		return "oe_connector";
	}

	@Override
	public String getFullyQualifiedName() {
		return getParent().getFullyQualifiedName() + "." + getName();
	}

	public DiagramShape getEndShape() {
		return endShape;
	}

	public void setEndShape(DiagramShape endShape) {
		this.endShape = endShape;
		// NPE Protection
		if (endShape != null) {
			endShape.addToIncomingConnectors(this);
		}
	}

	public DiagramShape getStartShape() {
		return startShape;
	}

	public void setStartShape(DiagramShape startShape) {
		this.startShape = startShape;
		startShape.addToOutgoingConnectors(this);
	}

	@Override
	public boolean isContainedIn(DiagramElement<?> o) {
		if (o == this) {
			return true;
		}
		if (getParent() != null && getParent() == o) {
			return true;
		}
		if (getParent() != null) {
			return getParent().isContainedIn(o);
		}
		return false;
	}

	@Override
	public String getDisplayableDescription() {
		return "ConnectorSpecification" + (getFlexoConcept() != null ? " representing " + getFlexoConcept() : "");
	}

	@Override
	public ConnectorRole getPatternRole() {
		return (ConnectorRole) super.getPatternRole();
	}

	private List<DiagramElement<?>> descendants;

	@Override
	public List<DiagramElement<?>> getDescendants() {
		if (descendants == null) {
			descendants = new ArrayList<DiagramElement<?>>();
			appendDescendants(this, descendants);
		}
		return descendants;
	}

	private void appendDescendants(DiagramElement<?> current, List<DiagramElement<?>> descendants) {
		descendants.add(current);
		for (DiagramElement<?> child : current.getChilds()) {
			if (child != current) {
				appendDescendants(child, descendants);
			}
		}
	}*/

	@Override
	public ConnectorRole getPatternRole(VirtualModelInstance vmInstance) {
		return (ConnectorRole) super.getPatternRole(vmInstance);
	}

}
