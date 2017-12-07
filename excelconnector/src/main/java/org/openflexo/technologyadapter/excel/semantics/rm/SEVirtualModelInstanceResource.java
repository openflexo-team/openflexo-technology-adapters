/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexo-foundation, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel.semantics.rm;

import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.IOFlexoException;
import org.openflexo.foundation.InconsistentDataException;
import org.openflexo.foundation.InvalidModelDefinitionException;
import org.openflexo.foundation.InvalidXMLException;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.rm.AbstractVirtualModelInstanceResource;
import org.openflexo.foundation.fml.rt.rm.AbstractVirtualModelInstanceResourceImpl;
import org.openflexo.foundation.resource.FileIODelegate;
import org.openflexo.foundation.resource.FlexoFileNotFoundException;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.rm.FileSystemResourceLocatorImpl;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.semantics.fml.SEInitializer;
import org.openflexo.technologyadapter.excel.semantics.fml.SEInitializerAction;
import org.openflexo.technologyadapter.excel.semantics.model.ExcelMappingException;
import org.openflexo.technologyadapter.excel.semantics.model.SEVirtualModelInstance;
import org.openflexo.toolbox.IProgress;

/**
 * This is the {@link FlexoResource} encoding a {@link FMLRTVirtualModelInstance}
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@ImplementationClass(SEVirtualModelInstanceResource.SEVirtualModelInstanceResourceImpl.class)
@XMLElement
public interface SEVirtualModelInstanceResource
		extends AbstractVirtualModelInstanceResource<SEVirtualModelInstance, ExcelTechnologyAdapter> {

	/**
	 * Default implementation for {@link SEVirtualModelInstanceResource}
	 * 
	 * 
	 * @author Sylvain
	 * 
	 */
	public abstract class SEVirtualModelInstanceResourceImpl
			extends AbstractVirtualModelInstanceResourceImpl<SEVirtualModelInstance, ExcelTechnologyAdapter>
			implements SEVirtualModelInstanceResource {

		static final Logger logger = Logger.getLogger(SEVirtualModelInstanceResourceImpl.class.getPackage().getName());

		@Override
		public ExcelTechnologyAdapter getTechnologyAdapter() {
			if (getServiceManager() != null) {
				return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(ExcelTechnologyAdapter.class);
			}
			return null;
		}

		@Deprecated
		@Override
		public Resource getDirectory() {
			String parentPath = getDirectoryPath();
			if (ResourceLocator.locateResource(parentPath) == null) {
				FileSystemResourceLocatorImpl.appendDirectoryToFileSystemResourceLocator(parentPath);
			}
			return ResourceLocator.locateResource(parentPath);
		}

		@Deprecated
		public String getDirectoryPath() {
			if (getIODelegate() instanceof FileIODelegate) {
				FileIODelegate ioDelegate = (FileIODelegate) getIODelegate();
				return ioDelegate.getFile().getParentFile().getAbsolutePath();
			}
			return "";
		}

		@Override
		public String computeDefaultURI() {
			if (getContainer() != null) {
				return getContainer().getURI() + "/" + (getName().endsWith(getSuffix()) ? getName() : getName() + getSuffix());
			}
			if (getResourceCenter() != null) {
				return getResourceCenter().getDefaultBaseURI() + "/"
						+ (getName().endsWith(getSuffix()) ? getName() : getName() + getSuffix());
			}
			return null;
		}

		@Override
		public Class<ExcelTechnologyAdapter> getTechnologyAdapterClass() {
			return ExcelTechnologyAdapter.class;
		}

		private String virtualModelURI;

		@Override
		public String getVirtualModelURI() {
			if (getVirtualModelResource() != null) {
				return getVirtualModelResource().getURI();
			}
			return virtualModelURI;
		}

		@Override
		public void setVirtualModelURI(String virtualModelURI) {
			this.virtualModelURI = virtualModelURI;
		}

		public String getSuffix() {
			return SEVirtualModelInstanceResourceFactory.EXCEL_SE_SUFFIX;
		}

		@Override
		public Class<SEVirtualModelInstance> getResourceDataClass() {
			return SEVirtualModelInstance.class;
		}

		@Override
		public SEVirtualModelInstance loadResourceData(IProgress progress) throws FlexoFileNotFoundException, IOFlexoException,
				InvalidXMLException, InconsistentDataException, InvalidModelDefinitionException {
			SEVirtualModelInstance returned = super.loadResourceData(progress);

			try {
				returned.updateData();
			} catch (ExcelMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (returned.getVirtualModel().getFlexoBehaviours(SEInitializer.class).size() > 0) {
				SEInitializer initializer = returned.getVirtualModel().getFlexoBehaviours(SEInitializer.class).get(0);
				FlexoEditor editor = null;
				if (getResourceCenter() instanceof FlexoProject) {
					editor = getServiceManager().getProjectLoaderService().getEditorForProject((FlexoProject) getResourceCenter());
				}
				SEInitializerAction action = new SEInitializerAction(initializer, returned, null, editor);
				action.doAction();
			}
			return returned;
		}

	}

}
