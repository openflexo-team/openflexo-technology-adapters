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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import org.apache.poi.sl.usermodel.Background;
import org.openflexo.fge.BackgroundImageBackgroundStyle;
import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.ForegroundStyle.DashStyle;
import org.openflexo.fge.GraphicalRepresentation;
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
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.swing.ImageUtils;
import org.openflexo.swing.ImageUtils.ImageType;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.CreateDiagram;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramResourceFactory;
import org.openflexo.toolbox.JavaUtils;
import org.openflexo.toolbox.StringUtils;

public abstract class AbstractCreateDiagramFromPPTSlide<A extends AbstractCreateDiagramFromPPTSlide<A, T>, T extends FlexoObject>
		extends FlexoAction<A, T, FMLObject> {

	private static final Logger logger = Logger.getLogger(CreateDiagram.class.getPackage().getName());

	public DrawingGraphicalRepresentation graphicalRepresentation;
	private String diagramName;
	private String diagramTitle;
	private String diagramURI;
	private DiagramResource diagramResource;
	// private File diagramFile;
	private Diagram diagram;

	private SlideShow selectedSlideShow;
	private ArrayList<Slide> currentSlides;
	private File file;
	private Slide slide;

	public AbstractCreateDiagramFromPPTSlide(FlexoActionFactory<A, T, FMLObject> actionType, T focusedObject,
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
			getPropertyChangeSupport().firePropertyChange("isValid", null, isValid());
			getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
		}
	}

	public Diagram getDiagram() {
		if (getDiagramResource() != null) {
			return getDiagramResource().getDiagram();
		}
		return diagram;
	}

	public void setDiagram(Diagram diagram) {

		if ((diagram == null && this.diagram != null) || (diagram != null && !diagram.equals(this.diagram))) {

			Diagram oldValue = this.diagram;
			this.diagram = diagram;
			if (diagram != null) {
				diagramResource = (DiagramResource) diagram.getResource();
			}
			getPropertyChangeSupport().firePropertyChange("diagram", oldValue, getDiagram());
			if (diagram != null) {
				getPropertyChangeSupport().firePropertyChange("diagramTitle", null, diagram.getTitle());
			}

			getPropertyChangeSupport().firePropertyChange("isValid", null, isValid());
			getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
		}

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
		return getLocales().localizedForKey("no_diagram_type_selected");
	}

	public String noTitleMessage() {
		return getLocales().localizedForKey("no_diagram_title_defined");
	}

	public String noFileMessage() {
		return getLocales().localizedForKey("no_ppt_file_defined");
	}

	public String existingFileMessage() {
		return getLocales().localizedForKey("file_already_existing");
	}

	public String noNameMessage() {
		return getLocales().localizedForKey("no_diagram_name_defined");
	}

	public String noSlideMessage() {
		return getLocales().localizedForKey("no_slide_defined");
	}

	public String invalidNameMessage() {
		return getLocales().localizedForKey("invalid_name_for_new_diagram");
	}

	public String duplicatedNameMessage() {
		return getLocales().localizedForKey("a_diagram_with_that_name_already_exists");
	}

	public String getDiagramName() {
		if (getDiagram() != null) {
			return getDiagram().getName();
		}
		return diagramName;
	}

	public void setDiagramName(String diagramName) {

		if (diagramName == null) {
			return;
		}

		if (!diagramName.endsWith(DiagramResourceFactory.DIAGRAM_SUFFIX)) {
			diagramName = diagramName + DiagramResourceFactory.DIAGRAM_SUFFIX;
		}

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

	/*public File getDiagramFile() {
		return diagramFile;
	}
	
	public void setDiagramFile(File diagramFile) {
		this.diagramFile = diagramFile;
	}*/

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

	public <I> I saveImageFile(BufferedImage image, String name) {
		FlexoResourceCenter<I> rc = (FlexoResourceCenter<I>) getDiagramResource().getResourceCenter();
		I serializationArtefact = rc.createEntry(JavaUtils.getClassName(name) + ".diagram-element" + ".png",
				rc.getContainer((I) getDiagramResource().getIODelegate().getSerializationArtefact()));

		if (serializationArtefact instanceof File) {
			try {
				ImageUtils.saveImageToFile(image, (File) serializationArtefact, ImageType.PNG);
				return serializationArtefact;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		/*File imageFile = new File(getDiagramFile().getParent(), JavaUtils.getClassName(name) + ".diagram-element" + ".png");
		try {
			ImageUtils.saveImageToFile(image, imageFile, ImageType.PNG);
			return imageFile;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}*/
		return null;
	}

	private HashMap<DiagramShape, Shape> shapesMap;
	private HashMap<DiagramConnector, Shape> connectorsMap;
	private List<Shape> poiShapes;

	// This cannot be determinated safely
	private final boolean computeOrderOfShape = true;
	// This cannot be determinated safely
	private final boolean computeStartEndShapesOfConnectors = true;

	public HashMap<DiagramShape, Shape> getDiagramShapesMap() {
		return shapesMap;
	}

	public void setDiagramShapesMap(HashMap<DiagramShape, Shape> shapesMap) {
		this.shapesMap = shapesMap;
	}

	public HashMap<DiagramConnector, Shape> getDiagramConnectorsMap() {
		return connectorsMap;
	}

	public void setDiagramConnectorsMap(HashMap<DiagramConnector, Shape> connectorsMap) {
		this.connectorsMap = connectorsMap;
	}

	/*
	 * Transfo PPT to Diagram
	 */
	public void convertSlideToDiagram(Slide slide) {
		MasterSheet master = slide.getMasterSheet();
		shapesMap = new LinkedHashMap<DiagramShape, Shape>();
		connectorsMap = new LinkedHashMap<DiagramConnector, Shape>();
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

		// Transform POI shapes into Flexo Shapes
		for (Shape shape : poiShapes) {
			transformPPTShapeInDiagramElement(getDiagram(), shape);
		}

		if (computeOrderOfShape) {
			computeLayers();
		}

		// Attach connectors.
		if (computeStartEndShapesOfConnectors) {
			for (Map.Entry<DiagramConnector, Shape> diagramConnectorEntry : getDiagramConnectorsMap().entrySet()) {
				attachConnector(diagramConnectorEntry.getKey(), diagramConnectorEntry.getValue());
			}
		}

	}

	private void computeLayers() {
		int currentLayer = 0;// getDiagramShapesMap().size() + getDiagramConnectorsMap().size();
		for (Map.Entry<DiagramShape, Shape> diagramShapeEntry : getDiagramShapesMap().entrySet()) {
			diagramShapeEntry.getKey().getGraphicalRepresentation().setLayer(currentLayer++);
		}
		for (Map.Entry<DiagramConnector, Shape> diagramConnectorEntry : getDiagramConnectorsMap().entrySet()) {
			diagramConnectorEntry.getKey().getGraphicalRepresentation().setLayer(currentLayer++);
		}
	}

	/**
	 * Transform a POI shape in a DiagramElement
	 * 
	 * @param container
	 * @param shape
	 * @return
	 */
	private DiagramElement<?> transformPPTShapeInDiagramElement(DiagramContainerElement<?> container, Shape shape) {
		DiagramElement<?> diagramElement = null;
		if (shape instanceof SimpleShape) {
			diagramElement = transformSimpleShape((SimpleShape) shape);

		}
		else if (shape instanceof Background) {
			diagramElement = transformBackground((Background) shape);
		}
		else if (shape instanceof ShapeGroup) {
			diagramElement = transformShapeGroup((ShapeGroup) shape);
		}
		return diagramElement;
	}

	private DiagramElement<?> transformSimpleShape(SimpleShape shape) {
		DiagramElement<?> diagramElement = null;
		if (shape instanceof Line) {
			diagramElement = makeDiagramConnector(shape);
		}
		else if (shape instanceof Picture) {
			diagramElement = transformPicture((Picture) shape);
		}
		else if (shape instanceof TextShape) {
			diagramElement = transformTextShape((TextShape) shape);
		}
		return diagramElement;
	}

	private DiagramElement<?> transformPicture(Picture shape) {
		DiagramElement<?> diagramElement = makePictureShape(shape);
		return diagramElement;
	}

	private DiagramElement<?> transformTextShape(TextShape shape) {
		DiagramElement<?> diagramElement = null;
		if (shape instanceof AutoShape) {
			diagramElement = transformAutoShape((AutoShape) shape);
		}
		else if (shape instanceof TextBox) {
			diagramElement = makeTextBox((TextBox) shape);
		}
		return diagramElement;
	}

	private DiagramElement<?> transformAutoShape(AutoShape shape) {
		DiagramElement<?> diagramElement = null;
		if (isConnector(shape.getShapeType())) {
			diagramElement = makeDiagramConnector(shape);
			applySimpleShapeGr(diagramElement.getGraphicalRepresentation(), shape);
		}
		else {
			diagramElement = makeDiagramShape(shape);
			applyTextShapeGr((ShapeGraphicalRepresentation) diagramElement.getGraphicalRepresentation(), shape);
		}
		return diagramElement;
	}

	private DiagramElement<?> transformBackground(Background shape) {
		DiagramElement<?> diagramElement = null;
		return diagramElement;
	}

	private DiagramElement<?> transformShapeGroup(ShapeGroup shape) {
		DiagramElement<?> diagramElement = null;
		if (shape instanceof Table) {
			diagramElement = makeTable((Table) shape);
		}
		else
			diagramElement = makeGroupShape(shape);
		return diagramElement;
	}

	private DiagramShape makeTextBox(TextBox textBox) {
		DiagramShape newShape = makeDiagramShape(textBox);
		applyTextShapeGr(newShape.getGraphicalRepresentation(), textBox);
		newShape.getGraphicalRepresentation().setLayer(2);
		return newShape;
	}

	// To optimize the group shape convertion, if there is a textbox and a shape, then the shape is the container of the text
	// This should cover a large set of cases
	private DiagramElement<?> makeGroupShape(ShapeGroup shapeGroup) {
		// If we find a shape without text and a text, then it could be optimized as a shape containing this text
		boolean isShapeAndText = false;
		if (shapeGroup.getShapes().length == 2) {
			TextShape textShape = null;
			SimpleShape containerShape = null;
			for (Shape shape : shapeGroup.getShapes()) {
				if ((shape instanceof TextShape) && (((TextShape) shape).getTextRun() != null)) {
					textShape = (TextShape) shape;
				}
				else if ((shape instanceof TextShape) && (((TextShape) shape).getTextRun() == null)) {
					containerShape = (TextShape) shape;
				}
				else if ((shape instanceof SimpleShape)) {
					containerShape = (SimpleShape) shape;
				}
			}
			if (containerShape != null && textShape != null) {
				DiagramElement<?> shape = null;
				shape = transformTextShape(textShape);
				if (isConnector(containerShape.getShapeType())) {
					shape = makeDiagramConnector(containerShape);
					applySimpleShapeGr(shape.getGraphicalRepresentation(), containerShape);
				}
				else {
					if (containerShape instanceof Picture) {
						applyPictureGr((ShapeGraphicalRepresentation) shape.getGraphicalRepresentation(), (Picture) containerShape);
					}
					else {
						applySimpleShapeGr(shape.getGraphicalRepresentation(), containerShape);
					}
				}

				return shape;
			}
		}
		if (!isShapeAndText) {
			// Transform shapes
			for (Shape shape : shapeGroup.getShapes()) {
				transformPPTShapeInDiagramElement(getDiagram(), shape);
			}
		}
		return null;
	}

	private DiagramShape makeTable(Table table) {

		DiagramShape shape = makeDiagramShape(table);
		applyTableGr(shape.getGraphicalRepresentation(), table);
		for (int col = 0; col < table.getNumberOfColumns(); col++) {
			for (int row = 0; row < table.getNumberOfRows(); row++) {
				TableCell cell = table.getCell(row, col);
				if (cell != null) {
					DiagramShape newCell = makeTextBox(cell);
					applyTableCellGr(newCell.getGraphicalRepresentation(), cell);
					getDiagram().addToShapes(newCell);
				}
			}
		}
		return shape;
	}

	private DiagramShape makePictureShape(Picture pictureShape) {
		DiagramShape shape = makeDiagramShape(pictureShape);
		applyPictureGr(shape.getGraphicalRepresentation(), pictureShape);
		return shape;
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
		return newShape;
	}

	private DiagramShape makeDiagramShape(Shape shape) {
		DiagramShape newShape = getDiagramFactory().makeNewShape(getShapeName(shape), getDiagram());
		shapesMap.put(newShape, shape);
		return newShape;
	}

	/**
	 * A line is without start/end shapes
	 * 
	 * @param line
	 * @return
	 */
	private DiagramConnector makeDiagramConnector(SimpleShape line) {
		DiagramConnector newConnector = getDiagramFactory().makeNewConnector(getShapeName(line), null, null, getDiagram());
		connectorsMap.put(newConnector, line);
		return newConnector;
	}

	private String getShapeName(Shape shape) {
		if (shape != null && shape instanceof TextShape && ((TextShape) shape).getText() != null) {
			return ((TextShape) shape).getText();
		}
		else {
			return "";
		}
	}

	private void attachConnector(DiagramConnector connector, Shape poiConnector) {
		DiagramShape sourceShape = null, targetShape = null;
		List<DiagramShape> potentialShapes = new ArrayList<DiagramShape>();

		// We must count the size of the arrow head
		double arrowStartWidth = poiConnector.getEscherProperty(EscherProperties.LINESTYLE__LINESTARTARROWWIDTH) + 1;
		double arrowEndWidth = poiConnector.getEscherProperty(EscherProperties.LINESTYLE__LINEENDARROWWIDTH) + 1 + arrowStartWidth;
		// Connectors which are closed to the shape are considered as linked to this shape.
		Rectangle2D connectorBorder = null;
		if (poiConnector instanceof Line) {
			connectorBorder = new Rectangle2D.Double(poiConnector.getOutline().getBounds().getX() - arrowStartWidth,
					poiConnector.getOutline().getBounds().getY() - arrowStartWidth,
					poiConnector.getOutline().getBounds().getWidth() + arrowEndWidth,
					poiConnector.getOutline().getBounds().getHeight() + arrowEndWidth);
		}
		else {
			connectorBorder = new Rectangle2D.Double(poiConnector.getAnchor2D().getX() - arrowStartWidth,
					poiConnector.getAnchor2D().getY() - arrowStartWidth, poiConnector.getAnchor2D().getWidth() + arrowEndWidth,
					poiConnector.getAnchor2D().getHeight() + arrowEndWidth);
		}

		// Find the closest source and target shape
		for (Map.Entry<DiagramShape, Shape> diagramShapeEntry : getDiagramShapesMap().entrySet()) {
			Shape poiShape = diagramShapeEntry.getValue();
			DiagramShape diagramShape = diagramShapeEntry.getKey();
			if (poiShape.getOutline().intersects(connectorBorder)) {
				potentialShapes.add(diagramShape);
			}
		}

		// Now the problem is what shape to connect? Because of overlapping shapes choose the most frontal ones.
		int layer = 0;
		for (DiagramShape potentialShape : potentialShapes) {
			if (layer <= potentialShape.getGraphicalRepresentation().getLayer()) {
				layer = potentialShape.getGraphicalRepresentation().getLayer();
				sourceShape = potentialShape;
			}
		}
		if (potentialShapes.size() > 1) {
			potentialShapes.remove(sourceShape);
			layer = 0;
			for (DiagramShape potentialShape : potentialShapes) {
				if (layer < potentialShape.getGraphicalRepresentation().getLayer()) {
					layer = potentialShape.getGraphicalRepresentation().getLayer();
					targetShape = potentialShape;
				}
			}
		}
		else if (potentialShapes.size() == 1) {
			targetShape = sourceShape;
		}

		// Create fictives end/start shapes
		else if (sourceShape == null && targetShape == null) {
			sourceShape = getDiagramFactory().makeNewShape("", getDiagram());
			targetShape = getDiagramFactory().makeNewShape("", getDiagram());
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
		}

		connector.setStartShape(sourceShape);
		connector.setEndShape(targetShape);
	}

	private void setDiagramElementType(GraphicalRepresentation gr, Shape shape) {
		switch (shape.getShapeType()) {
			case ShapeTypes.Chevron:
				((ShapeGraphicalRepresentation) gr).setShapeType(ShapeType.CHEVRON);
				break;
			case ShapeTypes.Plus:
				((ShapeGraphicalRepresentation) gr).setShapeType(ShapeType.PLUS);
				break;
			case ShapeTypes.Rectangle:
				((ShapeGraphicalRepresentation) gr).setShapeType(ShapeType.RECTANGLE);
				break;
			case ShapeTypes.RoundRectangle:
				((ShapeGraphicalRepresentation) gr).setShapeType(ShapeType.RECTANGLE);
				((Rectangle) ((ShapeGraphicalRepresentation) gr).getShapeSpecification()).setIsRounded(true);
				((Rectangle) ((ShapeGraphicalRepresentation) gr).getShapeSpecification()).setArcSize(20);
				break;
			case ShapeTypes.Star:
				((ShapeGraphicalRepresentation) gr).setShapeType(ShapeType.STAR);
				break;
			case ShapeTypes.Ellipse:
				((ShapeGraphicalRepresentation) gr).setShapeType(ShapeType.OVAL);
				break;
			case ShapeTypes.IsocelesTriangle:
				((ShapeGraphicalRepresentation) gr).setShapeType(ShapeType.TRIANGLE);
				break;
			case ShapeTypes.CurvedConnector2:
				((ConnectorGraphicalRepresentation) gr).setConnectorType(ConnectorType.CURVE);
				break;
			case ShapeTypes.CurvedConnector3:
				((ConnectorGraphicalRepresentation) gr).setConnectorType(ConnectorType.CURVE);
				break;
			case ShapeTypes.CurvedConnector4:
				((ConnectorGraphicalRepresentation) gr).setConnectorType(ConnectorType.CURVE);
				break;
			case ShapeTypes.CurvedConnector5:
				((ConnectorGraphicalRepresentation) gr).setConnectorType(ConnectorType.CURVE);
				break;
			case ShapeTypes.Line:
				((ConnectorGraphicalRepresentation) gr).setConnectorType(ConnectorType.LINE);
				break;
			case ShapeTypes.StraightConnector1:
				((ConnectorGraphicalRepresentation) gr).setConnectorType(ConnectorType.LINE);
				break;
			case ShapeTypes.BentConnector2:
				((ConnectorGraphicalRepresentation) gr).setConnectorType(ConnectorType.RECT_POLYLIN);
				break;
			case ShapeTypes.BentConnector3:
				((ConnectorGraphicalRepresentation) gr).setConnectorType(ConnectorType.RECT_POLYLIN);
				break;
			case ShapeTypes.BentConnector4:
				((ConnectorGraphicalRepresentation) gr).setConnectorType(ConnectorType.RECT_POLYLIN);
				break;
			case ShapeTypes.BentConnector5:
				((ConnectorGraphicalRepresentation) gr).setConnectorType(ConnectorType.RECT_POLYLIN);
				break;
		}
	}

	private void applySimpleShapeGr(GraphicalRepresentation gr, SimpleShape shape) {

		if (gr instanceof ShapeGraphicalRepresentation) {
			((ShapeGraphicalRepresentation) gr).setX(shape.getLogicalAnchor2D().getX());
			((ShapeGraphicalRepresentation) gr).setY(shape.getLogicalAnchor2D().getY());
			((ShapeGraphicalRepresentation) gr).setWidth(shape.getLogicalAnchor2D().getWidth());
			((ShapeGraphicalRepresentation) gr).setHeight(shape.getLogicalAnchor2D().getHeight());
			// ((ShapeGraphicalRepresentation) gr).setBorder(getDiagramFactory().makeShapeBorder(0, 0, 0, 0));
			((ShapeGraphicalRepresentation) gr).setShadowStyle(getDiagramFactory().makeDefaultShadowStyle());
			((ShapeGraphicalRepresentation) gr).setForeground(getDiagramFactory().makeNoneForegroundStyle());
			((ShapeGraphicalRepresentation) gr).setShadowStyle(getDiagramFactory().makeNoneShadowStyle());
			if (shape.getLineColor() != null) {
				((ShapeGraphicalRepresentation) gr).setForeground(getDiagramFactory().makeForegroundStyle(shape.getLineColor(),
						(float) shape.getLineWidth(), convertDashLineStyles(shape.getLineDashing())));
			}
			if (shape.getFillColor() != null) {
				((ShapeGraphicalRepresentation) gr).setBackground(getDiagramFactory().makeColoredBackground(shape.getFillColor()));
			}
			else {
				((ShapeGraphicalRepresentation) gr).setBackground(getDiagramFactory().makeEmptyBackground());
			}
		}
		if (gr instanceof ConnectorGraphicalRepresentation) {
			if (shape.getLineColor() != null) {
				((ConnectorGraphicalRepresentation) gr).setForeground(getDiagramFactory().makeForegroundStyle(shape.getLineColor(),
						(float) shape.getLineWidth(), convertDashLineStyles(shape.getLineDashing())));
			}
			// For now it we don't know how to determinate to whom end of the connector is affected the symbol
			// Because in the case of connectors, we don't knwo the end/start shape, which is computed by intersection.
			// So we can only handle correctly two cases, when there is no arrows ends at all and when there is two arrows ends
			if (shape.getEscherProperty(EscherProperties.LINESTYLE__LINESTARTARROWHEAD) != 0
					&& shape.getEscherProperty(EscherProperties.LINESTYLE__LINEENDARROWHEAD) != 0) {
				if (shape.getEscherProperty(EscherProperties.LINESTYLE__LINESTARTARROWHEAD) != 0) {
					((ConnectorGraphicalRepresentation) gr).getConnectorSpecification().setStartSymbol(StartSymbolType.ARROW);
				}
				if (shape.getEscherProperty(EscherProperties.LINESTYLE__LINESTARTARROWWIDTH) != 0) {
					((ConnectorGraphicalRepresentation) gr).getConnectorSpecification()
							.setStartSymbolSize(shape.getEscherProperty(EscherProperties.LINESTYLE__LINESTARTARROWWIDTH) * 10);
				}
				if (shape.getEscherProperty(EscherProperties.LINESTYLE__LINEENDARROWHEAD) != 0) {
					((ConnectorGraphicalRepresentation) gr).getConnectorSpecification().setEndSymbol(EndSymbolType.ARROW);
				}
				if (shape.getEscherProperty(EscherProperties.LINESTYLE__LINEENDARROWWIDTH) != 0) {
					((ConnectorGraphicalRepresentation) gr).getConnectorSpecification()
							.setEndSymbolSize(shape.getEscherProperty(EscherProperties.LINESTYLE__LINEENDARROWWIDTH) * 10);
				}
			}
		}
		setDiagramElementType(gr, shape);
	}

	private void applyTextShapeGr(ShapeGraphicalRepresentation gr, TextShape shape) {
		applySimpleShapeGr(gr, shape);
		if (shape.getTextRun() != null) {
			TextRun textRun = shape.getTextRun();
			RichTextRun[] rts = textRun.getRichTextRuns();
			if (rts.length > 0) {
				RichTextRun rtr = rts[0];
				String fontName = rtr.getFontName();
				int fontSize = rtr.getFontSize();
				Color color = rtr.getFontColor();
				int fontStyle = Font.PLAIN | (rtr.isBold() ? Font.BOLD : Font.PLAIN) | (rtr.isItalic() ? Font.ITALIC : Font.PLAIN);
				Font f = new Font(fontName, fontStyle, fontSize);
				TextStyle textStyle = getDiagramFactory().makeTextStyle(color, f);
				gr.setTextStyle(textStyle);
			}
		}
		if (gr instanceof ShapeGraphicalRepresentation) {
			gr.setIsFloatingLabel(false);
			gr.setRelativeTextX(0.5);
			gr.setRelativeTextY(0.5);
		}
		gr.setIsMultilineAllowed(true);
		try {
			switch (shape.getVerticalAlignment()) {
				case TextShape.AnchorTop:
					gr.setVerticalTextAlignment(VerticalTextAlignment.BOTTOM);
					break;
				case TextShape.AnchorMiddle:
					gr.setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
					break;
				case TextShape.AnchorBottom:
					gr.setVerticalTextAlignment(VerticalTextAlignment.TOP);
					break;
				case TextShape.AnchorTopCentered:
					gr.setVerticalTextAlignment(VerticalTextAlignment.BOTTOM);
					break;
				case TextShape.AnchorMiddleCentered:
					gr.setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
					break;
				case TextShape.AnchorBottomCentered:
					gr.setVerticalTextAlignment(VerticalTextAlignment.TOP);
					break;
				case TextShape.AnchorTopBaseline:
					gr.setVerticalTextAlignment(VerticalTextAlignment.BOTTOM);
					break;
				case TextShape.AnchorBottomBaseline:
					gr.setVerticalTextAlignment(VerticalTextAlignment.TOP);
					break;
				case TextShape.AnchorTopCenteredBaseline:
					gr.setVerticalTextAlignment(VerticalTextAlignment.BOTTOM);
					break;
				case TextShape.AnchorBottomCenteredBaseline:
					gr.setVerticalTextAlignment(VerticalTextAlignment.TOP);
					break;
			}
		} catch (Exception e) {
			// Set by default
			gr.setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
		}

		try {
			switch (shape.getHorizontalAlignment()) {
				case TextShape.AlignLeft:
					gr.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
					gr.setParagraphAlignment(ParagraphAlignment.LEFT);
					break;
				case TextShape.AlignCenter:
					gr.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
					gr.setParagraphAlignment(ParagraphAlignment.CENTER);
					break;
				case TextShape.AlignRight:
					gr.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT);
					gr.setParagraphAlignment(ParagraphAlignment.RIGHT);
					break;
				case TextShape.AlignJustify:
					gr.setParagraphAlignment(ParagraphAlignment.RIGHT);
					gr.setParagraphAlignment(ParagraphAlignment.JUSTIFY);
					break;
			}
		} catch (Exception e) {
			gr.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
			gr.setParagraphAlignment(ParagraphAlignment.RIGHT);
		}

		gr.setLineWrap(true);
	}

	private void applyTableGr(ShapeGraphicalRepresentation gr, Table shape) {
		gr.getForeground().setNoStroke(true);
		gr.setTransparency(1);
		gr.setBackground(getDiagramFactory().makeEmptyBackground());
	}

	private void applyTableCellGr(ShapeGraphicalRepresentation gr, TableCell shape) {
		gr.getForeground().setNoStroke(false);
		gr.setDimensionConstraints(DimensionConstraints.UNRESIZABLE);
	}

	private void applyPictureGr(ShapeGraphicalRepresentation gr, Picture shape) {
		applySimpleShapeGr(gr, shape);
		int width = (int) shape.getLogicalAnchor2D().getWidth() < 0 ? 0 : (int) shape.getLogicalAnchor2D().getWidth();
		int height = (int) shape.getLogicalAnchor2D().getHeight() < 0 ? 0 : (int) shape.getLogicalAnchor2D().getHeight();
		int x = (int) shape.getLogicalAnchor2D().getX() < 0 ? 0 : (int) shape.getLogicalAnchor2D().getX();
		int y = (int) shape.getLogicalAnchor2D().getY() < 0 ? 0 : (int) shape.getLogicalAnchor2D().getY();
		if (width * height < Integer.MAX_VALUE) {
			gr.setX(x);
			gr.setY(y);
			gr.setWidth(width);
			gr.setHeight(height);
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = image.createGraphics();
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			graphics.translate(-x, -y);
			graphics.clipRect(x, y, width, height);
			graphics.setPaint(Color.WHITE);
			graphics.fillRect(x, y, width, height);
			shape.getPictureData().draw(graphics, shape);
			File imageFile = saveImageFile(image, getDiagramName() + getSlide().getTitle() + shape.getShapeId());
			gr.setBackground(getDiagramFactory().makeImageBackground(ResourceLocator.locateResource(imageFile.getAbsolutePath())));
			gr.setForeground(getDiagramFactory().makeNoneForegroundStyle());
			gr.setShadowStyle(getDiagramFactory().makeNoneShadowStyle());
			((BackgroundImageBackgroundStyle) gr.getBackground()).setFitToShape(true);
		}
	}

	/*private void setTextProperties(DiagramElement<?> shape, TextShape textShape, boolean convertBulletsToShapes) {
		GraphicalRepresentation gr = shape.getGraphicalRepresentation();
		if (textShape.getTextRun() != null) {
			TextRun textRun = textShape.getTextRun();
			RichTextRun[] rts = textRun.getRichTextRuns();
			if (rts.length > 0) {
				RichTextRun rtr = rts[0];
				String fontName = rtr.getFontName();
				int fontSize = rtr.getFontSize();
				Color color = rtr.getFontColor();
				int fontStyle = Font.PLAIN | (rtr.isBold() ? Font.BOLD : Font.PLAIN) | (rtr.isItalic() ? Font.ITALIC : Font.PLAIN);
				Font f = new Font(fontName, fontStyle, fontSize);
				TextStyle textStyle = getDiagramFactory().makeTextStyle(color, f);
				gr.setTextStyle(textStyle);
			}
		}
		if (gr instanceof ShapeGraphicalRepresentation) {
			((ShapeGraphicalRepresentation) gr).setIsFloatingLabel(false);
			((ShapeGraphicalRepresentation) gr).setRelativeTextX(0.5);
			((ShapeGraphicalRepresentation) gr).setRelativeTextY(0.5);
		}
		gr.setIsMultilineAllowed(true);
		try {
			switch (textShape.getVerticalAlignment()) {
				case TextShape.AnchorTop:
					gr.setVerticalTextAlignment(VerticalTextAlignment.BOTTOM);
					break;
				case TextShape.AnchorMiddle:
					gr.setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
					break;
				case TextShape.AnchorBottom:
					gr.setVerticalTextAlignment(VerticalTextAlignment.TOP);
					break;
				case TextShape.AnchorTopCentered:
					gr.setVerticalTextAlignment(VerticalTextAlignment.BOTTOM);
					break;
				case TextShape.AnchorMiddleCentered:
					gr.setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
					break;
				case TextShape.AnchorBottomCentered:
					gr.setVerticalTextAlignment(VerticalTextAlignment.TOP);
					break;
				case TextShape.AnchorTopBaseline:
					gr.setVerticalTextAlignment(VerticalTextAlignment.BOTTOM);
					break;
				case TextShape.AnchorBottomBaseline:
					gr.setVerticalTextAlignment(VerticalTextAlignment.TOP);
					break;
				case TextShape.AnchorTopCenteredBaseline:
					gr.setVerticalTextAlignment(VerticalTextAlignment.BOTTOM);
					break;
				case TextShape.AnchorBottomCenteredBaseline:
					gr.setVerticalTextAlignment(VerticalTextAlignment.TOP);
					break;
			}
		} catch (Exception e) {
			// Set by default
			gr.setVerticalTextAlignment(VerticalTextAlignment.TOP);
		}
	
		try {
			switch (textShape.getHorizontalAlignment()) {
				case TextShape.AlignLeft:
					gr.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT);
					gr.setParagraphAlignment(ParagraphAlignment.LEFT);
					break;
				case TextShape.AlignCenter:
					gr.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
					gr.setParagraphAlignment(ParagraphAlignment.CENTER);
					break;
				case TextShape.AlignRight:
					gr.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
					gr.setParagraphAlignment(ParagraphAlignment.RIGHT);
					break;
				case TextShape.AlignJustify:
					gr.setParagraphAlignment(ParagraphAlignment.RIGHT);
					gr.setParagraphAlignment(ParagraphAlignment.JUSTIFY);
					break;
			}
		} catch (Exception e) {
			gr.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
			gr.setParagraphAlignment(ParagraphAlignment.RIGHT);
		}
	
		gr.setLineWrap(true);
	}*/

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

	/*private void setDefaultGraphicalProperties(GraphicalRepresentation returned, SimpleShape shape) {
	
	if (returned instanceof ShapeGraphicalRepresentation) {
	
	}
	returned.setX(shape.getLogicalAnchor2D().getX());
	returned.setY(shape.getLogicalAnchor2D().getY());
	returned.setWidth(shape.getLogicalAnchor2D().getWidth());
	returned.setHeight(shape.getLogicalAnchor2D().getHeight());
	returned.setBorder(getDiagramFactory().makeShapeBorder(0, 0, 0, 0));
	returned.setShadowStyle(getDiagramFactory().makeDefaultShadowStyle());
	if (shape instanceof SimpleShape) {
		if (shape.getLineColor() != null) {
			returned.setForeground(getDiagramFactory().makeForegroundStyle(shape.getLineColor(), (float) shape.getLineWidth(),
					convertDashLineStyles(shape.getLineDashing())));
		}
		else {
			returned.setForeground(getDiagramFactory().makeNoneForegroundStyle());
		}
		if (shape.getFillColor() != null) {
			returned.setBackground(getDiagramFactory().makeColoredBackground(shape.getFillColor()));
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
	}*/

	/*private DiagramShape makeShapeFromBullet(TextRun textRun, RichTextRun rtr, DiagramShape shape, TextShape textShape) {
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
	}*/

}
