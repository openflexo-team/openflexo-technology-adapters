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

package org.openflexo.technologyadapter.gina.model.action;

import java.io.FileNotFoundException;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;
import org.openflexo.technologyadapter.gina.rm.GINAFIBComponentResource;
import org.openflexo.technologyadapter.gina.rm.GINAResourceRepository;
import org.openflexo.toolbox.StringUtils;

/**
 * This action is called to create a new {@link FIBComponent} in a repository folder
 * 
 * @author sylvain
 */
public class CreateGINAFIBComponent extends FlexoAction<CreateGINAFIBComponent, RepositoryFolder, FlexoObject> {

	private static final Logger logger = Logger.getLogger(CreateGINAFIBComponent.class.getPackage().getName());

	public static FlexoActionFactory<CreateGINAFIBComponent, RepositoryFolder, FlexoObject> actionType = new FlexoActionFactory<CreateGINAFIBComponent, RepositoryFolder, FlexoObject>(
			"create_component", FlexoActionFactory.newMenu, FlexoActionFactory.defaultGroup, FlexoActionFactory.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateGINAFIBComponent makeNewAction(RepositoryFolder focusedObject, Vector<FlexoObject> globalSelection,
				FlexoEditor editor) {
			return new CreateGINAFIBComponent(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(RepositoryFolder object, Vector<FlexoObject> globalSelection) {
			if (object != null && object.getResourceRepository() instanceof GINAResourceRepository) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isEnabledForSelection(RepositoryFolder object, Vector<FlexoObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateGINAFIBComponent.actionType, RepositoryFolder.class);
	}

	private String componentName;

	private GINAFIBComponentResource componentResource;

	private String description;

	CreateGINAFIBComponent(RepositoryFolder<?, ?> focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws FlexoException {

		try {
			componentResource = _makeGINAFIBComponentResource();
		} catch (ModelDefinitionException e) {
			throw new FlexoException(e);
		}

		/*GINATechnologyAdapter ginaTA = getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(GINATechnologyAdapter.class);
		
		if (!componentName.endsWith(GINATechnologyAdapter.GINA_COMPONENT_EXTENSION)
				&& !componentName.endsWith(GINATechnologyAdapter.GINA_INSPECTOR_EXTENSION)) {
			componentName = componentName + GINATechnologyAdapter.GINA_COMPONENT_EXTENSION;
		}
		
		File componentFile = new File(getFocusedObject().getFile(), componentName);
		
		componentResource = GINAFIBComponentResourceImpl.makeComponentResource(componentFile, ginaTA.getTechnologyContextManager());
		
		getFocusedObject().addToResources(componentResource);
		
		GINAFIBComponent component = componentResource.getFactory().makeNewGINAFIBComponent();
		componentResource.setResourceData(component);
		component.setResource(componentResource);
		componentResource.save(null);*/

	}

	private <I> GINAFIBComponentResource _makeGINAFIBComponentResource() throws SaveResourceException, ModelDefinitionException {
		GINATechnologyAdapter ginaTA = getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(GINATechnologyAdapter.class);

		return ginaTA.getGINAFIBComponentResourceFactory().makeGINAFIBComponentResource(getComponentName(), getFocusedObject(), true);
	}

	@Override
	public boolean isValid() {
		if (StringUtils.isEmpty(componentName)) {
			return false;
		}

		return true;
	}

	public GINAFIBComponent getNewComponent() {
		if (getNewComponentResource() != null) {
			try {
				return getNewComponentResource().getResourceData(null);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ResourceLoadingCancelledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FlexoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public GINAFIBComponentResource getNewComponentResource() {
		return componentResource;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		if ((componentName == null && this.componentName != null) || (componentName != null && !componentName.equals(this.componentName))) {
			String oldValue = this.componentName;
			this.componentName = componentName;
			getPropertyChangeSupport().firePropertyChange("componentName", oldValue, componentName);
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
