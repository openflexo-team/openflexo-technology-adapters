package org.openflexo.technologyadapter.oslcconnector.test.fib;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestOSLCWidgetFibs extends GenericFIBTestCase {

	public static void main(String[] args) {
		System.out.println(generateFIBTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Fib")).getFile(), "Fib"));
	}

	@Test
	public void testFIBOSLCResourceViewWidget() {
		validateFIB("Fib/OSLCResourceView.fib");
	}

}
