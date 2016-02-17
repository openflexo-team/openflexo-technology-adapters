package org.openflexo.technologyadapter.pdf.model;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.openflexo.toolbox.StringUtils;

public class TextBox extends AbstractBox {
	private final String text;
	private final float dir;

	public TextBox(String text, Rectangle box, float dir) {
		super(box);
		this.text = text;
		this.dir = dir;
		// System.out.println("Box for [" + text + "] box=" + box + " dir=" + dir);
	}

	public String getText() {
		return text;
	}

	public float getDir() {
		return dir;
	}

	@Override
	public String toString() {
		return "Box for [" + text + "] box=" + getBox() + " dir=" + dir;
	}

	/**
	 * Use topological informations found in supplied TextBox list to arrange unordered text as a list of rows, each row composed of a list
	 * of TextBox in left-to right order
	 * 
	 * @param textBoxes
	 * @return
	 */
	public static List<List<TextBox>> arrangeAsRows(List<TextBox> textBoxes) {
		List<Row> rows = _arrangeAsRows(textBoxes, 0.3);
		List<List<TextBox>> returned = new ArrayList<>();

		for (Row row : rows) {
			returned.add(row.boxes);
		}

		return returned;
	}

	/**
	 * Use topological informations found in supplied TextBox list to arrange unordered text as a list of rows, each row composed of a list
	 * of TextBox in left-to right order
	 * 
	 * @param textBoxes
	 * @return
	 */
	public static List<List<TextBox>> arrangeAsSingleRow(List<TextBox> textBoxes) {
		List<Row> rows = _arrangeAsSingleRow(textBoxes);
		List<List<TextBox>> returned = new ArrayList<>();

		for (Row row : rows) {
			returned.add(row.boxes);
		}

		return returned;
	}

	/**
	 * Use topological informations found in supplied TextBox list to arrange unordered text as a list of items, each item composed of one
	 * or more row, based on indentation informations
	 * 
	 * @param textBoxes
	 * @return
	 */
	public static List<List<List<TextBox>>> arrangeAsItems(List<TextBox> textBoxes) {
		List<Row> rows = _arrangeAsRows(textBoxes, 0.3);

		List<List<List<TextBox>>> items = new ArrayList<>();

		int i = 0;
		double startX = -1;
		List<List<TextBox>> currentItem = null;
		for (Row row : rows) {
			// System.out.println("row: " + i + " start at " + row.boxes.get(0).getX() + " text=" + getSelectedText(row.boxes));
			if (startX < 0) {
				currentItem = new ArrayList<>();
				currentItem.add(row.boxes);
				items.add(currentItem);
				startX = row.boxes.get(0).getX();
			}
			else {
				if (row.boxes.get(0).getX() > startX + 1.0) {
					currentItem.add(row.boxes);
				}
				else {
					currentItem = new ArrayList<>();
					currentItem.add(row.boxes);
					items.add(currentItem);
				}
			}
			i++;
		}

		return items;
	}

	/**
	 * Use topological informations found in supplied TextBox list to arrange unordered text as a single row composed of a list of TextBox
	 * in left-to right order
	 * 
	 * @param textBoxes
	 * @return
	 */
	private static List<Row> _arrangeAsSingleRow(List<TextBox> textBoxes) {
		List<Row> rows = new ArrayList<>();
		Row newRow = null;

		for (TextBox tb : textBoxes) {
			if (newRow == null) {
				newRow = new Row(tb);
				rows.add(newRow);
			}
			else {
				newRow.add(tb);
			}
		}

		if (newRow != null) {
			Collections.sort(newRow.boxes, new Comparator<TextBox>() {
				@Override
				public int compare(TextBox o1, TextBox o2) {
					double center1 = o1.getX() + o1.getWidth() / 2;
					double center2 = o2.getX() + o2.getWidth() / 2;
					return center2 < center1 ? 1 : (center2 > center1 ? -1 : 0);
				}

			});
		}

		return rows;
	}

	/**
	 * Use topological informations found in supplied TextBox list to arrange unordered text as a list of rows, each row composed of a list
	 * of TextBox in left-to right order<br>
	 * Returned rows are ordered from top to bottom
	 * 
	 * @param textBoxes
	 * @return
	 */
	private static List<Row> _arrangeAsRows(List<TextBox> textBoxes, double rowDetectionThreshold) {
		List<Row> rows = new ArrayList<>();

		for (TextBox tb : textBoxes) {
			if (rows.size() == 0) {
				Row newRow = new Row(tb);
				rows.add(newRow);
			}
			else {
				double bestRatio = Double.NEGATIVE_INFINITY;
				Row bestRow = null;
				Rectangle box = tb.getBox();
				for (Row row : rows) {
					// System.out.println("Par rapport a la row: " + row.bottom + "-" + row.top);
					Rectangle rowRect = new Rectangle(box.x, (int) row.top, (int) box.getWidth(), (int) (row.bottom - row.top));
					Rectangle intersect = rowRect.intersection(box);
					/*double containmentRatio = 0;
					if (intersect.getHeight() > 0) {
						containmentRatio = intersect.getHeight() / box.getHeight();
					}*/
					double containmentRatio = intersect.getHeight() / box.getHeight();
					// System.out.println("Le tb " + tb + " est dedans a : " + containmentRatio);
					if (containmentRatio >= bestRatio) {
						bestRow = row;
						bestRatio = containmentRatio;
					}
				}
				if (bestRatio > rowDetectionThreshold) {
					// OK, this box is more than row detection threshold of this row, complete it
					bestRow.add(tb);
				}
				else {
					Row newRow = new Row(tb);
					rows.add(newRow);
				}
			}
		}

		Collections.sort(rows, new Comparator<Row>() {
			@Override
			public int compare(Row o1, Row o2) {
				double center1 = o1.top;
				double center2 = o2.top;
				return center2 < center1 ? 1 : (center2 > center1 ? -1 : 0);
			}
		});

		for (Row row : rows) {
			Collections.sort(row.boxes, new Comparator<TextBox>() {
				@Override
				public int compare(TextBox o1, TextBox o2) {
					double center1 = o1.getX() + o1.getWidth() / 2;
					double center2 = o2.getX() + o2.getWidth() / 2;
					return center2 < center1 ? 1 : (center2 > center1 ? -1 : 0);
				}

			});
		}

		return rows;
	}

