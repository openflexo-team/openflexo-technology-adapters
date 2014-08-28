/*
 *
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
 *   Properties common to all XML Model Slots
 *
 * @author xtof
 * 
 */

@ModelEntity
@XMLElement
public interface AbstractXMLModelSlot< T extends AbstractXMLURIProcessor> extends ModelSlot<XMLModel> {

	@PropertyIdentifier(type = List.class)
	public static final String URI_PROCESSORS_LIST_KEY = "uriProcessorsList";

	@Getter(value = URI_PROCESSORS_LIST_KEY,  cardinality = Cardinality.LIST)
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
