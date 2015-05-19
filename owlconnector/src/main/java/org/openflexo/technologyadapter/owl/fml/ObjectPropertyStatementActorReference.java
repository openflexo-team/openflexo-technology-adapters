/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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

package org.openflexo.technologyadapter.owl.fml;

import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.owl.model.OWLConcept;
import org.openflexo.technologyadapter.owl.model.OWLObjectProperty;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.ObjectPropertyStatement;

/**
 * Implements {@link ActorReference} for {@link ObjectPropertyStatement} as modelling elements.<br>
 * We need to serialize here the three values identifying related triplet: subject, property and value
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@ImplementationClass(ObjectPropertyStatementActorReference.ObjectPropertyStatementActorReferenceImpl.class)
@XMLElement
@FML("ObjectPropertyStatementActorReference")
public interface ObjectPropertyStatementActorReference extends ActorReference<ObjectPropertyStatement> {

	@PropertyIdentifier(type = String.class)
	public static final String SUBJECT_URI_KEY = "subjectURI";
	@PropertyIdentifier(type = String.class)
	public static final String OBJECT_PROPERTY_URI_KEY = "objectPropertyURI";
	@PropertyIdentifier(type = String.class)
	public static final String OBJECT_URI_KEY = "objectURI";

	@Getter(value = SUBJECT_URI_KEY)
	@XMLAttribute
	public String getSubjectURI();

	@Setter(SUBJECT_URI_KEY)
	public void setSubjectURI(String objectURI);

	@Getter(value = OBJECT_PROPERTY_URI_KEY)
	@XMLAttribute
	public String getObjectPropertyURI();

	@Setter(OBJECT_PROPERTY_URI_KEY)
	public void setObjectPropertyURI(String objectPropertyURI);

	@Getter(value = OBJECT_URI_KEY)
	@XMLAttribute
	public String getObjectURI();

	@Setter(OBJECT_URI_KEY)
	public void setObjectURI(String objectURI);

	public static abstract class ObjectPropertyStatementActorReferenceImpl extends ActorReferenceImpl<ObjectPropertyStatement> implements
			ObjectPropertyStatementActorReference {

		static final Logger logger = FlexoLogger.getLogger(ObjectPropertyStatementActorReferenceImpl.class.getPackage().toString());

		private ObjectPropertyStatement statement;
		private String subjectURI;
		private String objectPropertyURI;
		private String objectURI;

		/**
		 * Default constructor
		 */
		public ObjectPropertyStatementActorReferenceImpl() {
			super();
		}

		/*public ObjectPropertyStatementActorReference(ObjectPropertyStatement o, ObjectPropertyStatementRole aPatternRole,
				FlexoConceptInstance epi) {
			super(epi.getProject());
			setFlexoConceptInstance(epi);
			setPatternRole(aPatternRole);
			statement = o;
			subjectURI = o.getSubject().getURI();
			objectURI = o.getStatementObject().getURI();
			objectPropertyURI = o.getProperty().getURI();
		}

		// Constructor used during deserialization
		public ObjectPropertyStatementActorReference(FlexoProject project) {
			super(project);
		}*/

		@Override
		public void setModellingElement(ObjectPropertyStatement statement) {
			this.statement = statement;
			if (statement != null && getModelSlotInstance() != null) {
				subjectURI = statement.getSubject().getURI();
				objectURI = statement.getStatementObject().getURI();
				objectPropertyURI = statement.getProperty().getURI();
			}
		}

		@Override
		public ObjectPropertyStatement getModellingElement() {
			if (statement == null) {
				OWLOntology ontology = (OWLOntology) getModelSlotInstance().getAccessedResourceData();
				if (ontology != null) {
					OWLConcept<?> subject = ontology.getOntologyObject(subjectURI);
					OWLObjectProperty property = ontology.getObjectProperty(objectPropertyURI);
					OWLConcept<?> object = ontology.getOntologyObject(objectURI);
					if (subject != null && property != null && object != null) {
						// TODO: also handle value here
						statement = subject.getObjectPropertyStatement(property, object);
					}
				} else {
					logger.warning("Could not access to ontology referenced by " + getModelSlotInstance());
				}
			}
			if (statement == null) {
				logger.warning("Could not retrieve statement" + subjectURI + " " + objectPropertyURI + " " + objectURI);
			}
			return statement;
		}

		@Override
		public String getSubjectURI() {
			return subjectURI;
		}

		@Override
		public void setSubjectURI(String subjectURI) {
			this.subjectURI = subjectURI;
		}

		@Override
		public String getObjectPropertyURI() {
			return objectPropertyURI;
		}

		@Override
		public void setObjectPropertyURI(String objectPropertyURI) {
			this.objectPropertyURI = objectPropertyURI;
		}

		@Override
		public String getObjectURI() {
			return objectURI;
		}

		@Override
		public void setObjectURI(String objectURI) {
			this.objectURI = objectURI;
		}
	}
}
