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

package org.openflexo.technologyadapter.docx.fml;

import java.lang.reflect.Type;

import org.openflexo.foundation.doc.TextSelection;
import org.openflexo.foundation.doc.fml.TextSelectionRole;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.model.DocXDocument;

@ModelEntity
@ImplementationClass(DocXTextSelectionRole.DocXTextSelectionRoleImpl.class)
@XMLElement
public interface DocXTextSelectionRole extends TextSelectionRole<DocXDocument, DocXTechnologyAdapter> {

	public static abstract class DocXTextSelectionRoleImpl extends TextSelectionRoleImpl<DocXDocument, DocXTechnologyAdapter>
			implements DocXTextSelectionRole {

		@Override
		public Type getType() {
			return TextSelection.class;
		}

		@Override
		public boolean defaultBehaviourIsToBeDeleted() {
			return true;
		}

		@Override
		public RoleCloningStrategy defaultCloningStrategy() {
			return RoleCloningStrategy.Clone;
		}

		@Override
		public Class<? extends TechnologyAdapter> getRoleTechnologyAdapterClass() {
			return DocXTechnologyAdapter.class;
		}

	}
}
