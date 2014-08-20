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

import javax.swing.ImageIcon;

import org.openflexo.components.widget.OntologyView;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * This class represent the module view for an OWL ontology.<br>
 * Underlying representation is supported by OntologyView implementation.
 * 
 * @author sylvain
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractXMLOntologyView<T extends FlexoObject & IFlexoOntology> extends OntologyView<T> {

	public AbstractXMLOntologyView(T object, FlexoController controller, FlexoPerspective perspective) {
		super(object, controller, perspective);
	}

	@Override
	public ImageIcon getOntologyClassIcon() {
		return XMLIconLibrary.XML_TYPE_ICON;
	}

	@Override
	public ImageIcon getOntologyIndividualIcon() {
		return XMLIconLibrary.XML_INDIVIDUAL_ICON;
	}

	@Override
	public ImageIcon getOntologyDataPropertyIcon() {
		return XMLIconLibrary.XSD_DATAPROPERTY_ICON;
	}

	@Override
	public ImageIcon getOntologyObjectPropertyIcon() {
		return XMLIconLibrary.XSD_OBJECTPROPERTY_ICON;
	}

	@Override
	public ImageIcon getOntologyAnnotationIcon() {
		return XMLIconLibrary.XSD_DATAPROPERTY_ICON;
	}

	@Override
	public boolean supportTechnologySpecificHiddenConcepts() {
		return false;
	}

	@Override
	public String technologySpecificHiddenConceptsLabel() {
		// Not applicable
		return null;
	}

}
