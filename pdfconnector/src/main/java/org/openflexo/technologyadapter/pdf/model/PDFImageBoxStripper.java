/*
 * (c) Copyright 2015 Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.openflexo.technologyadapter.pdf.model;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDTransparencyGroup;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class PDFImageBoxStripper {

	private PDDocument document;
	private PDPage page;
	
	// Sax parser used to build Metadata document (unparsable with DomXmpParser from pdfbox..)
	// TODO: evolve to DomXmpParser when needed
	private SAXBuilder sb;
	private XPathFactory xpathFactory;
	
	private List<ImageBox> imageBoxes;

	
	// List of static field for Metadata field processing

	private HashMap<String,XPathExpression> metadataExpr;
	static final String altTitlefilter = "//dc:title";

	public PDFImageBoxStripper(PDDocument document, PDPage page) {
		super();
		this.document = document;
		this.page = page;
		sb= new SAXBuilder();
		xpathFactory = XPathFactory.instance();
		// fill the exp HashMap
		metadataExpr = new HashMap<String,XPathExpression>();
		metadataExpr.put(altTitlefilter, xpathFactory.compile(altTitlefilter, Filters.element(),null,
		        Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/")));
	}

	private void listEmbeddedXObjects( PDResources resources) throws IOException{
		PDMetadata md = null;
		PDRectangle rect = null;

		Iterable<COSName> xobjectNames = resources.getXObjectNames();

		// TODO Find a way to identify resources, boxes and limits of elements in metadata
		
		for (COSName name : xobjectNames){
			PDXObject localXObject = resources.getXObject(name);
			System.out.println("XTOF: found some XObjects : " + name.getName() + " .. " + localXObject.getClass().getCanonicalName());

			if (localXObject instanceof PDImageXObject){
				md = ((PDImageXObject) localXObject).getMetadata();
				
				System.out.println("\t XTOF: its some image:" + name.getName() + " ..   : " + md.toString() );
			}
			else if (localXObject instanceof PDTransparencyGroup){
				
				listEmbeddedXObjects(((PDTransparencyGroup)localXObject ).getResources());
				
				rect = ((PDTransparencyGroup)localXObject ).getBBox();
				md =((PDTransparencyGroup)localXObject ).getStream().getMetadata();
				
				System.out.println("\t XTOF: its some TransparencyGroup:" + name.getName() + " BBox :" + rect.toString() );
			}

		}
	}


	private void decodeMetadata (String content,ImageBox imgBox) {
		try {

			Document doc = sb.build(new StringReader(content));

			// Alt Title for images
			List<Element> lstAlt = metadataExpr.get(altTitlefilter).evaluate(doc);
			for (Element el : lstAlt){
				//System.out.println ("\t\t XTOF: " + el.getChild("Alt",Namespace.getNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#")).getChildText("li",Namespace.getNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#")));
				imgBox.setAltTitleText(el.getChild("Alt",Namespace.getNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#")).getChildText("li",Namespace.getNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#")).trim());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<ImageBox> extractImageBoxes() throws IOException {

		imageBoxes = new ArrayList<ImageBox>();

		PDResources resources = page.getResources();
		
		// TODO: figure out howthis can be useful
		// listEmbeddedXObjects(resources); No need for now.

		for (COSName name : resources.getPropertiesNames()) {

			
			// System.out.println("Property: " + name.getName());

			ImageStripperRenderer imageRenderer = new ImageStripperRenderer(document, name);
			BufferedImage originalImage = imageRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
			/*Image image = originalImage.getScaledInstance((int) page.getMediaBox().getWidth(), (int) page.getMediaBox().getHeight(),
					Image.SCALE_SMOOTH);*/

			/*JFrame newFrame = new JFrame();
			newFrame.getContentPane().add(new JLabel(new ImageIcon(image)));
			newFrame.validate();
			newFrame.pack();
			newFrame.show();*/

			Rectangle cropRectangle = cropImage(originalImage);
			// System.out.println("Cropping image " + originalImage.getWidth() + "x" + originalImage.getHeight() + " to " + cropRectangle);

			if (cropRectangle != null) {
				Image croppedImage = originalImage.getSubimage(cropRectangle.x, cropRectangle.y, cropRectangle.width, cropRectangle.height);
				Image image = croppedImage.getScaledInstance(
						(int) page.getMediaBox().getWidth() * cropRectangle.width / originalImage.getWidth(),
						(int) page.getMediaBox().getHeight() * cropRectangle.height / originalImage.getHeight(), Image.SCALE_SMOOTH);
				/*JFrame newFrame2 = new JFrame();
				newFrame2.getContentPane().add(new JLabel(new ImageIcon(image2)));
				newFrame2.validate();
				newFrame2.pack();
				newFrame2.show();*/

				cropRectangle.x = (int) (cropRectangle.x * page.getMediaBox().getWidth() / originalImage.getWidth());
				cropRectangle.y = (int) (cropRectangle.y * page.getMediaBox().getHeight() / originalImage.getHeight());
				cropRectangle.width = (int) (cropRectangle.width * page.getMediaBox().getWidth() / originalImage.getWidth());
				cropRectangle.height = (int) (cropRectangle.height * page.getMediaBox().getHeight() / originalImage.getHeight());

				ImageBox imageBox = new ImageBox(image, cropRectangle);
				
				System.out.println("\t XTOF: its some IMAGE CROPPED:" + name.getName() + " BBox :" + cropRectangle.toString() );
				
				// set AltTitle Metadata

				COSDictionary cd =  resources.getProperties(name).getCOSObject();
				COSStream md = (COSStream) cd.getDictionaryObject("Metadata");
				decodeMetadata(md.toTextString(),imageBox);
				
				imageBoxes.add(imageBox);
			}

		}

		return imageBoxes;

	}

	private Rectangle cropImage(BufferedImage originalImage) {

		boolean flag = false;

		// upper border
		int upperBorder = -1;

		do {
			upperBorder++;
			for (int c1 = 0; c1 < originalImage.getWidth(); c1++) {
				if (originalImage.getRGB(c1, upperBorder) != Color.white.getRGB()) {
					flag = true;
					break;
				}
			}

			if (upperBorder >= originalImage.getHeight() - 1)
				flag = true;
		} while (!flag);

		// System.out.println("upperBorder=" + upperBorder);

		// lower border
		flag = false;
		int lowerBorder = originalImage.getHeight();
		do {
			lowerBorder--;
			for (int c1 = 0; c1 < originalImage.getWidth(); c1++) {
				if (originalImage.getRGB(c1, lowerBorder) != Color.white.getRGB()) {
					flag = true;
					break;
				}
			}

			if (lowerBorder <= upperBorder)
				flag = true;
		} while (!flag);

		// System.out.println("lowerBorder=" + lowerBorder);

		if (upperBorder < lowerBorder) {
			// OK, some non empty contents were found

			// left border
			flag = false;
			int leftBorder = -1;
			do {
				leftBorder++;
				for (int c1 = upperBorder; c1 <= lowerBorder; c1++) {
					if (originalImage.getRGB(leftBorder, c1) != Color.white.getRGB()) {
						flag = true;
						break;
					}
				}

				if (leftBorder >= originalImage.getWidth() - 1)
					flag = true;
			} while (!flag);

			// System.out.println("leftBorder=" + leftBorder);

			// right border
			flag = false;
			int rightBorder = originalImage.getWidth();
			do {
				rightBorder--;
				for (int c1 = upperBorder; c1 <= lowerBorder; c1++) {
					if (originalImage.getRGB(rightBorder, c1) != Color.white.getRGB()) {
						flag = true;
						break;
					}
				}

				if (rightBorder <= leftBorder)
					flag = true;
			} while (!flag);

			// System.out.println("rightBorder=" + rightBorder);

			return new Rectangle(leftBorder, upperBorder, rightBorder - leftBorder, lowerBorder - upperBorder);

		}

		else {
			return null;
		}

	}

}
