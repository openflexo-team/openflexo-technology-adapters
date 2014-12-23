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
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.hslf.model.AutoShape;
import org.apache.poi.hslf.model.Freeform;
import org.apache.poi.hslf.model.Line;
import org.apache.poi.hslf.model.MasterSheet;
import org.apache.poi.hslf.model.Picture;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.ShapeGroup;
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
import org.openflexo.fge.BackgroundImageBackgroundStyle;
import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.ForegroundStyle.DashStyle;
import org.openflexo.fge.GraphicalRepresentation.HorizontalTextAlignment;
import org.openflexo.fge.GraphicalRepresentation.ParagraphAlignment;
import org.openflexo.fge.GraphicalRepresentation.VerticalTextAlignment;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation.DimensionConstraints;
import org.openflexo.fge.TextStyle;
import org.openflexo.fge.connectors.ConnectorSpecification.ConnectorType;
import org.openflexo.fge.connectors.ConnectorSymbol.EndSymbolType;
import org.openflexo.fge.connectors.ConnectorSymbol.StartSymbolType;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.Polygon;
import org.openflexo.fge.shapes.Rectangle;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.swing.ImageUtils;
import org.openflexo.swing.ImageUtils.ImageType;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.CreateDiagram;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.toolbox.JavaUtils;
import org.openflexo.toolbox.StringUtils;

