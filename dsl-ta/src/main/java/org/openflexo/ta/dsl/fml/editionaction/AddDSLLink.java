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

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.action.InvalidParametersException;
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
import org.openflexo.ta.dsl.model.DSLLink;
import org.openflexo.ta.dsl.model.DSLSlot;
import org.openflexo.ta.dsl.model.DSLSystem;

@ModelEntity
@ImplementationClass(AddDSLLink.AddDSLLinkImpl.class)
@XMLElement
@FML("AddDSLLink")
public interface AddDSLLink extends DSLAction<DSLLink> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String COMPONENT_NAME_KEY = "componentName";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String FROM_SLOT_KEY = "fromSlot";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String TO_SLOT_KEY = "toSlot";

	@Getter(value = COMPONENT_NAME_KEY)
	@XMLAttribute
	public DataBinding<String> getComponentName();

	@Setter(COMPONENT_NAME_KEY)
	public void setComponentName(DataBinding<String> componentName);

	@Getter(value = FROM_SLOT_KEY)
	@XMLAttribute
	public DataBinding<DSLSlot> getFromSlot();

	@Setter(FROM_SLOT_KEY)
	public void setFromSlot(DataBinding<DSLSlot> slot);

	@Getter(value = TO_SLOT_KEY)
	@XMLAttribute
	public DataBinding<DSLSlot> getToSlot();

	@Setter(TO_SLOT_KEY)
	public void setToSlot(DataBinding<DSLSlot> slot);

	public static abstract class AddDSLLinkImpl extends TechnologySpecificActionDefiningReceiverImpl<DSLModelSlot, DSLSystem, DSLLink>
			implements AddDSLLink {

		private static final Logger logger = Logger.getLogger(AddDSLLink.class.getPackage().getName());

		private DataBinding<String> componentName;
		private DataBinding<DSLSlot> fromSlot;
		private DataBinding<DSLSlot> toSlot;

		@Override
		public Type getAssignableType() {
			return DSLLink.class;
		}

		@Override
		public DSLLink execute(RunTimeEvaluationContext evaluationContext) throws InvalidParametersException {

			DSLLink newLink = null;

			DSLSystem resourceData = getReceiver(evaluationContext);

			try {
				if (resourceData == null) {
					throw new InvalidParametersException("Cannot create component in null DSLSystem");
				}
				String componentName = getComponentName().getBindingValue(evaluationContext);
				if (componentName == null) {
					throw new InvalidParametersException("Create a component requires a name");
				}
				DSLSlot fromSlot = getFromSlot().getBindingValue(evaluationContext);
				if (fromSlot == null) {
					throw new InvalidParametersException("Create a component requires a start DSLSlot");
				}
				DSLSlot toSlot = getToSlot().getBindingValue(evaluationContext);
				if (toSlot == null) {
					throw new InvalidParametersException("Create a component requires a end DSLSlot");
				}
				newLink = resourceData.getFactory().makeDSLLink(componentName, fromSlot, toSlot);
				resourceData.addToLinks(newLink);

			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}

			return null;

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

		@Override
		public DataBinding<DSLSlot> getFromSlot() {
			if (fromSlot == null) {
				fromSlot = new DataBinding<>(this, DSLSlot.class, DataBinding.BindingDefinitionType.GET);
				fromSlot.setBindingName("fromSlot");
			}
			return fromSlot;
		}

		@Override
		public void setFromSlot(DataBinding<DSLSlot> fromSlot) {
			if (fromSlot != null) {
				fromSlot.setOwner(this);
				fromSlot.setDeclaredType(DSLSlot.class);
				fromSlot.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				fromSlot.setBindingName("fromSlot");
			}
			this.fromSlot = fromSlot;
		}

		@Override
		public DataBinding<DSLSlot> getToSlot() {
			if (toSlot == null) {
				toSlot = new DataBinding<>(this, DSLSlot.class, DataBinding.BindingDefinitionType.GET);
				toSlot.setBindingName("toSlot");
			}
			return fromSlot;
		}

		@Override
		public void setToSlot(DataBinding<DSLSlot> toSlot) {
			if (toSlot != null) {
				toSlot.setOwner(this);
				toSlot.setDeclaredType(DSLSlot.class);
				toSlot.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				toSlot.setBindingName("toSlot");
			}
			this.toSlot = toSlot;
		}

	}

	@DefineValidationRule
	public static class ComponentNameBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddDSLLink> {
		public ComponentNameBindingIsRequiredAndMustBeValid() {
			super("'componentName'_binding_is_required_and_must_be_valid", AddDSLLink.class);
		}

		@Override
		public DataBinding<String> getBinding(AddDSLLink object) {
			return object.getComponentName();
		}

	}

}
