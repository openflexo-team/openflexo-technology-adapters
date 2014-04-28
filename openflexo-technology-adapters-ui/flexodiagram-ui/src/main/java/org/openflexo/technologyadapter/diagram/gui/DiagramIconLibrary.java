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
package org.openflexo.technologyadapter.diagram.gui;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.icon.IconLibrary;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.ImageIconResource;

/**
 * Utility class containing all icons used in context of VEModule
 * 
 * @author sylvain
 * 
 */
public class DiagramIconLibrary extends IconLibrary {

	private static final Logger logger = Logger.getLogger(DiagramIconLibrary.class.getPackage().getName());
	
	public static final ImageIconResource DIAGRAM_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Diagram_big.png"));
	public static final ImageIconResource DIAGRAM_MEDIUM_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Diagram_medium.png"));
	

	public static final ImageIconResource DIAGRAM_PALETTE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/DiagramPalette.png"));
	public static final ImageIconResource DIAGRAM_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Diagram.png"));
	public static final ImageIconResource SHAPE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/DiagramShape.png"));
	public static final ImageIconResource CONNECTOR_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/DiagramConnector.png"));
	
	public static final ImageIconResource FML_PALETTE_ELEMENT_BINDING_ICON_16X16 = new ImageIconResource(ResourceLocator.locateResource("Icons/FMLPaletteElementBindingIcon16x16.png"));
	public static final ImageIconResource FML_PALETTE_ELEMENT_BINDING_ICON_32X32 = new ImageIconResource(ResourceLocator.locateResource("Icons/FMLPaletteElementBindingIcon32x32.png"));
	public static final ImageIconResource FML_PALETTE_ELEMENT_BINDING_ICON_64X64 = new ImageIconResource(ResourceLocator.locateResource("Icons/FMLPaletteElementBindingIcon64x64.png"));
	
	public static final ImageIconResource DROP_SCHEME_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/DropSchemeIcon.png"));
	public static final ImageIconResource LINK_SCHEME_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/LinkSchemeIcon.png"));
	public static final ImageIconResource NAVIGATION_SCHEME_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/NavigationSchemeIcon.png"));

	public static final ImageIconResource DIAGRAM_SPECIFICATION_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/DiagramSpecification.png"));

	public static final ImageIconResource GRAPHICAL_ACTION_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/GraphicalActionIcon.png"));

	public static final ImageIconResource UNKNOWN_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/UnknownIcon.gif"));

	public static ImageIcon iconForObject(DiagramElement<?> object) {
		if (object instanceof Diagram) {
			return DIAGRAM_ICON;
		} else if (object instanceof DiagramConnector) {
			return CONNECTOR_ICON;
		} else if (object instanceof DiagramShape) {
			return SHAPE_ICON;
		} else if (object instanceof DiagramSpecification) {
			return DIAGRAM_SPECIFICATION_ICON;
		} else if (object instanceof DiagramPalette) {
			return DIAGRAM_PALETTE_ICON;
		} else if (object instanceof DiagramPaletteElement) {
			return SHAPE_ICON;
		} 
		logger.warning("No icon for " + object.getClass());
		return UNKNOWN_ICON;
	}

}
