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

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.rm.DiagramPaletteResource;

@ModelEntity(isAbstract = true)
@ImplementationClass(DiagramPaletteObject.DiagramPaletteObjectImpl.class)
public interface DiagramPaletteObject extends TechnologyObject<DiagramTechnologyAdapter> {

	public static final String NAME = "name";

	public abstract DiagramPalette getPalette();

	public abstract DiagramSpecification getDiagramSpecification();

	public DiagramPaletteFactory getFactory();

	/**
	 * Return name of this diagram element
	 * 
	 * @return
	 */
	@Getter(value = NAME)
	@XMLAttribute
	public String getName();

	/**
	 * Sets name of this diagram element
	 * 
	 * @param aName
	 */
	@Setter(value = NAME)
	public void setName(String aName);

	public static abstract class DiagramPaletteObjectImpl extends FlexoObjectImpl implements DiagramPaletteObject {

		@Override
		public abstract DiagramPalette getPalette();

		@Override
		public abstract DiagramSpecification getDiagramSpecification();

		@Override
		public final DiagramPaletteFactory getFactory() {
			return ((DiagramPaletteResource) getPalette().getResource()).getFactory();
		}

	}

}
