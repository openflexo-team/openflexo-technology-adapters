package org.openflexo.technologyadapter.excel.model;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.openflexo.pamela.exceptions.MissingImplementationException;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.excel.model.ExcelModelFactory;

public class TestExcelModelFactory {

	@Test
	public void test() throws ModelDefinitionException {
		ExcelModelFactory factory = new ExcelModelFactory(null, null);
		try {
			factory.checkMethodImplementations();
		} catch (MissingImplementationException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
