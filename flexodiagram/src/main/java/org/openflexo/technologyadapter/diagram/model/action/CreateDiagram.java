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
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.model.exceptions.ModelDefinitionException;
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

	public static FlexoActionFactory<CreateDiagram, RepositoryFolder, FlexoObject> actionType = new FlexoActionFactory<CreateDiagram, RepositoryFolder, FlexoObject>(
			"create_diagram", FlexoActionFactory.newMenu, FlexoActionFactory.defaultGroup, FlexoActionFactory.ADD_ACTION_TYPE) {

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
	public LocalizedDelegate getLocales() {
		if (getServiceManager() != null) {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class).getLocales();
		}
		return super.getLocales();
	}

	@Override
	protected void doAction(Object context) throws FlexoException {

		/*DiagramTechnologyAdapter diagramTA = getServiceManager().getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);
		
		FlexoResourceCenter<?> rc = getFocusedObject().getResourceRepository().getResourceCenter();
		
		diagramTA.getDiagramResourceFactory().makeResource(serializationArtefact, resourceCenter, technologyContextManager, createEmptyContents)
		
		diagramResource = diagramTA.createNewDiagram(getDiagramName(), getDiagramURI(), getDiagramFile(),
				getDiagramSpecification() != null ? getDiagramSpecification().getResource() : null,
				getFocusedObject().getResourceRepository().getResourceCenter());
		
		getFocusedObject().addToResources(diagramResource);
		
		diagramResource.save(null);*/

		try {
			diagramResource = _makeDiagram();
		} catch (ModelDefinitionException e) {
			throw new FlexoException(e);
		}
	}

	private <I> DiagramResource _makeDiagram() throws SaveResourceException, ModelDefinitionException {
		DiagramTechnologyAdapter diagramTA = getServiceManager().getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);

		return diagramTA.getDiagramResourceFactory().makeDiagramResource(getDiagramName(), getDiagramURI(),
				getDiagramSpecification().getResource(), getFocusedObject(), true);
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

		// TODO: handle duplicated name and uri
		return true;
	}

	public String noDiagramSpecificationSelectedMessage() {
		return getLocales().localizedForKey("no_diagram_type_selected");
	}

	public String noTitleMessage() {
		return getLocales().localizedForKey("no_diagram_title_defined");
	}

	public String noFileMessage() {
		return getLocales().localizedForKey("no_diagram_file_defined");
	}

	public String existingFileMessage() {
		return getLocales().localizedForKey("file_already_existing");
	}

	public String noNameMessage() {
		return getLocales().localizedForKey("no_diagram_name_defined");
	}

	public String invalidNameMessage() {
		return getLocales().localizedForKey("invalid_name_for_new_diagram");
	}

	public String duplicatedNameMessage() {
		return getLocales().localizedForKey("a_diagram_with_that_name_already_exists");
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

	/*public File getDiagramFile() {
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
	}*/

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
