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

package org.openflexo.technologyadapter.owl.gui;

import javax.swing.ImageIcon;

import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.ontology.components.widget.OntologyView;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
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
public class OWLOntologyView extends OntologyView<OWLOntology> {

	public OWLOntologyView(OWLOntology ontology, FlexoController controller, FlexoPerspective perspective) {
		super(ontology, controller, perspective,
				ontology != null ? ontology.getTechnologyAdapter().getLocales() : controller.getFlexoLocales());
	}

	@Override
	protected OWLOntologyBrowserModel performBuildOntologyBrowserModel(IFlexoOntology ontology) {
		return new OWLOntologyBrowserModel((OWLOntology) ontology);
	}

	@Override
	public ImageIcon getOntologyClassIcon() {
		return OWLIconLibrary.ONTOLOGY_CLASS_ICON;
	}

	@Override
	public ImageIcon getOntologyIndividualIcon() {
		return OWLIconLibrary.ONTOLOGY_INDIVIDUAL_ICON;
	}

	@Override
	public ImageIcon getOntologyDataPropertyIcon() {
		return OWLIconLibrary.ONTOLOGY_DATA_PROPERTY_ICON;
	}

	@Override
	public ImageIcon getOntologyObjectPropertyIcon() {
		return OWLIconLibrary.ONTOLOGY_OBJECT_PROPERTY_ICON;
	}

	@Override
	public ImageIcon getOntologyAnnotationIcon() {
		return OWLIconLibrary.ONTOLOGY_ANNOTATION_PROPERTY_ICON;
	}

	@Override
	public boolean supportTechnologySpecificHiddenConcepts() {
		return true;
	}

	@Override
	public String technologySpecificHiddenConceptsLabel() {
		return "show_OWL_RDF_concepts";
	}

}
