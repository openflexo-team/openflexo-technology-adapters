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
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Element;
import javax.xml.bind.JAXBElement;

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
import org.docx4all.xml.ElementML;
import org.docx4all.xml.IObjectFactory;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Text;
import org.openflexo.foundation.doc.FlexoDocFragment.FragmentConsistencyException;
import org.openflexo.foundation.doc.TextSelection;
import org.openflexo.foundation.doc.TextSelection.TextMarker;
import org.openflexo.foundation.task.Progress;
import org.openflexo.gina.controller.FIBController;
import org.openflexo.gina.model.widget.FIBCustom;
import org.openflexo.gina.model.widget.FIBCustom.FIBCustomComponent.CustomComponentParameter;
import org.openflexo.swing.CustomPopup.ApplyCancelListener;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXParagraph;
import org.openflexo.technologyadapter.docx.model.DocXRun;
import org.openflexo.technologyadapter.docx.model.DocXTable;
import org.openflexo.toolbox.ToolBox;

@SuppressWarnings("serial")
public abstract class AbstractDocXEditor extends JPanel {

	private static final Logger logger = Logger.getLogger(AbstractDocXEditor.class.getPackage().getName());

	protected DocXDocument document;
	private ToolBarStates _toolbarStates = new ToolBarStates();
	private final JPanel toolbar = null;
	protected WordMLTextPane editorView;
	private final List<ApplyCancelListener> applyCancelListener = new ArrayList<ApplyCancelListener>();

	protected FIBCustom component;
	protected FIBController controller;

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

	public AbstractDocXEditor(DocXDocument document) {
		this(document, false);
	}

	public AbstractDocXEditor(DocXDocument document, boolean showToolbar) {
		super(new BorderLayout());

		Progress.progress("init_docx_editor");
		this.document = document;
		if (showToolbar) {
			_toolbarStates = new ToolBarStates();
			Progress.progress("init_toolbar");
			// toolbar = FxScriptUIHelper.getInstance().createToolBar(_toolbarStates);
			// add(toolbar, BorderLayout.NORTH);
		}

		setDocXDocument(document);

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
		return getDocXDocument();
	}

