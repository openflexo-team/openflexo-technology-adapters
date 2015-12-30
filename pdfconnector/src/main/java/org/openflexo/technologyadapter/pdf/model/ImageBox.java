package org.openflexo.technologyadapter.pdf.model;

import java.awt.Image;
import java.awt.Rectangle;

import org.openflexo.foundation.DefaultFlexoObject;

public class ImageBox extends DefaultFlexoObject {
	private final Image image;
	private final Rectangle box;

	public ImageBox(Image image, Rectangle box) {
		super();
		this.image = image;
		this.box = box;
	}

	public Image getImage() {
		return image;
	}

	public Rectangle getBox() {
		return box;
	}

	public double getX() {
		// System.out.println("x=" + box.getX());
		return box.getX();
	}

	public double getY() {
		// System.out.println("y=" + box.getY());
		return box.getY();
	}

	public double getWidth() {
		// System.out.println("width=" + box.getWidth());
		return box.getWidth();
	}

	public double getHeight() {
		// System.out.println("height=" + box.getHeight());
		return box.getHeight();
	}

	@Override
	public String toString() {
		return "ImageBox for box=" + box;
	}

}
