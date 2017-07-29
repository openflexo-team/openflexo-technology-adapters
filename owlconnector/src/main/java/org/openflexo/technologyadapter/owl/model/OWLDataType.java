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

import java.util.logging.Level;

import org.apache.jena.rdf.model.Literal;
import org.openflexo.foundation.ontology.BuiltInDataType;
import org.openflexo.foundation.ontology.IFlexoOntologyDataType;
import org.openflexo.foundation.ontology.W3URIDefinitions;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;
import org.openflexo.toolbox.StringUtils;

public class OWLDataType extends OWLObject implements IFlexoOntologyDataType<OWLTechnologyAdapter>, W3URIDefinitions {

	private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger
			.getLogger(OWLDataType.class.getPackage().getName());

	private final String dataTypeURI;
	private BuiltInDataType builtInDataType;

	protected OWLDataType(String dataTypeURI, OWLTechnologyAdapter adapter) {
		super(adapter);
		this.dataTypeURI = dataTypeURI;
		builtInDataType = BuiltInDataType.fromURI(getURI());
		if (builtInDataType == null) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Could not map a data type: " + getURI() + ", String will be used instead.");
			}
			builtInDataType = BuiltInDataType.String;
		}
	}

	@Override
	public String getURI() {
		return dataTypeURI;
	}

	@Override
	public String getName() {
		if (StringUtils.isNotEmpty(getURI())) {
			if (getURI().lastIndexOf("/") > -1) {
				return getURI().substring(getURI().lastIndexOf("/") + 1);
			}
			if (getURI().lastIndexOf("\\") > -1) {
				return getURI().substring(getURI().lastIndexOf("\\") + 1);
			}
			return getURI();
		}
		return null;
	}

	// Not relevant
	@Override
	public void setName(String name) throws Exception {
	}

	@Override
	public String getDescription() {
		return getDisplayableDescription();
	}

	@Override
	public String getDisplayableDescription() {
		return getURI();
	}

	@Override
	public Class<?> getAccessedType() {
		return getBuiltInDataType().getAccessedType();
	}

	@Override
	public BuiltInDataType getBuiltInDataType() {
		return builtInDataType;
	}

	public Object valueFromLiteral(Literal literal) {
		if (getBuiltInDataType() != null) {
			switch (getBuiltInDataType()) {
				case String:
					return literal.getString();
				case Boolean:
					return literal.getBoolean();
				case Byte:
					return literal.getByte();
				case Int:
					return literal.getInt();
				case Integer:
					return literal.getInt();
				case Short:
					return literal.getShort();
				case Long:
					return literal.getLong();
				case Float:
					return literal.getFloat();
				case Double:
					return literal.getDouble();
				default:
					logger.warning("Unexpected type: " + getBuiltInDataType());
					break;
			}
		}
		return null;
	}

	@Override
	public OWLOntology getFlexoOntology() {
		return null;
	}

}
