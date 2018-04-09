/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.technologyadapter.excel.widget;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.openflexo.gina.controller.FIBController;
import org.openflexo.gina.model.widget.FIBCustom;
import org.openflexo.gina.model.widget.FIBCustom.FIBCustomComponent;
import org.openflexo.swing.CustomPopup.ApplyCancelListener;
import org.openflexo.technologyadapter.excel.model.ExcelCellRange;
import org.openflexo.technologyadapter.excel.model.ExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.view.ExcelSheetView;
import org.openflexo.toolbox.HasPropertyChangeSupport;

/**
 * A widget presenting an Excel Workbook
 * 
 * @author Sylvain Guerin
 *
 */
@SuppressWarnings("serial")
public class ExcelWorkbookEditorWidget extends JPanel
		implements FIBCustomComponent<ExcelWorkbook>, ChangeListener, HasPropertyChangeSupport, PropertyChangeListener {

	private static final Logger logger = Logger.getLogger(ExcelWorkbookEditorWidget.class.getPackage().getName());

	private JTabbedPane tabbedPane;
	private ExcelWorkbook workbook;
	protected FIBCustom component;
	protected FIBController controller;

	private ExcelSheetView listenedSheetView = null;

	private PropertyChangeSupport pcSupport;

	private final List<ApplyCancelListener> applyCancelListener = new ArrayList<>();

	public ExcelWorkbookEditorWidget(ExcelWorkbook workbook) {
		super(new BorderLayout());
		pcSupport = new PropertyChangeSupport(this);

		tabbedPane = new JTabbedPane();
		tabbedPane.addChangeListener(this);

		setEditedObject(workbook);

		add(tabbedPane, BorderLayout.CENTER);
	}

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		return pcSupport;
	}

	@Override
	public String getDeletedProperty() {
		return "deleted";
	}

	@Override
	public void delete() {
		getPropertyChangeSupport().firePropertyChange("deleted", false, true);
		pcSupport = null;
	}

	@Override
	public ExcelWorkbook getEditedObject() {
		return workbook;
	}

	@Override
	public void setEditedObject(ExcelWorkbook workbook) {
		this.workbook = workbook;
		if (workbook != null) {
			for (ExcelSheet sheet : workbook.getExcelSheets()) {
				tabbedPane.addTab(sheet.getName(), new ExcelSheetView(sheet));
			}
		}
	}

	@Override
	public ExcelWorkbook getRevertValue() {
		return workbook;
	}

	@Override
	public void setRevertValue(ExcelWorkbook object) {
	}

	@Override
	public Class<ExcelWorkbook> getRepresentedType() {
		return ExcelWorkbook.class;
	}

	@Override
	public void addApplyCancelListener(ApplyCancelListener l) {
		applyCancelListener.add(l);
	}

	@Override
	public void removeApplyCancelListener(ApplyCancelListener l) {
		applyCancelListener.remove(l);
	}

	public void apply() {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("apply()");
		}
		notifyApplyPerformed();
	}

	public void notifyApplyPerformed() {
		for (ApplyCancelListener l : applyCancelListener) {
			l.fireApplyPerformed();
		}
	}

	public void cancel() {
		for (ApplyCancelListener l : applyCancelListener) {
			l.fireCancelPerformed();
		}
	}

	@Override
	public void init(FIBCustom component, FIBController controller) {
		this.component = component;
		this.controller = controller;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		ExcelSheetView newSheetView = (ExcelSheetView) tabbedPane.getSelectedComponent();
		if (newSheetView != listenedSheetView) {
			if (listenedSheetView != null) {
				listenedSheetView.getPropertyChangeSupport().removePropertyChangeListener(this);
			}
			newSheetView.getPropertyChangeSupport().addPropertyChangeListener(this);
			listenedSheetView = newSheetView;
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ExcelSheetView.SELECTED_CELL)) {
			getPropertyChangeSupport().firePropertyChange(evt);
		}
		if (evt.getPropertyName().equals(ExcelSheetView.SELECTED_CELL_RANGE)) {
			getPropertyChangeSupport().firePropertyChange(evt);
		}
	}

	public void setCellRange(ExcelCellRange range) {
		if (range != null) {
			ExcelSheet sheet = range.getExcelSheet();
			int sheetIndex = sheet.getExcelWorkbook().getExcelSheets().indexOf(sheet);
			tabbedPane.setSelectedIndex(sheetIndex);
			listenedSheetView.setCellRange(range);
		}
		else {
			listenedSheetView.setCellRange(null);
		}
	}
}
