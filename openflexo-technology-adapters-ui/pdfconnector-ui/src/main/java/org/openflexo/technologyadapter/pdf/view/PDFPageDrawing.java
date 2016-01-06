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

package org.openflexo.technologyadapter.pdf.view;

import java.awt.Color;
import java.awt.Image;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.FGEModelFactory;
import org.openflexo.fge.FGEModelFactoryImpl;
import org.openflexo.fge.GRBinding.DrawingGRBinding;
import org.openflexo.fge.GRBinding.ShapeGRBinding;
import org.openflexo.fge.GRProvider.DrawingGRProvider;
import org.openflexo.fge.GRProvider.ShapeGRProvider;
import org.openflexo.fge.GRStructureVisitor;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.impl.DrawingImpl;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.pdf.model.ImageBox;
import org.openflexo.technologyadapter.pdf.model.PDFDocumentPage;
import org.openflexo.technologyadapter.pdf.model.TextBox;

/**
 * This is the abstraction of a drawing representing a {@link PDFDocumentPage}<br>
 * 
 * @author sylvain
 * 
 */
public class PDFPageDrawing extends DrawingImpl<PDFDocumentPage> {

	private static final Logger logger = Logger.getLogger(PDFPageDrawing.class.getPackage().getName());

	private static FGEModelFactory FACTORY;

