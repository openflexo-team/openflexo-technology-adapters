/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Excelconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel.fml;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.model.ExcelObject;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;

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
@FML("ExcelActorReference")
public interface ExcelActorReference<T extends ExcelObject> extends ActorReference<T> {

	@PropertyIdentifier(type = String.class)
	public static final String OBJECT_URI_KEY = "objectURI";

	@Getter(value = OBJECT_URI_KEY)
	@XMLAttribute
	public String getObjectURI();

	@Setter(OBJECT_URI_KEY)
	public void setObjectURI(String objectURI);

	public abstract static class ExcelActorReferenceImpl<T extends ExcelObject> extends ActorReferenceImpl<T>
			implements ExcelActorReference<T> {

		private static final Logger logger = FlexoLogger.getLogger(ExcelActorReference.class.getPackage().toString());

		private T object;
		private String objectURI;

		/**
		 * Default constructor
		 */
		public ExcelActorReferenceImpl() {
			super();
		}

		public ExcelWorkbook getExcelWorkbook() {
			if (getExcelWorkbookResource() != null) {
				try {
					return getExcelWorkbookResource().getResourceData(null);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ResourceLoadingCancelledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FlexoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		public ExcelWorkbookResource getExcelWorkbookResource() {
			ModelSlotInstance<?, ?> msInstance = getModelSlotInstance();
			if (msInstance != null && msInstance.getResource() instanceof ExcelWorkbookResource) {
				return (ExcelWorkbookResource) msInstance.getResource();
			}
			return null;
		}

		@Override
		public T getModellingElement(boolean forceLoading) {
			if (object == null && objectURI != null) {
				System.out.println("Tiens j'essaie de trouver l'objet avec l'URI " + objectURI);
				System.out.println("msInstance=" + getModelSlotInstance());
				System.out.println("msInstance.getModelSlot()=" + getModelSlotInstance().getModelSlot());
				System.out.println("ExcelWorkbookResource=" + getExcelWorkbookResource());
				ExcelWorkbookResource res = getExcelWorkbookResource();
				object = (T) res.getConverter().fromSerializationIdentifier(objectURI);
				System.out.println("je trouve " + object);

				/*if (msInstance != null && msInstance.getAccessedResourceData() != null) {
					object = (T) msInstance.getModelSlot().retrieveObjectWithURI(msInstance, objectURI);
				}
				else {
					logger.warning("Could not access to model in model slot " + getModelSlotInstance());
				}*/
			}
			if (object == null) {
				logger.warning("Could not retrieve object " + objectURI);
			}
			return object;

		}

		@Override
		public void setModellingElement(T object) {
			this.object = object;
			if (object != null) {
				objectURI = object.getSerializationIdentifier();
			}
		}

		@Override
		public String getObjectURI() {
			if (object != null) {
				return object.getSerializationIdentifier();
			}
			return objectURI;
		}

		@Override
		public void setObjectURI(String objectURI) {
			this.objectURI = objectURI;
		}

	}

}
