/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
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

package org.openflexo.technologyadapter.emf.gui;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.emf.metamodel.EMFAttributeDataProperty;
import org.openflexo.technologyadapter.emf.metamodel.EMFAttributeObjectProperty;
import org.openflexo.technologyadapter.emf.metamodel.EMFClassClass;
import org.openflexo.technologyadapter.emf.metamodel.EMFEnumClass;
import org.openflexo.technologyadapter.emf.metamodel.EMFEnumIndividual;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.metamodel.EMFReferenceObjectProperty;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividual;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividualAttributeDataPropertyValue;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividualAttributeObjectPropertyValue;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividualReferenceObjectPropertyValue;
import org.openflexo.toolbox.ImageIconResource;

public class EMFIconLibrary {

	private static final Logger logger = Logger.getLogger(EMFIconLibrary.class.getPackage().getName());

	

	public static final ImageIconResource EMF_TECHNOLOGY_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EMFTechnology32.png"));
	public static final ImageIconResource EMF_TECHNOLOGY_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EMFTechnology.png"));
	public static final ImageIconResource ECORE_FILE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EMFTechnology.png"));
	public static final ImageIconResource EMF_FILE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EMFTechnology.png"));
	public static final ImageIconResource EMF_CLASS_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EClass.gif"));
	public static final ImageIconResource EMF_ENUM_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EEnum.gif"));
	public static final ImageIconResource EMF_ENUM_LITERAL_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EEnumLiteral.gif"));
	public static final ImageIconResource EMF_INDIVIDUAL_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EObject.gif"));
	public static final ImageIconResource EMF_REFERENCE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EReference.gif"));
	public static final ImageIconResource EMF_ATTRIBUTE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EAttribute.gif"));

	public static ImageIcon iconForObject(Class<? extends TechnologyObject> objectClass) {
		if (EMFMetaModel.class.isAssignableFrom(objectClass)) {
			return ECORE_FILE_ICON;
		} else if (EMFModel.class.isAssignableFrom(objectClass)) {
			return EMF_FILE_ICON;
		} else if (EMFClassClass.class.isAssignableFrom(objectClass)) {
			return EMF_CLASS_ICON;
		} else if (EMFEnumClass.class.isAssignableFrom(objectClass)) {
			return EMF_ENUM_ICON;
		} else if (EMFEnumIndividual.class.isAssignableFrom(objectClass)) {
			return EMF_ENUM_LITERAL_ICON;
		} else if (EMFObjectIndividual.class.isAssignableFrom(objectClass)) {
			return EMF_INDIVIDUAL_ICON;
		} else if (EMFReferenceObjectProperty.class.isAssignableFrom(objectClass)) {
			return EMF_REFERENCE_ICON;
		} else if (EMFAttributeDataProperty.class.isAssignableFrom(objectClass)) {
			return EMF_ATTRIBUTE_ICON;
		} else if (EMFAttributeObjectProperty.class.isAssignableFrom(objectClass)) {
			return EMF_ATTRIBUTE_ICON;
		} else if (EMFObjectIndividualAttributeDataPropertyValue.class.isAssignableFrom(objectClass)) {
			return EMF_ATTRIBUTE_ICON;
		} else if (EMFObjectIndividualAttributeObjectPropertyValue.class.isAssignableFrom(objectClass)) {
			return EMF_ATTRIBUTE_ICON;
		} else if (EMFObjectIndividualReferenceObjectPropertyValue.class.isAssignableFrom(objectClass)) {
			return EMF_REFERENCE_ICON;
		} else if (EMFObjectIndividualAttributeDataPropertyValue.class.isAssignableFrom(objectClass)) {
			return EMF_ATTRIBUTE_ICON;
		} else if (EMFObjectIndividualAttributeObjectPropertyValue.class.isAssignableFrom(objectClass)) {
			return EMF_ATTRIBUTE_ICON;
		} else if (EMFObjectIndividualReferenceObjectPropertyValue.class.isAssignableFrom(objectClass)) {
			return EMF_REFERENCE_ICON;
		}
		logger.warning("No icon for " + objectClass);
		return null;
	}

}
