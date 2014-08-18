/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.technologyadapter.xml.metamodel;

import org.openflexo.toolbox.StringUtils;

public abstract class XMLObjectPropertyImpl extends XMLPropertyImpl implements XMLObjectProperty {


	@Override
	public boolean hasDefaultValue() {
		return StringUtils.isNotEmpty(getDefaultValue());
	}

	@Override
	public String getDefaultValue() {
		return null;
	}

	@Override
	public boolean hasFixedValue() {
		return StringUtils.isNotEmpty(getFixedValue());
	}

	@Override
	public String getFixedValue() {
		return null;
	}

	@Override
	public boolean isRequired() {
		return false;
	}

	@Override
	public String getDisplayableDescription() {
		StringBuffer buffer = new StringBuffer("InnerElement ");
		buffer.append(" (").append(getType().toString()).append(") is ");
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
		// Object Properties are unbound
		return -1;
	}

	@Override
	public boolean isFromXMLElement() {
		return true;
	}

}
