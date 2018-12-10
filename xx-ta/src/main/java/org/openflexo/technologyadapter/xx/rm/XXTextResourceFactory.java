/**
 * 
 * Copyright (c) 2018, Openflexo
 * 
 * This file is part of OpenflexoTechnologyAdapter, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.xx.rm;

import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceFactory;
import org.openflexo.foundation.resource.TechnologySpecificPamelaResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.xx.XXTechnologyAdapter;
import org.openflexo.technologyadapter.xx.model.XXModelFactory;
import org.openflexo.technologyadapter.xx.model.XXText;

/**
 * Implementation of {@link FlexoResourceFactory} for {@link XXTextResource}
 * 
 * @author sylvain
 *
 */
public class XXTextResourceFactory
		extends TechnologySpecificPamelaResourceFactory<XXTextResource, XXText, XXTechnologyAdapter, XXModelFactory> {

	private static final Logger logger = Logger.getLogger(XXTextResourceFactory.class.getPackage().getName());

	public static String TXT_FILE_EXTENSION = ".txt";

	public XXTextResourceFactory() throws ModelDefinitionException {
		super(XXTextResource.class);
	}

	@Override
	public XXText makeEmptyResourceData(XXTextResource resource) {
		return resource.getFactory().makeXXText();
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return (resourceCenter.retrieveName(serializationArtefact).endsWith(TXT_FILE_EXTENSION))
				&& !(resourceCenter.retrieveName(serializationArtefact).startsWith("~"));
	}

	@Override
	protected <I> XXTextResource registerResource(XXTextResource resource, FlexoResourceCenter<I> resourceCenter) {
		super.registerResource(resource, resourceCenter);

		// Register the resource in the repository of supplied resource center
		registerResourceInResourceRepository(resource,
				getTechnologyAdapter(resourceCenter.getServiceManager()).getXXResourceRepository(resourceCenter));

		return resource;
	}

	@Override
	public XXModelFactory makeResourceDataFactory(XXTextResource resource,
			TechnologyContextManager<XXTechnologyAdapter> technologyContextManager) throws ModelDefinitionException {
		return new XXModelFactory(resource, technologyContextManager.getServiceManager().getEditingContext());
	}

}