	public ToolBarStates getToolbarStates() {
		return _toolbarStates;
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

	protected TextMarker retrieveTextMarker(int pos) {

		WordMLDocument doc = editorView.getDocument();

		TextMarker returned = new TextMarker();

		// System.out.println("pos=" + pos);
		Element characterElement = doc.getCharacterElement(pos);
		Element paragraphElement = doc.getParagraphElement(pos);
		// System.out.println("characterElement=" + characterElement);
		// System.out.println("paragraphElement=" + paragraphElement);
		if (characterElement instanceof DocumentElement) {
			ElementML elementML = ((DocumentElement) characterElement).getElementML();
			// System.out.println("elementML=" + elementML);
			Object docXObject = elementML.getDocxObject();
			// System.out.println("docXObject=" + docXObject);
			if (docXObject instanceof JAXBElement) {
				docXObject = ((JAXBElement) docXObject).getValue();
			}
			// System.out.println("startDocXObject=" + docXObject);
			if (docXObject instanceof P) {
				returned.documentElement = getDocXDocument().getParagraph((P) docXObject);
			} else if (docXObject instanceof Tbl) {
				returned.documentElement = getDocXDocument().getTable((Tbl) docXObject);
			} else if (docXObject instanceof Text) {
				// System.out.println("Text= " + docXObject);
				R run = (R) ((Text) docXObject).getParent();
				// System.out.println("run=" + run);
				P p = (P) run.getParent();
				returned.documentElement = getDocXDocument().getParagraph(p);

				DocXRun docXRun = ((DocXParagraph) returned.documentElement).getRun(run);
				int runIndex = docXRun.getIndex();
				// System.out.println("runIndex=" + runIndex);
				int characterIndex = pos - paragraphElement.getStartOffset();
				// System.out.println("characterIndex=" + characterIndex);

				returned.runIndex = runIndex;
				returned.characterIndex = characterIndex;
				if (characterIndex == 0) {
					returned.firstChar = true;
				}
				if (pos == paragraphElement.getEndOffset() - 1) {
					returned.lastChar = true;
				}
				if (runIndex == 0) {
					returned.firstRun = true;
				}
				if (runIndex == ((DocXParagraph) returned.documentElement).getRuns().size() - 1) {
					returned.lastRun = true;
				}
			}
			// System.out.println("returned.documentElement=" + returned.documentElement);
		}

		return returned;

	}

	protected WordMLTextPane createEditorView(DocXDocument document, ToolBarStates _toolbarStates, IObjectFactory objectFactory) {

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
		editorView.addCaretListener(new DocXEditorSelectionListener(this) {
			@Override
			public void caretUpdate(CaretEvent e) {
				super.caretUpdate(e);
				// System.out.println("caretUpdate dot=" + e.getDot() + " mark=" + e.getMark());
				try {
					TextMarker startMarker = retrieveTextMarker(editorView.getSelectionStart());
					TextMarker endMarker = retrieveTextMarker(editorView.getSelectionEnd() - 1);
					// System.out.println("startMarker=" + startMarker);
					// System.out.println("endMarker=" + endMarker);
					getEditor().textSelection = getEditor().getDocXDocument().getFactory().makeTextSelection(startMarker, endMarker);
				} catch (FragmentConsistencyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		WordMLEditorKit editorKit = (WordMLEditorKit) editorView.getEditorKit();
		editorKit.addInputAttributeListener(_toolbarStates);

		// final WordMLDocument doc = null;

		wordMLDocument = openDocument(editorKit);

		editorView.setTransferHandler(new TransferHandler(wordMLDocument));

		wordMLDocument.putProperty(WordMLDocument.FILE_PATH_PROPERTY, document.getResource().getURI());
		wordMLDocument.addDocumentListener(_toolbarStates);
		wordMLDocument.setDocumentFilter(new WordMLDocumentFilter());
		editorView.setDocument(wordMLDocument);
		editorView.putClientProperty(Constants.LOCAL_VIEWS_SYNCHRONIZED_FLAG, Boolean.TRUE);

		if (DocUtil.isSharedDocument(wordMLDocument)) {
			editorKit.initPlutextClient(editorView);
		}

		return editorView;
	}

	private WordMLDocument wordMLDocument;

	protected WordMLDocument openDocument(WordMLEditorKit editorKit) {
		return editorKit.openDocument(document.getWordprocessingMLPackage(), objectFactory);
	}

	public WordMLDocument getWordMLDocument() {
		return wordMLDocument;
	}

	public DocXDocument getDocXDocument() {
		return document;
	}

	public void setDocXDocument(DocXDocument document) {
		this.document = document;
		// Avoid to recreate editorView all the time
		if (document != null && (editorView == null || editorView.getDocument() != document)) {
			editorView = createEditorView(document, _toolbarStates, getObjectFactory());
			JPanel editorPanel = FxScriptUIHelper.getInstance().createEditorPanel(editorView);
			add(editorPanel, BorderLayout.CENTER);
		}
	}

	public void addApplyCancelListener(ApplyCancelListener l) {
		applyCancelListener.add(l);
	}

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

	public void init(FIBCustom component, FIBController controller) {
		this.component = component;
		this.controller = controller;
	}

	public void delete() {
		// TODO
	}

	private TextSelection<DocXDocument, DocXTechnologyAdapter> textSelection;

	public TextSelection<DocXDocument, DocXTechnologyAdapter> getTextSelection() {
		return textSelection;
	}

	/*public void setTextSelection(TextSelection<DocXDocument,DocXTechnologyAdapter> textSelection) {
		this.textSelection = textSelection;
	}*/

	private TextSelection<DocXDocument, DocXTechnologyAdapter> highlightedTextSelection;

	public TextSelection<DocXDocument, DocXTechnologyAdapter> getHighlightedTextSelection() {
		return highlightedTextSelection;
	}

	@CustomComponentParameter(name = "highlightedTextSelection", type = CustomComponentParameter.Type.OPTIONAL)
	public void setHighlightedTextSelection(TextSelection<DocXDocument, DocXTechnologyAdapter> textSelection) {
		if ((textSelection == null && this.highlightedTextSelection != null)
				|| (textSelection != null && !textSelection.equals(this.highlightedTextSelection))) {
			this.highlightedTextSelection = textSelection;
			System.out.println("On change pour la selection " + textSelection);

			WordMLDocument wordMLDocument = editorView.getDocument();

			Object startDocXObject = null;
			Object endDocXObject = null;

			if (textSelection == null) {
				return;
			}

			if (textSelection.getStartElement() instanceof DocXParagraph) {
				if (textSelection.getStartRunIndex() > -1) {
					startDocXObject = ((DocXRun) ((DocXParagraph) textSelection.getStartElement()).getRuns().get(
							textSelection.getStartRunIndex())).getR();
				} else {
					startDocXObject = ((DocXParagraph) textSelection.getStartElement()).getP();
				}
			} else if (textSelection.getStartElement() instanceof DocXTable) {
				startDocXObject = ((DocXTable) textSelection.getStartElement()).getTbl();
			}

			if (textSelection.getEndElement() instanceof DocXParagraph) {
				if (textSelection.getEndRunIndex() > -1) {
					endDocXObject = ((DocXRun) ((DocXParagraph) textSelection.getEndElement()).getRuns()
							.get(textSelection.getEndRunIndex())).getR();
				} else {
					endDocXObject = ((DocXParagraph) textSelection.getEndElement()).getP();
				}
			} else if (textSelection.getEndElement() instanceof DocXTable) {
				endDocXObject = ((DocXTable) textSelection.getEndElement()).getTbl();
			}

			System.out.println("startDocXObject=" + startDocXObject);
			System.out.println("endDocXObject=" + endDocXObject);

			if (startDocXObject == null || endDocXObject == null) {
				System.out.println("cannot proceed");
				return;
			}

			DocumentElement startElement = wordMLDocument.getElement(startDocXObject);
			DocumentElement endElement = wordMLDocument.getElement(endDocXObject);

			if (startElement == null || endElement == null) {
				System.out.println("cannot proceed");
				return;
			}

			System.out.println("startElement=" + startElement);
			System.out.println("endElement=" + endElement);

			System.out.println("startOffset=" + startElement.getStartOffset() + "-" + startElement.getEndOffset());
			System.out.println("endOffset=" + endElement.getStartOffset() + "-" + endElement.getEndOffset());

			int startOffset = startElement.getStartOffset()
					+ (textSelection.getStartCharacterIndex() > -1 ? textSelection.getStartCharacterIndex() : 0);
			int endOffset = endElement.getEndOffset()
					- (textSelection.getEndCharacterIndex() > -1 ? textSelection.getEndCharacterIndex() : 0);

			editorView.requestFocus();
			editorView.select(startOffset, endOffset);
			scrollTo(startElement);

			// editorView.selectAll();

			// editorView.modelToView(pos)

		}
	}

	protected void scrollTo(final DocumentElement startElement) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// System.out.println("Hop, on scrolle a " + startElement);
				if (!getEditorView().scrollToElement(startElement, false)) {
					scrollTo(startElement);
				}
			}
		});
	}

	public static class DocXEditorSelectionListener implements CaretListener {

		private final AbstractDocXEditor editor;

		public DocXEditorSelectionListener(AbstractDocXEditor editor) {
			this.editor = editor;
		}

		public AbstractDocXEditor getEditor() {
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
