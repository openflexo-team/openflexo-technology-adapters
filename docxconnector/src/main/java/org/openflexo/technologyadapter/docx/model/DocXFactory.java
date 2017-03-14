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

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.text.TabStop;
import javax.xml.bind.JAXBElement;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.CTTabStop;
import org.docx4j.wml.Color;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.P;
import org.docx4j.wml.PPrBase;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPrAbstract;
import org.docx4j.wml.STLineSpacingRule;
import org.docx4j.wml.STTabJc;
import org.docx4j.wml.SdtBlock;
import org.docx4j.wml.Style;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;
import org.docx4j.wml.U;
import org.openflexo.foundation.doc.DocumentFactory;
import org.openflexo.foundation.doc.FlexoParagraphStyle.ParagraphAlignment;
import org.openflexo.foundation.doc.FlexoParagraphStyle.ParagraphIndent;
import org.openflexo.foundation.doc.FlexoParagraphStyle.ParagraphSpacing;
import org.openflexo.foundation.doc.FlexoParagraphStyle.ParagraphSpacing.LineSpacingRule;
import org.openflexo.foundation.doc.FlexoParagraphStyle.ParagraphTab;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.EditingContext;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.model.DocXDocument.DocXDocumentImpl;
import org.openflexo.technologyadapter.docx.model.DocXParagraph.DocXParagraphImpl;
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

	private final IdentifierManagementStrategy idStrategy;

	public static final int INDENTS_MULTIPLIER = 20;

	public DocXFactory(DocXDocumentResource resource, EditingContext editingContext, IdentifierManagementStrategy idStrategy)
			throws ModelDefinitionException {
		super(ModelContextLibrary.getCompoundModelContext(DocXDocument.class, DocXFragment.class, NamedDocXStyle.class), resource,
				editingContext);
		this.idStrategy = idStrategy;
	}

	public IdentifierManagementStrategy getIDStrategy() {
		return idStrategy;
	}

	@Override
	public DocXDocumentResource getResource() {
		return (DocXDocumentResource) super.getResource();
	}

	@Override
	public DocXDocument makeDocument() {
		DocXDocument returned = newInstance(DocXDocument.class);
		((DocXDocumentImpl) returned)._factory = this;
		return returned;
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
		DocXParagraph returned = newInstance(DocXParagraph.class);
		((DocXParagraphImpl) returned)._factory = this;
		return returned;
	}

	public boolean someIdHaveBeenGeneratedAccordingToBookmarkManagementStrategy = false;

	public DocXParagraph makeNewDocXParagraph(P p) {
		DocXParagraph returned = makeParagraph();
		returned.updateFromP(p, this);
		if (StringUtils.isEmpty(returned.getIdentifier())) {
			switch (getIDStrategy()) {
				case ParaId:
					p.setParaId(generateId());
					break;
				case Bookmark:
					((DocXParagraphImpl) returned).createCTBookmarkIdentifier(this);
					break;
			}
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
			if (o instanceof R.Tab) {
				return makeNewDocXTextRun(r);
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

		byte[] imageData;
		try {
			imageData = convertImageToByteArray(imageFile);

			R run = Context.getWmlObjectFactory().createR();
			Drawing drawing = Context.getWmlObjectFactory().createDrawing();
			run.getContent().add(drawing);

			drawing.getAnchorOrInline().add(makeImageInline(imageData));

			return makeNewDocXDrawingRun(run);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public DocXDrawingRun makeDrawingRun(BufferedImage image) {

		byte[] imageData;
		try {
			imageData = convertImageToByteArray(image);

			R run = Context.getWmlObjectFactory().createR();
			Drawing drawing = Context.getWmlObjectFactory().createDrawing();
			run.getContent().add(drawing);

			drawing.getAnchorOrInline().add(makeImageInline(imageData));

			return makeNewDocXDrawingRun(run);

		} catch (Exception e) {
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

	/**
	 * Build new empty DocXSdtBlock
	 * 
	 * @return
	 */
	@Override
	public DocXSdtBlock makeSdtBlock() {
		return newInstance(DocXSdtBlock.class);
	}

	public DocXSdtBlock makeNewSdtBlock(SdtBlock sdtBlock) {
		DocXSdtBlock returned = makeSdtBlock();
		returned.updateFromSdtBlock(sdtBlock, this);
		return returned;
	}

	/**
	 * Build new empty DocXUnmappedElement
	 * 
	 * @return
	 */
	@Override
	public DocXUnmappedElement<?> makeUnmappedElement() {
		return newInstance(DocXUnmappedElement.class);
	}

	public <T> DocXUnmappedElement<T> makeNewUnmappedElement(T docXObject) {
		DocXUnmappedElement<T> returned = (DocXUnmappedElement<T>) makeUnmappedElement();
		returned.updateFromDocXObject(docXObject, this);
		return returned;
	}

	private final java.util.Random RANDOM = new java.util.Random();

	@Override
	public String generateId() {
		return java.math.BigInteger.valueOf(Math.abs(RANDOM.nextInt())).toString(16).toUpperCase();
	}

	@Override
	public DocXParagraphStyle makeParagraphStyle() {
		return newInstance(DocXParagraphStyle.class);
	}

	@Override
	public DocXRunStyle makeRunStyle() {
		return newInstance(DocXRunStyle.class);
	}

	@Override
	protected NamedDocXStyle makeNamedStyle() {
		return newInstance(NamedDocXStyle.class);
	}

	public NamedDocXStyle makeNewDocXStyle(Style style, NamedDocXStyle parent) {
		NamedDocXStyle returned = makeNamedStyle();
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

	public Font makeFont(RFonts rFonts) {
		/*System.out.println("Comment faire une fonte avec " + rFonts);
		System.out.println("ascii=" + rFonts.getAscii());
		System.out.println("hAnsi=" + rFonts.getHAnsi());
		System.out.println("cs=" + rFonts.getCs());
		System.out.println("hint=" + rFonts.getHint());*/
		return null;
	}

	public java.awt.Color makeColor(Color color) {
		if (color.getVal().length() == 6) {
			int r = Integer.parseInt(color.getVal().substring(0, 2), 16);
			int g = Integer.parseInt(color.getVal().substring(2, 4), 16);
			int b = Integer.parseInt(color.getVal().substring(4, 6), 16);
			return new java.awt.Color(r, g, b);
		}
		return null;
	}

	public void extractStyleProperties(RPrAbstract rPr, DocXRunStyle style) {

		RFonts rFonts = rPr.getRFonts();
		if (rFonts != null) {
			style.setFont(makeFont(rFonts));
		}

		HpsMeasure sz = rPr.getSz();
		if (sz != null) {
			style.setFontSize(sz.getVal().intValue() / 2);
		}

		Color color = rPr.getColor();
		if (color != null) {
			style.setFontColor(makeColor(color));
		}

		if (rPr.getHighlight() != null) {
			String name = rPr.getHighlight().getVal();
			java.awt.Color bgColor = decodeHighlightName(name);
			style.setBackgroundColor(bgColor);
		}

		BooleanDefaultTrue b = rPr.getB();
		if (b != null) {
			style.setBold(b.isVal());
		}

		BooleanDefaultTrue i = rPr.getI();
		if (i != null) {
			style.setItalic(i.isVal());
		}

		U u = rPr.getU();
		if (u != null) {
			style.setUnderline(true);
		}

	}

	protected java.awt.Color decodeHighlightName(String name) {
		if ("yellow".equals(name.toLowerCase())) {
			return java.awt.Color.YELLOW;
		}
		else if ("blue".equals(name.toLowerCase())) {
			return java.awt.Color.BLUE;
		}
		else if ("cyan".equals(name.toLowerCase())) {
			return java.awt.Color.CYAN;
		}
		else if ("gray".equals(name.toLowerCase())) {
			return java.awt.Color.GRAY;
		}
		else if ("green".equals(name.toLowerCase())) {
			return java.awt.Color.GREEN;
		}
		else if ("magenta".equals(name.toLowerCase())) {
			return java.awt.Color.MAGENTA;
		}
		else if ("orange".equals(name.toLowerCase())) {
			return java.awt.Color.ORANGE;
		}
		else if ("pink".equals(name.toLowerCase())) {
			return java.awt.Color.PINK;
		}
		else if ("red".equals(name.toLowerCase())) {
			return java.awt.Color.RED;
		}
		else if ("white".equals(name.toLowerCase())) {
			return java.awt.Color.WHITE;
		}
		return null;
	}

	public void extractStyleProperties(PPrBase pPr, DocXParagraphStyle style) {

		if (pPr.getJc() != null) {
			JcEnumeration align = pPr.getJc().getVal();
			if (align.value().equals("center")) {
				style.setParagraphAlignment(ParagraphAlignment.Center);
			}
			else if (align.value().equals("left")) {
				style.setParagraphAlignment(ParagraphAlignment.Left);
			}
			else if (align.value().equals("right")) {
				style.setParagraphAlignment(ParagraphAlignment.Right);
			}
			else if (align.value().equals("both")) {
				style.setParagraphAlignment(ParagraphAlignment.Justify);
			}
		}

		if (pPr.getTabs() != null) {
			TabStop[] tabs = new TabStop[pPr.getTabs().getTab().size()];
			for (CTTabStop stop : pPr.getTabs().getTab()) {
				float pos = stop.getPos().intValue() / INDENTS_MULTIPLIER;
				int align = TabStop.ALIGN_LEFT;
				if (STTabJc.LEFT.equals(stop.getVal())) {
					align = TabStop.ALIGN_LEFT;
				}
				else if (STTabJc.RIGHT.equals(stop.getVal())) {
					align = TabStop.ALIGN_RIGHT;
				}
				else if (STTabJc.CENTER.equals(stop.getVal())) {
					align = TabStop.ALIGN_CENTER;
				}
				else if (STTabJc.BAR.equals(stop.getVal())) {
					align = TabStop.ALIGN_BAR;
				}
				else if (STTabJc.DECIMAL.equals(stop.getVal())) {
					align = TabStop.ALIGN_DECIMAL;
				}
				int leader = TabStop.LEAD_NONE;
				style.addToParagraphTabs(makeParagraphTab(pos, align, leader));
			}
		}

		if (pPr.getSpacing() != null) {
			ParagraphSpacing pSpacing = newInstance(ParagraphSpacing.class);
			style.setParagraphSpacing(pSpacing);
			if (STLineSpacingRule.AT_LEAST.equals(pPr.getSpacing().getLineRule())) {
				pSpacing.setLineSpacingRule(LineSpacingRule.AT_LEAST);
				pSpacing.setLine(pPr.getSpacing().getLine().intValue());
			}
			if (STLineSpacingRule.AUTO.equals(pPr.getSpacing().getLineRule())) {
				pSpacing.setLineSpacingRule(LineSpacingRule.AUTO);
				pSpacing.setLine(pPr.getSpacing().getLine().intValue());
			}
			if (pPr.getSpacing().getBefore() != null) {
				pSpacing.setBefore(pPr.getSpacing().getBefore().intValue() / INDENTS_MULTIPLIER);
			}
			if (pPr.getSpacing().getAfter() != null) {
				pSpacing.setAfter(pPr.getSpacing().getAfter().intValue() / INDENTS_MULTIPLIER);
			}
		}

		if (pPr.getInd() != null) {
			ParagraphIndent pIndent = newInstance(ParagraphIndent.class);
			style.setParagraphIndent(pIndent);
			if (pPr.getInd().getLeft() != null) {
				int left = pPr.getInd().getLeft().intValue() / INDENTS_MULTIPLIER;
				pIndent.setLeft(left);
			}
			if (pPr.getInd().getRight() != null) {
				int right = pPr.getInd().getRight().intValue() / INDENTS_MULTIPLIER;
				pIndent.setRight(right);
			}
			if (pPr.getInd().getFirstLine() != null) {
				int first = pPr.getInd().getFirstLine().intValue() / INDENTS_MULTIPLIER;
				pIndent.setFirst(first);
			}
		}

	}

	public ParagraphTab makeParagraphTab(float pos, int align, int leader) {
		ParagraphTab returned = newInstance(ParagraphTab.class);
		returned.setPos(pos);
		returned.setAlign(align);
		returned.setLeader(leader);
		return returned;
	}

	public DocXRunStyle makeRunStyle(RPrAbstract rPr) {

		DocXRunStyle returned = newInstance(DocXRunStyle.class);
		returned.updateFromRPr(rPr, this);
		return returned;
	}

	public DocXParagraphStyle makeParagraphStyle(PPrBase pPr) {

		DocXParagraphStyle returned = newInstance(DocXParagraphStyle.class);
		returned.updateFromPPr(pPr, this);
		return returned;
	}

}
