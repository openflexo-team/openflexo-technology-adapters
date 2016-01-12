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

public class ImageBox extends AbstractBox {
	private final Image image;
	private String altTitleText;

	public ImageBox(Image image, Rectangle box) {
		super(box);
		this.image = image;
	}
	


	public Image getImage() {
		return image;
	}

	public void setAltTitleText(String value){
		this.altTitleText = value;
	}
	
	public String getAltTitleText(){
		return this.altTitleText;
		}

	@Override
	public String toString() {
		return "ImageBox for box=" + getBox();
	}

}
