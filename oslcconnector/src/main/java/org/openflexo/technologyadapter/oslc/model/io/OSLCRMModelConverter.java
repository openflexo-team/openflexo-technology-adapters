/**
 * 
 * Copyright (c) 2015, Openflexo
 * 
 * This file is part of Oslcconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.oslc.model.io;

import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.lyo.client.oslc.OSLCConstants;
import org.eclipse.lyo.client.oslc.resources.Requirement;
import org.eclipse.lyo.client.oslc.resources.RequirementCollection;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.CreationFactory;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;
import org.eclipse.lyo.oslc4j.core.model.ResourceShape;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.model.rm.OSLCRequirement;
import org.openflexo.technologyadapter.oslc.model.rm.OSLCRequirementCollection;

public class OSLCRMModelConverter implements OSLCModelDedicatedConverter {

	private final OSLCModelConverter mainConverter;
	private final Set<Class<?>> providers = new LinkedHashSet<Class<?>>();

	private ModelFactory factory;

	/**
	 * Constructor.
	 */
	public OSLCRMModelConverter(OSLCModelConverter mainConverter) {
		this.mainConverter = mainConverter;
		try {
			factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext(OSLCResource.class, OSLCRequirement.class,
					OSLCRequirementCollection.class));
			providers.add(Requirement.class);
			providers.add(RequirementCollection.class);
		} catch (ModelDefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public OSLCRequirement createOSLCRequirement(String title, String description, CreationFactory creationFactory) {
		try {
			ResourceShape resourceShape;
			resourceShape = mainConverter.getOslcClient().getResourceShape(creationFactory, OslcMediaType.APPLICATION_RDF_XML,
					OSLCConstants.RM_REQUIREMENT_TYPE);
			Requirement requirement = new Requirement();
			requirement.setTitle(title);
			requirement.setDescription(description);
			requirement.setInstanceShape(resourceShape.getAbout());
			requirement = (Requirement) mainConverter.getOslcClient().create(creationFactory, requirement,
					OslcMediaType.APPLICATION_RDF_XML);

			return convertAbstractResource(requirement);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public <AR extends AbstractResource> boolean handleAbstractResource(AR resource) {
		for (Class<?> p : providers) {
			if (resource.getClass().equals(p)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <AR extends AbstractResource, OR extends OSLCResource> OR convertAbstractResource(AR resource) {
		OR oslcResource = null;
		if (resource instanceof Requirement) {
			oslcResource = (OR) factory.newInstance(OSLCRequirement.class);
		}
		oslcResource.setOSLCResource(resource);
		mainConverter.getOSLCResources().put(resource, oslcResource);
		return oslcResource;
	}
}
