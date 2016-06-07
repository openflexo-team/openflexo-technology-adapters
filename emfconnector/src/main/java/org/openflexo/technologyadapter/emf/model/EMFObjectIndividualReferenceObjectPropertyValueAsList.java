/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Emfconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.emf.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EObjectEList;

/**
 * EMF EObjectEList wrapper
 * 
 * @author xtof
 */
public class EMFObjectIndividualReferenceObjectPropertyValueAsList extends EMFObjectIndividualReferenceObjectPropertyValue
		implements List<EMFObjectIndividual> {

	private static final Logger logger = Logger
			.getLogger(EMFObjectIndividualReferenceObjectPropertyValueAsList.class.getPackage().getName());

	private EObjectEList referencelist;

	public EMFObjectIndividualReferenceObjectPropertyValueAsList(EMFModel model, EObject eObject, EReference aReference, Object refList) {
		super(model, eObject, aReference);
		this.referencelist = (EObjectEList) refList;
	}

	/********************************************
	 * Methods so that it acts as a List
	 */

	@Override
	public int size() {
		return referencelist.size();
	}

	@Override
	public boolean isEmpty() {
		return referencelist.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return referencelist.contains(o);
	}

	@Override
	public Iterator<EMFObjectIndividual> iterator() {
		List<EMFObjectIndividual> result = null;
		if (object.eGet(reference) != null) {
			if (reference.getUpperBound() == 1) {
				if (ontology.getConverter().getIndividuals().get(object.eGet(reference)) != null) {
					result = Collections.singletonList((ontology.getConverter().getIndividuals().get(object.eGet(reference))));
				}
				else {
					result = Collections.emptyList();
				}
			}
			else {
				result = new ArrayList<EMFObjectIndividual>();
				List<?> valueList = (List<?>) object.eGet(reference);
				for (Object value : valueList) {
					if (ontology.getConverter().getIndividuals().get(value) != null) {
						result.add((ontology.getConverter().getIndividuals().get(value)));
					}
				}
			}
		}
		else {
			result = Collections.emptyList();
		}
		return Collections.unmodifiableList(result).iterator();
	}

	@Override
	public Object[] toArray() {
		return referencelist.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return (T[]) referencelist.toArray(a);
	}

	@Override
	public boolean add(EMFObjectIndividual e) {
		return referencelist.add(e.getObject());
	}

	@Override
	public boolean remove(Object o) {
		return referencelist.remove(((EMFObjectIndividual) o).getObject());
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends EMFObjectIndividual> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends EMFObjectIndividual> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		referencelist.clear();

	}

	@Override
	public EMFObjectIndividual get(int index) {
		EObject o = (EObject) referencelist.get(index);
		EMFObjectIndividual returned = ontology.getConverter().convertObjectIndividual(ontology, o);

		return returned;
	}

	@Override
	public EMFObjectIndividual set(int index, EMFObjectIndividual element) {

		referencelist.set(index, element.getObject());
		return element;
	}

	@Override
	public void add(int index, EMFObjectIndividual element) {

		referencelist.add(index, element.getObject());
	}

	@Override
	public EMFObjectIndividual remove(int index) {
		return ontology.getConverter().convertObjectIndividual(ontology, (EObject) referencelist.remove(index));
	}

	@Override
	public int indexOf(Object o) {
		if (o instanceof EMFObjectIndividual) {
			return referencelist.indexOf(((EMFObjectIndividual) o).getObject());
		}
		else
			return -1;

	}

	@Override
	public int lastIndexOf(Object o) {
		if (o instanceof EMFObjectIndividual) {
			return referencelist.lastIndexOf(((EMFObjectIndividual) o).getObject());
		}
		else
			return -1;
	}

	@Override
	public ListIterator<EMFObjectIndividual> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<EMFObjectIndividual> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EMFObjectIndividual> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}
