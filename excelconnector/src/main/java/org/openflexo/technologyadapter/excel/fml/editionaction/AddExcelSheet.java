package org.openflexo.technologyadapter.excel.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openflexo.antar.binding.DataBinding;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.BasicExcelModelSlot;
import org.openflexo.technologyadapter.excel.model.ExcelRow;
import org.openflexo.technologyadapter.excel.model.ExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;

@FIBPanel("Fib/AddExcelSheetPanel.fib")
@ModelEntity
@ImplementationClass(AddExcelSheet.AddExcelSheetImpl.class)
@XMLElement
@FML("AddExcelSheet")
public interface AddExcelSheet extends ExcelAction<ExcelSheet> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String SHEET_NAME_KEY = "sheetName";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String SHEET_ROWS_KEY = "sheetRows";
	@PropertyIdentifier(type = boolean.class)
	public static final String OVERRIDE_KEY = "override";

	@Getter(value = SHEET_NAME_KEY)
	@XMLAttribute
	public DataBinding<String> getSheetName();

	@Setter(SHEET_NAME_KEY)
	public void setSheetName(DataBinding<String> sheetName);

	@Getter(value = SHEET_ROWS_KEY)
	@XMLAttribute
	public DataBinding<List<ExcelRow>> getSheetRows();

	@Setter(SHEET_ROWS_KEY)
	public void setSheetRows(DataBinding<List<ExcelRow>> sheetRows);

	@Getter(value = OVERRIDE_KEY, defaultValue = "false")
	@XMLAttribute
	public boolean getOverride();

	@Setter(OVERRIDE_KEY)
	public void setOverride(boolean override);

	public static abstract class AddExcelSheetImpl extends TechnologySpecificActionImpl<BasicExcelModelSlot, ExcelSheet> implements
			AddExcelSheet {

		private static final Logger logger = Logger.getLogger(AddExcelSheet.class.getPackage().getName());

		private DataBinding<String> sheetName;

		private DataBinding<List<ExcelRow>> sheetRows;

		private boolean override = false;

		public AddExcelSheetImpl() {
			super();
		}

		@Override
		public Type getAssignableType() {
			return ExcelSheet.class;
		}

		@Override
		public ExcelSheet execute(FlexoBehaviourAction action) {

			ExcelSheet result = null;

			FreeModelSlotInstance<ExcelWorkbook, BasicExcelModelSlot> modelSlotInstance = getModelSlotInstance(action);
			if (modelSlotInstance.getResourceData() != null) {
				Workbook wb = modelSlotInstance.getAccessedResourceData().getWorkbook();
				Sheet sheet = null;
				try {
					if (wb != null) {
						String name = getSheetName().getBindingValue(action);
						if (name != null) {
							// Create or retrieve this sheet
							sheet = retrieveOrCreateSheet(wb, name);
							// Instanciate Wrapper.
							result = modelSlotInstance.getAccessedResourceData().getConverter()
									.convertExcelSheetToSheet(sheet, modelSlotInstance.getAccessedResourceData(), null);
							modelSlotInstance.getAccessedResourceData().addToExcelSheets(result);
							modelSlotInstance.getAccessedResourceData().setIsModified();
						} else {
							logger.warning("Create a sheet requires a name");
						}
					} else {
						logger.warning("Create a sheet requires a workbook");
					}
				} catch (TypeMismatchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullReferenceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				logger.warning("Model slot not correctly initialised : model is null");
				return null;
			}

			return result;
		}

		// Create an Excel Sheet or get the existing one.
		private Sheet retrieveOrCreateSheet(Workbook wb, String name) {
			Sheet sheet = null;
			// A sheet with this name already exists
			if (wb.getSheet(name) != null) {
				if (override) {
					// Override it
					wb.removeSheetAt(wb.getSheetIndex(name));
					sheet = wb.createSheet(name);
					logger.info("Override excel sheet with the name " + name);
				} else {
					// Retrieve the existing one
					sheet = wb.getSheet(name);
					logger.warning("An excel sheet already exists with this name " + name + " , retrieve existing sheet");
				}
			} else {
				// Create a new one
				sheet = wb.createSheet(name);
				logger.info("Create a new excel sheet with the name " + name);
			}
			return sheet;
		}

		@Override
		public FreeModelSlotInstance<ExcelWorkbook, BasicExcelModelSlot> getModelSlotInstance(FlexoBehaviourAction action) {
			return (FreeModelSlotInstance<ExcelWorkbook, BasicExcelModelSlot>) super.getModelSlotInstance(action);
		}

		@Override
		public DataBinding<String> getSheetName() {
			if (sheetName == null) {
				sheetName = new DataBinding<String>(this, String.class, DataBinding.BindingDefinitionType.GET);
				sheetName.setBindingName("sheetName");
			}
			return sheetName;
		}

		@Override
		public void setSheetName(DataBinding<String> sheetName) {
			if (sheetName != null) {
				sheetName.setOwner(this);
				sheetName.setDeclaredType(String.class);
				sheetName.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				sheetName.setBindingName("sheetName");
			}
			this.sheetName = sheetName;
		}

		@Override
		public DataBinding<List<ExcelRow>> getSheetRows() {
			if (sheetRows == null) {
				sheetRows = new DataBinding<List<ExcelRow>>(this, List.class, DataBinding.BindingDefinitionType.GET);
				sheetRows.setBindingName("sheetRows");
			}
			return sheetRows;
		}

		@Override
		public void setSheetRows(DataBinding<List<ExcelRow>> sheetRows) {
			if (sheetRows != null) {
				sheetRows.setOwner(this);
				sheetRows.setDeclaredType(List.class);
				sheetRows.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				sheetRows.setBindingName("sheetRows");
			}
			this.sheetRows = sheetRows;
		}

		@Override
		public boolean getOverride() {
			return override;
		}

		@Override
		public void setOverride(boolean override) {
			this.override = override;
		}
	}
}
