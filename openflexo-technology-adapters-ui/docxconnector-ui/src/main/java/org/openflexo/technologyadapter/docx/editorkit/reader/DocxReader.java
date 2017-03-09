package org.openflexo.technologyadapter.docx.editorkit.reader;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import javax.xml.bind.JAXBElement;

import org.docx4j.dml.wordprocessingDrawing.Anchor;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.CTTabStop;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STLineSpacingRule;
import org.docx4j.wml.STTabJc;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblGrid;
import org.docx4j.wml.TblGridCol;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;
import org.openflexo.technologyadapter.docx.editorkit.DocxDocument;

/**
 * Implements reader of document.
 *
 * @author Stanislav Lapitsky
 */
public class DocxReader {
	/**
	 * document instance to the building.
	 */
	protected DocxDocument document;
	WordprocessingMLPackage wordMLPackage;

	/**
	 * Current offset in the document for insert action.
	 */
	private int currentOffset = 0;

	SimpleAttributeSet parAttrs;
	SimpleAttributeSet charAttrs;

	public static final int INDENTS_MULTIPLIER = 20;

	/**
	 * Builds new instance of reader.
	 *
	 * @param doc
	 *            document for reading to.
	 */
	public DocxReader(Document doc) {
		document = (DocxDocument) doc;
	}

	/**
	 * Reads content of specified file to the document.
	 *
	 * @param fileName
	 *            path to the file.
	 * @param offset
	 *            offset to read the content.
	 */
	public void read(String fileName, int offset) throws IOException, BadLocationException {
		FileInputStream in = new FileInputStream(fileName);
		read(in, offset);
		in.close();
	}

	/**
	 * Reads content of specified stream to the document.
	 *
	 * @param in
	 *            stream.
	 */
	public void read(InputStream in, int offset) throws IOException, BadLocationException {
		try {
			System.out.println("Et on commence a lire " + in);

			wordMLPackage = WordprocessingMLPackage.load(in);

			MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
			iteratePart(documentPart.getContent());

			this.currentOffset = offset;
		} catch (Docx4JException e) {
			e.printStackTrace();
			throw new IOException("Can't read the document.", e);
		}
	}

	public void iteratePart(List<Object> content) throws BadLocationException {
		System.out.println("on itere sur " + content);
		for (Object obj : content) {
			System.out.println(" * je tombe sur " + obj);
			if (obj instanceof P) {
				processParagraph((P) obj);
				if (obj != content.get(content.size() - 1)) {
					document.insertString(currentOffset, "\n", charAttrs);
					document.setParagraphAttributes(currentOffset, 1, parAttrs, true);
					currentOffset++;
				}
				else {
					document.setParagraphAttributes(currentOffset, 1, parAttrs, true);
				}
			}
			else if (obj instanceof R) {
				processRun((R) obj);
			}
			else if (obj instanceof Tbl) {
				processTable((Tbl) obj);
			}
			else if (obj instanceof Drawing) {
				processDrawing((Drawing) obj);
			}
			else if (obj instanceof JAXBElement) {
				JAXBElement el = (JAXBElement) obj;
				if (el.getDeclaredType().equals(Text.class)) {
					String text = ((Text) el.getValue()).getValue();
					document.insertString(currentOffset, text, charAttrs);
					currentOffset += text.length();
				}
				else if (el.getDeclaredType().equals(Tbl.class)) {
					Tbl tbl = (Tbl) el.getValue();
					processTable(tbl);
				}
				else if (el.getDeclaredType().equals(Drawing.class)) {
					Drawing d = (Drawing) el.getValue();
					processDrawing(d);
				}
			}
			else {
				System.out.println(obj);
			}
		}
	}

