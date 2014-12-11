package org.openflexo.technologyadapter.powerpoint.fib;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestPPTWizardFibs extends GenericFIBTestCase {

	public static void main(String[] args) {
		System.out.println(generateFIBTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Fib/Wizard")).getFile(),
				"Fib/Wizard/"));
	}

	@Test
	public void testChoosePPTSlide() {
		validateFIB("Fib/Wizard/ChoosePPTSlide.fib");
	}

}
