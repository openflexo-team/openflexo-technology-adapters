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

package org.openflexo.technologyadapter.owl.gui;

import java.util.logging.Logger;

import org.openflexo.components.widget.FIBFlexoObjectSelector;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.rm.Resource;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.OWLOntologyLibrary;

/**
 * Widget allowing to select an OWL Ontology while browsing in OWL Ontology library
 * 
 * @author sguerin
 * 
 */
@SuppressWarnings("serial")
public class FIBOWLOntologySelector extends FIBFlexoObjectSelector<OWLOntology> {

	static final Logger logger = Logger.getLogger(FIBOWLOntologySelector.class.getPackage().getName());

	public static Resource FIB_FILE = ResourceLocator.locateResource("Fib/FIBOWLOntologySelector.fib");

	public FIBOWLOntologySelector(OWLOntology editedObject) {
		super(editedObject);
	}

	/*@Override
	public void delete() {
		super.delete();
		ontologyLibrary = null;
	}*/

	@Override
	public Resource getFIBResource() {
		return FIB_FILE;
	}

	@Override
	public Class<OWLOntology> getRepresentedType() {
		return OWLOntology.class;
	}

	@Override
	public String renderedString(OWLOntology editedObject) {
		if (editedObject != null) {
			return editedObject.getName();
		}
		return "";
	}

	private OWLOntologyLibrary ontologyLibrary;

	public OWLOntologyLibrary getOntologyLibrary() {
		return ontologyLibrary;
	}

	@CustomComponentParameter(name = "ontologyLibrary", type = CustomComponentParameter.Type.MANDATORY)
	public void setOntologyLibrary(OWLOntologyLibrary ontologyLibrary) {
		this.ontologyLibrary = ontologyLibrary;
	}

	/**
	 * This method must be implemented if we want to implement completion<br>
	 * Completion will be performed on that selectable values<br>
	 * Return all viewpoints of this library
	 */
	/*@Override
	protected Enumeration<IFlexoOntology> getAllSelectableValues() {
		if (getOntologyLibrary() != null) {
			Vector<IFlexoOntology> allOntologies = new Vector<IFlexoOntology>(getOntologyLibrary().getAllOntologies());
			return allOntologies.elements();
		}
		return null;
	}*/

	// Please uncomment this for a live test
	// Never commit this uncommented since it will not compile on continuous build
	// To have icon, you need to choose "Test interface" in the editor (otherwise, flexo controller is not insanciated in EDIT mode)
	/*public static void main(String[] args) {
		FIBAbstractEditor editor = new FIBAbstractEditor() {
			@Override
			public Object[] getData() {
				FlexoResourceCenter resourceCenter = FlexoResourceCenterService.instance().getFlexoResourceCenter();
				FIBOntologySelector selector = new FIBOntologySelector(null);
				selector.setOntologyLibrary(resourceCenter.retrieveBaseOntologyLibrary());
				return makeArray(selector);
			}

			@Override
			public File getFIBFile() {
				return FIB_FILE;
			}

			@Override
			public FIBController makeNewController(FIBComponent component) {
				return new FlexoFIBController<FIBViewPointSelector>(component);
			}
		};
		editor.launch();
	}*/

}
