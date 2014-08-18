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

import java.util.ArrayList;
import java.util.List;

import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.foundation.ontology.IFlexoOntologyConceptVisitor;
import org.openflexo.foundation.ontology.IFlexoOntologyFeatureAssociation;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.toolbox.StringUtils;

import com.sun.xml.xsom.XSAttributeUse;

public class XMLObjectPropertyImpl extends XMLAttributeImpl implements XMLObjectProperty {

	private XSOntClass range;
	private boolean noRangeFoundYet = true;

	private List<XMLObjectPropertyImpl> superProperties;

	protected XMLObjectPropertyImpl(XSOntology ontology, String name, String uri, XMLTechnologyAdapter adapter) {
		super(ontology, name, uri, adapter);
		range = ontology.getRootConcept();
		superProperties = new ArrayList<XMLObjectPropertyImpl>();
	}

	protected XMLObjectPropertyImpl(XSOntology ontology, String name, XMLTechnologyAdapter adapter) {
		this(ontology, name, XS_ONTOLOGY_URI + "#" + name, adapter);
	}

	protected XMLObjectPropertyImpl(XSOntology ontology, String name, XSOntClass domainClass, XMLTechnologyAdapter adapter) {
		super(ontology, name, XS_ONTOLOGY_URI + "#" + name, adapter);
	}

	protected XMLObjectPropertyImpl(XSOntology ontology, String name, XSOntClass domainClass, XSAttributeUse attributeUse,
			XMLTechnologyAdapter adapter) {
		super(ontology, name, XS_ONTOLOGY_URI + "#" + name, adapter);
	}

	public void addSuperProperty(XMLObjectPropertyImpl parent) {
		superProperties.add(parent);
	}

	@Override
	public void clearSuperProperties() {
		superProperties.clear();
	}

	@Override
	public List<XMLObjectPropertyImpl> getSuperProperties() {
		return superProperties;
	}

	@Override
	public List<XMLObjectPropertyImpl> getSubProperties(IFlexoOntology<XMLTechnologyAdapter> context) {
		// TODO
		return new ArrayList<XMLObjectPropertyImpl>();
	}

	@Override
	public XSOntClass getRange() {
		return range;
	}

	public void newRangeFound(XSOntClass range) {
		if (noRangeFoundYet) {
			this.range = range;
			noRangeFoundYet = false;
		} else {
			this.range = getOntology().getRootConcept();
		}
	}

	@Override
	public void resetRange() {
		this.range = getOntology().getRootConcept();
		noRangeFoundYet = true;
	}

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
	public XMLObjectPropertyImpl getFeature() {
		return this;
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
	public boolean isSimpleAttribute() {
		return false;
	}

	@Override
	public boolean isElement() {
		return true;
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
		// TODO Auto-generated method stub
		return null;
	}

}