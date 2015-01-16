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
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlide;

@FIBPanel("Fib/SelectPowerpointSlidePanel.fib")
@ModelEntity
@ImplementationClass(SelectPowerpointSlide.SelectPowerpointSlideImpl.class)
@XMLElement
@FML("SelectPowerpointSlide")
public interface SelectPowerpointSlide extends FetchRequest<BasicPowerpointModelSlot, PowerpointSlide> {

	public static abstract class SelectPowerpointSlideImpl extends FetchRequestImpl<BasicPowerpointModelSlot, PowerpointSlide> implements
			SelectPowerpointSlide {

		private static final Logger logger = Logger.getLogger(SelectPowerpointSlide.class.getPackage().getName());

		@Override
		public Type getFetchedType() {
			return PowerpointSlide.class;
		}

		@Override
		public List<PowerpointSlide> execute(FlexoBehaviourAction action) {

			if (getModelSlotInstance(action) == null) {
				logger.warning("Could not access model slot instance. Abort.");
				return null;
			}
			if (getModelSlotInstance(action).getResourceData() == null) {
				logger.warning("Could not access model adressed by model slot instance. Abort.");
				return null;
			}

			/*ExcelWorkbook excelWorkbook = (ExcelWorkbook) getModelSlotInstance(action).getResourceData();

			List<ExcelSheet> selectedExcelSheets = new ArrayList<ExcelSheet>(0);

			selectedExcelSheets.addAll(excelWorkbook.getExcelSheets());

			List<ExcelSheet> returned = filterWithConditions(selectedExcelSheets, action);*/

			return null;
		}
	}

}
