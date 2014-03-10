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
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.apache.poi.hslf.model.AutoShape;
import org.apache.poi.hslf.model.MasterSheet;
import org.apache.poi.hslf.model.Picture;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextBox;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.model.TextShape;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.TextStyle;
import org.openflexo.fge.ForegroundStyle.DashStyle;
import org.openflexo.fge.GraphicalRepresentation.HorizontalTextAlignment;
import org.openflexo.fge.GraphicalRepresentation.ParagraphAlignment;
import org.openflexo.fge.GraphicalRepresentation.VerticalTextAlignment;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.resource.InvalidFileNameException;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.viewpoint.ViewPointObject;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramResourceImpl;
import org.openflexo.toolbox.JavaUtils;
import org.openflexo.toolbox.StringUtils;

public class CreateDiagramFromPPTSlide extends FlexoAction<CreateDiagramFromPPTSlide, RepositoryFolder, ViewPointObject> {

	private static final Logger logger = Logger.getLogger(CreateDiagramFromPPTSlide.class.getPackage().getName());

	public static FlexoActionType<CreateDiagramFromPPTSlide, RepositoryFolder, ViewPointObject> actionType = new FlexoActionType<CreateDiagramFromPPTSlide, RepositoryFolder, ViewPointObject>(
			"create_diagram_from_ppt_slide", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateDiagramFromPPTSlide makeNewAction(RepositoryFolder focusedObject, Vector<ViewPointObject> globalSelection,
				FlexoEditor editor) {
			return new CreateDiagramFromPPTSlide(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(RepositoryFolder object, Vector<ViewPointObject> globalSelection) {
			return object != null;
		}

		@Override
		public boolean isEnabledForSelection(RepositoryFolder object, Vector<ViewPointObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateDiagramFromPPTSlide.actionType, RepositoryFolder.class);
	}

	public DrawingGraphicalRepresentation graphicalRepresentation;
	private DiagramSpecification diagramSpecification;
	private String diagramName;
	private String diagramTitle;
	private String diagramURI;
	private DiagramResource diagramResource;
	private File diagramFile;
	
	private SlideShow selectedSlideShow;
	private ArrayList<Slide> currentSlides;
	private File file;
	private Slide slide;
	
	
	CreateDiagramFromPPTSlide(RepositoryFolder focusedObject, Vector<ViewPointObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws NotImplementedException, InvalidParameterException, SaveResourceException,
			InvalidFileNameException {
		logger.info("Add example diagram");

		diagramResource = DiagramResourceImpl.makeDiagramResource(getDiagramName(), getDiagramURI(), getDiagramFile(),
				getDiagramSpecification(), getServiceManager());

		getFocusedObject().addToResources(diagramResource);

		diagramResource.save(null);
		
		convertSlideToDiagram(slide);
	}

	
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
		
		else{
			errorMessage ="";
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

	public DiagramSpecification getDiagramSpecification() {
		return diagramSpecification;
	}

	public void setDiagramSpecification(DiagramSpecification diagramSpecification) {
		this.diagramSpecification = diagramSpecification;
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
		if (diagramURI == null) {
			return getDefaultDiagramURI();
		}
		return diagramURI;
	}

	public void setDiagramURI(String diagramURI) {
		this.diagramURI = diagramURI;
	}

	public String getDefaultDiagramURI() {
		return getFocusedObject().getResourceRepository().generateURI(getDiagramName());
	}

	public File getDiagramFile() {
		if (diagramFile == null) {
			return getDefaultDiagramFile();
		}
		return diagramFile;
	}

	public void setDiagramFile(File diagramFile) {
		this.diagramFile = diagramFile;
	}

	public File getDefaultDiagramFile() {
		return new File(getFocusedObject().getFile(), getDiagramName() + DiagramResource.DIAGRAM_SUFFIX);
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
			if(currentSlides==null){
				currentSlides = new ArrayList<Slide>();
			}
			else{
				currentSlides.clear();
			}
			for(Slide slide : selectedSlideShow.getSlides()){
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
		if(file!=null){
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
		Dimension d = s.getSlideShow().getPageSize();
		BufferedImage i = new BufferedImage((int) WIDTH, (int) (WIDTH * d.height / d.width), BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = i.createGraphics();
		graphics.transform(AffineTransform.getScaleInstance(WIDTH / d.width, WIDTH / d.width));
		s.draw(graphics);
		return new ImageIcon(i);
	}
	
	
	/*
	 * Transfo PPT to Diagram
	 */
	private Diagram convertSlideToDiagram(Slide slide){
		
		Diagram diagram = getNewDiagram();
		
		MasterSheet master = slide.getMasterSheet();

		if (slide.getFollowMasterObjects()) {
			if(master.getShapes()!=null){
				Shape[] sh = master.getShapes();
				for (int i = sh.length - 1; i >= 0; i--) {
					if (MasterSheet.isPlaceholder(sh[i])) {
						continue;
					}
					Shape shape = sh[i];
					if (shape instanceof Picture) {
						diagram.addToShapes(makePictureShape((Picture)shape));
					} else if (shape instanceof AutoShape) {
						diagram.addToShapes(makeAutoShape((AutoShape)shape));
					} else if (shape instanceof TextBox) {
						diagram.addToShapes(makeTextBox((TextBox)shape));
					}
				}
			}
		}

		for (Shape shape : slide.getShapes()) {
			if (shape instanceof Picture) {
				diagram.addToShapes(makePictureShape((Picture)shape));
			} else if (shape instanceof AutoShape) {
				diagram.addToShapes(makeAutoShape((AutoShape)shape));
			} else if (shape instanceof TextBox) {
				diagram.addToShapes(makeTextBox((TextBox)shape));
			} 
		}
		return diagram;
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
					DashStyle.values()[autoShape.getLineDashing()]));
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
		//gr.setBorder(getDiagramFactory().makeShapeBorder(0, 0, 0, 0));

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
			logger.warning("Unexpected POI exception while retrieving vertical alignment");
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

}