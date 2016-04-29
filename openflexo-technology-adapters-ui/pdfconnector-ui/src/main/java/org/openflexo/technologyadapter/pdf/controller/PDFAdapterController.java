/*
 * (c) Copyright 2013- Openflexo
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

package org.openflexo.technologyadapter.pdf.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.gina.utils.InspectorGroup;
import org.openflexo.technologyadapter.pdf.PDFTechnologyAdapter;
import org.openflexo.technologyadapter.pdf.gui.PDFIconLibrary;
import org.openflexo.technologyadapter.pdf.model.PDFDocument;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class PDFAdapterController extends TechnologyAdapterController<PDFTechnologyAdapter> {
	static final Logger logger = Logger.getLogger(PDFAdapterController.class.getPackage().getName());

	@Override
	public Class<PDFTechnologyAdapter> getTechnologyAdapterClass() {
		return PDFTechnologyAdapter.class;
	}

	@Override
	public void initializeActions(ControllerActionInitializer actionInitializer) {
	}

	@Override
	public ImageIcon getTechnologyBigIcon() {
		return PDFIconLibrary.PDF_TECHNOLOGY_BIG_ICON;
	}

	@Override
	public ImageIcon getTechnologyIcon() {
		return PDFIconLibrary.PDF_TECHNOLOGY_ICON;
	}

	@Override
	public ImageIcon getModelIcon() {
		return PDFIconLibrary.PDF_FILE_ICON;
	}

	@Override
	public ImageIcon getMetaModelIcon() {
		return PDFIconLibrary.PDF_FILE_ICON;
	}

	@Override
	public ImageIcon getIconForTechnologyObject(TechnologyObject<?> object) {
		if (object instanceof PDFDocument) {
			return PDFIconLibrary.PDF_FILE_ICON;
		}
		return super.getIconForTechnologyObject(object);
	}

	@Override
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject<?>> objectClass) {
		if (PDFDocument.class.isAssignableFrom(objectClass)) {
			return PDFIconLibrary.PDF_FILE_ICON;
		}
		return null;
	}

	/**
	 * Return icon representing supplied edition action
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForEditionAction(Class<? extends EditionAction> editionActionClass) {
		/*if (GeneratePDFDocument.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(PDFIconLibrary.DOCX_FILE_ICON, IconLibrary.DUPLICATE);
		}
		else if (AddPDFFragment.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(PDFIconLibrary.FRAGMENT_ICON, IconLibrary.DUPLICATE);
		}
		else if (AddPDFParagraph.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(PDFIconLibrary.PARAGRAPH_ICON, IconLibrary.DUPLICATE);
		}
		else if (ApplyTextBindings.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(PDFIconLibrary.FRAGMENT_ICON, IconLibrary.GENERATE);
		}
		else if (ReinjectTextBindings.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(PDFIconLibrary.FRAGMENT_ICON, IconLibrary.REINJECT);
		}
		else if (SelectGeneratedPDFFragment.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(PDFIconLibrary.FRAGMENT_ICON, IconLibrary.IMPORT);
		}
		else if (GeneratePDFTable.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(PDFIconLibrary.TABLE_ICON, IconLibrary.GENERATE);
		}
		else if (ReinjectFromPDFTable.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(PDFIconLibrary.TABLE_ICON, IconLibrary.REINJECT);
		}
		else if (SelectGeneratedPDFTable.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(PDFIconLibrary.TABLE_ICON, IconLibrary.IMPORT);
		}
		else if (GeneratePDFImage.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(PDFIconLibrary.IMAGE_ICON, IconLibrary.GENERATE);
		}
		else if (SelectGeneratedPDFImage.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(PDFIconLibrary.IMAGE_ICON, IconLibrary.IMPORT);
		}*/
		return super.getIconForEditionAction(editionActionClass);

	}

	@Override
	public ModuleView<?> createModuleViewForObject(TechnologyObject<PDFTechnologyAdapter> object, FlexoController controller,
			FlexoPerspective perspective) {
		/*if (object instanceof PDFDocument) {
			return new PDFDocumentModuleView((PDFDocument) object, perspective);
		}*/
		return null;
	}

	@Override
	public ImageIcon getIconForFlexoRole(Class<? extends FlexoRole<?>> roleClass) {
		/*if (PDFParagraphRole.class.isAssignableFrom(roleClass)) {
			return PDFIconLibrary.PARAGRAPH_ICON;
		}
		if (PDFTableRole.class.isAssignableFrom(roleClass)) {
			return PDFIconLibrary.TABLE_ICON;
		}
		if (PDFFragmentRole.class.isAssignableFrom(roleClass)) {
			return PDFIconLibrary.FRAGMENT_ICON;
		}
		if (PDFImageRole.class.isAssignableFrom(roleClass)) {
			return PDFIconLibrary.IMAGE_ICON;
		}*/
		return null;
	}

	@Override
	public String getWindowTitleforObject(TechnologyObject<PDFTechnologyAdapter> object, FlexoController controller) {
		if (object instanceof PDFDocument) {
			return ((PDFDocument) object).getName();
		}
		return null;
	}

	@Override
	public boolean hasModuleViewForObject(TechnologyObject<PDFTechnologyAdapter> object, FlexoController controller) {
		if (object instanceof PDFDocument) {
			return true;
		}
		return false;
	}

	@Override
	protected void initializeInspectors(FlexoController controller) {
		// docXInspectorGroup = controller.loadInspectorGroup("PDF", getFMLTechnologyAdapterInspectorGroup());
	}

	private InspectorGroup docXInspectorGroup;

	@Override
	public InspectorGroup getTechnologyAdapterInspectorGroup() {
		return docXInspectorGroup;
	}
}
