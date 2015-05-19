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

package org.openflexo.technologyadapter.emf.metamodel.io;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
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
 * EMF MetaModel Builder.
 * 
 * @author gbesancon
 */
public class EMFMetaModelBuilder {
	/** EMF Adapter. */
	protected final EMFTechnologyAdapter adapter;

	/**
	 * Constructor.
	 */
	public EMFMetaModelBuilder(EMFTechnologyAdapter adapter) {
		this.adapter = adapter;
	}

	/**
	 * Build MetaModel.
	 * 
	 * @param aPackage
	 * @return
	 */
	public EMFMetaModel buildMetaModel(EMFMetaModelConverter converter, EPackage aPackage) {
		return new EMFMetaModel(converter, aPackage, adapter);
	}

	/**
	 * Build Annotation.
	 * 
	 * @param metaModel
	 * @param aAnnotation
	 * @return
	 */
	public EMFAnnotationAnnotation buildAnnotation(EMFMetaModel metaModel, EAnnotation aAnnotation) {
		return new EMFAnnotationAnnotation(metaModel, aAnnotation);
	}

	/**
	 * Build Package Container.
	 * 
	 * @param metaModel
	 * @param aPackage
	 * @return
	 */
	public EMFPackageContainer buildPackage(EMFMetaModel metaModel, EPackage aPackage) {
		return new EMFPackageContainer(metaModel, aPackage);
	}

	/**
	 * Build Class.
	 * 
	 * @param aClass
	 * @return
	 */
	public EMFClassClass buildClass(EMFMetaModel metaModel, EClass aClass) {
		return new EMFClassClass(metaModel, aClass);
	}

	/**
	 * Build DataType.
	 * 
	 * @param aDataType
	 * @return
	 */
	public EMFDataTypeDataType buildDataType(EMFMetaModel metaModel, EDataType aDataType) {
		return new EMFDataTypeDataType(metaModel, aDataType);
	}

	/**
	 * Build Enum.
	 * 
	 * @param aDataType
	 * @return
	 */
	public EMFEnumClass buildEnum(EMFMetaModel metaModel, EEnum aEnum) {
		return new EMFEnumClass(metaModel, aEnum);
	}

	/**
	 * Build Enum Literal.
	 * 
	 * @param aEnumLiteral
	 * @return
	 */
	public EMFEnumIndividual buildEnumLiteral(EMFMetaModel metaModel, EEnumLiteral aEnumLiteral) {
		return new EMFEnumIndividual(metaModel, aEnumLiteral);
	}

	/**
	 * Build Attribute Association.
	 * 
	 * @param aAttribute
	 * @return
	 */
	public EMFAttributeAssociation buildAttributeAssociation(EMFMetaModel metaModel, EAttribute aAttribute) {
		return new EMFAttributeAssociation(metaModel, aAttribute);
	}

	/**
	 * Build Attribute Data Property.
	 * 
	 * @param aReference
	 * @return
	 */
	public EMFAttributeDataProperty buildAttributeDataProperty(EMFMetaModel metaModel, EAttribute aAttribute) {
		return new EMFAttributeDataProperty(metaModel, aAttribute);
	}

	/**
	 * Build Attribute Object Property.
	 * 
	 * @param aReference
	 * @return
	 */
	public EMFAttributeObjectProperty buildAttributeObjectProperty(EMFMetaModel metaModel, EAttribute aAttribute) {
		return new EMFAttributeObjectProperty(metaModel, aAttribute);
	}

	/**
	 * Build Reference Association.
	 * 
	 * @param aReference
	 * @return
	 */
	public EMFReferenceAssociation buildReferenceAssociation(EMFMetaModel metaModel, EReference aReference) {
		return new EMFReferenceAssociation(metaModel, aReference);
	}

	/**
	 * Build Reference Object Property.
	 * 
	 * @param aReference
	 * @return
	 */
	public EMFReferenceObjectProperty buildReferenceObjectProperty(EMFMetaModel metaModel, EReference aReference) {
		return new EMFReferenceObjectProperty(metaModel, aReference);
	}
}
