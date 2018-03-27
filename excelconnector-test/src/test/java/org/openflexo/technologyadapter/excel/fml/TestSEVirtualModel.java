/*
 * Copyright (c) 2013-2017, Openflexo
 *
 * This file is part of Flexo-foundation, a component of the software infrastructure
 * developed at Openflexo.
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
 *           Additional permission under GNU GPL version 3 section 7
 *           If you modify this Program, or any covered work, by linking or
 *           combining it with software containing parts covered by the terms
 *           of EPL 1.0, the licensors of this Program grant you additional permission
 *           to convey the resulting work.
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

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.type.PrimitiveType;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.FlexoBehaviourParameter;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateGenericBehaviourParameter;
import org.openflexo.foundation.fml.action.CreateModelSlot;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreateBasicVirtualModelInstance;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceType;
import org.openflexo.technologyadapter.excel.AbstractTestExcel;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.SemanticsExcelModelSlot;
import org.openflexo.technologyadapter.excel.action.CreateSemanticsExcelVirtualModel;
import org.openflexo.technologyadapter.excel.action.CreateSemanticsExcelVirtualModel.SEFlexoConceptSpecification;
import org.openflexo.technologyadapter.excel.model.ExcelCell;
import org.openflexo.technologyadapter.excel.model.ExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.technologyadapter.excel.semantics.fml.CreateSEResource;
import org.openflexo.technologyadapter.excel.semantics.fml.SEColumnRole;
import org.openflexo.technologyadapter.excel.semantics.fml.SEFlexoConcept;
import org.openflexo.technologyadapter.excel.semantics.model.SEFlexoConceptInstance;
import org.openflexo.technologyadapter.excel.semantics.model.SEVirtualModelInstance;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Testing Semantics/Excel
 *
 */
@RunWith(OrderedRunner.class)
public class TestSEVirtualModel extends AbstractTestExcel {

	private final String ROOT_VIRTUAL_MODEL_NAME = "RootVirtualModel";
	private final String ROOT_VIRTUAL_MODEL_URI = "http://openflexo.org/test/" + ROOT_VIRTUAL_MODEL_NAME + ".fml";

	private final String SE_VIRTUAL_MODEL_NAME = "PersonListing";

	private final String PROJECT_NAME = "TestPersonListingProject";

	private final String VIRTUAL_MODEL_INSTANCE_NAME = "TestPersonListing";

	private static VirtualModel rootVirtualModel;
	private static VirtualModel mappingVirtualModel;

	private static SemanticsExcelModelSlot modelSlot;
	private static CreationScheme mappingCreationScheme;
	private static CreationScheme creationScheme;

	private static FMLRTVirtualModelInstance vmi;
	private static SEVirtualModelInstance seVMI;

