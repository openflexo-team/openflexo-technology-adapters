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

package org.openflexo.technologyadapter.gina;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import org.openflexo.connie.BindingModel;
import org.openflexo.connie.DataBinding;
import org.openflexo.foundation.fml.AbstractVirtualModel;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.technologyadapter.ModelSlotObject;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.DefineValidationRule;
import org.openflexo.model.annotations.DeserializationFinalizer;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.validation.ValidationError;
import org.openflexo.model.validation.ValidationIssue;
import org.openflexo.model.validation.ValidationRule;
import org.openflexo.technologyadapter.gina.fml.FIBComponentRole;
import org.openflexo.technologyadapter.gina.fml.editionaction.ConfigureGINAFIBComponent;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;
import org.openflexo.technologyadapter.gina.rm.GINAFIBComponentResource;
import org.openflexo.toolbox.StringUtils;

/**
 * A {@link ModelSlot} used to reference a {@link GINAFIBComponent}
 * 
 * @author Sylvain Guérin
 * 
 */
@DeclareFlexoRoles({ FIBComponentRole.class })
@DeclareEditionActions({ ConfigureGINAFIBComponent.class })
@DeclareFetchRequests({})
@ModelEntity
@ImplementationClass(FIBComponentModelSlot.FIBComponentModelSlotImpl.class)
@XMLElement
public interface FIBComponentModelSlot extends FreeModelSlot<GINAFIBComponent> {

	@PropertyIdentifier(type = String.class)
	public static final String TEMPLATE_COMPONENT_URI_KEY = "templateComponentURI";
	@PropertyIdentifier(type = FlexoResource.class)
	public static final String TEMPLATE_RESOURCE_KEY = "templateResource";
	@PropertyIdentifier(type = VariableAssignment.class, cardinality = Cardinality.LIST)
	public static final String ASSIGNMENTS_KEY = "assignments";

	@Getter(value = TEMPLATE_COMPONENT_URI_KEY)
	@XMLAttribute
	public String getTemplateComponentURI();

	@Setter(TEMPLATE_COMPONENT_URI_KEY)
	public void setTemplateComponentURI(String templateComponentURI);

	@Getter(TEMPLATE_RESOURCE_KEY)
	public GINAFIBComponentResource getTemplateResource();

	@Setter(TEMPLATE_RESOURCE_KEY)
	public void setTemplateResource(GINAFIBComponentResource templateResource);

	@Getter(value = ASSIGNMENTS_KEY, cardinality = Cardinality.LIST, inverse = VariableAssignment.OWNER_KEY)
	@XMLElement
	@CloningStrategy(StrategyType.CLONE)
	public List<VariableAssignment> getAssignments();

	@Setter(ASSIGNMENTS_KEY)
	public void setAssignments(List<VariableAssignment> assignments);

	@Adder(ASSIGNMENTS_KEY)
	public void addToAssignments(VariableAssignment aAssignment);

	@Remover(ASSIGNMENTS_KEY)
	public void removeFromAssignments(VariableAssignment aAssignment);

	public VariableAssignment createAssignment();

	public VariableAssignment deleteAssignment(VariableAssignment assignment);

	@Override
	public GINATechnologyAdapter getModelSlotTechnologyAdapter();

	public static abstract class FIBComponentModelSlotImpl extends FreeModelSlotImpl<GINAFIBComponent> implements FIBComponentModelSlot {

		protected String templateComponentURI;
		private GINAFIBComponentResource templateResource;

		@Override
		public Class<GINATechnologyAdapter> getTechnologyAdapterClass() {
			return GINATechnologyAdapter.class;
		}

		/**
		 * Instanciate a new model slot instance configuration for this model slot
		 */
		@Override
		public ModelSlotInstanceConfiguration<? extends FreeModelSlot<GINAFIBComponent>, GINAFIBComponent> createConfiguration(
				FlexoConceptInstance fci, FlexoResourceCenter<?> rc) {
			return new FIBComponentModelSlotInstanceConfiguration(this, fci, rc);
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> flexoRoleClass) {
			if (FIBComponentRole.class.isAssignableFrom(flexoRoleClass)) {
				return "component";
			}
			return "";
		}

		@Override
		public Type getType() {
			return GINAFIBComponent.class;
		}

