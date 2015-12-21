package org.openflexo.technologyadapter.pdf.view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.swing.JDianaInteractiveEditor;
import org.openflexo.fge.swing.SwingViewFactory;
import org.openflexo.fge.swing.control.SwingToolFactory;
import org.openflexo.fge.swing.control.tools.JDianaScaleSelector;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.selection.SelectionListener;
import org.openflexo.technologyadapter.pdf.model.PDFDocumentPage;

public class PDFPagePanel extends JPanel {

	private PDFDocumentPage documentPage;

	private final PDFPageDrawing drawing;

	private List<SelectionListener> selectionListeners;

	public PDFPagePanel(PDFDocumentPage documentPage) {
		super(new BorderLayout());
		this.documentPage = documentPage;

		selectionListeners = new ArrayList<SelectionListener>();

		drawing = new PDFPageDrawing(documentPage);
		final PDFPageDrawingController dc = new PDFPageDrawingController(drawing, this);
		// dc.disablePaintingCache();
		dc.getDrawingView().setName(documentPage.getPDFDocument().getName());
		add(new JScrollPane(dc.getDrawingView()), BorderLayout.CENTER);
		add(dc.scaleSelector.getComponent(), BorderLayout.NORTH);
	}

	public void addToSelectionListeners(SelectionListener listener) {
		selectionListeners.add(listener);
	}

	public void removeFromSelectionListeners(SelectionListener listener) {
		selectionListeners.remove(listener);
	}

	public static class PDFPageDrawingController extends JDianaInteractiveEditor<PDFDocumentPage> {
		private final PDFPagePanel panel;
		private final JPopupMenu contextualMenu;
		private final JDianaScaleSelector scaleSelector;

		public PDFPageDrawingController(PDFPageDrawing drawing, PDFPagePanel panel) {
			super(drawing, drawing.getFactory(), SwingViewFactory.INSTANCE, SwingToolFactory.DEFAULT);
			this.panel = panel;
			scaleSelector = (JDianaScaleSelector) getToolFactory().makeDianaScaleSelector(this);
			contextualMenu = new JPopupMenu();
			contextualMenu.add(new JMenuItem("Item"));
		}

		@Override
		public void addToSelectedObjects(DrawingTreeNode<?, ?> anObject) {
			super.addToSelectedObjects(anObject);
			// System.out.println("Tiens j'ai selectionne " + anObject.getDrawable());
			if (anObject.getDrawable() instanceof FlexoObject) {
				for (SelectionListener l : panel.selectionListeners) {
					l.fireObjectSelected((FlexoObject) anObject.getDrawable());
				}
			}
		}

		@Override
		public void removeFromSelectedObjects(DrawingTreeNode<?, ?> anObject) {
			super.removeFromSelectedObjects(anObject);
			// System.out.println("Tiens j'ai deselectionne " + anObject.getDrawable());
			if (anObject.getDrawable() instanceof FlexoObject) {
				for (SelectionListener l : panel.selectionListeners) {
					l.fireObjectDeselected((FlexoObject) anObject.getDrawable());
				}
			}
		}

		@Override
		public void clearSelection() {
			super.clearSelection();
			// System.out.println("clear de la selection");
			for (SelectionListener l : panel.selectionListeners) {
				l.fireResetSelection();
			}
		}

		/*@Override
		public void selectDrawing() {
			super.selectDrawing();
			System.out.println("selection du drawing");
		}*/

		/*@Override
		protected void fireSelectionUpdated() {
			super.fireSelectionUpdated();
			System.out.println("******* Nouvelle selection: " + getSelectedObjects());
		}*/

		/*@Override
		public JDrawingView<Graph> makeDrawingView() {
			JDrawingView<Graph> returned = super.makeDrawingView();
			returned.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
						logger.info("Display contextual menu");
					}
				}
			});
			return returned;
		}*/

	}

}
