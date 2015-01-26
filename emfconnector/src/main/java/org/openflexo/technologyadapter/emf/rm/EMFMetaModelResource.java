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

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.model.EMFModel;

@ModelEntity
@ImplementationClass(EMFMetaModelResourceImpl.class)
@XMLElement
public interface EMFMetaModelResource extends FlexoMetaModelResource<EMFModel, EMFMetaModel, EMFTechnologyAdapter> {

	public static final String EXTENSION = "extension";
	public static final String PACKAGE_CLASSNAME = "package.classname";
	public static final String PACKAGE = "package";
	public static final String RESOURCE_FACTORY_CLASSNAME = "resourcefactory.classname";
	public static final String RESOURCE_FACTORY = "resourcefactory";

	/**
	 * Setter of extension for model files related to this MtaModel.
	 * 
	 * @return
	 */
	@Setter(EXTENSION)
	void setModelFileExtension(String modelFileExtension);

	/**
	 * Getter of extension for model files related to this MtaModel.
	 * 
	 * @return
	 */
	@Getter(EXTENSION)
	String getModelFileExtension();

	/**
	 * Setter of Package MetaModel.
	 * 
	 * @param ePackage
	 */
	@Setter(value = PACKAGE_CLASSNAME)
	void setPackageClassName(String ePackage);

	/**
	 * Getter of Package MetaModel.
	 * 
	 * @return
	 */
	@Getter(value = PACKAGE_CLASSNAME, ignoreType = true)
	String getPackageClassName();

	/**
	 * Setter of Package MetaModel.
	 * 
	 * @param ePackage
	 */
	@Setter(value = PACKAGE)
	void setPackage(EPackage ePackage);

	/**
	 * Getter of Package MetaModel.
	 * 
	 * @return
	 */
	@Getter(value = PACKAGE, ignoreType = true)
	EPackage getPackage();

	/**
	 * Setter of ResourceFactory of Model for MetaModel.
	 * 
	 * @param resourceFactory
	 */
	@Setter(RESOURCE_FACTORY_CLASSNAME)
	void setResourceFactoryClassName(String resourceFactory);

	/**
	 * Getter of ResourceFactory of Model for MetaModel.
	 * 
	 * @return
	 */
	@Getter(value = RESOURCE_FACTORY_CLASSNAME, ignoreType = true)
	String getResourceFactoryClassName();

	/**
	 * Setter of ResourceFactory of Model for MetaModel.
	 * 
	 * @param resourceFactory
	 */
	@Setter(RESOURCE_FACTORY)
	void setResourceFactory(Resource.Factory resourceFactory);

	/**
	 * Getter of ResourceFactory of Model for MetaModel.
	 * 
	 * @return
	 */
	@Getter(value = RESOURCE_FACTORY, ignoreType = true)
	Resource.Factory getResourceFactory();

	/**
	 * Get the MetaModel stored in the Resource..
	 * 
	 * @return
	 */
	@Override
	public EMFMetaModel getMetaModelData();
}
