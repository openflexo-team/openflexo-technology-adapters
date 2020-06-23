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

package org.openflexo.ta.dsl.rm;

import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceFactory;
import org.openflexo.foundation.resource.TechnologySpecificPamelaResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.ta.dsl.DSLTechnologyAdapter;
import org.openflexo.ta.dsl.model.DSLModelFactory;
import org.openflexo.ta.dsl.model.DSLSystem;

/**
 * Implementation of {@link FlexoResourceFactory} for {@link DSLResource}
 * 
 * @author sylvain
 *
 */
public class DSLResourceFactory
		extends TechnologySpecificPamelaResourceFactory<DSLResource, DSLSystem, DSLTechnologyAdapter, DSLModelFactory> {

	private static final Logger logger = Logger.getLogger(DSLResourceFactory.class.getPackage().getName());

	public static String DSL_FILE_EXTENSION = ".component";

	public DSLResourceFactory() throws ModelDefinitionException {
		super(DSLResource.class);
	}

	@Override
	public DSLSystem makeEmptyResourceData(DSLResource resource) {
		return resource.getFactory().makeSystem();
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return (resourceCenter.retrieveName(serializationArtefact).endsWith(DSL_FILE_EXTENSION))
				&& !(resourceCenter.retrieveName(serializationArtefact).startsWith("~"));
	}

	@Override
	public <I> DSLResource registerResource(DSLResource resource, FlexoResourceCenter<I> resourceCenter) {
		super.registerResource(resource, resourceCenter);

		// Register the resource in the repository of supplied resource center
		registerResourceInResourceRepository(resource,
				getTechnologyAdapter(resourceCenter.getServiceManager()).getDSLResourceRepository(resourceCenter));

		return resource;
	}

	@Override
	public DSLModelFactory makeModelFactory(DSLResource resource, TechnologyContextManager<DSLTechnologyAdapter> technologyContextManager)
			throws ModelDefinitionException {
		return new DSLModelFactory(resource, technologyContextManager.getServiceManager().getEditingContext());
	}

}
