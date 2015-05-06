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

package org.openflexo.technologyadapter.docx.gui;

import static org.junit.Assert.assertNotNull;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JEditorPane;
import javax.swing.JPanel;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.provider.local.DefaultLocalFileProvider;
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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.OpenflexoTestCaseWithGUI;
import org.openflexo.fib.testutils.GraphicalContextDelegate;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentRepository;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

import net.sf.vfsjfilechooser.utils.VFSUtils;

/**
 * Test the structural and behavioural features of FIBOWLPropertySelector
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestDocX4allEditor extends OpenflexoTestCaseWithGUI {

	private static GraphicalContextDelegate gcDelegate;

	private static DocXDocument simpleDocument;
	private static DocXDocument structuredDocument;
	private static DocXDocument documentWithTable;
	private static DocXDocument documentWithImage;

	@BeforeClass
	public static void setupClass() {
		instanciateTestServiceManager();
		initGUI();
	}

	private DocXDocument getDocument(String documentName) {
		String documentURI = resourceCenter.getDefaultBaseURI() + "TestResourceCenter" + File.separator + documentName;
		System.out.println("Searching " + documentURI);

		FlexoResource<DocXDocument> documentResource = serviceManager.getInformationSpace().getResource(documentURI, null,
				DocXDocument.class);
		assertNotNull(documentResource);

		try {
			documentResource.loadResourceData(null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlexoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DocXDocument document = documentResource.getLoadedResourceData();
		assertNotNull(document);
		assertNotNull(document.getWordprocessingMLPackage());

		return document;
	}

	@Test
	@TestOrder(1)
	public void testInitRetrieveDocuments() {

		DocXTechnologyAdapter docXTA = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(DocXTechnologyAdapter.class);
		assertNotNull(docXTA);

		DocXDocumentRepository docXRepository = resourceCenter.getRepository(DocXDocumentRepository.class, docXTA);
		assertNotNull(docXRepository);

	}

	@Test
	@TestOrder(2)
	public void testOpenSimpleDocumentEditor() throws FileSystemException {
		simpleDocument = getDocument("SimpleDocument.docx");
		assertNotNull(simpleDocument);
		openDoxEditor(simpleDocument.getResource());
	}

	@Test
	@TestOrder(3)
	public void testOpenStructuredDocumentEditor() throws FileSystemException {
		structuredDocument = getDocument("StructuredDocument.docx");
		assertNotNull(structuredDocument);
		openDoxEditor(structuredDocument.getResource());
	}

	@Test
	@TestOrder(4)
	public void testOpenDocumentWithTableEditor() throws FileSystemException {
		documentWithTable = getDocument("DocumentWithTable.docx");
		assertNotNull(documentWithTable);
		openDoxEditor(documentWithTable.getResource());
	}

	@Test
	@TestOrder(5)
	public void testOpenDocumentWithImageEditor() throws FileSystemException {
		documentWithImage = getDocument("DocumentWithImage.docx");
		assertNotNull(documentWithImage);
		openDoxEditor(documentWithImage.getResource());
	}

	public static void initGUI() {
		gcDelegate = new GraphicalContextDelegate(TestDocX4allEditor.class.getSimpleName());
	}

	@AfterClass
	public static void waitGUI() {
		gcDelegate.waitGUI();
	}

	@Before
	public void setUp() {
		gcDelegate.setUp();
	}

	@Override
	@After
	public void tearDown() throws Exception {
		gcDelegate.tearDown();
	}

	private void openDoxEditor(FlexoResource<DocXDocument> docResource) throws FileSystemException {

		DefaultLocalFileProvider p = new DefaultLocalFileProvider();
		File f = ((FileFlexoIODelegate) docResource.getFlexoIODelegate()).getFile();
		FileSystemManager fsManager = VFS.getManager();
		FileObject fo = fsManager.resolveFile(docResource.getURI());

		ToolBarStates _toolbarStates = new ToolBarStates();

		JPanel toolbar = FxScriptUIHelper.getInstance().createToolBar(_toolbarStates);

		JEditorPane editorView = createEditorView(fo, _toolbarStates);

		JPanel editorPanel = FxScriptUIHelper.getInstance().createEditorPanel(editorView);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(toolbar, BorderLayout.NORTH);
		panel.add(editorPanel, BorderLayout.CENTER);

		gcDelegate.addTab(f.getName(), panel);

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
