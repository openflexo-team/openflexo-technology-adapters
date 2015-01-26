/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Xmlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.xml.metamodel;

import org.openflexo.toolbox.StringUtils;

/**
 * An attribute with only simple values
 * @author xtof
 *
 */
public abstract class XMLDataPropertyImpl extends XMLPropertyImpl implements XMLDataProperty  {
	// TODO .... get anything from there
	//private final XSAttributeUse attributeUse = null;


	@Override
	public boolean hasDefaultValue() {
		return StringUtils.isNotEmpty(getDefaultValue());
	}

	@Override
	public String getDefaultValue() {
	/*	if (attributeUse != null) {
			if (attributeUse.getDefaultValue() != null) {
				return attributeUse.getDefaultValue().toString();
			}
		}
		*/
		return null;
	}

	@Override
	public boolean hasFixedValue() {
		return StringUtils.isNotEmpty(getFixedValue());
	}

	@Override
	public String getFixedValue() {
		/*
		if (attributeUse != null) {
			if (attributeUse.getFixedValue() != null) {
				return attributeUse.getFixedValue().toString();
			}
		}*/
		return null;
	}

	@Override
	public boolean isRequired() {
		/* if (attributeUse != null) {
			return attributeUse.isRequired();
		} */
		return false;
	}

	@Override
	public String getDisplayableDescription() {
		StringBuffer buffer = new StringBuffer("Attribute ");
		buffer.append(getName());
		if (isRequired()) {
			buffer.append("required");
		} else {
			buffer.append("optional");
		}
		if (hasDefaultValue()) {
			buffer.append(", default: '").append(getDefaultValue()).append("'");
		}
		if (hasFixedValue()) {
			buffer.append(", fixed: '").append(getFixedValue()).append("'");
		}
		return buffer.toString();
	}

	@Override
	public Integer getLowerBound() {
		if (isRequired())
			return 1;
		else
			return 0;
	}

	@Override
	public Integer getUpperBound() {
		return 1;
	}

}
