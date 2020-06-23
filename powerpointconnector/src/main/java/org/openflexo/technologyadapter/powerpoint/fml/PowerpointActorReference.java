/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Powerpointconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.powerpoint.fml;

import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
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

	public static abstract class PowerpointActorReferenceImpl<T extends PowerpointObject> extends ActorReferenceImpl<T>
			implements PowerpointActorReference<T> {

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
		public T getModellingElement(boolean forceLoading) {
			if (object == null) {
				ModelSlotInstance msInstance = getModelSlotInstance();
				if (msInstance.getAccessedResourceData() != null) {
					/** Model Slot is responsible for URI mapping */
					object = (T) msInstance.getModelSlot().retrieveObjectWithURI(msInstance.getAccessedResourceData(), objectURI);
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
				objectURI = msInstance.getModelSlot().getURIForObject(msInstance.getAccessedResourceData(), object);
			}
		}

		@Override
		public String getObjectURI() {
			if (object != null) {
				ModelSlotInstance msInstance = getModelSlotInstance();
				objectURI = msInstance.getModelSlot().getURIForObject(msInstance.getAccessedResourceData(), object);
			}
			return objectURI;
		}

		@Override
		public void setObjectURI(String objectURI) {
			this.objectURI = objectURI;
		}

	}

}
