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

import org.eclipse.emf.ecore.EObject;
import org.openflexo.foundation.InnerResourceData;
import org.openflexo.foundation.ontology.FlexoOntologyObjectImpl;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;

/**
 * Abstract Simple implementation of Flexo ontology object.
 * 
 * @author gbesancon
 * 
 */
public abstract class AEMFModelObjectImpl<T extends EObject> extends FlexoOntologyObjectImpl<EMFTechnologyAdapter> implements InnerResourceData<EMFModel>{

	/** MetaModel. */
	protected final EMFModel ontology;
	/** EMF Object Wrapped. */
	protected final T object;

	/**
	 * Constructor.
	 */
	public AEMFModelObjectImpl(EMFModel ontology, T object) {
		this.ontology = ontology;
		this.object = object;
	}

	public EMFTechnologyAdapter getTechnologyAdapter() {
		return getEMFModel().getTechnologyAdapter();
	}


	/**
	 * Return the {@link ResourceData} where this object is defined (the global functional root object giving access to the
	 * {@link FlexoResource})
	 * 
	 * @return
	 */
	@Override
	public EMFModel getResourceData(){
		return ontology;
	}
	
	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.FlexoOntologyObjectImpl#getFlexoOntology()
	 */
	@Override
	public EMFModel getFlexoOntology() {
		return ontology;
	}

	/**
	 * Return the EMF model this object belongs to
	 * 
	 * @return
	 */
	public EMFModel getEMFModel() {
		return ontology;
	}

	/**
	 * Return the wrapped objects.
	 * 
	 * @return
	 */
	public T getObject() {
		return object;
	}

}
