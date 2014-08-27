#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * (c) Copyright 2013- Openflexo
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

package ${package}.library;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.toolbox.ImageIconResource;


public class ${technologyPrefix}IconLibrary {
	private static final Logger logger = Logger.getLogger(${technologyPrefix}IconLibrary.class.getPackage().getName());

	public static final ImageIcon ${technologyPrefix}_TECHNOLOGY_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/${technologyExtension}-text_big.gif"));
	public static final ImageIcon ${technologyPrefix}_TECHNOLOGY_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/${technologyExtension}-text.gif"));
	public static final ImageIcon ${technologyPrefix}_FILE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/${technologyExtension}-text.gif"));

	public static ImageIcon iconForObject(Class<? extends TechnologyObject> objectClass) {
		return ${technologyPrefix}_TECHNOLOGY_ICON;
	}
}
