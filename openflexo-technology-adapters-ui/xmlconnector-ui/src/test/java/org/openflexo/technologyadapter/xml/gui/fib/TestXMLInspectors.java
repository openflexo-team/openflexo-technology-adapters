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
