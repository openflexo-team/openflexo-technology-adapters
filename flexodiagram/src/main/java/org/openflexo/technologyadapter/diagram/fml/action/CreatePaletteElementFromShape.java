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

import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import org.openflexo.fge.ScreenshotBuilder.ScreenshotImage;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.StringUtils;

/**
 * Action allowing to create new palette element from a shape selection in a diagram
 * 
 * @author sylvain
 *
 */
public class CreatePaletteElementFromShape
		extends AbstractCreatePaletteElement<CreatePaletteElementFromShape, DiagramShape, DiagramElement<?>> {

	private static final Logger logger = Logger.getLogger(CreatePaletteElementFromShape.class.getPackage().getName());

	public static FlexoActionFactory<CreatePaletteElementFromShape, DiagramShape, DiagramElement<?>> actionType = new FlexoActionFactory<CreatePaletteElementFromShape, DiagramShape, DiagramElement<?>>(
			"put_to_palette", FlexoActionFactory.defaultGroup, FlexoActionFactory.NORMAL_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreatePaletteElementFromShape makeNewAction(DiagramShape focusedObject, Vector<DiagramElement<?>> globalSelection,
				FlexoEditor editor) {
			return new CreatePaletteElementFromShape(focusedObject, globalSelection, editor);
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

	static {
		FlexoObjectImpl.addActionForClass(CreatePaletteElementFromShape.actionType, DiagramShape.class);
	}

	protected CreatePaletteElementFromShape(DiagramShape focusedObject, Vector<DiagramElement<?>> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
		diagramElementEntries = new Vector<>();
		updateDiagramElementsEntries();
	}

	@Override
	protected void updateDiagramElementsEntries() {
		diagramElementEntries.clear();
		int shapeIndex = 1;
		int connectorIndex = 1;
		List<? extends DiagramElement<?>> elements = (getFocusedObject() instanceof DiagramContainerElement
				? ((DiagramContainerElement<?>) getFocusedObject()).getDescendants() : Collections.singletonList(getFocusedObject()));

		for (DiagramElement<?> o : elements) {
			if (o instanceof DiagramShape) {
				DiagramShape shape = (DiagramShape) o;
				String shapeRoleName = StringUtils.isEmpty(shape.getName()) ? "shape" + (shapeIndex > 1 ? shapeIndex : "")
						: shape.getName();
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

	@Override
	public ScreenshotImage<DiagramShape> makeScreenshot() {
		return getFocusedObject().getScreenshotImage();
	}

	@Override
	public ShapeGraphicalRepresentation makePaletteElementGraphicalRepresentation() {
		return getFocusedObject().getGraphicalRepresentation();
	}

	public DiagramElementEntry getEntry(DiagramElement<?> o) {
		for (GraphicalElementEntry e : diagramElementEntries) {
			if (((DiagramElementEntry) e).graphicalObject == o) {
				return (DiagramElementEntry) e;
			}
		}
		return null;
	}

	public class DiagramElementEntry extends GraphicalElementEntry {
		private DiagramElement<?> graphicalObject;
		private boolean selectThis;

		public DiagramElementEntry(DiagramElement<?> graphicalObject, String elementName) {
			super(elementName);
			this.graphicalObject = graphicalObject;
		}

		@Override
		public boolean isMainEntry() {
			return graphicalObject == getFocusedObject();
		}

		@Override
		public boolean getSelectThis() {
			if (isMainEntry()) {
				return true;
			}
			return selectThis;
		}

		@Override
		public void setSelectThis(boolean aFlag) {
			selectThis = aFlag;
			if (getFlexoRole() == null && graphicalObject instanceof DiagramShape && getFlexoConcept() != null) {
				GraphicalElementRole<?, ?> parentEntryPatternRole = getParentEntry().getFlexoRole();
				for (ShapeRole r : getFlexoConcept().getDeclaredProperties(ShapeRole.class)) {
					if (r.getParentShapeRole() == parentEntryPatternRole && getFlexoRole() == null) {
						setFlexoRole(r);
					}
				}
			}
		}

		@Override
		public DiagramElementEntry getParentEntry() {
			return getEntry(graphicalObject.getParent());
		}

		@Override
		public List<? extends GraphicalElementRole<?, ?>> getAvailableFlexoRoles() {
			if (getFlexoConcept() != null) {
				if (graphicalObject instanceof DiagramShape) {
					return getFlexoConcept().getDeclaredProperties(ShapeRole.class);
				}
				else if (graphicalObject instanceof DiagramConnector) {
					return getFlexoConcept().getDeclaredProperties(ConnectorRole.class);
				}
			}
			return null;
		}
	}

}
