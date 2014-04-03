package org.openflexo.technologyadapter.diagram.fml.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.poi.hslf.model.AutoShape;
import org.apache.poi.hslf.model.Line;
import org.apache.poi.hslf.model.MasterSheet;
import org.apache.poi.hslf.model.Picture;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.ShapeTypes;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.Table;
import org.apache.poi.hslf.model.TableCell;
import org.apache.poi.hslf.model.TextBox;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.fge.ShapeGraphicalRepresentation.DimensionConstraints;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.FileSystemResourceLocatorImpl;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
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
public class TestActions extends OpenflexoProjectAtRunTimeTestCase{

	private static final Logger logger = FlexoLogger.getLogger(TestActions.class.getPackage().getName());

	private static FlexoServiceManager testApplicationContext;
	private static DiagramTechnologyAdapter technologicalAdapter;
	private static Resource resourceCenterDirectory;
	private static FlexoEditor editor;
	private static FlexoProject project;

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
		testApplicationContext = instanciateTestServiceManager();
		assertNotNull(testApplicationContext);

		resourceCenterDirectory = ResourceLocator.locateResource("src/test/resources");
		assertTrue(resourceCenterDirectory != null);
		editor = createProject("TestProject");
		project = editor.getProject();
		assertTrue(project.getProjectDirectory().exists());
		assertTrue(project.getProjectDataResource().getFile().exists());
	}

	/**
	 * Test the Diagram from a PPT slide
	 */
	@Test
	@TestOrder(2)
	public void testCreateExampleDiagramFrommPPTSlide() {

		CreateDiagramFromPPTSlide createExampleDiagramFromPPTSlide = CreateDiagramFromPPTSlide.actionType.makeNewAction(project.getRootFolder(), null,editor);

		for(Resource rsc : resourceCenterDirectory.getContents(Pattern.compile(".*[.]ppt"))){ 
			File pptFile = ((FileResourceImpl) rsc).getFile();
			logger.info("Testing file "+pptFile.getName());
			createExampleDiagramFromPPTSlide.setFile(pptFile);
			for(Slide slide : createExampleDiagramFromPPTSlide.getCurrentSlides()){
				logger.info("Testing Slide number "+slide.getSlideNumber() +" in file named "+pptFile.getName());
				createExampleDiagramFromPPTSlide.setDiagramName("diagramName");
				createExampleDiagramFromPPTSlide.setDiagramTitle("diagramTitle");
				createExampleDiagramFromPPTSlide.setSlide(slide);
				createExampleDiagramFromPPTSlide.doAction();

				DiagramContainerElement diag = (DiagramContainerElement)createExampleDiagramFromPPTSlide.getNewDiagram();
				assertNotNull(diag);
				
				
				int expectedNumberOfShapesAndConnectors = computedNumberOfShapesAndConnectors(slide);
				int realNumberOfShapesAndConnectors = diag.getShapes().size() + diag.getConnectors().size();
				logger.info("Testing file "+pptFile.getName() + " slide number " + slide.getSlideNumber() + 
						" expecting " + expectedNumberOfShapesAndConnectors +
						" in reality " + realNumberOfShapesAndConnectors);
				assertEquals(expectedNumberOfShapesAndConnectors,realNumberOfShapesAndConnectors);
			}
		}
	}

	private int computedNumberOfShapesAndConnectors(Slide slide){
		int numberOfShapes=0;
		int numberOfConnectors=0;
		int numberOfShapesAndConnectors=0;
		
		if (slide.getFollowMasterObjects()) {
			Shape[] sh = slide.getMasterSheet().getShapes();
			for (int i = sh.length - 1; i >= 0; i--) {
				if (MasterSheet.isPlaceholder(sh[i])) {
					continue;
				} else if (sh[i] instanceof Table) {
					numberOfShapes++;
					numberOfShapes = numberOfShapes+(((Table)sh[i]).getNumberOfColumns()*((Table)sh[i]).getNumberOfRows());
				} else if (sh[i] instanceof TextBox || sh[i] instanceof Picture) {
					numberOfShapes++;
				} else if (sh[i] instanceof Line){
					numberOfShapes= numberOfShapes+2;
					numberOfConnectors++;
				} else if (sh[i] instanceof AutoShape){
					if(!isConnector(sh[i].getShapeType())){
						numberOfShapes++;
					} else{
						numberOfConnectors++;
					}
				}
			}
		}
		for (Shape shape : slide.getShapes()) {
			if (shape instanceof Table) {
				numberOfShapes++;
				numberOfShapes = numberOfShapes+(((Table)shape).getNumberOfColumns()*((Table)shape).getNumberOfRows());
			}else if (shape instanceof TextBox || shape instanceof Picture) {
				numberOfShapes++;
			}else if (shape instanceof Line){
				numberOfShapes= numberOfShapes+2;
				numberOfConnectors++;
			}else if (shape instanceof AutoShape){
				if(!isConnector(shape.getShapeType())){
					numberOfShapes++;
				} else{
					numberOfConnectors++;
				}
			}
		}
		
		numberOfShapesAndConnectors = numberOfConnectors+numberOfShapes;
	
		return numberOfShapesAndConnectors;
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
}
