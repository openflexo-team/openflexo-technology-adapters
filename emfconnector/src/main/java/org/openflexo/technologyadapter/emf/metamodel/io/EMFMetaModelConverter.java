/**
 * 
 * Copyright (c) 2013-2015, Openflexo
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

package org.openflexo.technologyadapter.emf.metamodel.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.openflexo.foundation.ontology.IFlexoOntologyAnnotation;
import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.metamodel.EMFAnnotationAnnotation;
import org.openflexo.technologyadapter.emf.metamodel.EMFAttributeAssociation;
import org.openflexo.technologyadapter.emf.metamodel.EMFAttributeDataProperty;
import org.openflexo.technologyadapter.emf.metamodel.EMFAttributeObjectProperty;
import org.openflexo.technologyadapter.emf.metamodel.EMFClassClass;
import org.openflexo.technologyadapter.emf.metamodel.EMFDataTypeDataType;
import org.openflexo.technologyadapter.emf.metamodel.EMFEnumClass;
import org.openflexo.technologyadapter.emf.metamodel.EMFEnumIndividual;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.metamodel.EMFPackageContainer;
import org.openflexo.technologyadapter.emf.metamodel.EMFReferenceAssociation;
import org.openflexo.technologyadapter.emf.metamodel.EMFReferenceObjectProperty;

/**
 * EMF MetaModel Converter.
 * 
 * @author gbesancon
 */
public class EMFMetaModelConverter {


	protected static final Logger logger = Logger.getLogger(EMFMetaModelConverter.class.getPackage().getName());


	/** Builder. */
	protected final EMFMetaModelBuilder builder;
	/** Packages from EPackage. */
	protected Map<EPackage, EMFPackageContainer> packages = new HashMap<EPackage, EMFPackageContainer>();
	/** Classes from EClass. */
	protected Map<EClass, EMFClassClass> classes = new HashMap<EClass, EMFClassClass>();
	/** DataType from EDataType. */
	protected Map<EDataType, EMFDataTypeDataType> dataTypes = new HashMap<EDataType, EMFDataTypeDataType>();
	/** Classes from EEnum. */
	protected Map<EEnum, EMFEnumClass> enums = new HashMap<EEnum, EMFEnumClass>();
	/** Individual from EEnumLiteral. */
	protected Map<Enumerator, EMFEnumIndividual> enumLiterals = new HashMap<Enumerator, EMFEnumIndividual>();
	/** Features from EAttribute. */
	protected Map<EAttribute, EMFAttributeObjectProperty> objectAttributes = new HashMap<EAttribute, EMFAttributeObjectProperty>();
	/** Features from EAttribute. */
	protected Map<EAttribute, EMFAttributeDataProperty> dataAttributes = new HashMap<EAttribute, EMFAttributeDataProperty>();
	/** Feature Associations from EAttribute. */
	protected Map<EAttribute, EMFAttributeAssociation> attributeAssociations = new HashMap<EAttribute, EMFAttributeAssociation>();
	/** Features from EReference. */
	protected Map<EReference, EMFReferenceObjectProperty> references = new HashMap<EReference, EMFReferenceObjectProperty>();
	/** Feature Associations from EReference. */
	protected Map<EReference, EMFReferenceAssociation> referenceAssociations = new HashMap<EReference, EMFReferenceAssociation>();
	/** Annotations. */
	protected Map<EAnnotation, EMFAnnotationAnnotation> annotations = new HashMap<EAnnotation, EMFAnnotationAnnotation>();

	/**
	 * Constructor.
	 */
	public EMFMetaModelConverter(EMFTechnologyAdapter adapter) {
		builder = new EMFMetaModelBuilder(adapter);
	}

	/**
	 * Convert an EMF Resource into a MetaModel.
	 * 
	 * @param resource
	 * @return
	 */
	public EMFMetaModel convertMetaModel(Resource aResource) {
		EMFMetaModel metaModel = null;
		if (aResource.getContents().size() == 1 && aResource.getContents().get(0).eClass().getClassifierID() == EcorePackage.EPACKAGE) {
			EPackage aPackage = (EPackage) aResource.getContents().get(0);
			metaModel = convertMetaModel(aPackage);
		}
		return metaModel;
	}

	/**
	 * Convert an EMF Package into a MetaModel.
	 * 
	 * @param aPackage
	 * @return
	 */
	public EMFMetaModel convertMetaModel(EPackage aPackage) {
		EMFMetaModel metaModel = builder.buildMetaModel(this, aPackage);
		convertPackage(metaModel, aPackage);
		return metaModel;
	}

