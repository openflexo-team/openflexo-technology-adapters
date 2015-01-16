package org.openflexo.technologyadapter.powerpoint.fml.editionaction;

import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.FetchRequest;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.powerpoint.BasicPowerpointModelSlot;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointShape;

@FIBPanel("Fib/SelectPowerpointShapePanel.fib")
@ModelEntity
@ImplementationClass(SelectPowerpointShape.SelectPowerpointShapeImpl.class)
@XMLElement
@FML("SelectPowerpointShape")
public interface SelectPowerpointShape extends FetchRequest<BasicPowerpointModelSlot, PowerpointShape> {

	public static abstract class SelectPowerpointShapeImpl extends FetchRequestImpl<BasicPowerpointModelSlot, PowerpointShape> implements
			SelectPowerpointShape {

		private static final Logger logger = Logger.getLogger(SelectPowerpointShape.class.getPackage().getName());

		@Override
		public Type getFetchedType() {
			return PowerpointShape.class;
		}

		@Override
		public List<PowerpointShape> execute(FlexoBehaviourAction action) {

			if (getModelSlotInstance(action) == null) {
				logger.warning("Could not access model slot instance. Abort.");
				return null;
			}
			if (getModelSlotInstance(action).getResourceData() == null) {
				logger.warning("Could not access model adressed by model slot instance. Abort.");
				return null;
			}

			/*ExcelWorkbook excelWorkbook = (ExcelWorkbook) getModelSlotInstance(action).getResourceData();

			List<ExcelCell> selectedExcelCells = new ArrayList<ExcelCell>(0);
			for(ExcelSheet excelSheet : excelWorkbook.getExcelSheets()){
				for(ExcelRow excelRow : excelSheet.getExcelRows()){
					selectedExcelCells.addAll(excelRow.getExcelCells());
				}
			}

			List<ExcelCell> returned = filterWithConditions(selectedExcelCells, action);*/

			return null;
		}
	}

}
