/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
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

package org.openflexo.technologyadapter.powerpoint.gui;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.icon.ImageIconResource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointShape;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlide;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlideshow;

public class PowerpointIconLibrary {

	private static final Logger logger = Logger.getLogger(PowerpointIconLibrary.class.getPackage().getName());

	public static final ImageIconResource POWERPOINT_TECHNOLOGY_BIG_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/powerpoint_big.png"));
	public static final ImageIconResource POWERPOINT_TECHNOLOGY_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/powerpoint_small.png"));

	public static ImageIcon iconForObject(Class<? extends TechnologyObject<?>> objectClass) {
		if (PowerpointSlideshow.class.isAssignableFrom(objectClass)) {
			return POWERPOINT_TECHNOLOGY_ICON;
		}
		else if (PowerpointShape.class.isAssignableFrom(objectClass)) {
			return POWERPOINT_TECHNOLOGY_ICON;
		}
		else if (PowerpointSlide.class.isAssignableFrom(objectClass)) {
			return POWERPOINT_TECHNOLOGY_ICON;
		}
		logger.warning("No icon for " + objectClass);
		return null;
	}

}