	/**
	 * Convert an EMF Annotation into an EMF Annotation.
	 * 
	 * @param metaModel
	 * @param aAnnotation
	 * @return
	 */
	public EMFAnnotationAnnotation convertAnnotation(EMFMetaModel metaModel, EAnnotation aAnnotation) {
		EMFAnnotationAnnotation emfAnnotation = null;
		if (annotations.get(aAnnotation) == null) {
			emfAnnotation = builder.buildAnnotation(metaModel, aAnnotation);
		} else {
			emfAnnotation = annotations.get(aAnnotation);
		}
		return emfAnnotation;
	}

	/**
	 * Convert an EMF Package into an EMF Package container.
	 * 
	 * @param metaModel
	 * @param aPackage
	 * @return
	 */
	public EMFPackageContainer convertPackage(EMFMetaModel metaModel, EPackage aPackage) {
		EMFPackageContainer emfPackageContainer = packages.get(aPackage);
		if (emfPackageContainer == null) {
			// Super Package.
			if (aPackage.getESuperPackage() != null) {
				convertPackage(metaModel, aPackage.getESuperPackage());
			}

			emfPackageContainer = builder.buildPackage(metaModel, aPackage);
			packages.put(aPackage, emfPackageContainer);

			// DataTypes
			for (EClassifier aClassifier : aPackage.getEClassifiers()) {


				if (aClassifier.eClass().getClassifierID() == EcorePackage.EDATA_TYPE) {
					convertDataType(metaModel, (EDataType) aClassifier);
				}
				// Enum
				else if (aClassifier.eClass().getClassifierID() == EcorePackage.EENUM) {
					convertEnum(metaModel, (EEnum) aClassifier);
				}
				// Classes
				else if (aClassifier.eClass().getClassifierID() == EcorePackage.ECLASS) {
					convertClass(metaModel, (EClass) aClassifier, aPackage);
				}
			}

			// Sub Packages
			for (EPackage aSubPackage : aPackage.getESubpackages()) {
				convertPackage(metaModel, aSubPackage);
			}
		} 

		return emfPackageContainer;
	}

	/**
	 * Convert an EMF DataType into an EMF DataType.
	 * 
	 * @param metaModel
	 * @param aDataType
	 * @return
	 */
	public EMFDataTypeDataType convertDataType(EMFMetaModel metaModel, EDataType aDataType) {
		EMFDataTypeDataType emfDataType = null;
		if (dataTypes.get(aDataType) == null) {
			if (aDataType.getEPackage() != null) {
				convertPackage(metaModel, aDataType.getEPackage());
			}

			emfDataType = builder.buildDataType(metaModel, aDataType);
			dataTypes.put(aDataType, emfDataType);
		} else {
			emfDataType = dataTypes.get(aDataType);
		}
		return emfDataType;
	}

	/**
	 * Convert an EMF Enum into an EMF Enum Class Concept.
	 * 
	 * @param metaModel
	 * @param aEnum
	 * @return
	 */
	public EMFEnumClass convertEnum(EMFMetaModel metaModel, EEnum aEnum) {
		EMFEnumClass emfEnum = enums.get(aEnum);
		if (emfEnum == null) {
			if (aEnum.getEPackage() != null) {
				convertPackage(metaModel, aEnum.getEPackage());
			}

			emfEnum = builder.buildEnum(metaModel, aEnum);
			enums.put(aEnum, emfEnum);
			for (EEnumLiteral aEnumLiteral : aEnum.getELiterals()) {
				convertEnumLiteral(metaModel, aEnumLiteral);
			}
		} 
		return emfEnum;
	}

	/**
	 * Convert an EMF Enum Literal into an EMF Enum Individual Concept.
	 * 
	 * @param metaModel
	 * @param aEnumLiteral
	 * @return
	 */
	public EMFEnumIndividual convertEnumLiteral(EMFMetaModel metaModel, EEnumLiteral aEnumLiteral) {
		EMFEnumIndividual emfEnumLiteral = enumLiterals.get(aEnumLiteral.getInstance());
		if (emfEnumLiteral == null) {
			if (aEnumLiteral.getEEnum() != null) {
				convertEnum(metaModel, aEnumLiteral.getEEnum());
			}

			emfEnumLiteral = builder.buildEnumLiteral(metaModel, aEnumLiteral);
			enumLiterals.put(aEnumLiteral.getInstance(), emfEnumLiteral);
		}
		return emfEnumLiteral;
	}

