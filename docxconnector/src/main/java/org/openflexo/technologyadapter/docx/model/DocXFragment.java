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

import java.util.List;

import org.openflexo.foundation.doc.FlexoDocFragment;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;

/**
 * Implementation of {@link FlexoDocFragment} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(DocXFragment.DocXFragmentImpl.class)
@XMLElement
public interface DocXFragment extends FlexoDocFragment<DocXDocument, DocXTechnologyAdapter> {

	@Override
	public List<DocXElement<?>> getElements();

	public static abstract class DocXFragmentImpl extends FlexoDocumentFragmentImpl<DocXDocument, DocXTechnologyAdapter> implements
			DocXFragment {

		@Override
		public List<DocXElement<?>> getElements() {
			return (List<DocXElement<?>>) super.getElements();
		}

		@Override
		public String toString() {
			return getStringRepresentation();
		}

	}

}
