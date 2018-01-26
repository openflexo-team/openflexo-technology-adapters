/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012, THALES SYSTEMES AEROPORTES - All Rights Reserved
 * Copyright (c) 2012-2012, AgileBirds
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

package org.openflexo.technologyadapter.emf.model.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.openflexo.foundation.ontology.IFlexoOntologyPropertyValue;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.metamodel.EMFReferenceObjectProperty;
import org.openflexo.technologyadapter.emf.model.EMFAnnotationAnnotation;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividual;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividualAttributeDataPropertyValue;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividualAttributeObjectPropertyValue;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividualReferenceObjectPropertyValue;

/**
 * EMF Model Converter.
 * 
 * @author gbesancon
 */
public class EMFModelConverter {

	private static final Logger logger = Logger.getLogger(EMFModelConverter.class.getPackage().getName());

	/** Builder. */
	protected EMFModelBuilder builder = new EMFModelBuilder();
	/** Concepts. */
	protected final Map<EObject, EMFObjectIndividual> individuals = new HashMap<EObject, EMFObjectIndividual>();
	/** Property Values. */
	protected final Map<EObject, Map<EStructuralFeature, IFlexoOntologyPropertyValue<EMFTechnologyAdapter>>> propertyValues = new HashMap<EObject, Map<EStructuralFeature, IFlexoOntologyPropertyValue<EMFTechnologyAdapter>>>();

	/** Concepts. */
	protected final Map<EObject, List<EMFAnnotationAnnotation>> annotations = new HashMap<EObject, List<EMFAnnotationAnnotation>>();

	/**
	 * Constructor.
	 */
	public EMFModelConverter() {
	}

