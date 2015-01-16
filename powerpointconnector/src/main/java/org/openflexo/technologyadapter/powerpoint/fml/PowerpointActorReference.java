/*
 * (c) Copyright 2010-2012 AgileBirds
 * (c) Copyright 2012-2013 Openflexo
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
package org.openflexo.technologyadapter.powerpoint.fml;

import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointObject;

@FML("PowerpointActorReference")
public interface PowerpointActorReference<T extends PowerpointObject> extends ActorReference<T> {

	@PropertyIdentifier(type = String.class)
	public static final String OBJECT_URI_KEY = "objectURI";

	@Getter(value = OBJECT_URI_KEY)
	@XMLAttribute
	public String getObjectURI();

	@Setter(OBJECT_URI_KEY)
	public void setObjectURI(String objectURI);

	public static abstract class PowerpointActorReferenceImpl<T extends PowerpointObject> extends ActorReferenceImpl<T> implements
			PowerpointActorReference<T> {

		private static final Logger logger = FlexoLogger.getLogger(PowerpointActorReferenceImpl.class.getPackage().toString());

		private T object;
		private String objectURI;

		/**
		 * Default constructor
		 */
		public PowerpointActorReferenceImpl() {
			super();
		}

		@Override
		public T getModellingElement() {
			if (object == null) {
				ModelSlotInstance msInstance = getModelSlotInstance();
				if (msInstance.getAccessedResourceData() != null) {
					/** Model Slot is responsible for URI mapping */
					object = (T) msInstance.getModelSlot().retrieveObjectWithURI(msInstance, objectURI);
				} else {
					logger.warning("Could not access to model in model slot " + getModelSlotInstance());
				}
			}
			if (object == null) {
				logger.warning("Could not retrieve object " + objectURI);
			}
			return object;

		}

		@Override
		public void setModellingElement(T object) {
			this.object = object;
			if (object != null && getModelSlotInstance() != null) {
				ModelSlotInstance msInstance = getModelSlotInstance();
				/** Model Slot is responsible for URI mapping */
				objectURI = msInstance.getModelSlot().getURIForObject(msInstance, object);
			}
		}

		@Override
		public String getObjectURI() {
			if (object != null) {
				ModelSlotInstance msInstance = getModelSlotInstance();
				objectURI = msInstance.getModelSlot().getURIForObject(msInstance, object);
			}
			return objectURI;
		}

		@Override
		public void setObjectURI(String objectURI) {
			this.objectURI = objectURI;
		}

	}

}
