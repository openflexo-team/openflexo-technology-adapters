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

    1) The declaring class `XxTechnologyAdapter` defining its name, 
    
    2) The `XxTechnologyContextManager` extending `TechnologyContextManager<XxTechnologyAdapter>` to manage a context related to a technology. It encapsulates the TA instance, the resource center service (offering means to access resources) and stores the known resources of this technology.

    2) The various model slots, `YyXxModelSlot` interfaces
