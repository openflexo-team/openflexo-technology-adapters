/**
 * 
 * Copyright (c) 2015,2016 Openflexo
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

package org.openflexo.technologyadapter.pdf.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTabbedPane;

import org.openflexo.technologyadapter.pdf.model.PDFDocument;
import org.openflexo.technologyadapter.pdf.model.PDFDocumentPage;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class PDFDocumentModuleView extends JTabbedPane implements ModuleView<PDFDocument> {

	private PDFDocument pdfDocument;
	private final FlexoPerspective perspective;
	private List<PDFPagePanel> pdfPagePanels;

	private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger
			.getLogger(PDFDocumentModuleView.class.getPackage().getName());

	public PDFDocumentModuleView(PDFDocument pdfDocument, FlexoPerspective perspective) {
		this.pdfDocument = pdfDocument;
		this.perspective = perspective;
		pdfPagePanels = new ArrayList<>();
		for (PDFDocumentPage page : pdfDocument.getPages()) {
			PDFPagePanel pagePanel = new PDFPagePanel(page);
			pdfPagePanels.add(pagePanel);
			add("Page " + page.getPageNumber(), pagePanel);
		}
	}

	public List<PDFPagePanel> getPdfPagePanels() {
		return pdfPagePanels;
	}

	public void clearSelection() {
		if (getSelectedComponent() instanceof PDFPagePanel) {
			((PDFPagePanel) getSelectedComponent()).getController().clearSelection();
		}
	}

	@Override
	public void finalize() throws Throwable {
		System.out.println("************************************** PDFDocumentModuleView has been cleaned / GC.. ********************");
		logger.warning("PDFDocumentModuleView has been garbage collected");
		super.finalize();
	}

	@Override
	public PDFDocument getRepresentedObject() {
		return pdfDocument;
	}

	@Override
	public void deleteModuleView() {
		for (PDFPagePanel p : new ArrayList<>(pdfPagePanels)) {
			p.delete();
			pdfPagePanels.remove(p);
		}
		pdfPagePanels.clear();
		pdfPagePanels = null;
		pdfDocument = null;
		this.removeAll();
	}

	@Override
	public FlexoPerspective getPerspective() {
		return perspective;
	}

	@Override
	public void willShow() {
	}

	@Override
	public void willHide() {
	}

	@Override
	public void show(FlexoController controller, FlexoPerspective perspective) {
	}

	@Override
	public boolean isAutoscrolled() {
		return false;
	}

}