	private static ExcelWorkbookResource personListingResource;
	private static ExcelWorkbookResource personListing2Resource;
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
		personListing2Resource = getExcelResource("PersonListing2.xlsx");
	}

	@Test
	@TestOrder(3)
	public void createProject() throws Exception {
		log("createProject");
		_editor = createStandaloneProject(PROJECT_NAME);
	}

	@Test
	@TestOrder(4)
	public void createRootVirtualModel() throws Exception {
		log("createRootVirtualModel");
		rootVirtualModel = createTopLevelVirtualModel(_project, ROOT_VIRTUAL_MODEL_NAME, ROOT_VIRTUAL_MODEL_URI);
	}

	@SuppressWarnings("unchecked")
	@Test
	@TestOrder(5)
	public void generatePersonListingVirtualModel() throws Exception {
		log("generatePersonListingVirtualModel");

		CreateSemanticsExcelVirtualModel action = CreateSemanticsExcelVirtualModel.actionType.makeNewAction(rootVirtualModel, null,
				_editor);
		action.setNewVirtualModelName(SE_VIRTUAL_MODEL_NAME);
		action.setExcelWorkbookResource(personListingResource);
		SEFlexoConceptSpecification personSpec = action.makeNewFlexoConceptSpecification();
		personSpec.setConceptName("Person");
		ExcelSheet sheet1 = personListingWB.getExcelSheetAtPosition(0);
		ExcelCell a2 = sheet1.getCellAt(1, 0);
		ExcelCell e7 = sheet1.getCellAt(6, 4);
		personSpec.setCellRange(personListingResource.getFactory().makeExcelCellRange(a2, e7));

		personSpec.getProperties().get(3).setPrimitiveType(PrimitiveType.Integer);

		action.doAction();
		mappingVirtualModel = action.getNewVirtualModel();

		assertNotNull(mappingVirtualModel);

		assertEquals(1, mappingVirtualModel.getFlexoConcepts().size());
		SEFlexoConcept personConcept = (SEFlexoConcept) mappingVirtualModel.getFlexoConcept("Person");
		assertNotNull(personConcept);

		SEColumnRole<String> sexe = (SEColumnRole<String>) personConcept.getAccessibleProperty("sexe");
		assertNotNull(sexe);
		assertEquals(PrimitiveType.String, sexe.getPrimitiveType());

		SEColumnRole<String> name = (SEColumnRole<String>) personConcept.getAccessibleProperty("name");
		assertNotNull(name);
		assertEquals(PrimitiveType.String, name.getPrimitiveType());

		SEColumnRole<String> activity = (SEColumnRole<String>) personConcept.getAccessibleProperty("activity");
		assertNotNull(activity);
		assertEquals(PrimitiveType.String, activity.getPrimitiveType());

		SEColumnRole<Integer> age = (SEColumnRole<Integer>) personConcept.getAccessibleProperty("age");
		assertNotNull(age);
		assertEquals(PrimitiveType.Integer, age.getPrimitiveType());

		SEColumnRole<String> city = (SEColumnRole<String>) personConcept.getAccessibleProperty("city");
		assertNotNull(city);
		assertEquals(PrimitiveType.String, city.getPrimitiveType());

		assertNotNull(mappingCreationScheme = mappingVirtualModel.getCreationSchemes().get(0));

		mappingVirtualModel.getResource().save();

		System.out.println(mappingVirtualModel.getFMLRepresentation());

		assertVirtualModelIsValid(mappingVirtualModel);
	}

	@Test
	@TestOrder(6)
	public void updateVirtualModel() throws Exception {
		log("updateVirtualModel");

		// Now we create the personListing model slot
		CreateModelSlot createMS1 = CreateModelSlot.actionType.makeNewAction(rootVirtualModel, null, _editor);
		createMS1.setTechnologyAdapter(getTA(ExcelTechnologyAdapter.class));
		createMS1.setModelSlotClass(SemanticsExcelModelSlot.class);
		createMS1.setModelSlotName("personListing");
		createMS1.setVmRes((VirtualModelResource) mappingVirtualModel.getResource());
		createMS1.doAction();
		assertTrue(createMS1.hasActionExecutionSucceeded());

		assertNotNull(modelSlot = (SemanticsExcelModelSlot) createMS1.getNewModelSlot());
		System.out.println("Created " + modelSlot);

		// And the creation scheme of the rootVirtualModel
		CreateFlexoBehaviour creationSchemeAction = CreateFlexoBehaviour.actionType.makeNewAction(rootVirtualModel, null, _editor);
		creationSchemeAction.setFlexoBehaviourClass(CreationScheme.class);
		creationSchemeAction.setFlexoBehaviourName("create");
		creationSchemeAction.doAction();
		creationScheme = (CreationScheme) creationSchemeAction.getNewFlexoBehaviour();

		CreateGenericBehaviourParameter createRootVMParameter = CreateGenericBehaviourParameter.actionType.makeNewAction(creationScheme,
				null, _editor);
		createRootVMParameter.setParameterName("excelResource");
		createRootVMParameter.setParameterType(
				FlexoResourceType.getFlexoResourceType(ExcelWorkbook.class, serviceManager.getTechnologyAdapterService()));
		createRootVMParameter.doAction();
		FlexoBehaviourParameter param = createRootVMParameter.getNewParameter();
		assertNotNull(param);

		CreateEditionAction createEditionAction1 = CreateEditionAction.actionType.makeNewAction(creationScheme.getControlGraph(), null,
				_editor);
		createEditionAction1.setEditionActionClass(CreateSEResource.class);
		createEditionAction1.setAssignation(new DataBinding<>("personListing"));
		createEditionAction1.doAction();
		AssignationAction<?> action1 = (AssignationAction<?>) createEditionAction1.getNewEditionAction();

		CreateSEResource createSEResourceAction = (CreateSEResource) action1.getAssignableAction();
		createSEResourceAction.setExcelWorkbook(new DataBinding<>("parameters.excelResource.getResourceData(null)"));
		createSEResourceAction.setResourceName(new DataBinding<String>("(this.name + \"_xls\")"));
		createSEResourceAction.setResourceCenter(new DataBinding<FlexoResourceCenter<?>>("this.resourceCenter"));
		createSEResourceAction.setCreationScheme(mappingCreationScheme);

		rootVirtualModel.getResource().save();

		System.out.println(rootVirtualModel.getFMLRepresentation());

		assertVirtualModelIsValid(rootVirtualModel);

	}

	@Test
	@TestOrder(7)
	public void instantiate() throws Exception {

		log("instantiate");

		CreateBasicVirtualModelInstance action = CreateBasicVirtualModelInstance.actionType
				.makeNewAction(_project.getVirtualModelInstanceRepository().getRootFolder(), null, _editor);
		action.setNewVirtualModelInstanceName(VIRTUAL_MODEL_INSTANCE_NAME);
		action.setVirtualModel(rootVirtualModel);
		action.setCreationScheme(creationScheme);
		action.setParameterValue(creationScheme.getParameter("excelResource"), personListing2Resource);
		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
		vmi = action.getNewVirtualModelInstance();

		seVMI = vmi.execute("personListing");
		assertNotNull(seVMI);

		assertEquals(4, seVMI.getFlexoConceptInstances().size());
		SEFlexoConceptInstance jeanDupont = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(0);
		SEFlexoConceptInstance bernadetteDupont = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(1);
		SEFlexoConceptInstance julesDupont = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(2);
		SEFlexoConceptInstance ninaDupont = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(3);
		// SEFlexoConceptInstance gerardMenvusat = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(4);
		// SEFlexoConceptInstance alainTerrieur = (SEFlexoConceptInstance) seVMI.getFlexoConceptInstances().get(5);

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
		assertEquals(4, allPersons.size());
		assertSameList(allPersons, jeanDupont, bernadetteDupont, julesDupont, ninaDupont);

		/*System.out.println("Requesting clients...");
		
		List<HbnFlexoConceptInstance> smithClients = salesman1.execute("clients");
		
		assertNotNull(smithClients);
		assertEquals(4, smithClients.size());
		
		HbnFlexoConceptInstance client1 = smithClients.get(0);
		HbnFlexoConceptInstance client5 = smithClients.get(1);
		HbnFlexoConceptInstance client9 = smithClients.get(2);
		HbnFlexoConceptInstance client13 = smithClients.get(3);
		
		assertEquals(1, (long) client1.execute("id"));
		assertEquals(5, (long) client5.execute("id"));
		assertEquals(9, (long) client9.execute("id"));
		assertEquals(13, (long) client13.execute("id"));*/

	}

	/*@Test
	@TestOrder(8)
	public void createNew() throws Exception {
	
		log("createNew");
		HbnFlexoConceptInstance salesman1 = (HbnFlexoConceptInstance) dbVMI.getFlexoConceptInstances().get(0);
		assertNotNull(salesman1);
	
		assertEquals(((List<?>) salesman1.getFlexoPropertyValue("clients")).size(), 4);
	
		FlexoConcept clientConcept = mappingVirtualModel.getFlexoConcept("Client");
		FlexoConcept salesmanConcept = mappingVirtualModel.getFlexoConcept("Salesman");
		assertNotNull(clientConcept);
		assertNotNull(salesmanConcept);
		FlexoProperty<String> clientNameProperty = (FlexoProperty<String>) clientConcept.getAccessibleProperty("name");
		assertNotNull(clientNameProperty);
		FlexoConceptInstanceRole clientSalesmanProperty = (FlexoConceptInstanceRole) clientConcept.getAccessibleProperty("salesman");
		assertNotNull(clientSalesmanProperty);
	
		HbnFlexoConceptInstance clientN1 = salesman1.execute("this.createClient()");
		assertNotNull(clientN1);
		clientN1.setFlexoPropertyValue(clientNameProperty, "MON CLIENT NOUVEAU");
		// clientN1.setFlexoPropertyValue(clientSalesmanProperty, salesman1);
	
		assertEquals(((List<?>) salesman1.getFlexoPropertyValue("clients")).size(), 5);
	
	}*/

}
