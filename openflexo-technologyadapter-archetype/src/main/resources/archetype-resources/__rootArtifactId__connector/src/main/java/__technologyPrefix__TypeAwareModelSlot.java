#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * (c) Copyright 2013- Openflexo
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

package ${package};

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.ontology.IFlexoOntologyObject;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.DeclareEditionAction;
import org.openflexo.foundation.technologyadapter.DeclareEditionActions;
import org.openflexo.foundation.technologyadapter.DeclareFetchRequest;
import org.openflexo.foundation.technologyadapter.DeclareFetchRequests;
import org.openflexo.foundation.technologyadapter.DeclarePatternRole;
import org.openflexo.foundation.technologyadapter.DeclarePatternRoles;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import ${package}.${technologyPrefix}TechnologyAdapter;
import ${package}.metamodel.${technologyPrefix}MetaModel;
import ${package}.model.${technologyPrefix}Model;
import ${package}.rm.${technologyPrefix}MetaModelResource;
import ${package}.rm.${technologyPrefix}ModelResource;
import ${package}.virtualmodel.${technologyPrefix}ObjectIndividualRole;
import ${package}.virtualmodel.action.Add${technologyPrefix}ObjectIndividual;
import ${package}.virtualmodel.action.Select${technologyPrefix}ObjectIndividual;

/**
 * Implementation of the ModelSlot class for the ${technologyPrefix} technology adapter<br>
 * We expect here to connect an ${technologyPrefix} model conform to an ${technologyPrefix}MetaModel
 * 
 * @author ${author}
 * 
 */
@DeclareFlexoRoles({${technologyPrefix}ObjectIndividualRole.class})
@DeclareEditionActions({Add${technologyPrefix}ObjectIndividual.class})
@DeclareFetchRequests({Select${technologyPrefix}ObjectIndividual.class})
@ModelEntity
@ImplementationClass(${technologyPrefix}TypeAwareModelSlot.${technologyPrefix}TypeAwareModelSlotImpl.class)
@XMLElement
public interface ${technologyPrefix}TypeAwareModelSlot extends TypeAwareModelSlot<${technologyPrefix}Model, ${technologyPrefix}MetaModel> {

	@Override
	public ${technologyPrefix}TechnologyAdapter getTechnologyAdapter();

	public static abstract class ${technologyPrefix}TypeAwareModelSlotImpl extends TypeAwareModelSlotImpl<${technologyPrefix}Model, ${technologyPrefix}MetaModel> implements ${technologyPrefix}TypeAwareModelSlot {

		private static final Logger logger = Logger.getLogger(${technologyPrefix}TypeAwareModelSlot.class.getPackage().getName());

		@Override
		public Class<${technologyPrefix}TechnologyAdapter> getTechnologyAdapterClass() {
			return ${technologyPrefix}TechnologyAdapter.class;
		}

		/**
		 * Instanciate a new model slot instance configuration for this model slot
		 */
		@Override
		public ${technologyPrefix}TypeAwareModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
			return new ${technologyPrefix}TypeAwareModelSlotInstanceConfiguration(this, action);
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (${technologyPrefix}ObjectIndividualRole.class.isAssignableFrom(patternRoleClass)) {
				return "individual";
			}
			return null;
		}

		@Override
		public String getURIForObject(
				TypeAwareModelSlotInstance<${technologyPrefix}Model, ${technologyPrefix}MetaModel, ? extends TypeAwareModelSlot<${technologyPrefix}Model, ${technologyPrefix}MetaModel>> msInstance,
				Object o) {
			if (o instanceof IFlexoOntologyObject) {
				return ((IFlexoOntologyObject) o).getURI();
			}
			return null;
		}

		@Override
		public Object retrieveObjectWithURI(
				TypeAwareModelSlotInstance<${technologyPrefix}Model, ${technologyPrefix}MetaModel, ? extends TypeAwareModelSlot<${technologyPrefix}Model, ${technologyPrefix}MetaModel>> msInstance,
				String objectURI) {
			return msInstance.getResourceData().getObject(objectURI);
		}

		@Override
		public Type getType() {
			return ${technologyPrefix}Model.class;
		}

		@Override
		public ${technologyPrefix}TechnologyAdapter getTechnologyAdapter() {
			return (${technologyPrefix}TechnologyAdapter) super.getTechnologyAdapter();
		}

		@Override
		public ${technologyPrefix}ModelResource createProjectSpecificEmptyModel(FlexoProject project, String filename, String modelUri,
				FlexoMetaModelResource<${technologyPrefix}Model, ${technologyPrefix}MetaModel, ?> metaModelResource) {
			return ((${technologyPrefix}TechnologyAdapter) getTechnologyAdapter()).createNew${technologyPrefix}Model(project, filename, modelUri, (${technologyPrefix}MetaModelResource) metaModelResource);
		}

		@Override
		public ${technologyPrefix}ModelResource createSharedEmptyModel(FlexoResourceCenter<?> resourceCenter, String relativePath, String filename,
				String modelUri, FlexoMetaModelResource<${technologyPrefix}Model, ${technologyPrefix}MetaModel, ?> metaModelResource) {
			return ((${technologyPrefix}TechnologyAdapter) getTechnologyAdapter()).createNew${technologyPrefix}Model((FileSystemBasedResourceCenter) resourceCenter, relativePath, filename,
					modelUri, (${technologyPrefix}MetaModelResource) metaModelResource);
		}

		@Override
		public boolean isStrictMetaModelling() {
			return true;
		}

	}
}
