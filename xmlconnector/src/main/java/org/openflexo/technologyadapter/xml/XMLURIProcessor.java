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
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
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
@ImplementationClass(XMLURIProcessor.XMLURIProcessorImpl.class)
public interface XMLURIProcessor extends AbstractXMLURIProcessor {

	/**
	 * XMLURIProcessor interface implementation
	 * 
	 * @author xtof
	 *
	 */
	public static abstract class XMLURIProcessorImpl extends AbstractXMLURIProcessorImpl implements XMLURIProcessor {

		static final Logger logger = Logger.getLogger(XMLURIProcessor.class.getPackage().getName());

		// Cache des URis Pour aller plus vite ??
		// TODO some optimization required
		private final Map<String, XMLObject> uriCache = new HashMap<String, XMLObject>();

		/**
		 * initialises an URIProcessor with the given URI
		 * 
		 * @param typeURI
		 */
		public XMLURIProcessorImpl() {
			super();
		}

		/**
		 * initialises an URIProcessor with the given URI
		 * 
		 * @param typeURI
		 */
		public XMLURIProcessorImpl(String typeURI) {
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

		@Override
		public void setTypeURI(String name) {
			if (name != null) {
				typeURI = URI.create(name);
				bindtypeURIToMappedType();
			} else
				typeURI = null;
		}

		@Override
		public String getTypeURI() {
			if (mappedXMLType != null) {
				String _uri = mappedXMLType.getURI();
				return _uri;
			} else if (typeURI != null) {
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
		public void setMappedXMLType(XMLType aType) {
			mappedXMLType = aType;
		}

		public void bindtypeURIToMappedType() {
			XMLModelSlot modelSlot = (XMLModelSlot) getModelSlot();
			if (modelSlot != null) {
				String mmURI = modelSlot.getMetaModelURI();
				if (mmURI != null) {
					// FIXME : to be re-factored
					XSDMetaModelResource mmResource = (XSDMetaModelResource) modelSlot.getMetaModelResource();
					if (mmResource != null && typeURI != null) {
						mappedXMLType = mmResource.getMetaModelData().getTypeFromURI(typeURI.toString());
						String attrName = getAttributeName();
						if (getMappingStyle() == MappingStyle.ATTRIBUTE_VALUE && attrName != null) {
							setBasePropertyForURI((XMLDataProperty) ((XMLComplexType) getMappedXMLType()).getPropertyByName(attrName));
						}
					} else {
						logger.warning("unable to map typeURI to an OntClass, as metaModelResource or Type URI  is Null ");
					}
				} else
					setMappedXMLType(null);
			}
		}

		// URI Calculation
		// TODO : manage the fact that URI might change:
		@Override
		public String getURIForObject(ModelSlotInstance msInstance, XMLObject xsO) {
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
			} else {
				String attrName = getAttributeName();
				if (getMappingStyle() == MappingStyle.ATTRIBUTE_VALUE && attrName != null && getMappedXMLType() != null) {

					XMLProperty aProperty = ((XMLComplexType) getMappedXMLType()).getPropertyByName(attrName);
					XMLPropertyValue value = ((XMLIndividual) xsO).getPropertyValue(aProperty);
					try {
						// NPE protection
						if (value != null) {
							builtURI = URLEncoder.encode(value.toString(), "UTF-8");
						} else {
							logger.severe("XSURI: unable to compute an URI for given object");
							builtURI = null;
						}
					} catch (UnsupportedEncodingException e) {
						logger.warning("Cannot process URI - Unexpected encoding error");
						e.printStackTrace();
					}
				} else if (getMappingStyle() == MappingStyle.SINGLETON) {
					try {
						builtURI = URLEncoder.encode(((XMLIndividual) xsO).getType().getURI(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						logger.warning("Cannot process URI - Unexpected encoding error");
						e.printStackTrace();
					}
				} else {
					logger.warning("Cannot process URI - Unexpected or Unspecified mapping parameters");
				}
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
		public Object retrieveObjectWithURI(ModelSlotInstance msInstance, String objectURI) throws DuplicateURIException {

			XMLObject o = uriCache.get(objectURI);

			// if processor not initialized
			if (getMappedXMLType() == null) {
				bindtypeURIToMappedType();
			}

			// should not be a preoccupation of XSURI
			// if (!resource.isLoaded()) {
			// resource.getModelData();
			// }

			// retrieve object
			if (o == null) {
				String attrName = getAttributeName();
				if (getMappingStyle() == MappingStyle.ATTRIBUTE_VALUE && attrName != null) {

					XMLProperty aProperty = ((XMLComplexType) getMappedXMLType()).getPropertyByName(attrName);
					String attrValue = URI.create(objectURI).getQuery();

					for (XMLIndividual obj : ((XMLModel) msInstance.getAccessedResourceData()).getIndividualsOfType(getMappedXMLType())) {

						XMLPropertyValue value = obj.getPropertyValue(aProperty);
						try {
							if (value.equals(URLDecoder.decode(attrValue, "UTF-8"))) {
								return obj;
							}
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} else if (getMappingStyle() == MappingStyle.SINGLETON) {
					List<?> indivList = ((XMLModel) msInstance.getAccessedResourceData()).getIndividualsOfType(getMappedXMLType());
					if (indivList.size() > 1) {
						throw new DuplicateURIException("Cannot process URI - Several individuals found for singleton of type "
								+ this.getTypeURI().toString());
					} else if (indivList.size() == 0) {
						logger.warning("Cannot find Singleton for type : " + this.getTypeURI().toString());
					} else {
						o = (XMLObject) indivList.get(0);
					}
				}
			} else {
				logger.warning("Cannot process URI - Unexpected or Unspecified mapping parameters");

			}

			return o;
		}

		// get the right URIProcessor for URI
		public static String retrieveTypeURI(ModelSlotInstance msInstance, String objectURI) {

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
				return "XMLURIProcessor for " + this.mappedXMLType.getName();
			} else {
				return "";
			}
		}

	}
}
