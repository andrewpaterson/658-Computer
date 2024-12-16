package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitInstance;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.path.ViewPath;

public class SubcircuitInstanceCreation
{
  protected SubcircuitInstanceView subcircuitInstanceView;
  protected ViewPath path;
  protected CircuitSimulation circuitSimulation;
  protected SubcircuitSimulation containingSubcircuitSimulation;
  protected SubcircuitInstance subcircuitInstance;

  public SubcircuitInstanceCreation(SubcircuitInstanceView subcircuitInstanceView,
                                    ViewPath path,
                                    CircuitSimulation circuitSimulation,
                                    SubcircuitSimulation containingSubcircuitSimulation,
                                    SubcircuitInstance subcircuitInstance)
  {

    this.subcircuitInstanceView = subcircuitInstanceView;
    this.path = path;
    this.circuitSimulation = circuitSimulation;
    this.containingSubcircuitSimulation = containingSubcircuitSimulation;
    this.subcircuitInstance = subcircuitInstance;
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

  public ViewPath getViewPath()
  {
    return path;
  }

  public CircuitSimulation getCircuitSimulation()
  {
    return circuitSimulation;
  }
}

