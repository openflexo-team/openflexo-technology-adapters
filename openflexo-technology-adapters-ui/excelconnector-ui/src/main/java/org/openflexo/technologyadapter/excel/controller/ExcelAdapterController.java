/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel.controller;

import javax.swing.ImageIcon;

import org.openflexo.foundation.fml.FlexoBehaviourParameter;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.model.FIBModelFactory;
import org.openflexo.gina.model.container.FIBPanel;
import org.openflexo.gina.utils.InspectorGroup;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.fml.ExcelCellRole;
import org.openflexo.technologyadapter.excel.fml.ExcelRowRole;
import org.openflexo.technologyadapter.excel.fml.ExcelSheetRole;
import org.openflexo.technologyadapter.excel.fml.editionaction.AddExcelCell;
import org.openflexo.technologyadapter.excel.fml.editionaction.AddExcelRow;
import org.openflexo.technologyadapter.excel.fml.editionaction.AddExcelSheet;
import org.openflexo.technologyadapter.excel.fml.editionaction.CellStyleAction;
import org.openflexo.technologyadapter.excel.fml.editionaction.CreateExcelResource;
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

	/**
	 * Initialize inspectors for supplied module using supplied {@link FlexoController}
	 * 
	 * @param controller
	 */
	@Override
	protected void initializeInspectors(FlexoController controller) {

		csvInspectorGroup = controller.loadInspectorGroup("Excel", getTechnologyAdapter().getLocales(),
				getFMLTechnologyAdapterInspectorGroup());
		// actionInitializer.getController().getModuleInspectorController().loadDirectory(ResourceLocator.locateResource("Inspectors/Excel"));
	}

	private InspectorGroup csvInspectorGroup;

	/**
	 * Return inspector group for this technology
	 * 
	 * @return
	 */
	@Override
	public InspectorGroup getTechnologyAdapterInspectorGroup() {
		return csvInspectorGroup;
	}

	@Override
	protected void initializeActions(ControllerActionInitializer actionInitializer) {
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
	public ImageIcon getIconForFlexoRole(Class<? extends FlexoRole<?>> patternRoleClass) {
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
	public ImageIcon getIconForEditionAction(Class<? extends EditionAction> editionActionClass) {
		if (CreateExcelResource.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(ExcelWorkbook.class), IconLibrary.DUPLICATE);
		}
		else if (AddExcelSheet.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(ExcelSheet.class), IconLibrary.DUPLICATE);
		}
		else if (AddExcelCell.class.isAssignableFrom(editionActionClass)) {
			return ExcelIconLibrary.ADD_EXCEL_CELL_ICON;
		}
		else if (AddExcelRow.class.isAssignableFrom(editionActionClass)) {
			return ExcelIconLibrary.ADD_EXCEL_ROW_ICON;
		}
		else if (CellStyleAction.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(ExcelIconLibrary.EXCEL_GRAPHICAL_ACTION_ICON, IconLibrary.DUPLICATE);
		}
		else if (SelectExcelSheet.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(ExcelSheet.class), IconLibrary.IMPORT);
		}
		else if (SelectExcelRow.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(ExcelRow.class), IconLibrary.IMPORT);
		}
		else if (SelectExcelCell.class.isAssignableFrom(editionActionClass)) {
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

	/**
	 * Factory method used to instanciate a technology-specific FIBWidget for a given {@link FlexoBehaviourParameter}<br>
	 * Provides a hook to specialize this method in a given technology
	 * 
	 * @param parameter
	 * @param panel
	 * @param index
	 * @return
	 */
	@Override
	public FIBComponent makeWidget(final FlexoBehaviourParameter parameter, FIBPanel panel, int index, FlexoBehaviourAction<?, ?, ?> action,
			FIBModelFactory fibModelFactory) {
		return super.makeWidget(parameter, panel, index, action, fibModelFactory);
	}

}
