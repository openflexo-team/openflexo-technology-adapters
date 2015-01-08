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

package org.openflexo.technologyadapter.oslc.gui;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.oslc.model.core.OSLCCreationFactory;
import org.openflexo.technologyadapter.oslc.model.core.OSLCQueryCapability;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.model.core.OSLCService;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProvider;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProviderCatalog;
import org.openflexo.technologyadapter.oslc.model.rm.OSLCRequirement;
import org.openflexo.technologyadapter.oslc.model.rm.OSLCRequirementCollection;
import org.openflexo.toolbox.ImageIconResource;

public class OSLCIconLibrary {
	private static final Logger logger = Logger.getLogger(OSLCIconLibrary.class.getPackage().getName());

	public static final ImageIcon OSLC_TECHNOLOGY_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/oslc_32x32.png"));
	public static final ImageIcon OSLC_TECHNOLOGY_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/oslc_16x16.png"));
	public static final ImageIcon OSLC_FILE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/oslc_32x32.png"));

	public static ImageIcon iconForObject(Class<? extends TechnologyObject> objectClass) {
		if (OSLCResource.class.isAssignableFrom(objectClass)) {
			return OSLC_TECHNOLOGY_ICON;
		}
		else if (OSLCRequirement.class.isAssignableFrom(objectClass)) {
			return OSLC_TECHNOLOGY_ICON;
		}
		else if (OSLCRequirementCollection.class.isAssignableFrom(objectClass)) {
			return OSLC_TECHNOLOGY_ICON;
		}
		else if (OSLCServiceProvider.class.isAssignableFrom(objectClass)) {
			return OSLC_TECHNOLOGY_ICON;
		}
		else if (OSLCService.class.isAssignableFrom(objectClass)) {
			return OSLC_TECHNOLOGY_ICON;
		}
		else if (OSLCServiceProviderCatalog.class.isAssignableFrom(objectClass)) {
			return OSLC_TECHNOLOGY_ICON;
		}
		else if (OSLCQueryCapability.class.isAssignableFrom(objectClass)) {
			return OSLC_TECHNOLOGY_ICON;
		}
		else if (OSLCCreationFactory.class.isAssignableFrom(objectClass)) {
			return OSLC_TECHNOLOGY_ICON;
		}
		return null;
	}
}
