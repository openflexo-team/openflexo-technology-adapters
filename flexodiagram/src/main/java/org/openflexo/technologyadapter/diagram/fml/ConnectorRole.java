package org.openflexo.technologyadapter.diagram.fml;

import java.lang.reflect.Type;
import java.util.List;

import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.fml.FMLRepresentationContext;
import org.openflexo.foundation.fml.FMLRepresentationContext.FMLRepresentationOutput;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Embedded;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.dm.GraphicalRepresentationChanged;

@ModelEntity
@ImplementationClass(ConnectorRole.ConnectorRoleImpl.class)
@XMLElement
public interface ConnectorRole extends GraphicalElementRole<DiagramConnector, ConnectorGraphicalRepresentation> {

	@PropertyIdentifier(type = GraphicalRepresentation.class)
	public static final String GRAPHICAL_REPRESENTATION_KEY = "graphicalRepresentation";
	@PropertyIdentifier(type = ShapeGraphicalRepresentation.class)
	public static final String ARTIFACT_FROM_GRAPHICAL_REPRESENTATION_KEY = "artifactFromGraphicalRepresentation";
	@PropertyIdentifier(type = ShapeGraphicalRepresentation.class)
	public static final String ARTIFACT_TO_GRAPHICAL_REPRESENTATION_KEY = "artifactToGraphicalRepresentation";
	@PropertyIdentifier(type = ShapeRole.class)
	public static final String START_SHAPE_PATTERN_ROLE_KEY = "startShapeRole";
	@PropertyIdentifier(type = ShapeRole.class)
	public static final String END_SHAPE_PATTERN_ROLE_KEY = "endShapeRole";

	@Getter(value = GRAPHICAL_REPRESENTATION_KEY)
	@CloningStrategy(StrategyType.CLONE)
	@Embedded
	@XMLElement
	public ConnectorGraphicalRepresentation getGraphicalRepresentation();

	@Setter(GRAPHICAL_REPRESENTATION_KEY)
	public void setGraphicalRepresentation(ConnectorGraphicalRepresentation graphicalRepresentation);

	@Getter(value = ARTIFACT_FROM_GRAPHICAL_REPRESENTATION_KEY)
	@XMLElement(context = "ArtifactFromGraphicalRepresentation_")
	public ShapeGraphicalRepresentation getArtifactFromGraphicalRepresentation();

	@Setter(ARTIFACT_FROM_GRAPHICAL_REPRESENTATION_KEY)
	public void setArtifactFromGraphicalRepresentation(ShapeGraphicalRepresentation artifactFromGraphicalRepresentation);

	@Getter(value = ARTIFACT_TO_GRAPHICAL_REPRESENTATION_KEY)
	@XMLElement(context = "ArtifactToGraphicalRepresentation_")
	public ShapeGraphicalRepresentation getArtifactToGraphicalRepresentation();

	@Setter(ARTIFACT_TO_GRAPHICAL_REPRESENTATION_KEY)
	public void setArtifactToGraphicalRepresentation(ShapeGraphicalRepresentation artifactToGraphicalRepresentation);

	@Getter(value = START_SHAPE_PATTERN_ROLE_KEY)
	@XMLElement(context = "StartShape_")
	public ShapeRole getStartShapeRole();

	@Setter(START_SHAPE_PATTERN_ROLE_KEY)
	public void setStartShapeRole(ShapeRole startShapeRole);

	@Getter(value = END_SHAPE_PATTERN_ROLE_KEY)
	@XMLElement(context = "EndShape_")
	public ShapeRole getEndShapeRole();

	@Setter(END_SHAPE_PATTERN_ROLE_KEY)
	public void setEndShapeRole(ShapeRole endShapeRole);

	public boolean getStartShapeAsDefinedInAction();

	public void setStartShapeAsDefinedInAction(boolean flag);

	public boolean getEndShapeAsDefinedInAction();

	public void setEndShapeAsDefinedInAction(boolean flag);

	public List<ShapeRole> getAvailableShapeRoles();

