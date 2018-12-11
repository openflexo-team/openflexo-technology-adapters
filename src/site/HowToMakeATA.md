% A tutorial for building technology adapters
% Fabien Dagnat

# Conceptual view

A *Technology Adapter*, named TA after, is a library providing
abstractions to give the model federation core access to an external
source of data.

A TA provides the definition of:

  1) the abstractions representing the various elements of the
external data, called here after, the *TA concepts*

  2) the *actions* to manipulates these TA concepts instances from model
  federation behaviors

  3) a set of *model slot types* defining the way virtual model
  instances will be connected to the data

# Implementation view

A TA is composed of a set of Java classes. It serves several
objectives and therefore is structured as a set of Java packages. It
is generally defined using Pamela.

Let us suppose the TA is for the technology space XX. You should
structure it around the following packages:

1) `xx` contains

    1) The declaring class `XxTechnologyAdapter` defining the TA. It
    must extends the `TechnologyAdapter` class with the generic
    parameter `XxTechnologyAdapter`. It must declare model slots types
    and some resource factories. A model slot type is declared using the
    annotation `@@DeclareModelSlots({Yy1XxModelSlot.class,Yy2XxModelSlot.class})`
    while resource factories are declared by
    `@@DeclareResourceTypes({XxZzResourceFactory.class})`. Being a TA requires to define:
    
       * the name of the TA, method `String getName()`{.java}
       * an identifier for the FML language, method `String getIdentifier()`{.java}
       * the path to the localization resources (a.k.a dictionnaries) , method
      `String getLocalizationDirectory()`{.java}, it is often `"FlexoLocalization/XxTechnologyAdapter"`
       * specify which resource should ignored [(Sylvain, what for?)]{.todo}, method
       `<I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents)`,
       it often returns `false`
       * specify the *binding factory* for the specific types of the TA, this factory
       is a singleton often defined as a private final static attribute of the TA,
       method `TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory()`
    
    2) The `XxTechnologyContextManager` extending `TechnologyContextManager<XxTechnologyAdapter>` to manage a context related to a technology. It stores the known resources of this technology. [(Sylvain, not sure it is really common? Most `XxTechnologyContextManager` seems to do nothing)]{.todo} Pas le cas le plus courant

    2) The various model slots, `YyXxModelSlot` interfaces

