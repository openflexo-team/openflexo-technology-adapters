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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JEditorPane;
import javax.swing.JPanel;

import org.docx4all.datatransfer.TransferHandler;
import org.docx4all.script.FxScriptUIHelper;
import org.docx4all.swing.WordMLTextPane;
import org.docx4all.swing.text.WordMLDocument;
import org.docx4all.swing.text.WordMLDocumentFilter;
import org.docx4all.swing.text.WordMLEditorKit;
import org.docx4all.ui.main.Constants;
import org.docx4all.ui.main.ToolBarStates;
import org.docx4all.util.DocUtil;
import org.openflexo.fib.controller.FIBController;
import org.openflexo.fib.model.FIBCustom;
import org.openflexo.fib.model.FIBCustom.FIBCustomComponent;
import org.openflexo.swing.CustomPopup.ApplyCancelListener;
import org.openflexo.technologyadapter.docx.model.DocXDocument;

@SuppressWarnings("serial")
public class DocXEditor extends JPanel implements FIBCustomComponent<DocXDocument, DocXEditor> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DocXEditor.class.getPackage().getName());

	private DocXDocument document;
	private ToolBarStates _toolbarStates = new ToolBarStates();
	private final JPanel toolbar;
	private JEditorPane editorView;
	private final List<ApplyCancelListener> applyCancelListener = new ArrayList<ApplyCancelListener>();

	private FIBCustom component;
	private FIBController controller;

	public DocXEditor(DocXDocument document) {
		super(new BorderLayout());
		this.document = document;
		_toolbarStates = new ToolBarStates();
		toolbar = FxScriptUIHelper.getInstance().createToolBar(_toolbarStates);
		add(toolbar, BorderLayout.NORTH);

		setEditedObject(document);

		revalidate();
	}

	private JEditorPane createEditorView(DocXDocument document, ToolBarStates _toolbarStates) {

		// Clipboard clipboard = getContext().getClipboard();
		// clipboard.addFlavorListener(_toolbarStates);
		// As a FlavorListener, _toolbarStates will ONLY be notified
		// when there is a DataFlavor change in Clipboard.
		// Therefore, make sure that toolbarStates' _isPasteEnable property
		// is initialised correctly.
		/*	boolean available = clipboard.isDataFlavorAvailable(WordMLTransferable.STRING_FLAVOR)
			|| clipboard.isDataFlavorAvailable(WordMLTransferable.WORDML_FRAGMENT_FLAVOR);
		_toolbarStates.setPasteEnabled(available);*/

		WordMLTextPane editorView = new WordMLTextPane();
		editorView.addFocusListener(_toolbarStates);
		editorView.addCaretListener(_toolbarStates);
		editorView.setTransferHandler(new TransferHandler());

		WordMLEditorKit editorKit = (WordMLEditorKit) editorView.getEditorKit();
		editorKit.addInputAttributeListener(_toolbarStates);

		WordMLDocument doc = null;

		try {
			doc = editorKit.openDocument(document.getWordprocessingMLPackage());

			doc.putProperty(WordMLDocument.FILE_PATH_PROPERTY, document.getResource().getURI());
			doc.addDocumentListener(_toolbarStates);
			doc.setDocumentFilter(new WordMLDocumentFilter());
			editorView.setDocument(doc);
			editorView.putClientProperty(Constants.LOCAL_VIEWS_SYNCHRONIZED_FLAG, Boolean.TRUE);

			if (DocUtil.isSharedDocument(doc)) {
				editorKit.initPlutextClient(editorView);
			}

		} catch (Exception exc) {
			exc.printStackTrace();
			doc = null;
		}

		return editorView;
	}

	@Override
	public DocXEditor getJComponent() {
		return this;
	}

	@Override
	public DocXDocument getEditedObject() {
		return document;
	}

	@Override
	public void setEditedObject(DocXDocument document) {
		this.document = document;
		if (document != null) {
			editorView = createEditorView(document, _toolbarStates);
			JPanel editorPanel = FxScriptUIHelper.getInstance().createEditorPanel(editorView);
			add(editorPanel, BorderLayout.CENTER);
		}
	}

	@Override
	public DocXDocument getRevertValue() {
		return document;
	}

	@Override
	public void setRevertValue(DocXDocument object) {
		// TODO Auto-generated method stub
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
	public Class<DocXDocument> getRepresentedType() {
		return DocXDocument.class;
	}

	@Override
	public void init(FIBCustom component, FIBController controller) {
		this.component = component;
		this.controller = controller;
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
	}

}
