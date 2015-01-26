/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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
import java.util.logging.Logger;

import org.openflexo.antar.binding.DataBinding;
import org.openflexo.antar.binding.DataBinding.BindingDefinitionType;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.SetPropertyValueAction;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyConcept;
import org.openflexo.foundation.ontology.IndividualOfClass;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.technologyadapter.emf.EMFModelSlot;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.model.AEMFModelObjectImpl;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividual;

@ModelEntity(isAbstract = true)
@ImplementationClass(SetEMFPropertyValue.SetEMFPropertyValueImpl.class)
@FML("SetEMFPropertyValue")
public abstract interface SetEMFPropertyValue<T extends AEMFModelObjectImpl<?>> extends EMFAction<T>, SetPropertyValueAction<T> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String SUBJECT_KEY = "subject";

	@Override
	@Getter(value = SUBJECT_KEY)
	@XMLAttribute
	public DataBinding<?> getSubject();

	@Override
	@Setter(SUBJECT_KEY)
	public void setSubject(DataBinding<?> subject);

	public static abstract class SetEMFPropertyValueImpl<T extends AEMFModelObjectImpl<?>> extends
			TechnologySpecificActionImpl<EMFModelSlot, T> implements SetEMFPropertyValue<T> {

		private static final Logger logger = Logger.getLogger(SetEMFPropertyValue.class.getPackage().getName());

		public SetEMFPropertyValueImpl() {
			super();
		}

		private DataBinding<?> subject;

		@Override
		public Type getSubjectType() {
			if (getProperty() != null && getProperty().getDomain() instanceof IFlexoOntologyClass) {
				return IndividualOfClass.getIndividualOfClass((IFlexoOntologyClass) getProperty().getDomain());
			}
			return IFlexoOntologyConcept.class;
		}

		public EMFObjectIndividual getSubject(FlexoBehaviourAction action) {
			try {
				return (EMFObjectIndividual) getSubject().getBindingValue(action);
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public DataBinding<?> getSubject() {
			if (subject == null) {
				subject = new DataBinding<Object>(this, getSubjectType(), BindingDefinitionType.GET) {
					@Override
					public Type getDeclaredType() {
						return getSubjectType();
					}
				};
				subject.setBindingName("subject");
			}
			return subject;
		}

		@Override
		public void setSubject(DataBinding<?> subject) {
			if (subject != null) {
				subject = new DataBinding<Object>(subject.toString(), this, getSubjectType(), BindingDefinitionType.GET) {
					@Override
					public Type getDeclaredType() {
						return getSubjectType();
					}
				};
				subject.setBindingName("subject");
			}
			this.subject = subject;
		}

		@Override
		public TypeAwareModelSlotInstance<EMFModel, EMFMetaModel, EMFModelSlot> getModelSlotInstance(FlexoBehaviourAction action) {
			return (TypeAwareModelSlotInstance<EMFModel, EMFMetaModel, EMFModelSlot>) super.getModelSlotInstance(action);

		}

		public static class SubjectIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<SetEMFPropertyValue> {
			public SubjectIsRequiredAndMustBeValid() {
				super("'subject'_binding_is_required_and_must_be_valid", SetEMFPropertyValue.class);
			}

			@Override
			public DataBinding<IFlexoOntologyConcept> getBinding(SetEMFPropertyValue object) {
				return object.getSubject();
			}

		}

	}
}
