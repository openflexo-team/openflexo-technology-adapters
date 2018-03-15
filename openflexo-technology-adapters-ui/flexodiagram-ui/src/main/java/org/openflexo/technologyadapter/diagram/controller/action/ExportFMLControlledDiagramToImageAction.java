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

package org.openflexo.technologyadapter.diagram.controller.action;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.openflexo.fge.ScreenshotBuilder.ScreenshotImage;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoGUIAction;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
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
public class ExportFMLControlledDiagramToImageAction
		extends FlexoGUIAction<ExportFMLControlledDiagramToImageAction, FlexoConceptInstance, FlexoConceptInstance> {

	private static final Logger logger = Logger.getLogger(ExportFMLControlledDiagramToImageAction.class.getPackage().getName());

	public static final FlexoActionFactory<ExportFMLControlledDiagramToImageAction, FlexoConceptInstance, FlexoConceptInstance> actionType = new FlexoActionFactory<ExportFMLControlledDiagramToImageAction, FlexoConceptInstance, FlexoConceptInstance>(
			"export_diagram_to_image", FlexoActionFactory.docGroup) {

		@Override
		public boolean isEnabledForSelection(FlexoConceptInstance object, Vector<FlexoConceptInstance> globalSelection) {
			return true;
		}

		@Override
		public boolean isVisibleForSelection(FlexoConceptInstance object, Vector<FlexoConceptInstance> globalSelection) {
			if (object instanceof FMLRTVirtualModelInstance) {
				return ((FMLRTVirtualModelInstance) object).hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE);
			}
			if (object != null && object.getVirtualModelInstance() instanceof FMLRTVirtualModelInstance) {
				FMLRTVirtualModelInstance vmi = (FMLRTVirtualModelInstance) object.getVirtualModelInstance();
				if (vmi != null)
					return vmi.hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE);
			}
			return false;
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

	private ScreenshotImage<DiagramElement<?>> screenshot;

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
		chooser.setDialogTitle(getLocales().localizedForKey("save_as_image", chooser));

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
		return false;
	}

	private DiagramTechnologyAdapter getDiagramTechnologyAdapter() {
		return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);
	}

	public ScreenshotImage<DiagramElement<?>> getScreenshot() {
		ScreenshotImage<DiagramElement<?>> screenshotImage = null;
		FMLControlledDiagramEditor editor = null;
		FMLControlledDiagramScreenshotBuilder builder = (FMLControlledDiagramScreenshotBuilder) getDiagramTechnologyAdapter()
				.getFMLControlledDiagramElementScreenshotBuilder();
		FlexoConceptInstance focusedObject = getFocusedObject();
		if (focusedObject instanceof FMLRTVirtualModelInstance) {
			builder.setDrawing(new FMLControlledDiagramEditor((FMLRTVirtualModelInstance) focusedObject, true, null, null));
			screenshotImage = builder
					.getImage(FMLControlledDiagramVirtualModelInstanceNature.getDiagram((FMLRTVirtualModelInstance) focusedObject));
		}
		else if (focusedObject != null) {
			editor = new FMLControlledDiagramEditor((FMLRTVirtualModelInstance) focusedObject.getVirtualModelInstance(), true, null, null);
			FMLControlledDiagramElement<?, ?> element = editor.getDrawing().getFMLControlledDiagramElements(focusedObject).get(0);
			builder.setDrawing(editor);
			screenshotImage = builder.getImage(element.getDiagramElement());
		}
		else {
			logger.warning("Could not create a screenshot");
			return null;
		}

		if (this.screenshot == null || this.screenshot != screenshotImage) {
			setScreenshot(screenshotImage);
		}
		return this.screenshot;
	}

	public void setScreenshot(ScreenshotImage<DiagramElement<?>> screenshot) {
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