		@Override
		public GINATechnologyAdapter getModelSlotTechnologyAdapter() {
			return (GINATechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

		@Override
		public String getTemplateComponentURI() {
			if (getTemplateResource() != null) {
				return getTemplateResource().getURI();
			}
			return templateComponentURI;
		}

		@Override
		public void setTemplateComponentURI(String templateComponentURI) {
			if ((templateComponentURI == null && this.templateComponentURI != null)
					|| (templateComponentURI != null && !templateComponentURI.equals(this.templateComponentURI))) {
				String oldValue = this.templateComponentURI;
				this.templateComponentURI = templateComponentURI;
				getPropertyChangeSupport().firePropertyChange("templateComponentURI", oldValue, templateComponentURI);
			}
		}

		@Override
		public GINAFIBComponentResource getTemplateResource() {
			if (templateResource == null && StringUtils.isNotEmpty(templateComponentURI)
					&& getServiceManager().getResourceManager() != null) {
				// System.out.println("Looking up " + templateDocumentURI);
				templateResource = (GINAFIBComponentResource) getServiceManager().getResourceManager().getResource(templateComponentURI);
				// System.out.println("templateResource = " + returned);
				// for (FlexoResource r : getServiceManager().getResourceManager().getRegisteredResources()) {
				// System.out.println("> " + r.getURI());
				// }
			}
			return templateResource;
		}

		@Override
		public void setTemplateResource(GINAFIBComponentResource templateResource) {
			if (templateResource != this.templateResource) {
				GINAFIBComponentResource oldValue = this.templateResource;
				this.templateResource = templateResource;
				this.templateComponentURI = null;
				getPropertyChangeSupport().firePropertyChange("templateResource", oldValue, templateResource);
			}
		}

		@Override
		public VariableAssignment createAssignment() {
			System.out.println("Called createAssignment()");
			VariableAssignment newAssignment = getFMLModelFactory().newInstance(VariableAssignment.class);
			newAssignment.setVariable("data" + (getAssignments().size() > 0 ? getAssignments().size() + 1 : ""));
			addToAssignments(newAssignment);
			return newAssignment;
		}

		@Override
		public VariableAssignment deleteAssignment(VariableAssignment assignment) {
			System.out.println("Called deleteAssignment() with " + assignment);
			removeFromAssignments(assignment);
			return assignment;
		}

	}

	@ModelEntity
	@ImplementationClass(VariableAssignment.VariableAssignmentImpl.class)
	@XMLElement(xmlTag = "VariableAssignment")
	public static interface VariableAssignment extends FMLObject, ModelSlotObject<GINAFIBComponent> {
		@PropertyIdentifier(type = FIBComponentModelSlot.class)
		public static final String OWNER_KEY = "owner";
		@PropertyIdentifier(type = String.class)
		public static final String VARIABLE_KEY = "variable";
		@PropertyIdentifier(type = String.class)
		public static final String VARIABLE_TYPE_KEY = "variableType";
		@PropertyIdentifier(type = DataBinding.class)
		public static final String VALUE_KEY = "value";
		@PropertyIdentifier(type = Boolean.class)
		public static final String MANDATORY_KEY = "mandatory";

		@Getter(value = OWNER_KEY)
		@CloningStrategy(StrategyType.IGNORE)
		public FIBComponentModelSlot getOwner();

		@Setter(OWNER_KEY)
		public void setOwner(FIBComponentModelSlot customColumn);

		@Getter(value = VARIABLE_KEY)
		@XMLAttribute
		public String getVariable();

		@Setter(VARIABLE_KEY)
		public void setVariable(String variable);

		@Getter(value = VARIABLE_TYPE_KEY, isStringConvertable = true)
		@XMLAttribute
		public Type getVariableType();

		@Setter(VARIABLE_TYPE_KEY)
		public void setVariableType(Type type);

		@Getter(value = VALUE_KEY)
		@XMLAttribute
		public DataBinding<Object> getValue();

		@Setter(VALUE_KEY)
		public void setValue(DataBinding<Object> value);

		@Getter(value = MANDATORY_KEY, defaultValue = "false")
		@XMLAttribute
		public boolean isMandatory();

		@Setter(MANDATORY_KEY)
		public void setMandatory(boolean mandatory);

		@Override
		@DeserializationFinalizer
		public void finalizeDeserialization();

		public static abstract class VariableAssignmentImpl extends FMLObjectImpl implements VariableAssignment {

			private DataBinding<Object> value;

			@Override
			public void setOwner(FIBComponentModelSlot referencedComponent) {
				performSuperSetter(OWNER_KEY, referencedComponent);
				if (value != null) {
					value.setOwner(referencedComponent);
				}
			}

			@Override
			public DataBinding<Object> getValue() {
				if (value == null) {
					value = new DataBinding<>(getOwner(), Object.class, DataBinding.BindingDefinitionType.GET);
				}
				return value;
			}

			@Override
			public void setValue(DataBinding<Object> value) {
				if (value != null) {
					value.setOwner(getOwner()); // Warning, still null while deserializing
					value.setDeclaredType(Object.class);
					value.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
					this.value = value;
				}
				else {
					getValue();
				}
			}

			@Override
			public void finalizeDeserialization() {

				super.finalizeDeserialization();

				if (value != null) {
					value.setOwner(getOwner());
					value.decode();
				}
			}

			@Override
			public BindingModel getBindingModel() {
				if (getOwner() != null) {
					return getOwner().getBindingModel();
				}
				return null;
			}

			@Override
			public AbstractVirtualModel<?> getResourceData() {
				if (getOwner() != null) {
					return getOwner().getResourceData();
				}
				return null;
			}

			@Override
			public ModelSlot<GINAFIBComponent> getModelSlot() {
				return getOwner();
			}

			@Override
			public TechnologyAdapter getModelSlotTechnologyAdapter() {
				return getOwner().getModelSlotTechnologyAdapter();
			}

		}
	}

	@DefineValidationRule
	class FIBComponentModelSlotMustReferenceNonNullTemplateResource
			extends ValidationRule<FIBComponentModelSlotMustReferenceNonNullTemplateResource, FIBComponentModelSlot> {
		public FIBComponentModelSlotMustReferenceNonNullTemplateResource() {
			super(FIBComponentModelSlot.class, "fib_component_model_slot_must_reference_nonnull_template_resource");
		}

		@Override
		public ValidationIssue<FIBComponentModelSlotMustReferenceNonNullTemplateResource, FIBComponentModelSlot> applyValidation(
				FIBComponentModelSlot modelSlot) {
			if (modelSlot.getTemplateResource() == null) {
				return new ValidationError<>(this, modelSlot, "fib_component_model_slot_must_reference_nonnull_template_resource",
						Collections.emptyList());
			}
			return null;
		}
	}

}
