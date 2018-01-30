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

package org.openflexo.technologyadapter.xml.fml.editionaction;

import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.ontology.DuplicateURIException;
import org.openflexo.foundation.ontology.fml.editionaction.DataPropertyAssertion;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Import;
import org.openflexo.model.annotations.Imports;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.XMLModelSlot;
import org.openflexo.technologyadapter.xml.fml.XMLActorReference;
import org.openflexo.technologyadapter.xml.metamodel.XMLComplexType;
import org.openflexo.technologyadapter.xml.metamodel.XMLDataProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLModel;

/**
 * @author xtof
 * 
 */

@ModelEntity
@XMLElement
@ImplementationClass(AddXMLIndividual.AddXMLIndividualImpl.class)
@Imports({ @Import(XMLDataPropertyAssertion.class), @Import(XMLActorReference.class), })
@FML("AddXMLIndividual")
public interface AddXMLIndividual extends XMLAction<XMLModelSlot, XMLIndividual> {

	@PropertyIdentifier(type = String.class)
	public static final String TYPE_URI = "typeURI";
	public static final String DATA_ASSERTIONS_KEY = "dataAssertions";

	@Getter(TYPE_URI)
	@XMLAttribute
	public String getTypeURI();

	@Setter(TYPE_URI)
	public void setTypeURI(String aTypeURI);

	@Getter(value = DATA_ASSERTIONS_KEY, cardinality = Cardinality.LIST, inverse = DataPropertyAssertion.ACTION_KEY)
	@XMLElement(xmlTag = "DataPropertyAssertion")
	public List<XMLDataPropertyAssertion> getDataAssertions();

	@Setter(DATA_ASSERTIONS_KEY)
	public void setDataAssertions(List<XMLDataPropertyAssertion> dataAssertions);

	@Adder(DATA_ASSERTIONS_KEY)
	public void addToDataAssertions(XMLDataPropertyAssertion aDataAssertion);

	@Remover(DATA_ASSERTIONS_KEY)
	public void removeFromDataAssertions(XMLDataPropertyAssertion aDataAssertion);

	public XMLDataPropertyAssertion deleteDataPropertyAssertion(XMLDataPropertyAssertion assertion);

	public XMLDataPropertyAssertion createDataPropertyAssertion();

	public XMLComplexType getXMLType();

	public void setXMLType(XMLComplexType myType);

	public XMLMetaModel getMetamodel();

	public abstract static class AddXMLIndividualImpl
			extends TechnologySpecificActionDefiningReceiverImpl<XMLModelSlot, XMLModel, XMLIndividual> implements AddXMLIndividual {

		private static final Logger logger = Logger.getLogger(AddXMLIndividualImpl.class.getPackage().getName());

		private XMLComplexType xmlType = null;

		public AddXMLIndividualImpl() {
			super();
		}

		@Override
		public XMLMetaModel getMetamodel() {
			if (getInferedModelSlot() != null) {
				return getInferedModelSlot().getMetamodel();
			}
			return null;
			// return this.getModelSlot().getMetamodel();
		}

		@Override
		public XMLComplexType getXMLType() {
			if (xmlType == null && getTypeURI() != null) {
				rebindTypeURI();
			}
			return xmlType;
		}

		@Override
		public void setXMLType(XMLComplexType myType) {
			xmlType = myType;
			setTypeURI(myType.getURI());
		}

		private void rebindTypeURI() {
			String aTypeURI = getTypeURI();
			if (aTypeURI != null && getInferedModelSlot() != null) {
				XMLType t = getInferedModelSlot().getMetamodel().getTypeFromURI(aTypeURI);

				if (t instanceof XMLComplexType) {
					xmlType = (XMLComplexType) t;
				}
				else {
					logger.warning("Did not found XMLComplextType corresponding to URI " + aTypeURI);
				}
			}

		}

		@Override
		public Type getAssignableType() {
			return XMLIndividual.class;
		}

		@Override
		public XMLDataPropertyAssertion deleteDataPropertyAssertion(XMLDataPropertyAssertion assertion) {
			removeFromDataAssertions(assertion);
			assertion.delete();
			return assertion;
		}

		@Override
		public XMLDataPropertyAssertion createDataPropertyAssertion() {
			XMLDataPropertyAssertion newDataPropertyAssertion = getFMLModelFactory().newInstance(XMLDataPropertyAssertion.class);
			addToDataAssertions(newDataPropertyAssertion);
			return newDataPropertyAssertion;
		}

		@Override
		public XMLIndividual execute(RunTimeEvaluationContext evaluationContext) {

			XMLIndividual newIndividual = null;
			try {

				if (getXMLType() != null) {

					XMLModel receiver = getReceiver(evaluationContext);

					newIndividual = receiver.addNewIndividual(getXMLType());
					receiver.setIsModified();

					for (XMLDataPropertyAssertion dataPropertyAssertion : getDataAssertions()) {
						if (dataPropertyAssertion.evaluateCondition(evaluationContext)) {
							XMLDataProperty property = dataPropertyAssertion.getDataProperty();
							Object value = dataPropertyAssertion.getValue(evaluationContext);
							newIndividual.addPropertyValue(property, value);
						}
					}

					// add it to the model
					// Two phase creation, then addition, to be able to process URIs once you have the property values
					// and verify that there is no duplicate URIs

					XMLModelSlot modelSlot = getInferedModelSlot();

					if (modelSlot != null) {
						String processedURI = modelSlot.getURIForObject(receiver, newIndividual);
						if (processedURI != null) {
							Object o = modelSlot.retrieveObjectWithURI(receiver, processedURI);
							if (o != null) {
								receiver.removeFromIndividuals(newIndividual);
								throw new DuplicateURIException("Error while creating Individual of type " + getXMLType().getURI());
							}

							return newIndividual;
						}
						else {
							// TODO Provide a way to push an error message to the user!
							logger.warning("Error while creating Individual of type " + getXMLType().getURI());
							if (newIndividual != null) {
								receiver.removeFromIndividuals(newIndividual);
							}
						}
					}
				}
				return null;
			} catch (DuplicateURIException e) {
				e.printStackTrace();
				return null;
			}

		}

	}

}
