package org.openflexo.technologyadapter.pdf.test;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.resource.StreamIODelegate;
import org.openflexo.technologyadapter.pdf.model.AbstractTestPDF;
import org.openflexo.technologyadapter.pdf.rm.PDFDocumentResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestLoadPDF extends AbstractTestPDF {

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManagerForPDF();
	}

	@Test
	@TestOrder(2)
	public void testLoadPDF() throws IOException {

		PDFDocumentResource docResource = getDocumentResource("EH200052_MAXITAB Regular_5kg.pdf");

		Assume.assumeTrue(docResource.getIODelegate() instanceof StreamIODelegate);

		try (PDDocument document = PDDocument.load(((StreamIODelegate<?>) docResource.getIODelegate()).getInputStream())) {
			System.out.println("document=" + document);

			PDDocumentInformation docInfo = document.getDocumentInformation();

			System.out.println("docInfo = " + docInfo);
			for (String key : docInfo.getMetadataKeys()) {
				System.out.println("key=" + key + " = " + docInfo.getPropertyStringValue(key));
			}

			PDDocumentCatalog catalog = document.getDocumentCatalog();

			System.out.println("catalog = " + catalog);
			System.out.println("actions= " + catalog.getActions());
			System.out.println("dests= " + catalog.getDests());
			System.out.println("names= " + catalog.getNames());
			System.out.println("metadata= " + catalog.getMetadata());
			System.out.println("outline= " + catalog.getDocumentOutline());

			for (PDPage page : document.getPages()) {

				System.out.println("processing page " + page);

				PDRectangle pdr = page.getCropBox();
				Rectangle rec = new Rectangle();
				rec.setBounds(Math.round(pdr.getLowerLeftX()), Math.round(pdr.getLowerLeftY()), Math.round(pdr.getWidth()),
						Math.round(pdr.getHeight()));
				System.out.println("Crobox: " + rec);
				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
				stripper.addRegion("cropbox", rec);
				stripper.setSortByPosition(true);
				stripper.extractRegions(page);
				List<String> regions = stripper.getRegions();
				for (String region : regions) {
					System.out.println("region " + region);
					String text = stripper.getTextForRegion(region);
					System.out.println("Hop: " + text);
				}

			}
		}
	}

	@Test
	@TestOrder(3)
	public void testLoadPDF2() throws IOException {

		PDFDocumentResource docResource = getDocumentResource("EH200052_MAXITAB Regular_5kg.pdf");

		Assume.assumeTrue(docResource.getIODelegate() instanceof StreamIODelegate);

		try (PDDocument document = PDDocument.load(((StreamIODelegate<?>) docResource.getIODelegate()).getInputStream())) {
			System.out.println("document=" + document);

			PDDocumentInformation docInfo = document.getDocumentInformation();

			System.out.println("docInfo = " + docInfo);
			for (String key : docInfo.getMetadataKeys()) {
				System.out.println("key=" + key + " = " + docInfo.getPropertyStringValue(key));
			}

			PDDocumentCatalog catalog = document.getDocumentCatalog();

			System.out.println("catalog = " + catalog);
			System.out.println("actions= " + catalog.getActions());
			System.out.println("dests= " + catalog.getDests());
			System.out.println("names= " + catalog.getNames());
			System.out.println("metadata= " + catalog.getMetadata());
			System.out.println("outline= " + catalog.getDocumentOutline());

			int width = 612;
			int height = 792;

			// Unused int hX = 320,
			int tX = 340, cX = 100;
			// Unused int hY = 0,
			int tY = 580, cY = 200;
			// Unused int hW = width - hX,
			int tW = width - tX, cW = 100;
			// Unused int hH = 80,
			int tH = height - tY, cH = 60;

			Rectangle header = new Rectangle();
			// header.setBounds(hX, hY, hW, hH);
			header.setBounds(219, 313, 180, 30);
			Rectangle totals = new Rectangle();
			totals.setBounds(tX, tY, tW, tH);
			Rectangle customer = new Rectangle();
			customer.setBounds(cX, cY, cW, cH);

			PDFTextStripperByArea stripper = new PDFTextStripperByArea() {

				private StringBuffer currentString;
				private Rectangle box;
				private float fontSize;

				private void reset() {
					currentString = null;
					box = null;
				}

				@Override
				protected void processTextPosition(TextPosition text) {
					// System.out.println("* " + text + " on " + text.getX() + " " +
					// text.getY());
					super.processTextPosition(text);
					if (currentString == null) {
						currentString = new StringBuffer();
					}
					currentString.append(text.toString());
					if (box == null) {
						box = new Rectangle((int) text.getX(), (int) text.getY(), (int) text.getWidth(), (int) text.getHeight());
					}
					else {
						box = box.union(new Rectangle((int) text.getX(), (int) text.getY(), (int) text.getWidth(), (int) text.getHeight()));
					}
					fontSize = text.getFontSize();
				}

				@Override
				protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
					if (currentString != null && box != null) {
						System.out.println("> [" + currentString + "] box=" + box + " font=" + fontSize);
					}
					reset();
					super.processOperator(operator, operands);
				}

				@Override
				protected void processAnnotation(PDAnnotation annotation, PDAppearanceStream appearance) throws IOException {
					System.out.println("processAnnotation " + annotation + " " + appearance);
					super.processAnnotation(annotation, appearance);
				}

				@Override
				protected void processChildStream(PDContentStream contentStream, PDPage page) throws IOException {
					System.out.println("processChildStream " + contentStream + " " + page);
					super.processChildStream(contentStream, page);
				}

			};
			stripper.addRegion("header", header);
			stripper.addRegion("totals", totals);
			stripper.addRegion("customer", customer);
			stripper.setSortByPosition(true);

			int j = 0;
			for (PDPage page : document.getPages()) {
				stripper.extractRegions(page);
				List<String> regions = stripper.getRegions();
				for (String region : regions) {
					String text = stripper.getTextForRegion(region);
					System.out.println("Region: " + region + " on Page " + j);
					System.out.println("\tText: \n" + text);
				}
				j++;
			}
		}
	}
}
