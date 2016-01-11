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

import java.awt.Image;
import java.awt.Rectangle;

import org.openflexo.foundation.DefaultFlexoObject;

public class ImageBox extends DefaultFlexoObject {
	private final Image image;
	private final Rectangle box;
	private String altTitleText;

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
	
	public void setAltTitleText(String value){
		this.altTitleText = value;
		System.out.println ("XTOF: Setting Alt Text to : " + value);
	}
	
	public String getAltTitleText(){
		return this.altTitleText;
		}

	@Override
	public String toString() {
		return "ImageBox for box=" + box;
	}

}
