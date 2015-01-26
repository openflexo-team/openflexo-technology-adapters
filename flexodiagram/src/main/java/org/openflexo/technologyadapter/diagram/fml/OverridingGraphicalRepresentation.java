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

import org.openflexo.antar.binding.BindingModel;
import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.fml.AbstractVirtualModel;
import org.openflexo.foundation.fml.FMLRepresentationContext;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModelObject;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Import;
import org.openflexo.model.annotations.Imports;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;

@ModelEntity(isAbstract = true)
@ImplementationClass(OverridingGraphicalRepresentation.OverridingGraphicalRepresentationImpl.class)
@Imports({ @Import(OverridingGraphicalRepresentation.ShapeOverridingGraphicalRepresentation.class),
		@Import(OverridingGraphicalRepresentation.ConnectorOverridingGraphicalRepresentation.class) })
public interface OverridingGraphicalRepresentation<GR extends GraphicalRepresentation> extends VirtualModelObject {

	@PropertyIdentifier(type = FMLDiagramPaletteElementBinding.class)
	public static final String PALETTE_ELEMENT_BINDING_KEY = "paletteElementBinding";
	@PropertyIdentifier(type = GraphicalElementRole.class)
	public static final String PATTERN_ROLE_KEY = "patternRole";
	@PropertyIdentifier(type = GraphicalRepresentation.class)
	public static final String GRAPHICAL_REPRESENTATION_KEY = "graphicalRepresentation";

	// TODO: remove inverse
	@Getter(value = PALETTE_ELEMENT_BINDING_KEY, inverse = FMLDiagramPaletteElementBinding.OVERRIDING_GRAPHICAL_REPRESENTATIONS_KEY)
	public FMLDiagramPaletteElementBinding getDiagramPaletteElementBinding();

	@Setter(PALETTE_ELEMENT_BINDING_KEY)
	public void setDiagramPaletteElementBinding(FMLDiagramPaletteElementBinding diagramPaletteElementBinding);

	@Getter(value = PATTERN_ROLE_KEY)
	@XMLAttribute
	public GraphicalElementRole<?, GR> getPatternRole();

	@Setter(PATTERN_ROLE_KEY)
	public void setPatternRole(GraphicalElementRole<?, GR> aPatternRole);

	@Getter(value = GRAPHICAL_REPRESENTATION_KEY)
	@XMLElement
	public GR getGraphicalRepresentation();

	@Setter(GRAPHICAL_REPRESENTATION_KEY)
	public void setGraphicalRepresentation(GR graphicalRepresentation);

	public abstract class OverridingGraphicalRepresentationImpl<GR extends GraphicalRepresentation> extends FlexoConceptObjectImpl
			implements OverridingGraphicalRepresentation<GR> {

		// FMLDiagramPaletteElementBinding paletteElementBinding;
		// private String patternRoleName;

		// Do not use, required for deserialization
		public OverridingGraphicalRepresentationImpl() {
			super();
		}

		// Do not use, required for deserialization
		public OverridingGraphicalRepresentationImpl(GraphicalElementRole<?, GR> patternRole) {
			super();
			setPatternRole(patternRole);
			// patternRoleName = patternRole.getPatternRoleName();
		}

		@Override
		public FlexoConcept getFlexoConcept() {
			return getDiagramPaletteElementBinding().getFlexoConcept();
		}

		@Override
		public AbstractVirtualModel<?> getVirtualModel() {
			return getDiagramPaletteElementBinding().getVirtualModel();
		}

		@Override
		public BindingModel getBindingModel() {
			if (getDiagramPaletteElementBinding() != null) {
				return getDiagramPaletteElementBinding().getBindingModel();
			}
			return null;
		}

		/*public FMLDiagramPaletteElementBinding getPaletteElementBinding() {
			return paletteElementBinding;
		}*/

		/*@Override
		public String getPatternRoleName() {
			return patternRoleName;
		}

		@Override
		public void setPatternRoleName(String patternRoleName) {
			this.patternRoleName = patternRoleName;
		}*/

		@Override
		public String getFMLRepresentation(FMLRepresentationContext context) {
			return "<not_implemented:" + getStringRepresentation() + ">";
		}

		public DiagramPalette getPalette() {
			return getDiagramPaletteElementBinding().getPaletteElement().getPalette();
		}

		@Override
		public String getURI() {
			return null;
		}

	}

	@ModelEntity
	@XMLElement
	public static interface ShapeOverridingGraphicalRepresentation extends OverridingGraphicalRepresentation<ShapeGraphicalRepresentation> {
	}

	@ModelEntity
	@XMLElement
	public static interface ConnectorOverridingGraphicalRepresentation extends
			OverridingGraphicalRepresentation<ConnectorGraphicalRepresentation> {
	}

}
