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

package org.openflexo.technologyadapter.xml.model;

import java.lang.reflect.Type;

import org.openflexo.xml.IXMLIndividual;



/**
 *
 * This interface defines additional methods to be defined by all XMLModel that 
 * will be populated using the XMLSaxHandler
 *
 * @author xtof
 *
 */

public interface IXMLModel {

	public Object addNewIndividual(Type aType);

	public void setRoot(IXMLIndividual<?,?> anIndividual);

	public IXMLIndividual<?, ?> getRoot();

	public void setNamespace(String uri, String prefix);

	public String getNamespacePrefix();

	public String getNamespaceURI();



}
