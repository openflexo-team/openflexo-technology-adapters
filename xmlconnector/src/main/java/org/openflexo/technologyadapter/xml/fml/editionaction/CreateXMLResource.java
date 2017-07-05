/**
 * 
 * Copyright (c) 2017, Openflexo
 * 
 * This file is part of XMLconnector, a component of the software infrastructure 
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
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.AbstractCreateResource;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceFactory;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.xml.XMLModelSlot;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.rm.XMLResource;
import org.openflexo.technologyadapter.xml.rm.XMLResourceFactory;

/**
 * {@link EditionAction} used to create an empty XML resource
 * 
 * @author sylvain, xtof
 *
 */
@ModelEntity
@ImplementationClass(CreateXMLResource.CreateXMLResourceImpl.class)
@XMLElement
@FML("CreateXMLResource")
public interface CreateXMLResource extends AbstractCreateResource<XMLModelSlot, XMLModel, XMLTechnologyAdapter> {

	public static abstract class CreateXMLResourceImpl extends AbstractCreateResourceImpl<XMLModelSlot, XMLModel, XMLTechnologyAdapter>
			implements CreateXMLResource {

		private static final Logger logger = Logger.getLogger(CreateXMLResourceImpl.class.getPackage().getName());

		@Override
		public Type getAssignableType() {
			return XMLModel.class;
		}

		@Override
		protected <I, R extends TechnologyAdapterResource<XMLModel, XMLTechnologyAdapter>, RF extends FlexoResourceFactory<R, XMLModel, XMLTechnologyAdapter>> R createResource(
				XMLTechnologyAdapter technologyAdapter, Class<RF> resourceFactoryClass, FlexoResourceCenter<I> resourceCenter,
				String resourceName, String resourceURI, String relativePath, String extension, boolean createEmptyContents)
				throws SaveResourceException, ModelDefinitionException {

			XMLResource rsc = (XMLResource) super.createResource(technologyAdapter, resourceFactoryClass, resourceCenter, resourceName,
					resourceURI, relativePath, extension, false);

			XMLModelSlot modelSlot = this.getInferedModelSlot();

			XMLResourceFactory resourceFactory = (XMLResourceFactory) technologyAdapter.getResourceFactory(resourceFactoryClass);

			if (modelSlot != null) {
				rsc.setMetaModelResource(
						(FlexoMetaModelResource<XMLModel, XMLMetaModel, XMLTechnologyAdapter>) modelSlot.getMetaModelResource());
			}
			if (createEmptyContents && rsc != null) {
				XMLModel model = resourceFactory.makeEmptyResourceData(rsc);
				model.setResource(rsc);
			}

			rsc.save(null);

			return (R) rsc;

		}

		@Override
		public String editionActionRepresentation() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public XMLModel execute(RunTimeEvaluationContext evaluationContext) throws FlexoException {

			String resourceName = getResourceName(evaluationContext);
			String resourceURI = getResourceURI(evaluationContext);
			FlexoResourceCenter<?> rc = getResourceCenter(evaluationContext);

			XMLTechnologyAdapter xmlTA = getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(XMLTechnologyAdapter.class);

			XMLResource newResource;
			try {
				newResource = createResource(xmlTA, XMLResourceFactory.class, rc, resourceName, resourceURI, getRelativePath(), ".xml",
						true);

				return newResource.getModel();

			} catch (ModelDefinitionException e) {
				throw new FlexoException(e);
			}

		}
	}
}
