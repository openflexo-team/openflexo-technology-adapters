/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.model.action;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.openflexo.diana.ContainerGraphicalRepresentation;
import org.openflexo.diana.ScreenshotBuilder.ScreenshotImage;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoGUIAction;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.swing.ImageUtils;
import org.openflexo.swing.ImageUtils.ImageType;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

/**
 * @author vincent leilde
 * 
 */
public class ExportDiagramToImageAction extends FlexoGUIAction<ExportDiagramToImageAction, DiagramElement<?>, DiagramElement<?>> {

	private static final Logger logger = Logger.getLogger(ExportDiagramToImageAction.class.getPackage().getName());

	public static final FlexoActionFactory<ExportDiagramToImageAction, DiagramElement<?>, DiagramElement<?>> actionType = new FlexoActionFactory<ExportDiagramToImageAction, DiagramElement<?>, DiagramElement<?>>(
			"export_diagram_to_image", FlexoActionFactory.docGroup) {

		@Override
		public boolean isEnabledForSelection(DiagramElement<?> object, Vector<DiagramElement<?>> globalSelection) {
			return true;
		}

		@Override
		public boolean isVisibleForSelection(DiagramElement<?> object, Vector<DiagramElement<?>> globalSelection) {
			return true;
		}

		@Override
		public ExportDiagramToImageAction makeNewAction(DiagramElement<?> focusedObject, Vector<DiagramElement<?>> globalSelection,
				FlexoEditor editor) {
			return new ExportDiagramToImageAction(focusedObject, globalSelection, editor);
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(ExportDiagramToImageAction.actionType, DiagramElement.class);
	}

	/**
	 * @param actionType
	 * @param focusedObject
	 * @param globalSelection
	 * @param editor
	 */
	protected ExportDiagramToImageAction(DiagramElement<?> focusedObject, Vector<DiagramElement<?>> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getServiceManager() != null) {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class).getLocales();
		}
		return super.getLocales();
	}

	private ScreenshotImage<? extends DiagramElement<? extends ContainerGraphicalRepresentation>> screenshot;

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
				/*if (!chooser.getSelectedFile().getName().toLowerCase().endsWith("."+type.getExtension())) {
					dest = new File(chooser.getSelectedFile().getAbsolutePath() + "."+type.getExtension());
					imageType = type;
				}*/
			}

			/*if(imageType!=null){
				chooser.getFileFilter()
			}
			if(imageType==null){
				dest = chooser.getSelectedFile();
			}	*/
		}
		if (dest == null) {
			return false;
		}
		if (saveScreenshot() != null) {
			return true;
		}
		return false;
	}

	public ScreenshotImage<? extends DiagramElement<? extends ContainerGraphicalRepresentation>> getScreenshot() {
		if (getFocusedObject() instanceof DiagramShape) {
			if (this.screenshot == null || this.screenshot != ((DiagramShape) getFocusedObject()).getScreenshotImage()) {
				setScreenshot(((DiagramShape) getFocusedObject()).getScreenshotImage());
			}
		}
		if (getFocusedObject() instanceof Diagram) {
			if (this.screenshot == null || this.screenshot != ((Diagram) getFocusedObject()).getScreenshotImage()) {
				setScreenshot(((Diagram) getFocusedObject()).getScreenshotImage());
			}
		}
		return this.screenshot;
	}

	public void setScreenshot(ScreenshotImage<? extends DiagramElement<? extends ContainerGraphicalRepresentation>> screenshot) {
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
