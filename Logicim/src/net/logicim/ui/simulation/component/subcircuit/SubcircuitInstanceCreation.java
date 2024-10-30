package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.domain.passive.subcircuit.SubcircuitInstance;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

public class SubcircuitInstanceCreation
{
  protected SubcircuitInstanceView subcircuitInstanceView;
  protected SubcircuitSimulation containingSubcircuitSimulation;
  protected SubcircuitInstance subcircuitInstance;

  public SubcircuitInstanceCreation(SubcircuitInstanceView subcircuitInstanceView,
                                    SubcircuitSimulation containingSubcircuitSimulation,
                                    SubcircuitInstance subcircuitInstance)
  {

    this.subcircuitInstanceView = subcircuitInstanceView;
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
}

