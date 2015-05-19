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

package org.openflexo.technologyadapter.diagram.model.action;

import java.io.File;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.resource.InvalidFileNameException;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.rm.DiagramRepository;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.toolbox.JavaUtils;
import org.openflexo.toolbox.StringUtils;

/**
 * This action is called to create a new {@link Diagram} in a repository folder
 * 
 * @author sylvain
 */
public class CreateDiagram extends FlexoAction<CreateDiagram, RepositoryFolder, FlexoObject> {

	private static final Logger logger = Logger.getLogger(CreateDiagram.class.getPackage().getName());

	public static FlexoActionType<CreateDiagram, RepositoryFolder, FlexoObject> actionType = new FlexoActionType<CreateDiagram, RepositoryFolder, FlexoObject>(
			"create_diagram", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateDiagram makeNewAction(RepositoryFolder focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
			return new CreateDiagram(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(RepositoryFolder object, Vector<FlexoObject> globalSelection) {
			if (object != null && object.getResourceRepository() instanceof DiagramRepository) {
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
		FlexoObjectImpl.addActionForClass(CreateDiagram.actionType, RepositoryFolder.class);
	}

	public boolean skipChoosePopup = false;

	private DiagramSpecification diagramSpecification;
	private String diagramName;
	private String diagramTitle;
	private String diagramURI;
	private File diagramFile;

	private DiagramResource diagramResource;

	private String description;

	CreateDiagram(RepositoryFolder focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws InvalidFileNameException, SaveResourceException, InvalidArgumentException {

		DiagramTechnologyAdapter diagramTA = getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(
				DiagramTechnologyAdapter.class);

		diagramResource = diagramTA.createNewDiagram(getDiagramName(), getDiagramURI(), getDiagramFile(),
				getDiagramSpecification() != null ? getDiagramSpecification().getResource() : null);

		getFocusedObject().addToResources(diagramResource);

		diagramResource.save(null);

		/*	newVirtualModelInstanceName = JavaUtils.getClassName(newVirtualModelInstanceName);

			if (StringUtils.isNotEmpty(newVirtualModelInstanceName) && StringUtils.isEmpty(newVirtualModelInstanceTitle)) {
				newVirtualModelInstanceTitle = newVirtualModelInstanceName;
			}

			if (StringUtils.isEmpty(newVirtualModelInstanceName)) {
				throw new InvalidParameterException("virtual model instance name is undefined");
			}

			int index = 1;
			String baseName = newVirtualModelInstanceName;
			while (!getFocusedObject().isValidVirtualModelName(newVirtualModelInstanceName)) {
				newVirtualModelInstanceName = baseName + index;
				index++;
			}

			VirtualModelInstanceResource newVirtualModelInstanceResource = makeVirtualModelInstanceResource();

			newVirtualModelInstance = newVirtualModelInstanceResource.getVirtualModelInstance();

			logger.info("Added virtual model instance " + newVirtualModelInstance + " in view " + getFocusedObject());

			System.out.println("OK, we have created the file " + newVirtualModelInstanceResource.getFile().getAbsolutePath());

			for (ModelSlot ms : virtualModel.getModelSlots()) {
				ModelSlotInstanceConfiguration<?, ?> configuration = getModelSlotInstanceConfiguration(ms);
				if (configuration.isValidConfiguration()) {
					newVirtualModelInstance.addToModelSlotInstances(configuration.createModelSlotInstance(newVirtualModelInstance));
				} else {
					throw new InvalidArgumentException("Wrong configuration for model slot " + configuration.getModelSlot() + " configuration="
							+ configuration);
				}
			}

			if (creationSchemeAction != null) {
				creationSchemeAction.initWithFlexoConceptInstance(newVirtualModelInstance);
				creationSchemeAction.doAction();
			}

			System.out.println("Now, we try to synchronize the new virtual model instance");

			if (newVirtualModelInstance.isSynchronizable()) {
				System.out.println("Go for it");
				newVirtualModelInstance.synchronize(null);
			}

			System.out.println("Saving file again...");
			newVirtualModelInstanceResource.save(null);*/
	}

	/*public DiagramResource makeDiagramResource() throws InvalidFileNameException, SaveResourceException {
		return DiagramImpl.newDiagramResource(getDiagramName(), getDiagramTitle(), getDiagramURI(), getDiagramFile(),
				getDiagramSpecification(), getServiceManager());
	}*/

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
		if (StringUtils.isEmpty(diagramName)) {
			errorMessage = noNameMessage();
			return false;
		}

		if (!diagramName.equals(JavaUtils.getClassName(diagramName)) && !diagramName.equals(JavaUtils.getVariableName(diagramName))) {
			errorMessage = invalidNameMessage();
			return false;
		}

		if (StringUtils.isEmpty(diagramTitle)) {
			errorMessage = noTitleMessage();
			return false;
		}

		if (getDiagramFile() == null) {

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

	public DiagramSpecification getDiagramSpecification() {
		return diagramSpecification;
	}

	public void setDiagramSpecification(DiagramSpecification diagramSpecification) {
		this.diagramSpecification = diagramSpecification;
	}

	public Diagram getNewDiagram() {
		if (getNewDiagramResource() != null) {
			return getNewDiagramResource().getDiagram();
		}
		return null;
	}

	public DiagramResource getNewDiagramResource() {
		return diagramResource;
	}

	public String getDiagramName() {
		return diagramName;
	}

	public void setDiagramName(String diagramName) {
		boolean wasValid = isValid();
		this.diagramName = diagramName;
		getPropertyChangeSupport().firePropertyChange("diagramName", null, diagramName);
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

	public String getDiagramTitle() {
		return diagramTitle;
	}

	public void setDiagramTitle(String diagramTitle) {
		boolean wasValid = isValid();
		this.diagramTitle = diagramTitle;
		getPropertyChangeSupport().firePropertyChange("diagramTitle", null, diagramTitle);
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

	public String getDiagramURI() {
		if (diagramURI == null) {
			return getDefaultDiagramURI();
		}
		return diagramURI;
	}

	public void setDiagramURI(String diagramURI) {
		this.diagramURI = diagramURI;
	}

	public String getDefaultDiagramURI() {
		return getFocusedObject().getResourceRepository().generateURI(getDiagramName());
	}

	public File getDiagramFile() {
		if (diagramFile == null) {
			return getDefaultDiagramFile();
		}
		return diagramFile;
	}

	public void setDiagramFile(File diagramFile) {
		this.diagramFile = diagramFile;
	}

	public File getDefaultDiagramFile() {
		return new File(getFocusedObject().getFile(), getDiagramName() + DiagramResource.DIAGRAM_SUFFIX);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
