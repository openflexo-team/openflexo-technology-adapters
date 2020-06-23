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

package org.openflexo.technologyadapter.xml;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FMLRepresentationContext;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.ontology.DuplicateURIException;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.metamodel.XMLComplexType;
import org.openflexo.technologyadapter.xml.metamodel.XMLDataProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLObject;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.model.XMLPropertyValue;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelResource;

/* Correct processing of XML Objects URIs needs to add an internal class to store
 * for each XMLComplexType wich are the XML Elements (attributes or CDATA, or...) that will be 
 * used to calculate URIs
 */

// TODO Manage the fact that URI May Change

@ModelEntity
@XMLElement
@ImplementationClass(FreeXMLURIProcessor.FreeXMLURIProcessorImpl.class)
public interface FreeXMLURIProcessor extends AbstractXMLURIProcessor {

	public static abstract class FreeXMLURIProcessorImpl extends AbstractXMLURIProcessorImpl implements FreeXMLURIProcessor {

		static final Logger logger = Logger.getLogger(FreeXMLURIProcessor.class.getPackage().getName());

		// Properties used to calculate URIs
		private XMLType mappedXMLType;

		// Cache des URis Pour aller plus vite ??
		// TODO some optimization required
		private final Map<String, XMLObject> uriCache = new HashMap<>();

		/**
		 * initialises an URIProcessor with the given URI
		 * 
		 * @param typeURI
		 */
		public FreeXMLURIProcessorImpl() {
			super();
		}

		/**
		 * initialises an URIProcessor with the given URI
		 * 
		 * @param typeURI
		 */
		public FreeXMLURIProcessorImpl(String typeURI) {
			super();
			if (typeURI != null) {
				this.typeURI = URI.create(typeURI);
			}
		}

		// Lifecycle management methods
		@Override
		public void reset() {
			setModelSlot(null);
			setMappedXMLType(null);
			setMappingStyle(null);
			setBasePropertyForURI(null);
		}

		// TODO WARNING!!! Pb avec les typeURI....
		@Override
		public void setTypeURI(String name) {
			typeURI = URI.create(name);
			bindtypeURIToMappedType();
		}

		@Override
		public String getTypeURI() {
			if (mappedXMLType != null) {
				// FIXME : update _typeURI si on supprime le champs...
				// Parce que mappedClass doit rester prioritaire partout.
				return mappedXMLType.getURI();
			}
			this.bindtypeURIToMappedType();
			if (typeURI != null) {
				return typeURI.toString();
			}
			return null;
		}

		@Override
		public XMLType getMappedXMLType() {
			if (mappedXMLType == null && typeURI != null) {
				bindtypeURIToMappedType();
			}
			return mappedXMLType;
		}

		@Override
		public void setMappedXMLType(XMLType mappedClass) {
			this.mappedXMLType = mappedClass;
			if (mappedClass != null && !mappedClass.getURI().equals(getTypeURI())) {
				setTypeURI(mappedClass.getURI());
			}
			setChanged();
			notifyObservers();
		}

		public void bindtypeURIToMappedType() {
			AbstractXMLModelSlot<?> modelSlot = getModelSlot();
			if (modelSlot != null) {
				// TODO adapt this to the FreeXML case
				// FIXME
				// String mmURI = modelSlot.getMetaModelURI();
				String mmURI = null;
				if (mmURI != null) {
					// FIXME : to be re-factored
					// XSDMetaModelResource mmResource = (XSDMetaModelResource) modelSlot.getMetaModelResource();
					XSDMetaModelResource mmResource = null;
					String attributeName = getAttributeName();
					if (mmResource != null) {
						setMappedXMLType(mmResource.getMetaModelData().getTypeFromURI(typeURI.toString()));
						if (getMappingStyle() == MappingStyle.ATTRIBUTE_VALUE && attributeName != null) {
							setBasePropertyForURI((XMLDataProperty) ((XMLComplexType) getMappedXMLType()).getPropertyByName(attributeName));
						}
					}
					else {
						logger.warning("unable to map typeURI to an OntClass, as metaModelResource is Null ");
					}
				}
				else
					setMappedXMLType(null);
			}
			else {
				logger.warning("unable to map typeURI to an OntClass, as modelSlot is Null ");
			}
		}

		// URI Calculation

