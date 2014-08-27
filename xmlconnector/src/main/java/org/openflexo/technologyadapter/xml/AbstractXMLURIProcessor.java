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

import org.openflexo.foundation.ontology.DuplicateURIException;
import org.openflexo.foundation.technologyadapter.FlexoModelResource;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.view.ModelSlotInstance;
import org.openflexo.foundation.viewpoint.FMLRepresentationContext;
import org.openflexo.foundation.viewpoint.NamedViewPointObject;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Initializer;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Parameter;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.metamodel.XMLComplexType;
import org.openflexo.technologyadapter.xml.metamodel.XMLDataProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLObject;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.model.XMLPropertyValue;

/* Correct processing of XML Objects URIs needs to add an internal class to store
 * for each XMLComplexType wich are the XML Elements (attributes or CDATA, or...) that will be 
 * used to calculate URIs
 */

// TODO Manage the fact that URI May Change

@ModelEntity
@ImplementationClass(AbstractXMLURIProcessor.XMLURIProcessorImpl.class)
@XMLElement(xmlTag = "URIProcessor")
public interface AbstractXMLURIProcessor extends NamedViewPointObject {

	public enum MappingStyle {
		ATTRIBUTE_VALUE, SINGLETON;
	}

	@PropertyIdentifier(type = String.class)
	public static final String TYPE_URI_KEY       = "typeURI";
	@PropertyIdentifier(type = MappingStyle.class)
	public static final String MAPPING_STYLE_KEY  = "mappingStyle";
	@PropertyIdentifier(type = String.class)
	public static final String ATTRIBUTE_NAME_KEY = "attributeName";
	@PropertyIdentifier(type = XMLType.class)
	public static final String MAPPED_XMLTYPE = "mappedType";
	@PropertyIdentifier(type = ModelSlot.class)
	public static final String MODELSLOT = "modelSlot";
	@PropertyIdentifier(type = XMLDataProperty.class)
	public static final String BASE_PROPERTY = "basePropertyForURI";


	@Initializer
	public AbstractXMLURIProcessor init();

	@Initializer
	public AbstractXMLURIProcessor init(@Parameter(TYPE_URI_KEY) String typeURI);

	
	@Getter(value = TYPE_URI_KEY)
	@XMLAttribute
	public String _getTypeURI();

	@Setter(TYPE_URI_KEY)
	public void _setTypeURI(String typeURI);

	@Getter(value = MAPPING_STYLE_KEY)
	@XMLAttribute
	public MappingStyle getMappingStyle();

	@Setter(MAPPING_STYLE_KEY)
	public void setMappingStyle(MappingStyle mappingStyle);

	@Getter(value = ATTRIBUTE_NAME_KEY)
	@XMLAttribute
	public String _getAttributeName();

	@Setter(ATTRIBUTE_NAME_KEY)
	public void _setAttributeName(String attributeName);

	@Getter(MAPPED_XMLTYPE)
	public XMLType getMappedXMLType();

	@Setter(MAPPED_XMLTYPE)
	public void setMappedXMLType(XMLType mappedType);

	@Setter(MODELSLOT)
	public void setModelSlot(AbstractXMLModelSlot modelslot);

	@Getter(MODELSLOT)
	public AbstractXMLModelSlot getModelSlot();


	@Getter(BASE_PROPERTY)
	public XMLProperty getBasePropertyForURI();

	@Setter(BASE_PROPERTY)
	public void setBasePropertyForURI(XMLDataProperty basePropertyForURI);
		
	public Object retrieveObjectWithURI(ModelSlotInstance msInstance, String objectURI) throws DuplicateURIException;

	public String getURIForObject(ModelSlotInstance msInstance, XMLObject xsO);

	public void reset();

	/** 
	 * XSURIProcessor interface implementation
	 * @author xtof
	 *
	 */
	public static abstract class XMLURIProcessorImpl extends NamedViewPointObjectImpl implements AbstractXMLURIProcessor {

		static final Logger  logger   = Logger.getLogger(AbstractXMLURIProcessor.class.getPackage().getName());

		// Properties used to calculate URIs
		private XMLType mappedXMLType;        
		private XMLDataProperty baseDataPropertyForURI;

		// Serialized properties

		protected URI typeURI;
		protected String attributeName;

		// Cache des URis Pour aller plus vite ??
		// TODO some optimization required
		private final Map<String, XMLObject> uriCache = new HashMap<String, XMLObject>();


		/**
		 * initialises an URIProcessor with the given URI
		 * @param typeURI
		 */
		public XMLURIProcessorImpl() {
			super();
		}
		
		
		/**
		 * initialises an URIProcessor with the given URI
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
		public XMLProperty getBasePropertyForURI() {
			return baseDataPropertyForURI;
		}

		@Override
		public void setBasePropertyForURI(XMLDataProperty basePropertyForURI) {
			this.baseDataPropertyForURI = basePropertyForURI;
			if (this.baseDataPropertyForURI != null) {
				this._setAttributeName(basePropertyForURI.getName());
			}
		}


		// URI Calculation

		@Override
		public String getURIForObject(ModelSlotInstance msInstance, XMLObject xsO) {
			String builtURI = null;
			StringBuffer completeURIStr = new StringBuffer();

			// processor should be initialized
			if (getMappedXMLType() == null) {
				logger.warning("Cannot process URI as URIProcessor is not initialized for that class: " + typeURI);
				return null;
			}
			else {
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
							builtURI = null;
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

			// modelResource must also be loaded!

			FlexoModelResource<XMLModel, XMLMetaModel, XMLTechnologyAdapter> resource = (FlexoModelResource<XMLModel, XMLMetaModel, XMLTechnologyAdapter>) msInstance
					.getResource();

			// should not be a preoccupation of XSURI
			// if (!resource.isLoaded()) {
			// resource.getModelData();
			// }

			// retrieve object
			if (o == null) {

				if (getMappingStyle() == MappingStyle.ATTRIBUTE_VALUE && attributeName != null) {

					XMLProperty aProperty = ((XMLComplexType) getMappedXMLType()).getPropertyByName(attributeName);
					String attrValue = URI.create(objectURI).getQuery();

					for (XMLIndividual obj : resource.getModel().getIndividualsOfType(getMappedXMLType())) {

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

				}
				else if (getMappingStyle() == MappingStyle.SINGLETON) {
					List<?> indivList = ((XMLModel) msInstance.getAccessedResourceData()).getIndividualsOfType(getMappedXMLType());
					if (indivList.size() > 1) {
						throw new DuplicateURIException("Cannot process URI - Several individuals found for singleton of type "
								+ this._getTypeURI().toString());
					}
					else if (indivList.size() == 0) {
						logger.warning("Cannot find Singleton for type : " + this._getTypeURI().toString());
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
		public static String retrieveTypeURI(ModelSlotInstance msInstance, String objectURI) {

			URI fullURI;
			StringBuffer typeURIStr = new StringBuffer();

			fullURI = URI.create(objectURI);
			typeURIStr.append(fullURI.getScheme()).append("://").append(fullURI.getHost()).append(fullURI.getPath()).append("#")
			.append(fullURI.getFragment());

			return typeURIStr.toString();
		}

		@Override
		public String getURI() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ViewPoint getViewPoint() {
			if (getModelSlot() != null) {
				return getModelSlot().getViewPoint();
			}
			return null;
		}

		@Override
		public String getFMLRepresentation(FMLRepresentationContext context) {
			if (mappedXMLType != null){
				return "XSURIProcessor for " + this.mappedXMLType.getName();
			}
			else {
				return "";
			}
		}


	}
}
