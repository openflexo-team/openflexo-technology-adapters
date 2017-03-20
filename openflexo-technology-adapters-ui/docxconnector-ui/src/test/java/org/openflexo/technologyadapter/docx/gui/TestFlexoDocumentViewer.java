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

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.components.doc.editorkit.FlexoDocumentEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.gina.swing.editor.JFIBEditor.LayoutColumns;
import org.openflexo.swing.layout.JXMultiSplitPane;
import org.openflexo.swing.layout.MultiSplitLayout;
import org.openflexo.swing.layout.MultiSplitLayout.Leaf;
import org.openflexo.swing.layout.MultiSplitLayout.Node;
import org.openflexo.swing.layout.MultiSplitLayout.Split;
import org.openflexo.swing.layout.MultiSplitLayoutFactory;
import org.openflexo.technologyadapter.docx.AbstractTestDocX;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.gui.widget.FIBDocXDocumentBrowser;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentRepository;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test the structural and behavioural features of {@link FlexoDocumentEditor}
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestFlexoDocumentViewer extends AbstractTestDocX {

	private static DocXDocument simpleDocument;
	private static DocXDocument structuredDocument;
	private static DocXDocument documentWithTable;
	private static DocXDocument documentWithImage;
	private static DocXDocument exampleReport;

	@BeforeClass
	public static void setupClass() {
		instanciateTestServiceManager(DocXTechnologyAdapter.class);
		initGUI();
	}

	@Test
	@TestOrder(1)
	public void testInitRetrieveDocuments() {

		DocXTechnologyAdapter docXTA = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(DocXTechnologyAdapter.class);
		assertNotNull(docXTA);

		FlexoResourceCenter<?> resourceCenter = serviceManager.getResourceCenterService()
				.getFlexoResourceCenter("http://openflexo.org/docx-test");
		assertNotNull(resourceCenter);
		DocXDocumentRepository docXRepository = docXTA.getDocXDocumentRepository(resourceCenter);
		assertNotNull(docXRepository);

	}

	@Test
	@TestOrder(2)
	public void testOpenSimpleDocumentEditor() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		simpleDocument = getDocument("SimpleDocument.docx");
		assertNotNull(simpleDocument);
		openFlexoDocumentEditor(simpleDocument.getResource());
	}

	@Test
	@TestOrder(3)
	public void testOpenStructuredDocumentEditor() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		structuredDocument = getDocument("StructuredDocument.docx");
		assertNotNull(structuredDocument);
		openFlexoDocumentEditor(structuredDocument.getResource());
	}

	@Test
	@TestOrder(4)
	public void testOpenStructuredDocumentWithNumberingEditor()
			throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		structuredDocument = getDocument("StructuredDocumentWithNumbering.docx");
		assertNotNull(structuredDocument);
		openFlexoDocumentEditor(structuredDocument.getResource());
	}

	@Test
	@TestOrder(5)
	public void testOpenDocumentWithTableEditor() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		documentWithTable = getDocument("DocumentWithTable.docx");
		assertNotNull(documentWithTable);
		openFlexoDocumentEditor(documentWithTable.getResource());
	}

	@Test
	@TestOrder(6)
	public void testOpenDocumentWithImageEditor() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		documentWithImage = getDocument("DocumentWithImage.docx");
		assertNotNull(documentWithImage);
		openFlexoDocumentEditor(documentWithImage.getResource());
	}

	@Test
	@TestOrder(7)
	public void testOpenExampleReportEditor() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		exampleReport = getDocument("ExampleReport.docx");
		assertNotNull(exampleReport);
		openFlexoDocumentEditor(exampleReport.getResource());
	}

	private static final MultiSplitLayoutFactory MSL_FACTORY = new MultiSplitLayoutFactory.DefaultMultiSplitLayoutFactory();

	public static enum LayoutPosition {
		LEFT, CENTER, RIGHT;
	}

	private static Split getDefaultLayout() {
		Split root = MSL_FACTORY.makeSplit();
		root.setName("ROOT");
		Leaf left = MSL_FACTORY.makeLeaf(LayoutPosition.LEFT.name());
		left.setWeight(0.2);
		Node center = MSL_FACTORY.makeLeaf(LayoutPosition.CENTER.name());
		center.setWeight(0.6);
		center.setName(LayoutColumns.CENTER.name());
		Leaf right = MSL_FACTORY.makeLeaf(LayoutPosition.RIGHT.name());
		right.setWeight(0.2);
		right.setName(LayoutColumns.RIGHT.name());
		root.setChildren(left, MSL_FACTORY.makeDivider(), center, MSL_FACTORY.makeDivider(), right);
		return root;
	}

	private void openFlexoDocumentEditor(FlexoResource<DocXDocument> docResource)
			throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		DocXDocument doc = docResource.getResourceData(null);

		Split defaultLayout = getDefaultLayout();

		MultiSplitLayout centerLayout = new MultiSplitLayout(true, MSL_FACTORY);
		centerLayout.setLayoutMode(MultiSplitLayout.NO_MIN_SIZE_LAYOUT);
		centerLayout.setModel(defaultLayout);

		JXMultiSplitPane pane = new JXMultiSplitPane(centerLayout);
		pane.setDividerSize(8);

		FIBDocXDocumentBrowser docBrowser = new FIBDocXDocumentBrowser(doc, serviceManager.getApplicationFIBLibraryService()) {
			@Override
			public void singleClick(Object object) {
				System.out.println("Je viens cliquer sur " + object);
			}
		};
		docBrowser.setShowRuns(true);
		FlexoDocumentEditor<DocXDocument, DocXTechnologyAdapter> editor = new FlexoDocumentEditor<>(doc);

		final JTree tree = new JTree((TreeNode) editor.getStyledDocument().getDefaultRootElement());

		pane.add(docBrowser, LayoutPosition.LEFT.name());
		pane.add(editor.getEditorPanel(), LayoutPosition.CENTER.name());
		pane.add(new JScrollPane(tree), LayoutPosition.RIGHT.name());

		pane.revalidate();

		gcDelegate.addTab(docResource.getName(), pane);

		editor.getStyledDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				System.out.println("Hop ca change");
				((DefaultTreeModel) tree.getModel()).reload();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				System.out.println("Hop ca change");
				((DefaultTreeModel) tree.getModel()).reload();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				System.out.println("Hop ca change");
				((DefaultTreeModel) tree.getModel()).reload();
			}
		});

	}
}
