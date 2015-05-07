/*
 * (c) Copyright 2013- Openflexo
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

package org.openflexo.technologyadapter.docx.gui;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.rm.ResourceLocator;
import org.openflexo.toolbox.ImageIconResource;

public class DocXIconLibrary {
	private static final Logger logger = Logger.getLogger(DocXIconLibrary.class.getPackage().getName());

	public static final ImageIcon DOCX_TECHNOLOGY_BIG_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/WordIcon_32x32.png"));
	public static final ImageIcon DOCX_TECHNOLOGY_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/WordIcon_16x16.png"));
	public static final ImageIcon DOCX_FILE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/DocXFile_16x16.png"));

	public static final ImageIcon SECTION_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Section_16x16.png"));
	public static final ImageIcon PARAGRAPH_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Paragraph_16x16.png"));

	/*public static ImageIcon iconForObject(Class<? extends TechnologyObject<?>> objectClass) {
		return null;
	}*/
}
