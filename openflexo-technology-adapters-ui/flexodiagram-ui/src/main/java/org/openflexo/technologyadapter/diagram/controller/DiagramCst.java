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
package org.openflexo.technologyadapter.diagram.controller;

import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;

/**
 * Constants used by the DiagramTechnologyAdapter UI
 * 
 * @author sylvain
 */
public class DiagramCst {

	// UI components
	public static Resource DIAGRAM_FLEXO_CONCEPT_VIEW_FIB = ResourceLocator.locateResource("Fib/DiagramFlexoConceptView.fib");
	public static Resource DIAGRAM_SPECIFICATION_VIEW_FIB = ResourceLocator.locateResource("Fib/DiagramSpecificationView.fib");

	// Dialog components
	public static Resource CREATE_DIAGRAM_DIALOG_FIB = ResourceLocator.locateResource("Fib/Dialog/CreateDiagramDialog.fib");
	public static Resource CREATE_EXAMPLE_DIAGRAM_DIALOG_FIB = ResourceLocator.locateResource("Fib/Dialog/CreateExampleDiagramDialog.fib");
	public static Resource CREATE_EXAMPLE_DIAGRAM_FROM_PPTSLIDE_DIALOG_FIB = ResourceLocator
			.locateResource("Fib/Dialog/CreateDiagramFromPPTDialog.fib");
	public static Resource CREATE_PALETTE_DIALOG_FIB = ResourceLocator.locateResource("Fib/Dialog/CreatePaletteDialog.fib");
	public static Resource CREATE_DIAGRAM_SPECIFICATION_DIALOG_FIB = ResourceLocator
			.locateResource("Fib/Dialog/CreateDiagramSpecificationDialog.fib");
	public static Resource IMPORT_IMAGE_FILE_DIALOG_FIB = ResourceLocator.locateResource("Fib/Dialog/ImportImageFileDialog.fib");
	public static Resource DELETE_DIAGRAM_ELEMENTS_DIALOG_FIB = ResourceLocator
			.locateResource("Fib/Dialog/DeleteDiagramElementsDialog.fib");
	public static Resource REINDEX_DIAGRAM_ELEMENTS_DIALOG_FIB = ResourceLocator
			.locateResource("Fib/Dialog/ReindexDiagramElementsDialog.fib");

	public static Resource DECLARE_SHAPE_IN_FLEXO_CONCEPT_DIALOG_FIB = ResourceLocator
			.locateResource("Fib/Dialog/DeclareShapeInFlexoConceptDialog.fib");
	public static Resource DECLARE_CONNECTOR_IN_FLEXO_CONCEPT_DIALOG_FIB = ResourceLocator
			.locateResource("Fib/Dialog/DeclareConnectorInFlexoConceptDialog.fib");
	public static Resource PUSH_TO_PALETTE_DIALOG_FIB = ResourceLocator.locateResource("Fib/Dialog/PushToPaletteDialog.fib");

}
