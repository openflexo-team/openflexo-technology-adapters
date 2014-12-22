package org.openflexo.technologyadapter.xml.fml.binding;

import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.antar.binding.BindingEvaluationContext;
import org.openflexo.antar.binding.BindingPathElement;
import org.openflexo.antar.binding.ParameterizedTypeImpl;
import org.openflexo.antar.binding.SimplePathElement;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.technologyadapter.xml.metamodel.XMLDataProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
import org.openflexo.technologyadapter.xml.model.XMLDataPropertyValue;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;

public class XMLDataPropertyPathElement extends SimplePathElement {

	private final XMLDataProperty property;

	private static final Logger logger = Logger.getLogger(XMLDataPropertyPathElement.class.getPackage().getName());

	public XMLDataPropertyPathElement(BindingPathElement parent, XMLDataProperty property) {
		super(parent, property.getName(), property.getType());
		this.property = property;
	}

	public XMLDataProperty getDataProperty() {
		return property;
	}

	@Override
	public Type getType() {
		if (property  != null) {
			if (property.getUpperBound() == null || (property.getUpperBound() >= 0 && property.getUpperBound() <= 1)) {
				// Single cardinality
				if (property.getType() != null) {
					return property.getType();
				}
				return Object.class;
			} else {
				if (property != null && property.getType() != null) {
					return new ParameterizedTypeImpl(List.class, property.getType());
				}
				return new ParameterizedTypeImpl(List.class, Object.class);
			}
		}
		return null;
	}

	@Override
	public String getLabel() {
		return getPropertyName();
	}

	@Override
	public String getTooltipText(Type resultingType) {
		return "DataAttribute " + property.getDisplayableDescription();
	}

	@Override
	public Object getBindingValue(Object target, BindingEvaluationContext context) throws TypeMismatchException, NullReferenceException {
		XMLDataPropertyValue xsdAnswer = (XMLDataPropertyValue) ((XMLIndividual) target).getPropertyValue(getDataProperty());

		if (xsdAnswer != null){
			return xsdAnswer.getValue();
		}
		return null;
	}

	@Override
	public void setBindingValue(Object value, Object target, BindingEvaluationContext context) throws TypeMismatchException,
	NullReferenceException {
		XMLProperty prop = ((XMLIndividual) target).getType().getPropertyByName(getPropertyName());
		((XMLIndividual) target).addPropertyValue(prop, value);
	}
}
