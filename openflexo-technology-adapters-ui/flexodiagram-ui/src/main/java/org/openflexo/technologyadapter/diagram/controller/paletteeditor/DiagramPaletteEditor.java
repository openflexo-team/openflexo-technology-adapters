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
package org.openflexo.technologyadapter.diagram.controller.paletteeditor;

import java.util.logging.Logger;

import javax.swing.JTabbedPane;

import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation.LocationConstraints;
import org.openflexo.fge.swing.control.SwingToolFactory;
import org.openflexo.fge.swing.control.tools.JDianaPalette;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.selection.SelectionManagingDianaEditor;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteFactory;
import org.openflexo.view.controller.FlexoController;

public class DiagramPaletteEditor extends SelectionManagingDianaEditor<DiagramPalette> {

	private static final Logger logger = Logger.getLogger(DiagramPaletteEditor.class.getPackage().getName());

	private final FlexoController flexoController;
	private DiagramPalettePalette palettePaletteModel;
	private JDianaPalette palettePalette;
	private DiagramPaletteModuleView moduleView;

	private final SwingToolFactory swingToolFactory;

	public DiagramPaletteEditor(DiagramPalette palette, boolean readOnly, FlexoController controller, SwingToolFactory swingToolFactory) {
		super(new DiagramPaletteDrawing(palette, readOnly), controller.getSelectionManager(), palette.getResource().getFactory(),
				swingToolFactory);
		flexoController = controller;
		this.swingToolFactory = swingToolFactory;

		if (!readOnly) {
			palettePaletteModel = new DiagramPalettePalette(this);
			palettePalette = swingToolFactory.makeDianaPalette(palettePaletteModel);
			palettePalette.attachToEditor(this);
		}
		moduleView = new DiagramPaletteModuleView(this, flexoController.getCurrentPerspective());
	}

	// Only used for screenshot
	public DiagramPaletteEditor(DiagramPalette palette, boolean readOnly) {
		super(new DiagramPaletteDrawing(palette, readOnly), null, palette.getResource().getFactory(), null);
		flexoController = null;
		this.swingToolFactory = null;
		if (!readOnly) {
			palettePaletteModel = new DiagramPalettePalette(this);
			palettePalette = swingToolFactory.makeDianaPalette(palettePaletteModel);
			palettePalette.attachToEditor(this);
		}
	}

	@Override
	public void delete() {
		/*if (flexoController != null) {
			if (getDrawingView() != null && moduleView != null) {
				flexoController.removeModuleView(moduleView);
			}
		}
		getDrawing().delete();
		getPalettePalette().delete();
		super.delete();*/
		if (flexoController != null) {
			if (getDrawingView() != null && moduleView != null) {
				flexoController.removeModuleView(moduleView);
			}
		}
		super.delete();
		getDrawing().delete();
		// flexoController.removeModuleView(moduleView);
	}

	public FlexoController getFlexoController() {
		return flexoController;
	}

	@Override
	public DiagramPaletteFactory getFactory() {
		return (DiagramPaletteFactory) super.getFactory();
	}

	public DiagramPaletteModuleView getModuleView() {
		if (moduleView == null && flexoController != null) {
			moduleView = new DiagramPaletteModuleView(this, flexoController.getCurrentPerspective());
		}
		return moduleView;
	}

	public JDianaPalette getPalettePalette() {
		return palettePalette;
	}

	private JTabbedPane paletteView;

	public JTabbedPane getPaletteView() {
		if (paletteView == null) {
			paletteView = new JTabbedPane();
			paletteView.add(FlexoLocalization.localizedForKey("Common", getPalettePalette().getPaletteViewInScrollPane()),
					getPalettePalette().getPaletteViewInScrollPane());
		}
		return paletteView;
	}

	public DiagramPalette getDiagramPalette() {
		// Temporary code to Ensure GRs can be edited
		DiagramPalette diagramPalette = getDrawing().getRoot().getDrawable();
		if (diagramPalette != null && diagramPalette.getElements() != null) {
			for (DiagramPaletteElement pal : diagramPalette.getElements()) {
				ShapeGraphicalRepresentation gr = pal.getGraphicalRepresentation();
				if (gr != null) {
					gr.setIsFocusable(true);
					gr.setIsSelectable(true);
					gr.setIsReadOnly(false);
					gr.setLocationConstraints(LocationConstraints.FREELY_MOVABLE);
				}
				else {
					logger.warning(" INVESTIGATE : trying to update an null GraphicalRepresentation for PaletteElement: " + pal.getName());
				}
			}
		}

		return getDrawing().getModel();
	}
}
