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

package org.openflexo.technologyadapter.csv;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.ontology.IFlexoOntologyObject;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.csv.CSVTechnologyAdapter;
import org.openflexo.technologyadapter.csv.metamodel.CSVMetaModel;
import org.openflexo.technologyadapter.csv.model.CSVModel;
import org.openflexo.technologyadapter.csv.rm.CSVMetaModelResource;
import org.openflexo.technologyadapter.csv.rm.CSVModelResource;
import org.openflexo.technologyadapter.csv.virtualmodel.CSVObjectIndividualRole;
import org.openflexo.technologyadapter.csv.virtualmodel.action.AddCSVObjectIndividual;
import org.openflexo.technologyadapter.csv.virtualmodel.action.SelectCSVObjectIndividual;

/**
 * Implementation of the ModelSlot class for the CSV technology adapter<br>
 * We expect here to connect an CSV model conform to an CSVMetaModel
 * 
 * @author Jean Le Paon
 * 
 */
@DeclareFlexoRoles({CSVObjectIndividualRole.class})
@DeclareEditionActions({AddCSVObjectIndividual.class})
@DeclareFetchRequests({SelectCSVObjectIndividual.class})
@ModelEntity
@ImplementationClass(CSVTypeAwareModelSlot.CSVTypeAwareModelSlotImpl.class)
@XMLElement
public interface CSVTypeAwareModelSlot extends TypeAwareModelSlot<CSVModel, CSVMetaModel> {

	@Override
	public CSVTechnologyAdapter getModelSlotTechnologyAdapter();

	public static abstract class CSVTypeAwareModelSlotImpl extends TypeAwareModelSlotImpl<CSVModel, CSVMetaModel> implements CSVTypeAwareModelSlot {

		private static final Logger logger = Logger.getLogger(CSVTypeAwareModelSlot.class.getPackage().getName());

		@Override
		public Class<CSVTechnologyAdapter> getTechnologyAdapterClass() {
			return CSVTechnologyAdapter.class;
		}

		/**
		 * Instanciate a new model slot instance configuration for this model slot
		 */
		@Override
		public CSVTypeAwareModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
			return new CSVTypeAwareModelSlotInstanceConfiguration(this, action);
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (CSVObjectIndividualRole.class.isAssignableFrom(patternRoleClass)) {
				return "individual";
			}
			return null;
		}

		@Override
		public String getURIForObject(
				TypeAwareModelSlotInstance<CSVModel, CSVMetaModel, ? extends TypeAwareModelSlot<CSVModel, CSVMetaModel>> msInstance,
				Object o) {
			if (o instanceof IFlexoOntologyObject) {
				return ((IFlexoOntologyObject) o).getURI();
			}
			return null;
		}

		@Override
		public Object retrieveObjectWithURI(
				TypeAwareModelSlotInstance<CSVModel, CSVMetaModel, ? extends TypeAwareModelSlot<CSVModel, CSVMetaModel>> msInstance,
				String objectURI) {
			return msInstance.getResourceData().getObject(objectURI);
		}

		@Override
		public Type getType() {
			return CSVModel.class;
		}

		@Override
		public CSVTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (CSVTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

		@Override
		public CSVModelResource createProjectSpecificEmptyModel(FlexoProject project, String filename, String modelUri,
				FlexoMetaModelResource<CSVModel, CSVMetaModel, ?> metaModelResource) {
			return ((CSVTechnologyAdapter) getModelSlotTechnologyAdapter()).createNewCSVModel(project, filename, modelUri, (CSVMetaModelResource) metaModelResource);
		}

		@Override
		public CSVModelResource createSharedEmptyModel(FlexoResourceCenter<?> resourceCenter, String relativePath, String filename,
				String modelUri, FlexoMetaModelResource<CSVModel, CSVMetaModel, ?> metaModelResource) {
			return ((CSVTechnologyAdapter) getModelSlotTechnologyAdapter()).createNewCSVModel((FileSystemBasedResourceCenter) resourceCenter, relativePath, filename,
					modelUri, (CSVMetaModelResource) metaModelResource);
		}

		@Override
		public boolean isStrictMetaModelling() {
			return true;
		}

	}
}
