package org.openflexo.technologyadapter.pdf.test;

import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.resource.StreamIODelegate;
import org.openflexo.technologyadapter.pdf.model.AbstractTestPDF;
import org.openflexo.technologyadapter.pdf.rm.PDFDocumentResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestLoadPDF3 extends AbstractTestPDF {

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManagerForPDF();
	}

	@Test
	@TestOrder(2)
	public void loadImages() throws IOException {

		PDFDocumentResource docResource = getDocumentResource("EH200052_MAXITAB Regular_5kg.pdf");

		Assume.assumeTrue(docResource.getIODelegate() instanceof StreamIODelegate);

		try (PDDocument document = PDDocument.load(((StreamIODelegate<?>) docResource.getIODelegate()).getInputStream())) {

			System.out.println("document=" + document);

			// Unused int i = 1;
			// Unused String name = null;

			for (PDPage page : document.getPages()) {
				PDResources resources = page.getResources();

				System.out.println("xobjects =" + resources.getXObjectNames());

				for (COSName n : resources.getXObjectNames()) {
					System.out.println("for " + n);
					PDXObject obj = resources.getXObject(n);
					System.out.println("obj=" + obj);
					if (obj instanceof PDImageXObject) {
						PDImageXObject image = (PDImageXObject) obj;
						JFrame frame = new JFrame();
						frame.getContentPane().add(new JLabel(new ImageIcon(image.getImage())));
						frame.validate();
						frame.pack();
						frame.setVisible(true);
					}
					else if (obj instanceof PDFormXObject) {
						PDFormXObject form = (PDFormXObject) obj;
						System.out.println("form=" + form);
						PDResources formResources = form.getResources();
						System.out.println("xobjects =" + formResources.getXObjectNames());
						System.out.println("box=" + form.getBBox());
						for (COSName n2 : formResources.getXObjectNames()) {
							PDXObject obj2 = formResources.getXObject(n2);
							System.out.println("n2=" + n2 + " obj2=" + obj2);
						}
					}
				}
				// waits 10 seconds and stops
				int t = 0;
				while (t < 10) {
					System.out.println("hop");
					t++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				/*
				 * Map pageImages = resources.getImages(); if (pageImages != null) {
				 * Iterator imageIter = pageImages.keySet().iterator(); while
				 * (imageIter.hasNext()) { String key = (String) imageIter.next();
				 * PDXObjectImage image = (PDXObjectImage) pageImages.get(key);
				 * image.write2file("C:\\Users\\Pradyut\\Documents\\image" + i);
				 * i++; } }
				 */
			}
		}
	}

	public class PDFFrame extends JFrame {
	}
}
