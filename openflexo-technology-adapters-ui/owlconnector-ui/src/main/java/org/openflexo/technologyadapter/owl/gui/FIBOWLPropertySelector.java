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
