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
