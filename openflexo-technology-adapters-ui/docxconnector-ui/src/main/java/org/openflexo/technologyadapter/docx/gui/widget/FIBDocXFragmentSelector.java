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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;

import org.docx4all.swing.text.DocumentElement;
import org.docx4j.wml.P;
import org.openflexo.components.widget.FIBDocFragmentSelector;
import org.openflexo.foundation.doc.FlexoDocElement;
import org.openflexo.foundation.doc.FlexoDocFragment;
import org.openflexo.foundation.doc.FlexoDocFragment.FragmentConsistencyException;
import org.openflexo.foundation.doc.FlexoDocument;
import org.openflexo.foundation.task.FlexoTask;
import org.openflexo.gina.view.widget.FIBCustomWidget;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.gui.widget.AbstractDocXEditor.DocXEditorSelectionListener;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXFragment;
import org.openflexo.technologyadapter.docx.model.DocXParagraph;
import org.openflexo.technologyadapter.docx.model.DocXTable;

/**
 * Widget allowing to select an {@link FlexoDocFragment} inside a {@link FlexoDocument}<br>
 * 
 * @author sguerin
 * 
 */
@SuppressWarnings("serial")
public class FIBDocXFragmentSelector extends FIBDocFragmentSelector<DocXFragment, DocXDocument, DocXTechnologyAdapter> {
	static final Logger logger = Logger.getLogger(FIBDocXFragmentSelector.class.getPackage().getName());

	public static final Resource FIB_FILE = ResourceLocator.locateResource("Fib/Widget/FIBDocXFragmentSelector.fib");

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

		final DocXEditor docXEditor = (DocXEditor) documentEditorWidget.getCustomComponent();

		try {

			// List<DocXElement> fragmentElements = fragment.getElements();

			final List<DocumentElement> elts = new ArrayList<DocumentElement>();

			if (fragment != null) {

				// System.out.println("start=" + fragment.getStartElement().getIndex());
				// System.out.println("end=" + fragment.getEndElement().getIndex());

				for (FlexoDocElement<DocXDocument, DocXTechnologyAdapter> e : fragment.getElements()) {
					if (e instanceof DocXParagraph) {
						DocumentElement docElement = docXEditor.getMLDocument().getElement(((DocXParagraph) e).getP());
						elts.add(docElement);
					}
					if (e instanceof DocXTable) {
						DocumentElement docElement = docXEditor.getMLDocument().getElement(((DocXTable) e).getTbl());
						elts.add(docElement);
					}
				}
			}

			// Thread.dumpStack();
			docXEditor.getMLDocument().setSelectedElements(elts);

			if (fragment != null) {

				if (fragment.getStartElement() instanceof DocXParagraph) {
					final DocumentElement startElement = docXEditor.getMLDocument()
							.getElement(((DocXParagraph) fragment.getStartElement()).getP());
					if (startElement != null) {
						scrollTo(startElement, docXEditor);
					}
				}
				if (fragment.getStartElement() instanceof DocXTable) {
					DocumentElement startElement = docXEditor.getMLDocument().getElement(((DocXTable) fragment.getStartElement()).getTbl());
					if (startElement != null) {
						docXEditor.getEditorView().scrollToElement(startElement, false);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		docXEditor.getEditorView().revalidate();
		docXEditor.getEditorView().repaint();

	}

	private void scrollTo(final DocumentElement startElement, final DocXEditor docXEditor) {
		if (!docXEditor.getEditorView().scrollToElement(startElement, false)) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					scrollTo(startElement, docXEditor);
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
		DocXEditor docXEditor = (DocXEditor) documentEditorWidget.getCustomComponent();
		docXEditor.getEditorView().addCaretListener(new DocXEditorSelectionListener(docXEditor) {
			@Override
			public void caretUpdate(CaretEvent e) {

				if (isSelecting) {
					return;
				}

				super.caretUpdate(e);

				int startLocation = getEditor().getEditorView().getSelectionStart();
				int endLocation = getEditor().getEditorView().getSelectionEnd();

				// If selection is not empty, reduce the selection to be sure to be in a not implied run
				if (endLocation > startLocation) {
					endLocation = endLocation - 1;
				}

				DocumentElement startParagraphMLElement = (DocumentElement) getEditor().getMLDocument().getParagraphMLElement(startLocation,
						false);
				DocumentElement endParagraphMLElement = (DocumentElement) getEditor().getMLDocument().getParagraphMLElement(endLocation,
						false);

				Object startDocXObject = startParagraphMLElement.getElementML().getDocxObject();
				Object endDocXObject = endParagraphMLElement.getElementML().getDocxObject();

				// System.out.println("start=" + startDocXObject + " of " + (startDocXObject != null ? startDocXObject.getClass() : null));
				// System.out.println("end=" + endDocXObject + " of " + (endDocXObject != null ? endDocXObject.getClass() : null));

				FlexoDocElement<DocXDocument, DocXTechnologyAdapter> startElement = null;
				FlexoDocElement<DocXDocument, DocXTechnologyAdapter> endElement = null;

				if (startDocXObject instanceof P) {
					startElement = getDocument().getParagraph((P) startDocXObject);
				}
				if (endDocXObject instanceof P) {
					endElement = getDocument().getParagraph((P) endDocXObject);
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

				// System.out.println("fragment=" + newFragment);

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
