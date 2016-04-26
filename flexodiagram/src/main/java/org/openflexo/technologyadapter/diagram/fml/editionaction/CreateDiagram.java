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

package org.openflexo.technologyadapter.diagram.fml.editionaction;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.FMLRepresentationContext;
import org.openflexo.foundation.fml.FMLRepresentationContext.FMLRepresentationOutput;
import org.openflexo.foundation.fml.FlexoProperty;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.DiagramModelSlot;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.fml.DiagramRole;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResource;
import org.openflexo.toolbox.JavaUtils;
import org.openflexo.toolbox.StringUtils;

@ModelEntity
@ImplementationClass(CreateDiagram.CreateDiagramImpl.class)
@XMLElement
@FML("AddDiagram")
public interface CreateDiagram extends DiagramAction<DiagramModelSlot, Diagram> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String DIAGRAM_NAME_KEY = "diagramName";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String DIAGRAM_URI_KEY = "diagramURI";
	@PropertyIdentifier(type = String.class)
	public static final String DIAGRAM_SPECIFICATION_URI_KEY = "diagramSpecificationURI";
	@PropertyIdentifier(type = FlexoResourceCenter.class)
	public static final String RESOURCE_CENTER_KEY = "resourceCenter";
	@PropertyIdentifier(type = String.class)
	public static final String RELATIVE_PATH_KEY = "relativePath";

	@Getter(value = DIAGRAM_NAME_KEY)
	@XMLAttribute
	public DataBinding<String> getDiagramName();

	@Setter(DIAGRAM_NAME_KEY)
	public void setDiagramName(DataBinding<String> diagramName);

	@Getter(value = DIAGRAM_URI_KEY)
	@XMLAttribute
	public DataBinding<String> getDiagramURI();

	@Setter(DIAGRAM_URI_KEY)
	public void setDiagramURI(DataBinding<String> diagramURI);

	@Getter(value = DIAGRAM_SPECIFICATION_URI_KEY)
	@XMLAttribute
	public String getDiagramSpecificationURI();

	@Setter(DIAGRAM_SPECIFICATION_URI_KEY)
	public void setDiagramSpecificationURI(String diagramSpecificationURI);

	@Getter(value = RESOURCE_CENTER_KEY)
	@XMLAttribute
	public DataBinding<FlexoResourceCenter<?>> getResourceCenter();

	@Setter(RESOURCE_CENTER_KEY)
	public void setResourceCenter(DataBinding<FlexoResourceCenter<?>> resourceCenter);

	@Getter(value = RELATIVE_PATH_KEY)
	@XMLAttribute
	public String getRelativePath();

	@Setter(RELATIVE_PATH_KEY)
	public void setRelativePath(String relativePath);

	public DiagramSpecification getDiagramSpecification();

	public void setDiagramSpecification(DiagramSpecification diagramSpecification);

	public DiagramSpecificationResource getDiagramSpecificationResource();

	public void setDiagramSpecificationResource(DiagramSpecificationResource diagramSpecificationResource);

	public static abstract class CreateDiagramImpl extends TechnologySpecificActionImpl<DiagramModelSlot, Diagram> implements CreateDiagram {

		private static final Logger logger = Logger.getLogger(CreateDiagram.class.getPackage().getName());

		private DiagramSpecificationResource diagramSpecificationResource;
		private String diagramSpecificationURI;

		@Override
		public DiagramTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (DiagramTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

		@Override
		public String getFMLRepresentation(FMLRepresentationContext context) {
			FMLRepresentationOutput out = new FMLRepresentationOutput(context);
			/*if (getAssignation().isSet()) {
				out.append(getAssignation().toString() + " = (", context);
			}*/
			out.append(getClass().getSimpleName()
					+ (getDiagramSpecification() != null ? " conformTo " + getDiagramSpecification().getURI() : "")
					+ (getModelSlot() != null ? " from " + getModelSlot().getName() : "") + " {" + StringUtils.LINE_SEPARATOR, context);
			out.append("}", context);
			/*if (getAssignation().isSet()) {
				out.append(")", context);
			}*/
			return out.toString();
		}

		@Override
		public DiagramRole getAssignedFlexoProperty() {
			FlexoProperty<?> superFlexoRole = super.getAssignedFlexoProperty();
			if (superFlexoRole instanceof DiagramRole) {
				return (DiagramRole) superFlexoRole;
			} else if (superFlexoRole != null) {
				// logger.warning("Unexpected pattern property of type " + superPatternRole.getClass().getSimpleName());
				return null;
			}
			return null;
		}

		public String getDiagramName(RunTimeEvaluationContext evaluationContext) {
			try {
				return getDiagramName().getBindingValue(evaluationContext);
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}

		public String getDiagramURI(RunTimeEvaluationContext evaluationContext) {
			if (getDiagramURI() != null && getDiagramURI().isSet() && getDiagramURI().isValid()) {
				try {
					return getDiagramURI().getBindingValue(evaluationContext);
				} catch (TypeMismatchException e) {
					e.printStackTrace();
				} catch (NullReferenceException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		public FlexoResourceCenter<?> getResourceCenter(RunTimeEvaluationContext evaluationContext) {
			try {
				return getResourceCenter().getBindingValue(evaluationContext);
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

		private DataBinding<FlexoResourceCenter<?>> resourceCenter;

		@Override
		public DataBinding<FlexoResourceCenter<?>> getResourceCenter() {
			if (resourceCenter == null) {
				resourceCenter = new DataBinding<FlexoResourceCenter<?>>(this, FlexoResourceCenter.class,
						DataBinding.BindingDefinitionType.GET);
				resourceCenter.setBindingName("resourceCenter");
			}
			return resourceCenter;
		}

		@Override
		public void setResourceCenter(DataBinding<FlexoResourceCenter<?>> resourceCenter) {
			if (resourceCenter != null) {
				resourceCenter.setOwner(this);
				resourceCenter.setDeclaredType(FlexoResourceCenter.class);
				resourceCenter.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				resourceCenter.setBindingName("resourceCenter");
			}
			this.resourceCenter = resourceCenter;
		}

		@Override
		public DiagramSpecificationResource getDiagramSpecificationResource() {
			if (getAssignedFlexoProperty() instanceof DiagramRole) {
				return getAssignedFlexoProperty().getDiagramSpecificationResource();
			}
			if (diagramSpecificationResource == null && StringUtils.isNotEmpty(diagramSpecificationURI)) {
				diagramSpecificationResource = (DiagramSpecificationResource) getModelSlot().getModelSlotTechnologyAdapter()
						.getTechnologyContextManager().getResourceWithURI(diagramSpecificationURI);
				logger.info("Looked-up " + diagramSpecificationResource);
			}
			return diagramSpecificationResource;
		}

		@Override
		public void setDiagramSpecificationResource(DiagramSpecificationResource diagramSpecificationResource) {
			if (getAssignedFlexoProperty() instanceof DiagramRole) {
				getAssignedFlexoProperty().setDiagramSpecificationResource(diagramSpecificationResource);
			}
			this.diagramSpecificationResource = diagramSpecificationResource;
		}

		@Override
		public String getDiagramSpecificationURI() {
			if (diagramSpecificationResource != null) {
				return diagramSpecificationResource.getURI();
			}
			return diagramSpecificationURI;
		}

		@Override
		public void setDiagramSpecificationURI(String diagramSpecificationURI) {
			this.diagramSpecificationURI = diagramSpecificationURI;
		}

		@Override
		public DiagramSpecification getDiagramSpecification() {
			if (getDiagramSpecificationResource() != null) {
				return getDiagramSpecificationResource().getDiagramSpecification();
			}
			return null;
		}

		@Override
		public void setDiagramSpecification(DiagramSpecification diagramSpecification) {
			diagramSpecificationResource = diagramSpecification.getResource();
		}

		@Override
		public Type getAssignableType() {
			return Diagram.class;
		}

		@Override
		public Diagram execute(RunTimeEvaluationContext evaluationContext) {

			// TODO: reimplement this
			logger.warning("AddDiagram not implemented yet");

			System.out.println("DiagSpec = " + getDiagramSpecification());
			System.out.println("name=" + getDiagramName(evaluationContext));
			System.out.println("uri=" + getDiagramURI(evaluationContext));
			System.out.println("rc=" + getResourceCenter(evaluationContext));

			FlexoResourceCenter<?> resourceCenter = getResourceCenter(evaluationContext);

			DiagramTechnologyAdapter diagramTA = getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(
					DiagramTechnologyAdapter.class);

			File newFile = null;
			if (resourceCenter instanceof FileSystemBasedResourceCenter) {
				String relativePath = getRelativePath();
				if (!relativePath.endsWith(File.separator)) {
					relativePath = relativePath + File.separator;
				}
				relativePath = relativePath + JavaUtils.getClassName(getDiagramName(evaluationContext));
				if (!relativePath.endsWith(DiagramResource.DIAGRAM_SUFFIX)) {
					relativePath = relativePath + DiagramResource.DIAGRAM_SUFFIX;
				}
				newFile = new File(((FileSystemBasedResourceCenter) resourceCenter).getDirectory(), relativePath);

				DiagramResource newDiagramResource;
				try {
					newDiagramResource = diagramTA.createNewDiagram(getDiagramName(evaluationContext), getDiagramURI(evaluationContext),
							newFile, getDiagramSpecificationResource(), resourceCenter);
					return newDiagramResource.getDiagram();
				} catch (SaveResourceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

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
				logger.warning("Sorry, shape pattern property is undefined");
				return newShema;
			}
			// logger.info("ShapeSpecification pattern property: " + shapePatternRole);
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
			for (FlexoRole property : getFlexoConcept().getPatternRoles()) {
				if (property != action.getPatternRole() && property != shapePatternRole) {
					FlexoModelObject patternActor = getFlexoConceptInstance().getPatternActor(property);
					logger.info("Duplicate pattern actor for property " + property + " value=" + patternActor);
					newFlexoConceptInstance.setObjectForPatternRole(patternActor, property);
					patternActor.registerFlexoConceptReference(newFlexoConceptInstance, property);
				}
			}*/

			// return newDiagram;
			// }

			logger.warning("Cannot create Diagram !");

			return null;
		}
	}
}