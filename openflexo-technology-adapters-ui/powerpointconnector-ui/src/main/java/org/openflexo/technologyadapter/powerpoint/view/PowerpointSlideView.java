/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.powerpoint.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.logging.Logger;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlide;
import org.openflexo.view.controller.FlexoController;

/**
 * Widget allowing to edit/view a ExcelSheet.<br>
 * We use here an implementation of a MultiSpanCellTable to do it.
 * 
 * @author vincent, sguerin
 * 
 */
@SuppressWarnings("serial")
public class PowerpointSlideView extends JPanel {
	static final Logger logger = Logger.getLogger(PowerpointSlideView.class.getPackage().getName());

	private PowerpointSlide slide;
	private FlexoController controller;

	private PowerpointSlidePane slidePane;

	public PowerpointSlideView(PowerpointSlide slide, FlexoController controller) {
		super(new BorderLayout());
		this.slide = slide;
		this.controller = controller;
		slidePane = new PowerpointSlidePane(slide);
		Dimension dimension = slide.getSlideshow().getSlideShow().getPageSize();
		slidePane.setPreferredSize(dimension);

		JScrollPane pan = new JScrollPane(slidePane);

		add(pan, BorderLayout.CENTER);
	}

	public PowerpointSlide getSlide() {
		return slide;
	}

	/**
	 * @author vincent
	 * 
	 */
	class PowerpointSlidePane extends JLayeredPane {

		private PowerpointSlide slide;

		public PowerpointSlidePane(PowerpointSlide slide) {
			super();
			this.slide = slide;
		}

		public PowerpointSlide getSlide() {
			return slide;
		}

		public void setSlide(PowerpointSlide slide) {
			this.slide = slide;
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponents(g);
			try {
				slide.getSlide().draw((Graphics2D) g);
			} catch (ArrayIndexOutOfBoundsException e) {
				logger.warning("This slide might contains unparsable comments");
			}

		}
	}

}
