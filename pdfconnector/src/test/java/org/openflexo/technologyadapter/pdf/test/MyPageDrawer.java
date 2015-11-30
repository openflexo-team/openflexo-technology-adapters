package org.openflexo.technologyadapter.pdf.test;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType3CharProc;
import org.apache.pdfbox.pdmodel.graphics.form.PDTransparencyGroup;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.rendering.PageDrawer;
import org.apache.pdfbox.rendering.PageDrawerParameters;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;

public class MyPageDrawer extends PageDrawer {

	@Override
	public void beginText() throws IOException {
		super.beginText();
		// System.out.println("begin text");
	}

	@Override
	public void endText() throws IOException {
		super.endText();
		// System.out.println("end text ");
	}

	@Override
	public void clip(int windingRule) {
		super.clip(windingRule);
		// System.out.println("clip " + windingRule);
	}

	@Override
	public void drawImage(PDImage pdImage) throws IOException {
		super.drawImage(pdImage);
		// System.out.println("on dessine une image " + pdImage);

		/*Image img = pdImage.getImage();
		JFrame newFrame = new JFrame();
		newFrame.getContentPane().add(new JLabel(new ImageIcon(img)));
		newFrame.validate();
		newFrame.pack();
		newFrame.show();*/
	}

	@Override
	protected void processChildStream(PDContentStream contentStream, PDPage page) throws IOException {
		super.processChildStream(contentStream, page);
		// System.out.println("tiens, un child stream " + contentStream);
	}

	@Override
	protected void processType3Stream(PDType3CharProc charProc, Matrix textRenderingMatrix) throws IOException {
		// TODO Auto-generated method stub
		super.processType3Stream(charProc, textRenderingMatrix);
		// System.out.println("processType3Stream " + textRenderingMatrix);
	}

	@Override
	protected void processTransparencyGroup(PDTransparencyGroup group) throws IOException {
		// TODO Auto-generated method stub
		super.processTransparencyGroup(group);
		// System.out.println("processTransparencyGroup " + group);
		PDStream stream = group.getContentStream();
		// PDStream
	}

	/*@Override
	public void processOperator(String operation, List<COSBase> arguments) throws IOException {
		super.processOperator(operation, arguments);
		// System.out.println("operation: " + operation + " with " + arguments);
	}*/

	private boolean ignore = false;

	@Override
	protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
		if ((operator.getName().equals("BMC") || operator.getName().equals("BDC")) && operands.size() >= 2
				&& operands.get(0) instanceof COSName && ((COSName) operands.get(0)).getName().equals("PlacedPDF")
				&& operands.get(1) instanceof COSName && ((COSName) operands.get(1)).getName().equals("MC0")) {
			System.out.println("> on commence un truc");
			ignore = true;
		}
		if (ignore && operator.getName().equals("EMC")) {
			System.out.println("< on finit un truc");
			ignore = false;
		}
		if (ignore) {
			super.processOperator(operator, operands);
			System.out.println("processOperator: " + operator + " with " + operands);
		}
		else {
			System.out.println("IGNORE processOperator: " + operator + " with " + operands);
		}
	}

	/*@Override
	protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
		super.processOperator(operator, operands);
	}*/

	@Override
	protected void showText(byte[] string) throws IOException {
		super.showText(string);
		// System.out.println("texte: " + new String(string));
	}

	@Override
	protected void showGlyph(Matrix textRenderingMatrix, PDFont font, int code, String unicode, Vector displacement) throws IOException {
		// TODO Auto-generated method stub
		super.showGlyph(textRenderingMatrix, font, code, unicode, displacement);
		// System.out.println(unicode);
	}

	public MyPageDrawer(PageDrawerParameters parameters) throws IOException {
		super(parameters);
	}

}
