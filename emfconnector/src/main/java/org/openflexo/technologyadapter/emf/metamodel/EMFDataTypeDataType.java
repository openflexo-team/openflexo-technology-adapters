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

package org.openflexo.technologyadapter.emf.metamodel;

import org.eclipse.emf.ecore.EDataType;
import org.openflexo.foundation.ontology.BuiltInDataType;
import org.openflexo.foundation.ontology.IFlexoOntologyDataType;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;

/**
 * EMF Data Type.
 * 
 * @author gbesancon
 */
public class EMFDataTypeDataType extends AEMFMetaModelObjectImpl<EDataType>implements IFlexoOntologyDataType<EMFTechnologyAdapter> {

	/**
	 * Constructor.
	 */
	public EMFDataTypeDataType(EMFMetaModel metaModel, EDataType aDataType) {
		super(metaModel, aDataType);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getURI()
	 */
	@Override
	public String getURI() {
		return EMFMetaModelURIBuilder.getUri(object);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getName()
	 */
	@Override
	public String getName() {
		return object.getName();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyObject#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) throws Exception {
		System.out.println("Name can't be modified.");
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getDescription()
	 */
	@Override
	public String getDescription() {
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
		return getDescription();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyObject#getTechnologyAdapter()
	 */
	@Override
	public EMFTechnologyAdapter getTechnologyAdapter() {
		return ontology.getTechnologyAdapter();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyDataType#getAccessedType()
	 */
	@Override
	public Class<?> getAccessedType() {
		Class<?> result = null;
		if (getBuiltInDataType() != null) {
			result = getBuiltInDataType().getAccessedType();
		}
		else {
			result = object.getInstanceClass();
		}
		return result;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyDataType#getBuiltInDataType()
	 */
	@Override
	public BuiltInDataType getBuiltInDataType() {
		return BuiltInDataType.fromType(object.getInstanceClass());
	}

	@Override
	public String toString() {
		if (getAccessedType() != null) {
			return getAccessedType().getSimpleName();
		}
		return super.toString();
	}
}
