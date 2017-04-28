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

import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import org.openflexo.fge.ScreenshotBuilder.ScreenshotImage;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

public class CreatePaletteElementFromFlexoConcept
		extends AbstractCreatePaletteElement<CreatePaletteElementFromFlexoConcept, FlexoConcept, FMLObject> {

	private static final Logger logger = Logger.getLogger(CreatePaletteElementFromFlexoConcept.class.getPackage().getName());

	public static FlexoActionType<CreatePaletteElementFromFlexoConcept, FlexoConcept, FMLObject> actionType = new FlexoActionType<CreatePaletteElementFromFlexoConcept, FlexoConcept, FMLObject>(
			"put_to_palette", FlexoActionType.defaultGroup, FlexoActionType.NORMAL_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreatePaletteElementFromFlexoConcept makeNewAction(FlexoConcept focusedObject, Vector<FMLObject> globalSelection,
				FlexoEditor editor) {
			return new CreatePaletteElementFromFlexoConcept(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FlexoConcept shape, Vector<FMLObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FlexoConcept shape, Vector<FMLObject> globalSelection) {
			return shape != null /* && shape.getVirtualModel().getPalettes().size() > 0*/;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreatePaletteElementFromFlexoConcept.actionType, FlexoConcept.class);
	}

	protected CreatePaletteElementFromFlexoConcept(FlexoConcept focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
		diagramElementEntries = new Vector<GraphicalElementEntry>();
		updateDiagramElementsEntries();
	}

	@Override
	public FlexoConcept getFlexoConcept() {
		return getFocusedObject();
	}

	@Override
	public void setFlexoConcept(FlexoConcept flexoConcept) {
		// Not applicable
	}

	@Override
	public VirtualModel getVirtualModel() {
		if (getFlexoConcept() != null) {
			return (VirtualModel) getFlexoConcept().getOwningVirtualModel();
		}
		return null;
	}

	@Override
	public void setVirtualModel(VirtualModel virtualModel) {
		// Not applicable
	}

	@Override
	protected void updateDiagramElementsEntries() {
		super.updateDiagramElementsEntries();

		for (GraphicalElementRole<?, ?> elementRole : getFocusedObject().getAccessibleProperties(GraphicalElementRole.class)) {
			diagramElementEntries.add(new RoleEntry(elementRole));
		}
	}

	@Override
	public ScreenshotImage<DiagramShape> makeScreenshot() {
		// return getFocusedObject().getScreenshotImage();
		return null;
	}

	@Override
	public ShapeGraphicalRepresentation makePaletteElementGraphicalRepresentation() {
		// TODO improve this
		if (getFlexoConcept() != null && getFlexoConcept().getAccessibleProperties(ShapeRole.class).size() > 0) {
			return getFlexoConcept().getAccessibleProperties(ShapeRole.class).get(0).getGraphicalRepresentation();
		}
		return null;
	}

	public RoleEntry getEntry(GraphicalElementRole<?, ?> role) {
		for (GraphicalElementEntry e : diagramElementEntries) {
			if (((RoleEntry) e).getFlexoRole() == role) {
				return (RoleEntry) e;
			}
		}
		return null;
	}

	public class RoleEntry extends GraphicalElementEntry {
		private boolean selectThis = true;

		public RoleEntry(GraphicalElementRole<?, ?> role) {
			super(role.getRoleName());
			setFlexoRole(role);
		}

		@Override
		public boolean isMainEntry() {
			return true;
		}

		@Override
		public boolean getSelectThis() {
			return selectThis;
		}

		@Override
		public void setSelectThis(boolean aFlag) {
			selectThis = aFlag;
			/*if (getFlexoRole() == null && graphicalObject instanceof DiagramShape && getFlexoConcept() != null) {
				GraphicalElementRole<?, ?> parentEntryPatternRole = getParentEntry().getFlexoRole();
				for (ShapeRole r : getFlexoConcept().getDeclaredProperties(ShapeRole.class)) {
					if (r.getParentShapeRole() == parentEntryPatternRole && getFlexoRole() == null) {
						setFlexoRole(r);
					}
				}
			}*/
		}

		@Override
		public RoleEntry getParentEntry() {
			if (getFlexoRole() instanceof ShapeRole) {
				return getEntry(((ShapeRole) getFlexoRole()).getParentShapeRole());
			}
			return null;
		}

		@Override
		public List<? extends GraphicalElementRole<?, ?>> getAvailableFlexoRoles() {
			if (getFlexoConcept() != null) {
				return (List) getFlexoConcept().getAccessibleProperties(GraphicalElementRole.class);
			}
			return null;
		}
	}

}
