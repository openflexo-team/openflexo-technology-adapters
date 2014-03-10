package org.openflexo.technologyadapter.diagram.metamodel;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.OpenflexoTestCase;
import org.openflexo.foundation.TestFlexoServiceManager;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.rm.DiagramPaletteResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramPaletteResourceImpl;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationRepository;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResourceImpl;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.toolbox.FileResource;

/**
 * Test DiagramSpecification resource 
 * @author vincent
 * 
 */
@RunWith(OrderedRunner.class)
public class TestDiagramSpecificationResource extends OpenflexoTestCase{

	private final String diagramFileName = "myDiagramSpecification";
	private final String resourcesFolder = "src/test/resources";
	private final String diagramSpecificationURI = "http://myDiagramSpecification";
	private final String paletteName = "myDiagramSpecificationPalette";
	
	/**
	 * Test Create diagram specification resource
	 */
	@Test
	@TestOrder(1)
	public void testCreateDiagramSpecificationResource() {

		try {
			TestFlexoServiceManager applicationContext = new TestFlexoServiceManager(new FileResource(
					new File(resourcesFolder).getAbsolutePath()));
			DiagramTechnologyAdapter technologicalAdapter = applicationContext.getTechnologyAdapterService().getTechnologyAdapter(
					DiagramTechnologyAdapter.class);
	
			FlexoResourceCenter<?> resourceCenter = applicationContext.getResourceCenterService().getResourceCenters().get(0);
		
			DiagramSpecificationRepository repository = resourceCenter.getRepository(DiagramSpecificationRepository.class, technologicalAdapter);
			File diagramRep = new File(repository.getDirectory() + "/"+diagramFileName);
			File diagramFile = new File(diagramRep+ "/"+diagramFileName+DiagramSpecificationResource.DIAGRAM_SPECIFICATION_SUFFIX);
			DiagramSpecificationResource resource = DiagramSpecificationResourceImpl.makeDiagramSpecificationResource(diagramSpecificationURI, diagramRep, 
					diagramFile, applicationContext);
			
			resource.save(null);
			assertTrue(resource.getFile().exists());
			
		} catch (SaveResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Test Load diagram specification resource
	 */
	@Test
	@TestOrder(2)
	public void testLoadDiagramSpecificationResource() {

		TestFlexoServiceManager applicationContext = new TestFlexoServiceManager(new FileResource(
				new File(resourcesFolder).getAbsolutePath()));
	
		DiagramSpecificationResource resource = DiagramSpecificationResourceImpl.retrieveDiagramSpecificationResource(new File(resourcesFolder+"/"+diagramFileName), applicationContext);
			
		assertNotNull(resource);
		
	}
	
	/**
	 * Test Create Palette resource
	 */
	@Test
	@TestOrder(3)
	public void testCreatePaletteResource() {

		try {
			TestFlexoServiceManager applicationContext = new TestFlexoServiceManager(new FileResource(
					new File(resourcesFolder).getAbsolutePath()));
			DiagramSpecificationResource resource = DiagramSpecificationResourceImpl.retrieveDiagramSpecificationResource(new File(resourcesFolder+"/"+diagramFileName), applicationContext);
			DiagramPaletteResource palette = DiagramPaletteResourceImpl.makeDiagramPaletteResource(resource, paletteName, applicationContext);
			
			palette.save(null);
			assertTrue(palette.getFile().exists());
			resource.save(null);
			assertTrue(resource.getDiagramPaletteResources().contains(palette));
		} catch (SaveResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
