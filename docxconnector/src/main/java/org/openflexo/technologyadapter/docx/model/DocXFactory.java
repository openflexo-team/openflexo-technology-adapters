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

package org.openflexo.technologyadapter.docx.model;

import java.util.logging.Logger;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.P;
import org.docx4j.wml.Style;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.PamelaResourceModelFactory;
import org.openflexo.foundation.action.FlexoUndoManager;
import org.openflexo.foundation.resource.PamelaResourceImpl.IgnoreLoadingEdits;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.converter.RelativePathResourceConverter;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.EditingContext;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;

/**
 * DocX factory for managing {@link DocXDocument}<br>
 * One instance of this class should be used for each {@link DocXDocumentResource}
 * 
 * @author sylvain
 * 
 */
public class DocXFactory extends ModelFactory implements PamelaResourceModelFactory<DocXDocumentResource> {

	private static final Logger logger = Logger.getLogger(DocXFactory.class.getPackage().getName());

	private final DocXDocumentResource resource;
	private IgnoreLoadingEdits ignoreHandler = null;
	private FlexoUndoManager undoManager = null;

	public DocXFactory(DocXDocumentResource resource, EditingContext editingContext) throws ModelDefinitionException {
		super(ModelContextLibrary.getCompoundModelContext(DocXDocument.class, DocXParagraph.class, DocXFragment.class, DocXStyle.class));
		this.resource = resource;
		setEditingContext(editingContext);
		if (resource != null) {
			addConverter(new RelativePathResourceConverter(resource.getFlexoIODelegate().getParentPath()));
		}
	}

	@Override
	public DocXDocumentResource getResource() {
		return resource;
	}

	public DocXDocument makeNewDocXDocument(WordprocessingMLPackage wpmlPackage) {
		DocXDocument returned = newInstance(DocXDocument.class);
		returned.updateFromWordprocessingMLPackage(wpmlPackage, this);
		return returned;
	}

	public DocXParagraph makeNewDocXParagraph(P p) {
		DocXParagraph returned = newInstance(DocXParagraph.class);
		returned.updateFromP(p, this);
		return returned;
	}

	public DocXStyle makeNewDocXStyle(Style style, DocXStyle parent) {
		DocXStyle returned = newInstance(DocXStyle.class);
		returned.updateFromStyle(style, this);
		if (parent != null) {
			returned.setParentStyle(parent);
		}
		return returned;
	}

	public DocXFragment makeNewDocXFragment(DocXParagraph startParagraph, DocXParagraph endParagraph) {
		DocXFragment returned = newInstance(DocXFragment.class);
		returned.setStartElement(startParagraph);
		returned.setEndElement(endParagraph);
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

	@Override
	public <I> void objectHasBeenDeserialized(I newlyCreatedObject, Class<I> implementedInterface) {
		super.objectHasBeenDeserialized(newlyCreatedObject, implementedInterface);
		if (newlyCreatedObject instanceof FlexoObject) {
			if (getResource() != null) {
				getResource().setLastID(((FlexoObject) newlyCreatedObject).getFlexoID());
			} else {
				logger.warning("Could not access resource beeing deserialized");
			}
		}
	}

}
