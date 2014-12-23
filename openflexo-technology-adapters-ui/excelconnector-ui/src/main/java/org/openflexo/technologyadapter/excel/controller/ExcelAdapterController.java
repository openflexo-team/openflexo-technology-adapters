package org.openflexo.technologyadapter.excel.controller;

import javax.swing.ImageIcon;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.fml.ExcelCellRole;
import org.openflexo.technologyadapter.excel.fml.ExcelRowRole;
import org.openflexo.technologyadapter.excel.fml.ExcelSheetRole;
import org.openflexo.technologyadapter.excel.fml.editionaction.AddExcelCell;
import org.openflexo.technologyadapter.excel.fml.editionaction.AddExcelRow;
import org.openflexo.technologyadapter.excel.fml.editionaction.AddExcelSheet;
import org.openflexo.technologyadapter.excel.fml.editionaction.CellStyleAction;
import org.openflexo.technologyadapter.excel.fml.editionaction.SelectExcelCell;
import org.openflexo.technologyadapter.excel.fml.editionaction.SelectExcelRow;
import org.openflexo.technologyadapter.excel.fml.editionaction.SelectExcelSheet;
import org.openflexo.technologyadapter.excel.gui.ExcelIconLibrary;
import org.openflexo.technologyadapter.excel.model.ExcelCell;
import org.openflexo.technologyadapter.excel.model.ExcelRow;
import org.openflexo.technologyadapter.excel.model.ExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.view.ExcelWorkbookView;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class ExcelAdapterController extends TechnologyAdapterController<ExcelTechnologyAdapter> {

	@Override
	public Class<ExcelTechnologyAdapter> getTechnologyAdapterClass() {
		return ExcelTechnologyAdapter.class;
	}

	@Override
	public void initializeActions(ControllerActionInitializer actionInitializer) {
		actionInitializer.getController().getModuleInspectorController().loadDirectory(ResourceLocator.locateResource("Inspectors/Excel"));
	}

	@Override
	public ImageIcon getTechnologyBigIcon() {
		return ExcelIconLibrary.EXCEL_TECHNOLOGY_BIG_ICON;
	}

	@Override
	public ImageIcon getTechnologyIcon() {
		return ExcelIconLibrary.EXCEL_TECHNOLOGY_ICON;
	}

	@Override
	public ImageIcon getModelIcon() {
		// TODO Auto-generated method stub
		return ExcelIconLibrary.EXCEL_TECHNOLOGY_ICON;
	}

	@Override
	public ImageIcon getMetaModelIcon() {
		return ExcelIconLibrary.EXCEL_TECHNOLOGY_ICON;
	}

	@Override
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject<?>> objectClass) {
		return ExcelIconLibrary.iconForObject(objectClass);
	}

	@Override
	public ImageIcon getIconForPatternRole(Class<? extends FlexoRole<?>> patternRoleClass) {
		if (ExcelSheetRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(ExcelSheet.class);
		}
		if (ExcelCellRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(ExcelCell.class);
		}
		if (ExcelRowRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(ExcelRow.class);
		}
		return null;
	}

	/**
	 * Return icon representing supplied edition action
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForEditionAction(Class<? extends EditionAction<?, ?>> editionActionClass) {
		if (AddExcelSheet.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(ExcelSheet.class), IconLibrary.DUPLICATE);
		} else if (AddExcelCell.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(ExcelCell.class), IconLibrary.DUPLICATE);
		} else if (AddExcelRow.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(ExcelRow.class), IconLibrary.DUPLICATE);
		} else if (CellStyleAction.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(ExcelIconLibrary.EXCEL_GRAPHICAL_ACTION_ICON, IconLibrary.DUPLICATE);
		} else if (SelectExcelSheet.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(ExcelSheet.class), IconLibrary.IMPORT);
		} else if (SelectExcelRow.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(ExcelRow.class), IconLibrary.IMPORT);
		} else if (SelectExcelCell.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(ExcelCell.class), IconLibrary.IMPORT);
		}
		return super.getIconForEditionAction(editionActionClass);
	}

	@Override
	public boolean hasModuleViewForObject(TechnologyObject object, FlexoController controller) {
		if (object instanceof ExcelWorkbook) {
			return true;
		}
		return false;
	}

	@Override
	public String getWindowTitleforObject(TechnologyObject object, FlexoController controller) {
		if (object instanceof ExcelWorkbook) {
			return ((ExcelWorkbook) object).getName();
		}
		return object.toString();
	}

	@Override
	public ModuleView<?> createModuleViewForObject(TechnologyObject<ExcelTechnologyAdapter> object, FlexoController controller,
			FlexoPerspective perspective) {
		if (object instanceof ExcelWorkbook) {
			return new ExcelWorkbookView((ExcelWorkbook) object, controller, perspective);
		}
		return new EmptyPanel<TechnologyObject<ExcelTechnologyAdapter>>(controller, perspective, object);
	}

}
