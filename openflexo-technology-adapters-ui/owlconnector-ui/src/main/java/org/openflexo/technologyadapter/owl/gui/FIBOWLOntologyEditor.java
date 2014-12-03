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

import org.openflexo.components.widget.FIBOntologyEditor;
import org.openflexo.fib.model.FIBCustom.FIBCustomComponent.CustomComponentParameter;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.view.controller.FlexoController;

/**
 * Widget allowing to edit/view an {@link OWLOntology}.<br>
 * 
 * @author sguerin
 * 
 */
public class FIBOWLOntologyEditor extends FIBOntologyEditor {
	static final Logger logger = Logger.getLogger(FIBOWLOntologyEditor.class.getPackage().getName());

	public static final Resource FIB_FILE = ResourceLocator.locateResource("Fib/FIBOWLOntologyEditor.fib");

	public FIBOWLOntologyEditor(OWLOntology ontology, FlexoController controller) {
		super(ontology, controller, FIB_FILE);
	}

	@Override
	public OWLOntology getOntology() {
		return (OWLOntology) super.getOntology();
	}

	@Override
	public OWLOntologyBrowserModel getModel() {
		return (OWLOntologyBrowserModel) super.getModel();
	}

	@Override
	protected OWLOntologyBrowserModel performBuildOntologyBrowserModel(IFlexoOntology ontology) {
		return new OWLOntologyBrowserModel((OWLOntology) ontology);
	}

	public boolean getShowOWLAndRDFConcepts() {
		return showTechnologySpecificConcepts();
	}

	@CustomComponentParameter(name = "showOWLAndRDFConcepts", type = CustomComponentParameter.Type.OPTIONAL)
	public void setShowOWLAndRDFConcepts(boolean showOWLAndRDFConcepts) {
		setShowTechnologySpecificConcepts(showOWLAndRDFConcepts);
	}

	@Override
	public boolean showTechnologySpecificConcepts() {
		return getModel().getShowOWLAndRDFConcepts();
	}

	@Override
	public void setShowTechnologySpecificConcepts(boolean flag) {
		boolean oldValue = showTechnologySpecificConcepts();
		if (oldValue != flag) {
			getModel().setShowOWLAndRDFConcepts(flag);
			update();
			getPropertyChangeSupport().firePropertyChange("showTechnologySpecificConcepts", oldValue, flag);
			getPropertyChangeSupport().firePropertyChange("showOWLAndRDFConcepts", oldValue, flag);
		}
	}

	/*@Override
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
			model.recomputeStructure();
		}
		return (OWLOntologyBrowserModel) model;
	}*/

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
