/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
 * 
 * This file is part of Flexo-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.docx.gui.widget;

import java.util.Collections;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;

import org.docx4all.swing.text.DocumentElement;
import org.docx4j.wml.P;
import org.openflexo.components.widget.FIBDocTableSelector;
import org.openflexo.foundation.doc.FlexoDocFragment;
import org.openflexo.foundation.doc.FlexoDocument;
import org.openflexo.foundation.task.FlexoTask;
import org.openflexo.gina.view.widget.FIBCustomWidget;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.gui.widget.AbstractDocXEditor.DocXEditorSelectionListener;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXElement;
import org.openflexo.technologyadapter.docx.model.DocXTable;
import org.openflexo.technologyadapter.docx.model.DocXTableCell;

/**
 * Widget allowing to select an {@link FlexoDocFragment} inside a {@link FlexoDocument}<br>
 * 
 * @author sguerin
 * 
 */
@SuppressWarnings("serial")
public class FIBDocXTableSelector extends FIBDocTableSelector<DocXTable, DocXDocument, DocXTechnologyAdapter> {
	static final Logger logger = Logger.getLogger(FIBDocXTableSelector.class.getPackage().getName());

	public static final Resource FIB_FILE = ResourceLocator.locateResource("Fib/Widget/FIBDocXTableSelector.fib");

	public FIBDocXTableSelector(DocXTable editedObject) {
		super(editedObject);
	}

	@Override
	public Resource getFIBResource() {
		return FIB_FILE;
	}

	@Override
	public Class<DocXTable> getRepresentedType() {
		return DocXTable.class;
	}

	@Override
	protected void selectTableInDocumentEditor(final DocXTable table, FIBCustomWidget<?, ?> documentEditorWidget) {

		final DocXEditor docXEditor = (DocXEditor) documentEditorWidget.getTechnologyComponent();

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (table == null) {
					docXEditor.getMLDocument().setSelectedElements(Collections.EMPTY_LIST);
					docXEditor.getEditorView().repaint();
					return;
				}

				try {
					DocumentElement tableElement = docXEditor.getMLDocument().getElement(table.getTbl());
					docXEditor.getMLDocument().setSelectedElements(Collections.singletonList(tableElement));
					docXEditor.getEditorView().scrollToElement(tableElement, false);
					docXEditor.getEditorView().repaint();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public class LoadDocXEditor extends FlexoTask {

		private final DocXTable table;
		private TableSelectorDetailsPanel panel;

		public LoadDocXEditor(DocXTable table) {
			super(FlexoLocalization.localizedForKey("opening_docx_editor"));
			this.table = table;
		}

		@Override
		public void performTask() throws InterruptedException {
			setExpectedProgressSteps(10);
			panel = FIBDocXTableSelector.super.makeCustomPanel(table);
		}

		public TableSelectorDetailsPanel getPanel() {
			return panel;
		}
	}

	@Override
	protected TableSelectorDetailsPanel makeCustomPanel(final DocXTable editedObject) {

		TableSelectorDetailsPanel returned = null;

		if (getServiceManager() != null && getServiceManager().getTaskManager() != null) {
			LoadDocXEditor task = new LoadDocXEditor(editedObject);
			getServiceManager().getTaskManager().scheduleExecution(task);
			getServiceManager().getTaskManager().waitTask(task);
			returned = task.getPanel();
		}
		else {
			returned = super.makeCustomPanel(editedObject);
		}

		FIBCustomWidget<?, ?> documentEditorWidget = returned.getDocEditorWidget();
		DocXEditor docXEditor = (DocXEditor) documentEditorWidget.getTechnologyComponent();
		docXEditor.getEditorView().addCaretListener(new DocXEditorSelectionListener(docXEditor) {
			@Override
			public void caretUpdate(CaretEvent e) {

				if (isSelecting) {
					return;
				}

				super.caretUpdate(e);

				int startLocation = getEditor().getEditorView().getSelectionStart();

				DocumentElement startParagraphMLElement = (DocumentElement) getEditor().getMLDocument().getParagraphMLElement(startLocation,
						false);

				Object startDocXObject = startParagraphMLElement.getElementML().getDocxObject();

				// System.out.println("start=" + startDocXObject + " of " + (startDocXObject != null ? startDocXObject.getClass() : null));

				DocXElement startElement = null;

				if (startDocXObject instanceof P) {
					startElement = getDocument().getParagraph((P) startDocXObject);
					if (startElement.getContainer() instanceof DocXTableCell) {
						startElement = (DocXTable) ((DocXTableCell) startElement.getContainer()).getRow().getTable();
					}
				}

				isSelecting = true;
				if (startElement instanceof DocXTable) {
					setEditedObject((DocXTable) startElement);
				}
				else {
					setEditedObject(null);
				}
				isSelecting = false;

			}
		});
		return returned;
	}

	@Override
	public void setSelectedObject(Object selectedObject) {
		if (selectedObject instanceof DocXTable) {
			super.setSelectedObject(selectedObject);
		}
		else {
			super.setSelectedObject(null);
		}
	}

	private boolean isSelecting = false;

}
