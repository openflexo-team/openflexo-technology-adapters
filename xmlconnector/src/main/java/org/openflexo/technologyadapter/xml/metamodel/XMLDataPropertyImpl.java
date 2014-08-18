/*
 * (c) Copyright 2010-2011 AgileBirds
 * (c) Copyright 2012-2014 Openflexo
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

import com.sun.xml.xsom.XSAttributeUse;

/**
 * An attribute with only simple values
 * @author xtof
 *
 */
public abstract class XMLDataPropertyImpl extends XMLPropertyImpl implements XMLDataProperty  {
	private final XSAttributeUse attributeUse = null;


	@Override
	public boolean hasDefaultValue() {
		return StringUtils.isNotEmpty(getDefaultValue());
	}

	@Override
	public String getDefaultValue() {
		if (attributeUse != null) {
			if (attributeUse.getDefaultValue() != null) {
				return attributeUse.getDefaultValue().toString();
			}
		}
		return null;
	}

	@Override
	public boolean hasFixedValue() {
		return StringUtils.isNotEmpty(getFixedValue());
	}

	@Override
	public String getFixedValue() {
		if (attributeUse != null) {
			if (attributeUse.getFixedValue() != null) {
				return attributeUse.getFixedValue().toString();
			}
		}
		return null;
	}

	@Override
	public boolean isRequired() {
		if (attributeUse != null) {
			return attributeUse.isRequired();
		}
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
