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

package org.openflexo.technologyadapter.xml.rm;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoFileResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.xml.XMLTechnologyContextManager;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XSDMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XSDMetaModelImpl;
import org.openflexo.technologyadapter.xml.model.XMLType;
import org.openflexo.technologyadapter.xml.model.XSOntologyURIDefinitions;
import org.openflexo.toolbox.IProgress;

import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSType;

/**
 * 
 * This class defines and implements the XSD MetaModel FileResource
 * 
 * @author sylvain, luka, Christophe
 * 
 */

public abstract class XSDMetaModelResourceImpl extends FlexoFileResourceImpl<XMLMetaModel> implements XSOntologyURIDefinitions,
XSDMetaModelResource {

	private static final Logger logger = Logger.getLogger(XSDMetaModelResourceImpl.class.getPackage().getName());

	// Properties

	private XSSchemaSet schemaSet;
	private XSDeclarationsFetcher fetcher;

	private boolean isLoaded = false;
	private boolean isLoading = false;
	private boolean isReadOnly = true;

	public static XSDMetaModelResource makeXSDMetaModelResource(File xsdMetaModelFile, String uri,
			XMLTechnologyContextManager technologyContextManager) {
		try {
			ModelFactory factory = new ModelFactory(XSDMetaModelResource.class);
			XSDMetaModelResource returned = factory.newInstance(XSDMetaModelResource.class);
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setURI(uri);
			returned.setName("Unnamed");
			returned.setFile(xsdMetaModelFile);

			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public XMLMetaModel getMetaModelData() {
		try {
			return getResourceData(null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
		} catch (FlexoException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Load the &quot;real&quot; load resource data of this resource.
	 * 
	 * @param progress
	 *            a progress monitor in case the resource data is not immediately available.
	 * @return the resource data.
	 * @throws ResourceLoadingCancelledException
	 */
	@Override
	public XMLMetaModel loadResourceData(IProgress progress) throws ResourceLoadingCancelledException {

		if (loadWhenUnloaded())
			return resourceData;
		else {
			logger.warn("Not able to load resource");
			return null;
		}
	}

	private static boolean mapsToClass(XSElementDecl element) {
		if (element.getType().isComplexType()) {
			return true;
		} else {
			return false;
		}
	}

	private void loadTypes() {
		// TODO if a declaration (base) type is derived, get the correct
		// superclass

		if (resourceData != null){
			for (XSComplexType complexType : fetcher.getComplexTypes()) {
				XMLType xsType = (XMLType) resourceData.getTypeFromURI(fetcher.getUri(complexType));
				if (xsType == null) {
					// create New XMLType if it does not exist
					xsType = (XMLType) resourceData.createNewType(fetcher.getUri(complexType), complexType.getName());
					xsType.setIsAbstract(true);
				}
				XSType btype = complexType.getBaseType();
				if (btype != null && !btype.getName().equalsIgnoreCase("anyType")) {
					XMLType superType = (XMLType) resourceData.getTypeFromURI(fetcher.getUri(btype));
					if (superType == null) {
						// create New Type if it does not exist
						superType = (XMLType) resourceData.createNewType(btype.getName(), fetcher.getUri(btype));
						xsType.setIsAbstract(true);
					}
					if (superType != null) {
						xsType.setSuperType(superType);

					}
				}
			}

			for (XSElementDecl element : fetcher.getElementDecls()) {
				if (mapsToClass(element)) {

					XMLType xsType = (XMLType) resourceData.createNewType(fetcher.getUri(element),element.getName());
					XSType type = element.getType();
					if (type != null) {
						XMLType superType = (XMLType) resourceData.getTypeFromURI(fetcher.getUri(type));
						if (superType != null)
							xsType.setSuperType(superType);
					}
				}
			}
		}
		else {
			logger.fatal("Cannot load Types as MetaModel (resourceData) is NULL");
		}
	}

	private void loadDataProperties() {

		/*
		 * for (XSSimpleType simpleType : fetcher.getSimpleTypes()) {
		 * XSOntDataProperty xsDataProperty = loadDataProperty(simpleType);
		 * xsDataProperty.setDataType(computeDataType(simpleType)); }
		 */
		/*
		 * S for (XSComplexType complexType : fetcher.getComplexTypes()) { if
		 * (complexType.isLocal()){ // TODO Manage Local Types XSElementDecl
		 * ownerElem = complexType.getScope(); XSOntClass xsClass =
		 * loadClass(ownerElem); xsClass.addToSuperClasses(getRootConcept()); }
		 * }
		 */

		for (XSElementDecl element : fetcher.getElementDecls()) {
			if (mapsToClass(element) == false) {
				String uri = fetcher.getUri(element);
				String ownerUri = fetcher.getOwnerURI(uri);

				if (ownerUri != null) {
					XMLType owner = (XMLType) resourceData.getTypeFromURI(ownerUri);
					if (owner != null) {
						// TODO: better manage types
						owner.createAttribute(element.getName());
					}
					else {
						logger.warn("unable to find an owner type for attribute: " + uri);
					}
				}
				else {
					logger.warn("unable to find an owner for : " + uri);
				}

			}
		}

		for (XSAttributeDecl attribute : fetcher.getAttributeDecls()) {
			String uri = fetcher.getUri(attribute);

			String ownerUri = fetcher.getOwnerURI(uri);

			if (ownerUri != null) {
				XMLType owner = (XMLType) resourceData.getTypeFromURI(ownerUri);
				if (owner != null) {
					// TODO: better manage types
					owner.createAttribute(attribute.getName());
				}
				else {
					logger.warn("unable to find an owner type for attribute: " + uri);
				}
			}
			else {
				logger.warn("unable to find an owner for : " + uri);
			}
		}
	}

		/*

	private void loadObjectProperties() {

		XMLMetaModel aModel = resourceData; // Was: getMetaModelData(); is there a reason ????

		for (XSElementDecl element : fetcher.getElementDecls()) {
			if (mapsToClass(element)) {
				String uri = fetcher.getUri(element);
				XSOntClass c = aModel.getClass(fetcher.getUri(element));
				String name = element.getName();
				XSOntObjectProperty xsObjectProperty = aModel.createObjectProperty(name, fetcher.getNamespace(element) + "#" + name, c);
				addDomainIfPossible(xsObjectProperty, uri, aModel);
			}
		}

	}
		 */
		public boolean load() {

			if (resourceData == null) {
				this.resourceData =  XSDMetaModelImpl.getModelFactory().newInstance(XSDMetaModel.class);
				resourceData.getResource();
				resourceData.setResource(this);
			}

			if (isLoading() == true) {
				return false;
			}
			isLoading = true;
			isLoaded = false;
			schemaSet = XSOMUtils.read(getFile());
			if (schemaSet != null) {
				fetcher = new XSDeclarationsFetcher();
				fetcher.fetch(schemaSet);
				loadTypes();
				loadDataProperties();
				//			loadObjectProperties();
				isLoaded = true;
			} else
				logger.info("I've not been able to parse the file" + getFile());
			isLoading = false;
			return isLoaded;
		}

		public boolean loadWhenUnloaded() {
			if (isLoaded() == false) {
				return load();
			}
			return true;
		}

		@Override
		public boolean isLoaded() {
			return isLoaded;
		}

		@Override
		public boolean isLoading() {
			return isLoading;
		}

		public boolean getIsReadOnly() {
			return isReadOnly;
		}

		public void setReadOnly(boolean isReadOnly) {
			this.isReadOnly = isReadOnly;
		}

		// TODO : pas propre, a traiter rapidement

		public XSDeclarationsFetcher getFetcher() {
			return fetcher;
		}

		/**
		 * Save the &quot;real&quot; resource data of this resource.
		 */
		@Override
		public void save(IProgress progress) {
			logger.info("Not implemented yet");
		}

		@Override
		public Class<XMLMetaModel> getResourceDataClass() {
			return XMLMetaModel.class;
		}

	}
