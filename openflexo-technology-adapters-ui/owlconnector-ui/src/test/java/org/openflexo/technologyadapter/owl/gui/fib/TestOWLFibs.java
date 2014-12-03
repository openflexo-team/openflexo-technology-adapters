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
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestOWLFibs extends GenericFIBTestCase {

	/*
	 * Use this method to print all
	 * Then copy-paste 
	 */
	public static void main(String[] args) {
		System.out.println(generateFIBTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Fib")).getFile(), "Fib/"));
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
	public void testAddOWLIndividualPanel() {
		validateFIB("Fib/AddOWLIndividualPanel.fib");
	}

	@Test
	public void testAddRestrictionStatementPanel() {
		validateFIB("Fib/AddRestrictionStatementPanel.fib");
	}

	@Test
	public void testFIBOWLOntologyBrowser() {
		validateFIB("Fib/FIBOWLOntologyBrowser.fib");
	}

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
	*/

}
