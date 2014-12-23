/*
 * (c) Copyright 2010-2011 AgileBirds
 * (c) Copyright 2012-2015 Openflexo
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

package org.openflexo.technologyadapter.owl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FlexoBehaviour;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.DeclareEditionAction;
import org.openflexo.foundation.technologyadapter.DeclareEditionActions;
import org.openflexo.foundation.technologyadapter.DeclarePatternRole;
import org.openflexo.foundation.technologyadapter.DeclarePatternRoles;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.owl.fml.DataPropertyStatementActorReference;
import org.openflexo.technologyadapter.owl.fml.DataPropertyStatementRole;
import org.openflexo.technologyadapter.owl.fml.OWLClassRole;
import org.openflexo.technologyadapter.owl.fml.OWLDataPropertyRole;
import org.openflexo.technologyadapter.owl.fml.OWLIndividualRole;
import org.openflexo.technologyadapter.owl.fml.OWLObjectPropertyRole;
import org.openflexo.technologyadapter.owl.fml.OWLPropertyRole;
import org.openflexo.technologyadapter.owl.fml.ObjectPropertyStatementActorReference;
import org.openflexo.technologyadapter.owl.fml.ObjectPropertyStatementRole;
import org.openflexo.technologyadapter.owl.fml.StatementRole;
import org.openflexo.technologyadapter.owl.fml.SubClassStatementActorReference;
import org.openflexo.technologyadapter.owl.fml.SubClassStatementRole;
import org.openflexo.technologyadapter.owl.fml.editionaction.AddDataPropertyStatement;
import org.openflexo.technologyadapter.owl.fml.editionaction.AddOWLClass;
import org.openflexo.technologyadapter.owl.fml.editionaction.AddOWLIndividual;
import org.openflexo.technologyadapter.owl.fml.editionaction.AddObjectPropertyStatement;
import org.openflexo.technologyadapter.owl.fml.editionaction.AddRestrictionStatement;
import org.openflexo.technologyadapter.owl.fml.editionaction.AddSubClassStatement;
import org.openflexo.technologyadapter.owl.model.OWLObject;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.rm.OWLOntologyResource;

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
		// @DeclareFlexoRole(FML = "RestrictionStatement", flexoRoleClass = RestrictionStatementRole.class),
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
	public OWLTechnologyAdapter getModelSlotTechnologyAdapter();

	public static abstract class OWLModelSlotImpl extends TypeAwareModelSlotImpl<OWLOntology, OWLOntology> implements OWLModelSlot {

		private static final Logger logger = Logger.getLogger(OWLModelSlot.class.getPackage().getName());

		private static org.openflexo.model.factory.ModelFactory MF;

		static {
			try {
				MF = new org.openflexo.model.factory.ModelFactory(ModelContextLibrary.getCompoundModelContext(
						ObjectPropertyStatementRole.class, ObjectPropertyStatementActorReference.class, DataPropertyStatementRole.class,
						DataPropertyStatementActorReference.class, OWLClassRole.class, OWLDataPropertyRole.class,
						OWLObjectPropertyRole.class, OWLPropertyRole.class, StatementRole.class, SubClassStatementActorReference.class,
						SubClassStatementRole.class));
			} catch (ModelDefinitionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public static org.openflexo.model.factory.ModelFactory getModelFactory() {
			return MF;
		}

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
			}
			else if (OWLIndividualRole.class.isAssignableFrom(patternRoleClass)) {
				return "individual";
			}
			else if (OWLPropertyRole.class.isAssignableFrom(patternRoleClass)) {
				return "property";
			}
			else if (OWLDataPropertyRole.class.isAssignableFrom(patternRoleClass)) {
				return "dataProperty";
			}
			else if (OWLObjectPropertyRole.class.isAssignableFrom(patternRoleClass)) {
				return "objectProperty";
			}
			else if (DataPropertyStatementRole.class.isAssignableFrom(patternRoleClass)) {
				return "fact";
			}
			else if (ObjectPropertyStatementRole.class.isAssignableFrom(patternRoleClass)) {
				return "fact";
			}
			else if (SubClassStatementRole.class.isAssignableFrom(patternRoleClass)) {
				return "fact";
			}
			return null;
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
			return msInstance.getModel().getObject(objectURI);
		}

		@Override
		public Type getType() {
			return OWLOntology.class;
		}

		@Override
		public String getPreciseType() {
			return "OWL Ontology";
		};

		@Override
		public OWLTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (OWLTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

		@Override
		public OWLOntologyResource createProjectSpecificEmptyModel(FlexoProject project, String filename, String modelUri,
				FlexoMetaModelResource<OWLOntology, OWLOntology, ?> metaModelResource) {
			return getModelSlotTechnologyAdapter().createNewOntology(project, filename, modelUri, metaModelResource);
		}

		@Override
		public OWLOntologyResource createSharedEmptyModel(FlexoResourceCenter<?> resourceCenter, String relativePath, String filename,
				String modelUri, FlexoMetaModelResource<OWLOntology, OWLOntology, ?> metaModelResource) {
			return getModelSlotTechnologyAdapter().createNewOntology((FileSystemBasedResourceCenter) resourceCenter, relativePath, filename,
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

		@Override
		public List<Class<? extends FlexoBehaviour>> getAvailableFlexoBehaviourTypes() {
			List<Class<? extends FlexoBehaviour>> types = new ArrayList<Class<? extends FlexoBehaviour>>();
			return types;
		}

	}
}
