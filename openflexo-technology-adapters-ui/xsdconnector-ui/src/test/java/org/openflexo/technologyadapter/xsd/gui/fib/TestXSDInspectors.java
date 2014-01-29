package org.openflexo.technologyadapter.xsd.gui.fib;

import org.openflexo.fib.utils.GenericFIBInspectorTestCase;

public class TestXSDInspectors extends GenericFIBInspectorTestCase {

	public void testXMLXSDFileResourceInspector() {
		validateFIB("Inspectors/XSD/XMLXSDFileResource.inspector");
	}

	public void testXMLXSDModelInspector() {
		validateFIB("Inspectors/XSD/XMLXSDModel.inspector");
	}

	public void testXSDMetaModelInspector() {
		validateFIB("Inspectors/XSD/XSDMetaModel.inspector");
	}

	public void testXSDMetaModelResourceInspector() {
		validateFIB("Inspectors/XSD/XSDMetaModelResource.inspector");
	}

	public void testXSDModelSlotInspector() {
		validateFIB("Inspectors/XSD/XSDModelSlot.inspector");
	}

}
