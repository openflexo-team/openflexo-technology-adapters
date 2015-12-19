package org.openflexo.technologyadapter.pdf.model;

import java.awt.Rectangle;

import org.openflexo.foundation.DefaultFlexoObject;

public class TextBox extends DefaultFlexoObject {
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

	public String getText() {
		return text;
	}

	public Rectangle getBox() {
		return box;
	}

	public float getDir() {
		return dir;
	}

	public double getX() {
		if (dir == 90 || dir == 270) {
			return box.getX() - box.getWidth();
		}
		return box.getX();
	}

	public double getY() {
		if (dir == 0 || dir == 180) {
			return box.getY() - box.getHeight();
		}
		return box.getY();
	}

	public double getWidth() {
		if (dir == 90 || dir == 270) {
			return box.getWidth() * 1.1;
		}
		else {
			return box.getWidth();
		}
	}

	public double getHeight() {
		if (dir == 0 || dir == 180) {
			return box.getHeight() * 1.1;
		}
		else {
			return box.getHeight();
		}
	}

	@Override
	public String toString() {
		return "Box for [" + text + "] box=" + box + " dir=" + dir;
	}

}
