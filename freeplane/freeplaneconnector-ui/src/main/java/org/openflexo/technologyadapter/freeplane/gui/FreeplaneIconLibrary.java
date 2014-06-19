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

package org.openflexo.technologyadapter.freeplane.gui;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.toolbox.ImageIconResource;

public class FreeplaneIconLibrary {
	private static final Logger		logger							= Logger.getLogger(FreeplaneIconLibrary.class.getPackage().getName());

	public static final ImageIcon	FREEPLANE_TECHNOLOGY_BIG_ICON	= new ImageIconResource(ResourceLocator.locateResource("Icons/Freeplane_frame_icon_32x32.png"));
	public static final ImageIcon	FREEPLANE_TECHNOLOGY_ICON		= new ImageIconResource(ResourceLocator.locateResource("Icons/Freeplane_frame_icon.png"));
	public static final ImageIcon	FREEPLANE_FILE_ICON				= FREEPLANE_TECHNOLOGY_ICON;

	public static ImageIcon iconForObject(final Class<? extends TechnologyObject> objectClass) {
		return FREEPLANE_TECHNOLOGY_ICON;
	}
}
