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
 * Defines all RDFS URI constants, as in http://www.w3.org/2000/01/rdf-schema
 * 
 * @author sylvain
 * 
 */
public interface RDFSURIDefinitions {

	public static final String RDFS_ONTOLOGY_URI = "http://www.w3.org/2000/01/rdf-schema";

	public static final String RDFS_LITERAL_URI = RDFS_ONTOLOGY_URI + "#Literal";
	public static final String RDFS_RESOURCE_URI = RDFS_ONTOLOGY_URI + "#Resource";
	public static final String RDFS_CLASS_URI = RDFS_ONTOLOGY_URI + "#Class";
	public static final String RDFS_DATATYPE_URI = RDFS_ONTOLOGY_URI + "#Datatype";
	public static final String RDFS_CONTAINER_URI = RDFS_ONTOLOGY_URI + "#Container";

	public static final String RDFS_DOMAIN_URI = RDFS_ONTOLOGY_URI + "#domain";
	public static final String RDFS_RANGE_URI = RDFS_ONTOLOGY_URI + "#range";
	public static final String RDFS_SUB_CLASS_URI = RDFS_ONTOLOGY_URI + "#subClassOf";
	public static final String RDFS_SUB_PROPERTY_URI = RDFS_ONTOLOGY_URI + "#subPropertyOf";

	public static final String RDFS_MEMBER_URI = RDFS_ONTOLOGY_URI + "#member";

	// Annotations
	public static final String RDFS_SEE_ALSO_URI = RDFS_ONTOLOGY_URI + "#seeAlso";
	public static final String RDFS_IS_DEFINED_BY_URI = RDFS_ONTOLOGY_URI + "#isDefinedBy";
	public static final String RDFS_LABEL_URI = RDFS_ONTOLOGY_URI + "#label";
	public static final String RDFS_COMMENT_URI = RDFS_ONTOLOGY_URI + "#comment";

}
