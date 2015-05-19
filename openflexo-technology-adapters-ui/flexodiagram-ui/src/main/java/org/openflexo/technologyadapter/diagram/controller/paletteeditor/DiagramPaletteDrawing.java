/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.controller.paletteeditor;

import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.FGEModelFactory;
import org.openflexo.fge.GRBinding.DrawingGRBinding;
import org.openflexo.fge.GRBinding.ShapeGRBinding;
import org.openflexo.fge.GRProvider.DrawingGRProvider;
import org.openflexo.fge.GRProvider.ShapeGRProvider;
import org.openflexo.fge.GRStructureVisitor;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.impl.DrawingImpl;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteFactory;
import org.openflexo.toolbox.ToolBox;

public class DiagramPaletteDrawing extends DrawingImpl<DiagramPalette> {

	private static final Logger logger = Logger.getLogger(DiagramPaletteDrawing.class.getPackage().getName());

	public DiagramPaletteDrawing(DiagramPalette model, boolean readOnly) {
		super(model, model.getResource().getFactory(), PersistenceMode.UniqueGraphicalRepresentations);
		setEditable(!readOnly);
	}

	@Override
	public void init() {

		final DrawingGRBinding<DiagramPalette> paletteBinding = bindDrawing(DiagramPalette.class, "palette",
				new DrawingGRProvider<DiagramPalette>() {
					@Override
					public DrawingGraphicalRepresentation provideGR(DiagramPalette drawable, FGEModelFactory factory) {
						return retrieveGraphicalRepresentation(drawable, (DiagramPaletteFactory) factory);
					}
				});
		final ShapeGRBinding<DiagramPaletteElement> elementBinding = bindShape(DiagramPaletteElement.class, "paletteElement",
				new ShapeGRProvider<DiagramPaletteElement>() {
					@Override
					public ShapeGraphicalRepresentation provideGR(DiagramPaletteElement drawable, FGEModelFactory factory) {
						return retrieveGraphicalRepresentation(drawable, (DiagramPaletteFactory) factory);
					}
				});

		paletteBinding.addToWalkers(new GRStructureVisitor<DiagramPalette>() {

			@Override
			public void visit(DiagramPalette palette) {
				for (DiagramPaletteElement element : palette.getElements()) {
					drawShape(elementBinding, element, palette);
				}
			}
		});

		elementBinding.setDynamicPropertyValue(GraphicalRepresentation.TEXT, new DataBinding<String>("drawable.name"), true);

	}

	@Override
	public void delete() {
		super.delete();
	}

	public DiagramPalette getDiagramPalette() {
		return getModel();
	}

	private DrawingGraphicalRepresentation retrieveGraphicalRepresentation(DiagramPalette palette, DiagramPaletteFactory factory) {
		DrawingGraphicalRepresentation returned = null;
		if (palette.getGraphicalRepresentation() != null) {
			palette.getGraphicalRepresentation().setFactory(factory);
			returned = palette.getGraphicalRepresentation();
		} else {
			returned = factory.makeDrawingGraphicalRepresentation();
			palette.setGraphicalRepresentation(returned);
		}
		returned.addToMouseClickControls(new DiagramPaletteEditor.ShowContextualMenuControl(factory.getEditingContext()));
		if (ToolBox.getPLATFORM() != ToolBox.MACOS) {
			returned.addToMouseClickControls(new DiagramPaletteEditor.ShowContextualMenuControl(factory.getEditingContext(), true));
		}
		return returned;
	}

	private ShapeGraphicalRepresentation retrieveGraphicalRepresentation(DiagramPaletteElement element, DiagramPaletteFactory factory) {
		ShapeGraphicalRepresentation returned = null;
		if (element.getGraphicalRepresentation() != null) {
			element.getGraphicalRepresentation().setFactory(factory);
			returned = element.getGraphicalRepresentation();
		} else {
			returned = factory.makeShapeGraphicalRepresentation();
			element.setGraphicalRepresentation(returned);
		}
		returned.addToMouseClickControls(new DiagramPaletteEditor.ShowContextualMenuControl(factory.getEditingContext()));
		if (ToolBox.getPLATFORM() != ToolBox.MACOS) {
			returned.addToMouseClickControls(new DiagramPaletteEditor.ShowContextualMenuControl(factory.getEditingContext(), true));
		}
		return returned;
	}

}
