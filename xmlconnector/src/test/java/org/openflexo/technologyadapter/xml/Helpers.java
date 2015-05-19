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

import org.openflexo.technologyadapter.xml.metamodel.XMLComplexType;
import org.openflexo.technologyadapter.xml.metamodel.XMLDataProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLObjectProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLSimpleType;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLObjectPropertyValue;
import org.openflexo.technologyadapter.xml.model.XMLPropertyValue;

public class Helpers {

	/**
	 * Prints all types...
	 * 
	 * @param metamodel
	 */
	public static final void dumpTypes(XMLMetaModel metamodel) {

		System.out.println("\n\n");

		for (XMLType t : metamodel.getTypes()) {
			String prefix = new String();
			if (t.isAbstract())
				prefix = "*";
			else
				prefix = "-";
			if (t.getSuperType() != null) {
				System.out
						.println("Parsed Type: " + prefix + t.getName() + " :: " + t.getSuperType().getName() + " [ " + t.getURI() + " ]");
			} else {
				System.out.println("Parsed Type: " + prefix + t.getName() + " [ " + t.getURI() + " ]");

			}
			if (t instanceof XMLComplexType) {
				for (XMLProperty x : ((XMLComplexType) t).getProperties()) {
					XMLType pt = x.getType();
					if (pt instanceof XMLSimpleType) {
						System.out.println("     -data: " + x.getName() + "  :: " + pt.getName() + " [ " + pt.getURI() + " ]");
					} else {
						System.out.println("    --obj: " + x.getName() + "  :: " + pt.getName() + " [ " + pt.getURI() + " ]");
					}
				}
			} else {
				System.out.println("Its a simple Type...");
			}
			System.out.flush();
		}
	}

	/**
	 * Prints all the property values of an indivudal
	 */

	public static final void dumpProperties(XMLIndividual indiv, XMLType aType, String prefix) {
		if (aType == null) {
			dumpProperties(indiv, indiv.getType(), prefix);
		} else {
			if (aType instanceof XMLComplexType) {
				for (XMLProperty prop : ((XMLComplexType) aType).getProperties()) {
					if (prop instanceof XMLDataProperty) {
						XMLPropertyValue val = indiv.getPropertyValue(prop);
						if (val != null) {
							System.out.println(prefix + "    * attr: " + prop.getName() + " = " + indiv.getPropertyValue(prop).toString());
						} else {
							System.out.println(prefix + "    ! attr: " + prop.getName() + " n'est pas valuée");
						}
					} else if (prop instanceof XMLObjectProperty) {
						System.out.println(prefix + "    * obj: " + prop.getName());
						XMLObjectPropertyValue vals = (XMLObjectPropertyValue) indiv.getPropertyValue(prop);
						if (vals != null) {
							for (XMLIndividual v : vals.getValues()) {
								dumpIndividual(v, prefix + "          + ");
							}
						} else {
							System.out.println(" !! Etrange, la propriete " + prop.getName() + " ne contient rien ?!?");
						}
					}
				}

			}
			if (aType.getSuperType() != null) {
				dumpProperties(indiv, aType.getSuperType(), prefix);
			}
		}
	}

	/**
	 * Prints all individuals
	 * 
	 */

	public static final void dumpIndividual(XMLIndividual indiv, String prefix) {

		System.out.println(prefix + "Indiv : " + indiv.getName());
		dumpProperties(indiv, null, prefix);

		for (XMLIndividual x : indiv.getChildren())
			dumpIndividual(x, prefix + "    [C]");

		System.out.flush();

	}

}
