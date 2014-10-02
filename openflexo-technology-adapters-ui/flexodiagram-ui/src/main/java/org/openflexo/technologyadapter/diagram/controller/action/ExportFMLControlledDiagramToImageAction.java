/*
 * (c) Copyright 2013 Openflexo
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
package org.openflexo.technologyadapter.diagram.controller.action;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.action.FlexoGUIAction;
import org.openflexo.foundation.resource.ScreenshotBuilder.ScreenshotImage;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.swing.ImageUtils;
import org.openflexo.swing.ImageUtils.ImageType;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.controller.FMLControlledDiagramScreenshotBuilder;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramElement;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;

/**
 * @author vincent leilde
 * 
 */
public class ExportFMLControlledDiagramToImageAction extends
		FlexoGUIAction<ExportFMLControlledDiagramToImageAction, FlexoConceptInstance, FlexoConceptInstance> {

	private static final Logger logger = Logger.getLogger(ExportFMLControlledDiagramToImageAction.class.getPackage().getName());

	public static final FlexoActionType<ExportFMLControlledDiagramToImageAction, FlexoConceptInstance, FlexoConceptInstance> actionType = new FlexoActionType<ExportFMLControlledDiagramToImageAction, FlexoConceptInstance, FlexoConceptInstance>(
			"export_diagram_to_image", FlexoActionType.docGroup) {

		@Override
		public boolean isEnabledForSelection(FlexoConceptInstance object, Vector<FlexoConceptInstance> globalSelection) {
			return true;
		}

		@Override
		public boolean isVisibleForSelection(FlexoConceptInstance object, Vector<FlexoConceptInstance> globalSelection) {
			if (object instanceof VirtualModelInstance) {
				return ((VirtualModelInstance) object).hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE);
			}
			else {
				if (object != null) {
					VirtualModelInstance vmi = object.getVirtualModelInstance();
					if (vmi != null)
						return vmi.hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE);
				}
				return false;
			}
		}

		@Override
		public ExportFMLControlledDiagramToImageAction makeNewAction(FlexoConceptInstance focusedObject,
				Vector<FlexoConceptInstance> globalSelection, FlexoEditor editor) {
			return new ExportFMLControlledDiagramToImageAction(focusedObject, globalSelection, editor);
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(ExportFMLControlledDiagramToImageAction.actionType, FlexoConceptInstance.class);
	}

	/**
	 * @param actionType
	 * @param focusedObject
	 * @param globalSelection
	 * @param editor
	 */
	protected ExportFMLControlledDiagramToImageAction(FlexoConceptInstance focusedObject, Vector<FlexoConceptInstance> globalSelection,
			FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	private ScreenshotImage screenshot;

	private File dest;

	private ImageType imageType;

	public boolean saveAsImage() {
		dest = null;
		JFileChooser chooser = new JFileChooser() {
			@Override
			public void approveSelection() {
				File f = getSelectedFile();
				if (f.exists() && getDialogType() == SAVE_DIALOG) {
					int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file",
							JOptionPane.YES_NO_CANCEL_OPTION);
					switch (result) {
						case JOptionPane.YES_OPTION:
							super.approveSelection();
							return;
						case JOptionPane.NO_OPTION:
							return;
						case JOptionPane.CLOSED_OPTION:
							return;
						case JOptionPane.CANCEL_OPTION:
							cancelSelection();
							return;
					}
				}
				if (!f.exists() && getDialogType() == SAVE_DIALOG) {
					super.approveSelection();
					return;
				}
			}
		};
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setDialogTitle(FlexoLocalization.localizedForKey("save_as_image", chooser));

		for (ImageType type : ImageType.values()) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter(type.name(), type.getExtension());
			chooser.addChoosableFileFilter(filter);
		}

		int returnVal = chooser.showSaveDialog(null);
		if (returnVal == JFileChooser.CANCEL_OPTION) {
			return false;
		}
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			for (ImageType type : ImageType.values()) {
				if (type.getExtension().toUpperCase().equals(chooser.getFileFilter().getDescription().toUpperCase())) {
					dest = new File(chooser.getSelectedFile().getAbsolutePath() + "." + type.getExtension());
					imageType = type;
				}
			}
		}
		if (dest == null) {
			return false;
		}
		if (saveScreenshot() != null) {
			return true;
		}
		else {
			return false;
		}
	}

	private DiagramTechnologyAdapter getDiagramTechnologyAdapter() {
		return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);
	}

	public ScreenshotImage<DiagramElement> getScreenshot() {
		ScreenshotImage screenshotImage = null;
		FMLControlledDiagramEditor editor = null;
		FMLControlledDiagramScreenshotBuilder builder = (FMLControlledDiagramScreenshotBuilder) getDiagramTechnologyAdapter()
				.getFMLControlledDiagramElementScreenshotBuilder();
		if (getFocusedObject() instanceof VirtualModelInstance) {
			builder.setDrawing(new FMLControlledDiagramEditor((VirtualModelInstance) getFocusedObject(), true, null, null));
			screenshotImage = builder.getImage(FMLControlledDiagramVirtualModelInstanceNature
					.getDiagram((VirtualModelInstance) getFocusedObject()));
		}
		else if (getFocusedObject() instanceof FlexoConceptInstance) {
			editor = new FMLControlledDiagramEditor(getFocusedObject().getVirtualModelInstance(), true, null, null);
			FMLControlledDiagramElement element = editor.getDrawing().getFMLControlledDiagramElements(getFocusedObject()).get(0);
			builder.setDrawing(editor);
			screenshotImage = builder.getImage(element.getDiagramElement());
		}
		else {
			logger.warning("Could not create a screenshot for " + getFocusedObject().getStringRepresentation());
			return null;
		}

		if (this.screenshot == null || this.screenshot != screenshotImage) {
			setScreenshot(screenshotImage);
		}

		return this.screenshot;
	}

	public void setScreenshot(ScreenshotImage screenshot) {
		this.screenshot = screenshot;
	}

	public File saveScreenshot() {
		logger.info("Saving " + dest);
		try {
			ImageUtils.saveImageToFile(getScreenshot().image, dest, imageType);
			return dest;
		} catch (IOException e) {
			e.printStackTrace();
			logger.warning("Could not save " + dest.getAbsolutePath());
			return null;
		}
	}

}
