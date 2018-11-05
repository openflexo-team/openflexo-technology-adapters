/**
 * 
 * Copyright (c) 2013-2015, Openflexo
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

package org.openflexo.technologyadapter.emf.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileIODelegate;
import org.openflexo.foundation.resource.FlexoIODelegate;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.InJarIODelegate;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.metamodel.io.EMFMetaModelConverter;
import org.openflexo.toolbox.IProgress;

/**
 * IO Delegate to load a MetaModelResource from Directory in FileSystem
 * 
 * @author xtof
 */
public abstract class EMFMetaModelResourceImpl extends FlexoResourceImpl<EMFMetaModel> implements EMFMetaModelResource {

	protected static final Logger logger = Logger.getLogger(EMFMetaModelResourceImpl.class.getPackage().getName());

	/**
	 * 
	 * Follow the link.
	 * 
	 * @see org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource#getMetaModel()
	 */
	@Override
	public EMFMetaModel getMetaModelData() {

		try {
			return getResourceData(null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
		} catch (FlexoException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.resource.FlexoResource#loadResourceData(org.openflexo.toolbox.IProgress)
	 */
	@Override
	public EMFMetaModel loadResourceData(IProgress progress) throws ResourceLoadingCancelledException {

		EMFMetaModel result = null;
		Class<?> ePackageClass = null;
		ClassLoader classLoader = null;

		try {
			// Retrieve class loader to be used
			classLoader = getIODelegate().retrieveClassLoader();

			System.out.println("Reading EMF metamodel from " + getIODelegate());
			System.out.println("ClassLoader=" + classLoader);

			ePackageClass = classLoader.loadClass(getPackageClassName());
			if (ePackageClass != null) {
				Field ePackageField = ePackageClass.getField("eINSTANCE");
				if (ePackageField != null) {
					EPackage ePack = (EPackage) ePackageField.get(null);
					setPackage(ePack);
				}

				// And perform the load
				performLoadMetaModel(classLoader);

				if (getPackage() != null && getPackage().getNsURI().equalsIgnoreCase(getURI())) {

					EMFMetaModelConverter converter = new EMFMetaModelConverter(getTechnologyAdapter());
					result = converter.convertMetaModel(getPackage());
					result.setResource(this);
				}
			}
			return result;

		} catch (ClassNotFoundException e) {
			logger.warning("Unable to load EMF MEtaModel: could not find some class");
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			logger.warning("Unable to load EMF MEtaModel: could not find some property");
			e.printStackTrace();
		} catch (SecurityException e) {
			logger.warning("Unable to load EMF MEtaModel:");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			logger.warning("Unable to load EMF MEtaModel:");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			logger.warning("Unable to load EMF MEtaModel:");
			e.printStackTrace();
		} catch (InstantiationException e) {
			logger.warning("Unable to load EMF MEtaModel:");
			e.printStackTrace();
		}
		return null;

	}

	protected void performLoadMetaModel(ClassLoader classLoader)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class<?> resourceFactoryClass = classLoader.loadClass(getEMFResourceFactoryClassName());
		if (resourceFactoryClass != null) {
			setEMFResourceFactory((Resource.Factory) resourceFactoryClass.newInstance());
		}
		else {
			logger.warning("I will not be able to initialize EMF Model Factory for: " + getURI());
		}
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.resource.FlexoResource#save(org.openflexo.toolbox.IProgress)
	 */
	@Override
	public void save(IProgress progress) {
		logger.info("MetaModel is not supposed to be modified.");
	}

	@Override
	public Class<EMFMetaModel> getResourceDataClass() {
		return EMFMetaModel.class;
	}

	/**
	 * Creates a new ModelResource, for EMF, MetaModel decides wich type of serialization you should use!
	 * 
	 * @param flexoIODelegate
	 * @return
	 */
	@Override
	public Resource createEMFModelResource(FlexoIODelegate<?> flexoIODelegate) {

		// TODO: refactor this with IODelegate

		if (flexoIODelegate instanceof FileIODelegate) {
			return getEMFResourceFactory().createResource(
					org.eclipse.emf.common.util.URI.createFileURI(((FileIODelegate) flexoIODelegate).getFile().getAbsolutePath()));
		}

		if (flexoIODelegate instanceof InJarIODelegate) {
			try {
				InJarIODelegate inJarIODelegate = (InJarIODelegate) flexoIODelegate;
				JarEntry entry = inJarIODelegate.getInJarResource().getEntry();
				// TODO: Cannot use try-with-resource for jarFile below (breaks EMF connector)
				JarFile jarFile = inJarIODelegate.getInJarResource().getJarResource().getJarfile();
				File copiedFile = jarEntryAsFile(jarFile, entry);
				return getEMFResourceFactory().createResource(org.eclipse.emf.common.util.URI.createFileURI(copiedFile.getAbsolutePath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// not implemented
		logger.warning("createEMFModelResource() for " + flexoIODelegate + " : not implemented");

		return null;
	}

	private static File jarEntryAsFile(JarFile jarFile, JarEntry jarEntry) throws IOException {
		String name = jarEntry.getName().replace('/', '_');
		int i = name.lastIndexOf(".");
		String extension = i > -1 ? name.substring(i) : "";
		File file = File.createTempFile(name.substring(0, name.length() - extension.length()) + ".", extension);
		file.deleteOnExit();
		try (InputStream input = jarFile.getInputStream(jarEntry); OutputStream output = new FileOutputStream(file)) {
			int readCount;
			byte[] buffer = new byte[4096];
			while ((readCount = input.read(buffer)) != -1) {
				output.write(buffer, 0, readCount);
			}
			return file;
		}
	}
}
