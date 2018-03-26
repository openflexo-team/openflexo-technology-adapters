/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Xmlconnector, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.technologyadapter.xml.fml;

import java.lang.reflect.Type;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.AbstractVirtualModelInstanceModelFactory;
import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;

/**
 * @author xtof
 * 
 */
@ModelEntity
@XMLElement
@ImplementationClass(XMLIndividualRole.XMLIndividualRoleImpl.class)
@FML("XMLIndividualRole")
public interface XMLIndividualRole extends FlexoRole<XMLIndividual> {

	@PropertyIdentifier(type = String.class)
	public static final String INDIVIDUAL_URI_KEY = "individualURI";

	@Getter(value = INDIVIDUAL_URI_KEY)
	@XMLAttribute
	public String getIndividualURI();

	@Setter(INDIVIDUAL_URI_KEY)
	public void setIndividualURI(String conceptURI);

	public XMLTechnologyAdapter getXMLTechnologyAdapter();

	public static abstract class XMLIndividualRoleImpl extends FlexoRoleImpl<XMLIndividual> implements XMLIndividualRole {

		private String individualURI;

		@Override
		public XMLTechnologyAdapter getXMLTechnologyAdapter() {
			return (XMLTechnologyAdapter) getModelSlot().getModelSlotTechnologyAdapter();
		}

		@Override
		public Type getType() {
			return XMLIndividual.class;
		}

		@Override
		public String getTypeDescription() {
			return XMLIndividual.class.getSimpleName();
		}

		/**
		 * Encodes the default cloning strategy
		 * 
		 * @return
		 */
		@Override
		public RoleCloningStrategy defaultCloningStrategy() {
			return RoleCloningStrategy.Clone;
		}

		@Override
		public boolean defaultBehaviourIsToBeDeleted() {
			return true;
		}

		@Override
		public ActorReference<XMLIndividual> makeActorReference(XMLIndividual object, FlexoConceptInstance fci) {
			AbstractVirtualModelInstanceModelFactory<?> factory = fci.getFactory();
			XMLActorReference<XMLIndividual> returned = factory.newInstance(XMLActorReference.class);
			returned.setFlexoRole(this);
			returned.setFlexoConceptInstance(fci);
			returned.setModellingElement(object);
			return returned;
		}

		@Override
		public String getIndividualURI() {
			return individualURI;
		}

		@Override
		public void setIndividualURI(String conceptURI) {
			this.individualURI = conceptURI;
		}

		/*@Override
		public String getFMLRepresentation(FMLRepresentationContext context) {
			FMLRepresentationOutput out = new FMLRepresentationOutput(context);
			out.append("XMLIndividualRole " + getName() + " as Individual conformTo " + getTypeDescription() + " from "
					+ getModelSlot().getName() + " ;", context);
			return out.toString();
		}*/

		@Override
		public Class<? extends TechnologyAdapter> getRoleTechnologyAdapterClass() {
			return XMLTechnologyAdapter.class;
		}

	}

}
