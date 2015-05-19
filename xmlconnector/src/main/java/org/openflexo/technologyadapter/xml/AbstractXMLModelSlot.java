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


package org.openflexo.technologyadapter.xml;

import java.util.List;

import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.Embedded;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.model.XMLModel;

/**
 * 
 * Properties common to all XML Model Slots
 *
 * @author xtof
 * 
 */

@ModelEntity(isAbstract = true)
public interface AbstractXMLModelSlot<T extends AbstractXMLURIProcessor> extends ModelSlot<XMLModel> {

	@PropertyIdentifier(type = List.class)
	public static final String URI_PROCESSORS_LIST_KEY = "uriProcessorsList";

	@Getter(value = URI_PROCESSORS_LIST_KEY, cardinality = Cardinality.LIST, inverse = AbstractXMLURIProcessor.MODELSLOT)
	@XMLElement
	@Embedded
	public List<T> getUriProcessorsList();

	@Setter(URI_PROCESSORS_LIST_KEY)
	public void setUriProcessorsList(List<T> uriProcessorsList);

	@Adder(URI_PROCESSORS_LIST_KEY)
	public void addToUriProcessorsList(T aUriProcessorsList);

	@Remover(URI_PROCESSORS_LIST_KEY)
	public void removeFromUriProcessorsList(T aUriProcessorsList);

	public T createURIProcessor();

	public T retrieveURIProcessorForType(XMLType aXmlType);

}
