package org.openflexo.technologyadapter.owl.gui.fib;

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

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBInspectorTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestOWLInspectors extends GenericFIBInspectorTestCase {

	/*
	 * Use this method to print all
	 * Then copy-paste 
	 */

	public static void main(String[] args) {
		System.out.println(generateInspectorTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Inspectors/OWL")).getFile(),
				"Inspectors/OWL/"));
	}

	@Test
	public void testDataPropertyStatementRoleInspector() {
		validateFIB("Inspectors/OWL/DataPropertyStatementRole.inspector");
	}

	@Test
	public void testObjectPropertyStatementRoleInspector() {
		validateFIB("Inspectors/OWL/ObjectPropertyStatementRole.inspector");
	}

	@Test
	public void testOWLConceptInspector() {
		validateFIB("Inspectors/OWL/OWLConcept.inspector");
	}

	@Test
	public void testOWLOntologyInspector() {
		validateFIB("Inspectors/OWL/OWLOntology.inspector");
	}

	@Test
	public void testOWLOntologyResourceInspector() {
		validateFIB("Inspectors/OWL/OWLOntologyResource.inspector");
	}

	@Test
	public void testOWLStatementInspector() {
		validateFIB("Inspectors/OWL/OWLStatement.inspector");
	}

}
