/**
 * 
 * Copyright (c) 2013-2014, Openflexo
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
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.metamodel.io.EMFMetaModelConverter;
import org.openflexo.toolbox.IProgress;
import org.openflexo.toolbox.JarInDirClassLoader;

/**
 * EMF MetaModel Resource Implementation.
 * 
 * @author gbesancon
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
		FileFlexoIODelegate ffd = (FileFlexoIODelegate) getFlexoIODelegate();
		File f = ffd.getFile();

		// TODO: this should be totally refactored since IODelegate have been introduced
		// See with Christophe: IODelegate should be a wrapper above eclipse resource
		// > Eclipse Resource Center
		// See TA-46

		try {
			if (f != null) {

				classLoader = new JarInDirClassLoader(Collections.singletonList(ffd.getFile()));
				ePackageClass = classLoader.loadClass(getPackageClassName());

			} else {
				classLoader = EMFMetaModelResourceImpl.class.getClassLoader();
				ePackageClass = classLoader.loadClass(getPackageClassName());
			}
			if (ePackageClass != null) {
				Field ePackageField = ePackageClass.getField("eINSTANCE");
				if (ePackageField != null) {
					EPackage ePack = (EPackage) ePackageField.get(null);
					setPackage(ePack);
					EPackage.Registry.INSTANCE.put(ePack.getNsPrefix(), ePack);
					Class<?> resourceFactoryClass = classLoader.loadClass(getResourceFactoryClassName());
					if (resourceFactoryClass != null) {
						setResourceFactory((Resource.Factory) resourceFactoryClass.newInstance());

						if (getPackage() != null && getPackage().getNsURI().equalsIgnoreCase(getURI()) && getResourceFactory() != null) {

							EMFMetaModelConverter converter = new EMFMetaModelConverter(getTechnologyAdapter());
							result = converter.convertMetaModel(getPackage());
							result.setResource(this);
							// this.resourceData = result;
							setResourceData(result);
						}
					}
				}
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
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

}
