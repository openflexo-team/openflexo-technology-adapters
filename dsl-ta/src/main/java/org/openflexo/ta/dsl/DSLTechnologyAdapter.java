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

package org.openflexo.ta.dsl;

import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareResourceFactory;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.ta.dsl.fml.binding.DSLBindingFactory;
import org.openflexo.ta.dsl.rm.DSLResourceFactory;
import org.openflexo.ta.dsl.rm.DSLResourceRepository;

/**
 * This class defines and implements an archetype of a DSL technology adapter based on a sablecc grammar<br>
 * 
 * The idea is to demonstrate TechnologyAdapter API using a sablecc grammar
 * 
 * We offer the connexion to a file conform to DSL grammar
 * 
 * @author sylvain
 * 
 */
@DeclareModelSlots({ DSLModelSlot.class })
// You might declare your own types here
// @DeclareTechnologySpecificTypes({ YourCustomType.class })
@DeclareResourceFactory({ DSLResourceFactory.class })
public class DSLTechnologyAdapter extends TechnologyAdapter<DSLTechnologyAdapter> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DSLTechnologyAdapter.class.getPackage().getName());

	private static final DSLBindingFactory BINDING_FACTORY = new DSLBindingFactory();

	@Override
	public String getName() {
		return "DSL technology adapter";
	}

	@Override
	protected String getLocalizationDirectory() {
		return "FlexoLocalization/DSLTechnologyAdapter";
	}

	@Override
	public void ensureAllRepositoriesAreCreated(FlexoResourceCenter<?> rc) {
		super.ensureAllRepositoriesAreCreated(rc);
		getDSLResourceRepository(rc);

	}

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}

	@Override
	public DSLBindingFactory getTechnologyAdapterBindingFactory() {
		return BINDING_FACTORY;
	}

	@Override
	public String getIdentifier() {
		return "DSL";
	}

	public DSLResourceFactory getDSLResourceFactory() {
		return getResourceFactory(DSLResourceFactory.class);
	}

	@SuppressWarnings("unchecked")
	public <I> DSLResourceRepository<I> getDSLResourceRepository(FlexoResourceCenter<I> resourceCenter) {
		DSLResourceRepository<I> returned = resourceCenter.retrieveRepository(DSLResourceRepository.class, this);
		if (returned == null) {
			returned = DSLResourceRepository.instanciateNewRepository(this, resourceCenter);
			resourceCenter.registerRepository(returned, DSLResourceRepository.class, this);
		}
		return returned;
	}

}
