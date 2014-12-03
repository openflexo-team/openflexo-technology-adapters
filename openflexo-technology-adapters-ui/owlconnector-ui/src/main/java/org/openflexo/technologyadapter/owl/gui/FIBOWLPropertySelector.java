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

import org.openflexo.components.widget.FIBPropertySelector;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.OWLProperty;

/**
 * Widget allowing to select an {@link OWLProperty}.<br>
 * 
 * @see FIBPropertySelector
 * 
 * @author sguerin
 * 
 */
public class FIBOWLPropertySelector extends FIBPropertySelector {
	@SuppressWarnings("hiding")
	static final Logger logger = Logger.getLogger(FIBOWLPropertySelector.class.getPackage().getName());

	private final boolean showOWLAndRDFConcepts = false;

	public FIBOWLPropertySelector(OWLProperty editedObject) {
		super(editedObject);
	}

	@Override
	public OWLOntology getContext() {
		return (OWLOntology) super.getContext();
	}

	public void setContext(OWLOntology context) {
		super.setContext(context);
	}

	public boolean getShowOWLAndRDFConcepts() {
		return showTechnologySpecificConcepts();
	}

	@CustomComponentParameter(name = "showOWLAndRDFConcepts", type = CustomComponentParameter.Type.OPTIONAL)
	public void setShowOWLAndRDFConcepts(boolean showOWLAndRDFConcepts) {
		setShowTechnologySpecificConcepts(showOWLAndRDFConcepts);
	}

	public boolean showTechnologySpecificConcepts() {
		return getModel().getShowOWLAndRDFConcepts();
	}

	public void setShowTechnologySpecificConcepts(boolean flag) {
		boolean oldValue = showTechnologySpecificConcepts();
		if (oldValue != flag) {
			getModel().setShowOWLAndRDFConcepts(flag);
			update();
			getPropertyChangeSupport().firePropertyChange("showTechnologySpecificConcepts", oldValue, flag);
			getPropertyChangeSupport().firePropertyChange("showOWLAndRDFConcepts", oldValue, flag);
		}
	}

	@Override
	public OWLOntologyBrowserModel getModel() {
		return (OWLOntologyBrowserModel) super.getModel();
	}

	@Override
	protected OWLOntologyBrowserModel makeBrowserModel(IFlexoOntology ontology) {
		OWLOntologyBrowserModel returned = (OWLOntologyBrowserModel) super.makeBrowserModel(ontology);
		returned.setShowOWLAndRDFConcepts(true);
		return returned;
	}

	@Override
	protected OWLOntologyBrowserModel performBuildOntologyBrowserModel(IFlexoOntology ontology) {
		return new OWLOntologyBrowserModel((OWLOntology) ontology);
	}

}
