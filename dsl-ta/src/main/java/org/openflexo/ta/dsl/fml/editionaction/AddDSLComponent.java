/**
 * 
 * Copyright (c) 2018, Openflexo
 * 
 * This file is part of OpenflexoTechnologyAdapter, a component of the software infrastructure 
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

package org.openflexo.ta.dsl.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.pamela.annotations.DefineValidationRule;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.ta.dsl.DSLModelSlot;
import org.openflexo.ta.dsl.model.DSLComponent;
import org.openflexo.ta.dsl.model.DSLSystem;

@ModelEntity
@ImplementationClass(AddDSLComponent.AddDSLComponentImpl.class)
@XMLElement
@FML("AddDSLComponent")
public interface AddDSLComponent extends DSLAction<DSLComponent> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String COMPONENT_NAME_KEY = "componentName";

	@Getter(value = COMPONENT_NAME_KEY)
	@XMLAttribute
	public DataBinding<String> getComponentName();

	@Setter(COMPONENT_NAME_KEY)
	public void setComponentName(DataBinding<String> componentName);

	public static abstract class AddDSLComponentImpl
			extends TechnologySpecificActionDefiningReceiverImpl<DSLModelSlot, DSLSystem, DSLComponent> implements AddDSLComponent {

		private static final Logger logger = Logger.getLogger(AddDSLComponent.class.getPackage().getName());

		private DataBinding<String> componentName;

		@Override
		public Type getAssignableType() {
			return DSLComponent.class;
		}

		@Override
		public DSLComponent execute(RunTimeEvaluationContext evaluationContext) {

			DSLComponent newComponent = null;

			DSLSystem resourceData = getReceiver(evaluationContext);

			try {
				if (resourceData != null) {
					String componentName = getComponentName().getBindingValue(evaluationContext);
					if (componentName != null) {
						newComponent = resourceData.getFactory().makeDSLComponent(componentName);
						resourceData.addToComponents(newComponent);
					}
					else {
						logger.warning("Create a component requires a name");
					}
				}
				else {
					logger.warning("Cannot create component in null resource data");
				}

			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			return newComponent;

		}

		@Override
		public DataBinding<String> getComponentName() {
			if (componentName == null) {
				componentName = new DataBinding<>(this, String.class, DataBinding.BindingDefinitionType.GET);
				componentName.setBindingName("componentName");
			}
			return componentName;
		}

		@Override
		public void setComponentName(DataBinding<String> componentName) {
			if (componentName != null) {
				componentName.setOwner(this);
				componentName.setDeclaredType(String.class);
				componentName.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				componentName.setBindingName("componentName");
			}
			this.componentName = componentName;
		}

	}

	@DefineValidationRule
	public static class ComponentNameBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddDSLComponent> {
		public ComponentNameBindingIsRequiredAndMustBeValid() {
			super("'componentName'_binding_is_required_and_must_be_valid", AddDSLComponent.class);
		}

		@Override
		public DataBinding<String> getBinding(AddDSLComponent object) {
			return object.getComponentName();
		}

	}

}
