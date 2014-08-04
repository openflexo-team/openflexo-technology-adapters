package org.openflexo.technologyadapter.freeplane.fml.behavioural;

import org.openflexo.foundation.viewpoint.AbstractCreationScheme;
import org.openflexo.foundation.viewpoint.annotations.FIBPanel;
import org.openflexo.model.annotations.*;
import org.openflexo.technologyadapter.freeplane.fml.behavioural.FreeplaneCreationScheme.FreeplaneCreationSchemeImpl;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;

@ModelEntity
// TODO : Currently empty FIB
@FIBPanel(value = "Fib/FreeplaneCreationSchemePanel.fib")
@XMLElement
@ImplementationClass(value = FreeplaneCreationSchemeImpl.class)
public interface FreeplaneCreationScheme extends FreeplaneEditionScheme, AbstractCreationScheme {

	@PropertyIdentifier(type = IFreeplaneNode.class)
	public static final String PARENT_NODE_KEY = FreeplaneEditionScheme.PARENT_NODE;

	@Getter(value = PARENT_NODE_KEY)
	@XMLAttribute
	public IFreeplaneNode getParentNode();

	@Setter(value = PARENT_NODE_KEY)
	public void setParentNode(IFreeplaneNode node);

	public abstract class FreeplaneCreationSchemeImpl extends AbstractCreationSchemeImpl implements FreeplaneCreationScheme {

	}
}
