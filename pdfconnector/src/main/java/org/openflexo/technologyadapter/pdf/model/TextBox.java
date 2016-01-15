package org.openflexo.technologyadapter.pdf.model;

import java.awt.Rectangle;

public class TextBox extends AbstractBox {
	private final String text;
	private final float dir;

	public TextBox(String text, Rectangle box, float dir) {
		super(box);
		this.text = text;
		this.dir = dir;
		// System.out.println("Box for [" + text + "] box=" + box + " dir=" + dir);
	}

	public String getText() {
		return text;
	}

	public float getDir() {
		return dir;
	}


	@Override
	public String toString() {
		return "Box for [" + text + "] box=" + getBox() + " dir=" + dir;
	}

}
