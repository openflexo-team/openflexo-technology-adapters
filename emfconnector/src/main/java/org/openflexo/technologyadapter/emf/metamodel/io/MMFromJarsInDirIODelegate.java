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
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
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
 * An IO Delegate that loads an EMFMetaModel from jars in a directory
 * 
 * @author xtof
 */

@ModelEntity
@XMLElement
public interface MMFromJarsInDirIODelegate extends EMFMetaModelIODelegate<File> {


	/** Name of the .properties file to use **/
	static String EMF_PROPERTIES_FILENAME = "emf.properties";

	public String getProperty(String name);

	@Implementation
	public abstract class MMFromJarsInDirIODelegateImpl extends EMFMetaModelIODelegateImpl<File> implements MMFromJarsInDirIODelegate {

		protected static final Logger logger = Logger.getLogger(MMFromJarsInDirIODelegate.class.getPackage().getName());

		private EMFMetaModelResource emfMetaModelResource = null;
		private Properties emfproperties = null;


		public static MMFromJarsInDirIODelegate makeMMFromJarsInDirIODelegate(File file, ModelFactory factory) {
			MMFromJarsInDirIODelegate iodelegate = factory.newInstance(MMFromJarsInDirIODelegate.class);
			iodelegate.setSerializationArtefact(file);
			return iodelegate;
		}


		@Override 
		public EMFMetaModel loadMetaModel(EMFTechnologyContextManager ctxtManager) {

			EMFMetaModel result = null;
			Class<?> ePackageClass = null;
			ClassLoader classLoader = null;
			EMFMetaModelResource resource = (EMFMetaModelResource) this.getFlexoResource();

			try {
				classLoader = new JarInDirClassLoader(Collections.singletonList(this.getSerializationArtefact()));
				ePackageClass = classLoader.loadClass(resource.getPackageClassName());
				if (ePackageClass != null) {
					Field ePackageField = ePackageClass.getField("eINSTANCE");
					if (ePackageField != null) {
						EPackage ePack = (EPackage) ePackageField.get(null);
						resource.setPackage(ePack);
						EPackage.Registry.INSTANCE.put(ePack.getNsPrefix(), ePack);
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

		@Override
		public String getParentPath() {
			if (getSerializationArtefact() != null){
				return getSerializationArtefact().getParent();
			}
			else return "";
		}


		/** a Metamodel exists if directory contains jar and an emf.properties file  **/
		@Override
		public boolean exists() {
			return isValidMetaModelFile(getSerializationArtefact());
		}

		@Override
		public String stringRepresentation() {
			if (getSerializationArtefact() != null){
				return "MMFromJarsInDirIODelegate for directory " + getSerializationArtefact().getAbsolutePath();
			}
			else {
				return "MMFromJarsInDirIODelegate for NO directory";
			}
		}

		/** Return the property value for given name 
		 * 
		 * @return String
		 */

		public String getProperty(String name){
			if (emfproperties == null){
				emfproperties = getEmfProperties(getSerializationArtefact());
			}
			if (emfproperties != null){
				return emfproperties.getProperty(name);
			}
			return null;
		}
		/**
		 * Return EMF Property file of MetaModel Directory.
		 * 
		 * @return
		 */
		static public Properties getEmfProperties(File MetaModelDirectory) {
			Properties emfProperties = null;
			if (MetaModelDirectory != null && MetaModelDirectory.isDirectory()) {
				// Read emf.properties file.
				File[] emfPropertiesFiles = MetaModelDirectory.listFiles(FileUtils.PropertiesFileNameFilter);
				if (emfPropertiesFiles.length == 1) {
					try {
						emfProperties = new Properties();
						emfProperties.load(new FileReader(emfPropertiesFiles[0]));
					} catch (FileNotFoundException e) {
						emfProperties = null;
					} catch (IOException e) {
						emfProperties = null;
					}
				}
			}
			return emfProperties;
		}

		/** 
		 * Verifies that all is ok for a MetaModel to be found
		 */

		static public boolean isValidMetaModelFile(File aMetaModelFile){
			if (aMetaModelFile != null && aMetaModelFile.isDirectory()) {
				File[] jarFiles = aMetaModelFile.listFiles(FileUtils.JARFileNameFilter);

				Properties emfProperties = MMFromJarsInDirIODelegateImpl.getEmfProperties(aMetaModelFile);
				if (emfProperties != null){
					String uri = emfProperties.getProperty("URI");
					String extension = emfProperties.getProperty("EXTENSION");
					String ePackageClassName = emfProperties.getProperty("PACKAGE");
					String resourceFactoryClassName = emfProperties.getProperty("RESOURCE_FACTORY");

					return (uri != null && extension != null && ePackageClassName != null && resourceFactoryClassName != null && jarFiles.length > 0);
				}
				else return false;
			}
			else {
				return false;
			}
		}
	}

}
