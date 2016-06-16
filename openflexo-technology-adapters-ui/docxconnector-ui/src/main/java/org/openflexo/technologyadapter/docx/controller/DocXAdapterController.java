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

package org.openflexo.technologyadapter.docx.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.doc.fml.ColumnTableBinding;
import org.openflexo.foundation.doc.fml.TextBinding;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.gina.utils.InspectorGroup;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.fml.DocXFragmentRole;
import org.openflexo.technologyadapter.docx.fml.DocXImageRole;
import org.openflexo.technologyadapter.docx.fml.DocXParagraphRole;
import org.openflexo.technologyadapter.docx.fml.DocXTableRole;
import org.openflexo.technologyadapter.docx.fml.editionaction.AddDocXFragment;
import org.openflexo.technologyadapter.docx.fml.editionaction.AddDocXParagraph;
import org.openflexo.technologyadapter.docx.fml.editionaction.ApplyTextBindings;
import org.openflexo.technologyadapter.docx.fml.editionaction.GenerateDocXDocument;
import org.openflexo.technologyadapter.docx.fml.editionaction.GenerateDocXImage;
import org.openflexo.technologyadapter.docx.fml.editionaction.GenerateDocXTable;
import org.openflexo.technologyadapter.docx.fml.editionaction.ReinjectFromDocXTable;
import org.openflexo.technologyadapter.docx.fml.editionaction.ReinjectTextBindings;
import org.openflexo.technologyadapter.docx.fml.editionaction.SelectGeneratedDocXFragment;
import org.openflexo.technologyadapter.docx.fml.editionaction.SelectGeneratedDocXImage;
import org.openflexo.technologyadapter.docx.fml.editionaction.SelectGeneratedDocXTable;
import org.openflexo.technologyadapter.docx.gui.DocXIconLibrary;
import org.openflexo.technologyadapter.docx.gui.view.DocXDocumentModuleView;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXDrawingRun;
import org.openflexo.technologyadapter.docx.model.DocXFragment;
import org.openflexo.technologyadapter.docx.model.DocXParagraph;
import org.openflexo.technologyadapter.docx.model.DocXTable;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class DocXAdapterController extends TechnologyAdapterController<DocXTechnologyAdapter> {
	static final Logger logger = Logger.getLogger(DocXAdapterController.class.getPackage().getName());

	@Override
	public Class<DocXTechnologyAdapter> getTechnologyAdapterClass() {
		return DocXTechnologyAdapter.class;
	}

	@Override
	public void initializeActions(ControllerActionInitializer actionInitializer) {
	}

	@Override
	public ImageIcon getTechnologyBigIcon() {
		return DocXIconLibrary.DOCX_TECHNOLOGY_BIG_ICON;
	}

	@Override
	public ImageIcon getTechnologyIcon() {
		return DocXIconLibrary.DOCX_TECHNOLOGY_ICON;
	}

	@Override
	public ImageIcon getModelIcon() {
		return DocXIconLibrary.DOCX_FILE_ICON;
	}

	@Override
	public ImageIcon getMetaModelIcon() {
		return DocXIconLibrary.DOCX_FILE_ICON;
	}

	@Override
	public ImageIcon getIconForTechnologyObject(TechnologyObject<?> object) {
		if (object instanceof DocXParagraph) {
			DocXParagraph paragraph = (DocXParagraph) object;
			if (paragraph.getStyle() != null && paragraph.getStyle().isLevelled()) {
				return DocXIconLibrary.SECTION_ICON;
			}
			else {
				return DocXIconLibrary.PARAGRAPH_ICON;
			}
		}
		else if (object instanceof DocXTable) {
			return DocXIconLibrary.TABLE_ICON;
		}
		else if (object instanceof DocXDrawingRun) {
			return DocXIconLibrary.IMAGE_ICON;
		}
		else if (object instanceof DocXFragment) {
			return DocXIconLibrary.FRAGMENT_ICON;
		}
		else if (object instanceof TextBinding) {
			return DocXIconLibrary.TEXT_BINDING_ICON;
		}
		else if (object instanceof ColumnTableBinding) {
			return DocXIconLibrary.COLUMN_BINDING_ICON;
		}
		return super.getIconForTechnologyObject(object);
	}

	@Override
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject<?>> objectClass) {
		if (DocXDocument.class.isAssignableFrom(objectClass)) {
			return DocXIconLibrary.DOCX_FILE_ICON;
		}
		if (DocXParagraph.class.isAssignableFrom(objectClass)) {
			return DocXIconLibrary.SECTION_ICON;
		}
		if (DocXFragment.class.isAssignableFrom(objectClass)) {
			return DocXIconLibrary.FRAGMENT_ICON;
		}
		if (TextBinding.class.isAssignableFrom(objectClass)) {
			return DocXIconLibrary.TEXT_BINDING_ICON;
		}
		if (ColumnTableBinding.class.isAssignableFrom(objectClass)) {
			return DocXIconLibrary.COLUMN_BINDING_ICON;
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
		if (GenerateDocXDocument.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DocXIconLibrary.DOCX_FILE_ICON, IconLibrary.DUPLICATE);
		}
		else if (AddDocXFragment.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DocXIconLibrary.FRAGMENT_ICON, IconLibrary.DUPLICATE);
		}
		else if (AddDocXParagraph.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DocXIconLibrary.PARAGRAPH_ICON, IconLibrary.DUPLICATE);
		}
		else if (ApplyTextBindings.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DocXIconLibrary.FRAGMENT_ICON, IconLibrary.GENERATE);
		}
		else if (ReinjectTextBindings.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DocXIconLibrary.FRAGMENT_ICON, IconLibrary.REINJECT);
		}
		else if (SelectGeneratedDocXFragment.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DocXIconLibrary.FRAGMENT_ICON, IconLibrary.IMPORT);
		}
		else if (GenerateDocXTable.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DocXIconLibrary.TABLE_ICON, IconLibrary.GENERATE);
		}
		else if (ReinjectFromDocXTable.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DocXIconLibrary.TABLE_ICON, IconLibrary.REINJECT);
		}
		else if (SelectGeneratedDocXTable.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DocXIconLibrary.TABLE_ICON, IconLibrary.IMPORT);
		}
		else if (GenerateDocXImage.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DocXIconLibrary.IMAGE_ICON, IconLibrary.GENERATE);
		}
		else if (SelectGeneratedDocXImage.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DocXIconLibrary.IMAGE_ICON, IconLibrary.IMPORT);
		}
		return super.getIconForEditionAction(editionActionClass);

	}

	@Override
	public ModuleView<?> createModuleViewForObject(TechnologyObject<DocXTechnologyAdapter> object, FlexoController controller,
			FlexoPerspective perspective) {
		if (object instanceof DocXDocument) {
			return new DocXDocumentModuleView((DocXDocument) object, perspective);
		}
		return null;
	}

	@Override
	public ImageIcon getIconForFlexoRole(Class<? extends FlexoRole<?>> roleClass) {
		if (DocXParagraphRole.class.isAssignableFrom(roleClass)) {
			return DocXIconLibrary.PARAGRAPH_ICON;
		}
		if (DocXTableRole.class.isAssignableFrom(roleClass)) {
			return DocXIconLibrary.TABLE_ICON;
		}
		if (DocXFragmentRole.class.isAssignableFrom(roleClass)) {
			return DocXIconLibrary.FRAGMENT_ICON;
		}
		if (DocXImageRole.class.isAssignableFrom(roleClass)) {
			return DocXIconLibrary.IMAGE_ICON;
		}
		return null;
	}

	@Override
	public String getWindowTitleforObject(TechnologyObject<DocXTechnologyAdapter> object, FlexoController controller) {
		if (object instanceof DocXDocument) {
			return ((DocXDocument) object).getName();
		}
		return null;
	}

	@Override
	public boolean hasModuleViewForObject(TechnologyObject<DocXTechnologyAdapter> object, FlexoController controller) {
		if (object instanceof DocXDocument) {
			return true;
		}
		return false;
	}

	@Override
	protected void initializeInspectors(FlexoController controller) {
		docXInspectorGroup = controller.loadInspectorGroup("DocX", getTechnologyAdapter().getLocales(),
				getFMLTechnologyAdapterInspectorGroup());
	}

	private InspectorGroup docXInspectorGroup;

	@Override
	public InspectorGroup getTechnologyAdapterInspectorGroup() {
		return docXInspectorGroup;
	}
}
