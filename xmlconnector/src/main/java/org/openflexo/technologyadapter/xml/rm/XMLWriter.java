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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.technologyadapter.xml.metamodel.XMLDataProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLObjectProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLModel;


/**
 * This SaxHandler is used to serialize any XML file, either conformant or not to an XSD file The behavior of the Handler depends on the
 * situation (existing XSD).
 * 
 * @author xtof
 * 
 */

public class XMLWriter<R extends TechnologyAdapterResource<RD, ?>, RD extends ResourceData<RD> & TechnologyObject<?>> {

	private R taRes = null;
	private OutputStreamWriter outputStr = null;
	private XMLStreamWriter myWriter = null;
	private String NSURI = null;

	private static String LINE_SEP = "\n";
	private static String DEFAULT_NS = "ns1";

	private static XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();

	public XMLWriter(R resource, OutputStreamWriter out) throws XMLStreamException, IOException {
		super();
		this.taRes = resource;
		outputStr = out;
	}

	public void writeDocument() throws XMLStreamException, ResourceLoadingCancelledException, FlexoException, IOException {

		String NSPrefix = DEFAULT_NS;

		if (outputStr != null) {
			xmlOutputFactory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, false);
			myWriter = xmlOutputFactory.createXMLStreamWriter(outputStr);

			XMLModel model = ((XMLModel) taRes.getResourceData(null));
			if ( model.getNamespace().size() == 2){
				NSPrefix = model.getNamespace().get(XMLModel.NSPREFIX_INDEX);
				NSURI = model.getNamespace().get(XMLModel.NSURI_INDEX);
			}

			if (NSURI != null && !NSURI.isEmpty()) {
				if (NSPrefix == null || NSPrefix.isEmpty()) {
					NSPrefix = DEFAULT_NS; // default
				}
				myWriter.setDefaultNamespace(NSURI);
				myWriter.setPrefix(NSPrefix, NSURI);
			}

			if (myWriter != null) {
				myWriter.writeStartDocument("UTF-8", "1.0");
				myWriter.writeCharacters(LINE_SEP);

				XMLIndividual rootIndiv = ((XMLModel) taRes.getResourceData(null)).getRoot();

				if (rootIndiv != null) {
					writeRootElement(rootIndiv, NSURI, NSPrefix);
					myWriter.writeCharacters(LINE_SEP);
				}

				myWriter.writeEndDocument();
				myWriter.flush();
				myWriter.close();

				myWriter = null;
			}
		}
	}

	private void writeRootElement(XMLIndividual rootIndiv, String nSURI, String nSPrefix) throws XMLStreamException, IOException,
	ResourceLoadingCancelledException, FlexoException {

		myWriter.writeStartElement(nSURI, rootIndiv.getName());
		if (nSURI != null && !nSURI.isEmpty()){
			myWriter.writeNamespace(nSPrefix, nSURI);
		}
		// Attributes
		writeAttributes(rootIndiv);
		myWriter.writeCharacters(LINE_SEP);

		// children node
		for (Object i : rootIndiv.getChildren()) {
			writeElement(i, ((XMLIndividual) i).getName());
		}
		// CDATA
		String content = rootIndiv.getContentDATA();
		if (content != null && !content.isEmpty()) {
			myWriter.writeCData(content);
			myWriter.writeCharacters(LINE_SEP);
		}
		// Element End
		myWriter.writeEndElement();
		myWriter.writeCharacters(LINE_SEP);

	}

	private void writeElement(Object o, String name) throws XMLStreamException {
		XMLIndividual indiv = (XMLIndividual) o;

		myWriter.writeStartElement(NSURI, name);

		// Attributes
		writeAttributes(indiv);
		// children node
		for (Object i : indiv.getChildren()) {
			writeElement(i, ((XMLIndividual) i).getName());
		}
		// CDATA
		String content = indiv.getContentDATA();
		if (content != null && !content.isEmpty()) {
			myWriter.writeCData(content);
			myWriter.writeCharacters(LINE_SEP);
		}
		// Element End
		myWriter.writeEndElement();
		myWriter.writeCharacters(LINE_SEP);
	}

	private void writeAttributes(XMLIndividual indiv) throws XMLStreamException {
		// Simple Attributes First
		String value = null;

		// Data Properties
		for (XMLProperty prop : indiv.getType().getProperties()) {
			if (prop instanceof XMLDataProperty && !prop.isFromXMLElement()) {
				value = indiv.getPropertyStringValue(prop);
				if (value != null) {
					myWriter.writeAttribute(prop.getName(), value);
				}
			}
		}

		for (XMLProperty prop : indiv.getType().getProperties()) {
			if (prop instanceof XMLDataProperty && prop.isFromXMLElement()) {

				List<?> valueList = (List<?>) indiv.getPropertyValue(prop.getName());
				if (valueList != null && valueList.size() > 0) {
					myWriter.writeStartElement(prop.getName());
					for (Object o : valueList) {
						if (o != null) {
							myWriter.writeCData(o.toString());
						}
					}
					myWriter.writeEndElement();
					myWriter.writeCharacters(LINE_SEP);
				}
			}
		}
		// Object Properties
		for (XMLProperty prop : indiv.getType().getProperties()) {

			if (prop instanceof XMLObjectProperty) {
				List<?> valueList = (List<?>) indiv.getPropertyValue(prop.getName());
				if (valueList != null) {
					for (Object o : valueList) {
						this.writeElement(o, prop.getName());
					}
				}
			}
		}
	}

}
