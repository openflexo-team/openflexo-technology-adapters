package org.openflexo.technologyadapter.diagram.gui.fib;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestDiagramDialogFibs extends GenericFIBTestCase {

	/*
	 * Use this method to print all
	 * Then copy-paste 
	 */
	public static void main(String[] args) {
		System.out.println(generateFIBTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Fib/Dialog")).getFile(),
				"Fib/Dialog/"));
	}

	@Test
	public void testCreateDiagramDialog() {
		validateFIB("Fib/Dialog/CreateDiagramDialog.fib");
	}

	@Test
	public void testCreateDiagramFromPPTDialog() {
		validateFIB("Fib/Dialog/CreateDiagramFromPPTDialog.fib");
	}

	@Test
	public void testCreateDiagramSpecificationDialog() {
		validateFIB("Fib/Dialog/CreateDiagramSpecificationDialog.fib");
	}

	@Test
	public void testCreateExampleDiagramDialog() {
		validateFIB("Fib/Dialog/CreateExampleDiagramDialog.fib");
	}

	@Test
	public void testCreatePaletteDialog() {
		validateFIB("Fib/Dialog/CreatePaletteDialog.fib");
	}

	@Test
	public void testDeclareConnectorInFlexoConceptDialog() {
		validateFIB("Fib/Dialog/DeclareConnectorInFlexoConceptDialog.fib");
	}

	@Test
	public void testDeclareShapeInFlexoConceptDialog() {
		validateFIB("Fib/Dialog/DeclareShapeInFlexoConceptDialog.fib");
	}

	@Test
	public void testDeleteDiagramElementsDialog() {
		validateFIB("Fib/Dialog/DeleteDiagramElementsDialog.fib");
	}

	@Test
	public void testImportImageFileDialog() {
		validateFIB("Fib/Dialog/ImportImageFileDialog.fib");
	}

	@Test
	public void testPushToPaletteDialog() {
		validateFIB("Fib/Dialog/PushToPaletteDialog.fib");
	}

}
