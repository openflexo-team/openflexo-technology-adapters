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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.components.doc.editorkit.FlexoDocumentEditor;
import org.openflexo.components.doc.editorkit.element.ParagraphElement;
import org.openflexo.components.doc.editorkit.widget.FIBFlexoDocumentBrowser;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.doc.FlexoDocObject;
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
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXParagraph;
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
public class TestSimpleDocumentEditor extends AbstractTestDocX {

	private static DocXDocument docXDocument;

	private static JTree tree;

	private static FlexoDocumentEditor<DocXDocument, DocXTechnologyAdapter> editor;

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
		docXDocument = getDocument("SimpleDocument.docx");
		assertNotNull(docXDocument);
		openFlexoDocumentEditor(docXDocument.getResource());

		assertEquals(11, docXDocument.getElements().size());
		DocXParagraph title = (DocXParagraph) docXDocument.getElements().get(0);
		assertEquals("Document title, with 16pixel font", title.getRawText());
		DocXParagraph paragraph = (DocXParagraph) docXDocument.getElements().get(2);
		assertEquals(1, paragraph.getRuns().size());
		assertEquals("This is the first paragraph.", paragraph.getRawText());

		assertNotNull(editor.getStyledDocument().getRootElement());
		assertEquals(11, editor.getStyledDocument().getRootElement().getElementCount());
		assertEquals(docXDocument.getElements().get(0),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(0)).getDocObject());
		assertEquals(docXDocument.getElements().get(1),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(1)).getDocObject());
		assertEquals(docXDocument.getElements().get(2),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(2)).getDocObject());
		assertEquals(docXDocument.getElements().get(3),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(3)).getDocObject());
		assertEquals(docXDocument.getElements().get(4),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(4)).getDocObject());
		assertEquals(docXDocument.getElements().get(5),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(5)).getDocObject());
		assertEquals(docXDocument.getElements().get(6),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(6)).getDocObject());
		assertEquals(docXDocument.getElements().get(7),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(7)).getDocObject());
		assertEquals(docXDocument.getElements().get(8),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(8)).getDocObject());
		assertEquals(docXDocument.getElements().get(9),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(9)).getDocObject());
		assertEquals(docXDocument.getElements().get(10),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(10)).getDocObject());
	}

	@Test
	@TestOrder(3)
	public void appendCharInARun() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("appendCharInARun()");

		editor.getJEditorPane().select(49, 49);
		editor.getJEditorPane().replaceSelection("a");
		assertEquals(11, docXDocument.getElements().size());
		DocXParagraph title = (DocXParagraph) docXDocument.getElements().get(0);
		assertEquals("Document title, with 16pixel font", title.getRawText());
		DocXParagraph paragraph = (DocXParagraph) docXDocument.getElements().get(2);
		assertEquals(1, paragraph.getRuns().size());
		assertEquals("This is the fiarst paragraph.", paragraph.getRawText());

		assertNotNull(editor.getStyledDocument().getRootElement());
		assertEquals(11, editor.getStyledDocument().getRootElement().getElementCount());
		assertEquals(docXDocument.getElements().get(0),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(0)).getDocObject());
		assertEquals(docXDocument.getElements().get(1),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(1)).getDocObject());
		assertEquals(docXDocument.getElements().get(2),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(2)).getDocObject());
		assertEquals(docXDocument.getElements().get(3),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(3)).getDocObject());
		assertEquals(docXDocument.getElements().get(4),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(4)).getDocObject());
		assertEquals(docXDocument.getElements().get(5),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(5)).getDocObject());
		assertEquals(docXDocument.getElements().get(6),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(6)).getDocObject());
		assertEquals(docXDocument.getElements().get(7),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(7)).getDocObject());
		assertEquals(docXDocument.getElements().get(8),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(8)).getDocObject());
		assertEquals(docXDocument.getElements().get(9),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(9)).getDocObject());
		assertEquals(docXDocument.getElements().get(10),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(10)).getDocObject());
	}

	@Test
	@TestOrder(4)
	public void appendStringInARun() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("appendStringInARun()");

		editor.getJEditorPane().select(49, 49);
		editor.getJEditorPane().replaceSelection("hello");
		assertEquals(11, docXDocument.getElements().size());
		DocXParagraph title = (DocXParagraph) docXDocument.getElements().get(0);
		assertEquals("Document title, with 16pixel font", title.getRawText());
		DocXParagraph paragraph = (DocXParagraph) docXDocument.getElements().get(2);
		assertEquals(1, paragraph.getRuns().size());
		assertEquals("This is the fihelloarst paragraph.", paragraph.getRawText());

		assertNotNull(editor.getStyledDocument().getRootElement());
		assertEquals(11, editor.getStyledDocument().getRootElement().getElementCount());
		assertEquals(docXDocument.getElements().get(0),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(0)).getDocObject());
		assertEquals(docXDocument.getElements().get(1),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(1)).getDocObject());
		assertEquals(docXDocument.getElements().get(2),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(2)).getDocObject());
		assertEquals(docXDocument.getElements().get(3),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(3)).getDocObject());
		assertEquals(docXDocument.getElements().get(4),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(4)).getDocObject());
		assertEquals(docXDocument.getElements().get(5),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(5)).getDocObject());
		assertEquals(docXDocument.getElements().get(6),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(6)).getDocObject());
		assertEquals(docXDocument.getElements().get(7),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(7)).getDocObject());
		assertEquals(docXDocument.getElements().get(8),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(8)).getDocObject());
		assertEquals(docXDocument.getElements().get(9),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(9)).getDocObject());
		assertEquals(docXDocument.getElements().get(10),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(10)).getDocObject());
	}

	@Test
	@TestOrder(5)
	public void replaceStringInARun() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("replaceStringInARun()");

		editor.getJEditorPane().select(47, 58);
		editor.getJEditorPane().replaceSelection("modified");
		assertEquals(11, docXDocument.getElements().size());
		DocXParagraph title = (DocXParagraph) docXDocument.getElements().get(0);
		assertEquals("Document title, with 16pixel font", title.getRawText());
		DocXParagraph paragraph = (DocXParagraph) docXDocument.getElements().get(2);
		assertEquals(1, paragraph.getRuns().size());
		assertEquals("This is the modified paragraph.", paragraph.getRawText());

		assertNotNull(editor.getStyledDocument().getRootElement());
		assertEquals(11, editor.getStyledDocument().getRootElement().getElementCount());
		assertEquals(docXDocument.getElements().get(0),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(0)).getDocObject());
		assertEquals(docXDocument.getElements().get(1),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(1)).getDocObject());
		assertEquals(docXDocument.getElements().get(2),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(2)).getDocObject());
		assertEquals(docXDocument.getElements().get(3),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(3)).getDocObject());
		assertEquals(docXDocument.getElements().get(4),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(4)).getDocObject());
		assertEquals(docXDocument.getElements().get(5),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(5)).getDocObject());
		assertEquals(docXDocument.getElements().get(6),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(6)).getDocObject());
		assertEquals(docXDocument.getElements().get(7),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(7)).getDocObject());
		assertEquals(docXDocument.getElements().get(8),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(8)).getDocObject());
		assertEquals(docXDocument.getElements().get(9),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(9)).getDocObject());
		assertEquals(docXDocument.getElements().get(10),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(10)).getDocObject());
	}

	@Test
	@TestOrder(6)
	public void appendNewLineInARun() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("appendNewLineInARun()");

		editor.getJEditorPane().select(56, 56);
		editor.getJEditorPane().replaceSelection("\n");
		assertEquals(12, docXDocument.getElements().size());
		DocXParagraph title = (DocXParagraph) docXDocument.getElements().get(0);
		assertEquals("Document title, with 16pixel font", title.getRawText());
		DocXParagraph paragraph1 = (DocXParagraph) docXDocument.getElements().get(2);
		assertEquals(1, paragraph1.getRuns().size());
		assertEquals("This is the modified ", paragraph1.getRawText());
		DocXParagraph paragraph2 = (DocXParagraph) docXDocument.getElements().get(3);
		assertEquals(1, paragraph2.getRuns().size());
		assertEquals("paragraph.", paragraph2.getRawText());

		assertNotNull(editor.getStyledDocument().getRootElement());
		assertEquals(12, editor.getStyledDocument().getRootElement().getElementCount());
		assertEquals(docXDocument.getElements().get(0),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(0)).getDocObject());
		assertEquals(docXDocument.getElements().get(1),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(1)).getDocObject());
		assertEquals(docXDocument.getElements().get(2),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(2)).getDocObject());
		assertEquals(docXDocument.getElements().get(3),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(3)).getDocObject());
		assertEquals(docXDocument.getElements().get(4),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(4)).getDocObject());
		assertEquals(docXDocument.getElements().get(5),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(5)).getDocObject());
		assertEquals(docXDocument.getElements().get(6),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(6)).getDocObject());
		assertEquals(docXDocument.getElements().get(7),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(7)).getDocObject());
		assertEquals(docXDocument.getElements().get(8),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(8)).getDocObject());
		assertEquals(docXDocument.getElements().get(9),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(9)).getDocObject());
		assertEquals(docXDocument.getElements().get(10),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(10)).getDocObject());
		assertEquals(docXDocument.getElements().get(11),
				((ParagraphElement) editor.getStyledDocument().getRootElement().getElement(11)).getDocObject());
	}

	@Test
	@TestOrder(7)
	public void mergeTwoParagraphs() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("appendNewLineInARun()");

		editor.getJEditorPane().select(56, 60);
		editor.getJEditorPane().replaceSelection("foo");
		assertEquals(11, docXDocument.getElements().size());
		DocXParagraph title = (DocXParagraph) docXDocument.getElements().get(0);
		assertEquals("Document title, with 16pixel font", title.getRawText());
		DocXParagraph paragraph1 = (DocXParagraph) docXDocument.getElements().get(2);
		assertEquals(1, paragraph1.getRuns().size());
		assertEquals("This is the modified fooagraph.", paragraph1.getRawText());
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

		editor = new FlexoDocumentEditor<>(doc);

		FIBFlexoDocumentBrowser docBrowser = new FIBFlexoDocumentBrowser(doc, serviceManager.getApplicationFIBLibraryService()) {
			@Override
			public void singleClick(Object object) {
				if (object instanceof FlexoDocObject) {
					System.out.println("Je viens cliquer sur " + object);
					editor.highlight((FlexoDocObject<DocXDocument, DocXTechnologyAdapter>) object);
				}
			}
		};
		docBrowser.setShowRuns(true);

		pane.add(docBrowser, LayoutPosition.LEFT.name());
		pane.add(editor.getEditorPanel(), LayoutPosition.CENTER.name());
		if (DISPLAY_RIGHT_BROWSER) {
			tree = new JTree((TreeNode) editor.getStyledDocument().getDefaultRootElement());
			pane.add(new JScrollPane(tree), LayoutPosition.RIGHT.name());
		}
		pane.revalidate();

		gcDelegate.addTab(docResource.getName(), pane);

		editor.getStyledDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				System.out.println("removeUpdate()");
				if (tree != null) {
					SwingUtilities.invokeLater(() -> ((DefaultTreeModel) tree.getModel()).reload());
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				System.out.println("insertUpdate()");
				if (tree != null) {
					SwingUtilities.invokeLater(() -> ((DefaultTreeModel) tree.getModel()).reload());
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				System.out.println("changedUpdate()");
				if (tree != null) {
					SwingUtilities.invokeLater(() -> ((DefaultTreeModel) tree.getModel()).reload());
				}
			}
		});

	}
}
