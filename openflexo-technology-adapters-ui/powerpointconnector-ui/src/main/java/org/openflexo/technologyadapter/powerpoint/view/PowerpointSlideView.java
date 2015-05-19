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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hslf.model.AutoShape;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.openflexo.swing.msct.CellSpan;
import org.openflexo.swing.msct.MultiSpanCellTable;
import org.openflexo.swing.msct.MultiSpanCellTableModel;
import org.openflexo.swing.msct.TableCellExtendedRenderer;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointAutoShape;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointShape;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlide;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointTextBox;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointTextShape;
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
			try{
				slide.getSlide().draw((Graphics2D)g);
			}
			catch(ArrayIndexOutOfBoundsException e){
				logger.warning("This slide might contains unparsable comments");
			}

		}
	}

}
