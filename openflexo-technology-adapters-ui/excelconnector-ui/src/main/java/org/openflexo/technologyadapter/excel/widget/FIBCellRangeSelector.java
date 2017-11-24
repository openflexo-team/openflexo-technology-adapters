/**
 * 
 * Copyright (c) 2014-2017, Openflexo
 * 
 * This file is part of Flexo-Documentation-UI, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * Please not that some parts of that component are freely inspired from
 * Stanislav Lapitsky code (see http://java-sl.com/docx_editor_kit.html)
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

package org.openflexo.technologyadapter.excel.widget;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.components.widget.FIBFlexoObjectSelector;
import org.openflexo.foundation.task.FlexoTask;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.model.widget.FIBCustom;
import org.openflexo.gina.view.widget.FIBCustomWidget;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.excel.model.ExcelCellRange;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.view.ExcelSheetView;

/**
 * Widget allowing to select an {@link FlexoDocFragment} inside a {@link FlexoDocument}<br>
 * 
 * @author sguerin
 * 
 */
@SuppressWarnings("serial")
public class FIBCellRangeSelector extends FIBFlexoObjectSelector<ExcelCellRange> {

	static final Logger logger = Logger.getLogger(FIBCellRangeSelector.class.getPackage().getName());

	public static final Resource FIB_FILE = ResourceLocator.locateResource("Fib/Widget/FIBCellRangeSelector.fib");

	private ExcelWorkbook workbook;

	public FIBCellRangeSelector(ExcelCellRange editedObject) {
		super(editedObject);
		setSelectedValue(editedObject);
	}

	@Override
	public Resource getFIBResource() {
		return FIB_FILE;
	}

	@Override
	public Class<ExcelCellRange> getRepresentedType() {
		return ExcelCellRange.class;
	}

	public ExcelWorkbook getExcelWorkbook() {
		return workbook;
	}

	@CustomComponentParameter(name = "excelWorkbook", type = CustomComponentParameter.Type.MANDATORY)
	public void setExcelWorkbook(ExcelWorkbook workbook) {
		if ((workbook == null && this.workbook != null) || (workbook != null && !workbook.equals(this.workbook))) {
			ExcelWorkbook oldValue = this.workbook;
			this.workbook = workbook;
			getPropertyChangeSupport().firePropertyChange("workbook", oldValue, workbook);
		}
	}

	@Override
	public String renderedString(ExcelCellRange editedObject) {
		if (editedObject != null) {
			return editedObject.getExcelSheet().getName() + "[" + editedObject.getIdentifier() + "]";
		}
		return "";
	}

	@Override
	public void setEditedObject(ExcelCellRange object) {
		super.setEditedObject(object);
		setSelectedValue(object);
	}

	@Override
	protected RangeSelectorDetailsPanel makeCustomPanel(ExcelCellRange editedObject) {

		RangeSelectorDetailsPanel returned = null;

		if (getServiceManager() != null && getServiceManager().getTaskManager() != null) {
			LoadEditor task = new LoadEditor(editedObject);
			getServiceManager().getTaskManager().scheduleExecution(task);
			getServiceManager().getTaskManager().waitTask(task);
			returned = task.getPanel();
		}
		if (returned == null) {
			returned = new RangeSelectorDetailsPanel(editedObject);
		}

		FIBCustomWidget<?, ?, ?> documentEditorWidget = returned.getWorkbookEditorWidget();
		ExcelWorkbookEditorWidget wbEditor = (ExcelWorkbookEditorWidget) documentEditorWidget.getCustomComponent();
		wbEditor.getPropertyChangeSupport().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ExcelSheetView.SELECTED_CELL_RANGE)) {
					ExcelCellRange newRange = (ExcelCellRange) evt.getNewValue();
					setEditedObject(newRange);
				}
			}
		});
		return returned;
	}

	@Override
	protected CellRangeSelectorFIBController makeCustomFIBController(FIBComponent fibComponent) {
		return new CellRangeSelectorFIBController(fibComponent, this);
	}

	@Override
	protected CellRangeSelectorFIBController getController() {
		return (CellRangeSelectorFIBController) super.getController();
	}

	@Override
	public RangeSelectorDetailsPanel getCustomPanel() {
		return (RangeSelectorDetailsPanel) super.getCustomPanel();
	}

	public static class CellRangeSelectorFIBController extends SelectorFIBController {
		public CellRangeSelectorFIBController(final FIBComponent component, final FIBCellRangeSelector selector) {
			super(component, selector);
		}
	}

	public class RangeSelectorDetailsPanel extends SelectorDetailsPanel {

		private FIBCustomWidget<?, ?, ?> wbEditorWidget = null;

		protected RangeSelectorDetailsPanel(ExcelCellRange anObject) {
			super(anObject);
			update();
		}

		public FIBCustomWidget<?, ?, ?> getWorkbookEditorWidget() {
			if (wbEditorWidget == null) {
				wbEditorWidget = retrieveWorkbookEditorWidget();
			}
			return wbEditorWidget;
		}

		@SuppressWarnings("rawtypes")
		private FIBCustomWidget<?, ?, ?> retrieveWorkbookEditorWidget() {
			List<FIBComponent> listComponent = getFIBComponent().getAllSubComponents();
			for (FIBComponent c : listComponent) {
				if (c instanceof FIBCustom) {
					return (FIBCustomWidget) getController().viewForComponent(c);
				}
			}
			return null;
		}

		@Override
		public CellRangeSelectorFIBController getController() {
			return (CellRangeSelectorFIBController) super.getController();
		}

		@Override
		public void update() {
			getController().setDataObject(FIBCellRangeSelector.this);
			selectValue(getEditedObject());
		}

		@Override
		protected void selectValue(ExcelCellRange range) {
			if (getWorkbookEditorWidget() != null) {
				final ExcelWorkbookEditorWidget wbEditor = (ExcelWorkbookEditorWidget) getWorkbookEditorWidget().getCustomComponent();

				try {
					wbEditor.setCellRange(range);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public class LoadEditor extends FlexoTask {

		private final ExcelCellRange fragment;
		private RangeSelectorDetailsPanel panel;

		public LoadEditor(ExcelCellRange fragment) {
			super(FlexoLocalization.getMainLocalizer().localizedForKey("opening_document_editor"));
			this.fragment = fragment;
		}

		@Override
		public void performTask() throws InterruptedException {
			setExpectedProgressSteps(10);
			panel = new RangeSelectorDetailsPanel(fragment);
		}

		public RangeSelectorDetailsPanel getPanel() {
			return panel;
		}
	}

}
