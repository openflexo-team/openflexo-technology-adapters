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

package org.openflexo.technologyadapter.emf.model.io;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EObjectEList;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividual;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividualAttributeDataPropertyValue;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividualAttributeObjectPropertyValue;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividualReferenceObjectPropertyValue;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividualReferenceObjectPropertyValueAsList;

/**
 * EMF Model Builder.
 * 
 * @author gbesancon
 */
public class EMFModelBuilder {
	/**
	 * Build Model.
	 * 
	 * @param eObject
	 * @return
	 */
	public EMFModel buildModel(EMFMetaModel metaModel, EMFModelConverter converter, Resource resource) {
		return new EMFModel(metaModel, converter, resource);
	}

	/**
	 * Build Object Individual.
	 * 
	 * @param model
	 * @param eObject
	 * @return
	 */
	public EMFObjectIndividual buildObjectIndividual(EMFModel model, EObject eObject) {
		return new EMFObjectIndividual(model, eObject);
	}

	/**
	 * Build Object Individual Attribute Data Property Value.
	 * 
	 * @param model
	 * @param eObject
	 * @param eAttribute
	 * @return
	 */
	public EMFObjectIndividualAttributeDataPropertyValue buildObjectIndividualAttributeDataPropertyValue(EMFModel model, EObject eObject,
			EAttribute eAttribute) {
		return new EMFObjectIndividualAttributeDataPropertyValue(model, eObject, eAttribute);
	}

	/**
	 * Build Object Individual Attribute Object Property Value.
	 * 
	 * @param model
	 * @param eObject
	 * @param eAttribute
	 * @return
	 */
	public EMFObjectIndividualAttributeObjectPropertyValue buildObjectIndividualAttributeObjectPropertyValue(EMFModel model,
			EObject eObject, EAttribute eAttribute) {
		return new EMFObjectIndividualAttributeObjectPropertyValue(model, eObject, eAttribute);
	}

	/**
	 * Build Object Individual Reference Object Property Value.
	 * 
	 * @param model
	 * @param eObject
	 * @param eReference
	 * @return
	 */
	public EMFObjectIndividualReferenceObjectPropertyValue buildObjectIndividualReferenceObjectPropertyValue(EMFModel model,
			EObject eObject, EReference eReference) {

		Object refList = eObject.eGet(eObject.eClass().getEStructuralFeature(eReference.getFeatureID()));

		if (refList instanceof EObjectEList) {
			return new EMFObjectIndividualReferenceObjectPropertyValueAsList(model, eObject, eReference, refList);
		} else {
			return new EMFObjectIndividualReferenceObjectPropertyValue(model, eObject, eReference);
		}
	}
}
