/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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

import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;

public class CreateFMLControlledDiagramPaletteElement extends
		FlexoAction<CreateFMLControlledDiagramPaletteElement, VirtualModel, FMLObject> {

	private static final Logger logger = Logger.getLogger(CreateFMLControlledDiagramPaletteElement.class.getPackage().getName());

	public static FlexoActionFactory<CreateFMLControlledDiagramPaletteElement, VirtualModel, FMLObject> actionType = new FlexoActionFactory<CreateFMLControlledDiagramPaletteElement, VirtualModel, FMLObject>(
			"add_new_palette_element", FlexoActionFactory.newMenu, FlexoActionFactory.defaultGroup, FlexoActionFactory.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateFMLControlledDiagramPaletteElement makeNewAction(VirtualModel focusedObject,
				Vector<FMLObject> globalSelection, FlexoEditor editor) {
			return new CreateFMLControlledDiagramPaletteElement(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(VirtualModel object, Vector<FMLObject> globalSelection) {
			return object.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE);
		}

		@Override
		public boolean isEnabledForSelection(VirtualModel object, Vector<FMLObject> globalSelection) {
			return object.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE);
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateFMLControlledDiagramPaletteElement.actionType, VirtualModel.class);
	}

	private String newElementName;
	private DiagramPaletteElement newElement;
	private ShapeGraphicalRepresentation graphicalRepresentation;
	private FlexoConcept concept;
	private DropScheme dropScheme;
	private DiagramPalette palette;

	CreateFMLControlledDiagramPaletteElement(VirtualModel focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws NotImplementedException, InvalidParameterException {

		if (getPalette().getResource() != null) {
			newElement = getPalette().getResource().getFactory().makeDiagramPaletteElement();
			newElement.setName(getNewElementName());
			newElement.setGraphicalRepresentation(getGraphicalRepresentation());

			TypedDiagramModelSlot ms = FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(getFocusedObject());
			CreateFMLDiagramPaletteElementBinding createBinding = CreateFMLDiagramPaletteElementBinding.actionType.makeNewEmbeddedAction(
					ms, null, this);
			createBinding.setDiagramPalette(getPalette());
			createBinding.setDiagramPaletteElement(getNewElement());
			createBinding.setFlexoConcept(getConcept());
			createBinding.setDropScheme(getDropScheme());

			createBinding.doAction();

			getPalette().addToElements(newElement);
		}

	}

	public DiagramPaletteElement getNewElement() {
		return newElement;
	}

	private String computeNewName() {
		String newName = getConcept().getName();
		while (getPalette().getPaletteElement(newName) != null) {
			newName = newName + "_new";
		}
		return newName;
	}

	public String getNewElementName() {
		if (newElementName == null && getConcept().getName() != null) {
			newElementName = computeNewName();
		}
		return newElementName;
	}

	public void setNewElementName(String newElementName) {
		this.newElementName = newElementName;
	}

	public ShapeGraphicalRepresentation getGraphicalRepresentation() {
		return graphicalRepresentation;
	}

	public void setGraphicalRepresentation(ShapeGraphicalRepresentation graphicalRepresentation) {
		this.graphicalRepresentation = graphicalRepresentation;
	}

	public FlexoConcept getConcept() {
		return concept;
	}

	public void setConcept(FlexoConcept concept) {
		this.concept = concept;
	}

	public DropScheme getDropScheme() {
		return dropScheme;
	}

	public void setDropScheme(DropScheme dropScheme) {
		this.dropScheme = dropScheme;
	}

	public DiagramPalette getPalette() {
		return palette;
	}

	public void setPalette(DiagramPalette palette) {
		this.palette = palette;
	}

}
