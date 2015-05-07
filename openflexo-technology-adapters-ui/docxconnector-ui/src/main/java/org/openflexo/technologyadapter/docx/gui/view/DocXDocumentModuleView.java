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

package org.openflexo.technologyadapter.docx.gui.view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;
import org.docx4all.datatransfer.TransferHandler;
import org.docx4all.script.FxScriptUIHelper;
import org.docx4all.swing.WordMLTextPane;
import org.docx4all.swing.text.WordMLDocument;
import org.docx4all.swing.text.WordMLDocumentFilter;
import org.docx4all.swing.text.WordMLEditorKit;
import org.docx4all.ui.main.Constants;
import org.docx4all.ui.main.ToolBarStates;
import org.docx4all.util.DocUtil;
import org.jdesktop.application.ResourceMap;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.technologyadapter.docx.controller.DocXAdapterController;
import org.openflexo.technologyadapter.docx.gui.widget.FIBDocXDocumentBrowser;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterControllerService;
import org.openflexo.view.controller.model.FlexoPerspective;

import net.sf.vfsjfilechooser.utils.VFSUtils;

@SuppressWarnings("serial")
public class DocXDocumentModuleView extends JPanel implements ModuleView<DocXDocument>, PropertyChangeListener {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DocXDocumentModuleView.class.getPackage().getName());

	private final FlexoPerspective perspective;
	private final DocXDocument document;

	private JPanel docxEditor;
	// private final JPanel bottomPanel;

	private final FIBDocXDocumentBrowser browser;

	public DocXDocumentModuleView(DocXDocument document, FlexoPerspective perspective) {
		super();
		setLayout(new BorderLayout());
		this.document = document;
		this.perspective = perspective;

		try {
			docxEditor = createDocxEditor(document.getResource());
			add(docxEditor, BorderLayout.CENTER);

		} catch (FileSystemException e) {
			add(new JLabel("Cannot load: " + e.getMessage()), BorderLayout.CENTER);
			e.printStackTrace();
		}

		browser = new FIBDocXDocumentBrowser(document, perspective.getController());

		// bottomPanel = new JPanel(new BorderLayout());
		// bottomPanel.add(perspective.getController().makeInfoLabel(), BorderLayout.CENTER);
		// add(bottomPanel, BorderLayout.SOUTH);

		add(browser, BorderLayout.EAST);

		// perspective.getController().setInfoMessage(document.getResource().getURI(), false);

		validate();

		getRepresentedObject().getPropertyChangeSupport().addPropertyChangeListener(getRepresentedObject().getDeletedProperty(), this);
	}

	@Override
	public FlexoPerspective getPerspective() {
		return perspective;
	}

	@Override
	public void deleteModuleView() {
		getRepresentedObject().getPropertyChangeSupport().removePropertyChangeListener(getRepresentedObject().getDeletedProperty(), this);
		perspective.getController().removeModuleView(this);
		// TODO: delete docx editor
	}

	@Override
	public DocXDocument getRepresentedObject() {
		return document;
	}

	@Override
	public boolean isAutoscrolled() {
		return true;
	}

	@Override
	public void willHide() {

	}

	@Override
	public void willShow() {

		getPerspective().focusOnObject(getRepresentedObject());

	}

	public DocXAdapterController getDocXTechnologyAdapterController(FlexoController controller) {
		TechnologyAdapterControllerService tacService = controller.getApplicationContext().getTechnologyAdapterControllerService();
		return tacService.getTechnologyAdapterController(DocXAdapterController.class);
	}

	@Override
	public void show(final FlexoController controller, FlexoPerspective perspective) {

		// perspective.setTopRightView(browser);
		// controller.getControllerModel().setRightViewVisible(true);

		// Sets palette view of editor to be the top right view
		// perspective.setTopRightView(getEditor().getPaletteView());
		// perspective.setHeader(((FreeDiagramModuleView) moduleView).getEditor().getS());

		/*SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Force right view to be visible
				controller.getControllerModel().setRightViewVisible(true);
			}
		});
		
		controller.getControllerModel().setRightViewVisible(true);*/
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == getRepresentedObject() && evt.getPropertyName().equals(getRepresentedObject().getDeletedProperty())) {
			deleteModuleView();
		}
	}

	private JPanel createDocxEditor(FlexoResource<DocXDocument> docResource) throws FileSystemException {

		FileObject fo = VFS.getManager().resolveFile(docResource.getURI());

		ToolBarStates _toolbarStates = new ToolBarStates();

		JPanel toolbar = FxScriptUIHelper.getInstance().createToolBar(_toolbarStates);

		JEditorPane editorView = createEditorView(fo, _toolbarStates);

		JPanel editorPanel = FxScriptUIHelper.getInstance().createEditorPanel(editorView);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(toolbar, BorderLayout.NORTH);
		panel.add(editorPanel, BorderLayout.CENTER);

		return panel;

	}

	private JEditorPane createEditorView(FileObject f, ToolBarStates _toolbarStates) {

		// Clipboard clipboard = getContext().getClipboard();
		// clipboard.addFlavorListener(_toolbarStates);
		// As a FlavorListener, _toolbarStates will ONLY be notified
		// when there is a DataFlavor change in Clipboard.
		// Therefore, make sure that toolbarStates' _isPasteEnable property
		// is initialised correctly.
		/*	boolean available = clipboard.isDataFlavorAvailable(WordMLTransferable.STRING_FLAVOR)
					|| clipboard.isDataFlavorAvailable(WordMLTransferable.WORDML_FRAGMENT_FLAVOR);
			_toolbarStates.setPasteEnabled(available);*/

		String fileUri = f.getName().getURI();

		WordMLTextPane editorView = new WordMLTextPane();
		editorView.addFocusListener(_toolbarStates);
		editorView.addCaretListener(_toolbarStates);
		editorView.setTransferHandler(new TransferHandler());

		WordMLEditorKit editorKit = (WordMLEditorKit) editorView.getEditorKit();
		editorKit.addInputAttributeListener(_toolbarStates);

		WordMLDocument doc = null;

		try {
			if (f.exists()) {
				doc = editorKit.read(f);
			}
		} catch (Exception exc) {
			exc.printStackTrace();

			ResourceMap rm = null; // getContext().getResourceMap();
			String title = rm.getString(Constants.INIT_EDITOR_VIEW_IO_ERROR_DIALOG_TITLE);
			StringBuffer msg = new StringBuffer();
			msg.append(rm.getString(Constants.INIT_EDITOR_VIEW_IO_ERROR_MESSAGE));
			msg.append(Constants.NEWLINE);
			msg.append(VFSUtils.getFriendlyName(fileUri));
			// showMessageDialog(title, msg.toString(), JOptionPane.ERROR_MESSAGE);
			doc = null;
		}

		if (doc == null) {
			doc = (WordMLDocument) editorKit.createDefaultDocument();
		}

		doc.putProperty(WordMLDocument.FILE_PATH_PROPERTY, fileUri);
		doc.addDocumentListener(_toolbarStates);
		doc.setDocumentFilter(new WordMLDocumentFilter());
		editorView.setDocument(doc);
		editorView.putClientProperty(Constants.LOCAL_VIEWS_SYNCHRONIZED_FLAG, Boolean.TRUE);

		if (DocUtil.isSharedDocument(doc)) {
			editorKit.initPlutextClient(editorView);
		}

		return editorView;
	}

}
