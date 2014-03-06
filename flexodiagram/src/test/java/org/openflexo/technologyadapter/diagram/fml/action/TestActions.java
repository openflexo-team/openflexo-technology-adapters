package org.openflexo.technologyadapter.diagram.fml.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.logging.Logger;

import org.apache.poi.hslf.model.MasterSheet;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.Slide;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.toolbox.ResourceLocator;

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
	private static File resourceCenterDirectory;
	private static FlexoEditor editor;
	private static FlexoProject project;
	
	/**
	 * Instantiate test resource center
	 */
	@Test
	@TestOrder(1)
	public void test0InstantiateResourceCenter() {

		log("test0InstantiateResourceCenter()");
		testApplicationContext = instanciateTestServiceManager();
		assertNotNull(testApplicationContext);
		
		resourceCenterDirectory = ResourceLocator.locateDirectory(new File("src/test/resources").getAbsolutePath());
		assertTrue(resourceCenterDirectory.exists());
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
		
		for(int i=0;i<resourceCenterDirectory.list().length;i++){ 
			if(resourceCenterDirectory.list()[i].endsWith(".ppt")==true){
				//
				// TODO Des choses à faire ici!!!!
				///
				File pptFile = ResourceLocator.locateFile(resourceCenterDirectory.list()[i]);
				logger.info("Testing file "+pptFile.getName());
				createExampleDiagramFromPPTSlide.setFile(pptFile);
				for(Slide slide : createExampleDiagramFromPPTSlide.getCurrentSlides()){
					logger.info("Testing Slide number "+slide.getSlideNumber() +" in file named "+pptFile.getName());
					createExampleDiagramFromPPTSlide.setDiagramName("diagramName");
					createExampleDiagramFromPPTSlide.setDiagramTitle("diagramTitle");
					createExampleDiagramFromPPTSlide.doAction();
					
					assertNotNull(createExampleDiagramFromPPTSlide.getNewDiagram());
					
					assertEquals(computedNumberOfShapes(slide),
							createExampleDiagramFromPPTSlide.getNewDiagram().getShapes().size());
				}
			}
		}
	}
	
	private int computedNumberOfShapes(Slide slide){
		int numberOfShapes=0;
		Shape[] sh = slide.getMasterSheet().getShapes();
		for (int i = sh.length - 1; i >= 0; i--) {
			if (MasterSheet.isPlaceholder(sh[i])) {
				continue;
			}
			numberOfShapes++;
		}
		numberOfShapes = numberOfShapes+slide.getShapes().length;
		return numberOfShapes;
	}
}
