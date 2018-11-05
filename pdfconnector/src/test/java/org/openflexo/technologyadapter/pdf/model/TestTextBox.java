package org.openflexo.technologyadapter.pdf.model;

import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;
import java.util.Arrays;

import org.junit.Test;

public class TestTextBox {

	static TextBox a = new TextBox("A", new Rectangle(20, 20, 20, 20), 0);
	static TextBox b = new TextBox("B", new Rectangle(50, 10, 50, 40), 0);
	static TextBox c = new TextBox("C", new Rectangle(110, 20, 50, 40), 0);
	static TextBox d = new TextBox("D", new Rectangle(40, 70, 30, 30), 0);
	static TextBox e = new TextBox("E", new Rectangle(80, 80, 50, 30), 0);
	static TextBox f = new TextBox("F", new Rectangle(140, 70, 30, 20), 0);
	static TextBox g = new TextBox("G", new Rectangle(180, 50, 50, 40), 0);

	@Test
	public void testNormalizeString() {
		String s = "➤➤➤➤CLARIFIANT➤pRéveNTIF➤TRIpLe➤ACTION➤:➤\n1.➤ANTI-ALGUeS➤/➤2.➤ANTI-TARTRe➤➤\n3.➤ANTI-DépOTS➤MéTALLIQUeS";
		assertEquals("  CLARIFIANT pRéveNTIF TRIpLe ACTION : \n1. ANTI-ALGUeS / 2. ANTI-TARTRe \n3. ANTI-DépOTS MéTALLIQUeS",
				TextBox.normalizeString(s));
	}

	@Test
	public void testRowArrangement1() {
		TextBox[] l = { a, b, c, d, e, f, g };
		assertEquals("A B C\nD E F G", TextBox.getSelectedText(Arrays.asList(l), false));
	}

	@Test
	public void testRowArrangement2() {
		TextBox[] l = { a, c, f, g, b, e, d };
		assertEquals("A B C\nD E F G", TextBox.getSelectedText(Arrays.asList(l), false));
	}

}
