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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Element;

import org.docx4all.datatransfer.TransferHandler;
import org.docx4all.script.FxScriptUIHelper;
import org.docx4all.swing.WordMLTextPane;
import org.docx4all.swing.text.DocumentElement;
import org.docx4all.swing.text.WordMLDocument;
import org.docx4all.swing.text.WordMLDocumentFilter;
import org.docx4all.swing.text.WordMLEditorKit;
import org.docx4all.ui.main.Constants;
import org.docx4all.ui.main.ToolBarStates;
import org.docx4all.util.DocUtil;
import org.docx4all.xml.IObjectFactory;
import org.docx4j.fonts.PhysicalFonts;
import org.openflexo.fib.controller.FIBController;
import org.openflexo.fib.model.FIBCustom;
import org.openflexo.fib.model.FIBCustom.FIBCustomComponent;
import org.openflexo.foundation.task.Progress;
import org.openflexo.swing.CustomPopup.ApplyCancelListener;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.toolbox.ToolBox;

@SuppressWarnings("serial")
public class DocXEditor extends JPanel implements FIBCustomComponent<DocXDocument, DocXEditor> {

	private static final Logger logger = Logger.getLogger(DocXEditor.class.getPackage().getName());

	private DocXDocument document;
	private ToolBarStates _toolbarStates = new ToolBarStates();
	private JPanel toolbar = null;
	private WordMLTextPane editorView;
	private final List<ApplyCancelListener> applyCancelListener = new ArrayList<ApplyCancelListener>();

	private FIBCustom component;
	private FIBController controller;

	private IObjectFactory objectFactory;

	// Font regex (optional)
	// Set regex if you want to restrict to some defined subset of fonts
	// Here we have to do this before calling createContent,
	// since that discovers fonts
	static {
		String regex = null;
		if (ToolBox.isMacOS()) {
			regex = ".*(Courier New|Arial|Times New Roman|Comic Sans|Georgia|Impact|Lucida Console|Lucida Sans Unicode|Palatino Linotype|Tahoma|Trebuchet|Verdana|Symbol|Webdings|Wingdings|MS Sans Serif|MS Serif).*";
		} else {
			regex = ".*(calibri|cour|arial|times|comic|georgia|impact|LSANS|pala|tahoma|trebuc|verdana|symbol|webdings|wingding).*";
		}
		PhysicalFonts.setRegex(regex);
	}

	public DocXEditor(DocXDocument document) {
		this(document, false);
	}

	public DocXEditor(DocXDocument document, boolean showToolbar) {
		super(new BorderLayout());

		Thread.dumpStack();

		Progress.progress("init_docx_editor");
		this.document = document;
		if (showToolbar) {
			_toolbarStates = new ToolBarStates();
			Progress.progress("init_toolbar");
			toolbar = FxScriptUIHelper.getInstance().createToolBar(_toolbarStates);
			add(toolbar, BorderLayout.NORTH);
		}

		setEditedObject(document);

		Progress.progress("doc_x_editor_finalization");
		revalidate();
	}

	public WordMLTextPane getEditorView() {
		return editorView;
	}

	public WordMLDocument getMLDocument() {
		if (getEditorView() != null) {
			return getEditorView().getDocument();
		}
		return null;
	}

	public DocXDocument getDocument() {
		return getEditedObject();
	}

	/**
	 * Return {@link IObjectFactory} used to edit this document, create default one when non existant
	 * 
	 * @return
	 */
	public IObjectFactory getObjectFactory() {
		if (objectFactory == null) {
			return new DocXEditorObjectFactory(getDocument());
		}
		return objectFactory;
	}

	/**
	 * Sets {@link IObjectFactory} used to edit this document
	 * 
	 * @param objectFactory
	 */
	public void setObjectFactory(IObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}

