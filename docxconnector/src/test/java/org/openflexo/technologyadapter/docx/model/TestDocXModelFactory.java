package org.openflexo.technologyadapter.docx.model;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.openflexo.model.exceptions.MissingImplementationException;
import org.openflexo.model.exceptions.ModelDefinitionException;

public class TestDocXModelFactory {

	@Test
	public void test() throws ModelDefinitionException {
		DocXFactory factory = new DocXFactory(null, null);
		try {
			factory.checkMethodImplementations();
		} catch (MissingImplementationException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
