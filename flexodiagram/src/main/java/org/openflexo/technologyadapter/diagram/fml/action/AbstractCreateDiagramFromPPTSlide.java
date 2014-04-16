/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.technologyadapter.diagram.fml.action;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.hslf.model.AutoShape;
import org.apache.poi.hslf.model.Line;
import org.apache.poi.hslf.model.MasterSheet;
import org.apache.poi.hslf.model.Picture;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.ShapeTypes;
import org.apache.poi.hslf.model.SimpleShape;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.Table;
import org.apache.poi.hslf.model.TableCell;
import org.apache.poi.hslf.model.TextBox;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.model.TextShape;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.ForegroundStyle.DashStyle;
import org.openflexo.fge.GraphicalRepresentation.HorizontalTextAlignment;
import org.openflexo.fge.GraphicalRepresentation.ParagraphAlignment;
import org.openflexo.fge.GraphicalRepresentation.VerticalTextAlignment;
import org.openflexo.fge.ShapeGraphicalRepresentation.DimensionConstraints;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.TextStyle;
import org.openflexo.fge.connectors.ConnectorSpecification.ConnectorType;
import org.openflexo.fge.connectors.ConnectorSymbol.EndSymbolType;
import org.openflexo.fge.connectors.ConnectorSymbol.StartSymbolType;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.viewpoint.ViewPointObject;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.toolbox.JavaUtils;
import org.openflexo.toolbox.StringUtils;

