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



import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLObject;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * This class represent the module view for an XSD meta model<br>
 * Underlying representation is supported by OntologyView implementation.
 * 
 * @author sylvain
 * 
 */
@SuppressWarnings("serial")
public class XMLMetaModelView extends AbstractXMLModuleView<XMLMetaModel> {

	
	public static final Resource FIB_FILE = ResourceLocator.locateResource("Fib/FIBXMLMetaModelView.fib");
	public static final Resource XMLType_FIB_FILE = ResourceLocator.locateResource("Fib/FIBPanelXMLType.fib");
	public static final Resource XMLProperty_FIB_FILE = ResourceLocator.locateResource("Fib/FIBPanelXMLProperty.fib");

	
	
	public XMLMetaModelView(XMLMetaModel object, FlexoController controller, FlexoPerspective perspective) {
		super(controller, object, perspective, FIB_FILE);
	}

	public XMLMetaModel getMetamodel() {
		return representedObject;
	}

	/**
	 * Selects the FIB Panel to display depending of selected Object type
	 * @param object
	 * @return
	 */
	public Resource getFibForXMLObject(XMLObject object){
		if (object instanceof XMLType) {
			return XMLType_FIB_FILE;
		}
		else if (object instanceof XMLProperty) {
			return XMLProperty_FIB_FILE;
		}
		else return null;
	}
	

	

	
}
