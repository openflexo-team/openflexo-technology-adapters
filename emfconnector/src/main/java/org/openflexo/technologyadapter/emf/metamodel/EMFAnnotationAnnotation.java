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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EAnnotation;
import org.openflexo.foundation.ontology.IFlexoOntologyAnnotation;

/**
 * EMF Annotation annotation.
 * 
 * @author gbesancon
 */
public class EMFAnnotationAnnotation extends AEMFMetaModelObjectImpl<EAnnotation> implements IFlexoOntologyAnnotation {

	/**
	 * Constructor.
	 */
	public EMFAnnotationAnnotation(EMFMetaModel metaModel, EAnnotation annotation) {
		super(metaModel, annotation);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyAnnotation#getName()
	 */
	@Override
	public String getName() {
		return object.getSource();
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
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyAnnotation#getDetails()
	 */
	@Override
	public Map<String, String> getDetails() {
		Map<String, String> details = new HashMap<String, String>();
		for (Entry<String, String> entry : object.getDetails().entrySet()) {
			details.put(entry.getKey(), entry.getValue());
		}
		return Collections.unmodifiableMap(details);
	}

	@Override
	public String getURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) throws Exception {
		// TODO Auto-generated method stub

	}
}
