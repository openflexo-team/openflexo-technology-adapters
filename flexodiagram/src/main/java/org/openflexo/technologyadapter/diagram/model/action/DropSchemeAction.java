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

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.connie.BindingVariable;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation.ShapeBorder;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.action.InvalidParametersException;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceObject;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.binding.DiagramBehaviourBindingModel;
import org.openflexo.technologyadapter.diagram.fml.binding.DropSchemeBindingModel;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddShape;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

/**
 * Tooling for DropScheme in Openflexo<br>
 * This feature is wrapped into a {@link FlexoAction}<br>
 * The focused object is a VirtualModelInstance with a FMLControlledDiagramVirtualModelInstanceNature
 * 
 * @author sylvain
 * 
 */
public class DropSchemeAction extends DiagramFlexoBehaviourAction<DropSchemeAction, DropScheme, VirtualModelInstance> {

	private static final Logger logger = Logger.getLogger(DropSchemeAction.class.getPackage().getName());

	public static FlexoActionType<DropSchemeAction, VirtualModelInstance, VirtualModelInstanceObject> actionType = new FlexoActionType<DropSchemeAction, VirtualModelInstance, VirtualModelInstanceObject>(
			"drop_palette_element", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public DropSchemeAction makeNewAction(VirtualModelInstance focusedObject, Vector<VirtualModelInstanceObject> globalSelection,
				FlexoEditor editor) {
			return new DropSchemeAction(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(VirtualModelInstance object, Vector<VirtualModelInstanceObject> globalSelection) {
			return false;
		}

		@Override
		public boolean isEnabledForSelection(VirtualModelInstance object, Vector<VirtualModelInstanceObject> globalSelection) {
			return object.hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE);
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(actionType, VirtualModelInstance.class);
	}

	private DiagramPaletteElement _paletteElement;
	private DropScheme _dropScheme;
	private DiagramShape _primaryShape;

	private FlexoConceptInstance parentConceptInstance;
	private ShapeRole parentShapeRole;

	private FGEPoint dropLocation;

	DropSchemeAction(VirtualModelInstance focusedObject, Vector<VirtualModelInstanceObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	// private Hashtable<EditionAction,FlexoObject> createdObjects;

	private FlexoConceptInstance flexoConceptInstance;

	@Override
	protected void doAction(Object context) throws NotImplementedException, InvalidParametersException, FlexoException {
		logger.info("Drop palette element");

		// logger.info("project=" + getProject());
		// getFlexoConcept().getViewPoint().getViewpointOntology().loadWhenUnloaded();

		System.out.println("1-isModified=" + getVirtualModelInstance().isModified());

		flexoConceptInstance = getVirtualModelInstance().makeNewFlexoConceptInstance(getFlexoConcept());

		// logger.info("flexoConceptInstance=" + flexoConceptInstance);
		// logger.info("epi project=" + flexoConceptInstance.getProject());
		// logger.info("epi resource data =" + flexoConceptInstance.getResourceData());

		// System.out.println("2-isModified=" + getVirtualModelInstance().isModified());

		executeControlGraph();

		// System.out.println("3-isModified=" + getVirtualModelInstance().isModified());
	}

	public void setParentInformations(FlexoConceptInstance parentConceptInstance, ShapeRole parentShapeRole) {
		this.parentConceptInstance = parentConceptInstance;
		this.parentShapeRole = parentShapeRole;
	}

	public FlexoConceptInstance getParentConceptInstance() {
		return parentConceptInstance;
	}

	public ShapeRole getParentShapeRole() {
		return parentShapeRole;
	}

	public DiagramContainerElement<?> getParent() {
		if (parentConceptInstance == null) {
			return getDiagram();
		} else {
			return parentConceptInstance.getFlexoActor(getParentShapeRole());
		}
	}

	@Override
	public Diagram getDiagram() {
		return getParent().getDiagram();
	}

	public DropScheme getDropScheme() {
		return _dropScheme;
	}

	public void setDropScheme(DropScheme dropScheme) {
		_dropScheme = dropScheme;
	}

	@Override
	public DropScheme getFlexoBehaviour() {
		return getDropScheme();
	}

	public DiagramPaletteElement getPaletteElement() {
		return _paletteElement;
	}

	public void setPaletteElement(DiagramPaletteElement paletteElement) {
		_paletteElement = paletteElement;
	}

	public DiagramShape getPrimaryShape() {
		return _primaryShape;
	}

	@Override
	public FlexoConceptInstance getFlexoConceptInstance() {
		return flexoConceptInstance;
	}

	@Override
	public VirtualModelInstance retrieveVirtualModelInstance() {
		return getFocusedObject();
	}

	@Override
	public <T> void hasPerformedAction(EditionAction anAction, T object) {
		super.hasPerformedAction(anAction, object);
		if (anAction instanceof AddShape) {
			AddShape action = (AddShape) anAction;
			DiagramShape newShape = (DiagramShape) object;
			if (newShape != null) {
				ShapeGraphicalRepresentation gr = newShape.getGraphicalRepresentation();
				// if (action.getPatternRole().getIsPrimaryRepresentationRole()) {
				// Declare shape as new shape only if it is the primary representation property of the EP

				_primaryShape = newShape;
				gr.setX(dropLocation.getX());
				gr.setY(dropLocation.getY());

				// Temporary comment this portion of code if child shapes are declared inside this shape
				if (!action.getAssignedFlexoProperty().containsShapes()
						&& action.getContainer().toString().equals(DiagramBehaviourBindingModel.TOP_LEVEL)) {
					ShapeBorder border = gr.getBorder();
					ShapeBorder newBorder = gr.getFactory().makeShapeBorder(border);
					boolean requireNewBorder = false;
					double deltaX = 0;
					double deltaY = 0;
					if (border.getTop() < 25) {
						requireNewBorder = true;
						deltaY = border.getTop() - 25;
						newBorder.setTop(25);
					}
					if (border.getLeft() < 25) {
						requireNewBorder = true;
						deltaX = border.getLeft() - 25;
						newBorder.setLeft(25);
					}
					if (border.getRight() < 25) {
						requireNewBorder = true;
						newBorder.setRight(25);
					}
					if (border.getBottom() < 25) {
						requireNewBorder = true;
						newBorder.setBottom(25);
					}
					if (requireNewBorder) {
						gr.setBorder(newBorder);
						gr.setX(gr.getX() + deltaX);
						gr.setY(gr.getY() + deltaY);
						if (gr.getIsFloatingLabel()) {
							gr.setAbsoluteTextX(gr.getAbsoluteTextX() - deltaX);
							gr.setAbsoluteTextY(gr.getAbsoluteTextY() - deltaY);
						}
					}
				}
				/*} else if (action.getPatternRole().getParentShapeAsDefinedInAction()) {
					Object graphicalRepresentation = action.getFlexoConcept().getPrimaryRepresentationRole().getGraphicalRepresentation();
					if (graphicalRepresentation instanceof ShapeGraphicalRepresentation) {
						ShapeGraphicalRepresentation primaryGR = (ShapeGraphicalRepresentation) graphicalRepresentation;
						gr.setX(dropLocation.x + gr.getX() - primaryGR.getX());
						gr.setY(dropLocation.y + gr.getY() - primaryGR.getY());
					}
				}*/
			} else {
				logger.warning("Inconsistant data: shape has not been created");
			}

			/*if (action.getExtendParentBoundsToHostThisShape()) {
				((ShapeGraphicalRepresentation) newShape.getGraphicalRepresentation()).extendParentBoundsToHostThisShape();
			}*/

		}
	}

	/*@Override
	protected Object performAction(EditionAction anAction, Hashtable<EditionAction, Object> performedActions) throws FlexoException {
		Object assignedObject = super.performAction(anAction, performedActions);
		if (anAction instanceof AddShape) {
			AddShape action = (AddShape) anAction;
			DiagramShape newShape = (DiagramShape) assignedObject;
			if (newShape != null) {
				ShapeGraphicalRepresentation gr = newShape.getGraphicalRepresentation();
				// if (action.getPatternRole().getIsPrimaryRepresentationRole()) {
				// Declare shape as new shape only if it is the primary representation property of the EP
	
				_primaryShape = newShape;
				gr.setX(dropLocation.getX());
				gr.setY(dropLocation.getY());
	
				// Temporary comment this portion of code if child shapes are declared inside this shape
				if (!action.getFlexoRole().containsShapes()
						&& action.getContainer().toString().equals(DiagramBehaviourBindingModel.TOP_LEVEL)) {
					ShapeBorder border = gr.getBorder();
					ShapeBorder newBorder = gr.getFactory().makeShapeBorder(border);
					boolean requireNewBorder = false;
					double deltaX = 0;
					double deltaY = 0;
					if (border.getTop() < 25) {
						requireNewBorder = true;
						deltaY = border.getTop() - 25;
						newBorder.setTop(25);
					}
					if (border.getLeft() < 25) {
						requireNewBorder = true;
						deltaX = border.getLeft() - 25;
						newBorder.setLeft(25);
					}
					if (border.getRight() < 25) {
						requireNewBorder = true;
						newBorder.setRight(25);
					}
					if (border.getBottom() < 25) {
						requireNewBorder = true;
						newBorder.setBottom(25);
					}
					if (requireNewBorder) {
						gr.setBorder(newBorder);
						gr.setX(gr.getX() + deltaX);
						gr.setY(gr.getY() + deltaY);
						if (gr.getIsFloatingLabel()) {
							gr.setAbsoluteTextX(gr.getAbsoluteTextX() - deltaX);
							gr.setAbsoluteTextY(gr.getAbsoluteTextY() - deltaY);
						}
					}
			} else {
				logger.warning("Inconsistant data: shape has not been created");
			}
	
		}
		return assignedObject;
	}*/

	@Override
	public Object getValue(BindingVariable variable) {
		if (variable.getVariableName().equals(DropSchemeBindingModel.TARGET) && _dropScheme.getTargetFlexoConcept() != null) {
			return parentConceptInstance;
		}
		return super.getValue(variable);
	}

	public GraphicalRepresentation getOverridingGraphicalRepresentation(GraphicalElementRole patternRole) {
		/*if (getPaletteElement() != null) {
			if (getPaletteElement().getOverridingGraphicalRepresentation(patternRole) != null) {
				return getPaletteElement().getOverridingGraphicalRepresentation(patternRole);
			}
		}*/

		// return overridenGraphicalRepresentations.get(patternRole);
		// TODO temporary desactivate overriden GR
		return null;
	}

	public FGEPoint getDropLocation() {
		return dropLocation;
	}

	public void setDropLocation(FGEPoint dropLocation) {
		this.dropLocation = dropLocation;
	}

}
