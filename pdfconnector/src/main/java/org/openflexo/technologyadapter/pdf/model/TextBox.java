package org.openflexo.technologyadapter.pdf.model;

import java.awt.Rectangle;

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

	public String getText() {
		return text;
	}

	public Rectangle getBox() {
		return box;
	}

	public float getDir() {
		return dir;
	}

	@Override
	public String toString() {
		return "Box for [" + text + "] box=" + box + " dir=" + dir;
	}

}
