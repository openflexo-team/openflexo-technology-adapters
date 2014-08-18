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
package org.openflexo.technologyadapter.xml.gui;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XSOntClass;
import org.openflexo.technologyadapter.xml.metamodel.XMLDataProperty;
import org.openflexo.technologyadapter.xml.metamodel.XSOntObjectProperty;
import org.openflexo.technologyadapter.xml.metamodel.XSOntProperty;
import org.openflexo.technologyadapter.xml.model.AbstractXSOntObject;
import org.openflexo.technologyadapter.xml.model.XMLXSDModel;
import org.openflexo.technologyadapter.xml.model.XSOntIndividual;
import org.openflexo.toolbox.ImageIconResource;

public class XSDIconLibrary {

	private static final Logger logger = Logger.getLogger(XSDIconLibrary.class.getPackage().getName());

	

	public static final ImageIconResource XSD_TECHNOLOGY_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/XSDTechnology32.png"));
	public static final ImageIconResource XSD_TECHNOLOGY_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/XSDTechnology.png"));
	public static final ImageIconResource XSD_FILE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/XSDFile.png"));
	public static final ImageIconResource XML_FILE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/XMLFile.png"));
	public static final ImageIconResource XSD_CLASS_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/XSDClass.png"));
	public static final ImageIconResource XSD_INDIVIDUAL_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/XSDIndividual.png"));
	public static final ImageIconResource XSD_PROPERTY_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/XSDProperty.png"));
	public static final ImageIconResource XSD_DATAPROPERTY_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/XSDDataProperty.png"));
	public static final ImageIconResource XSD_OBJECTPROPERTY_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/XSDObjectProperty.png"));

	public static ImageIcon iconForObject(Class<? extends AbstractXSOntObject> objectClass) {
		if (XMLMetaModel.class.isAssignableFrom(objectClass)) {
			return XSD_FILE_ICON;
		} else if (XMLXSDModel.class.isAssignableFrom(objectClass)) {
			return XML_FILE_ICON;
		} else if (XSOntClass.class.isAssignableFrom(objectClass)) {
			return XSD_CLASS_ICON;
		} else if (XSOntIndividual.class.isAssignableFrom(objectClass)) {
			return XSD_INDIVIDUAL_ICON;
		} else if (XMLDataProperty.class.isAssignableFrom(objectClass)) {
			return XSD_DATAPROPERTY_ICON;
		}else if (XSOntObjectProperty.class.isAssignableFrom(objectClass)) {
			return XSD_OBJECTPROPERTY_ICON;
		}
		logger.warning("No icon for " + objectClass);
		return null;
	}
}