	protected void processParagraph(P p) throws BadLocationException {
		PPr pPr = p.getPPr();
		parAttrs = new SimpleAttributeSet();
		if (pPr != null) {
			if (pPr.getJc() != null) {
				JcEnumeration align = pPr.getJc().getVal();
				if (align.value().equals("center")) {
					StyleConstants.setAlignment(parAttrs, StyleConstants.ALIGN_CENTER);
				}
				else if (align.value().equals("left")) {
					StyleConstants.setAlignment(parAttrs, StyleConstants.ALIGN_LEFT);
				}
				else if (align.value().equals("right")) {
					StyleConstants.setAlignment(parAttrs, StyleConstants.ALIGN_RIGHT);
				}
				else if (align.value().equals("both")) {
					StyleConstants.setAlignment(parAttrs, StyleConstants.ALIGN_JUSTIFIED);
				}
			}
			if (pPr.getTabs() != null) {
				TabStop[] tabs = new TabStop[pPr.getTabs().getTab().size()];
				int i = 0;
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
					TabStop ts = new TabStop(pos, align, leader);
					tabs[i] = ts;
					i++;
				}
				StyleConstants.setTabSet(parAttrs, new TabSet(tabs));
			}
			if (pPr.getSpacing() != null) {
				if (STLineSpacingRule.AT_LEAST.equals(pPr.getSpacing().getLineRule())) {
					float ls = pPr.getSpacing().getLine().intValue() / 240;
					StyleConstants.setLineSpacing(parAttrs, ls);
				}
				if (STLineSpacingRule.AUTO.equals(pPr.getSpacing().getLineRule())) {
					float ls = pPr.getSpacing().getLine().intValue() / 240;
					StyleConstants.setLineSpacing(parAttrs, ls);
				}
				if (pPr.getSpacing().getBefore() != null) {
					int before = pPr.getSpacing().getBefore().intValue() / INDENTS_MULTIPLIER;
					StyleConstants.setSpaceAbove(parAttrs, before);
				}
				if (pPr.getSpacing().getAfter() != null) {
					int after = pPr.getSpacing().getAfter().intValue() / INDENTS_MULTIPLIER;
					StyleConstants.setSpaceBelow(parAttrs, after);
				}
			}
			if (pPr.getInd() != null) {
				if (pPr.getInd().getLeft() != null) {
					int left = pPr.getInd().getLeft().intValue() / INDENTS_MULTIPLIER;
					StyleConstants.setLeftIndent(parAttrs, left);
				}
				if (pPr.getInd().getRight() != null) {
					int right = pPr.getInd().getRight().intValue() / INDENTS_MULTIPLIER;
					StyleConstants.setRightIndent(parAttrs, right);
				}
				if (pPr.getInd().getFirstLine() != null) {
					int first = pPr.getInd().getFirstLine().intValue() / INDENTS_MULTIPLIER;
					StyleConstants.setFirstLineIndent(parAttrs, first);
				}
			}
		}
		iteratePart(p.getContent());
	}

	protected void processRun(R run) throws BadLocationException {
		RPr rPr = run.getRPr();
		charAttrs = new SimpleAttributeSet();

		if (rPr != null) {
			if (rPr.getSz() != null) {
				int size = rPr.getSz().getVal().intValue();
				StyleConstants.setFontSize(charAttrs, size / 2);
			}
			if (rPr.getB() != null) {
				StyleConstants.setBold(charAttrs, true);
			}
			if (rPr.getI() != null) {
				StyleConstants.setItalic(charAttrs, true);
			}
			if (rPr.getU() != null) {
				StyleConstants.setUnderline(charAttrs, true);
			}
			if (rPr.getRFonts() != null) {
				String fName = rPr.getRFonts().getCs();
				if (fName == null) {
					fName = rPr.getRFonts().getAscii();
				}
				if (fName == null) {
					fName = rPr.getRFonts().getHAnsi();
				}
				if (fName == null) {
					fName = rPr.getRFonts().getEastAsia();
				}
				if (fName != null) {
					StyleConstants.setFontFamily(charAttrs, fName);
				}
			}
			if (rPr.getColor() != null) {
				String name = rPr.getColor().getVal();
				if (!name.toLowerCase().equals("auto")) {
					Color color = new Color(Integer.parseInt(name.substring(0, 2), 16), Integer.parseInt(name.substring(2, 4), 16),
							Integer.parseInt(name.substring(4), 16));
					StyleConstants.setForeground(charAttrs, color);
				}
			}
			if (rPr.getHighlight() != null) {
				String name = rPr.getHighlight().getVal();
				Color color = decodeHighlightName(name);
				StyleConstants.setBackground(charAttrs, color);
			}

		}
		iteratePart(run.getContent());
	}

	protected Color decodeHighlightName(String name) {
		if ("yellow".equals(name.toLowerCase())) {
			return Color.YELLOW;
		}
		else if ("blue".equals(name.toLowerCase())) {
			return Color.BLUE;
		}
		else if ("cyan".equals(name.toLowerCase())) {
			return Color.CYAN;
		}
		else if ("gray".equals(name.toLowerCase())) {
			return Color.GRAY;
		}
		else if ("green".equals(name.toLowerCase())) {
			return Color.GREEN;
		}
		else if ("magenta".equals(name.toLowerCase())) {
			return Color.MAGENTA;
		}
		else if ("orange".equals(name.toLowerCase())) {
			return Color.ORANGE;
		}
		else if ("pink".equals(name.toLowerCase())) {
			return Color.PINK;
		}
		else if ("red".equals(name.toLowerCase())) {
			return Color.RED;
		}
		else if ("white".equals(name.toLowerCase())) {
			return Color.WHITE;
		}
		return null;
	}

	protected void processTable(Tbl table) throws BadLocationException {
		TblPr tblPr = table.getTblPr();
		TblGrid tblGrid = table.getTblGrid();
		SimpleAttributeSet tableAttrs = new SimpleAttributeSet();
		int cellCount = 0;
		int rowCount = 0;
		for (Object tblObj : table.getContent()) {
			if (tblObj instanceof Tr) {
				rowCount++;
				Tr row = (Tr) tblObj;
				cellCount = Math.max(cellCount, processRow(row));
			}
		}

		int[] rowHeights = new int[rowCount];
		for (int i = 0; i < rowHeights.length; i++) {
			rowHeights[i] = 1;
		}
		int[] colWidths = new int[cellCount];
		int i = 0;
		for (TblGridCol col : tblGrid.getGridCol()) {
			colWidths[i] = col.getW().intValue() / INDENTS_MULTIPLIER;
			i++;
		}

		document.insertTable(currentOffset, rowCount, cellCount, tableAttrs, colWidths, rowHeights);
		for (Object tblObj : table.getContent()) {
			if (tblObj instanceof Tr) {
				Tr row = (Tr) tblObj;
				for (Object rowObj : row.getContent()) {
					if (rowObj instanceof Tc) {
						Tc cell = (Tc) rowObj;
						iteratePart(cell.getContent());
						currentOffset++;
					}
					else if (rowObj instanceof JAXBElement) {
						JAXBElement el = (JAXBElement) rowObj;
						if (el.getDeclaredType().equals(Tc.class)) {
							Tc cell = (Tc) el.getValue();
							iteratePart(cell.getContent());
							currentOffset++;
						}
					}
				}
			}
		}
	}

	/**
	 *
	 * @param row
	 *            docx row
	 * @return cell count
	 * @throws BadLocationException
	 */
	protected int processRow(Tr row) throws BadLocationException {
		int res = 0;
		for (Object rowObj : row.getContent()) {
			res++;
			if (rowObj instanceof Tc) {
				Tc cell = (Tc) rowObj;
				TcPr tcPr = cell.getTcPr();
				// iteratePart(cell.getContent());
			}
		}
		return res;
	}

	protected void processDrawing(Drawing drawing) throws BadLocationException {
		for (Object obj : drawing.getAnchorOrInline()) {
			if (obj instanceof Inline) {
				Inline inline = (Inline) obj;
				String id = inline.getGraphic().getGraphicData().getPic().getBlipFill().getBlip().getEmbed();
				insertImageFromId(id);
			}
			else if (obj instanceof Anchor) {
				Anchor anchor = (Anchor) obj;
				String id = anchor.getGraphic().getGraphicData().getPic().getBlipFill().getBlip().getEmbed();
				insertImageFromId(id);
			}
		}
	}

	private void insertImageFromId(String id) {
		Relationship rel = wordMLPackage.getMainDocumentPart().getRelationshipsPart().getRelationshipByID(id);

		Part p = wordMLPackage.getMainDocumentPart().getRelationshipsPart().getPart(rel);
		ByteBuffer bb = ((BinaryPartAbstractImage) p).getBuffer();
		byte[] bytes = new byte[bb.remaining()];
		bb.get(bytes);
		ImageIcon ii = new ImageIcon(bytes);
		document.insertPicture(ii, currentOffset);
		currentOffset++;
	}

	public static void main(String[] args) throws Exception {
		if (args.length > 0) {
			String filePath = args[0];
			DocxReader reader = new DocxReader(new DocxDocument());
			reader.read(filePath, 0);

			System.out.println(reader.document.getText(0, reader.document.getLength()));
		}

	}
}
