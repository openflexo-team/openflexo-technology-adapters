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
package org.openflexo.technologyadapter.diagram.metamodel;

import java.util.logging.Logger;

import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;

@ModelEntity
@ImplementationClass(DiagramPaletteElement.DiagramPaletteElementImpl.class)
@XMLElement
public interface DiagramPaletteElement extends DiagramPaletteObject {

	@PropertyIdentifier(type = DiagramPalette.class)
	public static final String PALETTE_KEY = "palette";
	@PropertyIdentifier(type = ShapeGraphicalRepresentation.class)
	public static final String GRAPHICAL_REPRESENTATION_KEY = "graphicalRepresentation";

	@Override
	@Getter(value = PALETTE_KEY, ignoreType = true, inverse = DiagramPalette.PALETTE_ELEMENTS_KEY)
	public DiagramPalette getPalette();

	@Setter(PALETTE_KEY)
	public void setPalette(DiagramPalette palette);

	@Getter(value = GRAPHICAL_REPRESENTATION_KEY)
	@XMLElement
	public ShapeGraphicalRepresentation getGraphicalRepresentation();

	@Setter(value = GRAPHICAL_REPRESENTATION_KEY)
	public void setGraphicalRepresentation(ShapeGraphicalRepresentation graphicalRepresentation);

	public abstract static class DiagramPaletteElementImpl extends DiagramPaletteObjectImpl implements DiagramPaletteElement {

		private static final Logger logger = Logger.getLogger(DiagramPaletteElement.class.getPackage().getName());

		// Represent graphical representation to be used as representation in the palette
		// private ShapeGraphicalRepresentation graphicalRepresentation;

		private final DiagramPaletteElement parent = null;

		// private String name;
		private DiagramPalette _palette;

		/*public FlexoServiceManager getServiceManager() {
			return getPalette().getServiceManager();
		}*/

		/*@Override
		public String getName() {
			return name;
		}

		@Override
		public void setName(String name) {
			if (requireChange(this.name, name)) {
				String oldName = this.name;
				this.name = name;
				setChanged();
				notifyObservers(new NameChanged(oldName, name));
			}
		}*/

		public String getURI() {
			return getPalette().getURI() + "/" + getName();
		}

		@Override
		public DiagramSpecification getDiagramSpecification() {
			if (getPalette() != null) {
				return getPalette().getDiagramSpecification();
			}
			return null;
		}

		@Override
		public DiagramPalette getPalette() {
			return _palette;
		}

		@Override
		public void setPalette(DiagramPalette palette) {
			_palette = palette;
		}

		public DiagramPaletteElement getParent() {
			return parent;
		}

		@Override
		public void setChanged() {
			super.setChanged();
			if (getPalette() != null) {
				getPalette().setIsModified();
			}
		}

		@Override
		public boolean delete(Object... context) {
			if (getPalette() != null) {
				getPalette().removeFromElements(this);
			}
			performSuperDelete(context);
			deleteObservers();
			return true;
		}

		/*@Override
		public ShapeGraphicalRepresentation getGraphicalRepresentation() {
			return graphicalRepresentation;
		}

		@Override
		public void setGraphicalRepresentation(ShapeGraphicalRepresentation graphicalRepresentation) {
			this.graphicalRepresentation = graphicalRepresentation;
		}*/

	}

}
