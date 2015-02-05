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
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.AddIndividual;
import org.openflexo.foundation.fml.editionaction.DataPropertyAssertion;
import org.openflexo.foundation.fml.editionaction.ObjectPropertyAssertion;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.emf.EMFModelSlot;
import org.openflexo.technologyadapter.emf.metamodel.AEMFMetaModelObjectImpl;
import org.openflexo.technologyadapter.emf.metamodel.EMFAttributeDataProperty;
import org.openflexo.technologyadapter.emf.metamodel.EMFAttributeObjectProperty;
import org.openflexo.technologyadapter.emf.metamodel.EMFClassClass;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.metamodel.EMFReferenceObjectProperty;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividual;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividualReferenceObjectPropertyValueAsList;

/**
 * Create EMF Object.
 * 
 * @author gbesancon
 * 
 */

@FIBPanel("Fib/AddEMFObjectIndividual.fib")
@ModelEntity
@ImplementationClass(AddEMFObjectIndividual.AddEMFObjectIndividualImpl.class)
@XMLElement
@FML("AddEMFObjectIndividual")
public interface AddEMFObjectIndividual extends AddIndividual<EMFModelSlot, EMFObjectIndividual>, EMFAction<EMFObjectIndividual> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String CONTAINER_KEY = "container";

	@Getter(value = CONTAINER_KEY)
	@XMLAttribute
	public DataBinding<List> getContainer();

	@Setter(CONTAINER_KEY)
	public void setContainer(DataBinding<List> containerReference);

	public static abstract class AddEMFObjectIndividualImpl extends AddIndividualImpl<EMFModelSlot, EMFObjectIndividual> implements
			AddEMFObjectIndividual {

		private static final Logger logger = Logger.getLogger(AddEMFObjectIndividual.class.getPackage().getName());

		// Binding to host the container specification for the individual to be
		// created
		private DataBinding<List> container;

		public AddEMFObjectIndividualImpl() {
			super();
		}

		@Override
		public EMFClassClass getOntologyClass() {
			return (EMFClassClass) super.getOntologyClass();
		}

		public void setOntologyClass(EMFClassClass ontologyClass) {
			super.setOntologyClass(ontologyClass);
		}

		@Override
		public Class<EMFObjectIndividual> getOntologyIndividualClass() {
			return EMFObjectIndividual.class;
		}

		@Override
		public EMFObjectIndividual execute(FlexoBehaviourAction action) {
			EMFObjectIndividual result = null;
			List container = null;
			TypeAwareModelSlotInstance<EMFModel, EMFMetaModel, EMFModelSlot> modelSlotInstance = (TypeAwareModelSlotInstance<EMFModel, EMFMetaModel, EMFModelSlot>) getModelSlotInstance(action);
			if (modelSlotInstance.getResourceData() != null) {
				IFlexoOntologyClass aClass = getOntologyClass();
				if (aClass instanceof EMFClassClass) {
					EMFClassClass emfClassClass = (EMFClassClass) aClass;
					// Create EMF Object
					EObject eObject = EcoreUtil.create(emfClassClass.getObject());
					// put it in its container
					try {
						container = getContainer().getBindingValue(action);
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

					// Instanciate Wrapper.
					result = modelSlotInstance.getAccessedResourceData().getConverter()
							.convertObjectIndividual(modelSlotInstance.getAccessedResourceData(), eObject);

					// Put it in its container
					if (container == null) {
						modelSlotInstance.getAccessedResourceData().getEMFResource().getContents().add(eObject);
					} else {
						// TODO This needs strong testing
						container.add(result);
						result.setContainPropertyValue((EMFObjectIndividualReferenceObjectPropertyValueAsList) container);
					}

					for (DataPropertyAssertion dataPropertyAssertion : getDataAssertions()) {
						if (dataPropertyAssertion.evaluateCondition(action)) {
							logger.info("DataPropertyAssertion=" + dataPropertyAssertion);
							EMFAttributeDataProperty property = (EMFAttributeDataProperty) dataPropertyAssertion.getOntologyProperty();
							logger.info("Property=" + property);
							// Vincent: force to recompute the declared
							// type(since it is automatically compute from the
							// uri).
							// Not sure of this, when loading a view/viewpoint,
							// the declared type is seek from the ontology
							// but while the virtual model is null, the declared
							// type is always Object.(see
							// DataPropertyAssertion).
							// In the case we manipulate IntegerParameters in
							// openflexo connected with int in EMF, then value
							// of Integer is
							// a
							// Long, producing a cast exception.
							dataPropertyAssertion.getValue().setDeclaredType(dataPropertyAssertion.getType());
							Object value = dataPropertyAssertion.getValue(action);
							logger.info("Value=" + value);
							// Set Data Attribute in EMF
							result.getObject().eSet(property.getObject(), value);
						}
					}
					for (ObjectPropertyAssertion objectPropertyAssertion : getObjectAssertions()) {
						if (objectPropertyAssertion.evaluateCondition(action)) {
							logger.info("ObjectPropertyAssertion=" + objectPropertyAssertion);
							if (objectPropertyAssertion.getOntologyProperty() instanceof EMFAttributeObjectProperty) {
								EMFAttributeObjectProperty property = (EMFAttributeObjectProperty) objectPropertyAssertion
										.getOntologyProperty();
								logger.info("Property=" + property);
								Object value = objectPropertyAssertion.getValue(action);
								logger.info("Value=" + value);
								// Set Data Attribute in EMF
								if (value instanceof AEMFMetaModelObjectImpl) {
									result.getObject().eSet(property.getObject(), ((AEMFMetaModelObjectImpl<?>) value).getObject());
								} else {
									result.getObject().eSet(property.getObject(), value);
								}
							} else if (objectPropertyAssertion.getOntologyProperty() instanceof EMFReferenceObjectProperty) {
								EMFReferenceObjectProperty property = (EMFReferenceObjectProperty) objectPropertyAssertion
										.getOntologyProperty();
								logger.info("Property=" + property);
								Object value = objectPropertyAssertion.getValue(action);
								logger.info("Value=" + value);
								// Set Data Attribute in EMF
								if (value instanceof AEMFMetaModelObjectImpl) {
									result.getObject().eSet(property.getObject(), ((AEMFMetaModelObjectImpl<?>) value).getObject());
								} else {
									if (value instanceof EMFObjectIndividual) {
										result.getObject().eSet(property.getObject(), ((EMFObjectIndividual) value).getObject());
									} else {
										result.getObject().eSet(property.getObject(), value);
									}
								}
							} else {
								logger.warning("Unexpected "
										+ objectPropertyAssertion.getOntologyProperty()
										+ " of "
										+ (objectPropertyAssertion.getOntologyProperty() != null ? objectPropertyAssertion
												.getOntologyProperty().getClass() : null));
							}
						}
					}
					modelSlotInstance.getResourceData().setIsModified();
					logger.info("********* Added individual " + result.getName() + " as " + aClass.getName());
				} else {
					logger.warning("Not allowed to create new Enum values. getOntologyClass()=" + getOntologyClass());
					return null;
				}
			} else {
				logger.warning("Model slot not correctly initialised : model is null");
				return null;
			}

			return result;
		}

		@Override
		public DataBinding<List> getContainer() {
			if (container == null) {
				container = new DataBinding<List>(this, List.class, DataBinding.BindingDefinitionType.GET_SET);
			}
			return container;
		}

		@Override
		public void setContainer(DataBinding<List> containerReference) {
			if (containerReference != null) {
				containerReference.setOwner(this);
				containerReference.setBindingName("container");
				containerReference.setDeclaredType(List.class);
				containerReference.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET_SET);
			}
			this.container = containerReference;
		}
	}
}
