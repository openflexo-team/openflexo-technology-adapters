/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.technologyadapter.diagram.metamodel;

import java.util.logging.Logger;

import org.openflexo.diana.ShapeGraphicalRepresentation;
import org.openflexo.foundation.InnerResourceData;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.pamela.validation.Validable;

@ModelEntity
@ImplementationClass(DiagramPaletteElement.DiagramPaletteElementImpl.class)
@XMLElement
public interface DiagramPaletteElement extends DiagramPaletteObject, InnerResourceData<DiagramPalette>, Validable {

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
		public synchronized void setChanged() {
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

		@Override
		public DiagramPalette getResourceData() {
			return getPalette();
		}
	}

}
