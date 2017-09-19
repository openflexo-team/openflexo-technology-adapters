/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.fml;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.logging.Logger;

import org.openflexo.connie.BindingFactory;
import org.openflexo.connie.BindingModel;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NotSettableContextException;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptObject;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.validation.Validable;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;

/**
 * This class represent a constraint of graphical feature that is to be applied on a DiagramElement
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@ImplementationClass(GraphicalElementSpecification.GraphicalElementSpecificationImpl.class)
@XMLElement(xmlTag = "GRSpec")
public interface GraphicalElementSpecification<T, GR extends GraphicalRepresentation> extends FlexoConceptObject {

	@PropertyIdentifier(type = String.class)
	public static final String FEATURE_NAME_KEY = "featureName";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String VALUE_KEY = "value";
	@PropertyIdentifier(type = boolean.class)
	public static final String READ_ONLY_KEY = "readOnly";

	@Getter(value = FEATURE_NAME_KEY)
	@XMLAttribute
	public String getFeatureName();

	@Setter(FEATURE_NAME_KEY)
	public void _setFeatureName(String featureName);

	public GraphicalFeature<T, GR> getFeature();

	public void setFeature(GraphicalFeature<T, GR> feature);

	@Getter(value = VALUE_KEY)
	@XMLAttribute
	public DataBinding<T> getValue();

	@Setter(VALUE_KEY)
	public void setValue(DataBinding<T> value);

	@Getter(value = READ_ONLY_KEY, defaultValue = "false")
	@XMLAttribute
	public boolean getReadOnly();

	@Setter(READ_ONLY_KEY)
	public void setReadOnly(boolean readOnly);

	public GraphicalElementRole<?, GR> getFlexoRole();

	public void setFlexoRole(GraphicalElementRole<?, GR> flexoRole);

	public boolean getMandatory();

	public void setMandatory(boolean mandatory);

	public static abstract class GraphicalElementSpecificationImpl<T, GR extends GraphicalRepresentation> extends FlexoConceptObjectImpl
			implements GraphicalElementSpecification<T, GR> {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(GraphicalElementSpecification.class.getPackage().getName());

		private GraphicalElementRole<?, GR> patternRole;
		private GraphicalFeature<T, GR> feature;
		private String featureName;
		private DataBinding<T> value;
		private boolean readOnly;
		private boolean mandatory;

		// Use it only for deserialization
		public GraphicalElementSpecificationImpl() {
			super();
		}

		public GraphicalElementSpecificationImpl(GraphicalElementRole<?, GR> patternRole, GraphicalFeature<T, GR> feature, boolean readOnly,
				boolean mandatory) {
			super();
			this.patternRole = patternRole;
			this.feature = feature;
			this.readOnly = readOnly;
			this.mandatory = mandatory;
		}

		@Override
		public String getURI() {
			return null;
		}

		@Override
		public Collection<? extends Validable> getEmbeddedValidableObjects() {
			return null;
		}

		@Override
		public GraphicalFeature<T, GR> getFeature() {
			return feature;
		}

		@Override
		public void setFeature(GraphicalFeature<T, GR> feature) {
			this.feature = feature;
			if (feature != null && value != null) {
				value.setDeclaredType(feature.getType());
			}
		}

		@Override
		public String getFeatureName() {
			if (feature == null) {
				return featureName;
			}
			return feature.getName();
		}

		@Override
		public void _setFeatureName(String featureName) {
			this.featureName = featureName;
		}

		@Override
		public DataBinding<T> getValue() {
			if (value == null) {
				value = new DataBinding<>(this, (getFeature() != null ? getFeature().getType() : Object.class),
						getReadOnly() ? BindingDefinitionType.GET : BindingDefinitionType.GET_SET);
				value.setBindingName(featureName);
				value.setMandatory(mandatory);
			}
			return value;
		}

		@Override
		public void setValue(DataBinding<T> value) {
			if (value != null) {
				value.setOwner(this);
				value.setDeclaredType(String.class);
				value.setBindingName(featureName);
				value.setMandatory(mandatory);
				value.setBindingDefinitionType(getReadOnly() ? BindingDefinitionType.GET : BindingDefinitionType.GET_SET);
			}
			this.value = value;
		}

		@Override
		public boolean getReadOnly() {
			return readOnly;
		}

		@Override
		public void setReadOnly(boolean readOnly) {
			if (this.readOnly != readOnly) {
				this.readOnly = readOnly;
				getValue().setBindingDefinitionType(getReadOnly() ? BindingDefinitionType.GET : BindingDefinitionType.GET_SET);
				notifiedBindingChanged(getValue());
			}
		}

		@Override
		public boolean getMandatory() {
			return mandatory;
		}

		@Override
		public void setMandatory(boolean mandatory) {
			this.mandatory = mandatory;
		}

		@Override
		public GraphicalElementRole<?, GR> getFlexoRole() {
			return patternRole;
		}

		@Override
		public void setFlexoRole(GraphicalElementRole<?, GR> patternRole) {
			this.patternRole = patternRole;
		}

		@Override
		public FlexoConcept getFlexoConcept() {
			return getFlexoRole() != null ? getFlexoRole().getFlexoConcept() : null;
		}

		@Override
		public BindingFactory getBindingFactory() {
			return getFlexoConcept().getInspector().getBindingFactory();
		}

		@Override
		public BindingModel getBindingModel() {
			if (getFlexoConcept() != null && getFlexoConcept().getInspector() != null) {
				return getFlexoConcept().getInspector().getBindingModel();
			}
			return null;
		}

		/**
		 * This method is called to extract a value from the model and conform to the related feature, and apply it to graphical
		 * representation
		 * 
		 * @param gr
		 * @param element
		 */
		// public void applyToGraphicalRepresentation(GR gr, DiagramElement<GR>
		// element) {
		public void applyToGraphicalRepresentation(FlexoConceptInstance fci, GraphicalElementRole<?, GR> patternRole) {

			try {

				/*
				 * if (getValue().toString().equals("company.companyName")) {
				 * System.out.
				 * println("applyToGraphicalRepresentation in GraphicalElementSpecification for feature "
				 * + getFeatureName()); System.out.println("value=" +
				 * getValue()); System.out.println("valid=" +
				 * getValue().isValid()); System.out.println("reason=" +
				 * getValue().invalidBindingReason()); System.out.println("fci="
				 * + fci); System.out.println("Result=" +
				 * getValue().getBindingValue(fci)); }
				 */

				DiagramElement<GR> diagramElement = fci.getFlexoActor(patternRole);
				getFeature().applyToGraphicalRepresentation(diagramElement.getGraphicalRepresentation(), getValue().getBindingValue(fci));
				// getFeature().applyToGraphicalRepresentation(gr, (T)
				// getValue().getBindingValue(element.getFlexoConceptInstance()));
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		/**
		 * This method is called to extract a value from the graphical representation and conform to the related feature, and apply it to
		 * model
		 * 
		 * @param gr
		 * @param element
		 * @return
		 */
		public T applyToModel(FlexoConceptInstance epi, GraphicalElementRole<?, GR> patternRole) {
			DiagramElement<GR> diagramElement = epi.getFlexoActor(patternRole);
			T newValue = getFeature().retrieveFromGraphicalRepresentation(diagramElement.getGraphicalRepresentation());
			try {
				getValue().setBindingValue(newValue, epi);
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NotSettableContextException e) {
				e.printStackTrace();
			}
			return newValue;
		}

	}
}
