/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.technologyadapter.diagram.fml.action;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.IOFlexoException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.viewpoint.ViewPointObject;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification.DiagramSpecificationImpl;
import org.openflexo.toolbox.StringUtils;

public class CreateDiagramSpecification extends FlexoAction<CreateDiagramSpecification, RepositoryFolder, ViewPointObject> {

	private static final Logger logger = Logger.getLogger(CreateDiagramSpecification.class.getPackage().getName());

	public static FlexoActionType<CreateDiagramSpecification, RepositoryFolder, ViewPointObject> actionType = new FlexoActionType<CreateDiagramSpecification, RepositoryFolder, ViewPointObject>(
			"create_diagram_specification", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateDiagramSpecification makeNewAction(RepositoryFolder focusedObject, Vector<ViewPointObject> globalSelection,
				FlexoEditor editor) {
			return new CreateDiagramSpecification(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(RepositoryFolder object, Vector<ViewPointObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(RepositoryFolder object, Vector<ViewPointObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateDiagramSpecification.actionType, RepositoryFolder.class);
	}

	private String newDiagramSpecificationName;
	private String newDiagramSpecificationURI;
	private String newDiagramSpecificationDescription;

	private DiagramSpecification newDiagramSpecification;

	CreateDiagramSpecification(RepositoryFolder focusedObject, Vector<ViewPointObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws IOFlexoException {

		newDiagramSpecification = DiagramSpecificationImpl.newDiagramSpecification(newDiagramSpecificationURI, newDiagramSpecificationName,
				getFocusedObject(), getServiceManager());
		newDiagramSpecification.setDescription(newDiagramSpecificationDescription);
		// getFocusedObject().addToVirtualModels(newDiagramSpecification);
		getFocusedObject().getResourceRepository().registerResource(newDiagramSpecification.getResource());
		getFocusedObject().addToResources(newDiagramSpecification.getResource());
	}

	public boolean isNewDiagramSpecificationNameValid() {
		if (StringUtils.isEmpty(newDiagramSpecificationName)) {
			errorMessage = "please_supply_valid_diagram_specification_name";
			return false;
		}
		return true;
	}

	public boolean isNewDiagramSpecificationUriValid() {
		if (StringUtils.isEmpty(newDiagramSpecificationURI)) {
			errorMessage = "please_supply_valid_diagram_specification_uri";
			return false;
		}
		try {
			new URL(newDiagramSpecificationURI);
		} catch (MalformedURLException e) {
			errorMessage = "malformed_uri";
			return false;
		}
		if (getFocusedObject().getResourceRepository() == null) {
			errorMessage = "could_not_access_registered_resources";
			return false;
		}
		if (getFocusedObject().getResourceRepository().getResource(newDiagramSpecificationURI) != null) {
			errorMessage = "already_existing_diagram_specification_uri";
			return false;
		}
		return true;
	}

	public String errorMessage;

	@Override
	public boolean isValid() {
		if (!isNewDiagramSpecificationNameValid()) {
			return false;
		}
		if (!isNewDiagramSpecificationUriValid()) {
			return false;
		}
		return true;
	}

	public DiagramSpecification getNewDiagramSpecification() {
		return newDiagramSpecification;
	}

	public String getNewDiagramSpecificationName() {
		return newDiagramSpecificationName;
	}

	public void setNewDiagramSpecificationName(String newDiagramSpecificationName) {
		this.newDiagramSpecificationName = newDiagramSpecificationName;
	}

	public String getNewDiagramSpecificationURI() {
		return newDiagramSpecificationURI;
	}

	public void setNewDiagramSpecificationURI(String newDiagramSpecificationURI) {
		this.newDiagramSpecificationURI = newDiagramSpecificationURI;
	}

	public String getNewDiagramSpecificationDescription() {
		return newDiagramSpecificationDescription;
	}

	public void setNewDiagramSpecificationDescription(String newDiagramSpecificationDescription) {
		this.newDiagramSpecificationDescription = newDiagramSpecificationDescription;
	}

}
