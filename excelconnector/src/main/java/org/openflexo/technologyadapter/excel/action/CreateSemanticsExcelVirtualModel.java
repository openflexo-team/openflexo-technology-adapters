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

package org.openflexo.technologyadapter.excel.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.type.PrimitiveType;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceType;
import org.openflexo.foundation.fml.FlexoProperty;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.action.AbstractCreateNatureSpecificVirtualModel;
import org.openflexo.foundation.fml.action.AddUseDeclaration;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateFlexoConcept;
import org.openflexo.foundation.fml.action.PropertyEntry;
import org.openflexo.foundation.fml.action.PropertyEntry.PropertyType;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.task.Progress;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.SemanticsExcelModelSlot;
import org.openflexo.technologyadapter.excel.action.CreateSemanticsExcelVirtualModel.SEFlexoConceptSpecification.SEFlexoPropertySpecification;
import org.openflexo.technologyadapter.excel.model.ExcelCell;
import org.openflexo.technologyadapter.excel.model.ExcelCellRange;
import org.openflexo.technologyadapter.excel.model.ExcelColumn;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.technologyadapter.excel.semantics.fml.SEColumnRole;
import org.openflexo.technologyadapter.excel.semantics.fml.SEInitializer;
import org.openflexo.technologyadapter.excel.semantics.fml.SEReferenceRole;
import org.openflexo.technologyadapter.excel.semantics.fml.SEVirtualModel;
import org.openflexo.toolbox.JavaUtils;
import org.openflexo.toolbox.StringUtils;

/**
 * This action allows to create a {@link VirtualModel} defined as a contract for interpretating data in a excel workbook as
 * {@link FlexoConceptInstance}
 * 
 * @author sylvain
 *
 */
