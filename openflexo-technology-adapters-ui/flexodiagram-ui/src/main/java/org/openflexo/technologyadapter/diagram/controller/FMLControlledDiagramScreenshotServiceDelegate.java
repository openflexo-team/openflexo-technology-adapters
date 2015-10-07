package org.openflexo.technologyadapter.diagram.controller;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.nature.ScreenshotService.ScreenshotServiceDelegate;
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

	@Override
	public Class<FMLControlledDiagramVirtualModelInstanceNature> getNatureClass() {
		return FMLControlledDiagramVirtualModelInstanceNature.class;
	}

	@Override
	public BufferedImage generateScreenshot(VirtualModelInstance virtualModelInstance) {

		System.out.println("La faut vraiment faire, maintenant");

		FMLControlledDiagramEditor editor = new FMLControlledDiagramEditor(virtualModelInstance, true, null, null);

		JFrame frame = new JFrame("view");
		frame.getContentPane().add(editor.getDrawingView());
		frame.validate();
		frame.pack();
		// frame.setVisible(true);

		BufferedImage returned = editor.getDrawingView().getScreenshot();

		JFrame frame2 = new JFrame("screenshot");
		JLabel imageLabel = new JLabel(new ImageIcon(returned));
		frame2.getContentPane().add(imageLabel);
		frame2.validate();
		frame2.pack();
		frame2.setVisible(true);

		return returned;
	}
}
