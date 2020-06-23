/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.gina.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.rm.GINAFIBComponentResource;
import org.openflexo.technologyadapter.gina.rm.GINAResourceRepository;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test basic diagram manipulations
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestLoadGINAFIBComponent extends OpenflexoTestCase {

	public static GINATechnologyAdapter technologicalAdapter;
	public static FlexoServiceManager applicationContext;
	public static FlexoResourceCenter<?> resourceCenter;
	public static GINAResourceRepository<?> repository;
	public static RepositoryFolder<GINAFIBComponentResource, ?> componentFolder;

	public static FlexoEditor editor;

	// Unused private static GINAFIBComponentResource componentResource;

	/**
	 * Initialize
	 */
	@Test
	@TestOrder(1)
	public void testInitialize() {

		log("testInitialize()");

		applicationContext = instanciateTestServiceManager(GINATechnologyAdapter.class);

		assertNotNull(applicationContext);

		technologicalAdapter = applicationContext.getTechnologyAdapterService().getTechnologyAdapter(GINATechnologyAdapter.class);

		assertNotNull(technologicalAdapter);

		resourceCenter = serviceManager.getResourceCenterService().getFlexoResourceCenter("http://openflexo.org/gina-test");

		assertNotNull(resourceCenter);

		repository = technologicalAdapter.getGINAResourceRepository(resourceCenter);

		assertNotNull(repository);

		editor = new FlexoTestEditor(null, applicationContext);

		assertNotNull(editor);

		System.out.println("RC=" + resourceCenter);
		System.out.println("resources: " + resourceCenter.getAllResources());

	}

	/**
	 * Test load FIB component
	 * 
	 * @throws FlexoException
	 * @throws ResourceLoadingCancelledException
	 * @throws FileNotFoundException
	 */
	@Test
	@TestOrder(2)
	public void testLoadComponent() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("testLoadComponent()");

		GINAFIBComponentResource componentResource = repository
				.getResource(resourceCenter.getDefaultBaseURI() + "/TestResourceCenter/Test.fib");

		System.out.println("All resources=" + repository.getAllResources());
		System.out.println("searched: " + resourceCenter.getDefaultBaseURI() + "/TestResourceCenter/Test.fib");

		assertNotNull(componentResource);

		GINAFIBComponent component = componentResource.getResourceData();

		assertNotNull(component);
		assertSame(componentResource, component.getResource());

	}

}
