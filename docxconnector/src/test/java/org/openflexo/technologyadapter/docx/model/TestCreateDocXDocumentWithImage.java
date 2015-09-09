/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Excelconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.docx.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * This test is intented to test blank .docx document creation features
 * 
 * @author sylvain
 *
 */
@RunWith(OrderedRunner.class)
public class TestCreateDocXDocumentWithImage extends AbstractTestDocX {
	protected static final Logger logger = Logger.getLogger(TestCreateDocXDocumentWithImage.class.getPackage().getName());

	private static DocXTechnologyAdapter technologicalAdapter;

	private static DocXDocument newDocument = null;
	private static DocXDocumentResource newDocResource;

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager();
	}

	@Test
	@TestOrder(2)
	public void testEmptyDocXCreation() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(DocXTechnologyAdapter.class);

		newDocResource = technologicalAdapter.createNewDocXDocumentResource(resourceCenter, "DocX", "TestDocumentWithTable.docx", true);

		System.out.println("uri=" + newDocResource.getURI());
		System.out.println("newDocResource=" + newDocResource);

		assertNotNull(newDocResource);
		assertEquals("http://openflexo.org/test/TestResourceCenter/TestDocumentWithTable.docx", newDocResource.getURI());

		assertNotNull(newDocument = newDocResource.getResourceData(null));

		System.out.println(newDocument.debugStructuredContents());

		assertEquals(0, newDocument.getRootElements().size());

		newDocResource.save(null);

		assertFalse(newDocResource.isModified());
	}

	@Test
	@TestOrder(3)
	public void testAddSomeParagraphs() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("testAddSomeParagraphs");

		DocXStyle normalStyle = (DocXStyle) newDocument.getStyleByIdentifier("Normal");
		DocXStyle heading1 = (DocXStyle) newDocument.getStyleByIdentifier("Heading1");
		DocXStyle heading2 = (DocXStyle) newDocument.getStyleByIdentifier("Heading2");

		newDocument.addStyledParagraphOfText(heading1, "Title of document");
		newDocument.addStyledParagraphOfText(heading2, "Subtitle1");
		newDocument.addStyledParagraphOfText(normalStyle, "This is a paragraph");
		newDocument.addStyledParagraphOfText(heading2, "Subtitle2");
		newDocument.addStyledParagraphOfText(normalStyle, "This is an other paragraph");

		System.out.println(newDocument.debugStructuredContents());

		assertEquals(5, newDocument.getElements().size());
		assertEquals(1, newDocument.getRootElements().size());

		DocXParagraph title = (DocXParagraph) newDocument.getElements().get(0);
		DocXParagraph subtitle1 = (DocXParagraph) newDocument.getElements().get(1);
		DocXParagraph text1 = (DocXParagraph) newDocument.getElements().get(2);
		DocXParagraph subtitle2 = (DocXParagraph) newDocument.getElements().get(3);
		DocXParagraph text2 = (DocXParagraph) newDocument.getElements().get(4);

		assertEquals("Title of document", title.getRawText());
		assertEquals(heading1, title.getStyle());
		assertEquals("Subtitle1", subtitle1.getRawText());
		assertEquals(heading2, subtitle1.getStyle());
		assertEquals("This is a paragraph", text1.getRawText());
		assertEquals(normalStyle, text1.getStyle());
		assertEquals("Subtitle2", subtitle2.getRawText());
		assertEquals(heading2, subtitle2.getStyle());
		assertEquals("This is an other paragraph", text2.getRawText());
		assertEquals(normalStyle, text2.getStyle());

		assertTrue(newDocResource.isModified());
		newDocResource.save(null);
		assertFalse(newDocResource.isModified());

		System.out.println(newDocument.debugStructuredContents());

	}

	@Test
	@TestOrder(4)
	public void testAddImage() throws Exception {
		log("testAddImage");

		File imageFile = new File(resourceCenter.getDirectory(), "TestResourceCenter/Images/CarteDeVoeuxOF2015.png");
		assertTrue(imageFile.exists());
		byte[] bytes = convertImageToByteArray(imageFile);
		addImageToPackage(newDocument.getWordprocessingMLPackage(), bytes);

		System.out.println(resourceCenter.getDirectory());

		System.out.println(newDocument.debugStructuredContents());

		newDocResource.save(null);

	}

	/**
	 * Docx4j contains a utility method to create an image part from an array of bytes and then adds it to the given package. In order to be
	 * able to add this image to a paragraph, we have to convert it into an inline object. For this there is also a method, which takes a
	 * filename hint, an alt-text, two ids and an indication on whether it should be embedded or linked to. One id is for the drawing object
	 * non-visual properties of the document, and the second id is for the non visual drawing properties of the picture itself. Finally we
	 * add this inline object to the paragraph and the paragraph to the main document of the package.
	 *
	 * @param wordMLPackage
	 *            The package we want to add the image to
	 * @param bytes
	 *            The bytes of the image
	 * @throws Exception
	 *             Sadly the createImageInline method throws an Exception (and not a more specific exception type)
	 */
	private static void addImageToPackage(WordprocessingMLPackage wordMLPackage, byte[] bytes) throws Exception {
		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

		int docPrId = 1;
		int cNvPrId = 2;
		Inline inline = imagePart.createImageInline("Filename hint", "Alternative text", docPrId, cNvPrId, false);

		P paragraph = addInlineImageToParagraph(inline);

		DocXParagraph paragraphWithImage = newDocument.getFactory().makeNewDocXParagraph(paragraph);
		newDocument.addToElements(paragraphWithImage);

		// wordMLPackage.getMainDocumentPart().addObject(paragraph);
	}

	/**
	 * We create an object factory and use it to create a paragraph and a run. Then we add the run to the paragraph. Next we create a
	 * drawing and add it to the run. Finally we add the inline object to the drawing and return the paragraph.
	 *
	 * @param inline
	 *            The inline object containing the image.
	 * @return the paragraph containing the image
	 */
	private static P addInlineImageToParagraph(Inline inline) {
		// Now add the in-line image to a paragraph
		ObjectFactory factory = new ObjectFactory();
		P paragraph = factory.createP();
		R run = factory.createR();
		paragraph.getContent().add(run);
		Drawing drawing = factory.createDrawing();
		run.getContent().add(drawing);
		drawing.getAnchorOrInline().add(inline);
		return paragraph;
	}

	/**
	 * Convert the image from the file into an array of bytes.
	 *
	 * @param file
	 *            the image file to be converted
	 * @return the byte array containing the bytes from the image
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static byte[] convertImageToByteArray(File file) throws FileNotFoundException, IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		// You cannot create an array using a long, it needs to be an int.
		if (length > Integer.MAX_VALUE) {
			System.out.println("File too large!!");
		}
		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		// Ensure all the bytes have been read
		if (offset < bytes.length) {
			System.out.println("Could not completely read file " + file.getName());
		}
		is.close();
		return bytes;
	}
}