	private static class Row {
		private List<TextBox> boxes = new ArrayList<>();
		private double top = Double.POSITIVE_INFINITY;
		private double bottom = Double.NEGATIVE_INFINITY;

		public Row(TextBox tb) {
			super();
			add(tb);
		}

		public void add(TextBox tb) {
			boxes.add(tb);
			if (tb.getY() < top) {
				top = tb.getY();
			}
			if (tb.getY() + tb.getHeight() > bottom) {
				bottom = tb.getY() + tb.getHeight();
			}
		}
	}

	/**
	 * Compute and returned a formatted text based on unordered set of TextBox<br>
	 * String are normalized, and white space are computed according to topological facts
	 * 
	 * @param textBoxes
	 * @return
	 */
	public static String getSelectedText(List<TextBox> textBoxes, boolean interpretAsSingleRow) {

		List<List<TextBox>> rows;

		if (interpretAsSingleRow) {
			rows = TextBox.arrangeAsSingleRow(textBoxes);
		}
		else {
			rows = TextBox.arrangeAsRows(textBoxes);
		}

		StringBuffer returned = new StringBuffer();

		boolean isFirst = true;
		for (List<TextBox> row : rows) {
			if (!isFirst) {
				returned.append(StringUtils.LINE_SEPARATOR);
			}
			StringBuffer sb = new StringBuffer();
			TextBox previousTB = null;
			boolean needsSpace = false;
			for (TextBox tb : row) {
				String normalizedString = normalizeString(tb.getText());
				if (StringUtils.isNotEmpty(normalizedString)) {
					if (previousTB != null) {
						double space = tb.getX() - previousTB.getX() - previousTB.getWidth();
						// System.out.println("space=" + space + " width=" + tb.getWidth() + " between ["
						// + normalizeString(previousTB.getText()) + "] and [" + normalizedString + "]");
						if ((space > 1 || needsSpace) && !(sb.length() > 0 && sb.charAt(sb.length() - 1) == ' ')) {
							sb.append(" ");
						}
					}
					if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ' ' && normalizedString.charAt(0) == ' ') {
						// In this case, resulting string has already space
						// Skip one space
						sb.append(normalizedString.substring(1));
					}
					else {
						sb.append(normalizedString);
					}
					previousTB = tb;
					if (tb.getText().length() > 0) {
						char lastChar = tb.getText().charAt(tb.getText().length() - 1);
						if (lastChar < 32 || lastChar > 255) {
							needsSpace = true;
						}
						else {
							needsSpace = false;
						}
					}
				}
				/*for (int i = 0; i < tb.getText().length(); i++) {
					System.out.print(tb.getText().charAt(i) + "/[" + tb.getText().codePointAt(i) + "]");
				}*/
			}
			returned.append(sb.toString().trim());
			isFirst = false;
		}
		return returned.toString();
	}

	/**
	 * Return boolean indicating if this char should be handled as significative char
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isIgnorable(char c) {
		return c < 32 || c == 10148;
	}

	/**
	 * Normalize a string which might occurs in a PDF document.<br>
	 * Ignore some chars according to {@link #isIgnorable(char)} semantics. Those chars to be ignored are replaced by white spaces, with the
	 * limitation of one white space for multiple ignored chars.
	 * 
	 * @param s
	 * @return
	 */
	public static String normalizeString(String s) {
		StringBuffer returned = new StringBuffer();
		for (int l = 0; l < StringUtils.linesNb(s); l++) {
			StringBuffer sb = new StringBuffer();
			String line = StringUtils.extractStringAtLine(s, l);
			boolean previousCharWasReplacedByASpace = false;
			for (int i = 0; i < line.length(); i++) {
				char c = line.charAt(i);
				if (!isIgnorable(c)) {
					sb.append(c);
					previousCharWasReplacedByASpace = false;
				}
				else if (!previousCharWasReplacedByASpace) {
					sb.append(' ');
					previousCharWasReplacedByASpace = true;
					// System.out.println("1-Discard char [" + c + "] as " + (int) c);
				}
				else {
					previousCharWasReplacedByASpace = false;
					// System.out.println("2-Discard char [" + c + "] as " + (int) c);
				}
			}
			returned.append((l > 0 ? StringUtils.LINE_SEPARATOR : "") + sb.toString());
		}
		// System.out.println("Normalized [" + s + "] to [" + returned.toString() + "]");
		return returned.toString();
	}

}
