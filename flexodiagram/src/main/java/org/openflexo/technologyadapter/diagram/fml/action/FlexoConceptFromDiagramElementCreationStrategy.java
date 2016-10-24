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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.openflexo.connie.DataBinding;
import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.action.transformation.FlexoConceptCreationStrategy;
import org.openflexo.foundation.fml.FlexoBehaviour;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.inspector.FlexoConceptInspector;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.action.DeclareDiagramElementInFlexoConcept.DrawingObjectEntry;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.StringUtils;

/**
 * This abstract class is the base class for a {@link FlexoConcept} creation strategy, using a {@link DiagramElement}
 * 
 * @author sylvain
 *
 */
public abstract class FlexoConceptFromDiagramElementCreationStrategy<A extends DeclareDiagramElementInFlexoConcept<A, ?>>
		extends FlexoConceptCreationStrategy<A> {

	protected LinkedHashMap<DrawingObjectEntry, GraphicalElementRole<?, ?>> newGraphicalElementRoles;

	public FlexoConceptFromDiagramElementCreationStrategy(A transformationAction) {
		super(transformationAction);
	}

	public abstract String getDefaultRoleName();

	protected GraphicalElementRole<?, ?> primaryRepresentationRole = null;

	@Override
	public FlexoConcept performStrategy() {
		FlexoConcept newFlexoConcept = super.performStrategy();

		// Create graphical elements pattern property

		newGraphicalElementRoles = new LinkedHashMap<DrawingObjectEntry, GraphicalElementRole<?, ?>>();

		for (DrawingObjectEntry entry : getTransformationAction().getDrawingObjectEntries()) {
			if (entry.getSelectThis()) {
				if (entry.graphicalObject instanceof DiagramShape) {
					DiagramShape grShape = (DiagramShape) entry.graphicalObject;
					ShapeRole newShapeRole = getTransformationAction().getFactory().newInstance(ShapeRole.class);
					newShapeRole.setRoleName(entry.flexoRoleName);
					newShapeRole.setModelSlot(getTransformationAction().getTypedDiagramModelSlot());
					/*if (mainPropertyDescriptor != null && entry.isMainEntry()) {
						newShapeFlexoRole.setLabel(new DataBinding<String>(getIndividualFlexoRoleName() + "."
								+ mainPropertyDescriptor.property.getName()));
					} else {*/
					newShapeRole.setReadOnlyLabel(true);
					if (StringUtils.isNotEmpty(entry.graphicalObject.getName())) {
						newShapeRole.setLabel(new DataBinding<String>("\"" + entry.graphicalObject.getName() + "\""));
					}
					// }

					// newShapeRole.setExampleLabel(grShape.getGraphicalRepresentation().getText());
					newShapeRole.setExampleLabel(grShape.getName());
					// We clone here the GR (fixed unfocusable GR bug)
					newShapeRole.setGraphicalRepresentation((ShapeGraphicalRepresentation) grShape.getGraphicalRepresentation().clone());
					// Forces GR to be displayed in view
					newShapeRole.getGraphicalRepresentation().setAllowToLeaveBounds(false);
					newFlexoConcept.addToFlexoProperties(newShapeRole);
					if (entry.getParentEntry() != null) {
						newShapeRole.setParentShapeRole((ShapeRole) newGraphicalElementRoles.get(entry.getParentEntry()));
					}
					if (entry.isMainEntry()) {
						primaryRepresentationRole = newShapeRole;
					}
					newGraphicalElementRoles.put(entry, newShapeRole);
				}
				if (entry.graphicalObject instanceof DiagramConnector) {
					DiagramConnector grConnector = (DiagramConnector) entry.graphicalObject;
					ConnectorRole newConnectorRole = getTransformationAction().getFactory().newInstance(ConnectorRole.class);
					newConnectorRole.setRoleName(entry.flexoRoleName);
					newConnectorRole.setReadOnlyLabel(true);
					if (StringUtils.isNotEmpty(entry.graphicalObject.getName())) {
						newConnectorRole.setLabel(new DataBinding<String>("\"" + entry.graphicalObject.getName() + "\""));
					}
					newConnectorRole.setExampleLabel(grConnector.getName());
					newConnectorRole.setGraphicalRepresentation(
							(ConnectorGraphicalRepresentation) grConnector.getGraphicalRepresentation().clone());
					newFlexoConcept.addToFlexoProperties(newConnectorRole);
					// Set the source/target
					// newConnectorFlexoRole.setEndShapeFlexoRole(endShapeFlexoRole);
					if (entry.isMainEntry()) {
						primaryRepresentationRole = newConnectorRole;
					}
					newGraphicalElementRoles.put(entry, newConnectorRole);
				}

			}
		}
		// newFlexoConcept.setPrimaryRepresentationRole(primaryRepresentationRole);

		// Add inspector
		FlexoConceptInspector inspector = newFlexoConcept.getInspector();
		inspector.setInspectorTitle(getFlexoConceptName());

		/*for (PropertyEntry e : propertyEntries) {
				if (e.selectEntry) {
					if (e.property instanceof IFlexoOntologyObjectProperty) {
						IFlexoOntologyConcept range = ((IFlexoOntologyObjectProperty) e.property).getRange();
						if (range instanceof IFlexoOntologyClass) {
							InspectorEntry newInspectorEntry = null;
							newInspectorEntry = new TextFieldInspectorEntry(builder);
							newInspectorEntry.setName(e.property.getName());
							newInspectorEntry.setLabel(e.label);
							newInspectorEntry.setIsReadOnly(true);
							newInspectorEntry.setData(new DataBinding<Object>(e.property.getName() + ".uriName"));
							inspector.addToEntries(newInspectorEntry);
						}
					} else if (e.property instanceof IFlexoOntologyDataProperty) {
						InspectorEntry newInspectorEntry = null;
						switch (((IFlexoOntologyDataProperty) e.property).getRange().getBuiltInDataType()) {
						case Boolean:
							newInspectorEntry = new CheckboxInspectorEntry(builder);
							break;
						case Byte:
						case Integer:
						case Long:
						case Short:
							newInspectorEntry = new IntegerInspectorEntry(builder);
							break;
						case Double:
						case Float:
							newInspectorEntry = new FloatInspectorEntry(builder);
							break;
						case String:
							newInspectorEntry = new TextFieldInspectorEntry(builder);
							break;
						default:
							logger.warning("Not handled: " + ((IFlexoOntologyDataProperty) e.property).getRange());
						}
						if (newInspectorEntry != null) {
							newInspectorEntry.setName(e.property.getName());
							newInspectorEntry.setLabel(e.label);
							newInspectorEntry.setData(new DataBinding<Object>(getIndividualFlexoRoleName() + "."
									+ e.property.getName()));
							inspector.addToEntries(newInspectorEntry);
						}
					}
				}
			}*/

		return newFlexoConcept;
	}

	private ArrayList<FlexoBehaviourConfiguration> flexoBehaviours;

	public void setFlexoBehaviours(ArrayList<FlexoBehaviourConfiguration> editionSchemes) {
		this.flexoBehaviours = editionSchemes;
	}

	public List<FlexoBehaviourConfiguration> getFlexoBehaviours() {
		if (flexoBehaviours == null) {
			flexoBehaviours = new ArrayList<FlexoBehaviourConfiguration>();

			initializeBehaviours();

			FlexoBehaviourConfiguration deleteShapeBehaviour = new FlexoBehaviourConfiguration(FlexoBehaviourChoice.DELETE_GR_ONLY);
			FlexoBehaviourConfiguration deleteShapeAndModelAllBehaviours = new FlexoBehaviourConfiguration(
					FlexoBehaviourChoice.DELETE_GR_AND_MODEL);

			flexoBehaviours.add(deleteShapeBehaviour);
			flexoBehaviours.add(deleteShapeAndModelAllBehaviours);
		}
		return flexoBehaviours;
	}

	public abstract void initializeBehaviours();

	/*public boolean editionSchemesNamedAreValid() {
		for (FlexoBehaviourConfiguration editionSchemeConfiguration : getFlexoBehaviours()) {
			if (editionSchemeConfiguration == null || editionSchemeConfiguration.getFlexoBehaviour() == null
					|| StringUtils.isEmpty(editionSchemeConfiguration.getFlexoBehaviour().getName()))
				return false;
		}
		return true;
	}*/

	public void addFlexoBehaviourConfigurationDeleteGROnly() {
		FlexoBehaviourConfiguration editionSchemeConfiguration = new FlexoBehaviourConfiguration(FlexoBehaviourChoice.DELETE_GR_ONLY);
		getFlexoBehaviours().add(editionSchemeConfiguration);
	}

	public void addFlexoBehaviourConfigurationDeleteGRAndModel() {
		FlexoBehaviourConfiguration editionSchemeConfiguration = new FlexoBehaviourConfiguration(FlexoBehaviourChoice.DELETE_GR_AND_MODEL);
		getFlexoBehaviours().add(editionSchemeConfiguration);
	}

	public void removeFlexoBehaviourConfiguration(FlexoBehaviourConfiguration editionSchemeConfiguration) {
		getFlexoBehaviours().remove(editionSchemeConfiguration);
	}

	public void updateEditionSchemesName(String name) {
		for (FlexoBehaviourConfiguration editionSchemeConfiguration : getFlexoBehaviours()) {
			if (editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DELETE_GR_ONLY) {
				editionSchemeConfiguration.getFlexoBehaviour().setName("deleteGR");
			}
			if (editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DELETE_GR_AND_MODEL) {
				editionSchemeConfiguration.getFlexoBehaviour().setName("deleteGRandModel");
			}
			if (name != null) {
				editionSchemeConfiguration.getFlexoBehaviour().setName(editionSchemeConfiguration.getFlexoBehaviour().getName() + name);
			}
		}
		updateSpecialSchemeNames();
	}

	public void updateSpecialSchemeNames() {

	}

	public static enum FlexoBehaviourChoice {
		DELETE_GR_ONLY, DELETE_GR_AND_MODEL, DROP_AND_CREATE, DROP_AND_SELECT, LINK, CREATION
	}

	public class FlexoBehaviourConfiguration {

		private FlexoBehaviourChoice type;

		private boolean isValid;

		private FlexoBehaviour flexoBehaviour;

		public FlexoBehaviourConfiguration(FlexoBehaviourChoice type) {
			this.type = type;
			this.isValid = true;
			if (getTransformationAction().getFactory() != null) {
				if (type == FlexoBehaviourChoice.DELETE_GR_ONLY) {
					flexoBehaviour = getTransformationAction().getFactory().newDeletionScheme();
					flexoBehaviour.setName("defaultDeleteGROnly");
				}
				if (type == FlexoBehaviourChoice.DELETE_GR_AND_MODEL) {
					flexoBehaviour = getTransformationAction().getFactory().newDeletionScheme();
					flexoBehaviour.setName("defaultDeleteGRandModel");
				}
				if (type == FlexoBehaviourChoice.DROP_AND_CREATE) {
					flexoBehaviour = getTransformationAction().getFactory().newInstance(DropScheme.class);
					((DropScheme) flexoBehaviour).setTopTarget(true);
					flexoBehaviour.setName("defaultDropAndCreate");
				}
				if (type == FlexoBehaviourChoice.DROP_AND_SELECT) {
					flexoBehaviour = getTransformationAction().getFactory().newInstance(DropScheme.class);
					((DropScheme) flexoBehaviour).setTopTarget(true);
					flexoBehaviour.setName("defaultDropAndSelect");
				}
				if (type == FlexoBehaviourChoice.LINK) {
					flexoBehaviour = getTransformationAction().getFactory().newInstance(LinkScheme.class);
					flexoBehaviour.setName("defaultLink");
				}
			}
		}

		public FlexoBehaviourChoice getType() {
			return type;
		}

		public void setType(FlexoBehaviourChoice type) {
			this.type = type;
		}

		public FlexoBehaviour getFlexoBehaviour() {
			return flexoBehaviour;
		}

		public boolean isValid() {
			return isValid;
		}

		public void setValid(boolean isValid) {
			this.isValid = isValid;
		}

	}

	/*private void createSchemeActions(FlexoBehaviourConfiguration editionSchemeConfiguration) {
		FlexoBehaviour editionScheme = null;
	
		// Delete shapes as well as model
		if (editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DELETE_GR_AND_MODEL) {
			editionScheme = createDeleteFlexoBehaviourActions(editionSchemeConfiguration, false);
		}
	
		// Delete only shapes
		if (editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DELETE_GR_ONLY) {
			editionScheme = createDeleteFlexoBehaviourActions(editionSchemeConfiguration, true);
		}
	
		// Drop
		if (editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DROP_AND_SELECT
				|| editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DROP_AND_CREATE) {
			editionScheme = createDropFlexoBehaviourActions(editionSchemeConfiguration);
		}
	
		getNewFlexoConcept().addToFlexoBehaviours(editionScheme);
	}*/

	/*private FlexoBehaviour createDeleteFlexoBehaviourActions(FlexoBehaviourConfiguration editionSchemeConfiguration, boolean shapeOnly) {
	
		DeletionScheme editionScheme = (DeletionScheme) editionSchemeConfiguration.getFlexoBehaviour();
	
		List<FlexoProperty<?>> propertiesToDelete = new ArrayList<FlexoProperty<?>>();
		if (shapeOnly) {
			for (FlexoProperty<?> pr : getNewFlexoConcept().getFlexoProperties()) {
				if (pr instanceof GraphicalElementRole) {
					propertiesToDelete.add(pr);
				}
			}
		} else {
			for (FlexoProperty<?> pr : getNewFlexoConcept().getFlexoProperties()) {
				propertiesToDelete.add(pr);
			}
		}
	
		Collections.sort(propertiesToDelete, new Comparator<FlexoProperty<?>>() {
			@Override
			public int compare(FlexoProperty<?> o1, FlexoProperty<?> o2) {
				if (o1 instanceof ShapeRole && o2 instanceof ConnectorRole) {
					return 1;
				} else if (o1 instanceof ConnectorRole && o2 instanceof ShapeRole) {
					return -1;
				}
	
				if (o1 instanceof ShapeRole) {
					if (o2 instanceof ShapeRole) {
						if (((ShapeRole) o1).isContainedIn((ShapeRole) o2)) {
							return -1;
						}
						if (((ShapeRole) o2).isContainedIn((ShapeRole) o1)) {
							return 1;
						}
						return 0;
					}
				}
				return 0;
			}
	
		});
		for (FlexoProperty<?> pr : propertiesToDelete) {
			DeleteAction a = getTransformationAction().getFactory().newDeleteAction();
			a.setObject(new DataBinding<Object>(pr.getPropertyName()));
			editionScheme.addToActions(a);
		}
		return editionScheme;
	}*/

}
