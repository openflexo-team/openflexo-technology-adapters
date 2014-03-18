package org.openflexo.technologyadapter.diagram.fml.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.poi.hslf.model.MasterSheet;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.Slide;
import org.junit.Test;
import org.junit.runner.RunWith;
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
				createExampleDiagramFromPPTSlide.doAction();

				assertNotNull(createExampleDiagramFromPPTSlide.getNewDiagram());

				assertEquals(computedNumberOfShapes(slide),
						createExampleDiagramFromPPTSlide.getNewDiagram().getShapes().size());
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
