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

import java.util.List;
import java.util.Map;

import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Embedded;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Initializer;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Parameter;
import org.openflexo.model.annotations.PastingPoint;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.xml.metamodel.XMLComplexType;
import org.openflexo.technologyadapter.xml.metamodel.XMLObject;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
/**
 * 
 * an XMLIndividual represents a single instance of XML Element in a XMLModel
 * 
 * 
 * @author xtof
 * 
 */
@ModelEntity
@ImplementationClass(XMLIndividualImpl.class)
public interface XMLIndividual extends XMLObject {

	// TODO : manage the calculation of FQN

	public static final String _UUID = "uuid";
	public static final String TYPE = "myType";
	public static final String MODEL = "containerModel";
	public static final String CHILD = "children";
	public static final String PARENT = "parent";
	public static final String PROPERTIES_VALUES = "propertiesValues";
	/**
	 * Property used to host XML's PCDATA textual content
	 */
	public static final String CONTENT = "contentDATA";

	@Initializer
	public XMLIndividual init(@Parameter(MODEL) XMLModel m, @Parameter(TYPE) XMLType t);

	@Getter(MODEL)
    public XMLModel getContainerModel();

	@Getter(TYPE)
	public XMLComplexType getType();
	
	@Setter(TYPE)
	public void setType(XMLComplexType aType);
	
	@Getter(_UUID)
	public String getUUID();
	

	@Getter(PARENT)
	public XMLIndividual getParent();
	
	@Setter(PARENT)
	public void setParent(XMLIndividual xmlind);
	

	@Getter(value = CHILD, cardinality = Cardinality.LIST, inverse = PARENT)
	@CloningStrategy(StrategyType.IGNORE)
	@Embedded
	public List<XMLIndividual> getChildren();

	@Remover(CHILD)
	public void removeChild(XMLIndividual ind);

	@Adder(CHILD)
	@PastingPoint
	public void addChild(XMLIndividual ind);
	
	@Getter(value = PROPERTIES_VALUES, cardinality = Cardinality.MAP)
	public Map<? extends XMLProperty, ? extends XMLPropertyValue> getPropertiesValues();

	public XMLPropertyValue getPropertyValue(String pname);

	public XMLPropertyValue getPropertyValue(XMLProperty prop);

	public String getPropertyStringValue(XMLProperty prop);
	
	@Adder(value = PROPERTIES_VALUES)
	public void addPropertyValue(XMLProperty prop, XMLPropertyValue value);

	public void addPropertyValue(String name, Object value);

	public void addPropertyValue(XMLProperty prop, Object value);
	
	@Remover(value = PROPERTIES_VALUES)
	public void deletePropertyValues(XMLProperty attr);
	
	@Getter(CONTENT)
	public String getContentDATA();
	
	// TODO : refactor to get rid of any JDOM reference
	public Element toXML(Document doc);


}
