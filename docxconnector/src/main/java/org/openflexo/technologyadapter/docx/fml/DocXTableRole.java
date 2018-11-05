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
import org.openflexo.foundation.doc.fml.FlexoTableRole;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXFactory;
import org.openflexo.technologyadapter.docx.model.DocXFragment;
import org.openflexo.technologyadapter.docx.model.DocXTable;

@ModelEntity
@ImplementationClass(DocXTableRole.DocXTableRoleImpl.class)
@XMLElement
public interface DocXTableRole extends FlexoTableRole<DocXTable, DocXDocument, DocXTechnologyAdapter> {

	public static final String TABLE_FRAGMENT_KEY = "tableFragment";

	public DocXFragment getTableFragment();

	public static abstract class DocXTableRoleImpl extends FlexoTableRoleImpl<DocXTable, DocXDocument, DocXTechnologyAdapter>
			implements DocXTableRole {

		@Override
		public Type getType() {
			return DocXTable.class;
		}

		@Override
		public boolean defaultBehaviourIsToBeDeleted() {
			return true;
		}

		@Override
		public RoleCloningStrategy defaultCloningStrategy() {
			return RoleCloningStrategy.Clone;
		}

		private DocXFragment tableFragment;

		private DocXFragment makeTableFragment() {
			if (getTable() != null) {
				try {
					return (DocXFragment) ((DocXFactory) getDocument().getFactory()).makeFragment(getTable(), getTable());
				} catch (FragmentConsistencyException e) {
					e.printStackTrace();
					return null;
				}
			}
			return null;
		}

		@Override
		public DocXFragment getTableFragment() {
			if (tableFragment == null && getTable() != null) {
				tableFragment = makeTableFragment();
			}
			return tableFragment;
		}

		@Override
		public void setTable(DocXTable table) {
			super.setTable(table);
			tableFragment = makeTableFragment();
			getPropertyChangeSupport().firePropertyChange(TABLE_FRAGMENT_KEY, getTableFragment() == null ? false : null,
					getTableFragment());
		}

		/*@Override
		public ActorReference<DocXTable> makeActorReference(DocXTable table, FlexoConceptInstance fci) {
		
			VirtualModelInstanceModelFactory factory = fci.getFactory();
			TableActorReference<DocXTable> returned = factory.newInstance(TableActorReference.class);
			returned.setFlexoRole(this);
			returned.setFlexoConceptInstance(fci);
			returned.setModellingElement(table);
			return returned;
		
		}*/

		@Override
		public Class<? extends TechnologyAdapter> getRoleTechnologyAdapterClass() {
			return DocXTechnologyAdapter.class;
		}

	}
}
