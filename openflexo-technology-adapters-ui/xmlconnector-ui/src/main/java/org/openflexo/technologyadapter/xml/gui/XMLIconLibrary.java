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

package org.openflexo.technologyadapter.xml.gui;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.icon.ImageIconResource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.xml.metamodel.XMLDataProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLObject;
import org.openflexo.technologyadapter.xml.metamodel.XMLObjectProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLModel;

public class XMLIconLibrary {

	private static final Logger logger = Logger.getLogger(XMLIconLibrary.class.getPackage().getName());

	public static final ImageIconResource XSD_TECHNOLOGY_BIG_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/XMLTechnology32.png"));
	public static final ImageIconResource XSD_TECHNOLOGY_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/XMLTechnology.png"));
	public static final ImageIconResource XSD_FILE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/XSDFile.png"));
	public static final ImageIconResource XML_FILE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/XMLFile.png"));
	public static final ImageIconResource XML_DEFAULT_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/XMLDefault.png"));
	public static final ImageIconResource XML_TYPE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/XMLType.png"));
	public static final ImageIconResource XML_INDIVIDUAL_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/XMLIndividual.png"));
	public static final ImageIconResource XSD_PROPERTY_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/XSDProperty.png"));
	public static final ImageIconResource XSD_DATAPROPERTY_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/XSDDataProperty.png"));
	public static final ImageIconResource XSD_OBJECTPROPERTY_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/XSDObjectProperty.png"));

	public static ImageIcon iconForObject(Class<? extends XMLObject> objectClass) {
		if (XMLMetaModel.class.isAssignableFrom(objectClass)) {
			return XSD_FILE_ICON;
		}
		else if (XMLModel.class.isAssignableFrom(objectClass)) {
			return XML_FILE_ICON;
		}
		else if (XMLType.class.isAssignableFrom(objectClass)) {
			return XML_TYPE_ICON;
		}
		else if (XMLIndividual.class.isAssignableFrom(objectClass)) {
			return XML_INDIVIDUAL_ICON;
		}
		else if (XMLDataProperty.class.isAssignableFrom(objectClass)) {
			return XSD_DATAPROPERTY_ICON;
		}
		else if (XMLObjectProperty.class.isAssignableFrom(objectClass)) {
			return XSD_OBJECTPROPERTY_ICON;
		}
		else if (XMLObject.class.isAssignableFrom(objectClass)) {
			return XML_DEFAULT_ICON;
		}
		logger.warning("No icon for " + objectClass);
		return null;
	}
}
