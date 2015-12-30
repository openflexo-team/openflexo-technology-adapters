package org.openflexo.technologyadapter.pdf.model;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.rendering.ImageType;

public class PDFImageBoxStripper {

	private PDDocument document;
	private PDPage page;

	private List<ImageBox> imageBoxes;

	public PDFImageBoxStripper(PDDocument document, PDPage page) {
		super();
		this.document = document;
		this.page = page;
	}

	public List<ImageBox> extractImageBoxes() throws IOException {

		imageBoxes = new ArrayList<ImageBox>();

		PDResources resources = page.getResources();

		for (COSName name : resources.getPropertiesNames()) {
			System.out.println("Property: " + name.getName());

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
			System.out.println("Cropping image " + originalImage.getWidth() + "x" + originalImage.getHeight() + " to " + cropRectangle);

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

		System.out.println("upperBorder=" + upperBorder);

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

		System.out.println("lowerBorder=" + lowerBorder);

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

			System.out.println("leftBorder=" + leftBorder);

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

			System.out.println("rightBorder=" + rightBorder);

			return new Rectangle(leftBorder, upperBorder, rightBorder - leftBorder, lowerBorder - upperBorder);

		}

		else {
			return null;
		}

	}

}
