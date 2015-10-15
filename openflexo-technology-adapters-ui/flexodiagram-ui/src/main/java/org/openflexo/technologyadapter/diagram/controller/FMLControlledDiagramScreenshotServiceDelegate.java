package org.openflexo.technologyadapter.diagram.controller;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import javax.swing.JFrame;

import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.nature.ScreenshotService.ScreenshotServiceDelegate;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;

/**
 * Handle screenshot for a {@link VirtualModelInstance} having {@link FMLControlledDiagramVirtualModelInstanceNature}
 * 
 * @author sylvain
 *
 */
public class FMLControlledDiagramScreenshotServiceDelegate
		implements ScreenshotServiceDelegate<VirtualModelInstance, FMLControlledDiagramVirtualModelInstanceNature> {

	private static final Logger logger = FlexoLogger.getLogger(FMLControlledDiagramScreenshotServiceDelegate.class.getPackage().getName());

	@Override
	public Class<FMLControlledDiagramVirtualModelInstanceNature> getNatureClass() {
		return FMLControlledDiagramVirtualModelInstanceNature.class;
	}

	@Override
	public BufferedImage generateScreenshot(VirtualModelInstance virtualModelInstance) {

		logger.info("Generate screenshot for " + virtualModelInstance + " and " + getNatureClass());

		FMLControlledDiagramEditor editor = new FMLControlledDiagramEditor(virtualModelInstance, true, null, null);

		JFrame frame = new JFrame("screenshot");
		frame.getContentPane().add(editor.getDrawingView());
		frame.validate();
		frame.pack();
		// frame.setVisible(true);

		BufferedImage returned = editor.getDrawingView().getScreenshot();

		/*JFrame frame2 = new JFrame("screenshot");
		JLabel imageLabel = new JLabel(new ImageIcon(returned));
		frame2.getContentPane().add(imageLabel);
		frame2.validate();
		frame2.pack();
		frame2.setVisible(true);*/

		frame.dispose();

		return returned;
	}
}
