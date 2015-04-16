/**
 * 
 * Copyright (c) 2015-2015, Openflexo
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

import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.emf.fml.EMFClassClassRole;
import org.openflexo.technologyadapter.emf.fml.EMFEnumClassRole;
import org.openflexo.technologyadapter.emf.fml.EMFObjectIndividualRole;
import org.openflexo.technologyadapter.emf.fml.editionaction.AddEMFObjectIndividual;
import org.openflexo.technologyadapter.emf.fml.editionaction.RemoveEMFObjectIndividual;
import org.openflexo.technologyadapter.emf.fml.editionaction.SelectEMFObjectIndividual;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource;

/**
 * Implementation of the ModelSlot class for the EMF technology adapter<br>
 * We expect here to connect an EMF model conform to the UML MetaModel
 * 
 * @author sylvain
 * 
 */
@DeclareFlexoRoles({ EMFObjectIndividualRole.class, EMFClassClassRole.class, EMFEnumClassRole.class })
@DeclareEditionActions({ AddEMFObjectIndividual.class, RemoveEMFObjectIndividual.class })
@DeclareFetchRequests({ SelectEMFObjectIndividual.class })
@ModelEntity
@ImplementationClass(UMLEMFModelSlot.UMLEMFModelSlotImpl.class)
@XMLElement
@FML("UMLEMFModelSlot")
public interface UMLEMFModelSlot extends EMFModelSlot{


	public static abstract class UMLEMFModelSlotImpl extends EMFModelSlotImpl implements UMLEMFModelSlot {

		private static final Logger logger = Logger.getLogger(UMLEMFModelSlot.class.getPackage().getName());

		/** whatever, URI is UML's Metamodel **/

		@Override
		@Getter(value = META_MODEL_URI_KEY)
		@XMLAttribute
		public String getMetaModelURI(){
			return EMFTechnologyAdapter.UML_MM_URI;
		}

		@Override
		@Setter(META_MODEL_URI_KEY)
		public void setMetaModelURI(String metaModelURI){
			// TODO: do best here sometime.
			logger.warning("You cannot override MetaModel URI here, I will use the default URI for UML");
			performSuperSetter(META_MODEL_URI_KEY,EMFTechnologyAdapter.UML_MM_URI);
		}
		
		@Override
		public  FlexoMetaModelResource<EMFModel, EMFMetaModel, EMFTechnologyAdapter> getMetaModelResource(){
			return  ((EMFTechnologyAdapter) getModelSlotTechnologyAdapter()).getTechnologyContextManager().getMetaModelResourceByURI(EMFTechnologyAdapter.UML_MM_URI);
		}

		@Override
		public void setMetaModelResource(FlexoMetaModelResource<EMFModel, EMFMetaModel, ?> metaModelResource) {
			EMFMetaModelResource umlMetamodelREsource = ((EMFTechnologyAdapter) getModelSlotTechnologyAdapter()).getTechnologyContextManager().getMetaModelResourceByURI(EMFTechnologyAdapter.UML_MM_URI);
			if (metaModelResource != umlMetamodelREsource) {
				logger.warning("You cannot override MetaModel Resource here, I will use the default MetaModelResource for UML");
			}
			super.setMetaModelResource(umlMetamodelREsource);
		}
		
		
		/**
		 * Instanciate a new model slot instance configuration for this model slot
		 */
		@Override
		public UMLEMFModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
			return new UMLEMFModelSlotInstanceConfiguration(this, action);
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (EMFObjectIndividualRole.class.isAssignableFrom(patternRoleClass)) {
				return "individual";
			}
			return null;
		}

		@Override
		public boolean isStrictMetaModelling() {
			return true;
		}

	}
}
