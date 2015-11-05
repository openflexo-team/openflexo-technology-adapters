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

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.fib.model.FIBComponent;
import org.openflexo.fib.model.FIBTab;
import org.openflexo.fib.utils.FIBInspector;
import org.openflexo.fib.view.GinaViewFactory;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramPalette;
import org.openflexo.technologyadapter.diagram.fml.action.CreateExampleDiagram;
import org.openflexo.technologyadapter.diagram.fml.action.DeleteDiagramPalette;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.action.DeleteDiagram;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.FlexoFIBController;

public class DiagramFIBController extends FlexoFIBController {

	protected static final Logger logger = FlexoLogger.getLogger(DiagramFIBController.class.getPackage().getName());

	public DiagramFIBController(FIBComponent component, GinaViewFactory<?> viewFactory) {
		super(component, viewFactory);
	}

	public DiagramFIBController(FIBComponent component, GinaViewFactory<?> viewFactory, FlexoController controller) {
		super(component, viewFactory, controller);
	}

	public Diagram createExampleDiagram(DiagramSpecification diagramSpecification) {
		CreateExampleDiagram createExampleDiagram = CreateExampleDiagram.actionType.makeNewAction(diagramSpecification, null, getEditor());
		createExampleDiagram.doAction();
		return createExampleDiagram.getNewDiagram();
	}

	public void deleteExampleDiagram(Diagram diagram) {
		DeleteDiagram deleteDiagram = DeleteDiagram.actionType.makeNewAction(diagram, null, getEditor());
		deleteDiagram.doAction();
	}

	public DiagramPalette createDiagramPalette(DiagramSpecification diagramSpecification) {
		CreateDiagramPalette createDiagramPalette = CreateDiagramPalette.actionType.makeNewAction(diagramSpecification, null, getEditor());
		createDiagramPalette.doAction();
		return createDiagramPalette.getNewPalette();
	}

	public void deleteDiagramPalette(DiagramPalette diagramPalette) {
		DeleteDiagramPalette deleteDiagramPalette = DeleteDiagramPalette.actionType.makeNewAction(diagramPalette, null, getEditor());
		deleteDiagramPalette.doAction();
	}

	@Override
	public ImageIcon iconForObject(Object object) {
		// TODO Auto-generated method stub
		if (object instanceof FlexoObject) {
			return DiagramIconLibrary.iconForObject((FlexoObject) object);
		}
		else {
			return DiagramIconLibrary.DIAGRAM_ICON;
		}

	}

	public FIBInspector inspectorForObject(Object object) {
		return getFlexoController().getModuleInspectorController().inspectorForObject(object);
	}

	public FIBTab basicInspectorTabForObject(Object object) {
		FIBInspector inspector = inspectorForObject(object);
		if (inspector != null && inspector.getTabPanel() != null) {
			return (FIBTab) inspector.getTabPanel().getSubComponentNamed("BasicTab");
		}
		return null;
	}
}
