package org.openflexo.technologyadapter.pdf.model;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.PageDrawer;
import org.apache.pdfbox.rendering.PageDrawerParameters;

public class FlexoPDFRenderer extends PDFRenderer {

	public FlexoPDFRenderer(PDDocument document) {
		super(document);
	}

	@Override
	protected PageDrawer createPageDrawer(PageDrawerParameters parameters) throws IOException {
		return new FlexoPageDrawer(parameters);
	}

	public class FlexoPageDrawer extends PageDrawer {
		public FlexoPageDrawer(PageDrawerParameters parameters) throws IOException {
			super(parameters);
		}

		@Override
		protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
			try {
				super.processOperator(operator, operands);
			} catch (RuntimeException e) {
				e.printStackTrace();
				// System.out.println("On essaie de continer 1, cependant");
			} catch (Exception e) {
				e.printStackTrace();
				// System.out.println("On essaie de continer 2, cependant");
			}
		}

	}
}
