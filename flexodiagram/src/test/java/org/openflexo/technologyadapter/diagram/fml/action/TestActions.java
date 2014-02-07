package org.openflexo.technologyadapter.diagram.fml.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hslf.model.MasterSheet;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.junit.Test;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.TestFlexoServiceManager;
import org.openflexo.foundation.OpenflexoTestCase.FlexoTestEditor;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.toolbox.FileResource;

/**
 * Test actions
 * 
 * @author vincent
 * 
 */
public class TestActions {

	private static final Logger logger = FlexoLogger.getLogger(TestActions.class.getPackage().getName());
	
	public static SlideShow loadSlideShow(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			SlideShow slideShow = new SlideShow(fis);
			return slideShow;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	
	/**
	 * Test the Diagram from a PPT slide
	 */
	@Test
	public void testCreateExampleDiagramFrommPPTSlide() {

		File resourceCenterDirectory = new FileResource(
				new File("src/test/resources").getAbsolutePath());
		FlexoServiceManager applicationContext = new TestFlexoServiceManager(resourceCenterDirectory);
		DiagramTechnologyAdapter technologicalAdapter = applicationContext.getTechnologyAdapterService().getTechnologyAdapter(
				DiagramTechnologyAdapter.class);
		
		FlexoTestEditor editor = new FlexoTestEditor(null,applicationContext);
		
		DiagramSpecification diagramSpecification = DiagramSpecification.newDiagramSpecification("http://testDiagram", "testDiagram", resourceCenterDirectory, applicationContext);
			
		CreateExampleDiagramFromPPTSlide createExampleDiagramFromPPTSlide = CreateExampleDiagramFromPPTSlide.actionType.makeNewAction(diagramSpecification, null,editor);
		
		for(int i=0;i<resourceCenterDirectory.list().length;i++){ 
			if(resourceCenterDirectory.list()[i].endsWith(".ppt")==true){ 
				File pptFile = new FileResource(resourceCenterDirectory.list()[i]);
				SlideShow slideShow = loadSlideShow(pptFile);
				logger.info("Testing file "+pptFile.getName());
				for(int j=0;j<slideShow.getSlides().length;j++){
					logger.info("Testing Slide number "+j +" in file named "+pptFile.getName());
					createExampleDiagramFromPPTSlide.newDiagramName = "diagramName";
					createExampleDiagramFromPPTSlide.newDiagramTitle = "diagramTitle";
					createExampleDiagramFromPPTSlide.setSelectedSlide(slideShow.getSlides()[i]);
					createExampleDiagramFromPPTSlide.doAction();
					
					assertNotNull(createExampleDiagramFromPPTSlide.getNewDiagram());
					
					assertEquals(computedNumberOfShapes(createExampleDiagramFromPPTSlide.getSelectedSlide()),
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
