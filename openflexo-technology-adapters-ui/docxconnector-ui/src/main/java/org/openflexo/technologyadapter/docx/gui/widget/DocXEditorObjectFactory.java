package org.openflexo.technologyadapter.docx.gui.widget;

import java.math.BigInteger;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.docx4all.xml.ObjectFactory;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.CTSmartTagRun;
import org.docx4j.wml.Document;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.Id;
import org.docx4j.wml.Jc;
import org.docx4j.wml.P;
import org.docx4j.wml.P.Hyperlink;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase.PStyle;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.RStyle;
import org.docx4j.wml.SdtBlock;
import org.docx4j.wml.SdtContentBlock;
import org.docx4j.wml.SdtPr;
import org.docx4j.wml.Tag;
import org.docx4j.wml.Text;
import org.docx4j.wml.U;
import org.jvnet.jaxb2_commons.ppp.Child;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXParagraph;

final class DocXEditorObjectFactory extends ObjectFactory {

	private static final Logger logger = Logger.getLogger(DocXEditorObjectFactory.class.getPackage().getName());

	/**
	 * 
	 */
	private final DocXDocument document;

	/**
	 * @param docXEditor
	 */
	DocXEditorObjectFactory(DocXDocument document) {
		this.document = document;
	}

	private void updateDocXDocument() {
		// TODO: i know, this is ugly.
		// I do not see yet any other solution with the short time i got
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				document.updateFromWordprocessingMLPackage(document.getWordprocessingMLPackage(), document.getFactory());
			}
		});
	}

	@Override
	public P createP(String textContent) {
		System.out.println("**************** on cree un P pour " + textContent);
		P returned = super.createP(textContent);
		updateDocXDocument();
		return returned;
	}

	@Override
	public R createR(String textContent) {
		System.out.println("**************** on cree un R pour " + textContent);
		return super.createR(textContent);
	}

	@Override
	public SdtBlock createSdtBlock() {
		System.out.println("**************** on cree un SdtBlock");
		return super.createSdtBlock();
	}

	@Override
	public SdtContentBlock createSdtContentBlock() {
		System.out.println("**************** on cree un SdtContentBlock");
		return super.createSdtContentBlock();
	}

	@Override
	public Text createT(String textContent) {
		System.out.println("**************** on cree un T");
		return super.createT(textContent);
	}

	@Override
	public Document createEmptyDocument() {
		System.out.println("**************** on cree un Empty Document");
		return super.createEmptyDocument();
	}

	@Override
	public WordprocessingMLPackage createEmptyDocumentPackage() {
		System.out.println("**************** on cree un Empty Document Package (WordprocessingMLPackage)");
		return super.createEmptyDocumentPackage();
	}

	@Override
	public WordprocessingMLPackage createDocumentPackage(Document doc) {
		System.out.println("**************** on cree un Document Package (WordprocessingMLPackage)");
		return super.createDocumentPackage(doc);
	}

	@Override
	public Document createEmptySharedDocument() {
		System.out.println("**************** on cree un Shared Document");
		return super.createEmptySharedDocument();
	}

	@Override
	public PStyle createPStyle(String styleId) {
		System.out.println("**************** on cree un PStyle avec " + styleId);
		return super.createPStyle(styleId);
	}

	@Override
	public Tag createTag(String val) {
		System.out.println("**************** on cree un Tag avec " + val);
		return super.createTag(val);
	}

	@Override
	public Id createId(BigInteger val) {
		System.out.println("**************** on cree un Id avec " + val);
		return super.createId(val);
	}

	@Override
	public Hyperlink createHyperlink() {
		System.out.println("**************** on cree un Hyperlink");
		return super.createHyperlink();
	}

	@Override
	public BooleanDefaultTrue createBooleanDefaultTrue(Boolean b) {
		System.out.println("**************** on cree un BooleanDefaultTrue avec " + b);
		return super.createBooleanDefaultTrue(b);
	}

	@Override
	public CTSmartTagRun createCTSmartTagRun(String textContent) {
		System.out.println("**************** on cree un CTSmartTagRun avec " + textContent);
		return super.createCTSmartTagRun(textContent);
	}

	@Override
	public HpsMeasure createHpsMeasure(Integer value) {
		System.out.println("**************** on cree un HpsMeasure avec " + value);
		return super.createHpsMeasure(value);
	}

	@Override
	public Jc createJc(Integer align) {
		System.out.println("**************** on cree un Jc avec " + align);
		return super.createJc(align);
	}

	@Override
	public PPr createPPr() {
		System.out.println("**************** on cree un PPr");
		return super.createPPr();
	}

	@Override
	public RPr createRPr() {
		System.out.println("**************** on cree un RPr");
		return super.createRPr();
	}

	@Override
	public RFonts createRPrRFonts(String ascii) {
		System.out.println("**************** on cree un RPrRFonts avec " + ascii);
		return super.createRPrRFonts(ascii);
	}

	@Override
	public RStyle createRStyle(String styleId) {
		System.out.println("**************** on cree un RStyle avec " + styleId);
		return super.createRStyle(styleId);
	}

	@Override
	public SdtPr createSdtPr() {
		System.out.println("**************** on cree un SdtPr");
		return super.createSdtPr();
	}

	@Override
	public U createUnderline(String value, String color) {
		System.out.println("**************** on cree un U avec " + value + " and " + color);
		return super.createUnderline(value, color);
	}

	@Override
	public void textChanged(Text text) {

		// We first update the structure, in case of inducted structure modifications
		updateDocXDocument();

		P docXP = getContainer(text, P.class);
		if (docXP != null) {
			DocXParagraph paragraph = document.getParagraph(docXP);
			if (paragraph != null) {
				// We then fire textChanged on related paragraph
				paragraph.fireTextChanged();
			} else {
				logger.warning("Cannot find paragraph for " + docXP);
			}
		} else {
			logger.warning("Cannot find paragraph for " + text);
		}
	}

	public static <T> T getContainer(Object o, Class<T> containerClass) {
		if (o == null) {
			return null;
		}
		if (containerClass.isAssignableFrom(o.getClass())) {
			return (T) o;
		}
		if (o instanceof Child) {
			return getContainer(((Child) o).getParent(), containerClass);
		}
		return null;
	}
}