public abstract class AbstractCreateDiagramFromPPTSlide<A extends AbstractCreateDiagramFromPPTSlide<A, T>, T extends FlexoObject>
	extends FlexoAction<A,T,ViewPointObject> {

	public AbstractCreateDiagramFromPPTSlide(
			FlexoActionType<A, T, ViewPointObject> actionType,
			T focusedObject, Vector<ViewPointObject> globalSelection,
			FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	public DrawingGraphicalRepresentation graphicalRepresentation;
	private String diagramName;
	
	public DrawingGraphicalRepresentation getGraphicalRepresentation() {
		return graphicalRepresentation;
	}

	public void setGraphicalRepresentation(
			DrawingGraphicalRepresentation graphicalRepresentation) {
		this.graphicalRepresentation = graphicalRepresentation;
	}

	public DiagramResource getDiagramResource() {
		return diagramResource;
	}

	public void setDiagramResource(DiagramResource diagramResource) {
		this.diagramResource = diagramResource;
	}

	public HashMap<DiagramShape, Shape> getShapesMap() {
		return shapesMap;
	}

	public void setShapesMap(HashMap<DiagramShape, Shape> shapesMap) {
		this.shapesMap = shapesMap;
	}

	public List<Shape> getPoiShapes() {
		return poiShapes;
	}

	public void setPoiShapes(List<Shape> poiShapes) {
		this.poiShapes = poiShapes;
	}

	public List<AutoShape> getConnectors() {
		return connectors;
	}

	public void setConnectors(List<AutoShape> connectors) {
		this.connectors = connectors;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	private String diagramTitle;
	private String diagramURI;
	private DiagramResource diagramResource;
	private File diagramFile;

	private SlideShow selectedSlideShow;
	private ArrayList<Slide> currentSlides;
	private File file;
	private Slide slide;

	/*
	 * Diagram Configuration
	 */

	private String errorMessage;

	public String getErrorMessage() {
		isValid();
		return errorMessage;
	}

	@Override
	public boolean isValid() {
		if (StringUtils.isEmpty(diagramName)) {
			errorMessage = noNameMessage();
			return false;
		}

		if (!diagramName.equals(JavaUtils.getClassName(diagramName)) && !diagramName.equals(JavaUtils.getVariableName(diagramName))) {
			errorMessage = invalidNameMessage();
			return false;
		}

		if (StringUtils.isEmpty(diagramTitle)) {
			errorMessage = noTitleMessage();
			return false;
		}

		if (getFile() == null) {
			errorMessage = noFileMessage();
			return false;
		}

		if (getSlide() == null) {
			errorMessage = noSlideMessage();
			return false;
		}

		else {
			errorMessage = "";
		}

		return true;
	}

	public String noDiagramSpecificationSelectedMessage() {
		return FlexoLocalization.localizedForKey("no_diagram_type_selected");
	}

	public String noTitleMessage() {
		return FlexoLocalization.localizedForKey("no_diagram_title_defined");
	}

	public String noFileMessage() {
		return FlexoLocalization.localizedForKey("no_ppt_file_defined");
	}

	public String existingFileMessage() {
		return FlexoLocalization.localizedForKey("file_already_existing");
	}

	public String noNameMessage() {
		return FlexoLocalization.localizedForKey("no_diagram_name_defined");
	}

	public String noSlideMessage() {
		return FlexoLocalization.localizedForKey("no_slide_defined");
	}

	public String invalidNameMessage() {
		return FlexoLocalization.localizedForKey("invalid_name_for_new_diagram");
	}

	public String duplicatedNameMessage() {
		return FlexoLocalization.localizedForKey("a_diagram_with_that_name_already_exists");
	}

	public Diagram getNewDiagram() {
		if (getNewDiagramResource() != null) {
			return getNewDiagramResource().getDiagram();
		}
		return null;
	}

	public DiagramResource getNewDiagramResource() {
		return diagramResource;
	}

	public String getDiagramName() {
		return diagramName;
	}

	public void setDiagramName(String diagramName) {
		boolean wasValid = isValid();
		this.diagramName = diagramName;
		getPropertyChangeSupport().firePropertyChange("diagramName", null, diagramName);
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

	public String getDiagramTitle() {
		return diagramTitle;
	}

	public void setDiagramTitle(String diagramTitle) {
		boolean wasValid = isValid();
		this.diagramTitle = diagramTitle;
		getPropertyChangeSupport().firePropertyChange("diagramTitle", null, diagramTitle);
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

	public String getDiagramURI() {
		return diagramURI;
	}

	public void setDiagramURI(String diagramURI) {
		this.diagramURI = diagramURI;
	}
	
	public File getDiagramFile() {
		return diagramFile;
	}

	public void setDiagramFile(File diagramFile) {
		this.diagramFile = diagramFile;
	}

	/*
	 * PPT Configuration
	 */

	public DiagramFactory getDiagramFactory() {
		return diagramResource.getDiagram().getDiagramFactory();
	}

	public void loadSlideShow() {
		try {
			FileInputStream fis = new FileInputStream(getFile());
			selectedSlideShow = new SlideShow(fis);
			if (currentSlides == null) {
				currentSlides = new ArrayList<Slide>();
			} else {
				currentSlides.clear();
			}
			for (Slide slide : selectedSlideShow.getSlides()) {
				currentSlides.add(slide);
			}
			setCurrentSlides(currentSlides);
			getPropertyChangeSupport().firePropertyChange("selectedSlideShow", null, selectedSlideShow);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		if (file != null) {
			loadSlideShow();
		}
		boolean wasValid = isValid();
		getPropertyChangeSupport().firePropertyChange("file", null, file);
		getPropertyChangeSupport().firePropertyChange("currentSlides", null, getCurrentSlides());
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

	public Slide getSlide() {
		return slide;
	}

	public void setSlide(Slide slide) {
		this.slide = slide;
		boolean wasValid = isValid();
		getPropertyChangeSupport().firePropertyChange("slide", null, getSlide());
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

	public SlideShow getSelectedSlideShow() {
		return selectedSlideShow;
	}

	public void setSelectedSlideShow(SlideShow selectedSlideShow) {
		this.selectedSlideShow = selectedSlideShow;
	}

	public ArrayList<Slide> getCurrentSlides() {
		return currentSlides;
	}

	public void setCurrentSlides(ArrayList<Slide> currentSlides) {
		this.currentSlides = currentSlides;
	}

	public ImageIcon getMiniature(Slide s) {
		double WIDTH = 75;
		Dimension d = s.getSlideShow().getPageSize();
		BufferedImage i = new BufferedImage((int) WIDTH, (int) (WIDTH * d.height / d.width), BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = i.createGraphics();
		graphics.transform(AffineTransform.getScaleInstance(WIDTH / d.width, WIDTH / d.width));
		s.draw(graphics);
		return new ImageIcon(i);
	}

	public ImageIcon getOverview(Slide s) {
		double WIDTH = 400;
		if(s!=null && s.getSlideShow()!=null){
			Dimension d = s.getSlideShow().getPageSize();
			BufferedImage i = new BufferedImage((int) WIDTH, (int) (WIDTH * d.height / d.width), BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = i.createGraphics();
			graphics.transform(AffineTransform.getScaleInstance(WIDTH / d.width, WIDTH / d.width));
			s.draw(graphics);
			return new ImageIcon(i);
		}
		return null;
		
	}
	
	private HashMap<DiagramShape,Shape> shapesMap;
	private List<Shape> poiShapes;
	private List<AutoShape> connectors;

	/*
	 * Transfo PPT to Diagram
	 */
	public void convertSlideToDiagram(Slide slide) {
		MasterSheet master = slide.getMasterSheet();
		
		connectors = new ArrayList<AutoShape>();
		shapesMap = new HashMap<DiagramShape,Shape>();
		poiShapes = new ArrayList<Shape>();

		// Handle Shapes
		if (slide.getFollowMasterObjects()) {
			if (master.getShapes() != null) {
				Shape[] sh = master.getShapes();
				for (int i = sh.length - 1; i >= 0; i--) {
					if (MasterSheet.isPlaceholder(sh[i])) {
						continue;
					}
					poiShapes.add(sh[i]);
				}
			}
		}
		for (Shape shape : slide.getShapes()) {
			poiShapes.add(shape);
		}
		
		for(Shape shape : poiShapes){
			transformPowerpointShape(shape);
		}
		
		// Handle connectors
		for(AutoShape connector : connectors){
			DiagramConnector con = makeConnector(connector, shapesMap);
			if(con==null){
				 makeLine(connector);
			}
		}

	}
	
	private void transformPowerpointShape(Shape shape){
		if (shape instanceof Picture) {
			DiagramShape newShape = makePictureShape((Picture) shape);
			shapesMap.put(newShape, shape);
			getNewDiagram().addToShapes(newShape);
		} else if (shape instanceof AutoShape) {
			if(!isConnector(shape.getShapeType())){
				DiagramShape newShape = makeAutoShape((AutoShape) shape);
				shapesMap.put(newShape, shape);
				getNewDiagram().addToShapes(newShape);
			} else{
				connectors.add((AutoShape)shape);
			}
		} else if (shape instanceof TextBox) {
			DiagramShape newShape = makeTextBox((TextBox) shape);
			shapesMap.put(newShape, shape);
			getNewDiagram().addToShapes(newShape);
		} else if (shape instanceof Table){
			DiagramShape newShape = makeTable((Table) shape);
			shapesMap.put(newShape, shape);
			getNewDiagram().addToShapes(newShape);
		} else if (shape instanceof Line){
			DiagramConnector con = makeLine((Line) shape);
			getNewDiagram().addToConnectors(con);
			getNewDiagram().addToShapes(con.getStartShape());
			getNewDiagram().addToShapes(con.getEndShape());
		}
		
	}
	
	private DiagramShape makeTable(Table table) {

		DiagramShape newTable = getDiagramFactory().makeNewShape(table.getShapeName(), getNewDiagram());
		ShapeGraphicalRepresentation gr = newTable.getGraphicalRepresentation();
		gr.setX(table.getAnchor2D().getX());
		gr.setY(table.getAnchor2D().getY());
		gr.setWidth(table.getCoordinates().getWidth());
		gr.setHeight(table.getCoordinates().getHeight());
		gr.setBorder(getDiagramFactory().makeShapeBorder(0, 0, 0, 0));
		gr.setShadowStyle(getDiagramFactory().makeDefaultShadowStyle());
		gr.getForeground().setNoStroke(true);
		gr.setTransparency(1);
		
        for (int col = 0; col < table.getNumberOfColumns(); col++) {
        	for (int row = 0; row < table.getNumberOfRows(); row++) {
        		TableCell cell = table.getCell(row, col);
        		DiagramShape newCell = makeTextBox(cell);
        		newCell.getGraphicalRepresentation().getForeground().setNoStroke(false);
        		newCell.getGraphicalRepresentation().setDimensionConstraints(DimensionConstraints.UNRESIZABLE);
        		newTable.addToShapes(newCell);
        	}
        }
		
        newTable.setGraphicalRepresentation(gr);
		return newTable;
	}


	private DiagramShape makeAutoShape(AutoShape autoShape) {
		DiagramShape newShape = getDiagramFactory().makeNewShape(autoShape.getText(), getNewDiagram());

		ShapeGraphicalRepresentation gr = newShape.getGraphicalRepresentation();
		gr.setX(autoShape.getAnchor2D().getX());
		gr.setY(autoShape.getAnchor2D().getY());
		gr.setWidth(autoShape.getAnchor2D().getWidth());
		gr.setHeight(autoShape.getAnchor2D().getHeight());
		gr.setBorder(getDiagramFactory().makeShapeBorder(0, 0, 0, 0));

		gr.setShadowStyle(getDiagramFactory().makeDefaultShadowStyle());

		if (autoShape.getLineColor() != null) {
			gr.setForeground(getDiagramFactory().makeForegroundStyle(autoShape.getLineColor(), (float) autoShape.getLineWidth(),
					convertDashLineStyles(autoShape.getLineDashing())));
		} else {
			gr.setForeground(getDiagramFactory().makeNoneForegroundStyle());
		}

		if (autoShape.getFillColor() != null) {
			gr.setBackground(getDiagramFactory().makeColoredBackground(autoShape.getFillColor()));
		} else {
			gr.setBackground(getDiagramFactory().makeEmptyBackground());
			gr.setShadowStyle(getDiagramFactory().makeNoneShadowStyle());
		}
		
		setTextProperties(gr, autoShape);
		newShape.setGraphicalRepresentation(gr);
		return newShape;
	}

	/**
	 * A line is without start/end shapes
	 * @param line
	 * @return
	 */
	private DiagramConnector makeLine(Line line){
		//Create a virtual start/end shape.
		DiagramShape sourceShape = getDiagramFactory().makeNewShape("", getNewDiagram());
		DiagramShape targetShape = getDiagramFactory().makeNewShape("", getNewDiagram());
		ShapeGraphicalRepresentation sourceShapeGR = sourceShape.getGraphicalRepresentation();
		sourceShapeGR.setX(line.getAnchor2D().getMinX());
		sourceShapeGR.setY(line.getAnchor2D().getMaxY());
		sourceShapeGR.setWidth(2);
		sourceShapeGR.setHeight(2);
		sourceShapeGR.setDimensionConstraints(DimensionConstraints.UNRESIZABLE);
		ShapeGraphicalRepresentation targetShapeGR = targetShape.getGraphicalRepresentation();
		targetShapeGR.setX(line.getAnchor2D().getMaxX());
		targetShapeGR.setY(line.getAnchor2D().getMinY());
		targetShapeGR.setWidth(2);
		targetShapeGR.setHeight(2);
		targetShapeGR.setDimensionConstraints(DimensionConstraints.UNRESIZABLE);
		
		DiagramConnector newConnector = getDiagramFactory().makeNewConnector(line.getShapeName(), sourceShape, targetShape, getNewDiagram());
		ConnectorGraphicalRepresentation gr = newConnector.getGraphicalRepresentation();
		
		if (line.getLineColor() != null) {
			gr.setForeground(getDiagramFactory().makeForegroundStyle(line.getLineColor(), (float) line.getLineWidth(),
					convertDashLineStyles(line.getLineDashing())));
		} else {
			gr.setForeground(getDiagramFactory().makeNoneForegroundStyle());
		}
		setConnectorType(gr, line);
		newConnector.setGraphicalRepresentation(gr);
		return newConnector;
	}
	
	/**
	 * A line is without start/end shapes
	 * @param line
	 * @return
	 */
	private DiagramConnector makeLine(AutoShape poiConnector){
		//Create a virtual start/end shape.
		DiagramShape sourceShape = getDiagramFactory().makeNewShape("", getNewDiagram());
		DiagramShape targetShape = getDiagramFactory().makeNewShape("", getNewDiagram());
		ShapeGraphicalRepresentation sourceShapeGR = sourceShape.getGraphicalRepresentation();
		sourceShapeGR.setX(poiConnector.getAnchor2D().getMinX());
		sourceShapeGR.setY(poiConnector.getAnchor2D().getMaxY());
		sourceShapeGR.setWidth(2);
		sourceShapeGR.setHeight(2);
		sourceShapeGR.setDimensionConstraints(DimensionConstraints.UNRESIZABLE);
		ShapeGraphicalRepresentation targetShapeGR = targetShape.getGraphicalRepresentation();
		targetShapeGR.setX(poiConnector.getAnchor2D().getMaxX());
		targetShapeGR.setY(poiConnector.getAnchor2D().getMinY());
		targetShapeGR.setWidth(2);
		targetShapeGR.setHeight(2);
		targetShapeGR.setDimensionConstraints(DimensionConstraints.UNRESIZABLE);
		
		DiagramConnector newConnector = getDiagramFactory().makeNewConnector(poiConnector.getShapeName(), sourceShape, targetShape, getNewDiagram());
		ConnectorGraphicalRepresentation gr = newConnector.getGraphicalRepresentation();
		
		if (poiConnector.getLineColor() != null) {
			gr.setForeground(getDiagramFactory().makeForegroundStyle(poiConnector.getLineColor(), (float) poiConnector.getLineWidth(),
					convertDashLineStyles(poiConnector.getLineDashing())));
		} else {
			gr.setForeground(getDiagramFactory().makeNoneForegroundStyle());
		}
		newConnector.setGraphicalRepresentation(gr);
		return newConnector;
	}
	
	private DiagramConnector makeConnector(AutoShape poiConnector , HashMap<DiagramShape,Shape> possibleShapes) {
		DiagramShape sourceShape=null,targetShape = null;
		
		for (DiagramShape diagramShape : possibleShapes.keySet()) {
			Shape poiShape = possibleShapes.get(diagramShape);
			
			if(poiConnector.getAnchor().intersects(poiShape.getAnchor())){
				if(sourceShape==null && targetShape == null){
					sourceShape = diagramShape;
				}
				else if(sourceShape!=null && targetShape == null){
					targetShape = diagramShape;
				}
			}
		}
		if(sourceShape!=null && targetShape==null){
			targetShape =sourceShape;
		} else if(targetShape!=null && sourceShape==null){
			sourceShape =targetShape;
		}
		if(sourceShape !=null && targetShape !=null){
			DiagramConnector newConnector = getDiagramFactory().makeNewConnector(poiConnector.getShapeName(), sourceShape, targetShape, getNewDiagram());
			ConnectorGraphicalRepresentation gr = newConnector.getGraphicalRepresentation();
			if (poiConnector.getLineColor() != null) {
				gr.setForeground(getDiagramFactory().makeForegroundStyle(poiConnector.getLineColor(), (float) poiConnector.getLineWidth(),
						convertDashLineStyles(poiConnector.getLineDashing())));
			} else {
				gr.setForeground(getDiagramFactory().makeNoneForegroundStyle());
			}
			setConnectorType(gr, poiConnector);
			newConnector.setGraphicalRepresentation(gr);
			return newConnector;
		}
		else{
			return null;
		}
	}
	
	private DiagramShape makeTextBox(TextBox textBox) {

		DiagramShape newShape = getDiagramFactory().makeNewShape(textBox.getText(), getNewDiagram());

		ShapeGraphicalRepresentation gr = newShape.getGraphicalRepresentation();
		gr.setX(textBox.getAnchor2D().getX());
		gr.setY(textBox.getAnchor2D().getY());
		gr.setWidth(textBox.getAnchor2D().getWidth());
		gr.setHeight(textBox.getAnchor2D().getHeight());
		gr.setBorder(getDiagramFactory().makeShapeBorder(0, 0, 0, 0));

		gr.setForeground(getDiagramFactory().makeNoneForegroundStyle());

		gr.setBackground(getDiagramFactory().makeEmptyBackground());
		gr.setShadowStyle(getDiagramFactory().makeNoneShadowStyle());

		setTextProperties(gr, textBox);

		newShape.setGraphicalRepresentation(gr);

		return newShape;
	}

	private DiagramShape makePictureShape(Picture pictureShape) {

		DiagramShape newShape = getDiagramFactory().makeNewShape(pictureShape.getPictureName(), getNewDiagram());

		ShapeGraphicalRepresentation gr = newShape.getGraphicalRepresentation();

		gr.setX(pictureShape.getAnchor2D().getX());
		gr.setY(pictureShape.getAnchor2D().getY());
		gr.setWidth(pictureShape.getAnchor2D().getWidth());
		gr.setHeight(pictureShape.getAnchor2D().getHeight());
		// gr.setBorder(getDiagramFactory().makeShapeBorder(0, 0, 0, 0));

		BufferedImage image = new BufferedImage((int) pictureShape.getAnchor2D().getWidth(), (int) pictureShape.getAnchor2D().getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		graphics.translate(-pictureShape.getAnchor2D().getX(), -pictureShape.getAnchor2D().getY());
		graphics.clipRect((int) pictureShape.getAnchor2D().getX(), (int) pictureShape.getAnchor2D().getY(), (int) pictureShape
				.getAnchor2D().getWidth(), (int) pictureShape.getAnchor2D().getHeight());
		// graphics.transform(AffineTransform.getScaleInstance(WIDTH / d.width, WIDTH / d.width));

		graphics.setPaint(Color.WHITE);
		graphics.fillRect((int) pictureShape.getAnchor2D().getX(), (int) pictureShape.getAnchor2D().getY(), (int) pictureShape
				.getAnchor2D().getWidth(), (int) pictureShape.getAnchor2D().getHeight());

		pictureShape.getPictureData().draw(graphics, pictureShape);
		gr.setBackground(getDiagramFactory().makeImageBackground(image));
		gr.setForeground(getDiagramFactory().makeNoneForegroundStyle());
		gr.setShadowStyle(getDiagramFactory().makeNoneShadowStyle());

		newShape.setGraphicalRepresentation(gr);

		return newShape;
	}

	private DashStyle convertDashLineStyles(int powerpointDashStyle){
		
		switch (powerpointDashStyle){
			case Line.PEN_DASH : return DashStyle.MEDIUM_DASHES;
			case Line.PEN_DASHDOT : return DashStyle.DOTS_DASHES;
			case Line.PEN_DASHDOTDOT : return DashStyle.DOT_LINES_DASHES;
			case Line.PEN_DASHDOTGEL : return DashStyle.SMALL_DASHES;
			case Line.PEN_DOT : return DashStyle.DOTS_DASHES;
			case Line.PEN_DOTGEL : return DashStyle.DOTS_DASHES;
			case Line.PEN_LONGDASHDOTDOTGEL : return DashStyle.BIG_DASHES;
			case Line.PEN_LONGDASHDOTGEL : return DashStyle.BIG_DASHES;
			case Line.PEN_LONGDASHGEL : return DashStyle.BIG_DASHES;
			case Line.PEN_PS_DASH : return DashStyle.SMALL_DASHES;
			case Line.PEN_SOLID : return DashStyle.PLAIN_STROKE;
		}
		return null;
	}
	
	private boolean isConnector(int shapeType){
		switch(shapeType){
			case ShapeTypes.CurvedConnector2:return true;
			case ShapeTypes.CurvedConnector3:return true;
			case ShapeTypes.CurvedConnector4:return true;
			case ShapeTypes.CurvedConnector5:return true;
			case ShapeTypes.Line:return true;
			case ShapeTypes.StraightConnector1:return true;
		}
		return false;
	}
	
	private void setTextProperties(ShapeGraphicalRepresentation returned, TextShape textShape) {

		// TODO Handle several text styles in a text shape
		if (textShape.getTextRun() != null) {
			TextRun textRun = textShape.getTextRun();
			RichTextRun[] rt = textRun.getRichTextRuns();

			if (rt.length > 0) {
				RichTextRun rtr = rt[0];
				String fontName = rtr.getFontName();
				int fontSize = rtr.getFontSize();
				Color color = rtr.getFontColor();
				int fontStyle = Font.PLAIN | (rtr.isBold() ? Font.BOLD : Font.PLAIN) | (rtr.isItalic() ? Font.ITALIC : Font.PLAIN);
				Font f = new Font(fontName, fontStyle, fontSize);
				TextStyle textStyle = getDiagramFactory().makeTextStyle(color, f);
				returned.setTextStyle(textStyle);
			}
		}

		returned.setIsFloatingLabel(false);
		returned.setIsMultilineAllowed(true);

		returned.setRelativeTextX(0.5);
		returned.setRelativeTextY(0.5);

		try {
			switch (textShape.getVerticalAlignment()) {
			case TextShape.AnchorTop:
				returned.setVerticalTextAlignment(VerticalTextAlignment.BOTTOM);
				break;
			case TextShape.AnchorMiddle:
				returned.setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
				break;
			case TextShape.AnchorBottom:
				returned.setVerticalTextAlignment(VerticalTextAlignment.TOP);
				break;
			case TextShape.AnchorTopCentered:
				returned.setVerticalTextAlignment(VerticalTextAlignment.BOTTOM);
				break;
			case TextShape.AnchorMiddleCentered:
				returned.setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
				break;
			case TextShape.AnchorBottomCentered:
				returned.setVerticalTextAlignment(VerticalTextAlignment.TOP);
				break;
			case TextShape.AnchorTopBaseline:
				returned.setVerticalTextAlignment(VerticalTextAlignment.BOTTOM);
				break;
			case TextShape.AnchorBottomBaseline:
				returned.setVerticalTextAlignment(VerticalTextAlignment.TOP);
				break;
			case TextShape.AnchorTopCenteredBaseline:
				returned.setVerticalTextAlignment(VerticalTextAlignment.BOTTOM);
				break;
			case TextShape.AnchorBottomCenteredBaseline:
				returned.setVerticalTextAlignment(VerticalTextAlignment.TOP);
				break;
			}
		} catch (NullPointerException e) {
			
		}

		switch (textShape.getHorizontalAlignment()) {
		case TextShape.AlignLeft:
			returned.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT);
			returned.setParagraphAlignment(ParagraphAlignment.LEFT);
			break;
		case TextShape.AlignCenter:
			returned.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
			returned.setParagraphAlignment(ParagraphAlignment.CENTER);
			break;
		case TextShape.AlignRight:
			returned.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
			returned.setParagraphAlignment(ParagraphAlignment.RIGHT);
			break;
		case TextShape.AlignJustify:
			returned.setParagraphAlignment(ParagraphAlignment.RIGHT);
			returned.setParagraphAlignment(ParagraphAlignment.JUSTIFY);
			break;
		}

		returned.setLineWrap(true);

	}

	
	// there is still an issue for connectors ends, even if we can find at least that there is specific ends(such as arrows) it seems that there is no information on the side they are applied...
	private void setConnectorType(ConnectorGraphicalRepresentation returned, SimpleShape connectorShape){
		
		switch(connectorShape.getShapeType()){
		case ShapeTypes.CurvedConnector2:
			returned.setConnectorType(ConnectorType.CURVE);
			break;
		case ShapeTypes.CurvedConnector3:
			returned.setConnectorType(ConnectorType.CURVE);
			break;
		case ShapeTypes.CurvedConnector4:
			returned.setConnectorType(ConnectorType.CURVE);
			break;
		case ShapeTypes.CurvedConnector5:
			returned.setConnectorType(ConnectorType.CURVE);
			break;
		case ShapeTypes.Line:
			returned.setConnectorType(ConnectorType.LINE);
			break;
		case ShapeTypes.StraightConnector1:
			returned.setConnectorType(ConnectorType.RECT_POLYLIN);
			break;
		}
	
		if(connectorShape.getEscherProperty(EscherProperties.LINESTYLE__LINESTARTARROWHEAD)!=0){
			returned.getConnectorSpecification().setStartSymbol(StartSymbolType.ARROW);
		}if(connectorShape.getEscherProperty(EscherProperties.LINESTYLE__LINESTARTARROWWIDTH)!=0){
			returned.getConnectorSpecification().setStartSymbolSize(connectorShape.getEscherProperty(EscherProperties.LINESTYLE__LINESTARTARROWWIDTH)*10);
		}if(connectorShape.getEscherProperty(EscherProperties.LINESTYLE__LINEENDARROWHEAD)!=0){
			returned.getConnectorSpecification().setEndSymbol(EndSymbolType.ARROW);
		}if(connectorShape.getEscherProperty(EscherProperties.LINESTYLE__LINEENDARROWWIDTH)!=0){
			returned.getConnectorSpecification().setEndSymbolSize(connectorShape.getEscherProperty(EscherProperties.LINESTYLE__LINEENDARROWWIDTH)*10);
		}
	}
}
