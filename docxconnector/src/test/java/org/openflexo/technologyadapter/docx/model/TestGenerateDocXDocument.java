/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.technologyadapter.docx.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.logging.Logger;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.P;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.model.DocXDocument.DocXDocumentImpl;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentRepository;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResourceImpl;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestGenerateDocXDocument extends OpenflexoProjectAtRunTimeTestCase {
	protected static final Logger logger = Logger.getLogger(TestGenerateDocXDocument.class.getPackage().getName());

	private static FlexoEditor editor;
	private static DocXTechnologyAdapter technologicalAdapter;

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager();
	}

	@Test
	@TestOrder(3)
	public void testDocXLoading() {
		technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(DocXTechnologyAdapter.class);

		for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
			DocXDocumentRepository docXRepository = resourceCenter.getRepository(DocXDocumentRepository.class, technologicalAdapter);
			assertNotNull(docXRepository);
			Collection<DocXDocumentResource> documents = docXRepository.getAllResources();
			for (DocXDocumentResource docResource : documents) {
				try {
					docResource.loadResourceData(null);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ResourceLoadingCancelledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FlexoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				assertNotNull(docResource.getLoadedResourceData());
				System.out.println("URI of document: " + docResource.getURI());
			}
		}
	}

	private DocXDocument getDocument(String documentName) {
		String documentURI = resourceCenter.getDefaultBaseURI() + File.separator + documentName;
		System.out.println("Searching " + documentURI);

		FlexoResource<DocXDocument> documentResource = serviceManager.getInformationSpace().getResource(documentURI, null,
				DocXDocument.class);
		assertNotNull(documentResource);

		DocXDocument document = documentResource.getLoadedResourceData();
		assertNotNull(document);
		assertNotNull(document.getWordprocessingMLPackage());

		return document;
	}

	private static DocXDocument templateDocument;
	private static DocXDocument generatedDocument;

	@Test
	@TestOrder(4)
	public void testTemplateDocumentLoading() {

		templateDocument = getDocument("StructuredDocument.docx");

		System.out.println("Template document:\n" + templateDocument.debugStructuredContents());

		assertEquals(13, templateDocument.getElements().size());

		assertEquals(5, templateDocument.getStyles().size());

	}

	@Test
	@TestOrder(5)
	public void testDocumentGenerating() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		DocXDocumentResource templateResource = (DocXDocumentResource) templateDocument.getResource();

		File f = new File(((FileFlexoIODelegate) templateResource.getFlexoIODelegate()).getFile().getParent(), "Generated.docx");

		System.out.println("Generating " + f);
		FlexoResource<DocXDocument> generatedResource = DocXDocumentResourceImpl.makeDocXDocumentResource(f,
				technologicalAdapter.getTechnologyContextManager());

		WordprocessingMLPackage generatedPackage = new WordprocessingMLPackage();

		// MainDocumentPart mdp = XmlUtils.deepCopy(templateDocument.getWordprocessingMLPackage().getMainDocumentPart());
		// generatedPackage.set
		// templateDocument.getWordprocessingMLPackage().getMainDocumentPart()

		generatedResource.setResourceData(templateDocument);
		generatedResource.save(null);
		generatedResource.unloadResourceData();
		generatedResource.loadResourceData(null);

		generatedDocument = generatedResource.getResourceData(null);

		for (P p : DocXDocumentImpl.getAllElementsFromObject(generatedDocument.getWordprocessingMLPackage().getMainDocumentPart(),
				P.class)) {
			String oldId = p.getParaId();
			p.setParaId(generatedDocument.getFactory().generateId());
			System.out.println("Paragraph " + p + " change id from " + oldId + " to " + p.getParaId());
		}

		/*generatedResource.save(null);
		generatedResource.unloadResourceData();
		generatedResource.loadResourceData(null);
		generatedDocument = generatedResource.getResourceData(null);*/

		assertFalse(generatedDocument == templateDocument);

		assertEquals(13, generatedDocument.getElements().size());

		assertEquals(5, generatedDocument.getStyles().size());

	}

}
