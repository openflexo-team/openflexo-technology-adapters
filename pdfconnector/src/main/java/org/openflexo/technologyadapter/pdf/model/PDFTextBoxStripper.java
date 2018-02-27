package org.openflexo.technologyadapter.pdf.model;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;

public class PDFTextBoxStripper extends PDFTextStripperByArea {

	private List<TextBox> textBoxes;

	// Unused private PDDocument document;
	private PDPage page;
	private StringBuffer currentString;
	private Rectangle box;
	// Unused private float fontSize;
	private float dir;

	public PDFTextBoxStripper(PDDocument document, PDPage page) throws IOException {
		super();
		this.document = document;
		this.page = page;
		addRegion("all", new Rectangle(0, 0, (int) page.getMediaBox().getWidth(), (int) page.getMediaBox().getHeight()));
		setSortByPosition(true);
	}

	public List<TextBox> extractTextBoxes() {
		if (textBoxes != null) {
			textBoxes.clear();
		}
		textBoxes = new ArrayList<>();
		try {
			extractRegions(page);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return textBoxes;
	}

	private void reset() {
		currentString = null;
		box = null;
	}

	@Override
	protected void processTextPosition(TextPosition text) {
		// System.out.println("* " + text + " on (" + text.getX() + "," + text.getY() + ") width=" + text.getWidth() + " height="
		// + text.getHeight() + " font:" + text.getFontSize() + " " + text.getFontSizeInPt() + " matrix=" + text.getTextMatrix()
		// + " dir=" + text.getDir() + " font=" + text.getFont());
		super.processTextPosition(text);
		if (currentString == null) {
			currentString = new StringBuffer();
		}
		currentString.append(text.toString());

		dir = text.getDir();
		// Unused fontSize = text.getFontSizeInPt();

		int width = (int) text.getWidth();
		int height = (int) text.getHeight();
		if (text.getDir() == 0 || text.getDir() == 180) {
			height = Math.max((int) text.getHeight(), (int) (text.getFontSizeInPt()));
		}
		else if (text.getDir() == 90 || text.getDir() == 270) {
			width = Math.max((int) text.getWidth(), (int) (text.getFontSizeInPt()));
		}

		int x = (int) text.getX();
		int y = (int) text.getY();

		if (dir == 90 || dir == 270) {
			x = (int) (text.getX() - width);
			width = (int) (width * 1.1);
		}
		if (dir == 0 || dir == 180) {
			y = (int) (text.getY() - height);
			height = (int) (height * 1.1);
		}
		Rectangle r = new Rectangle(x, y, width, height);

		if (box == null) {
			box = r;
		}
		else {
			box = box.union(r);
		}

		/*if (dir != 0) {
			System.out.println("> text=" + text.toString() + " dir=" + dir);
		}*/

		// Does not work !!!
		// http://stackoverflow.com/questions/4361242/extract-rotation-scale-values-from-2d-transformation-matrix
		/*Matrix m = text.getTextMatrix();
		Point2D.Double p = new Point2D.Double(1.0, 0.0);
		System.out.println("pt=" + p);
		m.transform(p);
		System.out.println("pt.t=" + p);
		Double rotate = Math.atan2(p.getY(), p.getX());
		System.out.println("dir was: " + dir + " but in fact: " + rotate);*/

	}

	@Override
	protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
		if (currentString != null && box != null) {
			// System.out.println("> [" + currentString + "] box=" + box + " font=" + fontSize + " dir=" + dir);
			textBoxes.add(new TextBox(currentString.toString(), box, dir));
		}
		reset();
		super.processOperator(operator, operands);
	}

}
