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
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.event.CaretEvent;

import org.docx4all.swing.text.DocumentElement;
import org.docx4j.wml.P;
import org.openflexo.components.widget.FIBDocumentFragmentSelector;
import org.openflexo.fib.view.widget.FIBCustomWidget;
import org.openflexo.foundation.doc.FlexoDocument;
import org.openflexo.foundation.doc.FlexoDocumentElement;
import org.openflexo.foundation.doc.FlexoDocumentFragment;
import org.openflexo.foundation.doc.FlexoDocumentFragment.FragmentConsistencyException;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.gui.widget.DocXEditor.DocXEditorSelectionListener;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXElement;
import org.openflexo.technologyadapter.docx.model.DocXFragment;
import org.openflexo.technologyadapter.docx.model.DocXParagraph;

/**
 * Widget allowing to select an {@link FlexoDocumentFragment} inside a {@link FlexoDocument}<br>
 * 
 * @author sguerin
 * 
 */
@SuppressWarnings("serial")
public class FIBDocXFragmentSelector extends FIBDocumentFragmentSelector<DocXFragment, DocXDocument, DocXTechnologyAdapter> {
	static final Logger logger = Logger.getLogger(FIBDocXFragmentSelector.class.getPackage().getName());

	public static final Resource FIB_FILE = ResourceLocator.locateResource("Fib/Widget/FIBDocXFragmentSelector.fib");

	public FIBDocXFragmentSelector(DocXFragment editedObject) {
		super(editedObject);
	}

	@Override
	public Resource getFIBResource() {
		return FIB_FILE;
	}

	@Override
	public Class<DocXFragment> getRepresentedType() {
		return DocXFragment.class;
	}

	@Override
	protected void selectFragmentInDocumentEditor(DocXFragment fragment, FIBCustomWidget<?, ?> documentEditorWidget) {
		super.selectFragmentInDocumentEditor(fragment, documentEditorWidget);

		// System.out.println("customPanel" + getCustomPanel());
		// System.out.println("docEditorWidget=" + getCustomPanel().getDocEditorWidget());

		DocXEditor docXEditor = (DocXEditor) documentEditorWidget.getCustomComponent();

		if (fragment == null) {
			docXEditor.getMLDocument().setSelectedElements(Collections.EMPTY_LIST);
			return;
		}

		try {

			List<DocXElement> fragmentElements = fragment.getElements();

			List<DocumentElement> elts = new ArrayList<DocumentElement>();

			for (FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> e : fragment.getElements()) {
				if (e instanceof DocXParagraph) {
					DocumentElement docElement = docXEditor.getMLDocument().getElement(((DocXParagraph) e).getP());
					elts.add(docElement);
				}
			}
			docXEditor.getMLDocument().setSelectedElements(elts);

			if (fragment.getStartElement() instanceof DocXParagraph) {
				DocumentElement startElement = docXEditor.getMLDocument().getElement(((DocXParagraph) fragment.getStartElement()).getP());
				if (startElement != null) {
					docXEditor.getEditorView().scrollToElement(startElement);
				}
			}

			docXEditor.getEditorView().repaint();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected FragmentSelectorDetailsPanel makeCustomPanel(DocXFragment editedObject) {
		FragmentSelectorDetailsPanel returned = super.makeCustomPanel(editedObject);
		FIBCustomWidget<?, ?> documentEditorWidget = returned.getDocEditorWidget();
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

				DocumentElement startParagraphMLElement = (DocumentElement) getEditor().getMLDocument().getParagraphMLElement(
						startLocation, false);
				DocumentElement endParagraphMLElement = (DocumentElement) getEditor().getMLDocument().getParagraphMLElement(endLocation,
						false);

				Object startDocXObject = startParagraphMLElement.getElementML().getDocxObject();
				Object endDocXObject = endParagraphMLElement.getElementML().getDocxObject();

				// System.out.println("start=" + startDocXObject + " of " + (startDocXObject != null ? startDocXObject.getClass() : null));
				// System.out.println("end=" + endDocXObject + " of " + (endDocXObject != null ? endDocXObject.getClass() : null));

				FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> startElement = null;
				FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> endElement = null;

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
		return returned;
	}

	private boolean isSelecting = false;
}
