/*
 * (c) Copyright 2013- Openflexo
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

package org.openflexo.technologyadapter.csv.model;

import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.foundation.ontology.FlexoOntologyObjectImpl;
import org.openflexo.technologyadapter.csv.CSVTechnologyAdapter;

/**
 * Abstract Simple implementation of Flexo ontology object wrapping a Technology Object
 * 
 * @author Jean Le Paon
 * 
 */
public abstract class CSVModelObjectImpl<T extends Object> extends FlexoOntologyObjectImpl<CSVTechnologyAdapter> implements TechnologyObject<CSVTechnologyAdapter> {

	/** MetaModel. */
	protected final CSVModel ontology;
	/** CSV Object Wrapped. */
	protected final T object;

	/**
	 * Constructor.
	 */
	public CSVModelObjectImpl(CSVModel ontology, T object) {
		this.ontology = ontology;
		this.object = object;
	}

	@Override
	public CSVTechnologyAdapter getTechnologyAdapter() {
		return (CSVTechnologyAdapter) getCSVModel().getTechnologyAdapter();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.FlexoOntologyObjectImpl#getFlexoOntology()
	 */
	@Override
	public CSVModel getFlexoOntology() {
		return ontology;
	}

	/**
	 * Return the CSV model this object belongs to
	 * 
	 * @return
	 */
	public CSVModel getCSVModel() {
		return ontology;
	}

	/**
	 * Return the wrapped objects.
	 * 
	 * @return
	 */
	public T getObject() {
		return object;
	}

}
