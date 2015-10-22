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

import org.docx4all.swing.WordMLTextPane;
import org.docx4all.swing.text.DocumentElement;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.openflexo.components.widget.FIBDocImageSelector;
import org.openflexo.fib.swing.view.widget.FIBCustomWidget;
import org.openflexo.foundation.doc.FlexoDocument;
import org.openflexo.foundation.task.FlexoTask;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.gui.widget.AbstractDocXEditor.DocXEditorSelectionListener;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXDrawingRun;
import org.openflexo.technologyadapter.docx.model.DocXParagraph;

/**
 * Widget allowing to select an {@link DocXDrawingRun} inside a {@link FlexoDocument}<br>
 * 
 * @author sguerin
 * 
 */
@SuppressWarnings("serial")
public class FIBDocXImageSelector extends FIBDocImageSelector<DocXDrawingRun, DocXDocument, DocXTechnologyAdapter> {
	static final Logger logger = Logger.getLogger(FIBDocXImageSelector.class.getPackage().getName());

	public static final Resource FIB_FILE = ResourceLocator.locateResource("Fib/Widget/FIBDocXImageSelector.fib");

	public FIBDocXImageSelector(DocXDrawingRun editedObject) {
		super(editedObject);
	}

	@Override
	public Resource getFIBResource() {
		return FIB_FILE;
	}

	@Override
	public Class<DocXDrawingRun> getRepresentedType() {
		return DocXDrawingRun.class;
	}

	@Override
	protected void selectImageInDocumentEditor(final DocXDrawingRun drawingRun, FIBCustomWidget<?, ?> documentEditorWidget) {

		final DocXEditor docXEditor = (DocXEditor) documentEditorWidget.getCustomComponent();

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (drawingRun == null) {
					docXEditor.getMLDocument().setSelectedElements(Collections.EMPTY_LIST);
					docXEditor.getEditorView().repaint();
					return;
				}

				try {
					DocumentElement tableElement = docXEditor.getMLDocument().getElement(drawingRun.getR());
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

		private final DocXDrawingRun drawingRun;
		private ImageSelectorDetailsPanel panel;

		public LoadDocXEditor(DocXDrawingRun drawingRun) {
			super(FlexoLocalization.localizedForKey("opening_docx_editor"));
			this.drawingRun = drawingRun;
		}

		@Override
		public void performTask() throws InterruptedException {
			setExpectedProgressSteps(10);
			panel = FIBDocXImageSelector.super.makeCustomPanel(drawingRun);
		}

		public ImageSelectorDetailsPanel getPanel() {
			return panel;
		}
	}

	@Override
	protected ImageSelectorDetailsPanel makeCustomPanel(final DocXDrawingRun editedObject) {

		ImageSelectorDetailsPanel returned = null;

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
		DocXEditor docXEditor = (DocXEditor) documentEditorWidget.getCustomComponent();
		WordMLTextPane pane = docXEditor.getEditorView();
		docXEditor.getEditorView().addCaretListener(new DocXEditorSelectionListener(docXEditor) {
			@Override
			public void caretUpdate(CaretEvent e) {

				if (isSelecting) {
					return;
				}

				super.caretUpdate(e);

				int startLocation = getEditor().getEditorView().getSelectionStart();

				DocumentElement startMLElement = (DocumentElement) getEditor().getMLDocument().getCharacterElement(startLocation);
				Object startDocXObject = startMLElement.getElementML().getDocxObject();
				// System.out.println("startDocXObject=" + startDocXObject + " of " + startDocXObject.getClass().getSimpleName());
				DocXDrawingRun drawingRun = null;

				if (startDocXObject instanceof Drawing) {
					DocumentElement runMLElement = (DocumentElement) startMLElement.getParentElement();
					Object r = runMLElement.getElementML().getDocxObject();
					if (r instanceof R && ((R) r).getParent() instanceof P) {
						R drawingR = (R) r;
						P p = (P) ((R) r).getParent();
						DocXParagraph paragraph = getDocument().getParagraph(p);
						drawingRun = (DocXDrawingRun) paragraph.getRun(drawingR);
					}
				}

				isSelecting = true;
				setEditedObject(drawingRun);
				isSelecting = false;

			}
		});

		return returned;
	}

	@Override
	public void setSelectedObject(Object selectedObject) {
		if (selectedObject instanceof DocXDrawingRun) {
			super.setSelectedObject(selectedObject);
		}
		else {
			super.setSelectedObject(null);
		}
	}

	private boolean isSelecting = false;

}
