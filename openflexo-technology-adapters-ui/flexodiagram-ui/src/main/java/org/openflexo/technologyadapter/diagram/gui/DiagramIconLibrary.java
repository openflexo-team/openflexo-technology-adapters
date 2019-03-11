/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.gui;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.icon.IconLibrary;
import org.openflexo.icon.IconMarker;
import org.openflexo.icon.ImageIconResource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

/**
 * Utility class containing all icons used in context of VEModule
 * 
 * @author sylvain
 * 
 */
public class DiagramIconLibrary extends IconLibrary {

	private static final Logger logger = Logger.getLogger(DiagramIconLibrary.class.getPackage().getName());

	public static final ImageIconResource DIAGRAM_BIG_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/Diagram-64x64.png"));
	public static final ImageIconResource DIAGRAM_MEDIUM_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/Diagram-32x32.png"));

	public static final ImageIconResource DIAGRAM_PALETTE_BIG_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/DiagramPalette64x64.png"));
	public static final ImageIconResource DIAGRAM_PALETTE_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/DiagramPalette.png"));
	public static final ImageIconResource DIAGRAM_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Diagram-16x16.png"));
	public static final ImageIconResource SHAPE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/DiagramShape.png"));
	public static final ImageIconResource CONNECTOR_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/DiagramConnector.png"));

	public static final IconMarker SHAPE_MARKER = new IconMarker(SHAPE_ICON, 20, 20);
	public static final IconMarker CONNECTOR_MARKER = new IconMarker(CONNECTOR_ICON, 20, 20);

	public static final ImageIconResource FML_PALETTE_ELEMENT_BINDING_ICON_16X16 = new ImageIconResource(
			ResourceLocator.locateResource("Icons/FMLPaletteElementBindingIcon16x16.png"));
	public static final ImageIconResource FML_PALETTE_ELEMENT_BINDING_ICON_32X32 = new ImageIconResource(
			ResourceLocator.locateResource("Icons/FMLPaletteElementBindingIcon32x32.png"));
	public static final ImageIconResource FML_PALETTE_ELEMENT_BINDING_ICON_64X64 = new ImageIconResource(
			ResourceLocator.locateResource("Icons/FMLPaletteElementBindingIcon64x64.png"));

	public static final ImageIconResource DROP_SCHEME_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/DropSchemeIcon.png"));
	public static final ImageIconResource DRAW_RECTANGLE_SCHEME_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/DrawRectangleSchemeIcon.png"));
	public static final ImageIconResource LINK_SCHEME_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/LinkSchemeIcon.png"));
	public static final ImageIconResource NAVIGATION_SCHEME_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/NavigationSchemeIcon.png"));

	public static final ImageIconResource DIAGRAM_SPECIFICATION_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/DiagramSpecification.png"));

	public static final ImageIconResource GRAPHICAL_ACTION_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/GraphicalActionIcon.png"));

	public static final IconMarker DROP_SCHEME_MARKER = new IconMarker(DROP_SCHEME_ICON, 45, 0);

	public static final ImageIconResource UNKNOWN_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/UnknownIcon.gif"));

	public static ImageIcon iconForObject(FlexoObject object) {
		if (object instanceof Diagram) {
			return DIAGRAM_ICON;
		}
		else if (object instanceof DiagramConnector) {
			return CONNECTOR_ICON;
		}
		else if (object instanceof DiagramShape) {
			return SHAPE_ICON;
		}
		else if (object instanceof DiagramSpecification) {
			return DIAGRAM_SPECIFICATION_ICON;
		}
		else if (object instanceof DiagramPalette) {
			return DIAGRAM_PALETTE_ICON;
		}
		else if (object instanceof DiagramPaletteElement) {
			return SHAPE_ICON;
		}
		else if (object instanceof FMLDiagramPaletteElementBinding) {
			return FML_PALETTE_ELEMENT_BINDING_ICON_16X16;
		}
		logger.warning("No icon for " + object.getClass());
		return UNKNOWN_ICON;
	}

}
