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

package org.openflexo.technologyadapter.powerpoint;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.openflexo.connie.BindingModel;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptObject;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.pamela.validation.Validable;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointObject;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlideshow;

@ModelEntity
@ImplementationClass(BasicPowerpointModelSlotURIProcessor.BasicPowerpointModelSlotURIProcessorImpl.class)
@XMLElement
public interface BasicPowerpointModelSlotURIProcessor extends FlexoConceptObject {

	public String getURIForObject(PowerpointSlideshow resourceData, PowerpointObject powerpointObject);

	// get the Object given the URI
	public Object retrieveObjectWithURI(PowerpointSlideshow resourceData, String objectURI) throws Exception;

	public BasicPowerpointModelSlot getModelSlot();

	public void setModelSlot(BasicPowerpointModelSlot powerpointModelSlot);

	public static abstract class BasicPowerpointModelSlotURIProcessorImpl extends FlexoConceptObjectImpl
			implements BasicPowerpointModelSlotURIProcessor {

		private static final Logger logger = Logger.getLogger(BasicPowerpointModelSlotURIProcessor.class.getPackage().getName());

		// Properties actually used to calculate URis
		private BasicPowerpointModelSlot modelSlot;

		// Cache des URis Pour aller plus vite ??
		// TODO some optimization required
		private final Map<String, PowerpointObject> uriCache = new HashMap<>();

		public BasicPowerpointModelSlotURIProcessorImpl(String typeURI) {
			super();
			this.typeURI = URI.create(typeURI);
		}

		@Override
		public FlexoConcept getFlexoConcept() {
			return getModelSlot().getFlexoConcept();
		}

		@Override
		public BasicPowerpointModelSlot getModelSlot() {
			return modelSlot;
		}

		@Override
		public void setModelSlot(BasicPowerpointModelSlot powerpointModelSlot) {
			modelSlot = powerpointModelSlot;
		}

		// Serialized properties

		protected URI typeURI;
		protected String attributeName;

		public String _getTypeURI() {
			return typeURI.toString();
		}

		public void _setTypeURI(String name) {
			typeURI = URI.create(name);
		}

		public String _getAttributeName() {
			return attributeName;
		}

		public void _setAttributeName(String attributeName) {
			this.attributeName = attributeName;
		}

		// Lifecycle management methods
		public void reset() {
			modelSlot = null;
		}

		// URI Calculation

		@Override
		public String getURIForObject(PowerpointSlideshow resourceData, PowerpointObject powerpointObject) {
			String builtURI = null;

			try {
				builtURI = URLEncoder.encode(powerpointObject.getUri(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.warning("Cannot process URI - Unexpected encoding error");
				e.printStackTrace();
			}

			if (builtURI != null) {
				if (uriCache.get(builtURI) == null) {
					// TODO Manage the fact that URI May Change
					uriCache.put(builtURI, powerpointObject);
				}
			}
			return builtURI.toString();
		}

		// get the Object given the URI
		@Override
		public Object retrieveObjectWithURI(PowerpointSlideshow resourceData, String objectURI) throws Exception {
			PowerpointObject o = uriCache.get(objectURI);
			return o;
		}

		@Override
		public BindingModel getBindingModel() {
			return null;
		}

		@Override
		public Collection<Validable> getEmbeddedValidableObjects() {
			return Collections.emptyList();
		}

	}
}
