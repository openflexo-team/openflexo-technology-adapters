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

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.openflexo.antar.binding.BindingFactory;
import org.openflexo.antar.binding.BindingModel;
import org.openflexo.foundation.FlexoProperty;
import org.openflexo.foundation.fml.AbstractVirtualModel;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModelObject;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.ontology.DuplicateURIException;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Initializer;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Parameter;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.technologyadapter.xml.metamodel.XMLDataProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLObject;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;

/* Correct processing of XML Objects URIs needs to add an internal class to store
 * for each XMLComplexType wich are the XML Elements (attributes or CDATA, or...) that will be 
 * used to calculate URIs
 */

// TODO Manage the fact that URI May Change

@ModelEntity(isAbstract = true)
@ImplementationClass(AbstractXMLURIProcessor.AbstractXMLURIProcessorImpl.class)
public interface AbstractXMLURIProcessor extends VirtualModelObject {

	public enum MappingStyle {
		ATTRIBUTE_VALUE, SINGLETON;
	}

	@PropertyIdentifier(type = String.class)
	public static final String TYPE_URI_KEY = "typeURI";
	@PropertyIdentifier(type = MappingStyle.class)
	public static final String MAPPING_STYLE_KEY = "mappingStyle";
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
	public String getTypeURI();

	@Setter(TYPE_URI_KEY)
	public void setTypeURI(String typeURI);

	@Getter(value = MAPPING_STYLE_KEY)
	@XMLAttribute
	public MappingStyle getMappingStyle();

	@Setter(MAPPING_STYLE_KEY)
	public void setMappingStyle(MappingStyle mappingStyle);

	@Getter(value = ATTRIBUTE_NAME_KEY)
	@XMLAttribute
	public String getAttributeName();

	@Setter(ATTRIBUTE_NAME_KEY)
	public void setAttributeName(String attributeName);

	@Getter(MAPPED_XMLTYPE)
	public XMLType getMappedXMLType();

	@Setter(MAPPED_XMLTYPE)
	public void setMappedXMLType(XMLType mappedType);

	@Setter(MODELSLOT)
	public void setModelSlot(AbstractXMLModelSlot modelslot);

	@Getter(MODELSLOT)
	public AbstractXMLModelSlot getModelSlot();

	@Getter(BASE_PROPERTY)
	public XMLDataProperty getBasePropertyForURI();

	@Setter(BASE_PROPERTY)
	public void setBasePropertyForURI(XMLDataProperty basePropertyForURI);

	public Object retrieveObjectWithURI(ModelSlotInstance msInstance, String objectURI) throws DuplicateURIException;

	public String getURIForObject(ModelSlotInstance msInstance, XMLObject xsO);

	public void reset();

	/**
	 * XSURIProcessor interface implementation
	 * 
	 * @author xtof
	 *
	 */
	public static abstract class AbstractXMLURIProcessorImpl extends FlexoConceptObjectImpl implements AbstractXMLURIProcessor {

		static final Logger logger = Logger.getLogger(AbstractXMLURIProcessor.class.getPackage().getName());

		// Properties used to calculate URIs

		protected URI typeURI;
		protected XMLType mappedXMLType;

		protected String attributeName;
		protected XMLDataProperty baseDataPropertyForURI;

		// Cache des URis Pour aller plus vite ??
		// TODO some optimization required
		private final Map<String, XMLObject> uriCache = new HashMap<String, XMLObject>();

		/**
		 * initialises an URIProcessor with the given URI
		 * 
		 * @param typeURI
		 */
		public AbstractXMLURIProcessorImpl() {
			super();
		}

		/**
		 * initialises an URIProcessor with the given URI
		 * 
		 * @param typeURI
		 */
		public AbstractXMLURIProcessorImpl(String typeURI) {
			super();

			if (typeURI != null) {
				this.typeURI = URI.create(typeURI);
			}
		}

		@Override
		public FlexoConcept getFlexoConcept() {
			return getModelSlot().getFlexoConcept();
		}

		@Override
		public AbstractVirtualModel<?> getVirtualModel() {
			return getModelSlot().getVirtualModel();
		}

		@Override
		public String getTypeURI() {
			return this.typeURI.toString();
		}

		@Override
		public void setTypeURI(String typeURI) {
			this.typeURI = URI.create(typeURI);
		}

		// Lifecycle management methods
		@Override
		public void reset() {
			setModelSlot(null);
			setMappingStyle(null);
			setBasePropertyForURI(null);
		}

		@Override
		public String getAttributeName() {
			if (baseDataPropertyForURI != null) {
				return baseDataPropertyForURI.getName();
			} else {
				return attributeName;
			}
		}

		@Override
		public void setAttributeName(String aName) {
			attributeName = aName;
			if (aName != null && mappedXMLType != null) {
				FlexoProperty dataP = mappedXMLType.getPropertyNamed(aName);
				attributeName = aName;
				if (dataP != null) {
					baseDataPropertyForURI = (XMLDataProperty) dataP;
				} else {
					logger.warning("Unable to set attribute name for uri processor : property not found in XMLType "
							+ mappedXMLType.getName());
				}
			} else
				logger.warning("Unable to set attribute name for uri processor : null XMLType ");

		}

		@Override
		public XMLDataProperty getBasePropertyForURI() {
			return baseDataPropertyForURI;
		}

		@Override
		public void setBasePropertyForURI(XMLDataProperty basePropertyForURI) {
			this.baseDataPropertyForURI = basePropertyForURI;
			if (this.baseDataPropertyForURI != null) {
				attributeName = basePropertyForURI.getName();
			}
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
			return "URIProcessor/" + this.getFlexoID();
		}

		@Override
		public BindingModel getBindingModel() {
			return null;
		}

		@Override
		public BindingFactory getBindingFactory() {
			if (getModelSlot() != null) {
				return getModelSlot().getBindingFactory();
			}
			return null;
		}

	}
}
