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

import java.io.File;
import java.security.InvalidParameterException;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.resource.InvalidFileNameException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.viewpoint.ViewPointObject;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramImpl;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.toolbox.JavaUtils;
import org.openflexo.toolbox.StringUtils;

public class CreateExampleDiagram extends FlexoAction<CreateExampleDiagram, DiagramSpecification, ViewPointObject> {

	private static final Logger logger = Logger.getLogger(CreateExampleDiagram.class.getPackage().getName());

	public static FlexoActionType<CreateExampleDiagram, DiagramSpecification, ViewPointObject> actionType = new FlexoActionType<CreateExampleDiagram, DiagramSpecification, ViewPointObject>(
			"create_example_diagram", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateExampleDiagram makeNewAction(DiagramSpecification focusedObject, Vector<ViewPointObject> globalSelection,
				FlexoEditor editor) {
			return new CreateExampleDiagram(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramSpecification object, Vector<ViewPointObject> globalSelection) {
			return object != null;
		}

		@Override
		public boolean isEnabledForSelection(DiagramSpecification object, Vector<ViewPointObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateExampleDiagram.actionType, DiagramSpecification.class);
	}

	private String newDiagramName;
	private String newDiagramTitle;
	private String description;
	// public DrawingGraphicalRepresentation graphicalRepresentation;

	private DiagramResource newDiagramResource;

	CreateExampleDiagram(DiagramSpecification focusedObject, Vector<ViewPointObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws NotImplementedException, InvalidParameterException, SaveResourceException,
			InvalidFileNameException {
		logger.info("Add example diagram");

		String newDiagramURI = getFocusedObject().getURI() + "/" + newDiagramName;
		File newDiagramFile = new File(getFocusedObject().getResource().getDirectory(), newDiagramName + DiagramResource.DIAGRAM_SUFFIX);
		newDiagramResource = DiagramImpl.newDiagramResource(newDiagramName, newDiagramTitle, newDiagramURI, newDiagramFile,
				getFocusedObject(), getServiceManager());
		getFocusedObject().getResource().addToContents(newDiagramResource);

		newDiagramResource.getDiagram().setDescription(description);
		newDiagramResource.save(null);

	}

	private String errorMessage;

	public String getErrorMessage() {
		isValid();
		// System.out.println("valid=" + isValid());
		// System.out.println("errorMessage=" + errorMessage);
		return errorMessage;
	}

	@Override
	public boolean isValid() {
		/*if (diagramSpecification == null) {
			errorMessage = noDiagramSpecificationSelectedMessage();
			return false;
		}*/
		if (StringUtils.isEmpty(newDiagramName)) {
			errorMessage = noNameMessage();
			return false;
		}

		if (!newDiagramName.equals(JavaUtils.getClassName(newDiagramName))
				&& !newDiagramName.equals(JavaUtils.getVariableName(newDiagramName))) {
			errorMessage = invalidNameMessage();
			return false;
		}

		if (StringUtils.isEmpty(newDiagramTitle)) {
			errorMessage = noTitleMessage();
			return false;
		}

		// TODO: handle duplicated name and uri
		return true;
	}

	public String noDiagramSpecificationSelectedMessage() {
		return FlexoLocalization.localizedForKey("no_diagram_type_selected");
	}

	public String noTitleMessage() {
		return FlexoLocalization.localizedForKey("no_diagram_title_defined");
	}

	public String noFileMessage() {
		return FlexoLocalization.localizedForKey("no_diagram_file_defined");
	}

	public String existingFileMessage() {
		return FlexoLocalization.localizedForKey("file_already_existing");
	}

	public String noNameMessage() {
		return FlexoLocalization.localizedForKey("no_diagram_name_defined");
	}

	public String invalidNameMessage() {
		return FlexoLocalization.localizedForKey("invalid_name_for_new_diagram");
	}

	public String duplicatedNameMessage() {
		return FlexoLocalization.localizedForKey("a_diagram_with_that_name_already_exists");
	}

	public Diagram getNewDiagram() {
		return newDiagramResource.getDiagram();
	}

	public String getNewDiagramName() {
		return newDiagramName;
	}

	public void setNewDiagramName(String newDiagramName) {
		this.newDiagramName = newDiagramName;
	}

	public String getNewDiagramTitle() {
		return newDiagramTitle;
	}

	public void setNewDiagramTitle(String newDiagramTitle) {
		this.newDiagramTitle = newDiagramTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}