	static {
		try {
			FACTORY = new FGEModelFactoryImpl();
		} catch (ModelDefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private DrawingGraphicalRepresentation drawingRepresentation;
	private ShapeGraphicalRepresentation renderingPageRepresentation;
	private ShapeGraphicalRepresentation textBoxRepresentation;
	// private ShapeGraphicalRepresentation debugTextBoxRepresentation;
	private ShapeGraphicalRepresentation imageBoxRepresentation;

	public PDFPageDrawing(PDFDocumentPage page) {
		super(page, FACTORY, PersistenceMode.UniqueGraphicalRepresentations);
		setEditable(false);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void init() {
		Image renderingPageImage = getModel().getRenderingImage();

		drawingRepresentation = getFactory().makeDrawingGraphicalRepresentation();
		drawingRepresentation.setWidth(renderingPageImage.getWidth(null));
		drawingRepresentation.setHeight(renderingPageImage.getHeight(null));

		renderingPageRepresentation = getFactory().makeShapeGraphicalRepresentation(ShapeType.RECTANGLE);
		renderingPageRepresentation.setBackground(getFactory().makeImageBackground(renderingPageImage));
		renderingPageRepresentation.setX(0);
		renderingPageRepresentation.setY(0);
		renderingPageRepresentation.setWidth(renderingPageImage.getWidth(null));
		renderingPageRepresentation.setHeight(renderingPageImage.getHeight(null));
		// renderingPageRepresentation.setIsSelectable(false);
		// renderingPageRepresentation.setIsFocusable(false);

		textBoxRepresentation = getFactory().makeShapeGraphicalRepresentation(ShapeType.RECTANGLE);
		// textBoxRepresentation.setForeground(getFactory().makeNoneForegroundStyle());
		textBoxRepresentation.setBackground(getFactory().makeEmptyBackground());
		textBoxRepresentation.setForeground(getFactory().makeForegroundStyle(Color.red, 0.3f));
		// textBoxRepresentation.setFocusedForeground(getFactory().makeForegroundStyle(Color.RED, 0.5f));
		textBoxRepresentation.setSelectedForeground(getFactory().makeForegroundStyle(Color.BLUE, 0.5f));
		// ColorBackgroundStyle bg = getFactory().makeColoredBackground(Color.white);
		// bg.setUseTransparency(true);
		// bg.setTransparencyLevel(0.2f);
		// textBoxRepresentation.setFocusedBackground(bg);
		textBoxRepresentation.setShadowStyle(getFactory().makeNoneShadowStyle());

		/*debugTextBoxRepresentation = getFactory().makeShapeGraphicalRepresentation(ShapeType.RECTANGLE);
		debugTextBoxRepresentation.setBackground(getFactory().makeColoredBackground(Color.blue));
		debugTextBoxRepresentation.setForeground(getFactory().makeForegroundStyle(Color.yellow, 3f));
		debugTextBoxRepresentation.setShadowStyle(getFactory().makeNoneShadowStyle());*/

		imageBoxRepresentation = getFactory().makeShapeGraphicalRepresentation(ShapeType.RECTANGLE);
		imageBoxRepresentation.setBackground(getFactory().makeEmptyBackground());
		imageBoxRepresentation.setForeground(getFactory().makeForegroundStyle(Color.green, 0.3f));
		imageBoxRepresentation.setSelectedForeground(getFactory().makeForegroundStyle(Color.BLUE, 0.5f));
		imageBoxRepresentation.setShadowStyle(getFactory().makeNoneShadowStyle());

		/*MouseDragControl moveControl = nodeRepresentation.getMouseDragControl("Move");
		nodeRepresentation.removeFromMouseDragControls(moveControl);
		nodeRepresentation.addToMouseDragControls(new MouseDragControlImpl("dragNode", MouseButton.LEFT, new MoveAction() {
		
			@Override
			public boolean handleMouseReleased(org.openflexo.fge.Drawing.DrawingTreeNode<?, ?> node,
					DianaInteractiveViewer<?, ?, ?> editor, MouseControlContext context, boolean isSignificativeDrag) {
				boolean returned = super.handleMouseReleased(node, editor, context, isSignificativeDrag);
				System.out.println("Detected mouse released");
				return returned;
			}
		}, false, false, false, false, getFactory().getEditingContext()));*/

		final DrawingGRBinding<PDFDocumentPage> drawingBinding = bindDrawing(PDFDocumentPage.class, "drawing",
				new DrawingGRProvider<PDFDocumentPage>() {
					@Override
					public DrawingGraphicalRepresentation provideGR(PDFDocumentPage drawable, FGEModelFactory factory) {
						return drawingRepresentation;
					}
				});
		final ShapeGRBinding<PDFDocumentPage> renderingPageBinding = bindShape(PDFDocumentPage.class, "renderingPage",
				new ShapeGRProvider<PDFDocumentPage>() {
					@Override
					public ShapeGraphicalRepresentation provideGR(PDFDocumentPage drawable, FGEModelFactory factory) {
						return renderingPageRepresentation;
					}
				});
		final ShapeGRBinding<TextBox> textBoxBinding = bindShape(TextBox.class, "textBox", new ShapeGRProvider<TextBox>() {
			@Override
			public ShapeGraphicalRepresentation provideGR(TextBox drawable, FGEModelFactory factory) {
				return textBoxRepresentation;
			}
		});
		/*final ShapeGRBinding<TextBox> debugTextBoxBinding = bindShape(TextBox.class, "debugTextBox", new ShapeGRProvider<TextBox>() {
			@Override
			public ShapeGraphicalRepresentation provideGR(TextBox drawable, FGEModelFactory factory) {
				return debugTextBoxRepresentation;
			}
		});*/

		final ShapeGRBinding<ImageBox> imageBoxBinding = bindShape(ImageBox.class, "imageBox", new ShapeGRProvider<ImageBox>() {
			@Override
			public ShapeGraphicalRepresentation provideGR(ImageBox drawable, FGEModelFactory factory) {
				return imageBoxRepresentation;
			}
		});

		drawingBinding.addToWalkers(new GRStructureVisitor<PDFDocumentPage>() {

			@Override
			public void visit(PDFDocumentPage page) {
				drawShape(renderingPageBinding, page);
			}
		});

		renderingPageBinding.addToWalkers(new GRStructureVisitor<PDFDocumentPage>() {
			@Override
			public void visit(PDFDocumentPage page) {
				// System.out.println("on visite la page");
				for (TextBox tb : page.getTextBoxes()) {
					// System.out.println("On dessine une text box pour " + tb);
					drawShape(textBoxBinding, tb, renderingPageBinding, page);
				}
				/*for (TextBox tb : page.getAVirer()) {
					System.out.println("On dessine une DEBUG text box pour " + tb);
					drawShape(debugTextBoxBinding, tb, renderingPageBinding, page);
				}*/
				for (ImageBox ib : page.getImageBoxes()) {
					// System.out.println("On dessine une image box pour " + ib);
					drawShape(imageBoxBinding, ib, renderingPageBinding, page);
				}
			}
		});

		textBoxBinding.setDynamicPropertyValue(ShapeGraphicalRepresentation.X, new DataBinding<Double>("drawable.x"), false);
		textBoxBinding.setDynamicPropertyValue(ShapeGraphicalRepresentation.Y, new DataBinding<Double>("drawable.y"), false);
		textBoxBinding.setDynamicPropertyValue(ShapeGraphicalRepresentation.WIDTH, new DataBinding<Double>("drawable.width"), false);
		textBoxBinding.setDynamicPropertyValue(ShapeGraphicalRepresentation.HEIGHT, new DataBinding<Double>("drawable.height"), false);
		// textBoxBinding.setDynamicPropertyValue(ShapeGraphicalRepresentation.TEXT, new DataBinding<String>("drawable.text"), false);

		// debugTextBoxBinding.setDynamicPropertyValue(ShapeGraphicalRepresentation.X, new DataBinding<Double>("drawable.x"), false);
		// debugTextBoxBinding.setDynamicPropertyValue(ShapeGraphicalRepresentation.Y, new DataBinding<Double>("drawable.y"), false);
		// debugTextBoxBinding.setDynamicPropertyValue(ShapeGraphicalRepresentation.WIDTH, new DataBinding<Double>("drawable.width"),
		// false);
		// debugTextBoxBinding.setDynamicPropertyValue(ShapeGraphicalRepresentation.HEIGHT, new DataBinding<Double>("drawable.height"),
		// false);

		imageBoxBinding.setDynamicPropertyValue(ShapeGraphicalRepresentation.X, new DataBinding<Double>("drawable.x"), false);
		imageBoxBinding.setDynamicPropertyValue(ShapeGraphicalRepresentation.Y, new DataBinding<Double>("drawable.y"), false);
		imageBoxBinding.setDynamicPropertyValue(ShapeGraphicalRepresentation.WIDTH, new DataBinding<Double>("drawable.width"), false);
		imageBoxBinding.setDynamicPropertyValue(ShapeGraphicalRepresentation.HEIGHT, new DataBinding<Double>("drawable.height"), false);

	}
}
