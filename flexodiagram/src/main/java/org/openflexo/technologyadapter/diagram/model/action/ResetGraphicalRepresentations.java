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

import java.security.InvalidParameterException;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;

/**
 * This action reset all graphical representations found in view to conform to those described in FlexoConcept
 * 
 * @author sylvain
 * 
 */
public class ResetGraphicalRepresentations extends FlexoAction<ResetGraphicalRepresentations, DiagramElement<?>, DiagramElement<?>> {

	private static final Logger logger = Logger.getLogger(ResetGraphicalRepresentations.class.getPackage().getName());

	public static FlexoActionFactory<ResetGraphicalRepresentations, DiagramElement<?>, DiagramElement<?>> actionType = new FlexoActionFactory<ResetGraphicalRepresentations, DiagramElement<?>, DiagramElement<?>>(
			"reset_graphical_representations", FlexoActionFactory.editGroup, FlexoActionFactory.NORMAL_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public ResetGraphicalRepresentations makeNewAction(DiagramElement<?> focusedObject, Vector<DiagramElement<?>> globalSelection,
				FlexoEditor editor) {
			return new ResetGraphicalRepresentations(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramElement<?> element, Vector<DiagramElement<?>> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(DiagramElement<?> element, Vector<DiagramElement<?>> globalSelection) {
			return element instanceof Diagram;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(ResetGraphicalRepresentations.actionType, Diagram.class);
	}

	private ResetGraphicalRepresentations(DiagramElement<?> focusedObject, Vector<DiagramElement<?>> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getServiceManager() != null) {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class).getLocales();
		}
		return super.getLocales();
	}

	@Override
	protected void doAction(Object context) throws NotImplementedException, InvalidParameterException {
		logger.info("Reset graphical representations for view");

		Diagram diagram = null;

		if (getFocusedObject() instanceof Diagram) {
			diagram = (Diagram) getFocusedObject();
		}

		if (diagram != null) {
			processElement(diagram);
		}

	}

	private void processElement(DiagramElement<?> o) {
		if (o instanceof DiagramElement) {
			// TODO
			logger.warning("Please implement resetGraphicalRepresentation() for DiagramElement<?>");
			// ((DiagramElement) o).resetGraphicalRepresentation();
		}
		/*if (o instanceof DiagramShape) {
			DiagramShape shape = (DiagramShape) o;
			if (shape.getPatternRole() != null) {
				((ShapeGraphicalRepresentation) shape.getGraphicalRepresentation()).setsWith((ShapeGraphicalRepresentation) shape
						.getPatternRole().getGraphicalRepresentation(), GraphicalRepresentation.Parameters.text,
						GraphicalRepresentation.Parameters.isVisible, GraphicalRepresentation.Parameters.absoluteTextX,
						GraphicalRepresentation.Parameters.absoluteTextY, ShapeGraphicalRepresentation.Parameters.x,
						ShapeGraphicalRepresentation.Parameters.y, ShapeGraphicalRepresentation.Parameters.width,
						ShapeGraphicalRepresentation.Parameters.height, ShapeGraphicalRepresentation.Parameters.relativeTextX,
						ShapeGraphicalRepresentation.Parameters.relativeTextY);
			}
		} else if (o instanceof DiagramConnector) {
			DiagramConnector connector = (DiagramConnector) o;
			if (connector.getPatternRole() != null) {
				((ConnectorGraphicalRepresentation) connector.getGraphicalRepresentation()).setsWith(
						(ConnectorGraphicalRepresentation) connector.getPatternRole().getGraphicalRepresentation(),
						GraphicalRepresentation.Parameters.text, GraphicalRepresentation.Parameters.isVisible,
						GraphicalRepresentation.Parameters.absoluteTextX, GraphicalRepresentation.Parameters.absoluteTextY);
			}
		}*/
		if (o instanceof DiagramContainerElement) {
			for (DiagramElement<?> child : ((DiagramContainerElement<?>) o).getShapes()) {
				processElement(child);
			}
			for (DiagramElement<?> child : ((DiagramContainerElement<?>) o).getConnectors()) {
				processElement(child);
			}
		}
	}

}
