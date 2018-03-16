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

import javax.swing.JTabbedPane;

import org.openflexo.diana.ShapeGraphicalRepresentation;
import org.openflexo.diana.ShapeGraphicalRepresentation.LocationConstraints;
import org.openflexo.diana.swing.control.SwingToolFactory;
import org.openflexo.diana.swing.control.tools.JDianaPalette;
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
			paletteView.add(
					FlexoLocalization.getMainLocalizer().localizedForKey("Common", getPalettePalette().getPaletteViewInScrollPane()),
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
