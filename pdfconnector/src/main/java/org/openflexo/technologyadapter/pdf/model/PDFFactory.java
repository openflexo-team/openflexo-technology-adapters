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

package org.openflexo.technologyadapter.pdf.model;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.openflexo.foundation.PamelaResourceModelFactory;
import org.openflexo.foundation.action.FlexoUndoManager;
import org.openflexo.foundation.resource.PamelaResourceImpl.IgnoreLoadingEdits;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.converter.RelativePathResourceConverter;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.EditingContext;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.pdf.rm.PDFDocumentResource;

/**
 * PDF factory for managing {@link PDFDocument}<br>
 * One instance of this class should be used for each {@link PDFDocumentResource}
 * 
 * @author sylvain
 * 
 */
public class PDFFactory extends ModelFactory implements PamelaResourceModelFactory<PDFDocumentResource> {

	private static final Logger logger = Logger.getLogger(PDFFactory.class.getPackage().getName());

	private final PDFDocumentResource resource;
	private IgnoreLoadingEdits ignoreHandler = null;
	private FlexoUndoManager undoManager = null;

	private RelativePathResourceConverter relativePathResourceConverter;

	public PDFFactory(PDFDocumentResource resource, EditingContext editingContext) throws ModelDefinitionException {
		super(ModelContextLibrary.getCompoundModelContext(PDFDocument.class));
		this.resource = resource;
		setEditingContext(editingContext);
		addConverter(relativePathResourceConverter = new RelativePathResourceConverter(null));
		if (resource != null && resource.getIODelegate() != null
				&& resource.getIODelegate().getSerializationArtefactAsResource() != null) {
			relativePathResourceConverter
					.setContainerResource(resource.getIODelegate().getSerializationArtefactAsResource().getContainer());
		}
	}

	@Override
	public PDFDocumentResource getResource() {
		return resource;
	}

	protected PDFDocument makeDocument() {
		return newInstance(PDFDocument.class);
	}

	protected PDFDocumentPage makeDocumentPage() {
		return newInstance(PDFDocumentPage.class);
	}

	public PDFDocument makeNewPDFDocument() {
		PDFDocument returned = makeDocument();
		PDDocument document = null;

		// init document here

		returned.updateFromPDDocument(document, this);

		return returned;
	}

	public PDFDocument makeNewPDFDocument(PDDocument document) {
		PDFDocument returned = makeDocument();
		returned.updateFromPDDocument(document, this);
		// TODO : this might be dangerous! We do this to avoid Performance issue for ArchWater
		try {
			document.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block ==> check if its dangerous
			e.printStackTrace();
		}
		return returned;
	}

	public PDFDocumentPage makeNewPDFPage(PDDocument document, PDPage page) {
		PDFDocumentPage returned = makeDocumentPage();
		returned.updateFromPDPage(document, page, this);
		return returned;
	}

	@Override
	public synchronized void startDeserializing() {
		EditingContext editingContext = getResource().getServiceManager().getEditingContext();

		if (editingContext != null && editingContext.getUndoManager() instanceof FlexoUndoManager) {
			undoManager = (FlexoUndoManager) editingContext.getUndoManager();
			undoManager.addToIgnoreHandlers(ignoreHandler = new IgnoreLoadingEdits(resource));
			// System.out.println("@@@@@@@@@@@@@@@@ START LOADING RESOURCE " + resource.getURI());
		}

	}

	@Override
	public synchronized void stopDeserializing() {
		if (ignoreHandler != null) {
			undoManager.removeFromIgnoreHandlers(ignoreHandler);
			// System.out.println("@@@@@@@@@@@@@@@@ END LOADING RESOURCE " + resource.getURI());
		}

	}

}
