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

import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.docx.AbstractTestDocX;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.gui.widget.DocXEditor;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentRepository;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test the structural and behavioural features of FIBOWLPropertySelector
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestDocX4allEditor extends AbstractTestDocX {

	private static DocXDocument simpleDocument;
	private static DocXDocument structuredDocument;
	private static DocXDocument documentWithTable;
	private static DocXDocument documentWithImage;
	private static DocXDocument exampleReport;

	/*private DocXDocument getDocument(String documentName) {
		String documentURI = resourceCenter.getDefaultBaseURI() + "TestResourceCenter/" + documentName;
	
		FlexoResource<DocXDocument> documentResource = serviceManager.getResourceManager().getResource(documentURI, null,
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
	}*/

	@Test
	@TestOrder(1)
	public void testInitRetrieveDocuments() {

		log("testInitRetrieveDocuments");

		DocXTechnologyAdapter docXTA = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(DocXTechnologyAdapter.class);
		assertNotNull(docXTA);

		DocXDocumentRepository docXRepository = resourceCenter.getRepository(DocXDocumentRepository.class, docXTA);
		assertNotNull(docXRepository);

	}

	@Test
	@TestOrder(2)
	public void testOpenSimpleDocumentEditor() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		log("testOpenSimpleDocumentEditor");
		simpleDocument = getDocument("SimpleDocument.docx");
		assertNotNull(simpleDocument);
		openDocXEditor(simpleDocument.getResource());
	}

	@Test
	@TestOrder(3)
	public void testOpenStructuredDocumentEditor() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		log("testOpenStructuredDocumentEditor");
		structuredDocument = getDocument("StructuredDocument.docx");
		assertNotNull(structuredDocument);
		openDocXEditor(structuredDocument.getResource());
	}

	@Test
	@TestOrder(4)
	public void testOpenDocumentWithTableEditor() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		log("testOpenDocumentWithTableEditor");
		documentWithTable = getDocument("DocumentWithTable.docx");
		assertNotNull(documentWithTable);
		openDocXEditor(documentWithTable.getResource());
	}

	@Test
	@TestOrder(5)
	public void testOpenDocumentWithImageEditor() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		log("testOpenDocumentWithImageEditor");
		documentWithImage = getDocument("DocumentWithImage.docx");
		assertNotNull(documentWithImage);
		openDocXEditor(documentWithImage.getResource());
	}

	@Test
	@TestOrder(6)
	public void testOpenExampleReportEditor() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		log("testOpenExampleReportEditor");
		exampleReport = getDocument("ExampleReport.docx");
		assertNotNull(exampleReport);
		openDocXEditor(exampleReport.getResource());
	}

	private void openDocXEditor(FlexoResource<DocXDocument> docResource) throws FileNotFoundException, ResourceLoadingCancelledException,
			FlexoException {

		/*DefaultLocalFileProvider p = new DefaultLocalFileProvider();
		File f = ((FileFlexoIODelegate) docResource.getFlexoIODelegate()).getFile();
		FileSystemManager fsManager = VFS.getManager();
		FileObject fo = fsManager.resolveFile(docResource.getURI());*/

		/*ToolBarStates _toolbarStates = new ToolBarStates();
		
		JPanel toolbar = FxScriptUIHelper.getInstance().createToolBar(_toolbarStates);
		
		DocXDocument document = docResource.getResourceData(null);
		
		JEditorPane editorView = createEditorView(document, _toolbarStates);
		
		JPanel editorPanel = FxScriptUIHelper.getInstance().createEditorPanel(editorView);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(toolbar, BorderLayout.NORTH);
		panel.add(editorPanel, BorderLayout.CENTER);*/

		DocXEditor editor = new DocXEditor(docResource.getResourceData(null), true);

		gcDelegate.addTab(docResource.getName(), editor);

	}

	/*private JEditorPane createEditorView(DocXDocument document, ToolBarStates _toolbarStates) {
	
		// Clipboard clipboard = getContext().getClipboard();
		// clipboard.addFlavorListener(_toolbarStates);
		// As a FlavorListener, _toolbarStates will ONLY be notified
		// when there is a DataFlavor change in Clipboard.
		// Therefore, make sure that toolbarStates' _isPasteEnable property
		// is initialised correctly.
	
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
	}*/

}
