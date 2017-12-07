/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexo-foundation, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel.fml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.connie.exception.InvalidBindingException;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.DeletionScheme;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModelLibrary;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.ActionSchemeAction;
import org.openflexo.foundation.fml.rt.action.CreateBasicVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.DeletionSchemeAction;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.excel.AbstractTestExcel;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.ExcelCell;
import org.openflexo.technologyadapter.excel.model.ExcelRow;
import org.openflexo.technologyadapter.excel.model.ExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.technologyadapter.excel.semantics.model.SEFlexoConceptInstance;
import org.openflexo.technologyadapter.excel.semantics.model.SEVirtualModelInstance;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * This unit test is intented to test View creation facilities with a ViewPoint created on the fly
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestSEUsingExistingVirtualModel extends AbstractTestExcel {

	private static VirtualModel viewPoint;
	private static FlexoEditor editor;
	private static FlexoProject<File> project;
	private static FMLRTVirtualModelInstance rootVMI;
	private static SEVirtualModelInstance seVMI;

	private static ExcelWorkbookResource personListingResource;
	private static ExcelWorkbook personListingWB;

	private static SEFlexoConceptInstance jeanDupont;
	private static SEFlexoConceptInstance bernadetteDupont;
	private static SEFlexoConceptInstance julesDupont;
	private static SEFlexoConceptInstance ninaDupont;
	private static SEFlexoConceptInstance gerardMenvusat;
	private static SEFlexoConceptInstance alainTerrieur;
	private static SEFlexoConceptInstance newPerson;

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(ExcelTechnologyAdapter.class);

		for (FlexoResourceCenter<?> rc : serviceManager.getResourceCenterService().getResourceCenters()) {
			System.out.println("***** Found RC " + rc.getDefaultBaseURI());
			for (FlexoResource<?> r : rc.getAllResources()) {
				System.out.println("   > " + r.getURI());
			}
		}
	}

	@Test
	@TestOrder(2)
	public void loadWorkbook() throws Exception {
		log("loadWorkbook");
		personListingResource = getExcelResource("PersonListing.xlsx");
		System.out.println("Found resource " + personListingResource.getURI());
		personListingWB = personListingResource.getExcelWorkbook();
	}

	/**
	 * Retrieve the ViewPoint
	 * 
	 * @throws FlexoException
	 * @throws ResourceLoadingCancelledException
	 * @throws FileNotFoundException
	 */
	@Test
	@TestOrder(3)
	public void testLoadViewPoint() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		VirtualModelLibrary vpLib = serviceManager.getVirtualModelLibrary();
		assertNotNull(vpLib);
		viewPoint = vpLib.getVirtualModel("http://www.openflexo.org/test/excel/RootVM.fml");

		System.out.println("hop:" + vpLib.getVirtualModels());

		assertNotNull(viewPoint);

		assertVirtualModelIsValid(viewPoint);
	}

	@Test
	@TestOrder(4)
	public void testCreateProject() {
		editor = createStandaloneProject("TestProject");
		project = (FlexoProject<File>) editor.getProject();
		System.out.println("Created project " + project.getProjectDirectory());
		assertTrue(project.getProjectDirectory().exists());
	}

	/**
	 * Instantiate in project a View conform to the ViewPoint
	 * 
	 * @throws InvalidBindingException
	 * @throws InvocationTargetException
	 * @throws NullReferenceException
	 * @throws TypeMismatchException
	 */
	@Test
	@TestOrder(5)
	public void testInstantiate() throws TypeMismatchException, NullReferenceException, InvocationTargetException, InvalidBindingException {
		CreateBasicVirtualModelInstance action = CreateBasicVirtualModelInstance.actionType
				.makeNewAction(project.getVirtualModelInstanceRepository().getRootFolder(), null, editor);
		action.setNewVirtualModelInstanceName("MyView");
		action.setNewVirtualModelInstanceTitle("Test creation of a new view");
		action.setVirtualModel(viewPoint);
		CreationScheme creationScheme = viewPoint.getCreationSchemes().get(0);
		action.setCreationScheme(creationScheme);
		action.setParameterValue(creationScheme.getParameter("excelResource"), personListingResource);
		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
		rootVMI = action.getNewVirtualModelInstance();
		assertNotNull(rootVMI);
		assertNotNull(rootVMI.getResource());

		seVMI = rootVMI.execute("personListing");
		assertNotNull(seVMI);

		assertEquals(6, seVMI.getFlexoConceptInstances().size());
		jeanDupont = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(0);
		bernadetteDupont = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(1);
		julesDupont = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(2);
		ninaDupont = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(3);
		gerardMenvusat = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(4);
		alainTerrieur = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(5);

		assertEquals("MR", jeanDupont.execute("sexe"));
		assertEquals("Jean Dupont", jeanDupont.execute("name"));
		assertEquals("Architect", jeanDupont.execute("activity"));
		assertEquals(45, (long) jeanDupont.execute("age"));
		assertEquals("BREST", jeanDupont.execute("city"));

		assertEquals("MS", bernadetteDupont.execute("sexe"));
		assertEquals("Bernardette Dupont", bernadetteDupont.execute("name"));
		assertEquals("Professor", bernadetteDupont.execute("activity"));
		assertEquals(45, (long) bernadetteDupont.execute("age"));
		assertEquals("BREST", bernadetteDupont.execute("city"));

		assertEquals("MR", julesDupont.execute("sexe"));
		assertEquals("Jules Dupont", julesDupont.execute("name"));
		assertEquals("Studient", julesDupont.execute("activity"));
		assertEquals(18, (long) julesDupont.execute("age"));
		assertEquals("BREST", julesDupont.execute("city"));

		assertEquals("MS", ninaDupont.execute("sexe"));
		assertEquals("Nina Dupont", ninaDupont.execute("name"));
		assertEquals("Studient", ninaDupont.execute("activity"));
		assertEquals(16, (long) ninaDupont.execute("age"));
		assertEquals("BREST", ninaDupont.execute("city"));

		assertEquals("MR", gerardMenvusat.execute("sexe"));
		assertEquals("Gérard Menvusat", gerardMenvusat.execute("name"));
		assertEquals("Cooker", gerardMenvusat.execute("activity"));
		assertEquals(41, (long) gerardMenvusat.execute("age"));
		assertEquals("MULHOUSE", gerardMenvusat.execute("city"));

		assertEquals("MR", alainTerrieur.execute("sexe"));
		assertEquals("Alain Terrieur", alainTerrieur.execute("name"));
		assertEquals("Restorer", alainTerrieur.execute("activity"));
		assertEquals(55, (long) alainTerrieur.execute("age"));
		assertEquals("MULHOUSE", alainTerrieur.execute("city"));

		List<SEFlexoConceptInstance> allPersons = seVMI.execute("persons");
		assertEquals(6, allPersons.size());
		assertSameList(allPersons, jeanDupont, bernadetteDupont, julesDupont, ninaDupont, gerardMenvusat, alainTerrieur);

	}

	@Test
	@TestOrder(7)
	public void testInsertNewPerson()
			throws TypeMismatchException, NullReferenceException, InvocationTargetException, InvalidBindingException {

		ExcelSheet sheet = personListingWB.getExcelSheetAtPosition(0);

		assertEquals(8, sheet.getExcelRows().size());

		for (int i = 0; i < sheet.getExcelRows().size(); i++) {
			ExcelRow row = sheet.getRowAt(i);
			assertSame(row, sheet.getExcelRows().get(i));
			assertEquals(5, row.getExcelCells().size());
			assertEquals(i, row.getRowIndex());
			StringBuffer sb = new StringBuffer();
			sb.append("[ROW-" + row.getRowIndex() + "/" + row.getRow().getRowNum() + "]");
			for (int j = 0; j < row.getExcelCells().size(); j++) {
				ExcelCell cell = row.getExcelCellAt(j);
				assertSame(cell, row.getExcelCells().get(j));
				assertSame(cell, sheet.getCellAt(i, j));
				sb.append(" " + cell.getCellValue());
			}
			System.out.println(sb.toString());
		}

		System.out.println("Now create a new Person");
		ActionScheme actionScheme = seVMI.getVirtualModel().getActionSchemes().get(0);
		ActionSchemeAction addPerson = new ActionSchemeAction(actionScheme, seVMI, null, editor);
		addPerson.setParameterValue(actionScheme.getParameter("index"), 1);
		addPerson.doAction();

		assertTrue(addPerson.hasActionExecutionSucceeded());

		assertEquals(9, sheet.getExcelRows().size());
		for (int i = 0; i < sheet.getExcelRows().size(); i++) {
			ExcelRow row = sheet.getRowAt(i);
			assertSame(row, sheet.getExcelRows().get(i));
			assertEquals(5, row.getExcelCells().size());
			assertEquals(i, row.getRowIndex());
			StringBuffer sb = new StringBuffer();
			sb.append("[ROW-" + row.getRowIndex() + "/" + row.getRow().getRowNum() + "]");
			for (int j = 0; j < row.getExcelCells().size(); j++) {
				ExcelCell cell = row.getExcelCellAt(j);
				assertSame(cell, row.getExcelCells().get(j));
				assertSame(cell, sheet.getCellAt(i, j));
				sb.append(" " + cell.getCellValue());
			}
			System.out.println(sb.toString());
		}

		assertEquals(7, seVMI.getFlexoConceptInstances().size());
		newPerson = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(seVMI.getFlexoConceptInstances().size() - 1);

		assertEquals("", newPerson.execute("sexe"));
		assertEquals("<enter name>", newPerson.execute("name"));
		assertEquals("", newPerson.execute("activity"));
		assertEquals(0, (long) newPerson.execute("age"));
		assertEquals("", newPerson.execute("city"));

		newPerson.setFlexoPropertyValue("sexe", "MR");
		newPerson.setFlexoPropertyValue("name", "Jean-Paul Durant");
		newPerson.setFlexoPropertyValue("activity", "Farmer");
		newPerson.setFlexoPropertyValue("age", 47);
		newPerson.setFlexoPropertyValue("city", "Plouarzel");

		assertEquals("MR", newPerson.execute("sexe"));
		assertEquals("Jean-Paul Durant", newPerson.execute("name"));
		assertEquals("Farmer", newPerson.execute("activity"));
		assertEquals(47, (long) newPerson.execute("age"));
		assertEquals("Plouarzel", newPerson.execute("city"));

		assertEquals("MR", jeanDupont.execute("sexe"));
		assertEquals("Jean Dupont", jeanDupont.execute("name"));
		assertEquals("Architect", jeanDupont.execute("activity"));
		assertEquals(45, (long) jeanDupont.execute("age"));
		assertEquals("BREST", jeanDupont.execute("city"));

		assertEquals("MS", bernadetteDupont.execute("sexe"));
		assertEquals("Bernardette Dupont", bernadetteDupont.execute("name"));
		assertEquals("Professor", bernadetteDupont.execute("activity"));
		assertEquals(45, (long) bernadetteDupont.execute("age"));
		assertEquals("BREST", bernadetteDupont.execute("city"));

		assertEquals("MR", julesDupont.execute("sexe"));
		assertEquals("Jules Dupont", julesDupont.execute("name"));
		assertEquals("Studient", julesDupont.execute("activity"));
		assertEquals(18, (long) julesDupont.execute("age"));
		assertEquals("BREST", julesDupont.execute("city"));

		assertEquals("MS", ninaDupont.execute("sexe"));
		assertEquals("Nina Dupont", ninaDupont.execute("name"));
		assertEquals("Studient", ninaDupont.execute("activity"));
		assertEquals(16, (long) ninaDupont.execute("age"));
		assertEquals("BREST", ninaDupont.execute("city"));

		assertEquals("MR", gerardMenvusat.execute("sexe"));
		assertEquals("Gérard Menvusat", gerardMenvusat.execute("name"));
		assertEquals("Cooker", gerardMenvusat.execute("activity"));
		assertEquals(41, (long) gerardMenvusat.execute("age"));
		assertEquals("MULHOUSE", gerardMenvusat.execute("city"));

		assertEquals("MR", alainTerrieur.execute("sexe"));
		assertEquals("Alain Terrieur", alainTerrieur.execute("name"));
		assertEquals("Restorer", alainTerrieur.execute("activity"));
		assertEquals(55, (long) alainTerrieur.execute("age"));
		assertEquals("MULHOUSE", alainTerrieur.execute("city"));

		List<SEFlexoConceptInstance> allPersons = seVMI.execute("persons");
		assertEquals(7, allPersons.size());

		assertSame(jeanDupont, allPersons.get(0));
		assertSame(newPerson, allPersons.get(1));
		assertSame(bernadetteDupont, allPersons.get(2));
		assertSame(julesDupont, allPersons.get(3));
		assertSame(ninaDupont, allPersons.get(4));
		assertSame(gerardMenvusat, allPersons.get(5));
		assertSame(alainTerrieur, allPersons.get(6));

	}

	@Test
	@TestOrder(8)
	public void testRemovePerson()
			throws TypeMismatchException, NullReferenceException, InvocationTargetException, InvalidBindingException {

		ExcelSheet sheet = personListingWB.getExcelSheetAtPosition(0);

		assertEquals(9, sheet.getExcelRows().size());

		for (int i = 0; i < sheet.getExcelRows().size(); i++) {
			ExcelRow row = sheet.getRowAt(i);
			assertSame(row, sheet.getExcelRows().get(i));
			assertEquals(5, row.getExcelCells().size());
			assertEquals(i, row.getRowIndex());
			StringBuffer sb = new StringBuffer();
			sb.append("[ROW-" + row.getRowIndex() + "/" + row.getRow().getRowNum() + "]");
			for (int j = 0; j < row.getExcelCells().size(); j++) {
				ExcelCell cell = row.getExcelCellAt(j);
				assertSame(cell, row.getExcelCells().get(j));
				assertSame(cell, sheet.getCellAt(i, j));
				sb.append(" " + cell.getCellValue());
			}
			System.out.println(sb.toString());
		}

		System.out.println("Now remove a Person");
		DeletionScheme deletionScheme = seVMI.getVirtualModel().getDeletionSchemes().get(0);
		DeletionSchemeAction deletePerson = new DeletionSchemeAction(deletionScheme, ninaDupont, null, editor);
		deletePerson.doAction();

		assertTrue(deletePerson.hasActionExecutionSucceeded());

		assertEquals(8, sheet.getExcelRows().size());

		for (int i = 0; i < sheet.getExcelRows().size(); i++) {
			ExcelRow row = sheet.getRowAt(i);
			assertSame(row, sheet.getExcelRows().get(i));
			assertEquals(5, row.getExcelCells().size());
			assertEquals(i, row.getRowIndex());
			StringBuffer sb = new StringBuffer();
			sb.append("[ROW-" + row.getRowIndex() + "/" + row.getRow().getRowNum() + "]");
			for (int j = 0; j < row.getExcelCells().size(); j++) {
				ExcelCell cell = row.getExcelCellAt(j);
				assertSame(cell, row.getExcelCells().get(j));
				assertSame(cell, sheet.getCellAt(i, j));
				sb.append(" " + cell.getCellValue());
			}
			System.out.println(sb.toString());
		}

		assertEquals(6, seVMI.getFlexoConceptInstances().size());

		List<SEFlexoConceptInstance> allPersons = seVMI.execute("persons");
		assertEquals(6, allPersons.size());

		assertSame(jeanDupont, allPersons.get(0));
		assertSame(newPerson, allPersons.get(1));
		assertSame(bernadetteDupont, allPersons.get(2));
		assertSame(julesDupont, allPersons.get(3));
		assertSame(gerardMenvusat, allPersons.get(4));
		assertSame(alainTerrieur, allPersons.get(5));

	}
}
