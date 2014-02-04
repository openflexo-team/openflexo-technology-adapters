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
package org.openflexo.technologyadapter.xsd.gui;

import org.openflexo.technologyadapter.xsd.model.XMLXSDModel;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * This class represent the module view for an XML model conform to an XSD.<br>
 * Underlying representation is supported by OntologyView implementation.
 * 
 * @author sylvain
 * 
 */
@SuppressWarnings("serial")
public class XMLXSDModelView extends AbstractXMLOntologyView<XMLXSDModel> {

	public XMLXSDModelView(XMLXSDModel object, FlexoController controller, FlexoPerspective perspective) {
		super(object, controller, perspective);
	}

	@Override
	protected XMLModelBrowserModel makeBrowserModel() {

		return new XMLModelBrowserModel(getXMLModel());
	}

	public XMLXSDModel getXMLModel() {
		return getOntology();
	}

}