public class CreateSemanticsExcelVirtualModel extends AbstractCreateNatureSpecificVirtualModel<CreateSemanticsExcelVirtualModel> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateSemanticsExcelVirtualModel.class.getPackage().getName());

	public static FlexoActionFactory<CreateSemanticsExcelVirtualModel, FlexoObject, FMLObject> actionType = new FlexoActionFactory<CreateSemanticsExcelVirtualModel, FlexoObject, FMLObject>(
			"create_semantics_excel_virtual_model", FlexoActionFactory.newVirtualModelMenu, FlexoActionFactory.defaultGroup,
			FlexoActionFactory.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateSemanticsExcelVirtualModel makeNewAction(FlexoObject focusedObject, Vector<FMLObject> globalSelection,
				FlexoEditor editor) {
			return new CreateSemanticsExcelVirtualModel(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FlexoObject object, Vector<FMLObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FlexoObject object, Vector<FMLObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateSemanticsExcelVirtualModel.actionType, VirtualModel.class);
		FlexoObjectImpl.addActionForClass(CreateSemanticsExcelVirtualModel.actionType, RepositoryFolder.class);
	}

	private VirtualModel newVirtualModel;

	/*private JDBCDbType dbType;
	private String address;
	private String user;
	private String password;
	
	private JDBCConnection jdbcConnection = null;
	private List<JDBCTable> tablesToBeReflected;*/

	CreateSemanticsExcelVirtualModel(FlexoObject focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public ExcelTechnologyAdapter getTechnologyAdapter() {
		return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(ExcelTechnologyAdapter.class);
	}

	@Override
	protected void performCreateBehaviours() {
		setDefineSomeBehaviours(true);

		super.performCreateBehaviours();

		CreateFlexoBehaviour createHbnInitializer = CreateFlexoBehaviour.actionType.makeNewEmbeddedAction(getNewFlexoConcept(), null, this);
		createHbnInitializer.setFlexoBehaviourName("initialize");
		createHbnInitializer.setFlexoBehaviourClass(SEInitializer.class);
		createHbnInitializer.doAction();
		SEInitializer initializer = (SEInitializer) createHbnInitializer.getNewFlexoBehaviour();

	}

	@Override
	protected void doAction(Object context) throws FlexoException {

		Progress.progress(getLocales().localizedForKey("create_virtual_model"));

		try {
			setSpecializedVirtualModelClass(SEVirtualModel.class);
			VirtualModelResource vmResource = makeVirtualModelResource();
			newVirtualModel = vmResource.getLoadedResourceData();
			newVirtualModel.setDescription(getNewVirtualModelDescription());
			newVirtualModel.setAbstract(true);
			newVirtualModel.setModelSlotNatureClass(SemanticsExcelModelSlot.class);
		} catch (SaveResourceException e) {
			throw new SaveResourceException(null);
		} catch (ModelDefinitionException e) {
			throw new FlexoException(e);
		}

		AddUseDeclaration useDeclarationAction = AddUseDeclaration.actionType.makeNewEmbeddedAction(newVirtualModel, null, this);
		useDeclarationAction.setModelSlotClass(SemanticsExcelModelSlot.class);
		useDeclarationAction.doAction();

		performSetParentConcepts();
		performCreateProperties();
		performCreateBehaviours();
		performCreateInspectors();
		performPostProcessings();

		for (SEFlexoConceptSpecification conceptSpecification : getSEConcepts()) {
			CreateFlexoConcept createConceptAction = CreateFlexoConcept.actionType.makeNewEmbeddedAction(newVirtualModel, null, this);
			createConceptAction.setNewFlexoConceptName(conceptSpecification.getConceptName());
			if (StringUtils.isNotEmpty(conceptSpecification.getConceptDescription())) {
				createConceptAction.setNewFlexoConceptDescription(conceptSpecification.getConceptDescription());
			}
			for (SEFlexoPropertySpecification propertySpec : conceptSpecification.getProperties()) {
				PropertyEntry propertyEntry = createConceptAction.newPropertyEntry();
				propertyEntry.setName(propertySpec.getPropertyName());
				switch (propertySpec.getMappingType()) {
					case Primitive:
						propertyEntry.setPropertyType(PropertyType.TECHNOLOGY_ROLE);
						propertyEntry.setFlexoRoleClass((Class) SEColumnRole.class);
						propertyEntry.setType(propertySpec.getPrimitiveType().getType());
						System.out.println("Property " + propertyEntry + " type=" + propertyEntry.getType());
						break;
					case Reference:
						propertyEntry.setPropertyType(PropertyType.TECHNOLOGY_ROLE);
						propertyEntry.setFlexoRoleClass(SEReferenceRole.class);
						propertyEntry.setType(FlexoConceptInstanceType.UNDEFINED_FLEXO_CONCEPT_INSTANCE_TYPE);
						if (propertySpec.getOppositeConcept() != null && propertySpec.getOppositeConcept().getConcept() != null) {
							propertyEntry.setType(propertySpec.getOppositeConcept().getConcept().getInstanceType());
						}
						break;
					default:
						// TODO
				}
			}

			// createConceptAction.setDefineSomeBehaviours(true);
			// createConceptAction.setDefineDefaultCreationScheme(true);

			createConceptAction.setDefineInspector(false);
			createConceptAction.doAction();

			conceptSpecification.concept = createConceptAction.getNewFlexoConcept();
			conceptSpecification.concept.setAbstract(true);

			for (SEFlexoPropertySpecification propertySpec : conceptSpecification.getProperties()) {
				propertySpec.property = conceptSpecification.concept.getDeclaredProperty(propertySpec.getPropertyName());
				switch (propertySpec.getMappingType()) {
					case Primitive:
						SEColumnRole<?> columnRole = (SEColumnRole) propertySpec.property;
						columnRole.setColumnIndex(propertySpec.getCell().getColumnIndex());
						columnRole.setPrimitiveType(propertySpec.getPrimitiveType());
						break;
					case Reference:
						SEReferenceRole referenceRole = (SEReferenceRole) propertySpec.property;
						referenceRole.setColumnIndex(propertySpec.getCell().getColumnIndex());
						break;
					default:
						// TODO
				}
				if (propertySpec.isKey()) {
					conceptSpecification.concept.addToKeyProperties(propertySpec.property);
				}
			}

			createConceptAction.setDefineInspector(true);
			createConceptAction.performCreateInspectors();

			// Create a default creation scheme
			CreateFlexoBehaviour createCreationScheme = CreateFlexoBehaviour.actionType.makeNewEmbeddedAction(conceptSpecification.concept,
					null, this);
			createCreationScheme.setFlexoBehaviourName("create");
			createCreationScheme.setFlexoBehaviourClass(CreationScheme.class);
			createCreationScheme.doAction();
			CreationScheme creationScheme = (CreationScheme) createCreationScheme.getNewFlexoBehaviour();
		}

		for (SEFlexoConceptSpecification conceptSpecification : getSEConcepts()) {
			for (SEFlexoPropertySpecification propertySpec : conceptSpecification.getProperties()) {
				FlexoProperty<?> property = conceptSpecification.concept.getDeclaredProperty(propertySpec.getPropertyName());
				if (property instanceof SEReferenceRole) {
					// ((SEReferenceRole) property).setForeignKeyAttributeName(propertySpec.getPropertyName());
					((SEReferenceRole) property).setVirtualModelInstance(new DataBinding<>("container"));
					if (propertySpec.getOppositeConcept() != null) {
						((SEReferenceRole) property).setFlexoConceptType(propertySpec.getOppositeConcept().getConcept());
					}
				}

			}
		}

		newVirtualModel.getPropertyChangeSupport().firePropertyChange("name", null, newVirtualModel.getName());
		newVirtualModel.getResource().getPropertyChangeSupport().firePropertyChange("name", null, newVirtualModel.getName());
	}

	@Override
	public VirtualModel getNewVirtualModel() {
		return newVirtualModel;
	}

	private ExcelWorkbookResource excelWorkbookResource;

	public ExcelWorkbookResource getExcelWorkbookResource() {
		return excelWorkbookResource;
	}

	public void setExcelWorkbookResource(ExcelWorkbookResource excelWorkbookResource) {
		if ((excelWorkbookResource == null && this.excelWorkbookResource != null)
				|| (excelWorkbookResource != null && !excelWorkbookResource.equals(this.excelWorkbookResource))) {
			ExcelWorkbookResource oldValue = this.excelWorkbookResource;
			this.excelWorkbookResource = excelWorkbookResource;
			getPropertyChangeSupport().firePropertyChange("excelWorkbookResource", oldValue, excelWorkbookResource);
		}
	}

	@Override
	public String getNewVirtualModelName() {
		if (StringUtils.isEmpty(super.getNewVirtualModelName()) && getExcelWorkbookResource() != null) {
			if (getExcelWorkbookResource().getName().contains(".")) {
				return getExcelWorkbookResource().getName().substring(0, getExcelWorkbookResource().getName().lastIndexOf("."));
			}
			else {
				return getExcelWorkbookResource().getName();
			}
		}
		return super.getNewVirtualModelName();
	}

	private List<SEFlexoConceptSpecification> seConcepts = new ArrayList<>();

	public List<SEFlexoConceptSpecification> getSEConcepts() {
		return seConcepts;
	}

	public void addToSEConcepts(SEFlexoConceptSpecification conceptIdentification) {
		seConcepts.add(conceptIdentification);
	}

	public void removeFromSEConcepts(SEFlexoConceptSpecification conceptIdentification) {
		seConcepts.remove(conceptIdentification);
	}

	public SEFlexoConceptSpecification makeNewFlexoConceptSpecification() {
		SEFlexoConceptSpecification returned = new SEFlexoConceptSpecification();
		addToSEConcepts(returned);
		return returned;
	}

	public class SEFlexoConceptSpecification {

		private FlexoConcept concept;
		private String conceptName;
		private ExcelCellRange cellRange;
		private final List<SEFlexoPropertySpecification> properties = new ArrayList<>();
		private String conceptDescription;

		private SEFlexoConceptSpecification() {

		}

		public FlexoConcept getConcept() {
			return concept;
		}

		public String getConceptName() {
			return conceptName;
		}

		public void setConceptName(String conceptName) {
			if ((conceptName == null && this.conceptName != null) || (conceptName != null && !conceptName.equals(this.conceptName))) {
				String oldValue = this.conceptName;
				this.conceptName = conceptName;
				getPropertyChangeSupport().firePropertyChange("conceptName", oldValue, conceptName);
			}
		}

		public String getConceptDescription() {
			return conceptDescription;
		}

		public void setConceptDescription(String conceptDescription) {
			if ((conceptDescription == null && this.conceptDescription != null)
					|| (conceptDescription != null && !conceptDescription.equals(this.conceptDescription))) {
				String oldValue = this.conceptDescription;
				this.conceptDescription = conceptDescription;
				getPropertyChangeSupport().firePropertyChange("conceptDescription", oldValue, conceptDescription);
			}
		}

		public ExcelCellRange getCellRange() {
			return cellRange;
		}

		public void setCellRange(ExcelCellRange cellRange) {
			if ((cellRange == null && this.cellRange != null) || (cellRange != null && !cellRange.equals(this.cellRange))) {
				ExcelCellRange oldValue = this.cellRange;
				this.cellRange = cellRange;
				updateProperties();
				getPropertyChangeSupport().firePropertyChange("cellRange", oldValue, cellRange);
			}
		}

		public List<SEFlexoPropertySpecification> getProperties() {
			return properties;
		}

		private void updateProperties() {
			properties.clear();
			if (cellRange != null) {
				int firstRow = cellRange.getTopLeftCell().getRowIndex();
				for (int colIndex = cellRange.getTopLeftCell().getColumnIndex(); colIndex <= cellRange.getBottomRightCell()
						.getColumnIndex(); colIndex++) {
					ExcelCell cell = cellRange.getExcelSheet().getCellAt(firstRow, colIndex);
					SEFlexoPropertySpecification spec = new SEFlexoPropertySpecification(cell);
					properties.add(spec);
				}
				getPropertyChangeSupport().firePropertyChange("properties", null, properties);
			}
			else {
				getPropertyChangeSupport().firePropertyChange("properties", new Object(), null);
			}
		}

		public class SEFlexoPropertySpecification {

			private ExcelCell headerCell;
			private ExcelCell cell;
			private String propertyName;
			private FlexoProperty<?> property;
			private SEPropertyMappingType mappingType;
			private boolean selectIt;
			private boolean isKey = false;
			private SEFlexoConceptSpecification oppositeConcept;
			private PrimitiveType primitiveType;

			public SEFlexoPropertySpecification(ExcelCell cell) {
				this.cell = cell;
				this.headerCell = cell.getUpperCell();
				if (headerCell != null) {
					propertyName = JavaUtils.getVariableName(headerCell.getCellValueAsString());
				}
				else {
					propertyName = "value" + ExcelColumn.getColumnLetters(cell.getColumnIndex());
				}
				mappingType = SEPropertyMappingType.Primitive;
				primitiveType = cell.getInferedPrimitiveType();
				selectIt = true;
			}

			/**
			 * Return first cell used as value range for this property specification
			 * 
			 * @return
			 */
			public ExcelCell getCell() {
				return cell;
			}

			public ExcelCell getHeaderCell() {
				return headerCell;
			}

			public String getPropertyName() {
				return propertyName;
			}

			public void setPropertyName(String propertyName) {
				if ((propertyName == null && this.propertyName != null)
						|| (propertyName != null && !propertyName.equals(this.propertyName))) {
					String oldValue = this.propertyName;
					this.propertyName = propertyName;
					getPropertyChangeSupport().firePropertyChange("propertyName", oldValue, propertyName);
				}
			}

			public boolean isKey() {
				return isKey;
			}

			public boolean selectIt() {
				return selectIt;
			}

			public void setSelectIt(boolean selectIt) {
				if (selectIt != this.selectIt) {
					this.selectIt = selectIt;
					getPropertyChangeSupport().firePropertyChange("selectIt", !selectIt, selectIt);
				}
			}

			public FlexoProperty<?> getProperty() {
				return property;
			}

			public SEPropertyMappingType getMappingType() {
				return mappingType;
			}

			public void setMappingType(SEPropertyMappingType mappingType) {
				if (mappingType != this.mappingType) {
					SEPropertyMappingType oldValue = this.mappingType;
					this.mappingType = mappingType;
					getPropertyChangeSupport().firePropertyChange("mappingType", oldValue, mappingType);
				}
			}

			public PrimitiveType getPrimitiveType() {
				return primitiveType;
			}

			public void setPrimitiveType(PrimitiveType primitiveType) {
				if (primitiveType != this.primitiveType) {
					PrimitiveType oldValue = this.primitiveType;
					this.primitiveType = primitiveType;
					getPropertyChangeSupport().firePropertyChange("primitiveType", oldValue, primitiveType);
				}
			}

			public SEFlexoConceptSpecification getOppositeConcept() {
				if (getMappingType() == SEPropertyMappingType.Primitive) {
					return null;
				}
				return oppositeConcept;
			}

			public void setOppositeConcept(SEFlexoConceptSpecification oppositeConcept) {
				if ((oppositeConcept == null && this.oppositeConcept != null)
						|| (oppositeConcept != null && !oppositeConcept.equals(this.oppositeConcept))) {
					SEFlexoConceptSpecification oldValue = this.oppositeConcept;
					this.oppositeConcept = oppositeConcept;
					getPropertyChangeSupport().firePropertyChange("oppositeConcept", oldValue, oppositeConcept);
				}
			}
		}

	}

	public static enum SEPropertyMappingType {
		Primitive, Reference
	}

	/*
	public List<JDBCTable> getTablesToBeReflected() {
		// Obtain the JDBCConnection
		if (jdbcConnection == null) {
			getJDBCConnection();
		}
		return tablesToBeReflected;
	}
	
	public List<JDBCTable> getAllTables() {
		if (getJDBCConnection().getConnection() != null && getJDBCConnection().getSchema() != null) {
			return getJDBCConnection().getSchema().getTables();
		}
		return Collections.emptyList();
	}
	
	private List<TableMapping> tableMappings;
	
	public List<TableMapping> getTableMappings() {
		if (tableMappings == null) {
			tableMappings = new ArrayList<>();
			for (JDBCTable t : getTablesToBeReflected()) {
				TableMapping tm = new TableMapping(t);
				tableMappings.add(tm);
			}
		}
		return tableMappings;
	}
	
	public void clearTableMappings() {
		tableMappings.clear();
		tableMappings = null;
	}
	
	public enum ColumnPropertyMappingType {
		Primitive, ForeignKey, ManyToMany
	}
	
	public class TableMapping {
	
		private JDBCTable table;
		private FlexoConcept concept;
		private String conceptName;
	
		private List<ColumnMapping> columnMappings;
	
		public TableMapping(JDBCTable table) {
			this.table = table;
			conceptName = table.getName().substring(0, 1).toUpperCase() + table.getName().substring(1).toLowerCase();
			columnMappings = new ArrayList<>();
			for (JDBCColumn col : table.getColumns()) {
				ColumnMapping colMapping = new ColumnMapping(col);
				columnMappings.add(colMapping);
			}
		}
	
		public JDBCTable getTable() {
			return table;
		}
	
		public List<ColumnMapping> getColumnMappings() {
			return columnMappings;
		}
	
		public String getConceptName() {
			return conceptName;
		}
	
		public FlexoConcept getConcept() {
			return concept;
		}
	
		public class ColumnMapping {
			private JDBCColumn column;
			private String propertyName;
			private String columnName;
			private FlexoProperty<?> property;
			private ColumnPropertyMappingType mappingType;
			private boolean selectIt;
			private boolean isPrimaryKey;
			private JDBCTable oppositeTable;
	
			public ColumnMapping(JDBCColumn column) {
				this.column = column;
				columnName = column.getName();
				propertyName = column.getName().toLowerCase();
				mappingType = ColumnPropertyMappingType.Primitive;
				selectIt = true;
				isPrimaryKey = column.isPrimaryKey();
				for (JDBCTable t : getAllTables()) {
					if (t.getName().toUpperCase().equals(column.getName().toUpperCase())) {
						mappingType = ColumnPropertyMappingType.ForeignKey;
						oppositeTable = t;
					}
				}
			}
	
			public JDBCColumn getColumn() {
				return column;
			}
	
			public String getPropertyName() {
				return propertyName;
			}
	
			public String getColumnName() {
				return columnName;
			}
	
			public boolean isPrimaryKey() {
				return isPrimaryKey;
			}
	
			public boolean selectIt() {
				return selectIt;
			}
	
			public void setSelectIt(boolean selectIt) {
				if (selectIt != this.selectIt) {
					this.selectIt = selectIt;
					getPropertyChangeSupport().firePropertyChange("selectIt", !selectIt, selectIt);
				}
			}
	
			public FlexoProperty<?> getProperty() {
				return property;
			}
	
			public ColumnPropertyMappingType getMappingType() {
				return mappingType;
			}
	
			public void setMappingType(ColumnPropertyMappingType mappingType) {
				if ((mappingType == null && this.mappingType != null) || (mappingType != null && !mappingType.equals(this.mappingType))) {
					ColumnPropertyMappingType oldValue = this.mappingType;
					this.mappingType = mappingType;
					getPropertyChangeSupport().firePropertyChange("mappingType", oldValue, mappingType);
				}
			}
	
			public JDBCTable getOppositeTable() {
				if (getMappingType() == ColumnPropertyMappingType.Primitive) {
					return null;
				}
				return oppositeTable;
			}
	
			public void setOppositeTable(JDBCTable oppositeTable) {
				if ((oppositeTable == null && this.oppositeTable != null)
						|| (oppositeTable != null && !oppositeTable.equals(this.oppositeTable))) {
					JDBCTable oldValue = this.oppositeTable;
					this.oppositeTable = oppositeTable;
					getPropertyChangeSupport().firePropertyChange("oppositeTable", oldValue, oppositeTable);
				}
			}
		}
	
	}*/

}
