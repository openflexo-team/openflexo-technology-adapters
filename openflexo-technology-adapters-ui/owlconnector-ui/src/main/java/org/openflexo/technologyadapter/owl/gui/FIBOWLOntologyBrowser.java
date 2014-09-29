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
package org.openflexo.technologyadapter.owl.gui;

import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import org.openflexo.components.widget.FIBOntologyBrowser;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.owl.model.OWLOntology;

/**
 * Widget allowing to browse an {@link OWLOntology}.<br>
 * 
 * @see FIBOntologyBrowser
 * 
 * @author sguerin
 * 
 */
public class FIBOWLOntologyBrowser extends FIBOntologyBrowser {
	@SuppressWarnings("hiding")
	static final Logger logger = Logger.getLogger(FIBOWLOntologyBrowser.class.getPackage().getName());

	public static final Resource FIB_FILE = ResourceLocator.locateResource("Fib/FIBOWLOntologyBrowser.fib");

	public FIBOWLOntologyBrowser(OWLOntology ontology) {
		super(FIB_FILE, ontology);
	}

	@Override
	public OWLOntology getOntology() {
		return (OWLOntology) super.getOntology();
	}

	public boolean getShowOWLAndRDFConcepts() {
		return showTechnologySpecificConcepts();
	}

	@CustomComponentParameter(name = "showOWLAndRDFConcepts", type = CustomComponentParameter.Type.OPTIONAL)
	public void setShowOWLAndRDFConcepts(boolean showOWLAndRDFConcepts) {
		setShowTechnologySpecificConcepts(showOWLAndRDFConcepts);
		update();
	}

	@Override
	public OWLOntologyBrowserModel getModel() {
		if (model == null) {
			model = new OWLOntologyBrowserModel(getOntology()) {
				@Override
				public void recomputeStructure() {
					super.recomputeStructure();
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							getPropertyChangeSupport().firePropertyChange("model", null, getModel());
						}
					});
				}
			};
			model.setStrictMode(getStrictMode());
			model.setHierarchicalMode(getHierarchicalMode());
			model.setDisplayPropertiesInClasses(getDisplayPropertiesInClasses());
			model.setRootClass(getRootClass());
			model.setShowClasses(getShowClasses());
			model.setShowIndividuals(getShowIndividuals());
			model.setShowObjectProperties(getShowObjectProperties());
			model.setShowDataProperties(getShowDataProperties());
			model.setShowAnnotationProperties(getShowAnnotationProperties());
			((OWLOntologyBrowserModel) model).setShowOWLAndRDFConcepts(getShowOWLAndRDFConcepts());
			model.setDomain(getDomain());
			model.setRange(getRange());
			model.setDataType(getDataType());
			model.recomputeStructure();
		}
		return (OWLOntologyBrowserModel) model;
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
