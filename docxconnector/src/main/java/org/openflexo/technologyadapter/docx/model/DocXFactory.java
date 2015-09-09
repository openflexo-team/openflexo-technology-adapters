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

import java.io.File;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Style;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;
import org.openflexo.foundation.doc.DocumentFactory;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.EditingContext;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.toolbox.StringUtils;

/**
 * DocX factory for managing {@link DocXDocument}<br>
 * One instance of this class should be used for each {@link DocXDocumentResource}
 * 
 * @author sylvain
 * 
 */
public class DocXFactory extends DocumentFactory<DocXDocument, DocXTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(DocXFactory.class.getPackage().getName());

	public DocXFactory(DocXDocumentResource resource, EditingContext editingContext) throws ModelDefinitionException {
		super(ModelContextLibrary.getCompoundModelContext(DocXDocument.class, DocXParagraph.class, DocXTable.class, DocXFragment.class,
				DocXStyle.class), resource, editingContext);
	}

	@Override
	public DocXDocumentResource getResource() {
		return (DocXDocumentResource) super.getResource();
	}

	@Override
	protected DocXDocument makeDocument() {
		return newInstance(DocXDocument.class);
	}

	public DocXDocument makeNewDocXDocument() {
		DocXDocument returned = makeDocument();
		WordprocessingMLPackage wordMLPackage;
		try {
			wordMLPackage = WordprocessingMLPackage.createPackage();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			return returned;
		}
		// wordMLPackage.getMainDocumentPart().addParagraphOfText("Hello Word!");

		// wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Title", "Hello Word!");
		// wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Subtitle", "This is a subtitle!");

		returned.updateFromWordprocessingMLPackage(wordMLPackage, this);

		return returned;
	}

	public DocXDocument makeNewDocXDocument(WordprocessingMLPackage wpmlPackage) {
		DocXDocument returned = makeDocument();
		returned.updateFromWordprocessingMLPackage(wpmlPackage, this);
		return returned;
	}

	@Override
	protected DocXParagraph makeParagraph() {
		return newInstance(DocXParagraph.class);
	}

	public DocXParagraph makeNewDocXParagraph(P p) {
		DocXParagraph returned = makeParagraph();
		returned.updateFromP(p, this);
		if (StringUtils.isEmpty(returned.getIdentifier())) {
			p.setParaId(generateId());
		}
		return returned;
	}

	public DocXParagraph makeNewDocXParagraph(String text) {
		DocXParagraph returned = makeNewDocXParagraph(Context.getWmlObjectFactory().createP());
		DocXTextRun run = makeTextRun(text);
		returned.addToRuns(run);
		return returned;
	}

	@Override
	public DocXTextRun makeTextRun() {
		return newInstance(DocXTextRun.class);
	}

	public DocXRun makeNewDocXRun(R r) {

		for (Object o : r.getContent()) {
			if (o instanceof JAXBElement) {
				o = ((JAXBElement) o).getValue();
			}
			if (o instanceof Text) {
				return makeNewDocXTextRun(r);
			}
			if (o instanceof Drawing) {
				return makeNewDocXDrawingRun(r);
			}
		}

		return null;
	}

	public DocXTextRun makeNewDocXTextRun(R r) {

		DocXTextRun returned = makeTextRun();
		returned.updateFromR(r, this);
		return returned;
	}

	@Override
	public DocXTextRun makeTextRun(String text) {
		DocXTextRun returned = makeNewDocXTextRun(Context.getWmlObjectFactory().createR());
		returned.setText(text);
		return returned;
	}

	@Override
	public DocXDrawingRun makeDrawingRun() {
		return newInstance(DocXDrawingRun.class);
	}

	public DocXDrawingRun makeNewDocXDrawingRun(R r) {

		DocXDrawingRun returned = makeDrawingRun();
		returned.updateFromR(r, this);
		return returned;
	}

	@Override
	public DocXDrawingRun makeDrawingRun(File imageFile) {
		// TODO

		byte[] imageData;
		try {
			imageData = convertImageToByteArray(imageFile);

			R run = Context.getWmlObjectFactory().createR();
			Drawing drawing = Context.getWmlObjectFactory().createDrawing();
			run.getContent().add(drawing);

			drawing.getAnchorOrInline().add(makeImageInline(imageData));

			return makeNewDocXDrawingRun(run);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public DocXTable makeTable() {
		return newInstance(DocXTable.class);
	}

	public DocXTable makeNewDocXTable(Tbl tbl) {
		DocXTable returned = makeTable();
		returned.updateFromTbl(tbl, this);
		return returned;
	}

	@Override
	public DocXTableRow makeTableRow() {
		return newInstance(DocXTableRow.class);
	}

	public DocXTableRow makeNewDocXTableRow(Tr tr) {
		DocXTableRow returned = makeTableRow();
		returned.updateFromTr(tr, this);
		return returned;
	}

	@Override
	public DocXTableCell makeTableCell() {
		return newInstance(DocXTableCell.class);
	}

	public DocXTableCell makeNewDocXTableCell(Tc tc) {
		DocXTableCell returned = makeTableCell();
		returned.updateFromTc(tc, this);
		return returned;
	}

	private final java.util.Random RANDOM = new java.util.Random();

	public String generateId() {
		return java.math.BigInteger.valueOf(Math.abs(RANDOM.nextInt())).toString(16).toUpperCase();
	}

	@Override
	protected DocXStyle makeStyle() {
		return newInstance(DocXStyle.class);
	}

	public DocXStyle makeNewDocXStyle(Style style, DocXStyle parent) {
		DocXStyle returned = makeStyle();
		returned.updateFromStyle(style, this);
		if (parent != null) {
			returned.setParentStyle(parent);
		}
		return returned;
	}

	@Override
	protected DocXFragment makeFragment(DocXDocument document) {
		DocXFragment returned = newInstance(DocXFragment.class);
		returned.setFlexoDocument(document);
		return returned;
	}

	/**
	 * Docx4j contains a utility method to create an image part from an array of bytes and then adds it to the given package. In order to be
	 * able to add this image to a paragraph, we have to convert it into an inline object. For this there is also a method, which takes a
	 * filename hint, an alt-text, two ids and an indication on whether it should be embedded or linked to. One id is for the drawing object
	 * non-visual properties of the document, and the second id is for the non visual drawing properties of the picture itself. Finally we
	 * add this inline object to the paragraph and the paragraph to the main document of the package.
	 *
	 * @param bytes
	 *            The bytes of the image
	 * @throws Exception
	 *             Sadly the createImageInline method throws an Exception (and not a more specific exception type)
	 */
	public Inline makeImageInline(byte[] bytes) throws Exception {
		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage
				.createImagePart(getResource().getDocument().getWordprocessingMLPackage(), bytes);

		int docPrId = 1;
		int cNvPrId = 2;
		return imagePart.createImageInline("Filename hint", "Alternative text", docPrId, cNvPrId, false);
	}

}
