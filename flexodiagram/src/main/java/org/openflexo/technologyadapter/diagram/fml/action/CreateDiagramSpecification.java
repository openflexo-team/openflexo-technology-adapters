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

package org.openflexo.technologyadapter.diagram.fml.action;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationRepository;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResourceFactory;
import org.openflexo.toolbox.StringUtils;

public class CreateDiagramSpecification extends FlexoAction<CreateDiagramSpecification, RepositoryFolder, FMLObject> {

	private static final Logger logger = Logger.getLogger(CreateDiagramSpecification.class.getPackage().getName());

	public static FlexoActionFactory<CreateDiagramSpecification, RepositoryFolder, FMLObject> actionType = new FlexoActionFactory<CreateDiagramSpecification, RepositoryFolder, FMLObject>(
			"create_diagram_specification", FlexoActionFactory.newMenu, FlexoActionFactory.defaultGroup,
			FlexoActionFactory.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateDiagramSpecification makeNewAction(RepositoryFolder focusedObject, Vector<FMLObject> globalSelection,
				FlexoEditor editor) {
			return new CreateDiagramSpecification(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(RepositoryFolder object, Vector<FMLObject> globalSelection) {
			if (object != null && object.getResourceRepository() instanceof DiagramSpecificationRepository) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isEnabledForSelection(RepositoryFolder object, Vector<FMLObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateDiagramSpecification.actionType, RepositoryFolder.class);
	}

	private String newDiagramSpecificationName;
	private String newDiagramSpecificationURI;
	private String newDiagramSpecificationDescription;

	private boolean makeDefaultExampleDiagram = false;

	private DiagramSpecification newDiagramSpecification;

	private CreateDiagramSpecification(RepositoryFolder focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws FlexoException {

		DiagramSpecificationResource newDSResource;
		try {
			newDSResource = _makeDiagramSpecification();
		} catch (SaveResourceException e) {
			throw new FlexoException(e);
		} catch (ModelDefinitionException e) {
			throw new FlexoException(e);
		}

		newDiagramSpecification = newDSResource.getLoadedResourceData();
		newDiagramSpecification.setDescription(newDiagramSpecificationDescription);

		newDSResource.save(null);

		if (makeDefaultExampleDiagram()) {
			CreateExampleDiagram createDiagramAction = CreateExampleDiagram.actionType.makeNewEmbeddedAction(newDiagramSpecification, null,
					this);
			createDiagramAction.setNewDiagramName("DefaultExampleDiagram");
			createDiagramAction.setNewDiagramTitle("Default example diagram");
			createDiagramAction.doAction();
		}
	}

	protected <I> DiagramSpecificationResource _makeDiagramSpecification() throws SaveResourceException, ModelDefinitionException {
		DiagramTechnologyAdapter diagramTA = getServiceManager().getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);

		return diagramTA.getDiagramSpecificationResourceFactory().makeDiagramSpecificationResourceResource(newDiagramSpecificationName,
				getNewDiagramSpecificationURI(), getFocusedObject(), true);

	}

	public boolean isNewDiagramSpecificationNameValid() {
		if (StringUtils.isEmpty(newDiagramSpecificationName)) {
			return false;
		}
		return true;
	}

	public boolean isNewDiagramSpecificationUriValid() {
		if (StringUtils.isEmpty(getNewDiagramSpecificationURI())) {
			return false;
		}
		try {
			new URL(getNewDiagramSpecificationURI());
		} catch (MalformedURLException e) {
			return false;
		}
		if (getFocusedObject().getResourceRepository() == null) {
			return false;
		}
		if (getFocusedObject().getResourceRepository().getResource(getNewDiagramSpecificationURI()) != null) {
			return false;
		}
		return true;
	}

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
		if ((newDiagramSpecificationName == null && this.newDiagramSpecificationName != null)
				|| (newDiagramSpecificationName != null && !newDiagramSpecificationName.equals(this.newDiagramSpecificationName))) {
			String oldValue = this.newDiagramSpecificationName;
			this.newDiagramSpecificationName = newDiagramSpecificationName;
			getPropertyChangeSupport().firePropertyChange("newDiagramSpecificationName", oldValue, newDiagramSpecificationName);
			getPropertyChangeSupport().firePropertyChange("newDiagramSpecificationURI", null, getNewDiagramSpecificationURI());
		}
	}

	public String getNewDiagramSpecificationURI() {
		if (newDiagramSpecificationURI == null) {
			String baseURI = getFocusedObject().getDefaultBaseURI();
			if (!baseURI.endsWith("/")) {
				baseURI = baseURI + "/";
			}
			return baseURI + getNewDiagramSpecificationName() + DiagramSpecificationResourceFactory.DIAGRAM_SPECIFICATION_SUFFIX;
		}
		return newDiagramSpecificationURI;
	}

	public void setNewDiagramSpecificationURI(String newDiagramSpecificationURI) {
		if ((newDiagramSpecificationURI == null && this.newDiagramSpecificationURI != null)
				|| (newDiagramSpecificationURI != null && !newDiagramSpecificationURI.equals(this.newDiagramSpecificationURI))) {
			String oldValue = this.newDiagramSpecificationURI;
			this.newDiagramSpecificationURI = newDiagramSpecificationURI;
			getPropertyChangeSupport().firePropertyChange("newDiagramSpecificationURI", oldValue, newDiagramSpecificationURI);
		}
	}

	public String getNewDiagramSpecificationDescription() {
		return newDiagramSpecificationDescription;
	}

	public void setNewDiagramSpecificationDescription(String newDiagramSpecificationDescription) {
		if ((newDiagramSpecificationDescription == null && this.newDiagramSpecificationDescription != null)
				|| (newDiagramSpecificationDescription != null
						&& !newDiagramSpecificationDescription.equals(this.newDiagramSpecificationDescription))) {
			String oldValue = this.newDiagramSpecificationDescription;
			this.newDiagramSpecificationDescription = newDiagramSpecificationDescription;
			getPropertyChangeSupport().firePropertyChange("newDiagramSpecificationDescription", oldValue,
					newDiagramSpecificationDescription);
		}
	}

	public boolean makeDefaultExampleDiagram() {
		return makeDefaultExampleDiagram;
	}

	public void setMakeDefaultExampleDiagram(boolean makeDefaultExampleDiagram) {
		if (makeDefaultExampleDiagram != this.makeDefaultExampleDiagram) {
			this.makeDefaultExampleDiagram = makeDefaultExampleDiagram;
			getPropertyChangeSupport().firePropertyChange("makeDefaultExampleDiagram", !makeDefaultExampleDiagram,
					makeDefaultExampleDiagram);
		}
	}
}
