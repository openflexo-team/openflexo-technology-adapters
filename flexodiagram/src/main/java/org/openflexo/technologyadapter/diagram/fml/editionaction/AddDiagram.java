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
package org.openflexo.technologyadapter.diagram.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.antar.binding.DataBinding;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.view.View;
import org.openflexo.foundation.view.action.FlexoBehaviourAction;
import org.openflexo.foundation.viewpoint.FMLRepresentationContext;
import org.openflexo.foundation.viewpoint.FMLRepresentationContext.FMLRepresentationOutput;
import org.openflexo.foundation.viewpoint.FlexoRole;
import org.openflexo.foundation.viewpoint.annotations.FIBPanel;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.fml.DiagramRole;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.toolbox.StringUtils;

@FIBPanel("Fib/AddDiagramPanel.fib")
@ModelEntity
@ImplementationClass(AddDiagram.AddDiagramImpl.class)
@XMLElement
public interface AddDiagram extends DiagramAction<Diagram> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String DIAGRAM_NAME_KEY = "diagramName";

	@Getter(value = DIAGRAM_NAME_KEY)
	@XMLAttribute
	public DataBinding<String> getDiagramName();

	@Setter(DIAGRAM_NAME_KEY)
	public void setDiagramName(DataBinding<String> diagramName);

	public static abstract class AddDiagramImpl extends DiagramActionImpl<Diagram> implements AddDiagram {

		private static final Logger logger = Logger.getLogger(AddDiagram.class.getPackage().getName());

		public AddDiagramImpl() {
			super();
		}

		@Override
		public String getFMLRepresentation(FMLRepresentationContext context) {
			FMLRepresentationOutput out = new FMLRepresentationOutput(context);
			if (getAssignation().isSet()) {
				out.append(getAssignation().toString() + " = (", context);
			}
			out.append(getClass().getSimpleName() + " conformTo " + getDiagramSpecification().getURI() + " from "
					+ getModelSlot().getName() + " {" + StringUtils.LINE_SEPARATOR, context);
			out.append("}", context);
			if (getAssignation().isSet()) {
				out.append(")", context);
			}
			return out.toString();
		}

		@Override
		public DiagramRole getFlexoRole() {
			FlexoRole superFlexoRole = super.getFlexoRole();
			if (superFlexoRole instanceof DiagramRole) {
				return (DiagramRole) superFlexoRole;
			} else if (superFlexoRole != null) {
				// logger.warning("Unexpected pattern role of type " + superPatternRole.getClass().getSimpleName());
				return null;
			}
			return null;
		}

		public String getDiagramName(FlexoBehaviourAction action) {
			try {
				return getDiagramName().getBindingValue(action);
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}

		private DataBinding<String> diagramName;

		@Override
		public DataBinding<String> getDiagramName() {
			if (diagramName == null) {
				diagramName = new DataBinding<String>(this, String.class, DataBinding.BindingDefinitionType.GET);
				diagramName.setBindingName("diagramName");
			}
			return diagramName;
		}

		@Override
		public void setDiagramName(DataBinding<String> diagramName) {
			if (diagramName != null) {
				diagramName.setOwner(this);
				diagramName.setDeclaredType(String.class);
				diagramName.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				diagramName.setBindingName("diagramName");
			}
			this.diagramName = diagramName;
		}

		public DiagramSpecification getDiagramSpecification() {
			if (getFlexoRole() instanceof DiagramRole) {
				return getFlexoRole().getDiagramSpecification();
			}
			return null;
		}

		public void setDiagramSpecification(DiagramSpecification diagramSpecification) {
			if (getFlexoRole() instanceof DiagramRole) {
				getFlexoRole().setDiagramSpecification(diagramSpecification);
			}
		}

		@Override
		public Type getAssignableType() {
			return View.class;
		}

		@Override
		public Diagram performAction(FlexoBehaviourAction action) {
			// TODO: reimplement this
			logger.warning("AddDiagram not implemented yet");
			/*Diagram initialDiagram = (Diagram) action.retrieveVirtualModelInstance();
			ViewResource viewResource = initialDiagram.getView().getResource();
			org.openflexo.technologyadapter.diagram.model.action.CreateDiagram addDiagramAction = org.openflexo.technologyadapter.diagram.model.action.CreateDiagram.actionType
					.makeNewEmbeddedAction(initialDiagram.getView(), null, action);
			addDiagramAction.setNewVirtualModelInstanceName(getDiagramName(action));
			addDiagramAction.setDiagramSpecification(getPatternRole().getDiagramSpecification());
			addDiagramAction.skipChoosePopup = true;
			addDiagramAction.doAction();*/
			// if (addDiagramAction.hasActionExecutionSucceeded() && addDiagramAction.getNewDiagram() != null) {
			// Diagram newDiagram = addDiagramAction.getNewDiagram();
			/*ShapeRole shapePatternRole = action.getShapePatternRole();
			if (shapePatternRole == null) {
				logger.warning("Sorry, shape pattern role is undefined");
				return newShema;
			}
			// logger.info("ShapeSpecification pattern role: " + shapePatternRole);
			FlexoConceptInstance newFlexoConceptInstance = getProject().makeNewFlexoConceptInstance(getFlexoConcept());
			DiagramShape newShape = new DiagramShape(newShema);
			if (getFlexoConceptInstance().getPatternActor(shapePatternRole) instanceof DiagramShape) {
				DiagramShape primaryShape = (DiagramShape) getFlexoConceptInstance().getPatternActor(shapePatternRole);
				newShape.setGraphicalRepresentation(primaryShape.getGraphicalRepresentation());
			} else if (shapePatternRole.getGraphicalRepresentation() != null) {
				newShape.setGraphicalRepresentation(shapePatternRole.getGraphicalRepresentation());
			}
			// Register reference
			newShape.registerFlexoConceptReference(newFlexoConceptInstance, shapePatternRole);
			newShema.addToChilds(newShape);
			newFlexoConceptInstance.setObjectForPatternRole(newShape, shapePatternRole);
			// Duplicates all other pattern roles
			for (FlexoRole role : getFlexoConcept().getPatternRoles()) {
				if (role != action.getPatternRole() && role != shapePatternRole) {
					FlexoModelObject patternActor = getFlexoConceptInstance().getPatternActor(role);
					logger.info("Duplicate pattern actor for role " + role + " value=" + patternActor);
					newFlexoConceptInstance.setObjectForPatternRole(patternActor, role);
					patternActor.registerFlexoConceptReference(newFlexoConceptInstance, role);
				}
			}*/

			// return newDiagram;
			// }
			return null;
		}

	}
}
