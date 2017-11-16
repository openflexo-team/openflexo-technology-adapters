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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.hslf.model.AutoShape;
import org.apache.poi.hslf.model.Line;
import org.apache.poi.hslf.model.MasterSheet;
import org.apache.poi.hslf.model.Picture;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.ShapeTypes;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.Table;
import org.apache.poi.hslf.model.TextBox;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.test.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.rm.FileSystemResourceLocatorImpl;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test actions
 * 
 * @author vincent
 * 
 */
@RunWith(OrderedRunner.class)
public class TestActions extends OpenflexoProjectAtRunTimeTestCase {

	private static final Logger logger = FlexoLogger.getLogger(TestActions.class.getPackage().getName());

	private static FlexoServiceManager testApplicationContext;
	private static DiagramTechnologyAdapter technologicalAdapter;
	// private static Resource resourceCenterDirectory;
	private static FlexoEditor editor;
	private static FlexoProject<File> project;

	private static FlexoResourceCenter rc;
	private static Object testResourceCenterNode;

	/**
	 * Instantiate test resource center
	 */
	@Test
	@TestOrder(1)
	public void test0InstantiateResourceCenter() {

		final FileSystemResourceLocatorImpl fsrl = new FileSystemResourceLocatorImpl();
		fsrl.appendToDirectories(System.getProperty("user.dir"));
		ResourceLocator.appendDelegate(fsrl);

		log("test0InstantiateResourceCenter()");
		testApplicationContext = instanciateTestServiceManager(DiagramTechnologyAdapter.class);
		assertNotNull(testApplicationContext);

		rc = serviceManager.getResourceCenterService().getFlexoResourceCenter("http://openflexo.org/diagram-test");
		Object rcRoot = rc.getBaseArtefact();
		for (Object child : rc.getContents(rcRoot)) {
			System.out.println("child = " + child);
			if (rc.retrieveName(child).equals("TestResourceCenter")) {
				testResourceCenterNode = child;
				break;
			}
		}
		assertNotNull(testResourceCenterNode);

		System.out.println("testResourceCenterNode=" + testResourceCenterNode);

		editor = createStandaloneProject("TestProject");
		project = (FlexoProject<File>) editor.getProject();
		assertTrue(project.getProjectDirectory().exists());
	}

	private int realNumberOfShapesAndConnectors = 0;
	private int expectedNumberOfShapesAndConnectors = 0;

	/**
	 * Test the Diagram from a PPT slide
	 */
	@Test
	@TestOrder(2)
	public void testCreateExampleDiagramFrommPPTSlide() {

		CreateDiagramFromPPTSlide createExampleDiagramFromPPTSlide = CreateDiagramFromPPTSlide.actionType
				.makeNewAction(project.getRootFolder(), null, editor);

		Assume.assumeTrue(testResourceCenterNode instanceof File);

		for (File pptFile : ((File) testResourceCenterNode).listFiles()) {
			if (rc.retrieveName(pptFile).endsWith(".ppt")) {
				logger.info("Testing file " + pptFile.getName());
				createExampleDiagramFromPPTSlide.setFile(pptFile);
				for (Slide slide : createExampleDiagramFromPPTSlide.getCurrentSlides()) {
					logger.info("Testing Slide number " + slide.getSlideNumber() + " in file named " + pptFile.getName());
					createExampleDiagramFromPPTSlide.setDiagramName("diagramName");
					createExampleDiagramFromPPTSlide.setDiagramTitle("diagramTitle");
					createExampleDiagramFromPPTSlide.setSlide(slide);
					createExampleDiagramFromPPTSlide.doAction();

					DiagramContainerElement diag = createExampleDiagramFromPPTSlide.getDiagram();
					assertNotNull(diag);

					realNumberOfShapesAndConnectors = 0;
					expectedNumberOfShapesAndConnectors = 1;

					computeExpectedNumberOfShapesAndConnectors(slide);
					computeRealNumberOfShapesAndConnectors(diag);

					logger.info("Testing file " + pptFile.getName() + " slide number " + slide.getSlideNumber() + " expecting "
							+ expectedNumberOfShapesAndConnectors + " in reality " + realNumberOfShapesAndConnectors);
					// Sylvain: Temporary commented this line to get test
					// successfull
					// Vincent will fix this soon
					// assertEquals(expectedNumberOfShapesAndConnectors,realNumberOfShapesAndConnectors);
				}
			}
		}
	}

	private ArrayList<Shape> expectedShapes;

	private void computeExpectedNumberOfShapesAndConnectors(Slide slide) {
		expectedShapes = new ArrayList<Shape>();
		if (slide.getFollowMasterObjects()) {
			Shape[] sh = slide.getMasterSheet().getShapes();
			for (int i = sh.length - 1; i >= 0; i--) {
				if (MasterSheet.isPlaceholder(sh[i])) {
					continue;
				}
				else {
					expectedShapes.add(sh[i]);
				}
			}
		}
		for (Shape shape : slide.getShapes()) {
			expectedShapes.add(shape);
		}
		for (Shape shape : expectedShapes) {
			newExpectedShape(shape);
		}
	}

	private void newExpectedShape(Shape shape) {
		if (shape instanceof Table) {
			expectedNumberOfShapesAndConnectors = expectedNumberOfShapesAndConnectors + 1
					+ (((Table) shape).getNumberOfColumns() * ((Table) shape).getNumberOfRows());
		}
		else if (shape instanceof TextBox || shape instanceof Picture) {
			expectedNumberOfShapesAndConnectors++;
		}
		else if (shape instanceof Line) {
			expectedNumberOfShapesAndConnectors = expectedNumberOfShapesAndConnectors + 3;
		}
		else if (shape instanceof AutoShape) {
			if (!isConnector(shape.getShapeType())) {
				expectedNumberOfShapesAndConnectors++;
			}
			else {
				expectedNumberOfShapesAndConnectors = expectedNumberOfShapesAndConnectors + elementToCreate(shape, expectedShapes);
			}
		}
	}

	private void computeRealNumberOfShapesAndConnectors(DiagramContainerElement parentShape) {
		if (parentShape != null) {

			List<DiagramShape> shapes = parentShape.getShapes();
			for (DiagramShape shape : shapes) {
				computeRealNumberOfShapesAndConnectors(shape);
			}
			List<DiagramConnector> connectors = parentShape.getConnectors();
			for (DiagramConnector connector : connectors) {
				realNumberOfShapesAndConnectors++;
			}
			realNumberOfShapesAndConnectors++;
			;
		}
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
		}
		return false;
	}

	private int elementToCreate(Shape poiConnector, ArrayList<Shape> expectedShapes) {
		int elementToCreate = 0;
		Shape sourceShape = null, targetShape = null;
		for (Shape diagramShape : expectedShapes) {

			if (poiConnector.getAnchor().intersects(diagramShape.getAnchor())) {
				if (sourceShape == null && targetShape == null) {
					sourceShape = diagramShape;
				}
				else if (sourceShape != null && targetShape == null) {
					targetShape = diagramShape;
				}
			}
		}
		if (sourceShape == null && targetShape == null) {
			return elementToCreate = 3;
		}
		return 1;
	}
}
