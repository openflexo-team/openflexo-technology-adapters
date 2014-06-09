/*
 * (c) Copyright 2013-2014 Openflexo
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

package org.openflexo.technologyadapter.xsd.model;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import org.openflexo.technologyadapter.xsd.metamodel.XSOntClass;
import org.openflexo.technologyadapter.xsd.metamodel.XSOntProperty;
import org.openflexo.xml.IXMLAttribute;
import org.openflexo.xml.IXMLIndividual;
import org.openflexo.xml.XMLReaderSAXHandler;
import org.openflexo.xml.saxBasedObjectGraphFactory;
import org.xml.sax.SAXException;

public class XMLXSDModelFactory extends saxBasedObjectGraphFactory {

    private XMLXSDModel model = null;

    @Override
    public Object getInstanceOf(Type aType, String name) {
        if (aType instanceof XSOntClass) {

            System.out.println("Creating an individual of type: " + ((XSOntClass) aType).getName());
            XSOntIndividual _inst = (XSOntIndividual) model.addNewIndividual(aType);
            _inst.setName(name);
            return (Object) _inst;
        }
        return null;
    }

    @Override
    public Type getTypeFromURI(String uri) {
        System.out.println("Looking for a  type: " + uri);

        return model.getMetaModel().getTypeFromURI(uri);
    }

    @Override
    public Object deserialize(String input) throws IOException {
        if (model != null) {

            try {
                saxParser.parse(input, handler);
            } catch (SAXException e) {
                logger.warning("Cannot parse document: " + e.getMessage());
                throw new IOException(e.getMessage());
            }
            return this.model;

        }
        else {
            logger.warning("Context is not set for parsing, aborting");
        }
        return null;
    }

    @Override
    public Object deserialize(InputStream input) throws IOException {
        if (model != null) {

            try {
                saxParser.parse(input, handler);
            } catch (SAXException e) {
                logger.warning("Cannot parse document: " + e.getMessage());
                throw new IOException(e.getMessage());
            }
            return this.model;

        }
        else {
            logger.warning("Context is not set for parsing, aborting");
        }
        return null;
    }

    @Override
    public void addToRootNodes(Object anObject) {
        model.setRoot((XSOntIndividual) anObject);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setContextProperty(String propertyName, Object value) {
        if (propertyName.equals(XMLReaderSAXHandler.NAMESPACE_Property)) {
            model.setNamespace(((List<String>) value).get(0), ((List<String>) value).get(1));
        }

    }

    @Override
    public void setContext(Object objectGraph) {
        model = (XMLXSDModel) objectGraph;

    }

    @Override
    public void resetContext() {
        model = null;
    }

    @Override
    public boolean objectHasAttributeNamed(Object object, String attrName) {
        if (object instanceof XSOntIndividual) {

            XSOntProperty attr = ((XSOntIndividual) object).getAttributeByName(attrName);

            return (attr != null);
        }
        return false;
    }

    @Override
    public void addAttributeValueForObject(Object object, String attrName, Object value) {

        if (object instanceof XSOntIndividual) {
            XSOntProperty attr = ((XSOntIndividual) object).getAttributeByName(attrName);

            if (attr == null) {
                attr = (XSOntProperty) ((XSOntIndividual) object).createAttribute(attrName, String.class, (String) value);
            }
            else {

                attr.addValue(((XSOntIndividual) object), value);

            }
        }
    }

    @Override
    public void addChildToObject(Object currentObject, Object currentContainer) {
        if (currentContainer instanceof XSOntIndividual) {
            ((XSOntIndividual) currentContainer).addChild((IXMLIndividual<XSOntIndividual, XSOntProperty>) currentObject);
        }

    }

    @Override
    public Type getAttributeType(Object currentContainer, String localName) {
        IXMLAttribute attr = ((IXMLIndividual) currentContainer).getAttributeByName(localName);
        if (attr != null) {
            return attr.getAttributeType();
        }
        else
            return null;
    }

}
