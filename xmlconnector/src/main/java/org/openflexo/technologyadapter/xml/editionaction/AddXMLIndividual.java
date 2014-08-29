/*
 * (c) Copyright 2010-2012 AgileBirds
 * (c) Copyright 2012-2013 Openflexo
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

package org.openflexo.technologyadapter.xml.editionaction;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.ontology.DuplicateURIException;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.view.ModelSlotInstance;
import org.openflexo.foundation.view.action.FlexoBehaviourAction;
import org.openflexo.foundation.viewpoint.annotations.FIBPanel;
import org.openflexo.foundation.viewpoint.editionaction.AssignableAction;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.xml.XMLModelSlot;
import org.openflexo.technologyadapter.xml.metamodel.XMLComplexType;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLModel;

/**
 * @author xtof
 * 
 */

@FIBPanel("Fib/AddXMLIndividual.fib")
@ModelEntity
@XMLElement
@ImplementationClass(AddXMLIndividual.AddXMLIndividualImpl.class)
public interface AddXMLIndividual extends AssignableAction<XMLModelSlot, XMLIndividual> {

	@PropertyIdentifier(type = String.class)
	public static final String TYPE_URI = "typeURI";


	@Getter(TYPE_URI)
	@XMLAttribute
	public String getTypeURI();

	@Setter(TYPE_URI)
	public void setTypeURI(String aTypeURI);

	public XMLComplexType getXMLType();
	public void setXMLType(XMLComplexType myType);
	
	public XMLMetaModel getMetamodel();

	public abstract static class AddXMLIndividualImpl extends AssignableActionImpl<XMLModelSlot, XMLIndividual> implements AddXMLIndividual {

		private static final Logger logger = Logger.getLogger(AddXMLIndividualImpl.class.getPackage().getName());

		private XMLComplexType xmlType = null;

		@Override
		public XMLMetaModel getMetamodel(){
			return this.getModelSlot().getMetamodel();
		}

		@Override 
		public XMLComplexType getXMLType(){
			if (xmlType == null && getTypeURI() != null ){
				rebindTypeURI();
			}
			return xmlType;
		}

		@Override
		public void setXMLType(XMLComplexType myType){
			xmlType = myType;
			setTypeURI(myType.getURI());
		}

		private void rebindTypeURI(){
			String aTypeURI = getTypeURI();
			if (aTypeURI != null && getModelSlot() != null){
				XMLType t = getModelSlot().getMetamodel().getTypeFromURI(aTypeURI);

				if (t instanceof XMLComplexType){
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
		public XMLIndividual performAction(FlexoBehaviourAction action) {	

			XMLIndividual newIndividual = null;
			try {

				if (getXMLType() != null){
					ModelSlotInstance<? extends ModelSlot<XMLModel>, XMLModel> modelSlotInstance = (ModelSlotInstance<? extends ModelSlot<XMLModel>, XMLModel>) getModelSlotInstance(action);
					XMLModel model = modelSlotInstance.getAccessedResourceData();
					XMLModelSlot modelSlot = (XMLModelSlot) modelSlotInstance.getModelSlot();

					newIndividual = (XMLIndividual) model.addNewIndividual(getXMLType());
					modelSlotInstance.getResourceData().setIsModified();

/* TODO: Finish this
					for (DataPropertyAssertion dataPropertyAssertion : getDataAssertions()) {
						if (dataPropertyAssertion.evaluateCondition(action)) {
							logger.info("DataPropertyAssertion=" + dataPropertyAssertion);
							XSOntDataProperty property = (XSOntDataProperty) dataPropertyAssertion.getOntologyProperty();
							logger.info("Property=" + property);
							Object value = dataPropertyAssertion.getValue(action);
							logger.info("Value=" + value);
							newIndividual.addToPropertyValue(property, value);
						}
					}

					for (ObjectPropertyAssertion objectPropertyAssertion : getObjectAssertions()) {
						if (objectPropertyAssertion.evaluateCondition(action)) {
							// ... TODO
							logger.warning("***** AddObjectProperty Not Implemented yet");
						}
					}
*/
					// add it to the model
					// Two phase creation, then addition, to be able to process URIs once you have the property values
					// and verify that there is no duplicate URIs

					System.out.println("Toto");
					String processedURI = modelSlot.getURIForObject(modelSlotInstance, newIndividual);
					if (processedURI != null) {
						Object o = modelSlot.retrieveObjectWithURI(modelSlotInstance, processedURI);
						if (o != null) {
							model.removeFromIndividuals(newIndividual);
							throw new DuplicateURIException("Error while creating Individual of type " + getXMLType().getURI());
						}

						return newIndividual;
					}
					else {
						// TODO Provide a way to push an error message to the user!
						logger.warning("Error while creating Individual of type " + getXMLType().getURI());
						if (newIndividual != null){
						model.removeFromIndividuals(newIndividual);
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
