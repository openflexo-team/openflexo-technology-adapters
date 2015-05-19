/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012, THALES SYSTEMES AEROPORTES - All Rights Reserved
 * Copyright (c) 2012-2012, AgileBirds
 * 
 * This file is part of Emfconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.emf;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * EMF IO.
 * 
 * @author gbesancon
 */
public class EMFIOUtility {

	/**
	 * Load file.
	 * 
	 * @param filename
	 * @param ePackage
	 * @param eResourceFactory
	 * @return
	 */
	public static Resource load(String filename, EPackage ePackage, Resource.Factory eResourceFactory) {
		return load(URI.createFileURI(filename), ePackage, eResourceFactory);
	}

	/**
	 * Load URI.
	 * 
	 * @param uri
	 * @param ePackage
	 * @param eResourceFactory
	 * @return
	 */
	public static Resource load(URI uri, EPackage ePackage, Resource.Factory eResourceFactory) {
		Resource resource = null;
		try {
			ResourceSet resourceSet = new ResourceSetImpl();

			Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
			registry.getExtensionToFactoryMap().put(ePackage.getName(), eResourceFactory);
			if (resourceSet.getPackageRegistry() != null) {
				resourceSet.getPackageRegistry().put(ePackage.getName(), ePackage);
			}

			resource = resourceSet.createResource(uri);
			resource.load(null);
		} catch (IOException e) {
			resource = null;
			e.printStackTrace();
		}
		return resource;
	}

	/**
	 * Save file.
	 * 
	 * @param objects
	 * @param filename
	 * @param ePackage
	 * @param eResourceFactory
	 */
	public static void save(Collection<EObject> objects, String filename, EPackage ePackage, Resource.Factory eResourceFactory) {
		save(objects, URI.createFileURI(filename), ePackage, eResourceFactory);
	}

	/**
	 * Save URI.
	 * 
	 * @param objects
	 * @param filename
	 */
	public static void save(Collection<EObject> objects, URI uri, EPackage ePackage, Resource.Factory eResourceFactory) {
		try {
			ResourceSet resourceSet = new ResourceSetImpl();

			Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
			registry.getExtensionToFactoryMap().put(ePackage.getName(), eResourceFactory);
			if (resourceSet.getPackageRegistry() != null) {
				resourceSet.getPackageRegistry().put(ePackage.getName(), ePackage);
			}

			Resource resource = resourceSet.createResource(uri);
			resource.getContents().addAll(objects);

			resource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
