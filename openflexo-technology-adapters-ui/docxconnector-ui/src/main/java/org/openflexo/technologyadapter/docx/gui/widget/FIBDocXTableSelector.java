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

import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.text.Element;

import org.openflexo.components.doc.editorkit.FlexoDocumentEditorWidget;
import org.openflexo.components.doc.editorkit.FlexoStyledDocument;
import org.openflexo.components.doc.editorkit.element.AbstractDocumentElement;
import org.openflexo.components.widget.FIBDocTableSelector;
import org.openflexo.foundation.doc.FlexoDocElement;
import org.openflexo.foundation.doc.FlexoDocFragment;
import org.openflexo.foundation.doc.FlexoDocObject;
import org.openflexo.foundation.doc.FlexoDocTable;
import org.openflexo.foundation.doc.FlexoDocument;
import org.openflexo.foundation.task.FlexoTask;
import org.openflexo.gina.view.widget.FIBCustomWidget;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXTable;

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
	protected void selectTableInDocumentEditor(final DocXTable table, FIBCustomWidget<?, ?, ?> documentEditorWidget) {

		// System.out.println("customPanel" + getCustomPanel());
		// System.out.println("docEditorWidget=" + getCustomPanel().getDocEditorWidget());

		final FlexoDocumentEditorWidget docXEditor = (FlexoDocumentEditorWidget) documentEditorWidget.getCustomComponent();

		try {

			if (table != null) {

				docXEditor.getEditor().highlight(table);
				scrollTo(table, docXEditor);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		docXEditor.getJEditorPane().revalidate();
		docXEditor.getJEditorPane().repaint();

	}

	private void scrollTo(FlexoDocObject object, FlexoDocumentEditorWidget docXEditor) {
		if (!docXEditor.getEditor().scrollTo(object, false)) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					scrollTo(object, docXEditor);
				}
			});
		}
	}

	public class LoadDocXEditor extends FlexoTask {

		private final DocXTable table;
		private TableSelectorDetailsPanel panel;

		public LoadDocXEditor(DocXTable table) {
			super(FlexoLocalization.getMainLocalizer().localizedForKey("opening_document_editor"));
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

		FIBCustomWidget<?, ?, ?> documentEditorWidget = returned.getDocEditorWidget();
		FlexoDocumentEditorWidget<?, ?> docXEditor = (FlexoDocumentEditorWidget<?, ?>) documentEditorWidget.getCustomComponent();
		docXEditor.getJEditorPane().addCaretListener(new FlexoDocumentEditorWidget.FlexoDocumentSelectionListener(docXEditor) {
			@Override
			public void caretUpdate(CaretEvent evt) {

				if (isSelecting) {
					return;
				}

				super.caretUpdate(evt);

				System.out.println("Caret changed with " + evt);
				int start = Math.min(evt.getDot(), evt.getMark());
				int end = Math.max(evt.getDot(), evt.getMark());
				System.out.println("Selection: " + start + ":" + end);

				// Better ???
				// int startLocation = getEditor().getJEditorPane().getSelectionStart();
				// int endLocation = getEditor().getJEditorPane().getSelectionEnd();

				// If selection is not empty, reduce the selection to be sure to be in a not implied run
				if (start > end) {
					end = end - 1;
				}

				FlexoStyledDocument<?, ?> styledDocument = docXEditor.getEditor().getStyledDocument();

				Element startCharElement = styledDocument.getCharacterElement(start);
				Element endCharElement = styledDocument.getCharacterElement(end);

				FlexoDocObject<?, ?> startDocObject = null;
				FlexoDocObject<?, ?> endDocObject = null;

				if (startCharElement instanceof AbstractDocumentElement
						&& ((AbstractDocumentElement<?, ?, ?>) startCharElement).getDocObject() instanceof FlexoDocElement) {
					startDocObject = ((AbstractDocumentElement<?, ?, ?>) startCharElement).getDocObject();
				}
				if (endCharElement instanceof AbstractDocumentElement
						&& ((AbstractDocumentElement<?, ?, ?>) endCharElement).getDocObject() instanceof FlexoDocElement) {
					endDocObject = ((AbstractDocumentElement<?, ?, ?>) endCharElement).getDocObject();
				}

				System.out.println("Pour l'element: " + startCharElement);
				System.out.println("Pour l'element par: " + styledDocument.getParagraphElement(start));
				System.out.println("On detecte " + startDocObject);

				if (startDocObject instanceof FlexoDocTable) {
					setSelectedObject(startDocObject);
				}

				/*
				isSelecting = true;
				setEditedObject(drawingRun);
				isSelecting = false;*/

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