		@Override
		public String getURIForObject(XMLModel model, XMLObject xsO) {
			String builtURI = null;
			StringBuffer completeURIStr = new StringBuffer();

			// if processor not initialized
			if (getMappedXMLType() == null) {
				bindtypeURIToMappedType();
			}
			// processor should be initialized
			if (getMappedXMLType() == null) {
				logger.warning("Cannot process URI as URIProcessor is not initialized for that class: " + typeURI);
				return null;
			}

			String attributeName = getAttributeName();
			if (getMappingStyle() == MappingStyle.ATTRIBUTE_VALUE && attributeName != null && getMappedXMLType() != null) {

				XMLProperty aProperty = ((XMLComplexType) getMappedXMLType()).getPropertyByName(attributeName);
				XMLPropertyValue value = ((XMLIndividual) xsO).getPropertyValue(aProperty);
				try {
					// NPE protection
					if (value != null) {
						builtURI = URLEncoder.encode(value.toString(), "UTF-8");
					}
					else {
						logger.severe("XSURI: unable to compute an URI for given object");
					}
				} catch (UnsupportedEncodingException e) {
					logger.warning("Cannot process URI - Unexpected encoding error");
					e.printStackTrace();
				}
			}
			else if (getMappingStyle() == MappingStyle.SINGLETON) {
				try {
					builtURI = URLEncoder.encode(((XMLIndividual) xsO).getType().getURI(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					logger.warning("Cannot process URI - Unexpected encoding error");
					e.printStackTrace();
				}
			}
			else {
				logger.warning("Cannot process URI - Unexpected or Unspecified mapping parameters");
			}

			if (builtURI != null) {
				if (uriCache.get(builtURI) == null) {
					// TODO Manage the fact that URI May Change
					uriCache.put(builtURI, xsO);
				}
			}
			completeURIStr.append(typeURI.getScheme()).append("://").append(typeURI.getHost()).append(typeURI.getPath()).append("?")
					.append(builtURI).append("#").append(typeURI.getFragment());
			return completeURIStr.toString();
		}

		// get the Object given the URI

		@Override
		public Object retrieveObjectWithURI(XMLModel model, String objectURI) throws DuplicateURIException {

			XMLObject o = uriCache.get(objectURI);

			// if processor not initialized
			if (getMappedXMLType() == null) {
				bindtypeURIToMappedType();
			}

			// retrieve object
			if (o == null) {

				String attributeName = getAttributeName();
				if (getMappingStyle() == MappingStyle.ATTRIBUTE_VALUE && attributeName != null) {

					XMLProperty aProperty = ((XMLComplexType) getMappedXMLType()).getPropertyByName(attributeName);
					String attrValue = URI.create(objectURI).getQuery();

					for (XMLIndividual obj : model.getIndividualsOfType(getMappedXMLType())) {

						XMLPropertyValue value = obj.getPropertyValue(aProperty);
						try {
							if (value.equals(URLDecoder.decode(attrValue, "UTF-8"))) {
								return obj;
							}
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}

				}
				else if (getMappingStyle() == MappingStyle.SINGLETON) {
					List<?> indivList = model.getIndividualsOfType(getMappedXMLType());
					if (indivList.size() > 1) {
						throw new DuplicateURIException(
								"Cannot process URI - Several individuals found for singleton of type " + this.getTypeURI().toString());
					}
					else if (indivList.size() == 0) {
						logger.warning("Cannot find Singleton for type : " + this.getTypeURI().toString());
					}
					else {
						o = (XMLObject) indivList.get(0);
					}
				}
			}
			else {
				logger.warning("Cannot process URI - Unexpected or Unspecified mapping parameters");

			}

			return o;
		}

		// get the right URIProcessor for URI
		public static String retrieveTypeURI(ModelSlotInstance<?, ?> msInstance, String objectURI) {
			URI fullURI;
			StringBuffer typeURIStr = new StringBuffer();

			fullURI = URI.create(objectURI);
			typeURIStr.append(fullURI.getScheme()).append("://").append(fullURI.getHost()).append(fullURI.getPath()).append("#")
					.append(fullURI.getFragment());

			return typeURIStr.toString();
		}

		@Override
		public String getFMLRepresentation(FMLRepresentationContext context) {
			if (mappedXMLType != null) {
				return "FreeXMLURIProcessor for " + this.mappedXMLType.getName();
			}
			return "";
		}
	}
}
