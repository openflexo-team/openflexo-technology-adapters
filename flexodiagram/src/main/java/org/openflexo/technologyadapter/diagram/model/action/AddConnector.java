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

package org.openflexo.technologyadapter.diagram.model.action;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramElementImpl;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

public class AddConnector extends FlexoAction<AddConnector, DiagramShape, DiagramElement<?>> {

	private static final Logger logger = Logger.getLogger(AddConnector.class.getPackage().getName());

	public static FlexoActionType<AddConnector, DiagramShape, DiagramElement<?>> actionType = new FlexoActionType<AddConnector, DiagramShape, DiagramElement<?>>(
			"add_connector", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public AddConnector makeNewAction(DiagramShape focusedObject, Vector<DiagramElement<?>> globalSelection, FlexoEditor editor) {
			return new AddConnector(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramShape shape, Vector<DiagramElement<?>> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(DiagramShape shape, Vector<DiagramElement<?>> globalSelection) {
			return shape != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(AddConnector.actionType, DiagramShape.class);
	}

	private DiagramShape fromShape;
	private DiagramShape toShape;
	private String annotation;
	private DiagramConnector newConnector;
	private String newConnectorName;

	private ConnectorGraphicalRepresentation graphicalRepresentation;

	private boolean automaticallyCreateConnector = false;

	AddConnector(DiagramShape focusedObject, Vector<DiagramElement<?>> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) {
		if (logger.isLoggable(Level.INFO)) {
			logger.info("Add connector");
		}
		if (getFocusedObject() != null && getFromShape() != null && getToShape() != null) {
			DiagramContainerElement<?> parent = DiagramElementImpl.getFirstCommonAncestor(getFromShape(), getToShape());
			if (logger.isLoggable(Level.INFO)) {
				logger.info("Parent=" + parent);
			}
			if (parent == null) {
				logger.warning("No common ancestors for " + getFromShape() + " and " + getToShape());
				throw new IllegalArgumentException("No common ancestor");
			}
			newConnector = getFocusedObject().getDiagram().getDiagramFactory()
					.makeNewConnector(getNewConnectorName(), getFromShape(), getToShape(), parent);
			newConnector.setDescription(annotation);
			parent.addToConnectors(newConnector);
		} else {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Focused property is null !");
			}
		}
	}

	public DiagramShape getToShape() {
		return toShape;
	}

	public void setToShape(DiagramShape aShape) {
		toShape = aShape;
	}

	public DiagramShape getFromShape() {
		if (fromShape == null) {
			return getFocusedObject();
		}
		return fromShape;
	}

	public void setFromShape(DiagramShape fromShape) {
		fromShape = fromShape;
	}

	public DiagramConnector getConnector() {
		return newConnector;
	}

	public DiagramConnector getNewConnector() {
		return getConnector();
	}

	public boolean getAutomaticallyCreateConnector() {
		return automaticallyCreateConnector;
	}

	public void setAutomaticallyCreateConnector(boolean automaticallyCreateConnector) {
		this.automaticallyCreateConnector = automaticallyCreateConnector;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public String getNewConnectorName() {
		return newConnectorName;
	}

	public void setNewConnectorName(String newConnectorName) {
		this.newConnectorName = newConnectorName;
	}

	public ConnectorGraphicalRepresentation getGraphicalRepresentation() {
		return graphicalRepresentation;
	}

	public void setGraphicalRepresentation(ConnectorGraphicalRepresentation graphicalRepresentation) {
		this.graphicalRepresentation = graphicalRepresentation;
	}

}
