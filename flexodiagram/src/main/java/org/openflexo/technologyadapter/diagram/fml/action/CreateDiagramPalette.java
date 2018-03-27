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
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.rm.DiagramPaletteResource;
import org.openflexo.toolbox.StringUtils;

public class CreateDiagramPalette extends FlexoAction<CreateDiagramPalette, DiagramSpecification, FlexoObject> {

	private static final Logger logger = Logger.getLogger(CreateDiagramPalette.class.getPackage().getName());

	public static FlexoActionFactory<CreateDiagramPalette, DiagramSpecification, FlexoObject> actionType = new FlexoActionFactory<CreateDiagramPalette, DiagramSpecification, FlexoObject>(
			"create_new_palette", FlexoActionFactory.newMenu, FlexoActionFactory.defaultGroup, FlexoActionFactory.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateDiagramPalette makeNewAction(DiagramSpecification focusedObject, Vector<FlexoObject> globalSelection,
				FlexoEditor editor) {
			return new CreateDiagramPalette(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramSpecification object, Vector<FlexoObject> globalSelection) {
			return object != null;
		}

		@Override
		public boolean isEnabledForSelection(DiagramSpecification object, Vector<FlexoObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateDiagramPalette.actionType, DiagramSpecification.class);
	}

	private String newPaletteName;
	private String description;
	private Object graphicalRepresentation;

	private DiagramPalette _newPalette;

	private CreateDiagramPalette(DiagramSpecification focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws InvalidParameterException, FlexoException {
		logger.info("Add diagram palette to diagram specification");

		// DiagramPaletteResource paletteResource = DiagramPaletteImpl.newDiagramPalette(getFocusedObject(), newPaletteName,
		// (DrawingGraphicalRepresentation) graphicalRepresentation, getServiceManager());

		DiagramPaletteResource paletteResource;
		try {
			paletteResource = _makeDiagramPalette();
		} catch (ModelDefinitionException e) {
			throw new FlexoException(e);
		}

		_newPalette = paletteResource.getDiagramPalette();
		_newPalette.setDescription(description);
		getFocusedObject().addToPalettes(_newPalette);
		_newPalette.getResource().save();

	}

	protected DiagramPaletteResource _makeDiagramPalette() throws SaveResourceException, ModelDefinitionException {
		DiagramTechnologyAdapter diagramTA = getServiceManager().getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);

		String paletteName = getNewPaletteName() + (getNewPaletteName().endsWith(".palette") ? "" : ".palette");

		DiagramPaletteResource newPaletteResource = diagramTA.getDiagramSpecificationResourceFactory().getPaletteResourceFactory()
				.makeDiagramPaletteResource(paletteName, getFocusedObject().getResource(), true);

		return newPaletteResource;
	}

	public DiagramPalette getNewPalette() {
		return _newPalette;
	}

	private String nameValidityMessage = null;

	private static final String NAME_IS_VALID = "name_is_valid";
	private static final String DUPLICATED_NAME = "this_name_is_already_used_please_choose_an_other_one";
	private static final String EMPTY_NAME = "empty_name";

	public String getNameValidityMessage() {
		return nameValidityMessage;
	}

	public boolean isNameValid() {
		if (StringUtils.isEmpty(newPaletteName)) {
			nameValidityMessage = getLocales().localizedForKey(EMPTY_NAME);
			return false;
		}
		else if (getFocusedObject().getPalette(newPaletteName) != null) {
			nameValidityMessage = getLocales().localizedForKey(DUPLICATED_NAME);
			;
			return false;
		}
		else {
			nameValidityMessage = getLocales().localizedForKey(NAME_IS_VALID);
			return true;
		}
	}

	public String getNewPaletteName() {
		return newPaletteName;
	}

	public void setNewPaletteName(String newPaletteName) {
		this.newPaletteName = newPaletteName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getGraphicalRepresentation() {
		return graphicalRepresentation;
	}

	public void setGraphicalRepresentation(Object graphicalRepresentation) {
		this.graphicalRepresentation = graphicalRepresentation;
	}
}
