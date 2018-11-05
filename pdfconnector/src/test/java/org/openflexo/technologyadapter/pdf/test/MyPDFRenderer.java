package org.openflexo.technologyadapter.pdf.test;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.PageDrawerParameters;

public class MyPDFRenderer extends PDFRenderer {

	public MyPDFRenderer(PDDocument document) {
		super(document);
	}

	@Override
	protected MyPageDrawer createPageDrawer(PageDrawerParameters parameters) throws IOException {
		return new MyPageDrawer(parameters);
	}

}