public abstract class AbstractCreateDiagramFromPPTSlide<A extends AbstractCreateDiagramFromPPTSlide<A, T>, T extends FlexoObject> extends
		FlexoAction<A, T, FMLObject> {

	private static final Logger logger = Logger.getLogger(CreateDiagram.class.getPackage().getName());

	public DrawingGraphicalRepresentation graphicalRepresentation;
	private String diagramName;
	private String diagramTitle;
	private String diagramURI;
	private DiagramResource diagramResource;
	private File diagramFile;
	private Diagram diagram;

	private SlideShow selectedSlideShow;
	private ArrayList<Slide> currentSlides;
	private File file;
	private Slide slide;

	public AbstractCreateDiagramFromPPTSlide(FlexoActionType<A, T, FMLObject> actionType, T focusedObject,
			Vector<FMLObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	public DrawingGraphicalRepresentation getGraphicalRepresentation() {
		return graphicalRepresentation;
	}

	public void setGraphicalRepresentation(DrawingGraphicalRepresentation graphicalRepresentation) {
		this.graphicalRepresentation = graphicalRepresentation;
	}

	public DiagramResource getDiagramResource() {
		return diagramResource;
	}

	public void setDiagramResource(DiagramResource diagramResource) {
		if ((diagramResource == null && this.diagramResource != null)
				|| (diagramResource != null && !diagramResource.equals(this.diagramResource))) {

			DiagramResource oldValue = this.diagramResource;
			this.diagramResource = diagramResource;
			getPropertyChangeSupport().firePropertyChange("diagramResource", oldValue, diagramResource);
			Diagram diag = getDiagram();
			getPropertyChangeSupport().firePropertyChange("diagram", null, diag);
			if (diag != null)
				getPropertyChangeSupport().firePropertyChange("diagramTitle", null, diag.getTitle());
			else
				getPropertyChangeSupport().firePropertyChange("diagramTitle", null, null);
			getPropertyChangeSupport().firePropertyChange("isValid", null, isValid());
			getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
		}
	}

	public Diagram getDiagram() {
		if (getDiagramResource() != null) {
			setDiagram(getDiagramResource().getDiagram());
		}
		return diagram;
	}

	public void setDiagram(Diagram diagram) {

		if ((diagram == null && this.diagram != null) || (diagram != null && !diagram.equals(this.diagram))) {

			Diagram oldValue = this.diagram;
			this.diagram = diagram;
			getPropertyChangeSupport().firePropertyChange("diagram", oldValue, getDiagram());
			if (diagram != null)
				getPropertyChangeSupport().firePropertyChange("diagramTitle", null, diagram.getTitle());
			else
				getPropertyChangeSupport().firePropertyChange("diagramTitle", null, null);

			getPropertyChangeSupport().firePropertyChange("isValid", null, isValid());
			getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
		}

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

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/*
	 * Diagram Configuration
	 */

	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public boolean isValid() {
		if (getDiagram() == null) {
			if (StringUtils.isEmpty(getDiagramName())) {
				errorMessage = noNameMessage();
				return false;
			}
			if (!getDiagramName().equals(JavaUtils.getClassName(getDiagramName()))
					&& !getDiagramName().equals(JavaUtils.getVariableName(getDiagramName()))) {
				errorMessage = invalidNameMessage();
				return false;
			}
			/*
			if (StringUtils.isEmpty(getDiagramTitle())) {
				errorMessage = noTitleMessage();
				return false;
			}
			*/
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

		System.out.println("VALID");

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

	public String getDiagramName() {
		if (getDiagram() != null) {
			return getDiagram().getName();
		}
		return diagramName;
	}

	public void setDiagramName(String diagramName) {
		boolean wasValid = isValid();
		if (getDiagram() != null) {
			getDiagram().setName(diagramName);
		}
		this.diagramName = diagramName;
		getPropertyChangeSupport().firePropertyChange("diagramName", null, diagramName);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
	}

	public String getDiagramTitle() {
		if (getDiagram() != null) {
			return getDiagram().getTitle();
		}
		return diagramTitle;
	}

	public void setDiagramTitle(String diagramTitle) {
		if (getDiagram() != null) {
			getDiagram().setTitle(diagramTitle);
		}
		this.diagramTitle = diagramTitle;
		getPropertyChangeSupport().firePropertyChange("diagramTitle", null, diagramTitle);
		getPropertyChangeSupport().firePropertyChange("isValid", null, isValid());
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
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
		return getDiagram().getDiagramFactory();
	}

	public void loadSlideShow() {
		try {
			FileInputStream fis = new FileInputStream(getFile());
			selectedSlideShow = new SlideShow(fis);
			if (currentSlides == null) {
				currentSlides = new ArrayList<Slide>();
			}
			else {
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
		getPropertyChangeSupport().firePropertyChange("file", null, file);
		getPropertyChangeSupport().firePropertyChange("currentSlides", null, getCurrentSlides());
		getPropertyChangeSupport().firePropertyChange("isValid", null, isValid());
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
		getPropertyChangeSupport().firePropertyChange("valid", null, isValid());
	}

	public Slide getSlide() {
		return slide;
	}

	public void setSlide(Slide slide) {
		this.slide = slide;
		boolean wasValid = isValid();
		getPropertyChangeSupport().firePropertyChange("slide", null, getSlide());
		getPropertyChangeSupport().firePropertyChange("isValid", null, isValid());
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
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
		return getScreenShot(s, 75);
	}

	public ImageIcon getOverview(Slide s) {
		return getScreenShot(s, 400);
	}

	public ImageIcon getScreenShot(Slide s, double size) {
		if (s != null && s.getSlideShow() != null) {
			try {
				Dimension d = s.getSlideShow().getPageSize();
				BufferedImage i = new BufferedImage((int) size, (int) (size * d.height / d.width), BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = i.createGraphics();
				graphics.transform(AffineTransform.getScaleInstance(size / d.width, size / d.width));
				s.draw(graphics);
				return new ImageIcon(i);
			} catch (ArrayIndexOutOfBoundsException e) {
				logger.warning("Some fonts are cannot be previewed (Calibri, Gothic MS)");
			} catch (Exception e) {
				logger.warning("Unable to create a preview for the slide " + s.getSlideNumber());
			}
		}
		return null;
	}

	public File saveImageFile(BufferedImage image, String name) {
		File imageFile = new File(getDiagramFile().getParent(), JavaUtils.getClassName(name) + ".diagram-element" + ".png");
		try {
			ImageUtils.saveImageToFile(image, imageFile, ImageType.PNG);
			return imageFile;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private HashMap<DiagramShape, Shape> shapesMap;
	private List<Shape> poiShapes;

	/*
	 * Transfo PPT to Diagram
	 */
	public void convertSlideToDiagram(Slide slide) {
		MasterSheet master = slide.getMasterSheet();
		shapesMap = new HashMap<DiagramShape, Shape>();
		poiShapes = new ArrayList<Shape>();

		// Retrieve all transformable elements
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

		// Transform shapes
		for (Shape shape : poiShapes) {
			transformPowerpointShape(getDiagram(), shape);
		}
		// Transform connectors
		for (Shape shape : poiShapes) {
			transformPowerpointConnector(getDiagram(), shape);
		}
	}

	private DiagramConnector transformPowerpointConnector(DiagramContainerElement<?> container, Shape shape) {
		DiagramConnector diagramConnector = null;
		if (shape instanceof AutoShape) {
			// In the case the connector is a shape, the shape type give the kind of connector, no source/target
			// are available, we can only make an intersection with the already know shapes
			if (isConnector(shape.getShapeType())) {
				diagramConnector = makeConnector((AutoShape) shape, shapesMap);
				// If we are not able to find source/target shapes, create a line
				if (diagramConnector == null) {
					makeLine((AutoShape) shape);
				}
			}
		}
		else if (shape instanceof Line) {
			diagramConnector = makeLine((Line) shape);
		}
		if (diagramConnector != null) {
			container.addToConnectors(diagramConnector);
			container.addToShapes(diagramConnector.getStartShape());
			container.addToShapes(diagramConnector.getEndShape());
			return diagramConnector;
		}
		return null;
	}

	private DiagramShape transformPowerpointShape(DiagramContainerElement<?> container, Shape shape) {
		DiagramShape diagramShape = null;
		if (shape instanceof Freeform) {
			diagramShape = makeFreeformShape((Freeform) shape);
		}
		else if (shape instanceof Picture) {
			diagramShape = makePictureShape((Picture) shape);
		}
		else if ((shape instanceof AutoShape) && (!isConnector(shape.getShapeType()))) {
			diagramShape = makeAutoShape((AutoShape) shape);
		}
		else if (shape instanceof Table) {
			diagramShape = makeTable((Table) shape);
		}
		else if (shape instanceof ShapeGroup) {
			diagramShape = makeGroupShape((ShapeGroup) shape);
		}
		else if (shape instanceof TextBox) {
			diagramShape = makeTextBox((TextBox) shape);
		}
		if (diagramShape != null) {
			shapesMap.put(diagramShape, shape);
			container.addToShapes(diagramShape);
			return diagramShape;
		}
		return null;
	}

	// To optimize the group shape convertion, if there is a textbox and a shape, then the shape is the container of the text
	// This should cover a large set of cases
	private DiagramShape makeGroupShape(ShapeGroup shapeGroup) {
		// If we find a shape and a text, then it could be optimized as a shape containing this text
		boolean isShapeAndText = false;
		if (shapeGroup.getShapes().length == 2) {
			TextShape textShape = null;
			TextShape containerShape = null;
			for (Shape shape : shapeGroup.getShapes()) {
				if ((shape instanceof TextShape) && (((TextShape) shape).getTextRun() != null)) {
					textShape = (TextShape) shape;
				}
				if ((shape instanceof TextShape) && (((TextShape) shape).getTextRun() == null)) {
					containerShape = (TextShape) shape;
				}
			}
			if (containerShape != null && textShape != null) {
				isShapeAndText = true;
				DiagramShape newShape = transformPowerpointShape(getDiagram(), containerShape);
				setTextProperties(newShape, newShape.getGraphicalRepresentation(), textShape, true);
				newShape.setName(textShape.getText());
			}
		}
		if (!isShapeAndText) {
			// Transform shapes
			for (Shape shape : shapeGroup.getShapes()) {
				transformPowerpointShape(getDiagram(), shape);
			}
			// Transform connectors
			for (Shape shape : shapeGroup.getShapes()) {
				transformPowerpointConnector(getDiagram(), shape);
			}
		}

		return null;
	}

	private DiagramShape makeTable(Table table) {

		DiagramShape newTable = getDiagramFactory().makeNewShape(getShapeName(table), getDiagram());
		ShapeGraphicalRepresentation gr = newTable.getGraphicalRepresentation();
		setDefaultGraphicalProperties(gr, table);
		gr.getForeground().setNoStroke(true);
		gr.setTransparency(1);
		for (int col = 0; col < table.getNumberOfColumns(); col++) {
			for (int row = 0; row < table.getNumberOfRows(); row++) {
				TableCell cell = table.getCell(row, col);
				if (cell != null) {
					DiagramShape newCell = makeTextBox(cell);
					newCell.getGraphicalRepresentation().getForeground().setNoStroke(false);
					newCell.getGraphicalRepresentation().setDimensionConstraints(DimensionConstraints.UNRESIZABLE);
					getDiagram().addToShapes(newCell);
				}
			}
		}

		newTable.setGraphicalRepresentation(gr);
		return newTable;
	}

	private DiagramShape makeFreeformShape(Freeform freeformShape) {
		DiagramShape newShape = getDiagramFactory().makeNewShape(getShapeName(freeformShape), getDiagram());
		ShapeGraphicalRepresentation gr = newShape.getGraphicalRepresentation();
		gr.setShapeType(ShapeType.CUSTOM_POLYGON);
		Polygon ss = ((Polygon) gr.getShapeSpecification());

		PathIterator pi = freeformShape.getPath().getPathIterator(null);
		double[] coordinates = new double[6];
		while (pi.isDone() == false) {
			int type = pi.currentSegment(coordinates);
			switch (type) {
				case PathIterator.SEG_MOVETO:
					ss.addToPoints(new FGEPoint(coordinates[0], coordinates[1]));
					ss.getPoints().add(new FGEPoint(coordinates[0], coordinates[1]));
					break;
				case PathIterator.SEG_LINETO:
					ss.addToPoints(new FGEPoint(coordinates[0], coordinates[1]));
					ss.getPoints().add(new FGEPoint(coordinates[0], coordinates[1]));
					break;
				case PathIterator.SEG_QUADTO:
					ss.addToPoints(new FGEPoint(coordinates[0], coordinates[1]));
					ss.getPoints().add(new FGEPoint(coordinates[0], coordinates[1]));
					break;
				case PathIterator.SEG_CUBICTO:
					ss.addToPoints(new FGEPoint(coordinates[0], coordinates[1]));
					ss.getPoints().add(new FGEPoint(coordinates[0], coordinates[1]));
					break;
				case PathIterator.SEG_CLOSE:
					ss.addToPoints(new FGEPoint(coordinates[0], coordinates[1]));
					ss.getPoints().add(new FGEPoint(coordinates[0], coordinates[1]));
					break;
				default:
					break;
			}
			pi.next();
		}

		gr.setShapeSpecification(ss);
		setDefaultGraphicalProperties(gr, freeformShape);
		setTextProperties(newShape, gr, freeformShape, true);
		newShape.setGraphicalRepresentation(gr);
		return newShape;
	}

	private DiagramShape makeAutoShape(AutoShape autoShape) {
		DiagramShape newShape = getDiagramFactory().makeNewShape(getShapeName(autoShape), getDiagram());
		ShapeGraphicalRepresentation gr = newShape.getGraphicalRepresentation();
		setDiagramShapeShapeType(gr, autoShape);
		setDefaultGraphicalProperties(gr, autoShape);
		setTextProperties(newShape, gr, autoShape, true);
		newShape.setGraphicalRepresentation(gr);
		return newShape;
	}

	private String getShapeName(Shape shape) {
		if (shape instanceof TextShape && ((TextShape) shape).getText() != null) {
			return ((TextShape) shape).getText();
		} /*else if (shape instanceof Picture && ((Picture) shape).getPictureName() != null) {
			return ((Picture) shape).getPictureName();
			}*/
		else {
			return "";/* if (shape.getShapeName() != null) {
						return shape.getShapeName();
						} else {
						return "shape_" + shape.getShapeId();
						}*/
		}
	}

	/**
	 * A line is without start/end shapes
	 * 
	 * @param line
	 * @return
	 */
	private DiagramConnector makeLine(Line line) {
		// Create a virtual start/end shape.
		DiagramShape sourceShape = getDiagramFactory().makeNewShape("", getDiagram());
		DiagramShape targetShape = getDiagramFactory().makeNewShape("", getDiagram());
		ShapeGraphicalRepresentation sourceShapeGR = sourceShape.getGraphicalRepresentation();
		sourceShapeGR.setX(line.getLogicalAnchor2D().getMinX());
		sourceShapeGR.setY(line.getLogicalAnchor2D().getMaxY());
		sourceShapeGR.setWidth(2);
		sourceShapeGR.setHeight(2);
		sourceShapeGR.setDimensionConstraints(DimensionConstraints.UNRESIZABLE);
		ShapeGraphicalRepresentation targetShapeGR = targetShape.getGraphicalRepresentation();
		targetShapeGR.setX(line.getLogicalAnchor2D().getMaxX());
		targetShapeGR.setY(line.getLogicalAnchor2D().getMinY());
		targetShapeGR.setWidth(2);
		targetShapeGR.setHeight(2);
		targetShapeGR.setDimensionConstraints(DimensionConstraints.UNRESIZABLE);

		DiagramConnector newConnector = getDiagramFactory().makeNewConnector(getShapeName(line), sourceShape, targetShape, getDiagram());
		ConnectorGraphicalRepresentation gr = newConnector.getGraphicalRepresentation();
		setConnectorType(gr, line);
		newConnector.setGraphicalRepresentation(gr);
		return newConnector;
	}

	/**
	 * A line is without start/end shapes
	 * 
	 * @param line
	 * @return
	 */
	private DiagramConnector makeLine(AutoShape poiConnector) {
		// Create a virtual start/end shape.
		DiagramShape sourceShape = getDiagramFactory().makeNewShape("", getDiagram());
		DiagramShape targetShape = getDiagramFactory().makeNewShape("", getDiagram());
		ShapeGraphicalRepresentation sourceShapeGR = sourceShape.getGraphicalRepresentation();
		sourceShapeGR.setX(poiConnector.getLogicalAnchor2D().getMinX());
		sourceShapeGR.setY(poiConnector.getLogicalAnchor2D().getMaxY());
		sourceShapeGR.setWidth(2);
		sourceShapeGR.setHeight(2);
		sourceShapeGR.setDimensionConstraints(DimensionConstraints.UNRESIZABLE);
		ShapeGraphicalRepresentation targetShapeGR = targetShape.getGraphicalRepresentation();
		targetShapeGR.setX(poiConnector.getLogicalAnchor2D().getMaxX());
		targetShapeGR.setY(poiConnector.getLogicalAnchor2D().getMinY());
		targetShapeGR.setWidth(2);
		targetShapeGR.setHeight(2);
		targetShapeGR.setDimensionConstraints(DimensionConstraints.UNRESIZABLE);

		DiagramConnector newConnector = getDiagramFactory().makeNewConnector(getShapeName(poiConnector), sourceShape, targetShape,
				getDiagram());
		ConnectorGraphicalRepresentation gr = newConnector.getGraphicalRepresentation();
		setConnectorType(gr, poiConnector);
		newConnector.setGraphicalRepresentation(gr);
		return newConnector;
	}

	private DiagramConnector makeConnector(AutoShape poiConnector, HashMap<DiagramShape, Shape> possibleShapes) {
		DiagramShape sourceShape = null, targetShape = null;

		for (DiagramShape diagramShape : possibleShapes.keySet()) {
			Shape poiShape = possibleShapes.get(diagramShape);

			// Connectors which are closed to the shape are considered as linked to this shape.
			Rectangle2D r1 = new Rectangle2D.Double(poiConnector.getAnchor2D().getX(), poiConnector.getAnchor2D().getY(), poiConnector
					.getAnchor2D().getWidth() + 5, poiConnector.getAnchor2D().getHeight() + 5);

			if (poiShape.getAnchor().intersects(r1)) {
				if (sourceShape == null && targetShape == null) {
					sourceShape = diagramShape;
				}
				else if (sourceShape != null && targetShape == null) {
					targetShape = diagramShape;
				}
			}
		}
		if (sourceShape != null && targetShape == null) {
			targetShape = sourceShape;
		}
		else if (targetShape != null && sourceShape == null) {
			sourceShape = targetShape;
		}
		if (sourceShape != null && targetShape != null) {
			DiagramConnector newConnector = getDiagramFactory().makeNewConnector(getShapeName(poiConnector), sourceShape, targetShape,
					getDiagram());
			ConnectorGraphicalRepresentation gr = newConnector.getGraphicalRepresentation();
			setConnectorType(gr, poiConnector);
			newConnector.setGraphicalRepresentation(gr);
			return newConnector;
		}
		else {
			return null;
		}
	}

	private DiagramShape makeTextBox(TextBox textBox) {
		DiagramShape newShape = getDiagramFactory().makeNewShape(getShapeName(textBox), getDiagram());
		ShapeGraphicalRepresentation gr = newShape.getGraphicalRepresentation();
		setDiagramShapeShapeType(gr, textBox);
		setDefaultGraphicalProperties(gr, textBox);
		setTextProperties(newShape, gr, textBox, true);
		gr.setLayer(2);
		newShape.setGraphicalRepresentation(gr);
		return newShape;
	}

	private DiagramShape makePictureShape(Picture pictureShape) {

		int width = (int) pictureShape.getLogicalAnchor2D().getWidth() < 0 ? 0 : (int) pictureShape.getLogicalAnchor2D().getWidth();
		int height = (int) pictureShape.getLogicalAnchor2D().getHeight() < 0 ? 0 : (int) pictureShape.getLogicalAnchor2D().getHeight();
		int x = (int) pictureShape.getLogicalAnchor2D().getX() < 0 ? 0 : (int) pictureShape.getLogicalAnchor2D().getX();
		int y = (int) pictureShape.getLogicalAnchor2D().getY() < 0 ? 0 : (int) pictureShape.getLogicalAnchor2D().getY();
		if (width * height < Integer.MAX_VALUE) {
			DiagramShape newShape = getDiagramFactory().makeNewShape(getShapeName(pictureShape), getDiagram());

			ShapeGraphicalRepresentation gr = newShape.getGraphicalRepresentation();

			gr.setX(x);
			gr.setY(y);
			gr.setWidth(width);
			gr.setHeight(height);
			// gr.setBorder(getDiagramFactory().makeShapeBorder(0, 0, 0, 0));
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = image.createGraphics();
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			graphics.translate(-x, -y);
			graphics.clipRect(x, y, width, height);
			// graphics.transform(AffineTransform.getScaleInstance(WIDTH / d.width, WIDTH / d.width));

			graphics.setPaint(Color.WHITE);
			graphics.fillRect(x, y, width, height);

			pictureShape.getPictureData().draw(graphics, pictureShape);
			File imageFile = saveImageFile(image, getDiagramName() + getSlide().getTitle() + pictureShape.getShapeId());
			gr.setBackground(getDiagramFactory().makeImageBackground(ResourceLocator.locateResource(imageFile.getAbsolutePath())));
			gr.setForeground(getDiagramFactory().makeNoneForegroundStyle());
			gr.setShadowStyle(getDiagramFactory().makeNoneShadowStyle());
			((BackgroundImageBackgroundStyle) gr.getBackground()).setFitToShape(true);
			newShape.setGraphicalRepresentation(gr);

			return newShape;
		}
		return null;
	}

	private DashStyle convertDashLineStyles(int powerpointDashStyle) {

		switch (powerpointDashStyle) {
			case Line.PEN_DASH:
				return DashStyle.MEDIUM_DASHES;
			case Line.PEN_DASHDOT:
				return DashStyle.DOTS_DASHES;
			case Line.PEN_DASHDOTDOT:
				return DashStyle.DOT_LINES_DASHES;
			case Line.PEN_DASHDOTGEL:
				return DashStyle.SMALL_DASHES;
			case Line.PEN_DOT:
				return DashStyle.DOTS_DASHES;
			case Line.PEN_DOTGEL:
				return DashStyle.DOTS_DASHES;
			case Line.PEN_LONGDASHDOTDOTGEL:
				return DashStyle.BIG_DASHES;
			case Line.PEN_LONGDASHDOTGEL:
				return DashStyle.BIG_DASHES;
			case Line.PEN_LONGDASHGEL:
				return DashStyle.BIG_DASHES;
			case Line.PEN_PS_DASH:
				return DashStyle.SMALL_DASHES;
			case Line.PEN_SOLID:
				return DashStyle.PLAIN_STROKE;
		}
		return null;
	}

	private boolean isConnector(int shapeType) {
		switch (shapeType) {
			case ShapeTypes.CurvedConnector2:
				return true;
			case ShapeTypes.CurvedConnector3:
				return true;
			case ShapeTypes.CurvedConnector4:
				return true;
			case ShapeTypes.CurvedConnector5:
				return true;
			case ShapeTypes.Line:
				return true;
			case ShapeTypes.StraightConnector1:
				return true;
			case ShapeTypes.BentConnector2:
				return true;
			case ShapeTypes.BentConnector3:
				return true;
			case ShapeTypes.BentConnector4:
				return true;
			case ShapeTypes.BentConnector5:
				return true;
		}
		return false;
	}

	private void setDefaultGraphicalProperties(ShapeGraphicalRepresentation returned, Shape shape) {
		returned.setX(shape.getLogicalAnchor2D().getX());
		returned.setY(shape.getLogicalAnchor2D().getY());
		returned.setWidth(shape.getLogicalAnchor2D().getWidth());
		returned.setHeight(shape.getLogicalAnchor2D().getHeight());
		returned.setBorder(getDiagramFactory().makeShapeBorder(0, 0, 0, 0));
		returned.setShadowStyle(getDiagramFactory().makeDefaultShadowStyle());
		if (shape instanceof SimpleShape) {
			if (((SimpleShape) shape).getLineColor() != null) {
				returned.setForeground(getDiagramFactory().makeForegroundStyle(((SimpleShape) shape).getLineColor(),
						(float) ((SimpleShape) shape).getLineWidth(), convertDashLineStyles(((SimpleShape) shape).getLineDashing())));
			}
			else {
				returned.setForeground(getDiagramFactory().makeNoneForegroundStyle());
			}
			if (((SimpleShape) shape).getFillColor() != null) {
				returned.setBackground(getDiagramFactory().makeColoredBackground(((SimpleShape) shape).getFillColor()));
			}
			else {
				returned.setBackground(getDiagramFactory().makeEmptyBackground());
				returned.setShadowStyle(getDiagramFactory().makeNoneShadowStyle());
			}
		}
		else {
			returned.setForeground(getDiagramFactory().makeNoneForegroundStyle());
			returned.setBackground(getDiagramFactory().makeEmptyBackground());
			returned.setShadowStyle(getDiagramFactory().makeNoneShadowStyle());
		}
	}

	private DiagramShape makeShapeFromBullet(TextRun textRun, RichTextRun rtr, DiagramShape shape, TextShape textShape) {
		// Create Bullet
		FGEPoint bulletPosition = new FGEPoint(textRun.getTextRuler().getBulletOffsets()[0] + rtr.getIndentLevel(), textRun.getTextRuler()
				.getBulletOffsets()[1] + rtr.getLineSpacing() * 0.1 * rtr.getStartIndex());
		DiagramShape newBulletShape = getDiagramFactory().makeNewShape("", ShapeType.CIRCLE, bulletPosition, getDiagram());
		newBulletShape.getGraphicalRepresentation().setForeground(getDiagramFactory().makeNoneForegroundStyle());
		newBulletShape.getGraphicalRepresentation().setBackground(getDiagramFactory().makeColoredBackground(Color.BLACK));
		newBulletShape.getGraphicalRepresentation().setShadowStyle(getDiagramFactory().makeNoneShadowStyle());
		newBulletShape.getGraphicalRepresentation().setHeight(rtr.getBulletSize() * 0.1);
		newBulletShape.getGraphicalRepresentation().setWidth(rtr.getBulletSize() * 0.1);

		// Create Text
		DiagramShape newTextShape = getDiagramFactory().makeNewShape(rtr.getRawText(), getDiagram());
		newTextShape.getGraphicalRepresentation().setX(
				textRun.getTextRuler().getBulletOffsets()[0] + rtr.getIndentLevel() * rtr.getBulletSize());
		newTextShape.getGraphicalRepresentation().setY(
				textRun.getTextRuler().getBulletOffsets()[1] + rtr.getLineSpacing() * 0.1 * rtr.getStartIndex());
		newTextShape.getGraphicalRepresentation().setLayer(shape.getGraphicalRepresentation().getLayer() + 1);
		newTextShape.getGraphicalRepresentation().setForeground(getDiagramFactory().makeNoneForegroundStyle());
		newTextShape.getGraphicalRepresentation().setBackground(getDiagramFactory().makeEmptyBackground());
		newTextShape.getGraphicalRepresentation().setShadowStyle(getDiagramFactory().makeNoneShadowStyle());
		newTextShape.getGraphicalRepresentation().setHeight(rtr.getFontSize());
		newTextShape.getGraphicalRepresentation().setWidth(rtr.getLength());

		String fontName = rtr.getFontName();
		int fontSize = rtr.getFontSize();
		Color color = rtr.getFontColor();
		int fontStyle = Font.PLAIN | (rtr.isBold() ? Font.BOLD : Font.PLAIN) | (rtr.isItalic() ? Font.ITALIC : Font.PLAIN);
		Font f = new Font(fontName, fontStyle, fontSize);
		TextStyle textStyle = getDiagramFactory().makeTextStyle(color, f);
		newTextShape.getGraphicalRepresentation().setTextStyle(textStyle);

		getDiagram().addToShapes(newTextShape);
		getDiagram().addToShapes(newBulletShape);
		return null;
	}

	private boolean hasBullets(TextRun textRun) {
		boolean hasBullets = false;
		RichTextRun[] rts = textRun.getRichTextRuns();
		if (rts.length > 0) {
			for (int i = 0; i < rts.length; i++) {
				RichTextRun rtr = rts[i];
				if (rtr.isBullet() || rtr.isBulletHard()) {
					hasBullets = true;
				}
			}
		}
		return hasBullets;
	}

	private void setTextProperties(DiagramShape shape, ShapeGraphicalRepresentation returned, TextShape textShape,
			boolean convertBulletsToShapes) {

		if (textShape.getTextRun() != null) {
			TextRun textRun = textShape.getTextRun();
			RichTextRun[] rts = textRun.getRichTextRuns();
			if (rts.length > 0) {
				/*if (convertBulletsToShapes && hasBullets(textRun)) {
				for (int i = 0; i < rts.length; i++) {
					RichTextRun rtr = rts[i];
					makeShapeFromBullet(textRun, rtr, shape, textShape);
				}
				} else {*/
				RichTextRun rtr = rts[0];
				String fontName = rtr.getFontName();
				int fontSize = rtr.getFontSize();
				Color color = rtr.getFontColor();
				int fontStyle = Font.PLAIN | (rtr.isBold() ? Font.BOLD : Font.PLAIN) | (rtr.isItalic() ? Font.ITALIC : Font.PLAIN);
				Font f = new Font(fontName, fontStyle, fontSize);
				TextStyle textStyle = getDiagramFactory().makeTextStyle(color, f);
				returned.setTextStyle(textStyle);
				// }
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

	private void setDiagramShapeShapeType(ShapeGraphicalRepresentation gr, Shape shape) {

		try {
			switch (shape.getShapeType()) {
				case ShapeTypes.Chevron:
					gr.setShapeType(ShapeType.CHEVRON);
					break;
				case ShapeTypes.Plus:
					gr.setShapeType(ShapeType.PLUS);
					break;
				case ShapeTypes.Rectangle:
					gr.setShapeType(ShapeType.RECTANGLE);
					break;
				case ShapeTypes.RoundRectangle:
					gr.setShapeType(ShapeType.RECTANGLE);
					((Rectangle) gr.getShapeSpecification()).setIsRounded(true);
					((Rectangle) gr.getShapeSpecification()).setArcSize(20);
					break;
				case ShapeTypes.Star:
					gr.setShapeType(ShapeType.STAR);
					break;
				case ShapeTypes.Ellipse:
					gr.setShapeType(ShapeType.OVAL);
					break;
				case ShapeTypes.IsocelesTriangle:
					gr.setShapeType(ShapeType.TRIANGLE);
					break;
			}
		} catch (NullPointerException e) {

		}
	}

	// there is still an issue for connectors ends, even if we can find at least that there is specific ends(such as arrows) it seems that
	// there is no information on the side they are applied...
	private void setConnectorType(ConnectorGraphicalRepresentation returned, SimpleShape connectorShape) {

		switch (connectorShape.getShapeType()) {
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
				returned.setConnectorType(ConnectorType.LINE);
				break;
			case ShapeTypes.BentConnector2:
				returned.setConnectorType(ConnectorType.RECT_POLYLIN);
				break;
			case ShapeTypes.BentConnector3:
				returned.setConnectorType(ConnectorType.RECT_POLYLIN);
				break;
			case ShapeTypes.BentConnector4:
				returned.setConnectorType(ConnectorType.RECT_POLYLIN);
				break;
			case ShapeTypes.BentConnector5:
				returned.setConnectorType(ConnectorType.RECT_POLYLIN);
				break;
		}

		// For now it we don't know how to determinate to whom end of the connector is affected the symbol
		// Because in the case of connectors, we don't knwo the end/start shape, which is computed by intersection.
		// So we can only handle correctly two cases, when there is no arrows ends at all and when there is two arrows ends
		if (connectorShape.getEscherProperty(EscherProperties.LINESTYLE__LINESTARTARROWHEAD) != 0
				&& connectorShape.getEscherProperty(EscherProperties.LINESTYLE__LINEENDARROWHEAD) != 0) {
			if (connectorShape.getEscherProperty(EscherProperties.LINESTYLE__LINESTARTARROWHEAD) != 0) {
				returned.getConnectorSpecification().setStartSymbol(StartSymbolType.ARROW);
			}
			if (connectorShape.getEscherProperty(EscherProperties.LINESTYLE__LINESTARTARROWWIDTH) != 0) {
				returned.getConnectorSpecification().setStartSymbolSize(
						connectorShape.getEscherProperty(EscherProperties.LINESTYLE__LINESTARTARROWWIDTH) * 10);
			}
			if (connectorShape.getEscherProperty(EscherProperties.LINESTYLE__LINEENDARROWHEAD) != 0) {
				returned.getConnectorSpecification().setEndSymbol(EndSymbolType.ARROW);
			}
			if (connectorShape.getEscherProperty(EscherProperties.LINESTYLE__LINEENDARROWWIDTH) != 0) {
				returned.getConnectorSpecification().setEndSymbolSize(
						connectorShape.getEscherProperty(EscherProperties.LINESTYLE__LINEENDARROWWIDTH) * 10);
			}
		}
	}
}