	/**
	 * Convert a Resource into a Model with MetaModel support.
	 * 
	 * @param metaModel
	 * @param aResource
	 * @return
	 * @throws IOException
	 */
	public EMFModel convertModel(EMFMetaModel metaModel, Resource aResource) {
		if (!aResource.isLoaded()) {
			try {
				aResource.load(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		EMFModel model = builder.buildModel(metaModel, this, aResource);
		for (EObject aObject : aResource.getContents()) {
			convertObjectIndividual(model, aObject);
		}
		return model;
	}

	/**
	 * Convert EMF Object to EMF Object Individual.
	 * 
	 * @param model
	 * @param eObject
	 * @return
	 */
	public EMFObjectIndividual convertObjectIndividual(EMFModel model, EObject eObject) {
		EMFObjectIndividual individual = null;
		if (individuals.get(eObject) == null) {
			individual = builder.buildObjectIndividual(model, eObject);
			individuals.put(eObject, individual);

			// Convert container
			EStructuralFeature eContainingFeature = eObject.eContainingFeature();
			if (eContainingFeature != null) {
				eContainingFeature.eContainer();
				EObject eContainer = eObject.eContainer();
				EMFObjectIndividual containingIndiv = individuals.get(eContainer);
				if (containingIndiv == null) {
					containingIndiv = convertObjectIndividual(model, eContainer);
				}
			}

			// Convert Structural Features
			EClass objectClass = eObject.eClass();
			for (EStructuralFeature eSF : objectClass.getEAllStructuralFeatures()) {
				if (eSF instanceof EAttribute) {
					// Attribute
					convertObjectIndividualAttribute(model, eObject, (EAttribute) eSF);
				}
				else if (eSF instanceof EReference) {
					// Annotation content
					if (eObject instanceof EModelElement && eSF.getFeatureID() == EcorePackage.EMODEL_ELEMENT__EANNOTATIONS) {
						convertObjectIndividualAnnotations(model, individual, eObject, (EReference) eSF);
					}
					else {
						// Other References
						convertObjectIndividualReferenceObjectPropertyValue(model, eObject, (EReference) eSF);
					}
				}
			}

		}
		else {
			individual = individuals.get(eObject);
		}
		return individual;
	}

	/**
	 * Convert a EAnnotation to an Annotation
	 * 
	 * @param model
	 * @param emfAnswer
	 * @param eObject
	 * @param eSF
	 * @return
	 */

	private void convertObjectIndividualAnnotations(EMFModel model, EMFObjectIndividual indiv, EObject eObject, EReference eSF) {

		List<EMFAnnotationAnnotation> listAnn = annotations.get(eObject);
		if (listAnn == null) {
			listAnn = new ArrayList<EMFAnnotationAnnotation>();
			annotations.put(eObject, listAnn);
		}

		if (annotations != null) {

			Object value = eObject.eGet(eSF);
			if (value != null) {
				if (value instanceof EList) {
					for (Object aListValue : (EList<Object>) value) {
						if (aListValue instanceof EObject) {
							EMFAnnotationAnnotation annotation = convertObjectAnnotationl(model, indiv, (EAnnotation) aListValue);
							listAnn.add(annotation);
						}
					}
				}
			}

		}
	}

	private static EMFAnnotationAnnotation convertObjectAnnotationl(EMFModel model, EMFObjectIndividual container,
			EAnnotation anAnnotation) {
		// TODO, for now basic support for Annotations
		EMFAnnotationAnnotation annotation = new EMFAnnotationAnnotation(model, anAnnotation);
		// TODO : add annotations to container.addAnnotation();
		return annotation;
	}

	/**
	 * Convert single EMF Object or EObjectList of EMF Object to EMF Object Individual or List of EMF Object Individual
	 * 
	 * @param model
	 * @param emfAnswer
	 * @param eObject
	 * @return
	 */
	public Object convertIndividualReference(EMFModel model, Object reference) {

		if (reference instanceof EObject) {
			return convertObjectIndividual(model, (EObject) reference);
		}
		else if (reference instanceof Enum) {
			return reference;
		}
		else {
			logger.warning("Unexpected " + reference + " of " + (reference != null ? reference.getClass() : null));
			return reference;
		}
	}

	public Object convertIndividualReferenceList(EMFModel model, EObject object, EMFReferenceObjectProperty property) {

		EMFObjectIndividual individual;

		if (individuals.get(object) == null) {
			individual = builder.buildObjectIndividual(model, object);
			individuals.put(object, individual);
		}

		EMFObjectIndividualReferenceObjectPropertyValue propertyValue = convertObjectIndividualReferenceObjectPropertyValue(model, object,
				property.getObject());

		return propertyValue;

	}

	/**
	 * Convert EMF Attribute into Object Individual Attribute.
	 * 
	 * @param model
	 * @param eObject
	 * @param eAttribute
	 * @return
	 */
	public IFlexoOntologyPropertyValue convertObjectIndividualAttribute(EMFModel model, EObject eObject, EAttribute eAttribute) {
		IFlexoOntologyPropertyValue propertyValue = null;
		if (eAttribute.getEAttributeType().eClass().getClassifierID() == EcorePackage.EDATA_TYPE) {
			propertyValue = convertObjectIndividualAttributeDataPropertyValue(model, eObject, eAttribute);
		}
		else {
			propertyValue = convertObjectIndividualAttributeObjectPropertyValue(model, eObject, eAttribute);
		}
		return propertyValue;
	}

	/**
	 * Convert EMF Attribute into Object Individual Attribute Data Property Value.
	 * 
	 * @param model
	 * @param eObject
	 * @param eAttribute
	 * @return
	 */
	public EMFObjectIndividualAttributeDataPropertyValue convertObjectIndividualAttributeDataPropertyValue(EMFModel model, EObject eObject,
			EAttribute eAttribute) {
		EMFObjectIndividualAttributeDataPropertyValue propertyValue = null;
		if (propertyValues.get(eObject) == null) {
			propertyValues.put(eObject, new HashMap<EStructuralFeature, IFlexoOntologyPropertyValue<EMFTechnologyAdapter>>());
		}
		if (propertyValues.get(eObject).get(eAttribute) == null) {
			propertyValue = builder.buildObjectIndividualAttributeDataPropertyValue(model, eObject, eAttribute);
			propertyValues.get(eObject).put(eAttribute, propertyValue);
		}
		else {
			propertyValue = (EMFObjectIndividualAttributeDataPropertyValue) propertyValues.get(eObject).get(eAttribute);
		}
		return propertyValue;
	}

	/**
	 * Convert EMF Attribute into Object Individual Attribute Object Property Value.
	 * 
	 * @param model
	 * @param eObject
	 * @param eAttribute
	 * @return
	 */
	public EMFObjectIndividualAttributeObjectPropertyValue convertObjectIndividualAttributeObjectPropertyValue(EMFModel model,
			EObject eObject, EAttribute eAttribute) {
		EMFObjectIndividualAttributeObjectPropertyValue propertyValue = null;
		if (propertyValues.get(eObject) == null) {
			propertyValues.put(eObject, new HashMap<EStructuralFeature, IFlexoOntologyPropertyValue<EMFTechnologyAdapter>>());
		}
		if (propertyValues.get(eObject).get(eAttribute) == null) {
			propertyValue = builder.buildObjectIndividualAttributeObjectPropertyValue(model, eObject, eAttribute);
			propertyValues.get(eObject).put(eAttribute, propertyValue);
		}
		else {
			propertyValue = (EMFObjectIndividualAttributeObjectPropertyValue) propertyValues.get(eObject).get(eAttribute);
		}
		return propertyValue;
	}

	/**
	 * Convert EMF Reference into Object Individual Reference Object Property Value.
	 * 
	 * @param model
	 * @param eObject
	 * @param eReference
	 * @return
	 */
	public EMFObjectIndividualReferenceObjectPropertyValue convertObjectIndividualReferenceObjectPropertyValue(EMFModel model,
			EObject eObject, EReference eReference) {
		Map<EStructuralFeature, IFlexoOntologyPropertyValue<EMFTechnologyAdapter>> listVals = propertyValues.get(eObject);
		if (listVals == null) {
			listVals = new HashMap<EStructuralFeature, IFlexoOntologyPropertyValue<EMFTechnologyAdapter>>();
			propertyValues.put(eObject, listVals);
		}
		EMFObjectIndividualReferenceObjectPropertyValue propertyValue = (EMFObjectIndividualReferenceObjectPropertyValue) listVals
				.get(eReference);
		if (propertyValue == null) {
			propertyValue = builder.buildObjectIndividualReferenceObjectPropertyValue(model, eObject, eReference);
			propertyValues.get(eObject).put(eReference, propertyValue);

			Object value = eObject.eGet(eReference);
			if (value != null) {
				if (eReference.getUpperBound() == 1) {
					if (value instanceof EObject) {
						convertObjectIndividual(model, (EObject) value);
					}
				}
				else if (eReference.getUpperBound() == -1) {
					if (value instanceof EList) {
						for (Object aListValue : (EList<Object>) value) {
							if (aListValue instanceof EObject) {
								convertObjectIndividual(model, (EObject) aListValue);
							}
						}
					}
				}
			}

		}
		return propertyValue;
	}

	/**
	 * Getter of individuals.
	 * 
	 * @return the individuals value
	 */
	public Map<EObject, EMFObjectIndividual> getIndividuals() {
		return individuals;
	}

	/**
	 * Getter of propertyValues.
	 * 
	 * @return the propertyValues value
	 */
	public Map<EObject, Map<EStructuralFeature, IFlexoOntologyPropertyValue<EMFTechnologyAdapter>>> getPropertyValues() {
		return propertyValues;
	}

}
