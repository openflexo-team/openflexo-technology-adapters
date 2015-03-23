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

package org.openflexo.technologyadapter.emf.model;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.openflexo.foundation.ontology.IFlexoOntologyDataProperty;
import org.openflexo.foundation.ontology.IFlexoOntologyDataPropertyValue;
import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;

/**
 * EMF Object Individual Attribute Data Property Value.
 * 
 * @author gbesancon
 */
public class EMFObjectIndividualAttributeDataPropertyValue extends AEMFModelObjectImpl<EObject> implements
		IFlexoOntologyDataPropertyValue<EMFTechnologyAdapter> {

	/** Attribute. */
	protected final EAttribute attribute;

	/**
	 * Constructor.
	 */
	public EMFObjectIndividualAttributeDataPropertyValue(EMFModel model, EObject eObject, EAttribute aAttribute) {
		super(model, eObject);
		this.attribute = aAttribute;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.FlexoOntologyObjectImpl#getName()
	 */
	@Override
	public String getName() {
		return attribute.getName();
	}

	@Override
	public void setName(String name) throws Exception {
		attribute.setName(name);
	}

	@Override
	public String getURI() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.FlexoOntologyObjectImpl#getDisplayableDescription()
	 */
	@Override
	public String getDisplayableDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyDataPropertyValue#getDataProperty()
	 */
	@Override
	public IFlexoOntologyDataProperty<EMFTechnologyAdapter> getDataProperty() {
		return ontology.getMetaModel().getConverter().convertAttributeDataProperty(ontology.getMetaModel(), attribute,null);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyPropertyValue#getProperty()
	 */
	@Override
	public IFlexoOntologyStructuralProperty<EMFTechnologyAdapter> getProperty() {
		return getDataProperty();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyDataPropertyValue#getValue()
	 */
	@Override
	public List<Object> getValues() {
		List<Object> result = null;
		if (object.eGet(attribute) != null) {
			if (attribute.getUpperBound() == 1) {
				result = Collections.singletonList(object.eGet(attribute));
			} else {
				result = (List<Object>) object.eGet(attribute);
			}
		} else {
			result = Collections.emptyList();
		}
		return Collections.unmodifiableList(result);
	}

}
