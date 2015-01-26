/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
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

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public class RangeStatement extends OWLStatement {

	private static final Logger logger = Logger.getLogger(RangeStatement.class.getPackage().getName());

	private OWLConcept<?> range;
	private OWLDataType dataType;

	public OWLDataType getDataType() {
		return dataType;
	}

	public RangeStatement(OWLConcept<?> subject, Statement s, OWLTechnologyAdapter adapter) {
		super(subject, s, adapter);
		if (s.getObject() instanceof Resource) {
			// System.out.println("RangeStatement, subject=" + subject + " prodicate=" + s.getPredicate());
			range = getOntology().retrieveOntologyObject((Resource) s.getObject());
			// System.out.println("RangeStatement, range=" + range);
			// If range cannot be found, try to lookup a datatype
			if (range == null && ((Resource) s.getObject()).getURI() != null) {
				dataType = getTechnologyAdapter().getOntologyLibrary().getDataType(((Resource) s.getObject()).getURI());
			}
		} else {
			logger.warning("RangeStatement: object is not a Resource !");
		}
	}

	public OWLConcept<?> getRange() {
		return range;
	}

	@Override
	public String toString() {
		if (getDataType() != null) {
			return getSubject().getName() + " has range " + getDataType().getURI();
		}
		return getSubject().getName() + " has range "
				+ (getRange() != null ? getRange().getName() : "<NOT_FOUND:" + getStatement().getObject() + ">");
	}

	public String getStringRepresentation() {
		if (getDataType() != null) {
			return getDataType().toString();
		}
		if (getRange() != null) {
			return getRange().getName();
		}
		return "";
	}
}