	/**
	 * Convert an EMF Class into an EMF Class Concept.
	 * 
	 * @param metaModel
	 * @param aClass
	 * @return
	 */
	public EMFClassClass convertClass(EMFMetaModel metaModel, EClass aClass, EPackage mmRootEPackage) {

		EMFClassClass emfClass = classes.get(aClass);;

		if (emfClass == null) {

			// prevent converting EBObjects from EcorePackage when not in Ecore MM
			if ( (mmRootEPackage == org.eclipse.emf.ecore.EcorePackage.eINSTANCE) || 
					aClass.getEPackage() != org.eclipse.emf.ecore.EcorePackage.eINSTANCE ) {
				emfClass = builder.buildClass(metaModel, aClass);
				classes.put(aClass, emfClass);

				EPackage localPackage = aClass.getEPackage();
				if (localPackage != null && localPackage !=  org.eclipse.emf.ecore.EcorePackage.eINSTANCE) {
					if (localPackage != mmRootEPackage) logger.warning("Converting an EClass from a package different that MM Root One");
					convertPackage(metaModel, aClass.getEPackage());
				}
				// convert superTypes	
				for (EClass eSuperClass : aClass.getESuperTypes()) {
					convertClass(metaModel, eSuperClass, mmRootEPackage);
				}
				//convert StructuralFeatures
				for (EStructuralFeature eSF : aClass.getEAllStructuralFeatures()) {
					if (eSF instanceof EAttribute){
						// Attribute
						convertAttributeAssociation(metaModel, (EAttribute) eSF, emfClass, mmRootEPackage);
					}
					else if (eSF instanceof EReference){
						// Annotation content
						if (aClass instanceof EModelElement && eSF.getFeatureID() == EcorePackage.EMODEL_ELEMENT__EANNOTATIONS){
							emfClass.getAnnotations();
						}
						else{
							// Other References
							convertReferenceAssociation(metaModel, (EReference) eSF, emfClass, mmRootEPackage);
						}
					}

				}
			}
		}
		return emfClass;
	}

	/**
	 * Convert an EMF Attribute into an EMF Feature Association.
	 * 
	 * @param metaModel
	 * @param aAttribute
	 * @return
	 */
	public EMFAttributeAssociation convertAttributeAssociation(EMFMetaModel metaModel, EAttribute aAttribute, EMFClassClass containingClass, EPackage mmRootEPackage) {
		EMFAttributeAssociation emfAttributeAssociation = attributeAssociations.get(aAttribute);

		if (emfAttributeAssociation == null) {
			if (containingClass == null && aAttribute.getEContainingClass() != null) {
				convertClass(metaModel, aAttribute.getEContainingClass(), mmRootEPackage);
			}

			emfAttributeAssociation = builder.buildAttributeAssociation(metaModel, aAttribute);
			attributeAssociations.put(aAttribute, emfAttributeAssociation);
			convertAttributeProperty(metaModel, aAttribute, containingClass,mmRootEPackage);
		} 
		return emfAttributeAssociation;
	}

	/**
	 * Convert an EMF Reference into an EMF Feature Association.
	 * 
	 * @param metaModel
	 * @param aReference
	 * @return
	 */
	public EMFReferenceAssociation convertReferenceAssociation(EMFMetaModel metaModel, EReference aReference, EMFClassClass containingClass, EPackage mmRootEPackage) {
		EMFReferenceAssociation emfReferenceAssociation = referenceAssociations.get(aReference);
		if ( emfReferenceAssociation == null) {
			if (containingClass == null && aReference.getEContainingClass() != null) {
				convertClass(metaModel, aReference.getEContainingClass(),mmRootEPackage);
			}

			emfReferenceAssociation = builder.buildReferenceAssociation(metaModel, aReference);
			referenceAssociations.put(aReference, emfReferenceAssociation);
			convertReferenceObjectProperty(metaModel, aReference, containingClass, mmRootEPackage);
		} 
		return emfReferenceAssociation;
	}

	/**
	 * Convert an EMF Attribute into an EMF Structural Property.
	 * 
	 * @param metaModel
	 * @param aAttribute
	 * @return
	 */
	public IFlexoOntologyStructuralProperty<EMFTechnologyAdapter> convertAttributeProperty(EMFMetaModel metaModel, EAttribute aAttribute, EMFClassClass containingClass, EPackage mmRootEPackage) {
		IFlexoOntologyStructuralProperty<EMFTechnologyAdapter> structuralProperty = null;
		if (aAttribute.getEAttributeType().eClass().getClassifierID() == EcorePackage.EDATA_TYPE) {
			structuralProperty = convertAttributeDataProperty(metaModel, aAttribute, containingClass,mmRootEPackage);
		} else if (aAttribute.getEAttributeType().eClass().getClassifierID() == EcorePackage.EENUM) {
			structuralProperty = convertAttributeObjectProperty(metaModel, aAttribute, containingClass, mmRootEPackage);
		}
		return structuralProperty;
	}

