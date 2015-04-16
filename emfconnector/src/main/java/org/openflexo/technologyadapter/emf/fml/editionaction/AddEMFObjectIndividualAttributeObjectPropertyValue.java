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
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.SetObjectPropertyValueAction;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyConcept;
import org.openflexo.foundation.ontology.IFlexoOntologyObjectProperty;
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
import org.openflexo.technologyadapter.emf.metamodel.EMFAttributeObjectProperty;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividual;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividualAttributeObjectPropertyValue;
import org.openflexo.toolbox.StringUtils;

/**
 * Assign an Enum value to the attribute of an object.
 * 
 * @author gbesancon
 * 
 */
@ModelEntity
@ImplementationClass(AddEMFObjectIndividualAttributeObjectPropertyValue.AddEMFObjectIndividualAttributeObjectPropertyValueImpl.class)
@XMLElement
@FML("AddEMFObjectIndividualAttributeObjectPropertyValue")
public interface AddEMFObjectIndividualAttributeObjectPropertyValue extends
		SetEMFPropertyValue<EMFObjectIndividualAttributeObjectPropertyValue>,
		SetObjectPropertyValueAction<EMFObjectIndividualAttributeObjectPropertyValue> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String OBJECT_KEY = "object";
	@PropertyIdentifier(type = String.class)
	public static final String OBJECT_PROPERTY_URI_KEY = "objectPropertyURI";

	@Override
	@Getter(value = OBJECT_KEY)
	@XMLAttribute
	public DataBinding<?> getObject();

	@Override
	@Setter(OBJECT_KEY)
	public void setObject(DataBinding<?> object);

	@Getter(value = OBJECT_PROPERTY_URI_KEY)
	@XMLAttribute
	public String _getObjectPropertyURI();

	@Setter(OBJECT_PROPERTY_URI_KEY)
	public void _setObjectPropertyURI(String objectPropertyURI);

	public static abstract class AddEMFObjectIndividualAttributeObjectPropertyValueImpl extends
			SetEMFPropertyValueImpl<EMFObjectIndividualAttributeObjectPropertyValue> implements
			AddEMFObjectIndividualAttributeObjectPropertyValue {

		private String objectPropertyURI = null;
		private DataBinding<?> object;

		/**
		 * Constructor.
		 * 
		 * @param builder
		 */
		public AddEMFObjectIndividualAttributeObjectPropertyValueImpl() {
			super();
		}

		@Override
		public Type getSubjectType() {
			if (getObjectProperty() != null && getObjectProperty().getDomain() instanceof IFlexoOntologyClass) {
				return IndividualOfClass.getIndividualOfClass((IFlexoOntologyClass) getObjectProperty().getDomain());
			}
			return super.getSubjectType();
		}

		@Override
		public IFlexoOntologyStructuralProperty getProperty() {
			return getObjectProperty();
		}

		@Override
		public void setProperty(IFlexoOntologyStructuralProperty aProperty) {
			setObjectProperty((EMFAttributeObjectProperty) aProperty);
		}

		@Override
		public IFlexoOntologyObjectProperty getObjectProperty() {
			if (getOwningVirtualModel() != null && StringUtils.isNotEmpty(objectPropertyURI)) {
				return getOwningVirtualModel().getOntologyObjectProperty(objectPropertyURI);
			}
			return null;
		}

		@Override
		public void setObjectProperty(IFlexoOntologyObjectProperty ontologyProperty) {
			if (ontologyProperty != null) {
				objectPropertyURI = ontologyProperty.getURI();
			} else {
				objectPropertyURI = null;
			}
		}

		@Override
		public String _getObjectPropertyURI() {
			if (getObjectProperty() != null) {
				return getObjectProperty().getURI();
			}
			return objectPropertyURI;
		}

		@Override
		public void _setObjectPropertyURI(String objectPropertyURI) {
			this.objectPropertyURI = objectPropertyURI;
		}

		public EMFObjectIndividual getObject(FlexoBehaviourAction action) {
			try {
				return (EMFObjectIndividual) getObject().getBindingValue(action);
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}

		public Type getObjectType() {
			if (getObjectProperty() != null && getObjectProperty().getRange() instanceof IFlexoOntologyClass) {
				return IndividualOfClass.getIndividualOfClass((IFlexoOntologyClass) getObjectProperty().getRange());
			}
			return IFlexoOntologyConcept.class;
		}

		@Override
		public DataBinding<?> getObject() {
			if (object == null) {
				object = new DataBinding<Object>(this, getObjectType(), BindingDefinitionType.GET) {
					@Override
					public Type getDeclaredType() {
						return getObjectType();
					}
				};
				object.setBindingName("object");
			}
			return object;
		}

		@Override
		public void setObject(DataBinding<?> object) {
			if (object != null) {
				object = new DataBinding(object.toString(), this, getObjectType(), BindingDefinitionType.GET) {
					@Override
					public Type getDeclaredType() {
						return getObjectType();
					}
				};
				object.setBindingName("object");
			}
			this.object = object;
		}

		/**
		 * Follow the link.
		 * 
		 * @see org.openflexo.foundation.fml.editionaction.AssignableAction#getAssignableType()
		 */
		@Override
		public Type getAssignableType() {
			// if (value != null) {
			// return value.getClass();
			// }
			return Object.class;
		}

		/**
		 * Follow the link.
		 * 
		 * @see org.openflexo.foundation.fml.editionaction.EditionAction#execute(org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction)
		 */
		@Override
		public EMFObjectIndividualAttributeObjectPropertyValue execute(FlexoBehaviourAction action) {
			EMFObjectIndividualAttributeObjectPropertyValue result = null;
			TypeAwareModelSlotInstance<EMFModel, EMFMetaModel, EMFModelSlot> modelSlotInstance = getModelSlotInstance(action);
			EMFModel model = modelSlotInstance.getAccessedResourceData();
			// Add Attribute in EMF
			getSubject(action).getObject().eSet(((EMFAttributeObjectProperty) getObjectProperty()).getObject(), getObject(action));
			// // Instanciate Wrapper
			// result = model.getConverter().convertObjectIndividualAttributeObjectPropertyValue(model, objectIndividual.getObject(),
			// attributeObjectProperty.getObject());
			return result;
		}

	}
}
