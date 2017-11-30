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
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModelLibrary;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreateBasicVirtualModelInstance;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.excel.AbstractTestExcel;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
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

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(ExcelTechnologyAdapter.class);
	}

	@Test
	@TestOrder(2)
	public void loadWorkbook() throws Exception {
		log("loadWorkbook");
		personListingResource = getExcelResource("PersonListing.xlsx");
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
		SEFlexoConceptInstance jeanDupont = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(0);
		SEFlexoConceptInstance bernadetteDupont = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(1);
		SEFlexoConceptInstance julesDupont = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(2);
		SEFlexoConceptInstance ninaDupont = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(3);
		SEFlexoConceptInstance gerardMenvusat = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(4);
		SEFlexoConceptInstance alainTerrieur = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(5);

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

		List<SEFlexoConceptInstance> allPersons = seVMI.execute("persons");
		assertEquals(6, allPersons.size());
		assertSameList(allPersons, jeanDupont, bernadetteDupont, julesDupont, ninaDupont, gerardMenvusat, alainTerrieur);

	}
}