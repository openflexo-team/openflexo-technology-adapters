/*
 * (c) Copyright 2014 - Openflexo
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

package org.openflexo.technologyadapter.xml.gui.fib;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBTestCase;

public class TestXMLFibs extends GenericFIBTestCase {

	@Test
	public void testMetaModelView() {
		validateFIB("Fib/FIBXMLMetaModelView.fib");
	}

	@Test
	public void testModelView() {
		validateFIB("Fib/FIBXMLModelView.fib");
	}
	@Test
	public void testGetXMLDocumentRoot() {
		validateFIB("Fib/GetXMLDocumentRoot.fib");
	}
	@Test
	public void testAddXMLIndividual() {
		validateFIB("Fib/AddXMLIndividual.fib");
	}
	@Test
	public void testPanelXMLIndividual() {
		validateFIB("Fib/FIBPanelXMLIndividual.fib");
	}
	
	@Test
	public void testPanelXMLProperty() {
		validateFIB("Fib/FIBPanelXMLProperty.fib");
	}

	@Test
	public void testPanelXMLType() {
		validateFIB("Fib/FIBPanelXMLType.fib");
	}

}
