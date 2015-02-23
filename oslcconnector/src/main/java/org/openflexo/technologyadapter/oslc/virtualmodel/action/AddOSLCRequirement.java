/**
 * 
 * Copyright (c) 2015, Openflexo
 * 
 * This file is part of Oslcconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.oslc.virtualmodel.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.eclipse.lyo.client.oslc.resources.Requirement;
import org.eclipse.lyo.oslc4j.core.model.CreationFactory;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.TechnologySpecificAction;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.oslc.OSLCRMModelSlot;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.model.io.FlexoOslcClient;
import org.openflexo.technologyadapter.oslc.model.rm.OSLCRequirement;
import org.openflexo.technologyadapter.oslc.rm.OSLCResourceResource;

@FIBPanel("Fib/AddOSLCRequirementPanel.fib")
@ModelEntity
@ImplementationClass(AddOSLCRequirement.AddOSLCRequirementImpl.class)
@XMLElement
@FML("AddOSLCRequirement")
public interface AddOSLCRequirement extends TechnologySpecificAction<OSLCRMModelSlot, OSLCResource> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String CREATION_FACTORY = "creationFactory";

	@Getter(value = CREATION_FACTORY)
	@XMLAttribute
	public DataBinding<CreationFactory> getCreationFactory();

	@Setter(CREATION_FACTORY)
	public void setCreationFactory(DataBinding<CreationFactory> creationFactory);

	public static abstract class AddOSLCRequirementImpl extends TechnologySpecificActionImpl<OSLCRMModelSlot, OSLCResource> implements
			AddOSLCRequirement {

		private static final Logger logger = Logger.getLogger(AddOSLCRequirement.class.getPackage().getName());

		private DataBinding<CreationFactory> creationFactory;

		public AddOSLCRequirementImpl() {
			super();
		}

		@Override
		public Type getAssignableType() {
			return OSLCRequirement.class;
		}

		@Override
		public OSLCRequirement execute(FlexoBehaviourAction action) {

			OSLCRequirement oslcRequirement = null;

			ModelSlotInstance<OSLCRMModelSlot, ?> msi = getModelSlotInstance(action);
			if (msi.getResourceData() != null) {

				try {
					OSLCResourceResource resource = (OSLCResourceResource) (msi.getAccessedResourceData().getResource());
					FlexoOslcClient oslcClient = new FlexoOslcClient(resource.getAdaptorConfiguration());
					Requirement requirement = new Requirement();
					requirement.setTitle("Test");
					oslcClient.create(getCreationFactory().getBindingValue(action), requirement, null);
					oslcRequirement = resource.getConverter().createOSLCResource(OSLCRequirement.class, requirement);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TypeMismatchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullReferenceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				logger.warning("Model slot not correctly initialised : model is null");
				return null;
			}

			return oslcRequirement;
		}

		@Override
		public ModelSlotInstance<OSLCRMModelSlot, ?> getModelSlotInstance(FlexoBehaviourAction<?, ?, ?> action) {
			// TODO Auto-generated method stub
			return super.getModelSlotInstance(action);
		}

		/*@Override
		public FreeModelSlotInstance<OSLCResource, OSLCRMModelSlot> getModelSlotInstance(FlexoBehaviourAction action) {
			return (FreeModelSlotInstance<OSLCResource, OSLCRMModelSlot>) super.getModelSlotInstance(action);
		}*/

		@Override
		public DataBinding<CreationFactory> getCreationFactory() {
			if (creationFactory == null) {
				creationFactory = new DataBinding<CreationFactory>(this, CreationFactory.class, DataBinding.BindingDefinitionType.GET);
				creationFactory.setBindingName("creationFactory");
			}
			return creationFactory;
		}

		@Override
		public void setCreationFactory(DataBinding<CreationFactory> creationFactory) {
			if (creationFactory != null) {
				creationFactory.setOwner(this);
				creationFactory.setDeclaredType(CreationFactory.class);
				creationFactory.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				creationFactory.setBindingName("creationFactory");
			}
			this.creationFactory = creationFactory;
		}
	}
}
