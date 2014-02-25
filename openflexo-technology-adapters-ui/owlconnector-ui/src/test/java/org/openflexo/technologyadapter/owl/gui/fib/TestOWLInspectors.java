package org.openflexo.technologyadapter.owl.gui.fib;

import org.openflexo.fib.utils.GenericFIBInspectorTestCase;

public class TestOWLInspectors extends GenericFIBInspectorTestCase {

	public void testDataPropertyStatementPatternRoleInspector() {
		validateFIB("Inspectors/OWL/DataPropertyStatementRole.inspector");
	}

	public void testObjectPropertyStatementPatternRoleInspector() {
		validateFIB("Inspectors/OWL/ObjectPropertyStatementRole.inspector");
	}

	public void testOWLConceptInspector() {
		validateFIB("Inspectors/OWL/OWLConcept.inspector");
	}

	public void testOWLOntologyInspector() {
		validateFIB("Inspectors/OWL/OWLOntology.inspector");
	}

	public void testOWLOntologyResourceInspector() {
		validateFIB("Inspectors/OWL/OWLOntologyResource.inspector");
	}

	public void testOWLStatementInspector() {
		validateFIB("Inspectors/OWL/OWLStatement.inspector");
	}

}
