% A tutorial for building technology adapters
% Fabien Dagnat

# Context

Openflexo is a framework supporting model federation and free
modeling. It relies on a modeling language named FML that rely on a
notion of *concept* (`FlexoConcept` in the code). Each concept has
properties and an executable behavior specifying how to manipulate its
instances (`FlexoConceptInstance` in the code). The properties can
link together instances. Intuitively, FML provides a class and object
paradigm. The core of Openflexo is an FML interpretor. Tools build
using Openflexo consists of FML models.

Openflexo core is also built to link FML models to external source of
data named *resources*. Such resources are accessed by the FML
interpretor through *connectors* (`ModelSlot` in the code). Such a
connector is in charge of maintaining the link between the running FML
models and the resource. The implementation of connectors are offered
by libraries called *Technology Adapter* (named TA after).

The purpose of this tutorial is to give an insight of how to develop a
TA.

# Conceptual view

A TA provides the definition of:

  1) the abstractions representing the various elements of the
external data, called here after, the *TA concepts*

  2) the *actions* to manipulates these TA concepts instances from
  model federation behaviors

  3) a set of *model slot types* defining the way virtual model
  instances will be connected to the data

# Implementation view

A TA is composed of a set of Java classes. It serves several
objectives and therefore is structured as a set of Java packages. It
is generally defined using Pamela[^t].

[^t]: Pamela is java annotation based language offering services similar to beans in plain old Java.

Let us suppose the TA is for the technology space xx. You should
structure it around the following packages:

1) `xx` containing

    1) The declaring class `XxTechnologyAdapter` defining the TA. It declares the available model slots, custom types and factories.

    2) The various model slots, the `YyXxModelSlot` interfaces declaring the accessible TA concepts and the actions to manipulate them.

2) `xx.model` containing the abstractions of the underlying technology and the factory to instanciate them.

3) `xx.rm` containing the classes that implements the resource management: resource definition, serialization / deserialization, identification...

4) `xx.fml` containing the definition of the TA concepts and their actions.


## Details


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
       * specify which resource should be ignored [(Sylvain, what for?)]{.todo}, method
       `<I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents)`,
       it often returns `false`
       * specify the *binding factory* for the specific types of the TA, this factory
       is a singleton often defined as a private final static attribute of the TA,
       method `TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory()`
    
    2) The various model slots, `YyXxModelSlot` interfaces


    2) The `XxTechnologyContextManager` extending `TechnologyContextManager<XxTechnologyAdapter>` to manage a context related to a technology. It stores the known resources of this technology. [(Sylvain, not sure it is really common? Most `XxTechnologyContextManager` seems to do nothing)]{.todo} Pas le cas le plus courant

