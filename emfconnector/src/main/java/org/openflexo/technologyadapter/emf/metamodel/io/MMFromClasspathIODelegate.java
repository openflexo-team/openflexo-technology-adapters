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

package org.openflexo.technologyadapter.emf.metamodel.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.FlexoIODelegate;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Implementation;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.emf.EMFTechnologyContextManager;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource;
import org.openflexo.toolbox.FileUtils;
import org.openflexo.toolbox.JarInDirClassLoader;

/**
 * An IO Delegate that loads an EMFMetaModel from ClassPath
 * 
 * @author xtof
 */

@ModelEntity
@XMLElement
public interface MMFromClasspathIODelegate extends EMFMetaModelIODelegate<String>  {

	@Implementation
	public abstract class MMFromClasspathIODelegateImpl extends EMFMetaModelIODelegateImpl<String> implements MMFromClasspathIODelegate {

		protected static final Logger logger = Logger.getLogger(MMFromClasspathIODelegate.class.getPackage().getName());

		
		public static MMFromClasspathIODelegate makeMMFromClasspathIODelegate( ModelFactory factory) {
			MMFromClasspathIODelegate iodelegate = factory.newInstance(MMFromClasspathIODelegate.class);
			return iodelegate;
		}


		@Override
		public String getParentPath() {
			return "";
		}

		@Getter(value = SERIALIZATION_ARTEFACT, ignoreType = true)
		@Override
		public String getSerializationArtefact(){
			return "EMF MetaModel From Classpath: " + getFlexoResource().getURI();
		}



		@Override 
		public EMFMetaModel loadMetaModel(EMFTechnologyContextManager ctxtManager) {

			EMFMetaModel result = null;
			Class<?> ePackageClass = null;
			ClassLoader classLoader = null;
			EMFMetaModelResource resource = (EMFMetaModelResource) this.getFlexoResource();

			try {
				classLoader = MMFromClasspathIODelegateImpl.class.getClassLoader();
				ePackageClass = classLoader.loadClass(resource.getPackageClassName());
				if (ePackageClass != null) {
					Field ePackageField = ePackageClass.getField("eINSTANCE");
					if (ePackageField != null) {
						EPackage ePack = (EPackage) ePackageField.get(null);
						resource.setPackage(ePack);
						Class<?> resourceFactoryClass = classLoader.loadClass(resource.getEMFResourceFactoryClassName());
						if (resourceFactoryClass != null) {
							resource.setEMFResourceFactory((Resource.Factory) resourceFactoryClass.newInstance());

							if (resource.getPackage() != null && resource.getPackage().getNsURI().equalsIgnoreCase(resource.getURI()) && resource.getEMFResourceFactory() != null) {

								EMFMetaModelConverter converter = new EMFMetaModelConverter(resource.getTechnologyAdapter());
								result = converter.convertMetaModel(resource.getPackage());
								result.setResource(resource);
								resource.setResourceData(result);
							}
						}
						else {
							logger.warning("I will not be able to initialize EMF Model Factory for: " + resource.getURI());
						}
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
		

		/** a Metamodel exists if directory contains jar and an emf.properties file  **/
		@Override
		public boolean exists() {
			return true;
		}

		@Override
		public String stringRepresentation() {
			return "MMFromClasspathIODelegate for " + this.getSerializationArtefact();
		}

		
	}

}
