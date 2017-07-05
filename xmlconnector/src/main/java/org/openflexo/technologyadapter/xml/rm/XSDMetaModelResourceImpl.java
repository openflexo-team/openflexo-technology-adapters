/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.technologyadapter.xml.rm;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.StreamIODelegate;
import org.openflexo.technologyadapter.xml.metamodel.XMLComplexType;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
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

public abstract class XSDMetaModelResourceImpl extends FlexoResourceImpl<XMLMetaModel> implements XSDMetaModelResource {

	private static final Logger logger = Logger.getLogger(XSDMetaModelResourceImpl.class.getPackage().getName());

	// Properties

	private XSSchemaSet schemaSet;
	private XSDeclarationsFetcher fetcher;

	private boolean isLoaded = false;
	private boolean isLoading = false;
	private boolean isReadOnly = true;

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
	 * @throws FlexoException
	 */
	@Override
	public XMLMetaModel loadResourceData(IProgress progress) throws ResourceLoadingCancelledException, FlexoException {

		if (getFlexoIOStreamDelegate() == null) {
			throw new FlexoException("Cannot load XML document with this IO/delegate: " + getIODelegate());
		}

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

		if (resourceData != null) {
			for (XSComplexType complexType : fetcher.getComplexTypes()) {

				XMLType xsType = resourceData.getTypeFromURI(fetcher.getUri(complexType));

				if (xsType == null) {
					// create New XMLComplexeType as it does not exist
					xsType = resourceData.createNewType(fetcher.getUri(complexType), complexType.getName(), false);
					xsType.setIsAbstract(true);
				}

				XSType btype = complexType.getBaseType();

				if (btype != null && !btype.getName().equalsIgnoreCase("anyType")) {
					XMLType superType = resourceData.getTypeFromURI(fetcher.getUri(btype));
					if (superType == null) {
						// create New Type if it does not exist
						superType = resourceData.createNewType(btype.getName(), fetcher.getUri(btype), false);
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
					XMLType xsType = resourceData.createNewType(fetcher.getUri(element), element.getName(), false);
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
						XMLProperty prop = ((XMLComplexType) owner).createProperty(element.getName(),
								resourceData.getTypeFromURI(XMLMetaModel.STR_SIMPLETYPE_URI));
						prop.setIsFromElement(true);
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
				if (owner != null && owner instanceof XMLComplexType) {
					// TODO: better manage types
					((XMLComplexType) owner).createProperty(attribute.getName(),
							resourceData.getTypeFromURI(XMLMetaModel.STR_SIMPLETYPE_URI));
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
					if (owner != null && owner instanceof XMLComplexType) {

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
			this.resourceData = XSDMetaModelImpl.getModelFactory().newInstance(XSDMetaModel.class);
			resourceData.getResource();
			resourceData.setResource(this);
			resourceData.setURI(this.getURI());
		}

		if (isLoading() == true) {
			return false;
		}
		isLoading = true;
		isLoaded = false;
		schemaSet = XSOMUtils.read(getInputStream());
		if (schemaSet != null) {
			fetcher = new XSDeclarationsFetcher();
			fetcher.fetch(schemaSet);
			loadTypes();
			loadDataProperties();
			loadObjectProperties();
			isLoaded = true;
		}
		else
			logger.info("I've not been able to parse the stream" + getInputStream());
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

	/**
	 * Return a FlexoIOStreamDelegate associated to this flexo resource
	 * 
	 * @return
	 */
	public StreamIODelegate<?> getFlexoIOStreamDelegate() {
		if (getIODelegate() instanceof StreamIODelegate) {
			return (StreamIODelegate<?>) getIODelegate();
		}
		return null;
	}

	public InputStream getInputStream() {
		if (getFlexoIOStreamDelegate() != null) {
			return getFlexoIOStreamDelegate().getInputStream();
		}
		return null;
	}

	public OutputStream getOutputStream() {
		if (getFlexoIOStreamDelegate() != null) {
			return getFlexoIOStreamDelegate().getOutputStream();
		}
		return null;
	}

}