	private WordMLTextPane createEditorView(DocXDocument document, ToolBarStates _toolbarStates, IObjectFactory objectFactory) {

		// Clipboard clipboard = getContext().getClipboard();
		// clipboard.addFlavorListener(_toolbarStates);
		// As a FlavorListener, _toolbarStates will ONLY be notified
		// when there is a DataFlavor change in Clipboard.
		// Therefore, make sure that toolbarStates' _isPasteEnable property
		// is initialised correctly.
		/*	boolean available = clipboard.isDataFlavorAvailable(WordMLTransferable.STRING_FLAVOR)
			|| clipboard.isDataFlavorAvailable(WordMLTransferable.WORDML_FRAGMENT_FLAVOR);
		_toolbarStates.setPasteEnabled(available);*/

		editorView = new WordMLTextPane();
		editorView.addFocusListener(_toolbarStates);
		editorView.addCaretListener(_toolbarStates);

		WordMLEditorKit editorKit = (WordMLEditorKit) editorView.getEditorKit();
		editorKit.addInputAttributeListener(_toolbarStates);

		// final WordMLDocument doc = null;

		try {
			WordMLDocument doc = editorKit.openDocument(document.getWordprocessingMLPackage(), objectFactory);

			editorView.setTransferHandler(new TransferHandler(doc));

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
			// doc = null;
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
			editorView = createEditorView(document, _toolbarStates, getObjectFactory());
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

	public static class DocXEditorSelectionListener implements CaretListener {

		private final DocXEditor editor;

		public DocXEditorSelectionListener(DocXEditor editor) {
			this.editor = editor;
		}

		public DocXEditor getEditor() {
			return editor;
		}

		@Override
		public void caretUpdate(CaretEvent e) {
			// System.out.println("caretUpdate dot=" + e.getDot() + " mark=" + e.getMark());
		}
	}

	public class DeprecatedDocumentSelectionListener extends MouseAdapter {

		private final WordMLDocument doc;

		// private LabelView selectedView = null;

		public DeprecatedDocumentSelectionListener(WordMLDocument doc) {
			this.doc = doc;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			// System.out.println("selection start = " + editorView.getSelectionStart());
			// System.out.println("selection end = " + editorView.getSelectionEnd());
			int pos = editorView.getSelectionStart();
			// System.out.println("pos=" + pos);
			// System.out.println("paragraphML=" + doc.getParagraphElement(pos));
			// System.out.println("runML=" + doc.getRunMLElement(pos));
			// System.out.println("characterElement=" + doc.getCharacterElement(pos));
			// ViewFactory viewFactory = (ViewFactory) editorView.getEditorKit().getViewFactory();
			Element characterElement = doc.getCharacterElement(pos);
			if (characterElement instanceof DocumentElement) {
				doc.setSelectedElements((DocumentElement) characterElement);

				/*if (selectedElement != null) {
					selectedElement.setSelected(false);
				}
				selectedElement = (DocumentElement) characterElement;

				System.out.println("Paf, on selectionne " + selectedElement);
				selectedElement.setSelected(true);*/

				/*ElementML elementML = selectedElement.getElementML();
				View v = viewFactory.getViewForElementML(elementML);
				if (v instanceof LabelView) {
					((LabelView) v).setPropertiesFromAttributes();
				}*/

				// editorView.revalidate();
				editorView.repaint();
			}

			/*if (characterElement instanceof TextElement) {
				System.out.println("Il s'agit bien de texte");
				ElementML elementML = ((TextElement) characterElement).getElementML();
				System.out.println("elementML=" + elementML);
				View v = viewFactory.getViewForElementML(elementML);
				System.out.println("View = " + v);
				System.out.println("parent = " + v.getParent());
				if (v instanceof LabelView) {
					LabelView labelView = (LabelView) v;
					if (selectedView != null) {
						selectedView.setSelected(false);
					}
					labelView.setSelected(true);
					selectedView = labelView;
					editorView.repaint();
					// labelView.modelToView(pos, a)
					Shape s;
					try {
						s = labelView.modelToView(pos, getBounds(), Position.Bias.Forward);
						System.out.println("Shape = " + s);
						s = labelView.getParent().modelToView(pos, getBounds(), Position.Bias.Forward);
						System.out.println("Parent Shape = " + s);
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				Object docxobject = elementML.getDocxObject();
				System.out.println("docxobject=" + docxobject);
				if (docxobject instanceof JAXBElement) {
					Object obj = ((JAXBElement) docxobject).getValue();
					System.out.println("docxobject=" + obj);
					if (obj instanceof Text) {
						System.out.println("Texte=" + ((Text) obj).getValue());
					}
				}

			}*/

		}

	}
}
