/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Excelconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.docx.fml.editionaction;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.doc.FlexoDocElement;
import org.openflexo.foundation.fml.FlexoBehaviour;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.controlgraph.FMLControlGraph;
import org.openflexo.foundation.fml.editionaction.AbstractCreateResource;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.fml.rt.FMLExecutionException;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXModelSlot;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXElement;
import org.openflexo.technologyadapter.docx.model.DocXTable;
import org.openflexo.technologyadapter.docx.model.DocXTableCell;
import org.openflexo.technologyadapter.docx.model.DocXTableRow;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;

@ModelEntity
@ImplementationClass(GenerateDocXDocument.GenerateDocXDocumentImpl.class)
@XMLElement
@FML("GenerateDocXDocument")
public interface GenerateDocXDocument extends AbstractCreateResource<DocXModelSlot, DocXDocument, DocXTechnologyAdapter> {

	public static abstract class GenerateDocXDocumentImpl
			extends AbstractCreateResourceImpl<DocXModelSlot, DocXDocument, DocXTechnologyAdapter> implements GenerateDocXDocument {

		private static final Logger logger = Logger.getLogger(GenerateDocXDocument.class.getPackage().getName());

		/**
		 * 
		 * @param concept
		 * @param elementsToIgnore
		 */
		private void appendElementsToIgnore(FlexoConcept concept, List<DocXElement> elementsToIgnore) {

			for (FlexoBehaviour behaviour : concept.getFlexoBehaviours()) {
				// TODO handle deep action
				if (behaviour.getControlGraph() != null) {
					for (FMLControlGraph cg : behaviour.getControlGraph().getFlattenedSequence()) {
						if ((cg instanceof AddDocXFragment) && ((AddDocXFragment) cg).getFragment() != null) {
							for (DocXElement e : ((AddDocXFragment) cg).getFragment().getElements()) {
								elementsToIgnore.add(e);
							}
						}
						else if (cg instanceof AssignationAction
								&& ((AssignationAction) cg).getAssignableAction() instanceof AddDocXFragment) {
							if (((AddDocXFragment) ((AssignationAction) cg).getAssignableAction()).getFragment() != null) {
								for (DocXElement e : ((AddDocXFragment) ((AssignationAction) cg).getAssignableAction()).getFragment()
										.getElements()) {
									elementsToIgnore.add(e);
								}
							}
						}
					}
				}

			}

			if (concept instanceof VirtualModel) {
				for (FlexoConcept child : ((VirtualModel) concept).getFlexoConcepts()) {
					appendElementsToIgnore(child, elementsToIgnore);
				}
			}
		}

		@Override
		public Type getAssignableType() {
			return DocXDocument.class;
		}

		/**
		 * Main action
		 */
		@Override
		public DocXDocument execute(RunTimeEvaluationContext evaluationContext) throws FMLExecutionException {

			List<DocXElement> elementsToIgnore = new ArrayList<>();
			appendElementsToIgnore(evaluationContext.getFlexoConceptInstance().getFlexoConcept().getOwner(), elementsToIgnore);
			appendElementsToIgnore(evaluationContext.getFlexoConceptInstance().getFlexoConcept(), elementsToIgnore);

			// System.out.println("ToIgnore: " + elementsToIgnore);

			DocXDocument generatedDocument = null;

			try {

				DocXDocumentResource templateResource = getAssignedModelSlot().getTemplateResource();
				DocXDocument templateDocument = templateResource.getResourceData();

				FlexoResource<DocXDocument> generatedResource = null;

				String resourceName = getResourceName(evaluationContext);
				// Unused String resourceURI = getResourceURI(evaluationContext);
				FlexoResourceCenter<?> rc = getResourceCenter(evaluationContext);

				DocXTechnologyAdapter docxTA = getServiceManager().getTechnologyAdapterService()
						.getTechnologyAdapter(DocXTechnologyAdapter.class);

				generatedResource = docxTA.createNewDocXDocumentResource((FileSystemBasedResourceCenter) rc, getRelativePath(),
						resourceName, true, getAssignedModelSlot().getIdStrategy());

				// System.out.println("-------------> generating document " + generatedResource);

				generatedResource.setResourceData(templateDocument);
				generatedResource.save();
				generatedResource.unloadResourceData(false);
				generatedResource.loadResourceData();

				generatedDocument = generatedResource.getResourceData();

				for (FlexoDocElement<DocXDocument, DocXTechnologyAdapter> generatedElement : generatedDocument.getElements()) {
					String oldId = generatedElement.getIdentifier();
					DocXElement templateElement = (DocXElement) templateDocument.getElementWithIdentifier(oldId);
					generatedElement.setIdentifier(generatedDocument.getFactory().generateId());
					generatedElement.setBaseIdentifier(oldId);
					// System.out.println("Element " + generatedElement + " change id from " + oldId + " to "
					// + generatedElement.getIdentifier());
					if (generatedElement instanceof DocXTable) {
						DocXTable generatedTable = (DocXTable) generatedElement;
						DocXTable templateTable = (DocXTable) templateElement;
						for (int row = 0; row < generatedTable.getTableRows().size(); row++) {
							DocXTableRow r = (DocXTableRow) generatedTable.getTableRows().get(row);
							for (int column = 0; column < r.getTableCells().size(); column++) {
								DocXTableCell generatedCell = (DocXTableCell) generatedTable.getCell(row, column);
								DocXTableCell templateCell = (DocXTableCell) templateTable.getCell(row, column);
								for (int i = 0; i < templateCell.getElements().size(); i++) {
									if (!(column == 0 && row == 0)) {
										// No need to generate new id for first cell, because it has already been done
										// when changing id for the whole table !!!
										// Unused String oldId2 = generatedCell.getElements().get(i).getIdentifier();
										generatedCell.getElements().get(i).setIdentifier(generatedDocument.getFactory().generateId());
										// System.out.println("change id for cell row=" + row + " column=" + column + " from " + oldId2
										// + " to " + generatedCell.getParagraphs().get(i).getIdentifier());
									}
									generatedCell.getElements().get(i).setBaseIdentifier(templateCell.getElements().get(i).getIdentifier());
								}
							}
						}
					}
				}

				/*for (P p : DocXUtils.getAllElementsFromObject(generatedDocument.getWordprocessingMLPackage().getMainDocumentPart(),
						P.class)) {
					String oldId = p.getParaId();
					DocXElement templateElement = (DocXElement) templateDocument.getElementWithIdentifier(oldId);
					DocXElement generatedElement = (DocXElement) generatedDocument.getElementWithIdentifier(oldId);
					generatedElement.setIdentifier(generatedDocument.getFactory().generateId());
					generatedElement.setBaseIdentifier(oldId);
					// p.setParaId(generatedDocument.getFactory().generateId());
					System.out.println("Paragraph " + p + " change id from " + oldId + " to " + generatedElement.getIdentifier());
				}*/

				List<FlexoDocElement<DocXDocument, DocXTechnologyAdapter>> elementsToRemove = new ArrayList<>();
				for (FlexoDocElement<DocXDocument, DocXTechnologyAdapter> templateElement : templateDocument.getElements()) {
					if (elementsToIgnore.contains(templateElement)) {
						// System.out.println("Ignoring: " + templateElement);
						// System.out.println("Ignoring elements: "
						// + generatedDocument.getElementsWithBaseIdentifier(templateElement.getIdentifier()));
						elementsToRemove.addAll(generatedDocument.getElementsWithBaseIdentifier(templateElement.getIdentifier()));
					}
				}
				for (FlexoDocElement<DocXDocument, DocXTechnologyAdapter> elementToRemove : elementsToRemove) {
					generatedDocument.removeFromElements(elementToRemove);
				}

				// Very important: we must now set ModelSlotInstance !
				// TODO: need to check why this is so important...
				/*if (msInstance != null) {
					msInstance.setAccessedResourceData(generatedDocument);
				}*/

			}

			catch (Exception e) {
				e.printStackTrace();
				throw new FMLExecutionException(e);
			}

			return generatedDocument;
		}
	}
}
