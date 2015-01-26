/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
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

package org.openflexo.technologyadapter.owl.model;

import java.util.logging.Logger;

import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;

public class SomeValuesFromRestrictionClass extends OWLRestriction {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SomeValuesFromRestrictionClass.class.getPackage().getName());

	private final SomeValuesFromRestriction restriction;
	private OWLClass object;
	private OWLDataType dataRange;

	protected SomeValuesFromRestrictionClass(SomeValuesFromRestriction aRestriction, OWLOntology ontology, OWLTechnologyAdapter adapter) {
		super(aRestriction, ontology, adapter);
		this.restriction = aRestriction;
		retrieveRestrictionInformations();
	}

	@Override
	protected void retrieveRestrictionInformations() {
		super.retrieveRestrictionInformations();
		if (restriction.getSomeValuesFrom() != null) {
			if (restriction.getSomeValuesFrom().canAs(OntClass.class)) {
				object = getOntology().retrieveOntologyClass(restriction.getSomeValuesFrom().as(OntClass.class));
			} else {
				dataRange = getTechnologyAdapter().getOntologyLibrary().getDataType(restriction.getSomeValuesFrom().getURI());
			}
		}
	}

	@Override
	public SomeValuesFromRestriction getOntResource() {
		return restriction;
	}

	@Override
	public String getDisplayableDescription() {
		return (getProperty() != null ? getProperty().getName() : "null") + " some " + (object != null ? object.getName() : getDataRange());
	}

	@Override
	public OWLClass getObject() {
		return object;
	}

	@Override
	public OWLDataType getDataRange() {
		return dataRange;
	}

	@Override
	public Integer getLowerBound() {
		return 0;
	}

	@Override
	public Integer getUpperBound() {
		return -1;
	}

}
