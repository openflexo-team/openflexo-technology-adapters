/*
 * (c) Copyright 2011-2013 AgileBirds
 * (c) Copyright 2013-2014 Openflexo
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

package org.openflexo.technologyadapter.owl.gui.fib;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBTestCase;
import org.openflexo.technologyadapter.owl.controller.OWLFIBLibrary;

public class TestOWLFibs extends GenericFIBTestCase {

	@Test
	public void testCreateOntologyClassDialog() {
		validateFIB(OWLFIBLibrary.CREATE_ONTOLOGY_CLASS_DIALOG_FIB);
	}

	@Test
	public void testCreateOntologyIndividualDialog() {
		validateFIB(OWLFIBLibrary.CREATE_ONTOLOGY_INDIVIDUAL_FIB);
	}

	@Test
	public void testDeleteOntologyObjectsDialog() {
		validateFIB(OWLFIBLibrary.DELETE_ONTOLOGY_OBJECTS_DIALOG_FIB);
	}

	@Test
	public void testCreateDataPropertyDialog() {
		validateFIB(OWLFIBLibrary.CREATE_DATA_PROPERTY_DIALOG_FIB);
	}

	@Test
	public void testCreateObjectPropertyDialog() {
		validateFIB(OWLFIBLibrary.CREATE_OBJECT_PROPERTY_DIALOG_FIB);
	}

	@Test
	public void testAddDataPropertyStatementPanel() {
		validateFIB("Fib/AddDataPropertyStatementPanel.fib");
	}

	@Test
	public void testAddIsAPropertyPanel() {
		validateFIB("Fib/AddIsAPropertyPanel.fib");
	}

	@Test
	public void testAddObjectPropertyStatementPanel() {
		validateFIB("Fib/AddObjectPropertyStatementPanel.fib");
	}

	@Test
	public void testAddRestrictionPanel() {
		validateFIB("Fib/AddRestrictionStatementPanel.fib");
	}

	// TODO : Are those still useful?

	/*
	@Test
	public void testFIBOntologyLibraryBrowser() {
		validateFIB("Fib/FIBOntologyLibraryBrowser.fib");
	}

	@Test
	public void testFIBOWLClassEditor() {
		validateFIB("Fib/FIBOWLClassEditor.fib");
	}

	@Test
	public void testFIBOWLDataPropertyEditor() {
		validateFIB("Fib/FIBOWLDataPropertyEditor.fib");
	}

	@Test
	public void testFIBOWLIndividualEditor() {
		validateFIB("Fib/FIBOWLIndividualEditor.fib");
	}

	@Test
	public void testFIBOWLObjectPropertyEditor() {
		validateFIB("Fib/FIBOWLObjectPropertyEditor.fib");
	}

	@Test
	public void testFIBOWLOntologyBrowser() {
		validateFIB("Fib/FIBOWLOntologyBrowser.fib");
	}

	@Test
	public void testFIBOWLOntologySelector() {
		validateFIB("Fib/FIBOWLOntologySelector.fib");
	}
	*/

}
