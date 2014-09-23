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
package org.openflexo.technologyadapter.diagram.model.action;

import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.antar.binding.BindingVariable;
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
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.view.VirtualModelInstanceObject;
import org.openflexo.foundation.viewpoint.editionaction.EditionAction;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
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
public class DropSchemeAction extends DiagramEditionSchemeAction<DropSchemeAction, DropScheme, VirtualModelInstance> {

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

	private DiagramContainerElement<?> _parent;
	private DiagramPaletteElement _paletteElement;
	private DropScheme _dropScheme;
	private DiagramShape _primaryShape;

	private FGEPoint dropLocation;

	DropSchemeAction(VirtualModelInstance focusedObject, Vector<VirtualModelInstanceObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	// private Hashtable<EditionAction,FlexoObject> createdObjects;

	private FlexoConceptInstance flexoConceptInstance;

	@Override
	protected void doAction(Object context) throws NotImplementedException, InvalidParametersException, FlexoException {
		logger.info("Drop palette element");

		logger.info("project=" + getProject());
		// getFlexoConcept().getViewPoint().getViewpointOntology().loadWhenUnloaded();

		System.out.println("1-isModified=" + getVirtualModelInstance().isModified());

		flexoConceptInstance = getVirtualModelInstance().makeNewFlexoConceptInstance(getFlexoConcept());

		logger.info("flexoConceptInstance=" + flexoConceptInstance);
		logger.info("epi project=" + flexoConceptInstance.getProject());
		logger.info("epi resource data =" + flexoConceptInstance.getResourceData());

		System.out.println("2-isModified=" + getVirtualModelInstance().isModified());

		applyEditionActions();

		System.out.println("3-isModified=" + getVirtualModelInstance().isModified());
	}

	public DiagramContainerElement<?> getParent() {
		if (_parent == null) {
			/*if (getFocusedObject() instanceof DiagramShape) {
				_parent = (DiagramShape) getFocusedObject();
			} else if (getFocusedObject() instanceof DiagramRootPane) {
				_parent = (DiagramRootPane) getFocusedObject();
			}*/
			_parent = getDiagram();
		}
		return _parent;
	}

	public void setParent(DiagramContainerElement<?> parent) {
		_parent = parent;
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
	public DropScheme getEditionScheme() {
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
	protected Object performAction(EditionAction anAction, Hashtable<EditionAction, Object> performedActions) throws FlexoException {
		Object assignedObject = super.performAction(anAction, performedActions);
		if (anAction instanceof AddShape) {
			AddShape action = (AddShape) anAction;
			DiagramShape newShape = (DiagramShape) assignedObject;
			if (newShape != null) {
				ShapeGraphicalRepresentation gr = newShape.getGraphicalRepresentation();
				// if (action.getPatternRole().getIsPrimaryRepresentationRole()) {
				// Declare shape as new shape only if it is the primary representation role of the EP

				_primaryShape = newShape;
				gr.setX(dropLocation.getX());
				gr.setY(dropLocation.getY());

				// Temporary comment this portion of code if child shapes are declared inside this shape
				if (!action.getFlexoRole().containsShapes() && action.getContainer().toString().equals(DiagramBehaviourBindingModel.TOP_LEVEL)) {
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
		return assignedObject;
	}

	@Override
	public Object getValue(BindingVariable variable) {
		if (variable.getVariableName().equals(DropSchemeBindingModel.TARGET) && _dropScheme.getTargetFlexoConcept() != null) {
			/*if (getParent() instanceof DiagramShape) {
				return ((DiagramShape) getParent()).getFlexoConceptInstance();
			}*/
			// TODO
			logger.warning("Please implement getValue() for target");
			return null;
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
