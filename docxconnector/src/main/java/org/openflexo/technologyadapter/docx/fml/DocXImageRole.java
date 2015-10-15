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

import org.openflexo.foundation.doc.FlexoDocFragment.FragmentConsistencyException;
import org.openflexo.foundation.doc.fml.FlexoImageRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXDrawingRun;
import org.openflexo.technologyadapter.docx.model.DocXFactory;
import org.openflexo.technologyadapter.docx.model.DocXFragment;

@ModelEntity
@ImplementationClass(DocXImageRole.DocXImageRoleImpl.class)
@XMLElement
public interface DocXImageRole extends FlexoImageRole<DocXDrawingRun, DocXDocument, DocXTechnologyAdapter> {

	public static final String IMAGE_FRAGMENT_KEY = "imageFragment";

	public DocXFragment getImageFragment();

	public static abstract class DocXImageRoleImpl extends FlexoImageRoleImpl<DocXDrawingRun, DocXDocument, DocXTechnologyAdapter>
			implements DocXImageRole {

		@Override
		public Type getType() {
			return DocXDrawingRun.class;
		}

		@Override
		public boolean defaultBehaviourIsToBeDeleted() {
			return true;
		}

		@Override
		public RoleCloningStrategy defaultCloningStrategy() {
			return RoleCloningStrategy.Clone;
		}

		private DocXFragment imageFragment;

		private DocXFragment makeImageFragment() {
			if (getDrawingRun() != null) {
				try {
					return (DocXFragment) ((DocXFactory) getDocument().getFactory()).makeFragment(getDrawingRun().getParagraph(),
							getDrawingRun().getParagraph());
				} catch (FragmentConsistencyException e) {
					e.printStackTrace();
					return null;
				}
			}
			return null;
		}

		@Override
		public DocXFragment getImageFragment() {
			if (imageFragment == null && getDrawingRun() != null) {
				imageFragment = makeImageFragment();
			}
			return imageFragment;
		}

		@Override
		public void setDrawingRun(DocXDrawingRun run) {
			super.setDrawingRun(run);
			imageFragment = makeImageFragment();
			getPropertyChangeSupport().firePropertyChange(IMAGE_FRAGMENT_KEY, getImageFragment() == null ? false : null,
					getImageFragment());
		}

	}
}
