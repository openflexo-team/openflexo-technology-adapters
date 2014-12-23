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
package org.openflexo.technologyadapter.diagram.fml;

import org.openflexo.antar.binding.BindingModel;
import org.openflexo.foundation.fml.FMLRepresentationContext;
import org.openflexo.foundation.fml.FlexoBehaviourParameter;
import org.openflexo.foundation.fml.NamedFMLObject;
import org.openflexo.foundation.fml.URIParameter;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
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
public interface FMLDiagramPaletteElementBindingParameter extends NamedFMLObject {

	@PropertyIdentifier(type = FMLDiagramPaletteElementBinding.class)
	public static final String PALETTE_ELEMENT_BINDING_KEY = "paletteElementBinding";
	@PropertyIdentifier(type = FlexoBehaviourParameter.class)
	public static final String PARAMETER_KEY = "parameter";
	@PropertyIdentifier(type = String.class)
	public static final String VALUE_KEY = "value";

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

	public abstract class FMLDiagramPaletteElementBindingParameterImpl extends NamedFMLObjectImpl implements
			FMLDiagramPaletteElementBindingParameter {

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
		public String getURI() {
			return null;
		}

		@Override
		public String getValue() {
			if (getParameter() != null) {
				if (getParameter() instanceof URIParameter) {
					return "< Computed URI >";
				}
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

		@Override
		public ViewPoint getViewPoint() {
			if (getVirtualModel() != null) {
				return getVirtualModel().getViewPoint();
			}
			return null;
		}

		public VirtualModel getVirtualModel() {
			if (getDiagramPaletteElementBinding() != null) {
				return getDiagramPaletteElementBinding().getVirtualModel();
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
			return getViewPoint().getBindingModel();
		}

		@Override
		public String getFMLRepresentation(FMLRepresentationContext context) {
			return "<not_implemented:" + getStringRepresentation() + ">";
		}

	}

}
