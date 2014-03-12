package org.openflexo.technologyadapter.diagram.metamodel;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.OpenflexoTestCase;
import org.openflexo.foundation.TestFlexoServiceManager;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.foundation.viewpoint.ViewPoint.ViewPointImpl;
import org.openflexo.foundation.viewpoint.VirtualModel.VirtualModelImpl;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.FreeDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
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
	
	public static DiagramSpecificationResource diagramSpecificationResource;
	public static DiagramPaletteResource paletteResource;
	public static FlexoResourceCenter<?> resourceCenter;
	public static DiagramTechnologyAdapter technologicalAdapter;
	public static FlexoServiceManager applicationContext;
	public static DiagramSpecificationRepository repository;
	public static TypedDiagramModelSlot typedDiagramModelSlot;
	public static VirtualModel newVirtualModel;
	public static DiagramPaletteElement diagramPaletteElement;
	
	/**
	 * Initialize
	 */
	@Test
	@TestOrder(1)
	public void testInitialize() {

		log("testInitialize()");

		applicationContext = instanciateTestServiceManager();
		
		//applicationContext = new TestFlexoServiceManager(new FileResource(new File(resourcesFolder).getAbsolutePath()));
		technologicalAdapter = applicationContext.getTechnologyAdapterService().getTechnologyAdapter(
				DiagramTechnologyAdapter.class);
		resourceCenter = applicationContext.getResourceCenterService().getResourceCenters().get(0);
		repository = resourceCenter.getRepository(DiagramSpecificationRepository.class, technologicalAdapter);
		
		assertNotNull(applicationContext);
		assertNotNull(technologicalAdapter);
		assertNotNull(resourceCenter);
		assertNotNull(repository);
	}

	
	
	/**
	 * Test Create diagram specification resource
	 */
	@Test
	@TestOrder(2)
	public void testCreateDiagramSpecificationResource() {
		
		log("testCreateDiagramSpecificationResource()");

		try {
			File diagramRep = new File(repository.getDirectory() + "/"+diagramFileName);
			File diagramFile = new File(diagramRep+ "/"+diagramFileName+DiagramSpecificationResource.DIAGRAM_SPECIFICATION_SUFFIX);
			diagramSpecificationResource = DiagramSpecificationResourceImpl.makeDiagramSpecificationResource(diagramSpecificationURI, diagramRep, 
					diagramFile, applicationContext);
			
			diagramSpecificationResource.save(null);
			assertTrue(diagramSpecificationResource.getFile().exists());
			
		} catch (SaveResourceException e) {
			fail(e.getMessage());
		}
		
	}
	
	/**
	 * Test Load diagram specification resource
	 */
	@Test
	@TestOrder(3)
	public void testLoadDiagramSpecificationResource() {
		
		log("testLoadDiagramSpecificationResource()");
		
		DiagramSpecificationResource resource = DiagramSpecificationResourceImpl.retrieveDiagramSpecificationResource(new File(repository.getDirectory()+"/"+diagramFileName), applicationContext);
		assertNotNull(resource);
	}
	
	/**
	 * Test Create Palette resource
	 */
	@Test
	@TestOrder(4)
	public void testUpdateDiagramSpecificationResourceWithPaletteResource() {
		
		log("testUpdateDiagramSpecificationResourceWithPaletteResource()");
		
		try {
			paletteResource = DiagramPaletteResourceImpl.makeDiagramPaletteResource(diagramSpecificationResource, paletteName, applicationContext);
			paletteResource.save(null);
			assertTrue(paletteResource.getFile().exists());
			diagramSpecificationResource.save(null);
			assertTrue(diagramSpecificationResource.getDiagramPaletteResources().contains(paletteResource));
		} catch (SaveResourceException e) {
			fail(e.getMessage());
		}
		
	}
	
	/**
	 * Test update diagram specification resource with diagram resource
	 */
	@Test
	@TestOrder(5)
	public void testUpdateDiagramSpecificationResourceWithDiagramResource() {
		try {
			// Create an example diagram
			DiagramFactory factory = new DiagramFactory();
			Diagram diagram = factory.newInstance(Diagram.class);
			DiagramShape shape1 = factory.makeNewShape("Shape1a", ShapeType.RECTANGLE, new FGEPoint(100, 100), diagram);
			shape1.getGraphicalRepresentation().setForeground(factory.makeForegroundStyle(Color.RED));
			shape1.getGraphicalRepresentation().setBackground(factory.makeColoredBackground(Color.BLUE));
			DiagramShape shape2 = factory.makeNewShape("Shape2a", ShapeType.RECTANGLE, new FGEPoint(200, 100), diagram);
			shape2.getGraphicalRepresentation().setForeground(factory.makeForegroundStyle(Color.BLUE));
			shape2.getGraphicalRepresentation().setBackground(factory.makeColoredBackground(Color.WHITE));
			DiagramConnector connector1 = factory.makeNewConnector("Connector", shape1, shape2, diagram);
			diagramPaletteElement = paletteResource.getFactory().makeDiagramPaletteElement();
			paletteResource.getDiagramPalette().addToElements(diagramPaletteElement);
			
			// Add the palette and the example diagram
			diagramSpecificationResource.getDiagramSpecification().addToExampleDiagrams(diagram);
			diagramSpecificationResource.getDiagramSpecification().addToPalettes(paletteResource.getDiagramPalette());
			diagramSpecificationResource.save(null);
			paletteResource.save(null);
			
		} catch (SaveResourceException e) {
			fail(e.getMessage());
		} catch (ModelDefinitionException e) {
			fail(e.getMessage());
		}
		
	}
	
	/**
	 * Test update diagram specification resource with diagram resource
	 */
	@Test
	@TestOrder(6)
	public void testModelSlots() {
		try {
			ViewPoint newViewPoint = ViewPointImpl.newViewPoint("TestViewPoint", "http://openflexo.org/test/TestViewPoint",
					repository.getDirectory(), serviceManager.getViewPointLibrary());
			
			
			newVirtualModel = VirtualModelImpl.newVirtualModel("TestVirtualModel", newViewPoint);
		
			typedDiagramModelSlot = technologicalAdapter.makeModelSlot(TypedDiagramModelSlot.class, newVirtualModel);
			assertNotNull(typedDiagramModelSlot);
			
			FreeDiagramModelSlot freeDiagramModelSlot = technologicalAdapter.makeModelSlot(FreeDiagramModelSlot.class, newVirtualModel);
			assertNotNull(freeDiagramModelSlot);
			
			newVirtualModel.addToModelSlots(typedDiagramModelSlot);
			newVirtualModel.addToModelSlots(freeDiagramModelSlot);
			
			assertTrue(newVirtualModel.getModelSlots(TypedDiagramModelSlot.class)!=null);
			assertTrue(newVirtualModel.getModelSlots(FreeDiagramModelSlot.class)!=null);
			
		} catch (SaveResourceException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test update diagram specification resource with diagram resource
	 */
	@Test
	@TestOrder(7)
	public void testPaletteElementBindings() {
		
		FMLDiagramPaletteElementBinding newBinding =  newVirtualModel.getFactory().newInstance(FMLDiagramPaletteElementBinding.class);
		FlexoConcept flexoConcept = newVirtualModel.getFactory().newInstance(FlexoConcept.class);
		DropScheme newDropScheme = newVirtualModel.getFactory().newInstance(DropScheme.class);
		flexoConcept.addToFlexoBehaviours(newDropScheme);
		newBinding.setPaletteElement(diagramPaletteElement);
		newBinding.setFlexoConcept(flexoConcept);
		newBinding.setDropScheme(newDropScheme);
		typedDiagramModelSlot.addToPaletteElementBindings(newBinding);
		newVirtualModel.addToModelSlots(typedDiagramModelSlot);
		assertNotNull(newBinding);
		
	}
	
	
	
}
