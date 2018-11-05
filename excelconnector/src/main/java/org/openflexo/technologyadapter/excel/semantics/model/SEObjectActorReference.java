/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexo-foundation, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel.semantics.model;

import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.semantics.rm.SEVirtualModelInstanceResource;

/**
 * Implements {@link ActorReference} for {@link SEFlexoConceptInstance}<br>
 * 
 * @author sylvain
 * 
 * @param <T>
 */
@ModelEntity
@ImplementationClass(SEObjectActorReference.SEObjectActorReferenceImpl.class)
@XMLElement
public interface SEObjectActorReference extends ActorReference<SEFlexoConceptInstance> {

	@PropertyIdentifier(type = String.class)
	String RESOURCE_URI_KEY = "resourceURI";
	@PropertyIdentifier(type = String.class)
	String FLEXO_CONCEPT_URI_KEY = "flexoConceptURI";
	@PropertyIdentifier(type = String.class)
	String KEY_KEY = "key";

	@Getter(RESOURCE_URI_KEY)
	@XMLAttribute
	String getResourceURI();

	@Setter(RESOURCE_URI_KEY)
	void setResourceURI(String resourceURI);

	@Getter(FLEXO_CONCEPT_URI_KEY)
	@XMLAttribute
	String getFlexoConceptURI();

	@Setter(FLEXO_CONCEPT_URI_KEY)
	void setFlexoConceptURI(String conceptURI);

	@Getter(KEY_KEY)
	@XMLAttribute
	String getKey();

	@Setter(KEY_KEY)
	void setKey(String key);

	abstract class SEObjectActorReferenceImpl extends ActorReferenceImpl<SEFlexoConceptInstance> implements SEObjectActorReference {

		private static final Logger logger = FlexoLogger.getLogger(SEObjectActorReference.class.getPackage().toString());

		// private boolean isLoading = false;

		private SEFlexoConceptInstance modellingElement;

		@Override
		public void setModellingElement(SEFlexoConceptInstance object) {
			this.modellingElement = object;
			if (object != null) {
				setResourceURI(object.getVirtualModelInstance().getURI());
				setFlexoConceptURI(object.getFlexoConcept().getURI());
				setKey("" + object.getRowSupportObject().getRowNum());
			}
			else {
				setResourceURI(null);
				setKey(null);
			}
		}

		@Override
		public SEFlexoConceptInstance getModellingElement(boolean forceLoading) {
			// TODO: instantiate cache when retrieving fails and return null value
			// Otherwise, this will continuously loop
			if (modellingElement == null && forceLoading) {
				modellingElement = retrieveModellingElement();
			}
			return modellingElement;
		}

		protected SEFlexoConceptInstance retrieveModellingElement() {
			SEVirtualModelInstanceResource httpVMIResource = (SEVirtualModelInstanceResource) getServiceManager().getResourceManager()
					.getResource(getResourceURI());
			// Unused SEVirtualModelInstance hbnVMI = httpVMIResource.getVirtualModelInstance();
			VirtualModel vm = httpVMIResource.getVirtualModel();
			FlexoConcept concept = vm.getFlexoConcept(getFlexoConceptURI());
			// TODO: find the container !!!

			if (concept.getKeyProperties().size() == 0) {
				logger.warning("Could not retrieve JDBC object when no key defined on concept");
				return null;
			}
			// Unused String identifier = null;
			if (concept.getKeyProperties().size() == 1) {
				/*FlexoProperty<?> uniqueKey = concept.getKeyProperties().get(0);
				if (TypeUtils.isInteger(uniqueKey.getType()) || TypeUtils.isLong(uniqueKey.getType())) {
					identifier = Integer.parseInt(getKey());
				}
				else if (TypeUtils.isString(uniqueKey.getType())) {*/
				// Unused identifier = getKey();
				// }
			}
			else {
				// TODO
				logger.warning("Composite key not implemented here");
				return null;
			}

			// return hbnVMI.getFlexoConceptInstance(identifier, hbnVMI, concept);
			return null;
		}

		@Override
		public String toString() {
			return "HbnObjectActorReference [" + getRoleName() + "] " + Integer.toHexString(hashCode()) + " references "
					+ getModellingElement() + "[resource: " + getResourceURI() + " key:" + getKey() + "]";
		}
	}
}
