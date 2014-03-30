#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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

package ${package}.model;

import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.foundation.ontology.FlexoOntologyObjectImpl;
import ${package}.${technologyPrefix}TechnologyAdapter;

/**
 * Abstract Simple implementation of Flexo ontology object wrapping a Technology Object
 * 
 * @author ${author}
 * 
 */
public abstract class ${technologyPrefix}ModelObjectImpl<T extends Object> extends FlexoOntologyObjectImpl<${technologyPrefix}TechnologyAdapter> implements TechnologyObject<${technologyPrefix}TechnologyAdapter> {

	/** MetaModel. */
	protected final ${technologyPrefix}Model ontology;
	/** ${technologyPrefix} Object Wrapped. */
	protected final T object;

	/**
	 * Constructor.
	 */
	public ${technologyPrefix}ModelObjectImpl(${technologyPrefix}Model ontology, T object) {
		this.ontology = ontology;
		this.object = object;
	}

	@Override
	public ${technologyPrefix}TechnologyAdapter getTechnologyAdapter() {
		return (${technologyPrefix}TechnologyAdapter) get${technologyPrefix}Model().getTechnologyAdapter();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.FlexoOntologyObjectImpl#getFlexoOntology()
	 */
	@Override
	public ${technologyPrefix}Model getFlexoOntology() {
		return ontology;
	}

	/**
	 * Return the ${technologyPrefix} model this object belongs to
	 * 
	 * @return
	 */
	public ${technologyPrefix}Model get${technologyPrefix}Model() {
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
