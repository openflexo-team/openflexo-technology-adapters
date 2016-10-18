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
	// public static Resource DIAGRAM_FLEXO_CONCEPT_VIEW_FIB = ResourceLocator.locateResource("Fib/DiagramFlexoConceptView.fib");
	public static Resource DIAGRAM_SPECIFICATION_VIEW_FIB = ResourceLocator.locateResource("Fib/DiagramSpecificationView.fib");
	public static Resource FML_CONTROLLED_VIRTUAL_MODEL_VIEW_FIB = ResourceLocator.locateResource("Fib/FMLControlledVirtualModelView.fib");

	// Dialog components
	public static Resource CREATE_DIAGRAM_DIALOG_FIB = ResourceLocator.locateResource("Fib/Dialog/CreateDiagramDialog.fib");
	public static Resource CREATE_EXAMPLE_DIAGRAM_DIALOG_FIB = ResourceLocator.locateResource("Fib/Dialog/CreateExampleDiagramDialog.fib");
	public static Resource CREATE_DIAGRAM_FROM_PPTSLIDE_DIALOG_FIB = ResourceLocator
			.locateResource("Fib/Dialog/CreateDiagramFromPPTDialog.fib");
	public static Resource CREATE_EXAMPLE_DIAGRAM_FROM_PPTSLIDE_DIALOG_FIB = ResourceLocator
			.locateResource("Fib/Dialog/CreateExampleDiagramFromPPTDialog.fib");
	public static Resource CREATE_PALETTE_DIALOG_FIB = ResourceLocator.locateResource("Fib/Dialog/CreatePaletteDialog.fib");
	public static Resource CREATE_DIAGRAM_SPECIFICATION_DIALOG_FIB = ResourceLocator
			.locateResource("Fib/Dialog/CreateDiagramSpecificationDialog.fib");
	public static Resource IMPORT_IMAGE_FILE_DIALOG_FIB = ResourceLocator.locateResource("Fib/Dialog/ImportImageFileDialog.fib");
	public static Resource DELETE_DIAGRAM_ELEMENTS_DIALOG_FIB = ResourceLocator
			.locateResource("Fib/Dialog/DeleteDiagramElementsDialog.fib");
	public static Resource REINDEX_DIAGRAM_ELEMENTS_DIALOG_FIB = ResourceLocator
			.locateResource("Fib/Dialog/ReindexDiagramElementsDialog.fib");
	public static Resource DELETE_DIAGRAM_ELEMENTS_AND_FLEXO_CONCEPT_INSTANCES_DIALOG_FIB = ResourceLocator
			.locateResource("Fib/Dialog/DeleteDiagramElementsAndFlexoConceptInstancesDialog.fib");

	// @Deprecated
	// public static Resource DECLARE_SHAPE_IN_FLEXO_CONCEPT_DIALOG_FIB = ResourceLocator
	// .locateResource("Fib/Dialog/DeclareShapeInFlexoConceptDialog.fib");
	// @Deprecated
	// public static Resource DECLARE_CONNECTOR_IN_FLEXO_CONCEPT_DIALOG_FIB = ResourceLocator
	// .locateResource("Fib/Dialog/DeclareConnectorInFlexoConceptDialog.fib");
	public static Resource PUSH_TO_PALETTE_DIALOG_FIB = ResourceLocator.locateResource("Fib/Dialog/PushToPaletteDialog.fib");
	public static Resource CREATE_FML_DIAGRAM_PALETTE_ELEMENT_BINDING_DIALOG_FIB = ResourceLocator
			.locateResource("Fib/Dialog/CreateFMLPaletteElementBindingDialog.fib");
	public static Resource CREATE_FML_DIAGRAM_PALETTE_ELEMENT_BINDING_FROM_DIAGRAM_PALETTE_DIALOG_FIB = ResourceLocator
			.locateResource("Fib/Dialog/CreateFMLPaletteElementBindingFromDiagramPaletteElementDialog.fib");

}
