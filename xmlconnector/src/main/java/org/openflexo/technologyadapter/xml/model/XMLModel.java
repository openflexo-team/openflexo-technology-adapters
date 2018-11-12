/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.technologyadapter.xml.model;

import java.lang.reflect.Type;
import java.util.List;

import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.technologyadapter.FlexoModel;
import org.openflexo.pamela.annotations.Adder;
import org.openflexo.pamela.annotations.CloningStrategy;
import org.openflexo.pamela.annotations.Embedded;
import org.openflexo.pamela.annotations.Finder;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.Initializer;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.Parameter;
import org.openflexo.pamela.annotations.PastingPoint;
import org.openflexo.pamela.annotations.Remover;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.CloningStrategy.StrategyType;
import org.openflexo.pamela.annotations.Getter.Cardinality;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLObject;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;

/**
 * @author xtof
 * 
 *         This interface defines a PAMELA model to represent an XML Document that is conformant to an {@link XMLMetaModel} that might be: -
 *         given by an XSD - dynamically built (on purpose)
 * 
 */
@ModelEntity
@ImplementationClass(XMLModelImpl.class)
public interface XMLModel extends XMLObject, FlexoModel<XMLModel, XMLMetaModel> {

	/**
	 * Reference to the {@link XMLMetaModel} that this document is conformant to
	 */
	public static final String MM = "metaModel";

	/**
	 * Link to the {@XMLResource} that manages the concrete serialization of this model
	 * 
	 */
	public static final String RSC = "resource";

	/**
	 * Collection of {@link XMLIndividuals}
	 */
	public static final String IND = "individuals";

	/**
	 * Root individual of the XML Objects graph
	 */
	public static final String ROOT = "root";

	/**
	 * Properties that host uri and Namespace prefix for this Model
	 */
	public static final int NSPREFIX_INDEX = 0;
	public static final int NSURI_INDEX = 1;

	@Initializer
	public XMLModel init();

	@Initializer
	public XMLModel init(@Parameter(MM) XMLMetaModel mm);

	@Override
	@Getter(MM)
	XMLMetaModel getMetaModel();

	@Setter(MM)
	void setMetaModel(XMLMetaModel mm);

	@Override
	@Getter(RSC)
	public FlexoResource<XMLModel> getResource();

	@Override
	@Setter(RSC)
	public void setResource(FlexoResource<XMLModel> resource);

	@Getter(ROOT)
	public XMLIndividual getRoot();

	@Setter(ROOT)
	public void setRoot(XMLIndividual indiv);

	@Getter(value = IND, cardinality = Cardinality.LIST)
	@CloningStrategy(StrategyType.CLONE)
	@Embedded
	public List<? extends XMLIndividual> getIndividuals();

	// TODO ask Syl pourkoi on ne peut pas avoir +eurs adders...
	public XMLIndividual addNewIndividual(Type aType);

	@Adder(IND)
	@PastingPoint
	public void addIndividual(XMLIndividual ind);

	@Remover(IND)
	public void removeFromIndividuals(XMLIndividual ind);

	@Finder(attribute = XMLIndividual.TYPE, collection = IND, isMultiValued = true)
	public List<? extends XMLIndividual> getIndividualsOfType(XMLType aType);

	/*
	 * Non-PAMELA-managed properties
	 */
	public List<String> getNamespace();

	public void setNamespace(String ns, String prefix);

}
