package org.openflexo.technologyadapter.pdf.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.openflexo.technologyadapter.pdf.model.PDFDocumentPage;
import org.openflexo.technologyadapter.pdf.model.TextBox;

public class PDFPagePanel extends JPanel {

	private PDFDocumentPage documentPage;

	public PDFPagePanel(PDFDocumentPage documentPage) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.documentPage = documentPage;
		add(new JLabel(new ImageIcon(documentPage.getRenderingImage())));
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);

		System.out.println("on peint les autres trucs");

		for (TextBox tb : documentPage.getTextBoxes()) {
			g.setColor(Color.WHITE);
			g.setFont(g.getFont().deriveFont((float) (tb.getBox().height * 1.5)));
			if (tb.getDir() == 0) {
				// g.drawString(tb.text, tb.box.x, tb.box.y + tb.box.height / 2);
			}
			g.setColor(Color.RED);
			g.drawRect(tb.getBox().x, tb.getBox().y - tb.getBox().height, tb.getBox().width, tb.getBox().height);
		}

	}
}
