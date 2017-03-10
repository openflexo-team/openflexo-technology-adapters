/**
 * 
 * Copyright (c) 2013-2015, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
 * 
 * This file is part of Emfconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.emf;

import java.io.File;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareActorReferences;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoBehaviourParameters;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.annotations.DeclareInspectorEntries;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.AbstractVirtualModelInstance;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.ontology.IFlexoOntologyObject;
import org.openflexo.foundation.ontology.fml.ClassParameter;
import org.openflexo.foundation.ontology.fml.IndividualParameter;
import org.openflexo.foundation.ontology.fml.PropertyParameter;
import org.openflexo.foundation.ontology.fml.inspector.ClassInspectorEntry;
import org.openflexo.foundation.ontology.fml.inspector.DataPropertyInspectorEntry;
import org.openflexo.foundation.ontology.fml.inspector.IndividualInspectorEntry;
import org.openflexo.foundation.ontology.fml.inspector.ObjectPropertyInspectorEntry;
import org.openflexo.foundation.ontology.fml.inspector.PropertyInspectorEntry;
import org.openflexo.foundation.ontology.fml.rt.ConceptActorReference;
import org.openflexo.foundation.ontology.technologyadapter.FlexoOntologyModelSlot;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.emf.fml.EMFClassClassRole;
import org.openflexo.technologyadapter.emf.fml.EMFEnumClassRole;
import org.openflexo.technologyadapter.emf.fml.EMFObjectIndividualRole;
import org.openflexo.technologyadapter.emf.fml.editionaction.AddEMFObjectIndividual;
import org.openflexo.technologyadapter.emf.fml.editionaction.SelectEMFObjectIndividual;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource;
import org.openflexo.technologyadapter.emf.rm.EMFModelResource;
import org.openflexo.technologyadapter.emf.rm.EMFModelResourceFactory;

/**
 * Implementation of the ModelSlot class for the EMF technology adapter<br>
 * We expect here to connect an EMF model conform to an EMFMetaModel
 * 
 * @author sylvain
 * 
 */
@DeclareFlexoRoles({ EMFObjectIndividualRole.class, EMFClassClassRole.class, EMFEnumClassRole.class })
@DeclareEditionActions({ AddEMFObjectIndividual.class })
@DeclareFetchRequests({ SelectEMFObjectIndividual.class })
@DeclareFlexoBehaviourParameters({ ClassParameter.class, IndividualParameter.class, PropertyParameter.class })
@DeclareInspectorEntries({ ClassInspectorEntry.class, IndividualInspectorEntry.class, PropertyInspectorEntry.class,
		DataPropertyInspectorEntry.class, ObjectPropertyInspectorEntry.class })
@DeclareActorReferences({ ConceptActorReference.class })
@ModelEntity
@ImplementationClass(EMFModelSlot.EMFModelSlotImpl.class)
@XMLElement
@FML("EMFModelSlot")
public interface EMFModelSlot extends FlexoOntologyModelSlot<EMFModel, EMFMetaModel, EMFTechnologyAdapter> {

	@Override
	public EMFTechnologyAdapter getModelSlotTechnologyAdapter();

	public static abstract class EMFModelSlotImpl extends FlexoOntologyModelSlotImpl<EMFModel, EMFMetaModel, EMFTechnologyAdapter>
			implements EMFModelSlot {

		private static final Logger logger = Logger.getLogger(EMFModelSlot.class.getPackage().getName());

		@Override
		public Class<EMFTechnologyAdapter> getTechnologyAdapterClass() {
			return EMFTechnologyAdapter.class;
		}

		/**
		 * Instanciate a new model slot instance configuration for this model slot
		 */
		@Override
		public EMFModelSlotInstanceConfiguration createConfiguration(AbstractVirtualModelInstance<?, ?> virtualModelInstance,
				FlexoResourceCenter<?> rc) {
			return new EMFModelSlotInstanceConfiguration(this, virtualModelInstance, rc);
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (EMFObjectIndividualRole.class.isAssignableFrom(patternRoleClass)) {
				return "individual";
			}
			return null;
		}

		@Override
		public String getURIForObject(
				TypeAwareModelSlotInstance<EMFModel, EMFMetaModel, ? extends TypeAwareModelSlot<EMFModel, EMFMetaModel>> msInstance,
				Object o) {
			if (o instanceof IFlexoOntologyObject) {
				return ((IFlexoOntologyObject) o).getURI();
			}
			return null;
		}

		@Override
		public Object retrieveObjectWithURI(
				TypeAwareModelSlotInstance<EMFModel, EMFMetaModel, ? extends TypeAwareModelSlot<EMFModel, EMFMetaModel>> msInstance,
				String objectURI) {
			if (msInstance.getAccessedResourceData() != null) {
				return msInstance.getAccessedResourceData().getObject(objectURI);
			}
			return null;
		}

		@Override
		public Type getType() {
			return EMFModel.class;
		}

		@Override
		public String getTypeDescription() {
			return "EMF Model";
		};

		@Override
		public EMFTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (EMFTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

		@Override
		public EMFModelResource createProjectSpecificEmptyModel(FlexoResourceCenter<?> rc, String filename, String relativePath,
				String modelUri, FlexoMetaModelResource<EMFModel, EMFMetaModel, ?> metaModelResource) {

			EMFTechnologyAdapter emfTA = getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(EMFTechnologyAdapter.class);
			EMFModelResourceFactory factory = getModelSlotTechnologyAdapter().getEMFModelResourceFactory();

			Object serializationArtefact = emfTA.retrieveResourceSerializationArtefact(rc, filename, relativePath,
					((EMFMetaModelResource) metaModelResource).getModelFileExtension());

			EMFModelResource newEMFModelResource;
			try {
				newEMFModelResource = factory.makeResource(serializationArtefact, (FlexoResourceCenter) rc,
						emfTA.getTechnologyContextManager(), filename, modelUri, true);
				newEMFModelResource.setMetaModelResource((FlexoMetaModelResource) metaModelResource);
				return newEMFModelResource;
			} catch (SaveResourceException e) {
				e.printStackTrace();
			} catch (ModelDefinitionException e) {
				e.printStackTrace();
			}
			return null;

		}

		@Override
		public EMFModelResource createSharedEmptyModel(FlexoResourceCenter<?> resourceCenter, String relativePath, String filename,
				String modelUri, FlexoMetaModelResource<EMFModel, EMFMetaModel, ?> metaModelResource) {

			if (resourceCenter instanceof FileSystemBasedResourceCenter) {
				File modelDirectory = new File(((FileSystemBasedResourceCenter) resourceCenter).getRootDirectory(), relativePath);
				File modelFile = new File(modelDirectory, filename);
				try {
					return getModelSlotTechnologyAdapter().getEMFModelResourceFactory().makeResource(modelFile,
							(FlexoResourceCenter<File>) resourceCenter, getModelSlotTechnologyAdapter().getTechnologyContextManager(),
							true);
				} catch (SaveResourceException e) {
					e.printStackTrace();
				} catch (ModelDefinitionException e) {
					e.printStackTrace();
				}
			}

			return null;

			// return getModelSlotTechnologyAdapter().createNewEMFModel((FileSystemBasedResourceCenter) resourceCenter, relativePath,
			// filename, modelUri, (EMFMetaModelResource) metaModelResource);
		}

		@Override
		public boolean isStrictMetaModelling() {
			return true;
		}

	}
}
