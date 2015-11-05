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

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.eclipse.lyo.oslc4j.core.model.CreationFactory;
import org.openflexo.connie.DataBinding;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.oslc.OSLCCoreModelSlot;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProviderCatalog;

@FIBPanel("Fib/AddOSLCResourcePanel.fib")
@ModelEntity
@ImplementationClass(AddOSLCResource.AddOSLCResourceImpl.class)
@XMLElement
@FML("AddOSLCResource")
public interface AddOSLCResource extends OSLCCoreAction<OSLCResource> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String CREATION_FACTORY = "creationFactory";

	@Getter(value = CREATION_FACTORY)
	@XMLAttribute
	public DataBinding<CreationFactory> getCreationFactory();

	@Setter(CREATION_FACTORY)
	public void setCreationFactory(DataBinding<CreationFactory> creationFactory);

	public static abstract class AddOSLCResourceImpl extends TechnologySpecificActionImpl<OSLCCoreModelSlot, OSLCResource> implements
			AddOSLCResource {

		private DataBinding<CreationFactory> creationFactory;

		private static final Logger logger = Logger.getLogger(AddOSLCResource.class.getPackage().getName());

		public AddOSLCResourceImpl() {
			super();
		}

		@Override
		public Type getAssignableType() {
			return OSLCResource.class;
		}

		@Override
		public OSLCResource execute(RunTimeEvaluationContext evaluationContext) {

			OSLCResource resource = null;

			return resource;
		}

		@Override
		public FreeModelSlotInstance<OSLCServiceProviderCatalog, OSLCCoreModelSlot> getModelSlotInstance(
				RunTimeEvaluationContext evaluationContext) {
			return (FreeModelSlotInstance<OSLCServiceProviderCatalog, OSLCCoreModelSlot>) super.getModelSlotInstance(evaluationContext);
		}

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
