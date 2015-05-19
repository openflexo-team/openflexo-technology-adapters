/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
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

package org.openflexo.technologyadapter.owl.model;

/**
 * Defines all RDF URI constants, as in http://www.w3.org/1999/02/22-rdf-syntax-ns
 * 
 * @author sylvain
 * 
 */
public interface RDFURIDefinitions {

	public static final String RDF_ONTOLOGY_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns";

	public static final String RDF_PROPERTY_URI = RDF_ONTOLOGY_URI + "#Property";

	public static final String TYPE_URI = RDF_ONTOLOGY_URI + "#type";
	public static final String VALUE_URI = RDF_ONTOLOGY_URI + "#value";
	public static final String PREDICATE_URI = RDF_ONTOLOGY_URI + "#predicate";
	public static final String OBJECT_URI = RDF_ONTOLOGY_URI + "#object";
	public static final String SUBJECT_URI = RDF_ONTOLOGY_URI + "#subject";
	public static final String REST_URI = RDF_ONTOLOGY_URI + "#rest";
	public static final String FIRST_URI = RDF_ONTOLOGY_URI + "#first";

}
