package org.openflexo.technologyadapter.owl;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.DeclareEditionAction;
import org.openflexo.foundation.technologyadapter.DeclareEditionActions;
import org.openflexo.foundation.technologyadapter.DeclarePatternRole;
import org.openflexo.foundation.technologyadapter.DeclarePatternRoles;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.foundation.view.TypeAwareModelSlotInstance;
import org.openflexo.foundation.view.View;
import org.openflexo.foundation.view.action.CreateVirtualModelInstance;
import org.openflexo.foundation.viewpoint.FlexoRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.owl.model.OWLObject;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.rm.OWLOntologyResource;
import org.openflexo.technologyadapter.owl.viewpoint.DataPropertyStatementRole;
import org.openflexo.technologyadapter.owl.viewpoint.OWLClassRole;
import org.openflexo.technologyadapter.owl.viewpoint.OWLDataPropertyRole;
import org.openflexo.technologyadapter.owl.viewpoint.OWLIndividualRole;
import org.openflexo.technologyadapter.owl.viewpoint.OWLObjectPropertyRole;
import org.openflexo.technologyadapter.owl.viewpoint.OWLPropertyRole;
import org.openflexo.technologyadapter.owl.viewpoint.ObjectPropertyStatementRole;
import org.openflexo.technologyadapter.owl.viewpoint.RestrictionStatementRole;
import org.openflexo.technologyadapter.owl.viewpoint.SubClassStatementRole;
import org.openflexo.technologyadapter.owl.viewpoint.editionaction.AddDataPropertyStatement;
import org.openflexo.technologyadapter.owl.viewpoint.editionaction.AddOWLClass;
import org.openflexo.technologyadapter.owl.viewpoint.editionaction.AddOWLIndividual;
import org.openflexo.technologyadapter.owl.viewpoint.editionaction.AddObjectPropertyStatement;
import org.openflexo.technologyadapter.owl.viewpoint.editionaction.AddRestrictionStatement;
import org.openflexo.technologyadapter.owl.viewpoint.editionaction.AddSubClassStatement;

/**
 * <p>
 * Implementation of the ModelSlot class for the OWL technology adapter
 * 
 * @author sylvain, luka
 * 
 */
@DeclarePatternRoles({ // All pattern roles available through this model slot
@DeclarePatternRole(FML = "OWLIndividual", flexoRoleClass = OWLIndividualRole.class),
		@DeclarePatternRole(FML = "OWLClass", flexoRoleClass = OWLClassRole.class),
		@DeclarePatternRole(FML = "OWLDataProperty", flexoRoleClass = OWLDataPropertyRole.class),
		@DeclarePatternRole(FML = "OWLObjectProperty", flexoRoleClass = OWLObjectPropertyRole.class),
		@DeclarePatternRole(FML = "OWLProperty", flexoRoleClass = OWLPropertyRole.class),
		@DeclarePatternRole(FML = "DataPropertyStatement", flexoRoleClass = DataPropertyStatementRole.class),
		@DeclarePatternRole(FML = "ObjectPropertyStatement", flexoRoleClass = ObjectPropertyStatementRole.class),
		// @DeclarePatternRole(FML = "RestrictionStatement", flexoRoleClass = RestrictionStatementRole.class),
		@DeclarePatternRole(FML = "SubClassStatement", flexoRoleClass = SubClassStatementRole.class) })
@DeclareEditionActions({ // All edition actions available through this model slot
@DeclareEditionAction(FML = "AddOWLIndividual", editionActionClass = AddOWLIndividual.class), // Add instance
		@DeclareEditionAction(FML = "AddOWLClass", editionActionClass = AddOWLClass.class), // Add class
		@DeclareEditionAction(FML = "AddDataPropertyStatement", editionActionClass = AddDataPropertyStatement.class), // Add class
		@DeclareEditionAction(FML = "AddObjectPropertyStatement", editionActionClass = AddObjectPropertyStatement.class), // Add class
		@DeclareEditionAction(FML = "AddRestrictionStatement", editionActionClass = AddRestrictionStatement.class), // Add class
		@DeclareEditionAction(FML = "AddSubClassStatement", editionActionClass = AddSubClassStatement.class), // Add class
})
@ModelEntity
@ImplementationClass(OWLModelSlot.OWLModelSlotImpl.class)
@XMLElement
public interface OWLModelSlot extends TypeAwareModelSlot<OWLOntology, OWLOntology> {

