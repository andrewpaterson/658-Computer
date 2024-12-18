package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitInstance;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.path.ViewPath;

public class SubcircuitInstanceCreation
{
  protected SubcircuitInstanceView subcircuitInstanceView;
  protected ViewPath viewPath;
  protected CircuitSimulation circuitSimulation;
  protected SubcircuitSimulation containingSubcircuitSimulation;
  protected SubcircuitInstance subcircuitInstance;

  public SubcircuitInstanceCreation(SubcircuitInstanceView subcircuitInstanceView,
                                    ViewPath viewPath,
                                    CircuitSimulation circuitSimulation,
                                    SubcircuitSimulation containingSubcircuitSimulation,
                                    SubcircuitInstance subcircuitInstance)
  {

    this.subcircuitInstanceView = subcircuitInstanceView;
    this.viewPath = viewPath;
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
    return viewPath;
  }

  public CircuitSimulation getCircuitSimulation()
  {
    return circuitSimulation;
  }
}

