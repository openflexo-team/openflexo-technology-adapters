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

package org.openflexo.technologyadapter.xml.gui.fib;

import org.junit.Test;
import org.openflexo.gina.test.GenericFIBInspectorTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestXMLInspectors extends GenericFIBInspectorTestCase {

	/*
	 * Use this method to print all
	 * Then copy-paste 
	 */

	public static void main(String[] args) {
		System.out.println(generateInspectorTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Inspectors/XML")).getFile(),
				"Inspectors/XML/"));
	}

	@Test
	public void testAddXMLIndividualInspector() {
		validateFIB("Inspectors/XML/EditionAction/AddXMLIndividual.inspector");
	}

	@Test
	public void testGetXMLDocumentRootInspector() {
		validateFIB("Inspectors/XML/EditionAction/GetXMLDocumentRoot.inspector");
	}

	@Test
	public void testSetXMLDocumentRootInspector() {
		validateFIB("Inspectors/XML/EditionAction/SetXMLDocumentRoot.inspector");
	}

	@Test
	public void testFreeXMLModelSlotInspector() {
		validateFIB("Inspectors/XML/ModelSlot/FreeXMLModelSlot.inspector");
	}

	@Test
	public void testXMLModelSlotInspector() {
		validateFIB("Inspectors/XML/ModelSlot/XMLModelSlot.inspector");
	}

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

}
