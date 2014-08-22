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
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoFileResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.xml.XMLTechnologyContextManager;
import org.openflexo.technologyadapter.xml.metamodel.XMLComplexType;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.metamodel.XSDMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XSDMetaModelImpl;
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

public abstract class XSDMetaModelResourceImpl extends FlexoFileResourceImpl<XMLMetaModel>  implements XSDMetaModelResource {

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
			logger.warning("Not able to load resource");
			return null;
		}
	}

	private void loadTypes() {
		// TODO if a declaration (base) type is derived, get the correct
		// superclass

		
		if (resourceData != null){
			for (XSComplexType complexType : fetcher.getComplexTypes()) {
				
				XMLType xsType = resourceData.getTypeFromURI(fetcher.getUri(complexType));
				
				if (xsType == null) {
					// create New XMLComplexeType as it does not exist
					xsType = resourceData.createNewType(fetcher.getUri(complexType), complexType.getName(),false);
					xsType.setIsAbstract(true);
				}
				
				XSType btype = complexType.getBaseType();
				
				if (btype != null && !btype.getName().equalsIgnoreCase("anyType")) {
					XMLType superType = resourceData.getTypeFromURI(fetcher.getUri(btype));
					if (superType == null) {
						// create New Type if it does not exist
						superType = resourceData.createNewType(btype.getName(), fetcher.getUri(btype),false);
						xsType.setIsAbstract(true);
					}
					if (superType != null) {
						xsType.setSuperType(superType);

					}
				}
			}

			// Creates complex types that come with complex Element declarations
			
			for (XSElementDecl element : fetcher.getElementDecls()) {
				if (element.getType().isComplexType()) {
					XMLType xsType = resourceData.createNewType(fetcher.getUri(element),element.getName(),false);
					XSType type = element.getType();
					if (type != null) {
						XMLType superType = resourceData.getTypeFromURI(fetcher.getUri(type));
						if (superType != null)
							xsType.setSuperType(superType);
					}
				}
			}
		}
		else {
			logger.warning("Cannot load Types as MetaModel (resourceData) is NULL");
		}
	}

	private void loadDataProperties() {

		// Simple Elements that maps to a simpleType
		for (XSElementDecl element : fetcher.getElementDecls()) {
			XSType elementType = element.getType(); 
			if (!elementType.isComplexType()) {
				String uri = fetcher.getUri(element);
				String ownerUri = fetcher.getOwnerURI(uri);
				if (ownerUri != null) {
					XMLType owner = resourceData.getTypeFromURI(ownerUri);
					if (owner != null && owner instanceof XMLComplexType) {
						// TODO: better manage types
						System.out.println("SHOULD Create a new simple type for : " + elementType.getName());
						((XMLComplexType) owner).createProperty(element.getName(), resourceData.getTypeFromURI(XMLMetaModel.STR_SIMPLETYPE_URI));
					}
					else {
						logger.warning("unable to find an owner type for attribute: " + uri);
					}
				}
				else {
					logger.warning("unable to find an owner for : " + uri);
				}

			}
		}

		// Attributes defined on a complexType
		for (XSAttributeDecl attribute : fetcher.getAttributeDecls()) {
			String uri = fetcher.getUri(attribute);

			String ownerUri = fetcher.getOwnerURI(uri);

			if (ownerUri != null) {
				XMLType owner = resourceData.getTypeFromURI(ownerUri);
				if (owner != null && owner instanceof XMLComplexType ) {
					// TODO: better manage types
					((XMLComplexType) owner).createProperty(attribute.getName(), resourceData.getTypeFromURI(XMLMetaModel.STR_SIMPLETYPE_URI));
				}
				else {
					logger.warning("unable to find an owner type for attribute: " + uri);
				}
			}
			else {
				logger.warning("unable to find an owner for : " + uri);
			}
		}
	}

	private void loadObjectProperties() {

		for (XSElementDecl element : fetcher.getElementDecls()) {

			XSType elementType = element.getType();
			
			if (elementType.isComplexType()) {
				String uri = fetcher.getUri(element);
				XMLType t = resourceData.getTypeFromURI(fetcher.getUri(element));
				String name = element.getName();

				String ownerUri = fetcher.getOwnerURI(uri);

				if (ownerUri != null) {
					XMLType owner = resourceData.getTypeFromURI(ownerUri);
					if (owner != null && owner instanceof XMLComplexType ) {

						// TODO: better manage types
						((XMLComplexType) owner).createProperty(name, t);
					}
					else {
						logger.warning("unable to find an owner type for attribute: " + uri);
					}
				}
			}
		}

	}

	public boolean load() {

		if (resourceData == null) {
			this.resourceData =  XSDMetaModelImpl.getModelFactory().newInstance(XSDMetaModel.class);
			resourceData.getResource();
			resourceData.setResource(this);
			resourceData.setURI(this.getURI());
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
			loadObjectProperties();
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
