package org.openflexo.technologyadapter.pdf.test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.junit.Test;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestLoadPDF2 {

	@Test
	public void testLoadPDF() throws IOException {
		final File resourceDir = ((FileResourceImpl) ResourceLocator.locateResource("TestResourceCenter/PDF"))
				.getFile();
		System.out.println("resourceDir=" + resourceDir);
		for (File f : resourceDir.listFiles()) {
			System.out.println("> " + f);
		}
		final File resource = ((FileResourceImpl) ResourceLocator
				.locateResource("TestResourceCenter/PDF/EH200052_MAXITAB Regular_5kg.pdf")).getFile();

		PDDocument document = null;

		document = PDDocument.load(resource);

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

		COSDocument cosDocument = document.getDocument();
		System.out.println("cosDocument=" + cosDocument);

		for (COSObject obj : cosDocument.getObjects()) {
			System.out.println("> " + obj + " obj= " + obj.getObject());
		}

		PDFTextStripper textStripper = new PDFTextStripper();
		StringWriter textWriter = new StringWriter();
		textStripper.writeText(document, textWriter);
		System.out.println("text=" + textWriter.toString());

		PDPage page = document.getPage(0);

		/*
		 * PDFMarkedContentExtractor e = new PDFMarkedContentExtractor() {
		 * 
		 * @Override public void beginMarkedContentSequence(COSName tag,
		 * COSDictionary properties) { System.out.println("BEGIN with " + tag +
		 * " properties=" + properties); super.beginMarkedContentSequence(tag,
		 * properties); }
		 * 
		 * @Override public void endMarkedContentSequence() {
		 * System.out.println("END"); super.endMarkedContentSequence(); }
		 * 
		 * @Override protected void
		 * processTextPosition(org.apache.pdfbox.text.TextPosition text) {
		 * super.processTextPosition(text); //
		 * System.out.println("processTextPosition with " + text); } };
		 * e.processPage(page);
		 */

		PDFTextStripperByArea textStripper2 = new PDFTextStripperByArea() {
			@Override
			protected void processTextPosition(org.apache.pdfbox.text.TextPosition text) {
				super.processTextPosition(text);
				System.out.println("hop: " + text);
			}
		};
		// StringWriter textWriter2 = new StringWriter();
		// textStripper2.writeText(document, textWriter2);
		// System.out.println("text=" + textWriter2.toString());

		// textStripper2.processPage(page);

		textStripper2.extractRegions(page);

		System.out.println("regions=" + textStripper2.getRegions());

		document.close();

	}
}
