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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.foundation.ontology.IFlexoOntologyConceptVisitor;
import org.openflexo.foundation.ontology.IFlexoOntologyFeature;
import org.openflexo.foundation.ontology.IFlexoOntologyFeatureAssociation;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.model.XSOntIndividual;
import org.openflexo.technologyadapter.xml.model.XSOntology;
import org.openflexo.toolbox.StringUtils;

import com.sun.xml.xsom.XSAttributeUse;

public class XMLDataProperty extends XMLAttribute  {

	private XSDDataType dataType;
	private boolean isFromAttribute = false;
	// FIXME : attributeUse is null most of the time => when an element is used to define a property
	private XSAttributeUse attributeUse = null;

	protected XMLDataProperty(XSOntology ontology, String name, String uri, XMLTechnologyAdapter adapter) {
		super(ontology, name, uri, adapter);
	}

	protected XMLDataProperty(XSOntology ontology, String name, String uri, XSOntClass domainClass, XMLTechnologyAdapter adapter) {
		super(ontology, name, uri, adapter);
		this.domain = domainClass;
	}

	protected XMLDataProperty(XSOntology ontology, String name, String uri, XSOntClass domainClass, XSAttributeUse attributeUse,
			XMLTechnologyAdapter adapter) {
		super(ontology, name, uri, adapter);
		this.domain = domainClass;
		this.attributeUse = attributeUse;
	}

	@Override
	public List<XMLDataProperty> getSuperProperties() {
		// TODO Make sure it's always empty
		return new ArrayList<XMLDataProperty>();
	}

	@Override
	public List<XMLDataProperty> getSubProperties(IFlexoOntology<XMLTechnologyAdapter> context) {
		// TODO Make sure it's always empty
		return new ArrayList<XMLDataProperty>();
	}

	@Override
	public XSDDataType getRange() {
		return getDataType();
	}

	public XSDDataType getDataType() {
		return dataType;
	}

	public void setDataType(XSDDataType dataType) {
		this.dataType = dataType;
	}

	public void setIsFromAttribute(boolean isFromAttribute) {
		this.isFromAttribute = isFromAttribute;
	}

	public boolean getIsFromAttribute() {
		return isFromAttribute;
	}

	public boolean hasDefaultValue() {
		return StringUtils.isNotEmpty(getDefaultValue());
	}

	public String getDefaultValue() {
		if (attributeUse != null) {
			if (attributeUse.getDefaultValue() != null) {
				return attributeUse.getDefaultValue().toString();
			}
		}
		return null;
	}

	public boolean hasFixedValue() {
		return StringUtils.isNotEmpty(getFixedValue());
	}

	public String getFixedValue() {
		if (attributeUse != null) {
			if (attributeUse.getFixedValue() != null) {
				return attributeUse.getFixedValue().toString();
			}
		}
		return null;
	}

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
		buffer.append(" (").append(getRange().toString()).append(") is ");
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

	@Override
	public boolean isSimpleAttribute() {
		return true;
	}

	@Override
	public boolean isElement() {
		return !isFromAttribute;
	}

	@Override
	public void addValue(IXMLIndividual<?, ?> indiv, Object value) {

		XSOntIndividual anIndividual = (XSOntIndividual) indiv;
		anIndividual.addToPropertyValue(this, value);

	}

	@Override
	public <T> T accept(IFlexoOntologyConceptVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public List<? extends IFlexoOntologyFeatureAssociation<XMLTechnologyAdapter>> getReferencingFeatureAssociations() {
		return Collections.emptyList();
	}

	@Override
	public IFlexoOntologyFeature<XMLTechnologyAdapter> getFeature() {
		return this;
	}

}
