/*
 * (c) Copyright 2014- Openflexo
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
package org.openflexo.technologyadapter.xml.metamodel;

import java.lang.reflect.Type;

import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;


public abstract class XSDMetaModelImpl extends XMLMetaModelImpl implements XSDMetaModel {
//public abstract class XSDMetaModelImpl extends XSOntology implements XMLMetaModel {

	private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger.getLogger(XSDMetaModelImpl.class.getPackage()
			.getName());
	

	private FlexoResource<? extends XMLMetaModel> xsdResource;
	
    private static ModelFactory MF;
    
    static{
    	try {
			 MF = new ModelFactory(XSDMetaModel.class);
		} catch (ModelDefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

	public static ModelFactory getModelFactory() {
		return MF;
	}


	@Override
	public FlexoResource<XMLMetaModel> getResource() {
		return (FlexoResource<XMLMetaModel>) xsdResource;
	}
	
	@Override
	public void setResource(FlexoResource<XMLMetaModel> resource) {
		this.xsdResource = resource;
	}

	// private final XSOntClass thingClass;

/*

	public XSDMetaModelImpl(String ontologyURI, File xsdFile, XMLTechnologyAdapter adapter) {
		super(ontologyURI, xsdFile, adapter);

		this.thingClass = new XSOntClass(this, "Thing", XS_THING_URI, getTechnologyAdapter());

		addClass(thingClass);
	}
*/
	
	@Override
	public boolean isReadOnly() {
		return true;
	}

	@Override
	public void setIsReadOnly(boolean b) {
	}

	// *****************************************************************************
	// IXMLMetaModel related Functions

	@Override
	public Type getTypeFromURI(String uri) {
		return types.get(uri);
	}

	@Override
	public Type createNewType(String uri, String localName) {

		XMLType aType = XMLMetaModelImpl.getModelFactory().newInstance(XMLType.class,this);
		aType.setIsAbstract(false);
		aType.setURI(uri);
		aType.setName(localName);
		addType(aType);
		return aType;
		
		// TODO: pamela-iser XSDDatatype
		/*
		XMLType aType = new XSDDataType();
		aType.setURI(uri);
		aType.setName(localName);
		addType(aType);
		return aType;
		*/
	}

	// *****************************************************************************
	// IFlexoOntology related Functions
/*
	@Override
	public List<XSOntDataProperty> getDataProperties() {
		return new ArrayList<XSOntDataProperty>(dataProperties.values());
	}

	@Override
	public List<XSOntDataProperty> getAccessibleDataProperties() {
		Map<String, XSOntDataProperty> result = new HashMap<String, XSOntDataProperty>();
		for (XSOntology o : getImportedOntologies()) {
			for (XSOntDataProperty c : ((XSDMetaModelImpl) o).getDataProperties()) {
				result.put(c.getURI(), c);
			}
		}
		return new ArrayList<XSOntDataProperty>(result.values());
	}

	@Override
	public XSOntDataProperty getDataProperty(String propertyURI) {
		return dataProperties.get(propertyURI);
	}

	@Override
	public XSOntClass getRootConcept() {
		return thingClass;
	}
*/
	// *****************************************************************************
	// Utility Functions when building the model
/*


	private XSDDataType computeDataType(XSSimpleType simpleType) {
		XSDDataType returned = dataTypes.get(simpleType);
		if (returned == null) {
			returned = new XSDDataType(simpleType, this, getTechnologyAdapter());
			dataTypes.put(simpleType, returned);
		}
		return returned;
	}

	private boolean addClass(XSOntClass c) {
		if (classes.containsKey(c.getURI()) == false) {
			classes.put(c.getURI(), c);
			return true;
		}
		return false;
	}

	public XSOntClass createOntologyClass(String name, String uri) throws DuplicateURIException {
		XSOntClass xsClass = new XSOntClass(this, name, uri, getTechnologyAdapter());
		xsClass.addToSuperClasses(getRootConcept());
		addClass(xsClass);
		return xsClass;
	}

	public XSOntClass createOntologyClass(String name, String uri, XSOntClass superClass) throws DuplicateURIException {
		XSOntClass xsClass = createOntologyClass(name, uri);
		xsClass.addToSuperClasses(superClass);
		return xsClass;
	}

	public XSOntClass createOntologyClass(String name, XSOntClass superClass) throws DuplicateURIException {
		String uri = this.getURI() + "#" + "name";
		XSOntClass xsClass = createOntologyClass(name, uri, superClass);
		return xsClass;
	}

	public XSOntDataProperty createDataProperty(String name, String uri, XSType aType) {
		XSOntDataProperty xsDataProperty = new XSOntDataProperty(this, name, uri, getTechnologyAdapter());
		xsDataProperty.setDataType(computeDataType(aType.asSimpleType()));
		dataProperties.put(uri, xsDataProperty);
		return xsDataProperty;
	}

	public XSOntObjectProperty createObjectProperty(String name, String uri, XSOntClass c) {
		XSOntObjectProperty property = new XSOntObjectProperty(this, name, uri, getTechnologyAdapter());
		objectProperties.put(property.getURI(), property);
		property.newRangeFound(c);
		return property;
	}
*/
	
}
