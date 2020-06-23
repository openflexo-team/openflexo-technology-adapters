/*
 * (c) Copyright 2013 Openflexo
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

package org.openflexo.technologyadapter.docx.model;

import org.openflexo.foundation.doc.FlexoDocObject;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;

/**
 * Represents a {@link FlexoDocObject} for {@link DocXTechnologyAdapter}<br>
 * Wrap an object defined in docx4j library, supplied as type parameter of this interface.
 * 
 * @param DO
 *            type of object as defined in docx4j library, which this class wraps
 * 
 * @author sylvain
 */
@ModelEntity(isAbstract = true)
public interface DocXObject<DO> extends FlexoDocObject<DocXDocument, DocXTechnologyAdapter> {

	public DO getDocXObject();

}
