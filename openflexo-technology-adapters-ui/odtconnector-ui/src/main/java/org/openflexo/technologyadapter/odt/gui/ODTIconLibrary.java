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

package org.openflexo.technologyadapter.odt.gui;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.toolbox.ImageIconResource;

public class ODTIconLibrary {
	private static final Logger logger = Logger.getLogger(ODTIconLibrary.class.getPackage().getName());

	public static final ImageIcon ODT_TECHNOLOGY_BIG_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/OpenOfficeIcon_32x32.png"));
	public static final ImageIcon ODT_TECHNOLOGY_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/OpenOfficeIcon_16x16.png"));
	public static final ImageIcon ODT_FILE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/ODTFile_16x16.png"));

	public static ImageIcon iconForObject(Class<? extends TechnologyObject<?>> objectClass) {
		return null;
	}
}
