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
package org.openflexo.technologyadapter.excel.viewpoint;

import java.util.logging.Logger;

import org.openflexo.foundation.view.ActorReference;
import org.openflexo.foundation.view.ModelSlotInstance;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.model.ExcelObject;

/**
 * Implements {@link ActorReference} for {@link ExcelObject} as modelling elements.<br>
 * 
 * @author sylvain
 * 
 * @param <T>
 *            type of referenced object
 */
@ModelEntity
@ImplementationClass(ExcelActorReference.ExcelActorReferenceImpl.class)
@XMLElement
public interface ExcelActorReference<T extends ExcelObject> extends ActorReference<T> {

	@PropertyIdentifier(type = String.class)
	public static final String OBJECT_URI_KEY = "objectURI";

	@Getter(value = OBJECT_URI_KEY)
	@XMLAttribute
	public String getObjectURI();

	@Setter(OBJECT_URI_KEY)
	public void setObjectURI(String objectURI);

	public abstract static class ExcelActorReferenceImpl<T extends ExcelObject> extends ActorReferenceImpl<T> implements
			ExcelActorReference<T> {

		private static final Logger logger = FlexoLogger.getLogger(ExcelActorReference.class.getPackage().toString());

		private T object;
		private String objectURI;

		/**
		 * Default constructor
		 */
		public ExcelActorReferenceImpl() {
			super();
		}

		@Override
		public T getModellingElement() {
			if (object == null) {
				ModelSlotInstance msInstance = getModelSlotInstance();
				if (msInstance != null && msInstance.getAccessedResourceData() != null) {
					/** Model Slot is responsible for URI mapping */
					object = (T) msInstance.getModelSlot().retrieveObjectWithURI(msInstance, objectURI);
				}
				else {
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
