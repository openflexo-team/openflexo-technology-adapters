/*
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

import org.openflexo.technologyadapter.xml.metamodel.XMLComplexType;
import org.openflexo.technologyadapter.xml.metamodel.XMLDataProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLObjectProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLSimpleType;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLObjectPropertyValue;

public class Helpers {

	/**
	 * Prints all types...
	 * @param metamodel
	 */
	public static final void dumpTypes(XMLMetaModel metamodel) {

		System.out.println("\n\n");

		for (XMLType t : metamodel.getTypes()) {
			String prefix = new String();
			if ( t.isAbstract() ) prefix = "*";
			else prefix = "-";
			if (t.getSuperType() != null){
				System.out.println("Parsed Type: "+prefix + t.getName() + " :: " + t.getSuperType().getName() + " [ " + t.getURI() +" ]");
			}
			else {
				System.out.println("Parsed Type: "+prefix + t.getName() + " [ " + t.getURI() +" ]");

			}
			if (t instanceof XMLComplexType) {
				for ( XMLProperty x : t.getProperties()) {
					XMLType pt = (XMLType) x.getType();
					if (pt instanceof XMLSimpleType){
						System.out.println("     -data: " + x.getName() + "  :: " + pt.getName() + " [ " + pt.getURI() +" ]");
					}
					else {
						System.out.println("    --obj: " + x.getName() + "  :: " + pt.getName() + " [ " + pt.getURI() +" ]");
					}
				}
			}
			else {
				System.out.println("Its a simple Type...");
			}
			System.out.flush();
		}
	}

	/**
	 * Prints all the property values of an indivudal
	 */

	public static final void dumpProperties (XMLIndividual indiv, XMLType aType, String prefix) {
		if (aType == null){
			dumpProperties(indiv, indiv.getType(), prefix);
		}
		else {
			for (XMLProperty prop : aType.getProperties()) {
				if (prop instanceof XMLDataProperty) {
					System.out.println(prefix + "    * attr: " + prop.getName() + " = " + indiv.getPropertyValue(prop).toString());
				}
				else if (prop instanceof XMLObjectProperty) {
					System.out.println(prefix + "    * obj: " + prop.getName() );
					XMLObjectPropertyValue vals = (XMLObjectPropertyValue) indiv.getPropertyValue(prop);
					for (XMLIndividual v : vals.getValues()){
						dumpIndividual (v, prefix + "          + ");
					}
				}

			}
			if (aType.getSuperType() != null){
				dumpProperties(indiv,aType.getSuperType(),prefix);
			}
		}
	}

	/**
	 * Prints all individuals
	 * 
	 */

	public  static final void dumpIndividual (XMLIndividual indiv, String prefix) {

		System.out.println(prefix + "Indiv : " +  indiv.getName());
		dumpProperties(indiv, null, prefix);

		for (XMLIndividual x : indiv.getChildren())
			dumpIndividual(x, prefix + "    [C]");

		System.out.flush();

	}


}
