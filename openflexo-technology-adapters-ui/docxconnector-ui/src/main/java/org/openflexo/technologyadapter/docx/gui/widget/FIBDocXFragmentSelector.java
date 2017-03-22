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
import org.openflexo.components.widget.FIBDocFragmentSelector;
import org.openflexo.foundation.doc.FlexoDocElement;
import org.openflexo.foundation.doc.FlexoDocFragment;
import org.openflexo.foundation.doc.FlexoDocFragment.FragmentConsistencyException;
import org.openflexo.foundation.doc.FlexoDocObject;
import org.openflexo.foundation.doc.FlexoDocument;
import org.openflexo.foundation.task.FlexoTask;
import org.openflexo.gina.view.widget.FIBCustomWidget;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXFragment;

/**
 * Widget allowing to select an {@link FlexoDocFragment} inside a {@link FlexoDocument}<br>
 * 
 * @author sguerin
 * 
 */
@SuppressWarnings("serial")
public class FIBDocXFragmentSelector extends FIBDocFragmentSelector<DocXFragment, DocXDocument, DocXTechnologyAdapter> {
	static final Logger logger = Logger.getLogger(FIBDocXFragmentSelector.class.getPackage().getName());

	public static final Resource FIB_FILE = ResourceLocator.locateResource("Fib/Widget/FIBFragmentSelector.fib");

	public FIBDocXFragmentSelector(DocXFragment editedObject) {
		super(editedObject);
		// fireEditedObjectChanged();
		// editedObject = null;
		// setSelectedObject(editedObject);
	}

	/*@Override
	public void updateCustomPanel(DocXFragment editedObject) {
		// logger.info("updateCustomPanel with " + editedObject + " _selectorPanel=" + _selectorPanel);
		setSelectedObject(editedObject);
		if (_selectorPanel != null) {
			_selectorPanel.update();
		}
	}*/

	/*@Override
	public void setEditedObject(DocXFragment object) {
		super.setEditedObject(object);
		fireEditedObjectChanged();
	}*/

	@Override
	public Resource getFIBResource() {
		return FIB_FILE;
	}

	@Override
	public Class<DocXFragment> getRepresentedType() {
		return DocXFragment.class;
	}

	@Override
	protected void selectFragmentInDocumentEditor(final DocXFragment fragment, FIBCustomWidget<?, ?, ?> documentEditorWidget) {
		super.selectFragmentInDocumentEditor(fragment, documentEditorWidget);

		System.out.println("****************** selectFragmentInDocumentEditor with " + fragment + " and " + documentEditorWidget);

		// System.out.println("customPanel" + getCustomPanel());
		// System.out.println("docEditorWidget=" + getCustomPanel().getDocEditorWidget());

		final FlexoDocumentEditorWidget docXEditor = (FlexoDocumentEditorWidget) documentEditorWidget.getCustomComponent();

		try {

			if (fragment != null) {
				docXEditor.getEditor().highlightObjects(fragment.getElements());
				scrollTo(fragment.getStartElement(), docXEditor);
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

		private final DocXFragment fragment;
		private FragmentSelectorDetailsPanel panel;

		public LoadDocXEditor(DocXFragment fragment) {
			super(FlexoLocalization.getMainLocalizer().localizedForKey("opening_document_editor"));
			this.fragment = fragment;
		}

		@Override
		public void performTask() throws InterruptedException {
			setExpectedProgressSteps(10);
			panel = FIBDocXFragmentSelector.super.makeCustomPanel(fragment);
		}

		public FragmentSelectorDetailsPanel getPanel() {
			return panel;
		}
	}

	@Override
	protected FragmentSelectorDetailsPanel makeCustomPanel(final DocXFragment editedObject) {

		FragmentSelectorDetailsPanel returned = null;

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
				Element startParElement = styledDocument.getParagraphElement(start);
				System.out.println("startCharElement: " + startCharElement);
				System.out.println("startParElement: " + startParElement);

				Element endCharElement = styledDocument.getCharacterElement(end);
				Element endParElement = styledDocument.getParagraphElement(end);
				System.out.println("endCharElement: " + endCharElement);
				System.out.println("endParElement: " + endParElement);

				FlexoDocElement startElement = null;
				FlexoDocElement endElement = null;

				if (startParElement instanceof AbstractDocumentElement
						&& ((AbstractDocumentElement<?, ?, ?>) startParElement).getDocObject() instanceof FlexoDocElement) {
					startElement = (FlexoDocElement<?, ?>) ((AbstractDocumentElement<?, ?, ?>) startParElement).getDocObject();
				}

				if (endParElement instanceof AbstractDocumentElement
						&& ((AbstractDocumentElement<?, ?, ?>) endParElement).getDocObject() instanceof FlexoDocElement) {
					endElement = (FlexoDocElement<?, ?>) ((AbstractDocumentElement<?, ?, ?>) endParElement).getDocObject();
				}

				DocXFragment newFragment = null;

				if (startElement != null && endElement != null) {

					try {
						newFragment = getDocument().getFragment(startElement, endElement);
					} catch (FragmentConsistencyException exception) {
						System.out.println("This fragment is not valid: start=" + startElement + " end=" + endElement);
						newFragment = null;
					}

				}

				System.out.println("fragment=" + newFragment);

				isSelecting = true;
				setEditedObject(newFragment);
				isSelecting = false;

			}
		});

		if (editedObject != null) {
			selectFragmentInDocumentEditor(editedObject, documentEditorWidget);
		}

		return returned;
	}

	private boolean isSelecting = false;

}