	public static abstract class ConnectorRoleImpl extends GraphicalElementRoleImpl<DiagramConnector, ConnectorGraphicalRepresentation>
			implements ConnectorRole {

		private ShapeGraphicalRepresentation artifactFromGraphicalRepresentation;
		private ShapeGraphicalRepresentation artifactToGraphicalRepresentation;

		public ConnectorRoleImpl() {
			super();
		}

		@Override
		protected void initDefaultSpecifications() {
			super.initDefaultSpecifications();
			if (getVirtualModelFactory() != null) {
				for (GraphicalFeature<?, ?> GF : AVAILABLE_FEATURES) {
					GraphicalElementSpecification newGraphicalElementSpecification = getVirtualModelFactory().newInstance(
							GraphicalElementSpecification.class);
					newGraphicalElementSpecification.setPatternRole(this);
					newGraphicalElementSpecification.setFeature(GF);
					newGraphicalElementSpecification.setReadOnly(false);
					newGraphicalElementSpecification.setMandatory(true);
					grSpecifications.add(newGraphicalElementSpecification);
				}
			}
		}

		@Override
		public String getFMLRepresentation(FMLRepresentationContext context) {
			FMLRepresentationOutput out = new FMLRepresentationOutput(context);
			out.append("FlexoRole " + getName() + " as ConnectorSpecification from " + getVirtualModel().getName() + ";", context);
			return out.toString();
		}

		@Override
		public String getPreciseType() {
			return FlexoLocalization.localizedForKey("connector");
		}

		@Override
		public ShapeGraphicalRepresentation getArtifactFromGraphicalRepresentation() {
			return artifactFromGraphicalRepresentation;
		}

		@Override
		public void setArtifactFromGraphicalRepresentation(ShapeGraphicalRepresentation artifactFromGraphicalRepresentation) {
			this.artifactFromGraphicalRepresentation = artifactFromGraphicalRepresentation;
			setChanged();
			notifyObservers(new GraphicalRepresentationChanged(this, artifactFromGraphicalRepresentation));
		}

		@Override
		public ShapeGraphicalRepresentation getArtifactToGraphicalRepresentation() {
			return artifactToGraphicalRepresentation;
		}

		@Override
		public void setArtifactToGraphicalRepresentation(ShapeGraphicalRepresentation artifactToGraphicalRepresentation) {
			this.artifactToGraphicalRepresentation = artifactToGraphicalRepresentation;
			setChanged();
			notifyObservers(new GraphicalRepresentationChanged(this, artifactToGraphicalRepresentation));
		}

		private ShapeRole startShapeRole;
		private ShapeRole endShapeRole;

		@Override
		public ShapeRole getStartShapeRole() {
			return startShapeRole;
		}

		@Override
		public void setStartShapeRole(ShapeRole startShapeRole) {
			this.startShapeRole = startShapeRole;
			setChanged();
			notifyObservers(new GraphicalRepresentationChanged(this, startShapeRole != null ? startShapeRole.getGraphicalRepresentation()
					: artifactFromGraphicalRepresentation));
		}

		@Override
		public boolean getStartShapeAsDefinedInAction() {
			return getStartShapeRole() == null;
		}

		@Override
		public void setStartShapeAsDefinedInAction(boolean flag) {
			if (!flag && getFlexoConcept().getFlexoRoles(ShapeRole.class).size() > 0) {
				setStartShapeRole(getFlexoConcept().getFlexoRoles(ShapeRole.class).get(0));
			} else {
				// System.out.println("setStartShapePatternRole with null");
				setStartShapeRole(null);
			}
		}

		@Override
		public ShapeRole getEndShapeRole() {
			return endShapeRole;
		}

		@Override
		public void setEndShapeRole(ShapeRole endShapeRole) {
			this.endShapeRole = endShapeRole;
			setChanged();
			notifyObservers(new GraphicalRepresentationChanged(this, endShapeRole != null ? endShapeRole.getGraphicalRepresentation()
					: artifactToGraphicalRepresentation));
		}

		@Override
		public boolean getEndShapeAsDefinedInAction() {
			return getEndShapeRole() == null;
		}

		@Override
		public void setEndShapeAsDefinedInAction(boolean flag) {
			if (!flag && getFlexoConcept().getFlexoRoles(ShapeRole.class).size() > 0) {
				setEndShapeRole(getFlexoConcept().getFlexoRoles(ShapeRole.class).get(0));
			} else {
				// System.out.println("setEndShapePatternRole with null");
				setEndShapeRole(null);
			}
		}

		/*public ShapeRole getStartShape() {
			for (FlexoBehaviour es : getFlexoConcept().getEditionSchemes()) {
				for (EditionAction action : es.getActions()) {
					if ((action.getPatternRole() == this) && (action instanceof AddConnector)) {
						AddConnector addConnector = (AddConnector) action;
						for (FlexoRole r : getFlexoConcept().getPatternRoles()) {
							if ((r instanceof ShapeRole) && (addConnector.getFromShape() != null)
									&& addConnector.getFromShape().toString().equals(r.getPatternRoleName())) {
								return (ShapeRole) r;
							}
						}
					}
				}
			}

			return null;
		}

		public ShapeRole getEndShape() {
			for (FlexoBehaviour es : getFlexoConcept().getEditionSchemes()) {
				for (EditionAction action : es.getActions()) {
					if ((action.getPatternRole() == this) && (action instanceof AddConnector)) {
						AddConnector addConnector = (AddConnector) action;
						for (FlexoRole r : getFlexoConcept().getPatternRoles()) {
							if ((r instanceof ShapeRole) && (addConnector.getToShape() != null)
									&& addConnector.getToShape().toString().equals(r.getPatternRoleName())) {
								return (ShapeRole) r;
							}
						}
					}
				}
			}

			return null;
		}*/

		@Override
		public Type getType() {
			return DiagramConnector.class;
		}

		public static GraphicalFeature<?, ?>[] AVAILABLE_FEATURES = {};

		@Override
		public List<ShapeRole> getAvailableShapeRoles() {
			return getFlexoConcept().getFlexoRoles(ShapeRole.class);
		}
	}
}
