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

import org.openflexo.connie.type.TypeUtils;
import org.openflexo.foundation.doc.fml.FlexoFragmentRole;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.pamela.exceptions.InvalidDataException;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXFragment;

@ModelEntity
@ImplementationClass(DocXFragmentRole.DocXFragmentRoleImpl.class)
@XMLElement
public interface DocXFragmentRole extends FlexoFragmentRole<DocXFragment, DocXDocument, DocXTechnologyAdapter> {

	// @PropertyIdentifier(type = DocXFragment.class)
	// public static final String FRAGMENT_KEY = "fragment";

	/**
	 * Return the represented fragment in the template resource<br>
	 * Note that is not the fragment that is to be managed at run-time
	 * 
	 * @return
	 */
	@Override
	@Getter(value = FRAGMENT_KEY, isStringConvertable = true)
	@XMLAttribute
	public DocXFragment getFragment();

	/**
	 * Sets the represented fragment in the template resource<br>
	 * 
	 * @param fragment
	 */
	@Override
	@Setter(FRAGMENT_KEY)
	public void setFragment(DocXFragment fragment);

	public static abstract class DocXFragmentRoleImpl
			extends FlexoDocumentFragmentRoleImpl<DocXFragment, DocXDocument, DocXTechnologyAdapter> implements DocXFragmentRole {

		@Override
		public Type getType() {
			return DocXFragment.class;
		}

		@Override
		public boolean defaultBehaviourIsToBeDeleted() {
			return true;
		}

		@Override
		public RoleCloningStrategy defaultCloningStrategy() {
			return RoleCloningStrategy.Clone;
		}

		/*
		 * @Override public ActorReference<DocXFragment>
		 * makeActorReference(DocXFragment fragment, FlexoConceptInstance fci) {
		 * 
		 * VirtualModelInstanceModelFactory factory = fci.getFactory();
		 * FragmentActorReference<DocXFragment> returned =
		 * factory.newInstance(FragmentActorReference.class);
		 * returned.setFlexoRole(this); returned.setFlexoConceptInstance(fci);
		 * returned.setModellingElement(fragment); return returned;
		 * 
		 * }
		 */

		@Override
		public String getTypeDescription() {
			try {
				return TypeUtils.simpleRepresentation(getType()) + "(" + getFMLModelFactory().getStringEncoder().toString(getFragment())
						+ ")";
			} catch (InvalidDataException e) {
				return super.getTypeDescription();
			}
		}

		@Override
		public Class<? extends TechnologyAdapter> getRoleTechnologyAdapterClass() {
			return DocXTechnologyAdapter.class;
		}

		// TODO: this code is useless, but required by
		// DocXFMLModelFactoryIntegrationTest to pass
		// (issue with PAMELA checking)
		@Override
		public DocXFragment getFragment() {
			return (DocXFragment) performSuperGetter(FRAGMENT_KEY);
		}

		// TODO: this code is useless, but required by
		// DocXFMLModelFactoryIntegrationTest to pass
		// (issue with PAMELA checking)
		@Override
		public void setFragment(DocXFragment fragment) {
			performSuperSetter(FRAGMENT_KEY, fragment);
		}
	}
}
