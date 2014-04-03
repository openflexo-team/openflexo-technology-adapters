/*
 * (c) Copyright 2013-2014 Openflexo
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

import javax.swing.JComponent;

import org.openflexo.foundation.resource.ScreenshotBuilder;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FreeDiagramEditor;
import org.openflexo.technologyadapter.diagram.model.Diagram;

public class DiagramScreenshotBuilder extends ScreenshotBuilder<Diagram> {
	@Override
	public String getScreenshotName(Diagram o) {
		return o.getName();
	}

	@Override
	public JComponent getScreenshotComponent(Diagram diagram) {

		FreeDiagramEditor editor = new FreeDiagramEditor(diagram, true);
		return editor.getDrawingView();

		/*ExternalVPMModule vpmModule = null;
		try {
			IModuleLoader moduleLoader = getViewPointLibrary().getServiceManager().getService(IModuleLoader.class);
			if (moduleLoader != null) {
				vpmModule = moduleLoader.getVPMModuleInstance();
			}
		} catch (ModuleLoadingException e) {
			logger.warning("cannot load VPM module (and so can't create screenshoot." + e.getMessage());
			e.printStackTrace();
		}

		if (vpmModule == null) {
			return null;
		}

		logger.info("Building " + getExpectedScreenshotImageFile().getAbsolutePath());

		JComponent c = vpmModule.createScreenshotForExampleDiagram(this);
		c.setOpaque(true);
		c.setBackground(Color.WHITE);
		JFrame frame = new JFrame();
		frame.setBackground(Color.WHITE);
		frame.setUndecorated(true);
		frame.getContentPane().add(c);
		frame.pack();
		c.validate();*/
		// return null;
	}
}
