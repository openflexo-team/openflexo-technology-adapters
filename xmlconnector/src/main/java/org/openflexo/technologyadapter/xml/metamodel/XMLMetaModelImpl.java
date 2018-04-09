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

package org.openflexo.technologyadapter.xml.metamodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.model.XMLModel;

/**
 * A simple MetaModeling Structure that is not backed up in an XSD file and where you can create new types freely
 * 
 * @author xtof
 *
 */

public abstract class XMLMetaModelImpl extends FlexoObjectImpl implements XMLMetaModel {

	private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger
			.getLogger(XMLMetaModelImpl.class.getPackage().getName());

	protected Map<String, XMLType> types = null;

	private static ModelFactory MF;

	public XMLMetaModelImpl() {
		super();
		types = new HashMap<>();
	}

	static {
		try {
			MF = new ModelFactory(ModelContextLibrary.getCompoundModelContext(XMLModel.class, XMLType.class, XMLComplexType.class,
					XMLSimpleType.class, XMLProperty.class, XMLDataProperty.class, XMLObjectProperty.class));
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
	}

	public static ModelFactory getModelFactory() {
		return MF;
	}

	@Override
	public XMLType getTypeFromURI(String uri) {

		XMLType t = types.get(uri);

		if (t == null && uri.equals(XMLMetaModel.STR_SIMPLETYPE_URI)) {
			XMLSimpleType stringSimple = (XMLSimpleType) createNewType(XMLMetaModel.STR_SIMPLETYPE_URI, "STRING_BASIC_TYPE", true);
			stringSimple.setBasicType(String.class);
			return stringSimple;
		}
		return t;

	}

	@Override
	public void addType(XMLType aType) {
		types.put(aType.getURI(), aType);
	}

	@Override
	public void removeType(XMLType aType) {
		types.remove(aType);
	}

	@Override
	public Collection<? extends XMLType> getTypes() {
		return types.values();
	}

	@Override
	public XMLType createNewType(String uri, String localName, boolean simpleType) {

		XMLType aType = null;
		if (simpleType) {
			aType = XMLMetaModelImpl.getModelFactory().newInstance(XMLSimpleType.class, this);
		}
		else {
			aType = XMLMetaModelImpl.getModelFactory().newInstance(XMLComplexType.class, this);
		}
		aType.setIsAbstract(false);
		aType.setURI(uri);
		aType.setName(localName);

		addType(aType);

		return aType;
	}

	/**
	 * 
	 * creates a new empty MetaModel
	 * 
	 * @return
	 */
	public static XMLMetaModel createEmptyMetaModel(String uri) {

		XMLMetaModel metamodel = MF.newInstance(XMLMetaModel.class);
		metamodel.setReadOnly(false);

		metamodel.setURI(uri);

		return metamodel;

	}

	@Override
	public String getDisplayableDescription() {
		return null;
	}

	@Override
	public XMLTechnologyAdapter getTechnologyAdapter() {
		return null;
	}

	@Override
	public Object getObject(String objectURI) {
		return null;
	}

	@Override
	public void setIsReadOnly(boolean b) {

	}

	@Override
	public FlexoResource<XMLMetaModel> getResource() {
		return null;
	}

	@Override
	public void setResource(FlexoResource<XMLMetaModel> resource) {

	}

}
