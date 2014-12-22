package org.openflexo.technologyadapter.excel;

import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.fmlrt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.fmlrt.action.ModelSlotInstanceConfiguration;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.FreeModelSlotInstanceConfiguration;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;

public class BasicExcelModelSlotInstanceConfiguration extends FreeModelSlotInstanceConfiguration<ExcelWorkbook, BasicExcelModelSlot> {

	private static final Logger logger = Logger.getLogger(ModelSlotInstanceConfiguration.class.getPackage().getName());

	protected List<ModelSlotInstanceConfigurationOption> options;

	protected FlexoResourceCenter<?> resourceCenter;
	protected TechnologyAdapterResource<ExcelWorkbook, ExcelTechnologyAdapter> modelResource;

	protected BasicExcelModelSlotInstanceConfiguration(BasicExcelModelSlot ms, CreateVirtualModelInstance action) {
		super(ms, action);
		setResourceUri(getAction().getFocusedObject().getProject().getURI() + "/Models/myExcelModel");
		setRelativePath("/");
		setFilename("myExcelResource.xls");
	}

	@Override
	protected boolean checkValidFileName() {
		if (!super.checkValidFileName()) {
			return false;
		}
		if (!getFilename().endsWith(".xls") && !getFilename().endsWith(".xlsx")) {
			setErrorMessage(FlexoLocalization.localizedForKey("file_name_should_end_with_.xls_or_.xlsx_suffix"));
			return false;
		}
		return true;
	}

}
