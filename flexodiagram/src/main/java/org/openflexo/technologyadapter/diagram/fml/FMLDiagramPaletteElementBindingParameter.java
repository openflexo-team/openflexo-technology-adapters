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

import java.security.URIParameter;

import org.openflexo.connie.BindingModel;
import org.openflexo.foundation.fml.FMLRepresentationContext;
import org.openflexo.foundation.fml.FlexoBehaviourParameter;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptObject;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;

/**
 * Represents a valued parameter in the context of a {@link FMLDiagramPaletteElementBinding}
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@ImplementationClass(FMLDiagramPaletteElementBindingParameter.FMLDiagramPaletteElementBindingParameterImpl.class)
@XMLElement
public interface FMLDiagramPaletteElementBindingParameter extends FlexoConceptObject {

	@PropertyIdentifier(type = FMLDiagramPaletteElementBinding.class)
	public static final String PALETTE_ELEMENT_BINDING_KEY = "paletteElementBinding";
	@PropertyIdentifier(type = FlexoBehaviourParameter.class)
	public static final String PARAMETER_KEY = "parameter";
	@PropertyIdentifier(type = String.class)
	public static final String VALUE_KEY = "value";

	// TODO: remove inverse
	@Getter(value = PALETTE_ELEMENT_BINDING_KEY, inverse = FMLDiagramPaletteElementBinding.PARAMETERS_KEY)
	public FMLDiagramPaletteElementBinding getDiagramPaletteElementBinding();

	@Setter(PALETTE_ELEMENT_BINDING_KEY)
	public void setDiagramPaletteElementBinding(FMLDiagramPaletteElementBinding diagramPaletteElementBinding);

	@Getter(PARAMETER_KEY)
	public FlexoBehaviourParameter getParameter();

	@Setter(PARAMETER_KEY)
	public void setParameter(FlexoBehaviourParameter parameter);

	@Getter(VALUE_KEY)
	@XMLAttribute
	public String getValue();

	@Setter(VALUE_KEY)
	public void setValue(String value);

	public boolean isEditable();

	public abstract class FMLDiagramPaletteElementBindingParameterImpl extends FlexoConceptObjectImpl
			implements FMLDiagramPaletteElementBindingParameter {

		private FlexoBehaviourParameter _parameter;
		// private FMLDiagramPaletteElementBinding elementBinding;
		private String value;

		public FMLDiagramPaletteElementBindingParameterImpl() {
			super();
		}

		public FMLDiagramPaletteElementBindingParameterImpl(FlexoBehaviourParameter p) {
			super();
			_parameter = p;
			setName(p.getName());
			setValue(p.getDefaultValue().toString());
		}

		@Override
		public FlexoConcept getFlexoConcept() {
			return getDiagramPaletteElementBinding().getFlexoConcept();
		}

		@Override
		public String getURI() {
			return null;
		}

		@Override
		public String getValue() {
			if (getParameter() != null) {
				/*if (getParameter() instanceof URIParameter) {
					return "< Computed URI >";
				}*/
				/*if (getParameter().getUsePaletteLabelAsDefaultValue()) {
					return "< Takes palette element label >";
				}*/
			}
			return value;
		}

		@Override
		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public boolean isEditable() {
			if (getParameter() != null) {
				return !(getParameter() instanceof URIParameter) /*&& !getParameter().getUsePaletteLabelAsDefaultValue()*/;
			}
			return true;
		}

		public DiagramPalette getPalette() {
			if (getDiagramPaletteElementBinding() != null) {
				return getDiagramPaletteElementBinding().getPaletteElement().getPalette();
			}
			return null;
		}

		/*public void setElementBinding(FMLDiagramPaletteElementBinding elementBinding) {
			this.elementBinding = elementBinding;
		}
		
		public FMLDiagramPaletteElementBinding getElementBinding() {
			return elementBinding;
		}*/

		@Override
		public FlexoBehaviourParameter getParameter() {
			return _parameter;
		}

		@Override
		public void setParameter(FlexoBehaviourParameter parameter) {
			_parameter = parameter;
		}

		@Override
		public BindingModel getBindingModel() {
			return getDeclaringVirtualModel().getBindingModel();
		}

		@Override
		public String getFMLRepresentation(FMLRepresentationContext context) {
			return "<not_implemented:" + getStringRepresentation() + ">";
		}

	}

}
