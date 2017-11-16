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

import java.security.InvalidParameterException;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramResourceFactory;
import org.openflexo.toolbox.JavaUtils;
import org.openflexo.toolbox.StringUtils;

public class CreateExampleDiagram extends FlexoAction<CreateExampleDiagram, DiagramSpecification, FMLObject> {

	private static final Logger logger = Logger.getLogger(CreateExampleDiagram.class.getPackage().getName());

	public static FlexoActionFactory<CreateExampleDiagram, DiagramSpecification, FMLObject> actionType = new FlexoActionFactory<CreateExampleDiagram, DiagramSpecification, FMLObject>(
			"create_example_diagram", FlexoActionFactory.newMenu, FlexoActionFactory.defaultGroup, FlexoActionFactory.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateExampleDiagram makeNewAction(DiagramSpecification focusedObject, Vector<FMLObject> globalSelection,
				FlexoEditor editor) {
			return new CreateExampleDiagram(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramSpecification object, Vector<FMLObject> globalSelection) {
			return object != null;
		}

		@Override
		public boolean isEnabledForSelection(DiagramSpecification object, Vector<FMLObject> globalSelection) {
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

	CreateExampleDiagram(DiagramSpecification focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws InvalidParameterException, FlexoException {
		logger.info("Add example diagram");

		try {
			newDiagramResource = _makeDiagram();
		} catch (ModelDefinitionException e) {
			throw new FlexoException(e);
		}

		/*String newDiagramURI = getFocusedObject().getURI() + "/" + newDiagramName;
		File newDiagramFile = new File(ResourceLocator.retrieveResourceAsFile(getFocusedObject().getResource().getDirectory()),
				newDiagramName + DiagramResource.DIAGRAM_SUFFIX);
		newDiagramResource = DiagramImpl.newDiagramResource(newDiagramName, newDiagramTitle, newDiagramURI, newDiagramFile,
				getFocusedObject(), getFocusedObject().getResource().getResourceCenter(), getServiceManager());
		getFocusedObject().getResource().addToContents(newDiagramResource);
		getFocusedObject().addToExampleDiagrams(newDiagramResource.getDiagram());
		newDiagramResource.getDiagram().setDescription(description);
		newDiagramResource.save(null);*/

	}

	protected DiagramResource _makeDiagram() throws SaveResourceException, ModelDefinitionException {
		DiagramTechnologyAdapter diagramTA = getServiceManager().getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);

		String diagramName = getNewDiagramName().endsWith(DiagramResourceFactory.DIAGRAM_SUFFIX) ? getNewDiagramName()
				: getNewDiagramName() + DiagramResourceFactory.DIAGRAM_SUFFIX;

		DiagramResource newDiagramResource = diagramTA.getDiagramSpecificationResourceFactory().getExampleDiagramsResourceFactory()
				.makeExampleDiagramResource(diagramName, getFocusedObject().getResource(), true);

		return newDiagramResource;
	}

	@Override
	public boolean isValid() {
		if (StringUtils.isEmpty(getNewDiagramName())) {
			return false;
		}

		if (!getNewDiagramName().equals(JavaUtils.getClassName(getNewDiagramName()))
				&& !getNewDiagramName().equals(JavaUtils.getVariableName(getNewDiagramName()))) {
			return false;
		}

		if (StringUtils.isEmpty(newDiagramTitle)) {
			return false;
		}

		if (getFocusedObject().getExampleDiagram(getNewDiagramName()) != null) {
			return false;
		}

		return true;
	}

	public Diagram getNewDiagram() {
		return newDiagramResource.getDiagram();
	}

	public String getNewDiagramName() {
		return newDiagramName;
	}

	public void setNewDiagramName(String newDiagramName) {
		if ((newDiagramName == null && this.newDiagramName != null)
				|| (newDiagramName != null && !newDiagramName.equals(this.newDiagramName))) {
			String oldValue = this.newDiagramName;
			this.newDiagramName = newDiagramName;
			getPropertyChangeSupport().firePropertyChange("newDiagramName", oldValue, newDiagramName);
		}
	}

	public String getNewDiagramTitle() {
		return newDiagramTitle;
	}

	public void setNewDiagramTitle(String newDiagramTitle) {
		if ((newDiagramTitle == null && this.newDiagramTitle != null)
				|| (newDiagramTitle != null && !newDiagramTitle.equals(this.newDiagramTitle))) {
			String oldValue = this.newDiagramTitle;
			this.newDiagramTitle = newDiagramTitle;
			getPropertyChangeSupport().firePropertyChange("newDiagramTitle", oldValue, newDiagramTitle);
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if ((description == null && this.description != null) || (description != null && !description.equals(this.description))) {
			String oldValue = this.description;
			this.description = description;
			getPropertyChangeSupport().firePropertyChange("description", oldValue, description);
		}
	}

}
