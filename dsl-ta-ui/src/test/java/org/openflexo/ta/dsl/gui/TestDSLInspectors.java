/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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

package org.openflexo.ta.dsl.gui;

import org.junit.Test;
import org.openflexo.gina.test.GenericFIBInspectorTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

/**
 * Used to test all inspectors defined in this technology adapter<br>
 * 
 * To use that class, first execute main method to generate all tests in the console, then copy-paste all the tests in this source file
 * 
 * 
 * @author sylvain
 *
 */
public class TestDSLInspectors extends GenericFIBInspectorTestCase {

	/*
	 * Use this method to print all
	 * Then copy-paste 
	 */

	public static void main(String[] args) {
		System.out.println(generateInspectorTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Inspectors/DSL")).getFile(),
				"Inspectors/DSL/"));
	}

	@Test
	public void testDSLObjectInspector() {
		validateFIB("Inspectors/DSL/DSLObject.inspector");
	}

	@Test
	public void testDSLSystemInspector() {
		validateFIB("Inspectors/DSL/DSLSystem.inspector");
	}

	@Test
	public void testAbstractSelectDSLComponentInspector() {
		validateFIB("Inspectors/DSL/EditionAction/AbstractSelectDSLComponent.inspector");
	}

	@Test
	public void testAbstractSelectDSLLinkInspector() {
		validateFIB("Inspectors/DSL/EditionAction/AbstractSelectDSLLink.inspector");
	}

	@Test
	public void testAddDSLComponentInspector() {
		validateFIB("Inspectors/DSL/EditionAction/AddDSLComponent.inspector");
	}

	@Test
	public void testAddDSLLinkInspector() {
		validateFIB("Inspectors/DSL/EditionAction/AddDSLLink.inspector");
	}

}
