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
import org.openflexo.fib.utils.GenericFIBInspectorTestCase;

public class TestXMLInspectors extends GenericFIBInspectorTestCase {

	@Test
	public void testXMLFileResourceInspector() {
		validateFIB("Inspectors/XML/XMLFileResource.inspector");
	}

	@Test
	public void testXMLMetaModelInspector() {
		validateFIB("Inspectors/XML/XMLMetaModel.inspector");
	}

	@Test
	public void testXMLModelInspector() {
		validateFIB("Inspectors/XML/XMLModel.inspector");
	}

	@Test
	public void testXSDMetaModelResourceInspector() {
		validateFIB("Inspectors/XML/XSDMetaModelResource.inspector");
	}

	@Test
	public void testXMLModelSlotInspector() {
		validateFIB("Inspectors/XML/XMLModelSlot.inspector");
	}

	@Test
	public void testFreeXMLModelSlotInspector() {
		validateFIB("Inspectors/XML/FreeXMLModelSlot.inspector");
	}

}
