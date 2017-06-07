/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Owlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.owl.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.ontology.DuplicateURIException;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyConcept;
import org.openflexo.foundation.ontology.fml.editionaction.AddIndividual;
import org.openflexo.foundation.ontology.fml.editionaction.DataPropertyAssertion;
import org.openflexo.foundation.ontology.fml.editionaction.ObjectPropertyAssertion;
import org.openflexo.model.annotations.DefineValidationRule;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.owl.OWLModelSlot;
import org.openflexo.technologyadapter.owl.model.OWLClass;
import org.openflexo.technologyadapter.owl.model.OWLConcept;
import org.openflexo.technologyadapter.owl.model.OWLIndividual;
import org.openflexo.technologyadapter.owl.model.OWLObjectProperty;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.OWLProperty;

@ModelEntity
@ImplementationClass(AddOWLIndividual.AddOWLIndividualImpl.class)
@XMLElement
@FML("AddOWLIndividual")
public interface AddOWLIndividual extends AddIndividual<OWLModelSlot, OWLOntology, OWLIndividual>, OWLAction<OWLIndividual> {

	public static abstract class AddOWLIndividualImpl extends AddIndividualImpl<OWLModelSlot, OWLOntology, OWLIndividual>
			implements AddOWLIndividual {

		private static final Logger logger = Logger.getLogger(AddOWLIndividual.class.getPackage().getName());

		private final String dataPropertyURI = null;

		public AddOWLIndividualImpl() {
			super();
		}

		@Override
		public OWLClass getOntologyClass() {
			return (OWLClass) super.getOntologyClass();
		}

		@Override
		public Class<OWLIndividual> getOntologyIndividualClass() {
			return OWLIndividual.class;
		}

		private DataBinding<IFlexoOntologyClass<?>> dynamicType;

		@Override
		public DataBinding<IFlexoOntologyClass<?>> getDynamicType() {
			if (dynamicType == null) {
				dynamicType = new DataBinding<IFlexoOntologyClass<?>>(this, OWLClass.class, DataBinding.BindingDefinitionType.GET);
				dynamicType.setBindingName("dynamicType");
			}
			return dynamicType;
		}

		@Override
		public void setDynamicType(DataBinding<IFlexoOntologyClass<?>> dynamicType) {
			if (dynamicType != null) {
				dynamicType.setOwner(this);
				dynamicType.setDeclaredType(OWLClass.class);
				dynamicType.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				dynamicType.setBindingName("dynamicType");
			}
			this.dynamicType = dynamicType;
		}

		@Override
		public OWLIndividual execute(RunTimeEvaluationContext evaluationContext) {

			OWLClass father = null;

			if (getDynamicType().isValid()) {
				try {
					father = (OWLClass) getDynamicType().getBindingValue(evaluationContext);
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
				father = getOntologyClass();
			}

			String individualName = null;
			try {
				individualName = getIndividualName().getBindingValue(evaluationContext);
			} catch (TypeMismatchException e1) {
				e1.printStackTrace();
			} catch (NullReferenceException e1) {
				e1.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			// System.out.println("individualName="+individualName);
			OWLIndividual newIndividual = null;
			try {
				if (getModelSlotInstance(evaluationContext) != null) {
					if (getModelSlotInstance(evaluationContext).getResourceData() != null) {
						logger.info("Adding individual individualName=" + getIndividualName() + " father =" + getOntologyClass());
						logger.info("Adding individual individualName=" + individualName + " father =" + father);
						newIndividual = getModelSlotInstance(evaluationContext).getAccessedResourceData()
								.createOntologyIndividual(individualName, father);
						logger.info("********* Added individual " + newIndividual.getName() + " as " + father);

						for (DataPropertyAssertion dataPropertyAssertion : getDataAssertions()) {
							if (dataPropertyAssertion.evaluateCondition(evaluationContext)) {
								logger.info("DataPropertyAssertion=" + dataPropertyAssertion);
								OWLProperty property = (OWLProperty) dataPropertyAssertion.getOntologyProperty();
								logger.info("Property=" + property);
								Object value = dataPropertyAssertion.getValue(evaluationContext);
								if (value != null) {
									newIndividual.addPropertyStatement(property, value);
								}
							}
						}
						for (ObjectPropertyAssertion objectPropertyAssertion : getObjectAssertions()) {
							if (objectPropertyAssertion.evaluateCondition(evaluationContext)) {
								// logger.info("ObjectPropertyAssertion="+objectPropertyAssertion);
								OWLProperty property = (OWLProperty) objectPropertyAssertion.getOntologyProperty();
								// logger.info("Property="+property);
								if (property instanceof OWLObjectProperty) {
									if (((OWLObjectProperty) property).isLiteralRange()) {
										Object value = objectPropertyAssertion.getValue(evaluationContext);
										if (value != null) {
											newIndividual.addPropertyStatement(property, value);
										}
									}
									else {
										OWLConcept<?> assertionObject = (OWLConcept<?>) objectPropertyAssertion
												.getAssertionObject(evaluationContext);
										if (assertionObject != null) {
											newIndividual.getOntResource().addProperty(((OWLObjectProperty) property).getOntProperty(),
													assertionObject.getOntResource());
										}
									}
								}
								IFlexoOntologyConcept assertionObject = objectPropertyAssertion.getAssertionObject(evaluationContext);
								// logger.info("assertionObject="+assertionObject);
								if (assertionObject != null && newIndividual instanceof OWLIndividual && property instanceof OWLProperty
										&& assertionObject instanceof OWLConcept) {
									newIndividual.getOntResource().addProperty(property.getOntProperty(),
											((OWLConcept) assertionObject).getOntResource());
								}
								else {
									// logger.info("assertion object is null");
								}
							}
						}
						newIndividual.updateOntologyStatements();
					}
					else {
						logger.warning("No model defined for ModelSlotInstance " + getModelSlotInstance(evaluationContext));
					}
				}
				else {
					logger.warning("No model slot instance defined for " + getReceiver());
				}
				logger.info("Return " + newIndividual);
				return newIndividual;
			} catch (DuplicateURIException e) {
				e.printStackTrace();
				return null;
			}

		}

		@Override
		public TypeAwareModelSlotInstance<OWLOntology, OWLOntology, OWLModelSlot> getModelSlotInstance(
				RunTimeEvaluationContext evaluationContext) {
			return (TypeAwareModelSlotInstance<OWLOntology, OWLOntology, OWLModelSlot>) super.getModelSlotInstance(evaluationContext);
		}

	}

	@DefineValidationRule
	public static class IndividualNameBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddOWLIndividual> {
		public IndividualNameBindingIsRequiredAndMustBeValid() {
			super("'individual_name'_binding_is_required_and_must_be_valid", AddOWLIndividual.class);
		}

		@Override
		public DataBinding<String> getBinding(AddOWLIndividual object) {
			return object.getIndividualName();
		}

	}

}
