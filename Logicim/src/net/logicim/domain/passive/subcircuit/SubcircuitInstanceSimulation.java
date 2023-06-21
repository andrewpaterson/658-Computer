package net.logicim.domain.passive.subcircuit;

import net.logicim.data.simulation.SubcircuitInstanceSimulationData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

public class SubcircuitInstanceSimulation
    extends SubcircuitSimulation
{
  protected SubcircuitInstance subcircuitInstance;

  public SubcircuitInstanceSimulation(CircuitSimulation circuitSimulation,
                                      SubcircuitEditor subcircuitEditor,
                                      SubcircuitInstance subcircuitInstance)
  {
    super(circuitSimulation, subcircuitEditor);
    this.subcircuitInstance = subcircuitInstance;
  }

  public SubcircuitInstanceSimulation(CircuitSimulation circuitSimulation,
                                      SubcircuitEditor subcircuitEditor,
                                      long id,
                                      SubcircuitInstance subcircuitInstance)
  {
    super(circuitSimulation, subcircuitEditor, id);
    this.subcircuitInstance = subcircuitInstance;
  }

  @Override
  public SubcircuitInstanceSimulationData save()
  {
    return new SubcircuitInstanceSimulationData(getId(),
                                                subcircuitEditor.getId(),
                                                circuitSimulation.getId(),
                                                subcircuitInstance.getId());
  }

  @Override
  public SubcircuitInstance getSubcircuitInstance()
  {
    return subcircuitInstance;
  }
}

