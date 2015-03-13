/**
 * 
 * Copyright (c) 2013-2015, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
 * 
 * This file is part of Owlconnector, a component of the software infrastructure 
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


package org.openflexo.technologyadapter.owl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FlexoBehaviour;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
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
@DeclareFlexoRoles({ OWLIndividualRole.class, OWLClassRole.class, OWLDataPropertyRole.class, OWLObjectPropertyRole.class,
		OWLPropertyRole.class, DataPropertyStatementRole.class, ObjectPropertyStatementRole.class, SubClassStatementRole.class })
@DeclareEditionActions({ AddOWLIndividual.class, AddOWLClass.class, AddDataPropertyStatement.class, AddObjectPropertyStatement.class,
		AddRestrictionStatement.class, AddSubClassStatement.class })
@ModelEntity
@ImplementationClass(OWLModelSlot.OWLModelSlotImpl.class)
@XMLElement
@FML("OWLModelSlot")
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
			} else if (SubClassStatementRole.class.isAssignableFrom(patternRoleClass)) {
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
		public String getTypeDescription() {
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
			return getModelSlotTechnologyAdapter().createNewOntology((FileSystemBasedResourceCenter) resourceCenter, relativePath,
					filename, modelUri, (OWLOntologyResource) metaModelResource);
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
