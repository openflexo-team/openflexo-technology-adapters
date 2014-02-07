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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.security.InvalidParameterException;
import java.util.Vector;
import java.util.logging.Logger;

import org.apache.poi.hslf.model.AutoShape;
import org.apache.poi.hslf.model.MasterSheet;
import org.apache.poi.hslf.model.Picture;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextBox;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.model.TextShape;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.FGEModelFactory;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.TextStyle;
import org.openflexo.fge.ForegroundStyle.DashStyle;
import org.openflexo.fge.GraphicalRepresentation.HorizontalTextAlignment;
import org.openflexo.fge.GraphicalRepresentation.ParagraphAlignment;
import org.openflexo.fge.GraphicalRepresentation.VerticalTextAlignment;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.resource.InvalidFileNameException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.viewpoint.ViewPointObject;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramImpl;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.toolbox.StringUtils;

public class CreateExampleDiagramFromPPTSlide extends FlexoAction<CreateExampleDiagramFromPPTSlide, DiagramSpecification, ViewPointObject> {

	private static final Logger logger = Logger.getLogger(CreateExampleDiagramFromPPTSlide.class.getPackage().getName());

	public static FlexoActionType<CreateExampleDiagramFromPPTSlide, DiagramSpecification, ViewPointObject> actionType = new FlexoActionType<CreateExampleDiagramFromPPTSlide, DiagramSpecification, ViewPointObject>(
			"create_example_diagram", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateExampleDiagramFromPPTSlide makeNewAction(DiagramSpecification focusedObject, Vector<ViewPointObject> globalSelection,
				FlexoEditor editor) {
			return new CreateExampleDiagramFromPPTSlide(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramSpecification object, Vector<ViewPointObject> globalSelection) {
			return object != null;
		}

		@Override
		public boolean isEnabledForSelection(DiagramSpecification object, Vector<ViewPointObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateExampleDiagramFromPPTSlide.actionType, DiagramSpecification.class);
	}

	public String newDiagramName;
	public String newDiagramTitle;
	public String description;
	public DrawingGraphicalRepresentation graphicalRepresentation;
	private Slide selectedSlide;

	private DiagramResource newDiagramResource;

	CreateExampleDiagramFromPPTSlide(DiagramSpecification focusedObject, Vector<ViewPointObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws NotImplementedException, InvalidParameterException, SaveResourceException,
			InvalidFileNameException {
		logger.info("Add example diagram");

		String newDiagramURI = getFocusedObject().getURI() + "/" + newDiagramName;
		File newDiagramFile = new File(getFocusedObject().getResource().getDirectory(), newDiagramName + DiagramResource.DIAGRAM_SUFFIX);
		newDiagramResource = DiagramImpl.newDiagramResource(newDiagramName, newDiagramTitle, newDiagramURI, newDiagramFile,
				getFocusedObject(), getServiceManager());
		newDiagramResource.getDiagram().setDescription(description);
		newDiagramResource.save(null);
		convertSlideToDiagram(selectedSlide);
	}

	private String nameValidityMessage = EMPTY_NAME;

	private static final String NAME_IS_VALID = FlexoLocalization.localizedForKey("name_is_valid");
	private static final String DUPLICATED_NAME = FlexoLocalization.localizedForKey("this_name_is_already_used_please_choose_an_other_one");
	private static final String EMPTY_NAME = FlexoLocalization.localizedForKey("empty_name");

	public String getNameValidityMessage() {
		return nameValidityMessage;
	}

	public boolean isNameValid() {
		if (StringUtils.isEmpty(newDiagramName)) {
			nameValidityMessage = EMPTY_NAME;
			return false;
		} else if (getFocusedObject().getExampleDiagram(newDiagramName) != null) {
			nameValidityMessage = DUPLICATED_NAME;
			return false;
		} else {
			nameValidityMessage = NAME_IS_VALID;
			return true;
		}
	}

	public Diagram getNewDiagram() {
		return newDiagramResource.getDiagram();
	}

	public DiagramFactory getDiagramFactory() {
		return newDiagramResource.getDiagram().getDiagramFactory();
	}
	
	public Slide getSelectedSlide() {
		return selectedSlide;
	}

	public void setSelectedSlide(Slide selectedSlide) {
		this.selectedSlide = selectedSlide;
	}
	
	private Diagram convertSlideToDiagram(Slide slide){
		
		Diagram diagram = getNewDiagram();
		
		MasterSheet master = slide.getMasterSheet();

		if (slide.getFollowMasterObjects()) {
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

		for (Shape shape : selectedSlide.getShapes()) {
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
		
		DiagramShape newShape = getDiagramFactory().makeNewShape(autoShape.getShapeName(), getNewDiagram());
		
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
		
		DiagramShape newShape = getDiagramFactory().makeNewShape(textBox.getShapeName(), getNewDiagram());
		
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
		
		DiagramShape newShape = getDiagramFactory().makeNewShape(pictureShape.getShapeName(), getNewDiagram());
		
		ShapeGraphicalRepresentation gr = newShape.getGraphicalRepresentation();
		
		gr.setX(pictureShape.getAnchor2D().getX());
		gr.setY(pictureShape.getAnchor2D().getY());
		gr.setWidth(pictureShape.getAnchor2D().getWidth());
		gr.setHeight(pictureShape.getAnchor2D().getHeight());
		gr.setBorder(getDiagramFactory().makeShapeBorder(0, 0, 0, 0));

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
		
		newShape.setGraphicalRepresentation(gr);

		return newShape;
	}
	
	private void setTextProperties(ShapeGraphicalRepresentation returned, TextShape textShape) {

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