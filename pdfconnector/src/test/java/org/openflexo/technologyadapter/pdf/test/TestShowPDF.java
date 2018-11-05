package org.openflexo.technologyadapter.pdf.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.documentinterchange.markedcontent.PDPropertyList;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openflexo.foundation.resource.StreamIODelegate;
import org.openflexo.technologyadapter.pdf.model.AbstractTestPDF;
import org.openflexo.technologyadapter.pdf.rm.PDFDocumentResource;

public class TestShowPDF extends AbstractTestPDF {

	@BeforeClass
	public static void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManagerForPDF();
	}

	private PDDocument getPDDocument(String fileName) throws IOException {

		PDFDocumentResource docResource = getDocumentResource(fileName);

		Assume.assumeTrue(docResource.getIODelegate() instanceof StreamIODelegate);

		PDDocument document = PDDocument.load(((StreamIODelegate) docResource.getIODelegate()).getInputStream());
		System.out.println("document=" + document);

		return document;

	}

	private void loadAndDisplayDocument(String title, PDDocument doc) throws IOException {
		for (PDPage page : doc.getPages()) {
			new PDFFrame(title, doc, page);
		}
		doc.close();

	}

	@Test
	public void showFile1() throws IOException {
		loadAndDisplayDocument("EH200052_MAXITAB Regular_5kg.pdf", getPDDocument("EH200052_MAXITAB Regular_5kg.pdf"));
	}

	/*
	 * @Test public void showFile2() throws IOException {
	 * loadAndDisplayDocument("EH201895_MAXITAB Regular_5kg.pdf",
	 * getDocument("EH201895_MAXITAB Regular_5kg.pdf")); }
	 * 
	 * @Test public void showFile3() throws IOException {
	 * loadAndDisplayDocument("EH201976_MAXITAB Regular_1-2kg.pdf",
	 * getDocument("EH201976_MAXITAB Regular_1-2kg.pdf")); }
	 */

	/*
	 * @Test public void showFile4() throws IOException {
	 * loadAndDisplayDocument("EH202050-Action5-200g-5kg.pdf",
	 * getDocument("EH202050-Action5-200g-5kg.pdf")); }
	 */

	/*
	 * @Test public void showFile5() throws IOException {
	 * loadAndDisplayDocument("EH202051-Action5-200g-5kg.pdf",
	 * getDocument("EH202051-Action5-200g-5kg.pdf")); }
	 */

	/*
	 * @Test public void showFile6() throws IOException {
	 * loadAndDisplayDocument("EH200142_SHOCK_3L.pdf",
	 * getDocument("EH200142_SHOCK_3L.pdf")); }
	 */

	/*
	 * @Test public void showFile7() throws IOException {
	 * loadAndDisplayDocument("EtiqManchon-Easyclic_1-66kg.pdf",
	 * getDocument("EtiqManchon-Easyclic_1-66kg.pdf")); }
	 */

	@Test
	public void waitUser() {
		// wmall workaround so that tests won't go forever
		int i = 0;
		while (true && i < 10) {
			// System.out.println("waiting");
			i++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public class PDFFrame extends JFrame {

		public PDFFrame(String name, PDDocument document, PDPage page) {
			super();
			try {
				getContentPane().add(new PagePanel(document, page));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setTitle(name);
			validate();
			pack();
			setVisible(true);
		}

		public class PagePanel extends JPanel {

			private final List<PDFormXObject> formObjects;
			private final List<TextBox> textBoxes;

			public PagePanel(PDDocument document, PDPage page) throws IOException {
				super();

				// PDResources resources = page.getResources();
				/*
				 * Map pageImages = resources.getImages(); if (pageImages !=
				 * null) { Iterator imageIter = pageImages.keySet().iterator();
				 * while (imageIter.hasNext()) { String key = (String)
				 * imageIter.next(); PDXObjectImage image = (PDXObjectImage)
				 * pageImages.get(key);
				 * image.write2file("C:\\Users\\Pradyut\\Documents\\image" + i);
				 * i ++; } }
				 */

				MyPDFRenderer pdfRenderer = new MyPDFRenderer(document);
				BufferedImage originalImage = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
				Image image = originalImage.getScaledInstance((int) page.getMediaBox().getWidth(), (int) page.getMediaBox().getHeight(),
						Image.SCALE_SMOOTH);

				add(new JLabel(new ImageIcon(image)));

				PDRectangle cropBox = page.getCropBox();
				System.out.println("cropBox=" + cropBox);

				PDResources resources = page.getResources();
				System.out.println("xobjects =" + resources.getXObjectNames());

				formObjects = new ArrayList<>();
				textBoxes = new ArrayList<>();

				for (COSName n : resources.getPropertiesNames()) {
					System.out.println("prop=" + resources.getProperties(n).getCOSObject());

					PDPropertyList propList = resources.getProperties(n);
					COSDictionary dict = propList.getCOSObject();

					for (COSName key : dict.keySet()) {
						COSBase value = dict.getDictionaryObject(key);
						System.out.println("key: " + key + " value=" + dict.getDictionaryObject(key));
						if (value instanceof COSStream) {
							// System.out.println(((COSStream)
							// value).getString());
						}

					}
				}

				for (COSName n : resources.getXObjectNames()) {
					// System.out.println("for " + n);
					PDXObject obj;
					obj = resources.getXObject(n);
					System.out.println("obj=" + obj);
					/*
					 * if (obj instanceof PDImageXObject) { PDImageXObject image
					 * = (PDImageXObject) obj; add(new JLabel(new
					 * ImageIcon(image.getImage()))); } else if (obj instanceof
					 * PDFormXObject) { PDFormXObject form = (PDFormXObject)
					 * obj; formObjects.add(form); // System.out.println("form="
					 * + form); PDResources formResources = form.getResources();
					 * // System.out.println("xobjects =" +
					 * formResources.getXObjectNames()); //
					 * System.out.println("box=" + form.getBBox()); for (COSName
					 * n2 : formResources.getXObjectNames()) { PDXObject obj2 =
					 * formResources.getXObject(n2); // System.out.println("n2="
					 * + n2 + " obj2=" + obj2); } }
					 */
				}

				int width = 612;
				int height = 792;

				int hX = 320, tX = 340, cX = 100;
				int hY = 0, tY = 580, cY = 200;
				int hW = width - hX, tW = width - tX, cW = 100;
				int hH = 80, tH = height - tY, cH = 60;

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
					private float dir;

					private void reset() {
						currentString = null;
						box = null;
					}

					@Override
					protected void processTextPosition(TextPosition text) {
						System.out.println("* " + text + " on (" + text.getX() + "," + text.getY() + ") width=" + text.getWidth()
								+ " height=" + text.getHeight() + " font:" + text.getFontSize() + " " + text.getFontSizeInPt() + " matrix="
								+ text.getTextMatrix() + " dir=" + text.getDir() + " font=" + text.getFont());
						super.processTextPosition(text);
						if (currentString == null) {
							currentString = new StringBuffer();
						}
						currentString.append(text.toString());

						int width = (int) text.getWidth();
						int height = (int) text.getHeight();
						if (text.getDir() == 0) {
							height = Math.max((int) text.getHeight(), (int) text.getFontSizeInPt());
						}
						if (box == null) {
							box = new Rectangle((int) text.getX(), (int) text.getY(), width, height);
						}
						else {
							box = box.union(new Rectangle((int) text.getX(), (int) text.getY(), width, height));
						}
						fontSize = text.getFontSize();
						dir = text.getDir();
					}

					@Override
					protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
						if (currentString != null && box != null) {
							// System.out.println("> [" + currentString + "]
							// box=" + box + " font=" + fontSize + " dir=" +
							// dir);
							textBoxes.add(new TextBox(currentString.toString(), box, dir));
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
				// for (PDPage page : document.getPages()) {
				stripper.extractRegions(page);
				List<String> regions = stripper.getRegions();
				for (String region : regions) {
					String text = stripper.getTextForRegion(region);
					System.out.println("Region: " + region + " on Page " + j);
					System.out.println("\tText: \n" + text);
				}
				j++;
				// }

				System.out.println("on s'arrete");
			}

			public class TextBox {
				private final String text;
				private final Rectangle box;
				private final float dir;

				public TextBox(String text, Rectangle box, float dir) {
					super();
					this.text = text;
					this.box = box;
					this.dir = dir;
					System.out.println("Box for [" + text + "] box=" + box + " dir=" + dir);
				}

			}

			@Override
			public void paint(Graphics g) {
				// TODO Auto-generated method stub
				super.paint(g);

				for (PDFormXObject o : formObjects) {
					g.setColor(Color.RED);
					g.drawRect((int) o.getBBox().getLowerLeftX(), (int) o.getBBox().getUpperRightY(), (int) o.getBBox().getWidth(),
							(int) o.getBBox().getHeight());
				}

				for (TextBox tb : textBoxes) {
					g.setColor(Color.WHITE);
					g.setFont(g.getFont().deriveFont((float) (tb.box.height * 1.5)));
					if (tb.dir == 0) {
						// g.drawString(tb.text, tb.box.x, tb.box.y +
						// tb.box.height / 2);
					}
					g.setColor(Color.RED);
					g.drawRect(tb.box.x + 5, tb.box.y - tb.box.height / 2, tb.box.width, tb.box.height);
				}

			}
		}
	}
}
