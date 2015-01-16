package org.openflexo.technologyadapter.excel.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.antar.binding.DataBinding;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.FetchRequest;
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

@FIBPanel("Fib/SelectExcelRowPanel.fib")
@ModelEntity
@ImplementationClass(SelectExcelRow.SelectExcelRowImpl.class)
@XMLElement
@FML("SelectExcelRow")
public interface SelectExcelRow extends FetchRequest<BasicExcelModelSlot, ExcelRow> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String EXCEL_SHEET_KEY = "excelSheet";

	@Getter(value = EXCEL_SHEET_KEY)
	@XMLAttribute
	public DataBinding<ExcelSheet> getExcelSheet();

	@Setter(EXCEL_SHEET_KEY)
	public void setExcelSheet(DataBinding<ExcelSheet> excelSheet);

	public static abstract class SelectExcelRowImpl extends FetchRequestImpl<BasicExcelModelSlot, ExcelRow> implements SelectExcelRow {

		private static final Logger logger = Logger.getLogger(SelectExcelRow.class.getPackage().getName());

		private DataBinding<ExcelSheet> excelSheet;

		public SelectExcelRowImpl() {
			super();
		}

		@Override
		public Type getFetchedType() {
			return ExcelRow.class;
		}

		@Override
		public List<ExcelRow> execute(FlexoBehaviourAction action) {

			if (getModelSlotInstance(action) == null) {
				logger.warning("Could not access model slot instance. Abort.");
				return null;
			}
			if (getModelSlotInstance(action).getResourceData() == null) {
				logger.warning("Could not access model adressed by model slot instance. Abort.");
				return null;
			}

			ExcelWorkbook excelWorkbook = (ExcelWorkbook) getModelSlotInstance(action).getAccessedResourceData();

			List<ExcelRow> selectedExcelRows = new ArrayList<ExcelRow>();
			ExcelSheet excelSheet;
			try {
				excelSheet = getExcelSheet().getBindingValue(action);

				if (excelSheet != null) {
					selectedExcelRows.addAll(excelSheet.getExcelRows());
				} else {
					for (ExcelSheet excelSheetItem : excelWorkbook.getExcelSheets()) {
						selectedExcelRows.addAll(excelSheetItem.getExcelRows());
					}
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

			List<ExcelRow> returned = filterWithConditions(selectedExcelRows, action);

			return returned;
		}

		@Override
		public DataBinding<ExcelSheet> getExcelSheet() {
			if (excelSheet == null) {
				excelSheet = new DataBinding<ExcelSheet>(this, ExcelSheet.class, DataBinding.BindingDefinitionType.GET);
				excelSheet.setBindingName("excelSheet");
			}
			return excelSheet;
		}

		@Override
		public void setExcelSheet(DataBinding<ExcelSheet> excelSheet) {
			if (excelSheet != null) {
				excelSheet.setOwner(this);
				excelSheet.setDeclaredType(ExcelSheet.class);
				excelSheet.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				excelSheet.setBindingName("excelSheet");
			}
			this.excelSheet = excelSheet;
		}
	}
}
