/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
 * 
 * This file is part of Connie-core, a component of the software infrastructure 
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

package org.openflexo.ta.dsl.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.openflexo.ta.dsl.parser.lexer.Lexer;
import org.openflexo.ta.dsl.parser.node.Start;
import org.openflexo.ta.dsl.parser.parser.Parser;

/**
 * This class provides the parsing service for DSL.<br>
 * This includes syntactic and semantics analyzer.<br>
 * 
 * SableCC is used to generate the grammar located in dsl-ta-parser.<br>
 * 
 * @author sylvain
 */
public class DSLParser {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DSLParser.class.getPackage().getName());

	/**
	 * This is the method to invoke to perform a parsing.<br>
	 * Syntaxic and semantics analyzer are performed and returned value is a {@link DSLSystem}
	 * 
	 * @param inputStream
	 *            Stream where data is to read from
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static DSLSystem parse(InputStream inputStream, DSLModelFactory modelFactory) throws ParseException, IOException {
		String data = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		System.out.println("read: " + data);
		return parse(data, modelFactory);
	}

	/**
	 * This is the method to invoke to perform a parsing.<br>
	 * Syntaxic and semantics analyzer are performed and returned value is a {@link DSLSystem}
	 * 
	 * @param data
	 *            String representing data to parse
	 * @return
	 * @throws ParseException
	 *             if parsing expression lead to an error
	 */
	public static DSLSystem parse(String data, DSLModelFactory modelFactory) throws ParseException {
		try {
			// System.out.println("Parsing: " + anExpression);

			// Create a Parser instance.
			Parser p = new Parser(new Lexer(new PushbackReader(new StringReader(data))));

			// Parse the input.
			Start tree = p.parse();

			// Apply the semantics analyzer.
			DSLSemanticsAnalyzer t = new DSLSemanticsAnalyzer(modelFactory);
			tree.apply(t);

			return t.getSystem();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParseException(e.getMessage() + " while parsing " + data);
		}
	}

}