	/**
	 * Convert an EMF Attribute into an EMF Attribute Data Property.
	 * 
	 * @param metaModel
	 * @param aAttribute
	 * @return
	 */
	public EMFAttributeDataProperty convertAttributeDataProperty(EMFMetaModel metaModel, EAttribute aAttribute, EMFClassClass containingClass, EPackage mmRootEPackage) {
		EMFAttributeDataProperty dataProperty = dataAttributes.get(aAttribute);
		if ( dataProperty == null) {
			if (containingClass == null && aAttribute.getEContainingClass() != null) {
				convertClass(metaModel, aAttribute.getEContainingClass(),mmRootEPackage);
			}

			dataProperty = builder.buildAttributeDataProperty(metaModel, aAttribute);
			dataAttributes.put(aAttribute, dataProperty);

			if (aAttribute.getEAttributeType().eClass().getClassifierID() == EcorePackage.EDATA_TYPE) {
				convertDataType(metaModel, aAttribute.getEAttributeType());
			}
		} 
		return dataProperty;
	}

	/**
	 * Convert an EMF Attribute into an EMF Attribute Object Property.
	 * 
	 * @param metaModel
	 * @param aAttribute
	 * @return
	 */
	public EMFAttributeObjectProperty convertAttributeObjectProperty(EMFMetaModel metaModel, EAttribute aAttribute, EMFClassClass containingClass, EPackage mmRootEPackage) {
		EMFAttributeObjectProperty objectProperty = objectAttributes.get(aAttribute);
		if ( objectProperty == null) {
			if (containingClass == null && aAttribute.getEContainingClass() != null) {
				convertClass(metaModel, aAttribute.getEContainingClass(),mmRootEPackage);
			}

			objectProperty = builder.buildAttributeObjectProperty(metaModel, aAttribute);
			objectAttributes.put(aAttribute, objectProperty);

			if (aAttribute.getEAttributeType().eClass().getClassifierID() == EcorePackage.EENUM) {
				convertEnum(metaModel, (EEnum) aAttribute.getEAttributeType());
			}
		}
		return objectProperty;
	}

	/**
	 * Convert an EMF Reference into an EMF Object Property.
	 * 
	 * @param metaModel
	 * @param aReference
	 * @return
	 */
	public EMFReferenceObjectProperty convertReferenceObjectProperty(EMFMetaModel metaModel, EReference aReference, EMFClassClass containingClass,EPackage mmRootEPackage) {
		EMFReferenceObjectProperty objectProperty = references.get(aReference);
		if ( objectProperty == null) {
			if (containingClass == null && aReference.getEContainingClass() != null) {
				convertClass(metaModel, aReference.getEContainingClass(), mmRootEPackage);
			}

			objectProperty = builder.buildReferenceObjectProperty(metaModel, aReference);
			references.put(aReference, objectProperty);
		} 
		return objectProperty;
	}

	/**
	 * Getter of packages.
	 * 
	 * @return the packages value
	 */
	public Map<EPackage, EMFPackageContainer> getPackages() {
		return packages;
	}

	/**
	 * Getter of classes.
	 * 
	 * @return the classes value
	 */
	public Map<EClass, EMFClassClass> getClasses() {
		return classes;
	}

	/**
	 * Getter of dataTypes.
	 * 
	 * @return the dataTypes value
	 */
	public Map<EDataType, EMFDataTypeDataType> getDataTypes() {
		return dataTypes;
	}

	/**
	 * Getter of enums.
	 * 
	 * @return the enums value
	 */
	public Map<EEnum, EMFEnumClass> getEnums() {
		return enums;
	}

	/**
	 * Getter of enumLiterals.
	 * 
	 * @return the enumLiterals value
	 */
	public Map<Enumerator, EMFEnumIndividual> getEnumLiterals() {
		return enumLiterals;
	}

	/**
	 * Getter of objectAttributes.
	 * 
	 * @return the objectAttributes value
	 */
	public Map<EAttribute, EMFAttributeObjectProperty> getObjectAttributes() {
		return objectAttributes;
	}

	/**
	 * Getter of dataAttributes.
	 * 
	 * @return the dataAttributes value
	 */
	public Map<EAttribute, EMFAttributeDataProperty> getDataAttributes() {
		return dataAttributes;
	}

	/**
	 * Getter of attributeAssociations.
	 * 
	 * @return the attributeAssociations value
	 */
	public Map<EAttribute, EMFAttributeAssociation> getAttributeAssociations() {
		return attributeAssociations;
	}

	/**
	 * Getter of references.
	 * 
	 * @return the references value
	 */
	public Map<EReference, EMFReferenceObjectProperty> getReferences() {
		return references;
	}

	/**
	 * Getter of referenceAssociations.
	 * 
	 * @return the referenceAssociations value
	 */
	public Map<EReference, EMFReferenceAssociation> getReferenceAssociations() {
		return referenceAssociations;
	}
}
