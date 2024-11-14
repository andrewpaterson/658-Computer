package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.domain.passive.subcircuit.SubcircuitInstance;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.path.CircuitInstanceViewPath;

public class SubcircuitInstanceCreation
{
  protected SubcircuitInstanceView subcircuitInstanceView;
  protected SubcircuitSimulation containingSubcircuitSimulation;
  protected SubcircuitInstance subcircuitInstance;
  protected CircuitInstanceViewPath path;

  public SubcircuitInstanceCreation(SubcircuitInstanceView subcircuitInstanceView,
                                    SubcircuitSimulation containingSubcircuitSimulation,
                                    SubcircuitInstance subcircuitInstance,
                                    CircuitInstanceViewPath path)
  {

    this.subcircuitInstanceView = subcircuitInstanceView;
    this.containingSubcircuitSimulation = containingSubcircuitSimulation;
    this.subcircuitInstance = subcircuitInstance;
    this.path = path;
  }

  public SubcircuitInstanceView getSubcircuitInstanceView()
  {
    return subcircuitInstanceView;
  }

  public SubcircuitSimulation getContainingSubcircuitSimulation()
  {
    return containingSubcircuitSimulation;
  }

  public SubcircuitInstance getSubcircuitInstance()
  {
    return subcircuitInstance;
  }

  public CircuitInstanceViewPath getPath()
  {
    return path;
  }
}

