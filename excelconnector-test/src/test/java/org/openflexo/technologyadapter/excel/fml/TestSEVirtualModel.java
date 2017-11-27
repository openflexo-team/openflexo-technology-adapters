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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.technologyadapter.excel.AbstractTestExcel;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.SemanticsExcelModelSlot;
import org.openflexo.technologyadapter.excel.action.CreateSemanticsExcelVirtualModel;
import org.openflexo.technologyadapter.excel.action.CreateSemanticsExcelVirtualModel.SEFlexoConceptSpecification;
import org.openflexo.technologyadapter.excel.model.ExcelCell;
import org.openflexo.technologyadapter.excel.model.ExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.technologyadapter.excel.semantics.fml.SEColumnRole;
import org.openflexo.technologyadapter.excel.semantics.fml.SEFlexoConcept;
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
		action.doAction();
		mappingVirtualModel = action.getNewVirtualModel();

		/*AddUseDeclaration useDeclarationAction = AddUseDeclaration.actionType.makeNewAction(mappingVirtualModel, null, _editor);
		useDeclarationAction.setModelSlotClass(HbnModelSlot.class);
		useDeclarationAction.doAction();*/

		assertNotNull(mappingVirtualModel);

		System.out.println("FML=" + mappingVirtualModel.getFMLRepresentation());

		mappingVirtualModel.getResource().save(null);

		assertEquals(1, mappingVirtualModel.getFlexoConcepts().size());
		SEFlexoConcept personConcept = (SEFlexoConcept) mappingVirtualModel.getFlexoConcept("Person");
		assertNotNull(personConcept);

		SEColumnRole<String> sexe = (SEColumnRole<String>) personConcept.getAccessibleProperty("sexe");
		assertNotNull(sexe);
		SEColumnRole<String> name = (SEColumnRole<String>) personConcept.getAccessibleProperty("name");
		assertNotNull(name);
		SEColumnRole<String> activity = (SEColumnRole<String>) personConcept.getAccessibleProperty("activity");
		assertNotNull(activity);
		SEColumnRole<String> age = (SEColumnRole<String>) personConcept.getAccessibleProperty("age");
		assertNotNull(age);
		SEColumnRole<String> city = (SEColumnRole<String>) personConcept.getAccessibleProperty("city");
		assertNotNull(city);

		/*assertEquals("container", salesman.getVirtualModelInstance().toString());
		assertEquals(salesmanConcept, salesman.getFlexoConceptType());
		
		CreateFlexoBehaviour createClientCreationScheme = CreateFlexoBehaviour.actionType.makeNewAction(clientConcept, null, _editor);
		createClientCreationScheme.setFlexoBehaviourClass(CreationScheme.class);
		createClientCreationScheme.doAction();
		CreationScheme clientCreationScheme = (CreationScheme) createClientCreationScheme.getNewFlexoBehaviour();
		
		CreateGenericBehaviourParameter createSalesmanParameter = CreateGenericBehaviourParameter.actionType
				.makeNewAction(clientCreationScheme, null, _editor);
		createSalesmanParameter.setParameterName("salesman");
		createSalesmanParameter.setParameterType(salesmanConcept.getInstanceType());
		createSalesmanParameter.doAction();
		FlexoBehaviourParameter param = createSalesmanParameter.getNewParameter();
		assertNotNull(param);
		
		CreateEditionAction createAssignSalesman = CreateEditionAction.actionType.makeNewAction(clientCreationScheme.getControlGraph(),
				null, _editor);
		createAssignSalesman.setEditionActionClass(ExpressionAction.class);
		createAssignSalesman.setAssignation(new DataBinding<Object>("salesman"));
		createAssignSalesman.doAction();
		AssignationAction<?> assignation = (AssignationAction<?>) createAssignSalesman.getNewEditionAction();
		ExpressionAction<?> expression = (ExpressionAction<?>) assignation.getAssignableAction();
		expression.setExpression(new DataBinding<>("parameters.salesman"));
		
		CreateEditionAction createSave = CreateEditionAction.actionType.makeNewAction(clientCreationScheme.getControlGraph(), null,
				_editor);
		createSave.setEditionActionClass(SaveHbnObject.class);
		createSave.doAction();
		SaveHbnObject save = (SaveHbnObject) createSave.getBaseEditionAction();
		save.setReceiver(new DataBinding<HbnVirtualModelInstance>("this.container"));
		save.setObject(new DataBinding<FlexoConceptInstance>("this"));
		
		HbnColumnRole<Integer> salesmanId = (HbnColumnRole<Integer>) salesmanConcept.getAccessibleProperty("id");
		assertNotNull(salesmanId);
		HbnColumnRole<String> lastname = (HbnColumnRole<String>) salesmanConcept.getAccessibleProperty("lastname");
		assertNotNull(lastname);
		HbnColumnRole<String> firstname = (HbnColumnRole<String>) salesmanConcept.getAccessibleProperty("firstname");
		assertNotNull(firstname);
		
		// We create a HbnOneToManyReferenceRole role
		CreateTechnologyRole createClientsRole = CreateTechnologyRole.actionType.makeNewAction(salesmanConcept, null, _editor);
		createClientsRole.setRoleName("clients");
		createClientsRole.setFlexoRoleClass(HbnOneToManyReferenceRole.class);
		createClientsRole.doAction();
		assertTrue(createClientsRole.hasActionExecutionSucceeded());
		HbnOneToManyReferenceRole clients = (HbnOneToManyReferenceRole) createClientsRole.getNewFlexoRole();
		clients.setFlexoConceptType(clientConcept);
		clients.setDestinationKeyColumnName(salesman.getName());
		assertNotNull(clients);
		
		CreateFlexoBehaviour createClientScheme = CreateFlexoBehaviour.actionType.makeNewAction(salesmanConcept, null, _editor);
		createClientScheme.setFlexoBehaviourClass(ActionScheme.class);
		createClientScheme.setFlexoBehaviourName("createClient");
		createClientScheme.doAction();
		ActionScheme createClients = (ActionScheme) createClientScheme.getNewFlexoBehaviour();
		
		CreateEditionAction createOpenTransaction = CreateEditionAction.actionType.makeNewAction(createClients.getControlGraph(), null,
				_editor);
		createOpenTransaction.setEditionActionClass(OpenTransaction.class);
		createOpenTransaction.doAction();
		OpenTransaction openTransaction = (OpenTransaction) createOpenTransaction.getBaseEditionAction();
		openTransaction.setReceiver(new DataBinding<HbnVirtualModelInstance>("container"));
		
		CreateEditionAction createClient = CreateEditionAction.actionType.makeNewAction(createClients.getControlGraph(), null, _editor);
		createClient.setEditionActionClass(CreateHbnObject.class);
		createClient.setDeclarationVariableName("newClient");
		createClient.doAction();
		DeclarationAction<?> declaration = (DeclarationAction<?>) createClient.getNewEditionAction();
		CreateHbnObject createObject = (CreateHbnObject) declaration.getAssignableAction();
		createObject.setReceiver(new DataBinding<>("container"));
		createObject.setFlexoConceptType(clientConcept);
		createObject.setCreationScheme(clientCreationScheme);
		createObject.getParameter("salesman").setValue(new DataBinding<Object>("this"));
		
		CreateEditionAction createRefresh = CreateEditionAction.actionType.makeNewAction(createClients.getControlGraph(), null, _editor);
		createRefresh.setEditionActionClass(RefreshHbnObject.class);
		createRefresh.doAction();
		RefreshHbnObject refresh = (RefreshHbnObject) createRefresh.getBaseEditionAction();
		refresh.setReceiver(new DataBinding<HbnVirtualModelInstance>("this.container"));
		refresh.setObject(new DataBinding<FlexoConceptInstance>("this"));
		
		CreateEditionAction createCommit = CreateEditionAction.actionType.makeNewAction(createClients.getControlGraph(), null, _editor);
		createCommit.setEditionActionClass(CommitTransaction.class);
		createCommit.doAction();
		CommitTransaction commitTransaction = (CommitTransaction) createCommit.getBaseEditionAction();
		commitTransaction.setReceiver(new DataBinding<HbnVirtualModelInstance>("container"));
		
		CreateEditionAction returnStatement = CreateEditionAction.actionType.makeNewAction(createClients.getControlGraph(), null, _editor);
		returnStatement.setEditionActionClass(ExpressionAction.class);
		returnStatement.doAction();
		ExpressionAction assignReturn = (ExpressionAction) returnStatement.getBaseEditionAction();
		assignReturn.setExpression(new DataBinding<>("newClient"));
		assignReturn.addReturnStatement();
		
		CreateFlexoBehaviour createCreationScheme = CreateFlexoBehaviour.actionType.makeNewAction(mappingVirtualModel, null, _editor);
		createCreationScheme.setFlexoBehaviourClass(CreationScheme.class);
		createCreationScheme.doAction();
		mappingCreationScheme = (CreationScheme) createCreationScheme.getNewFlexoBehaviour();
		
		CreateEditionAction createSelectClients1 = CreateEditionAction.actionType.makeNewAction(mappingCreationScheme.getControlGraph(),
				null, _editor);
		createSelectClients1.setEditionActionClass(PerformSQLQuery.class);
		createSelectClients1.setAssignation(new DataBinding<Object>("salesmen"));
		createSelectClients1.doAction();
		AssignationAction<?> assignation1 = (AssignationAction<?>) createSelectClients1.getNewEditionAction();
		PerformSQLQuery sqlQuery1 = (PerformSQLQuery) assignation1.getAssignableAction();
		sqlQuery1.setReceiver(new DataBinding<>("this"));
		sqlQuery1.setFlexoConceptType(salesmanConcept);
		
		mappingInitializer = mappingVirtualModel.getFlexoBehaviours(HbnInitializer.class).get(0);
		
		CreateEditionAction createSelectClients2 = CreateEditionAction.actionType.makeNewAction(mappingInitializer.getControlGraph(), null,
				_editor);
		createSelectClients2.setEditionActionClass(PerformSQLQuery.class);
		createSelectClients2.setAssignation(new DataBinding<Object>("salesmen"));
		createSelectClients2.doAction();
		AssignationAction<?> assignation2 = (AssignationAction<?>) createSelectClients2.getNewEditionAction();
		PerformSQLQuery sqlQuery2 = (PerformSQLQuery) assignation2.getAssignableAction();
		sqlQuery2.setReceiver(new DataBinding<>("this"));
		sqlQuery2.setFlexoConceptType(salesmanConcept);
		
		CreateFlexoConceptInstanceRole createSalesmenProperty = CreateFlexoConceptInstanceRole.actionType.makeNewAction(mappingVirtualModel,
				null, _editor);
		createSalesmenProperty.setRoleName("salesmen");
		createSalesmenProperty.setVirtualModelInstance(new DataBinding<>("this"));
		createSalesmenProperty.setFlexoConceptInstanceType(salesmanConcept);
		createSalesmenProperty.setCardinality(PropertyCardinality.ZeroMany);
		createSalesmenProperty.doAction();
		assertTrue(createSalesmenProperty.hasActionExecutionSucceeded());
		assertNotNull(salesmenProperty = createSalesmenProperty.getNewFlexoRole());
		assertNotNull(salesmenProperty);
		
		mappingVirtualModel.getResource().save(null);
		
		System.out.println(mappingVirtualModel.getFMLRepresentation());
		
		assertVirtualModelIsValid(mappingVirtualModel);
		*/
	}

	/*@Test
	@TestOrder(6)
	public void createConnexionInMetaModel() throws Exception {
	
		log("createConnexionInMetaModel");
	
		// Now we create the vm1 model slot
		CreateModelSlot createMS1 = CreateModelSlot.actionType.makeNewAction(rootVirtualModel, null, _editor);
		createMS1.setTechnologyAdapter(getTA(JDBCTechnologyAdapter.class));
		createMS1.setModelSlotClass(HbnModelSlot.class);
		createMS1.setModelSlotName("db");
		createMS1.setVmRes((VirtualModelResource) mappingVirtualModel.getResource());
		createMS1.doAction();
		assertTrue(createMS1.hasActionExecutionSucceeded());
	
		assertNotNull(modelSlot = (HbnModelSlot) createMS1.getNewModelSlot());
		System.out.println("Created " + modelSlot);
	
		CreateFlexoBehaviour createCreationScheme = CreateFlexoBehaviour.actionType.makeNewAction(rootVirtualModel, null, _editor);
		createCreationScheme.setFlexoBehaviourClass(CreationScheme.class);
		createCreationScheme.doAction();
		creationScheme = (CreationScheme) createCreationScheme.getNewFlexoBehaviour();
	
		CreateGenericBehaviourParameter createParameter1 = CreateGenericBehaviourParameter.actionType.makeNewAction(creationScheme, null,
				_editor);
		createParameter1.setParameterName("address");
		createParameter1.setParameterType(String.class);
		createParameter1.doAction();
		FlexoBehaviourParameter adressParam = createParameter1.getNewParameter();
		assertNotNull(adressParam);
	
		CreateGenericBehaviourParameter createParameter2 = CreateGenericBehaviourParameter.actionType.makeNewAction(creationScheme, null,
				_editor);
		createParameter2.setParameterName("user");
		createParameter2.setParameterType(String.class);
		createParameter2.doAction();
		FlexoBehaviourParameter userParam = createParameter2.getNewParameter();
		assertNotNull(userParam);
	
		CreateGenericBehaviourParameter createParameter3 = CreateGenericBehaviourParameter.actionType.makeNewAction(creationScheme, null,
				_editor);
		createParameter3.setParameterName("password");
		createParameter3.setParameterType(String.class);
		createParameter3.doAction();
		FlexoBehaviourParameter passwordParam = createParameter3.getNewParameter();
		assertNotNull(passwordParam);
	
	
		CreateEditionAction createConnectionAction = CreateEditionAction.actionType.makeNewAction(creationScheme.getControlGraph(), null,
				_editor);
		createConnectionAction.setEditionActionClass(CreateJDBCConnection.class);
		createConnectionAction.setDeclarationVariableName("connection");
		createConnectionAction.doAction();
	
		CreateJDBCConnection createJDBCConnectionAction = (CreateJDBCConnection) createConnectionAction.getBaseEditionAction();
		createJDBCConnectionAction.setReceiver(new DataBinding<JDBCConnection>("null"));
		createJDBCConnectionAction.setAddress(new DataBinding<String>("parameters.address"));
		createJDBCConnectionAction.setUser(new DataBinding<String>("parameters.user"));
		createJDBCConnectionAction.setPassword(new DataBinding<String>("parameters.password"));
		createJDBCConnectionAction.setDbType(JDBCDbType.HSQLDB);
		createJDBCConnectionAction.setResourceName(new DataBinding<String>("(this.name + \"_connection\")"));
		createJDBCConnectionAction.setResourceCenter(new DataBinding<FlexoResourceCenter<?>>("this.resourceCenter"));
	
		CreateEditionAction createEditionAction1 = CreateEditionAction.actionType.makeNewAction(creationScheme.getControlGraph(), null,
				_editor);
		createEditionAction1.setEditionActionClass(CreateHbnResource.class);
		createEditionAction1.setAssignation(new DataBinding<Object>("db"));
		createEditionAction1.doAction();
		AssignationAction<?> action1 = (AssignationAction<?>) createEditionAction1.getNewEditionAction();
	
		CreateHbnResource createHbnResourceAction = (CreateHbnResource) action1.getAssignableAction();
		createHbnResourceAction.setCreationScheme(mappingCreationScheme);
		createHbnResourceAction.setReceiver(new DataBinding<HbnVirtualModelInstance>("null"));
		createHbnResourceAction.setConnection(new DataBinding<JDBCConnection>("connection"));
		createHbnResourceAction.setResourceName(new DataBinding<String>("(this.name + \"_db\")"));
		createHbnResourceAction.setResourceCenter(new DataBinding<FlexoResourceCenter<?>>("this.resourceCenter"));
		createHbnResourceAction.setCreationScheme(mappingCreationScheme);
	
		rootVirtualModel.getResource().save(null);
	
		System.out.println(rootVirtualModel.getFMLRepresentation());
	
		assertVirtualModelIsValid(rootVirtualModel);
	}*/

	/*@Test
	@TestOrder(7)
	public void instantiate() throws Exception {
	
		log("instantiate");
	
		CreateBasicVirtualModelInstance action = CreateBasicVirtualModelInstance.actionType
				.makeNewAction(_project.getVirtualModelInstanceRepository().getRootFolder(), null, _editor);
		action.setNewVirtualModelInstanceName(VIRTUAL_MODEL_INSTANCE_NAME);
		action.setVirtualModel(rootVirtualModel);
		action.setCreationScheme(creationScheme);
		action.setParameterValue(creationScheme.getParameter("address"), "jdbc:hsqldb:mem:db");
		action.setParameterValue(creationScheme.getParameter("user"), "SA");
		action.setParameterValue(creationScheme.getParameter("password"), "");
		// action.setParameterValue(creationScheme.getParameter("dbtype"), JDBCDbType.HSQLDB);
		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
		vmi = action.getNewVirtualModelInstance();
	
		dbVMI = vmi.execute("db");
		assertNotNull(dbVMI);
	
		assertEquals(4, dbVMI.getFlexoConceptInstances().size());
		HbnFlexoConceptInstance salesman1 = (HbnFlexoConceptInstance) dbVMI.getFlexoConceptInstances().get(0);
		HbnFlexoConceptInstance salesman2 = (HbnFlexoConceptInstance) dbVMI.getFlexoConceptInstances().get(1);
		HbnFlexoConceptInstance salesman3 = (HbnFlexoConceptInstance) dbVMI.getFlexoConceptInstances().get(2);
		HbnFlexoConceptInstance salesman4 = (HbnFlexoConceptInstance) dbVMI.getFlexoConceptInstances().get(3);
	
		assertEquals(1, (long) salesman1.execute("id"));
		assertEquals("Smith", salesman1.execute("lastname"));
		assertEquals("Alan", salesman1.execute("firstname"));
	
		assertEquals(2, (long) salesman2.execute("id"));
		assertEquals("Rowland", salesman2.execute("lastname"));
		assertEquals("Steve", salesman2.execute("firstname"));
	
		assertEquals(3, (long) salesman3.execute("id"));
		assertEquals("Mac Lane", salesman3.execute("lastname"));
		assertEquals("Jason", salesman3.execute("firstname"));
	
		assertEquals(4, (long) salesman4.execute("id"));
		assertEquals("Brian", salesman4.execute("lastname"));
		assertEquals("Kelly", salesman4.execute("firstname"));
	
		System.out.println("Requesting clients...");
	
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
		assertEquals(13, (long) client13.execute("id"));
	
	}*/

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
