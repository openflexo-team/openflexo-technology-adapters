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

package org.openflexo.technologyadapter.docx.gui.widget;

import java.awt.BorderLayout;
import java.util.logging.Logger;

import javax.swing.JPanel;

import org.docx4all.script.FxScriptUIHelper;
import org.docx4all.swing.text.WordMLDocumentFragment;
import org.docx4all.swing.text.WordMLEditorKit;
import org.openflexo.fib.model.FIBCustom.FIBCustomComponent;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXFragment;

@SuppressWarnings("serial")
public class DocXFragmentEditor extends AbstractDocXEditor implements FIBCustomComponent<DocXFragment, DocXFragmentEditor> {

	private static final Logger logger = Logger.getLogger(DocXFragmentEditor.class.getPackage().getName());

	// private long startIndex;
	// private long endIndex;

	private DocXFragment fragment;

	public DocXFragmentEditor(DocXFragment fragment) {
		this(fragment, false);
	}

	public DocXFragmentEditor(DocXFragment fragment, boolean showToolbar) {
		super(fragment != null ? fragment.getFlexoDocument() : null, showToolbar);
		setEditedObject(fragment);
	}

	public long getStartIndex() {
		if (getEditedObject() != null) {
			return getEditedObject().getStartElement().getIndex();
		}
		return -1;
	}

	/*@CustomComponentParameter(name = "startIndex", type = CustomComponentParameter.Type.OPTIONAL)
	public void setStartIndex(long startIndex) {
	
		if (startIndex != this.startIndex) {
			this.startIndex = startIndex;
			if (getWordMLDocument() != null) {
				getWordMLDocument().setStartIndex((int) startIndex);
			}
			editorView = null;
			installEditorView();
		}
	}*/

	public long getEndIndex() {
		if (getEditedObject() != null) {
			return getEditedObject().getEndElement().getIndex();
		}
		return -1;
	}

	/*@CustomComponentParameter(name = "endIndex", type = CustomComponentParameter.Type.OPTIONAL)
	public void setEndIndex(long endIndex) {
	
		if (endIndex != this.endIndex) {
			this.endIndex = endIndex;
			if (getWordMLDocument() != null) {
				getWordMLDocument().setEndIndex((int) endIndex);
			}
			editorView = null;
			installEditorView();
		}
	}*/

	@Override
	public WordMLDocumentFragment getWordMLDocument() {
		return (WordMLDocumentFragment) super.getWordMLDocument();
	}

	@CustomComponentParameter(name = "serviceManager", type = CustomComponentParameter.Type.OPTIONAL)
	@Override
	protected WordMLDocumentFragment openDocument(WordMLEditorKit editorKit) {

		return editorKit.openDocumentFragment(document.getWordprocessingMLPackage(), getObjectFactory(), (int) getStartIndex(),
				(int) getEndIndex());
	}

	@Override
	public DocXFragmentEditor getJComponent() {
		return this;
	}

	@Override
	public DocXFragment getEditedObject() {
		return fragment;
	}

	private JPanel editorPanel;

	@Override
	public void setDocXDocument(DocXDocument document) {

		this.document = document;
	}

	@Override
	public void setEditedObject(DocXFragment fragment) {
		if ((fragment == null && this.fragment != null) || (fragment != null && !fragment.equals(this.fragment))) {
			this.fragment = fragment;
			if (fragment != null) {
				setDocXDocument(fragment.getFlexoDocument());
			}

			if (editorPanel != null) {
				remove(editorPanel);
			}

			editorView = createEditorView(document, getToolbarStates(), getObjectFactory());
			editorPanel = FxScriptUIHelper.getInstance().createEditorPanel(editorView);
			add(editorPanel, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
	}

	private DocXFragment revertValue;

	@Override
	public DocXFragment getRevertValue() {
		return revertValue;
	}

	@Override
	public void setRevertValue(DocXFragment revertValue) {
		this.revertValue = revertValue;
	}

	@Override
	public Class<DocXFragment> getRepresentedType() {
		return DocXFragment.class;
	}

}
