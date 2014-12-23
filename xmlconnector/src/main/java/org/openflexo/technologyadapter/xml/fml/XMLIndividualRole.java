/*
 * (c) Copyright 2010-2012 AgileBirds
 * (c) Copyright 2012-2014 Openflexo
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
package org.openflexo.technologyadapter.xml.fml;

import java.lang.reflect.Type;

import org.openflexo.foundation.fml.FMLRepresentationContext;
import org.openflexo.foundation.fml.FMLRepresentationContext.FMLRepresentationOutput;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceModelFactory;
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
		public String getPreciseType() {
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
		public ActorReference<XMLIndividual> makeActorReference(XMLIndividual object, FlexoConceptInstance epi) {
			VirtualModelInstanceModelFactory factory = epi.getFactory();
			XMLActorReference<XMLIndividual> returned = factory.newInstance(XMLActorReference.class);
			returned.setFlexoRole(this);
			returned.setFlexoConceptInstance(epi);
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

		@Override
		public String getFMLRepresentation(FMLRepresentationContext context) {
			FMLRepresentationOutput out = new FMLRepresentationOutput(context);
			out.append("XMLIndividualRole " + getName() + " as Individual conformTo " + getPreciseType() + " from "
					+ getModelSlot().getName() + " ;", context);
			return out.toString();
		}

	}

}
