/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * Copyright (c) 2012, THALES SYSTEMES AEROPORTES - All Rights Reserved
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

package org.openflexo.technologyadapter.emf.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.SetDataPropertyValueAction;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyDataProperty;
import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.foundation.ontology.IndividualOfClass;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.emf.EMFModelSlot;
import org.openflexo.technologyadapter.emf.metamodel.EMFAttributeDataProperty;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividualAttributeDataPropertyValue;
import org.openflexo.toolbox.StringUtils;

/**
 * Assign a simple DataType value to the attribute of an object.
 * 
 * @author gbesancon
 * 
 */
@FIBPanel("Fib/AddEMFObjectIndividualAttributeDataPropertyValuePanel.fib")
@ModelEntity
@ImplementationClass(AddEMFObjectIndividualAttributeDataPropertyValue.AddEMFObjectIndividualAttributeDataPropertyValueImpl.class)
@XMLElement
@FML("AddEMFObjectIndividualAttributeDataPropertyValue")
public interface AddEMFObjectIndividualAttributeDataPropertyValue extends
		SetEMFPropertyValue<EMFObjectIndividualAttributeDataPropertyValue>,
		SetDataPropertyValueAction<EMFObjectIndividualAttributeDataPropertyValue> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String VALUE_KEY = "value";
	@PropertyIdentifier(type = String.class)
	public static final String DATA_PROPERTY_URI_KEY = "dataPropertyURI";

	@Override
	@Getter(value = VALUE_KEY)
	@XMLAttribute
	public DataBinding<?> getValue();

	@Override
	@Setter(VALUE_KEY)
	public void setValue(DataBinding<?> value);

	@Getter(value = DATA_PROPERTY_URI_KEY)
	@XMLAttribute
	public String _getDataPropertyURI();

	@Setter(DATA_PROPERTY_URI_KEY)
	public void _setDataPropertyURI(String dataPropertyURI);

	public static abstract class AddEMFObjectIndividualAttributeDataPropertyValueImpl extends
			SetEMFPropertyValueImpl<EMFObjectIndividualAttributeDataPropertyValue> implements
			AddEMFObjectIndividualAttributeDataPropertyValue {

		private String dataPropertyURI = null;
		private DataBinding<?> value;

		/**
		 * Constructor.
		 * 
		 * @param builder
		 */
		public AddEMFObjectIndividualAttributeDataPropertyValueImpl() {
			super();
		}

		@Override
		public Type getSubjectType() {
			if (getDataProperty() != null && getDataProperty().getDomain() instanceof IFlexoOntologyClass) {
				return IndividualOfClass.getIndividualOfClass((IFlexoOntologyClass) getDataProperty().getDomain());
			}
			return super.getSubjectType();
		}

		@Override
		public IFlexoOntologyStructuralProperty getProperty() {
			return getDataProperty();
		}

		@Override
		public void setProperty(IFlexoOntologyStructuralProperty aProperty) {
			setDataProperty((EMFAttributeDataProperty) aProperty);
		}

		@Override
		public IFlexoOntologyDataProperty getDataProperty() {
			if (getOwningVirtualModel() != null && StringUtils.isNotEmpty(dataPropertyURI)) {
				return getOwningVirtualModel().getOntologyDataProperty(dataPropertyURI);
			}
			return null;
		}

		@Override
		public void setDataProperty(IFlexoOntologyDataProperty ontologyProperty) {
			if (ontologyProperty != null) {
				dataPropertyURI = ontologyProperty.getURI();
			} else {
				dataPropertyURI = null;
			}
		}

		@Override
		public String _getDataPropertyURI() {
			if (getDataProperty() != null) {
				return getDataProperty().getURI();
			}
			return dataPropertyURI;
		}

		@Override
		public void _setDataPropertyURI(String dataPropertyURI) {
			this.dataPropertyURI = dataPropertyURI;
		}

		public Object getValue(FlexoBehaviourAction action) {
			try {
				return getValue().getBindingValue(action);
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}

		public Type getType() {
			if (getDataProperty() != null) {
				return getDataProperty().getRange().getAccessedType();
			}
			return Object.class;
		};

		@Override
		public DataBinding<?> getValue() {
			if (value == null) {
				value = new DataBinding<Object>(this, getType(), BindingDefinitionType.GET) {
					@Override
					public Type getDeclaredType() {
						return getType();
					}
				};
				value.setBindingName("value");
			}
			return value;
		}

		@Override
		public void setValue(DataBinding<?> value) {
			if (value != null) {
				value = new DataBinding<Object>(value.toString(), this, getType(), BindingDefinitionType.GET) {
					@Override
					public Type getDeclaredType() {
						return getType();
					}
				};
				value.setBindingName("value");
			}
			this.value = value;
		}

		@Override
		public Type getAssignableType() {
			return Object.class;
		}

		/**
		 * Follow the link.
		 * 
		 * @see org.openflexo.foundation.fml.editionaction.EditionAction#execute(org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction)
		 */
		@Override
		public EMFObjectIndividualAttributeDataPropertyValue execute(FlexoBehaviourAction action) {
			EMFObjectIndividualAttributeDataPropertyValue result = null;
			TypeAwareModelSlotInstance<EMFModel, EMFMetaModel, EMFModelSlot> modelSlotInstance = getModelSlotInstance(action);
			EMFModel model = modelSlotInstance.getAccessedResourceData();
			// // Add Attribute in EMF
			getSubject(action).getObject().eSet(((EMFAttributeDataProperty) getDataProperty()).getObject(), getValue(action));
			// // Instanciate Wrapper
			// result = model.getConverter().convertObjectIndividualAttributeDataPropertyValue(model, objectIndividual.getObject(),
			// attributeDataProperty.getObject());
			return result;
		}

	}
}
