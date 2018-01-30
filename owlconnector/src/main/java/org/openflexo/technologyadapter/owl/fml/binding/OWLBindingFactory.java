/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Owlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.owl.fml.binding;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.binding.Function;
import org.openflexo.connie.binding.FunctionPathElement;
import org.openflexo.connie.binding.IBindingPathElement;
import org.openflexo.connie.binding.SimplePathElement;
import org.openflexo.foundation.fml.TechnologySpecificType;
import org.openflexo.foundation.ontology.IndividualOfClass;
import org.openflexo.foundation.ontology.SubClassOfClass;
import org.openflexo.foundation.ontology.SubPropertyOfProperty;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.technologyadapter.owl.model.OWLClass;
import org.openflexo.technologyadapter.owl.model.OWLDataProperty;
import org.openflexo.technologyadapter.owl.model.OWLObjectProperty;
import org.openflexo.technologyadapter.owl.model.OWLProperty;
import org.openflexo.technologyadapter.owl.model.StatementWithProperty;

/**
 * Defines OWL-specific types binding path element strategy
 * 
 * @author sylvain
 *
 */
public final class OWLBindingFactory extends TechnologyAdapterBindingFactory {
	static final Logger logger = Logger.getLogger(OWLBindingFactory.class.getPackage().getName());

	public OWLBindingFactory() {
		super();
	}

	@Override
	protected SimplePathElement makeSimplePathElement(Object object, IBindingPathElement parent) {
		if ((parent.getType() instanceof IndividualOfClass) && (object instanceof OWLProperty)) {
			return PropertyStatementPathElement.makePropertyStatementPathElement(parent, (OWLProperty) object);
		}
		logger.warning("Unexpected " + object);
		return null;
	}

	@Override
	public boolean handleType(TechnologySpecificType technologySpecificType) {
		if ((technologySpecificType instanceof IndividualOfClass)
				&& ((IndividualOfClass) technologySpecificType).getOntologyClass() instanceof OWLClass) {
			return true;
		}
		if ((technologySpecificType instanceof SubClassOfClass)
				&& ((SubClassOfClass) technologySpecificType).getOntologyClass() instanceof OWLClass) {
			return true;
		}
		if ((technologySpecificType instanceof SubPropertyOfProperty)
				&& ((SubPropertyOfProperty) technologySpecificType).getOntologyProperty() instanceof OWLProperty) {
			return true;
		}
		if (technologySpecificType instanceof StatementWithProperty) {
			return true;
		}
		return false;
	}

	@Override
	public List<? extends SimplePathElement> getAccessibleSimplePathElements(IBindingPathElement element) {

		if (element.getType() instanceof IndividualOfClass) {
			IndividualOfClass parentType = (IndividualOfClass) element.getType();
			List<SimplePathElement> returned = new ArrayList<>();
			returned.add(new URIPathElement(element));
			returned.add(new URINamePathElement(element));
			if (parentType.getOntologyClass() instanceof OWLClass) {
				for (OWLProperty p : searchProperties((OWLClass) parentType.getOntologyClass())) {
					returned.add(getSimplePathElement(p, element));
				}
			}
			return returned;
		}
		else if (element.getType() instanceof StatementWithProperty) {

			StatementWithProperty eltType = (StatementWithProperty) element.getType();
			List<SimplePathElement> returned = new ArrayList<>();
			returned.add(new URIPathElement(element));
			returned.add(new StatementPropertyPathElement(element));
			returned.add(new StatementSubjectPathElement(element));
			OWLProperty property = eltType.getProperty();
			if (property instanceof OWLObjectProperty) {
				returned.add(new StatementObjectPathElement(element));
			}
			else if (property instanceof OWLDataProperty) {
				returned.add(new StatementValuePathElement(element));
			}
			returned.add(new StatementDisplayableRepresentationPathElement(element));
			return returned;

		}

		return super.getAccessibleSimplePathElements(element);

	}

	@Override
	public List<? extends FunctionPathElement> getAccessibleFunctionPathElements(IBindingPathElement parent) {
		return super.getAccessibleFunctionPathElements(parent);
	}

	@Override
	public SimplePathElement makeSimplePathElement(IBindingPathElement parent, String propertyName) {
		// We want to avoid code duplication, so iterate on all accessible simple path element and choose the right one
		for (SimplePathElement e : getAccessibleSimplePathElements(parent)) {
			if (e.getLabel().equals(propertyName)) {
				return e;
			}
		}
		return super.makeSimplePathElement(parent, propertyName);
	}

	@Override
	public FunctionPathElement makeFunctionPathElement(IBindingPathElement father, Function function, List<DataBinding<?>> args) {
		return super.makeFunctionPathElement(father, function, args);
	}

	private static List<OWLProperty> searchProperties(OWLClass owlClass) {

		List<OWLProperty> returned = new ArrayList<>();

		OWLProperty[] array = owlClass.getPropertiesTakingMySelfAsDomain()
				.toArray(new OWLProperty[owlClass.getPropertiesTakingMySelfAsDomain().size()]);

		// Big trick here
		// A property may shadow another one relatively from its name
		// We try to detect such shadowing, and we put the most specialized property first

		List<Integer> i1 = new Vector<>();
		List<Integer> i2 = new Vector<>();
		for (int i = 0; i < array.length; i++) {
			for (int j = i + 1; j < array.length; j++) {
				if (array[i].getName().equals(array[j].getName())) {
					// Detected name based shadowing between array[i] and array[j]
					// System.out.println("Detected name based shadowing between " + array[i] + " and " + array[j]);
					if (array[i].getFlexoOntology().getAllImportedOntologies().contains(array[j].getFlexoOntology())) {
						// array[i] appears to be the most specialized, don't do anything
					}
					else if (array[j].getFlexoOntology().getAllImportedOntologies().contains(array[i].getFlexoOntology())) {
						// array[j] appears to be the most specialized, we need to swap
						i1.add(i);
						i2.add(j);
					}
				}
			}
		}
		for (int i = 0; i < i1.size(); i++) {
			OWLProperty p1 = array[i1.get(i)];
			OWLProperty p2 = array[i2.get(i)];
			array[i1.get(i)] = p2;
			array[i2.get(i)] = p1;
			// Swapping p1 and p2
		}

		for (final OWLProperty property : array) {
			if (!returned.contains(property)) {
				returned.add(property);
			}
		}

		return returned;
	}

}
