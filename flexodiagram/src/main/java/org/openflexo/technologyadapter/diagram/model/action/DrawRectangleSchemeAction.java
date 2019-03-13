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

package org.openflexo.technologyadapter.diagram.model.action;

import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.connie.BindingVariable;
import org.openflexo.diana.ShapeGraphicalRepresentation;
import org.openflexo.diana.geom.DianaGeometricObject.Filling;
import org.openflexo.diana.geom.DianaPoint;
import org.openflexo.diana.geom.DianaRectangle;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceObject;
import org.openflexo.technologyadapter.diagram.fml.DrawRectangleScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.technologyadapter.diagram.fml.binding.DrawRectangleBindingModel;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddShape;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

/**
 * Tooling for {@link DrawRectangleScheme} in Openflexo<br>
 * This feature is wrapped into a {@link FlexoAction}<br>
 * The focused object is a FMLRTVirtualModelInstance with a FMLControlledDiagramVirtualModelInstanceNature
 * 
 * @author sylvain
 * 
 */
public class DrawRectangleSchemeAction
		extends DiagramFlexoBehaviourAction<DrawRectangleSchemeAction, DrawRectangleScheme, FMLRTVirtualModelInstance> {

	private static final Logger logger = Logger.getLogger(DrawRectangleSchemeAction.class.getPackage().getName());

	private DianaPoint fromLocation;
	private DianaPoint toLocation;
	private List<FlexoConceptInstance> selection;
	// The eventual concept instance in which we draw rectangle
	private FlexoConceptInstance targetConceptInstance;
	private DiagramShape primaryShape;

	/**
	 * Constructor to be used for creating a new action without factory
	 * 
	 * @param dropScheme
	 * @param focusedObject
	 * @param globalSelection
	 * @param editor
	 */
	public DrawRectangleSchemeAction(DrawRectangleScheme drawRectangleScheme, FMLRTVirtualModelInstance focusedObject,
			Vector<VirtualModelInstanceObject> globalSelection, FlexoEditor editor) {
		super(drawRectangleScheme, focusedObject, globalSelection, editor);
	}

	/**
	 * Constructor to be used for creating a new action as an action embedded in another one
	 * 
	 * @param dropScheme
	 * @param focusedObject
	 * @param globalSelection
	 * @param ownerAction
	 */
	public DrawRectangleSchemeAction(DrawRectangleScheme drawRectangleScheme, FMLRTVirtualModelInstance focusedObject,
			Vector<VirtualModelInstanceObject> globalSelection, FlexoAction<?, ?, ?> ownerAction) {
		super(drawRectangleScheme, focusedObject, globalSelection, ownerAction);
	}

	@Override
	public boolean isValid() {
		if (!super.isValid()) {
			return false;
		}

		if (getFromLocation() == null) {
			return false;
		}
		if (getToLocation() == null) {
			return false;
		}

		return true;
	}

	/*public DiagramContainerElement<?> getParent() {
		if (getTargetConceptInstance() == null) {
			return getDiagram();
		}
		else {
			return parentConceptInstance.getFlexoActor(getParentShapeRole());
		}
	}*/

	@Override
	public Diagram getDiagram() {
		return FMLControlledDiagramVirtualModelInstanceNature.getDiagram(getFocusedObject());
	}

	@Override
	public <T> void hasPerformedAction(EditionAction anAction, T object) {
		super.hasPerformedAction(anAction, object);
		if (anAction instanceof AddShape) {
			// AddShape action = (AddShape) anAction;
			DiagramShape newShape = (DiagramShape) object;
			if (newShape != null) {
				ShapeGraphicalRepresentation gr = newShape.getGraphicalRepresentation();
				System.out.println("Tiens, je choppe mon ADD");
				primaryShape = newShape;
				DianaRectangle r = new DianaRectangle(fromLocation, toLocation, Filling.FILLED);
				gr.setX(r.getX());
				gr.setY(r.getY());
				gr.setWidth(r.getWidth());
				gr.setHeight(r.getHeight());

			}
			else {
				logger.warning("Inconsistant data: shape has not been created");
			}

		}
	}

	@Override
	public Object getValue(BindingVariable variable) {
		if (variable.getVariableName().equals(DrawRectangleBindingModel.TARGET)) {
			return getTargetConceptInstance();
		}
		if (variable.getVariableName().equals(DrawRectangleBindingModel.SELECTION) && getFlexoBehaviour().getSelectObjects()) {
			return getSelection();
		}
		if (variable.getVariableName().equals(DrawRectangleBindingModel.FROM)) {
			return getFromLocation();
		}
		if (variable.getVariableName().equals(DrawRectangleBindingModel.TO)) {
			return getToLocation();
		}
		return super.getValue(variable);
	}

	public DianaPoint getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(DianaPoint fromLocation) {
		if ((fromLocation == null && this.fromLocation != null) || (fromLocation != null && !fromLocation.equals(this.fromLocation))) {
			DianaPoint oldValue = this.fromLocation;
			this.fromLocation = fromLocation;
			getPropertyChangeSupport().firePropertyChange("fromLocation", oldValue, fromLocation);
		}
	}

	public DianaPoint getToLocation() {
		return toLocation;
	}

	public void setToLocation(DianaPoint toLocation) {
		if ((toLocation == null && this.toLocation != null) || (toLocation != null && !toLocation.equals(this.toLocation))) {
			DianaPoint oldValue = this.toLocation;
			this.toLocation = toLocation;
			getPropertyChangeSupport().firePropertyChange("toLocation", oldValue, toLocation);
		}
	}

	public List<FlexoConceptInstance> getSelection() {
		return selection;
	}

	public void setSelection(List<FlexoConceptInstance> selection) {
		if ((selection == null && this.selection != null) || (selection != null && !selection.equals(this.selection))) {
			List<FlexoConceptInstance> oldValue = this.selection;
			this.selection = selection;
			getPropertyChangeSupport().firePropertyChange("selection", oldValue, selection);
		}
	}

	public FlexoConceptInstance getTargetConceptInstance() {
		return targetConceptInstance;
	}

	public void setTargetConceptInstance(FlexoConceptInstance targetConceptInstance) {
		if ((targetConceptInstance == null && this.targetConceptInstance != null)
				|| (targetConceptInstance != null && !targetConceptInstance.equals(this.targetConceptInstance))) {
			FlexoConceptInstance oldValue = this.targetConceptInstance;
			this.targetConceptInstance = targetConceptInstance;
			getPropertyChangeSupport().firePropertyChange("targetConceptInstance", oldValue, targetConceptInstance);
		}
	}

	public DiagramShape getPrimaryShape() {
		return primaryShape;
	}
}