	@Override
	public OWLTechnologyAdapter getTechnologyAdapter();

	public static abstract class OWLModelSlotImpl extends TypeAwareModelSlotImpl<OWLOntology, OWLOntology> implements OWLModelSlot {

		private static final Logger logger = Logger.getLogger(OWLModelSlot.class.getPackage().getName());

		@Override
		public Class<OWLTechnologyAdapter> getTechnologyAdapterClass() {
			return OWLTechnologyAdapter.class;
		}

		/**
		 * Instanciate a new model slot instance configuration for this model slot
		 */
		@Override
		public OWLModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
			return new OWLModelSlotInstanceConfiguration(this, action);
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (OWLClassRole.class.isAssignableFrom(patternRoleClass)) {
				return "class";
			} else if (OWLIndividualRole.class.isAssignableFrom(patternRoleClass)) {
				return "individual";
			} else if (OWLPropertyRole.class.isAssignableFrom(patternRoleClass)) {
				return "property";
			} else if (OWLDataPropertyRole.class.isAssignableFrom(patternRoleClass)) {
				return "dataProperty";
			} else if (OWLObjectPropertyRole.class.isAssignableFrom(patternRoleClass)) {
				return "objectProperty";
			} else if (DataPropertyStatementRole.class.isAssignableFrom(patternRoleClass)) {
				return "fact";
			} else if (ObjectPropertyStatementRole.class.isAssignableFrom(patternRoleClass)) {
				return "fact";
			} else if (RestrictionStatementRole.class.isAssignableFrom(patternRoleClass)) {
				return "restriction";
			} else if (SubClassStatementRole.class.isAssignableFrom(patternRoleClass)) {
				return "fact";
			}
			return super.defaultFlexoRoleName(patternRoleClass);
		}

		@Override
		public String getURIForObject(
				TypeAwareModelSlotInstance<OWLOntology, OWLOntology, ? extends TypeAwareModelSlot<OWLOntology, OWLOntology>> msInstance,
				Object o) {
			return ((OWLObject) o).getURI();
		}

		@Override
		public Object retrieveObjectWithURI(
				TypeAwareModelSlotInstance<OWLOntology, OWLOntology, ? extends TypeAwareModelSlot<OWLOntology, OWLOntology>> msInstance,
				String objectURI) {
			return msInstance.getResourceData().getObject(objectURI);
		}

		@Override
		public Type getType() {
			return OWLOntology.class;
		}

		@Override
		public OWLTechnologyAdapter getTechnologyAdapter() {
			return (OWLTechnologyAdapter) super.getTechnologyAdapter();
		}

		@Override
		public OWLOntologyResource createProjectSpecificEmptyModel(View view, String filename, String modelUri,
				FlexoMetaModelResource<OWLOntology, OWLOntology, ?> metaModelResource) {
			return getTechnologyAdapter().createNewOntology(view.getProject(), filename, modelUri, metaModelResource);
		}

		@Override
		public OWLOntologyResource createSharedEmptyModel(FlexoResourceCenter<?> resourceCenter, String relativePath, String filename,
				String modelUri, FlexoMetaModelResource<OWLOntology, OWLOntology, ?> metaModelResource) {
			return getTechnologyAdapter().createNewOntology((FileSystemBasedResourceCenter) resourceCenter, relativePath, filename,
					modelUri, (OWLOntologyResource) metaModelResource);
		}

		/**
		 * OWL ontologies conformity is not strict (an ontology might import many other ontologies)
		 */
		@Override
		public boolean isStrictMetaModelling() {
			return false;
		}

		@Override
		public String getModelSlotDescription() {
			return "Ontology importing " + getMetaModelURI();
		}

	}
}
