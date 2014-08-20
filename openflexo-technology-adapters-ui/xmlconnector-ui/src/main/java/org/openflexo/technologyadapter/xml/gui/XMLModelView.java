/*
 * (c) Copyright 2014 - Openflexo
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

import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * This class represent the module view for an XML model conform to an XSD.<br>
 * Underlying representation is supported by OntologyView implementation.
 * 
 * @author xtof
 * 
 */

@SuppressWarnings("serial")
public class XMLModelView extends AbstractXMLModuleView<XMLModel> {
	
	public static final Resource FIB_FILE = ResourceLocator.locateResource("Fib/FIBXMLModelView.fib");

	public XMLModelView(XMLModel object, FlexoController controller, FlexoPerspective perspective) {
		super(controller, object, perspective, FIB_FILE);
	}


}
