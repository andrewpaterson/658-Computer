package net.logicim.ui.info;

import net.common.util.StringUtil;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.Logicim;

public class SimulationInfo
    extends InfoLabel
{
  public SimulationInfo(Logicim editor)
  {
    super(editor);
  }

  @Override
  public String getInfo()
  {
    SubcircuitSimulation subcircuitSimulation = editor.getCurrentSubcircuitSimulation();
    CircuitSimulation circuitSimulation = null;
    if (subcircuitSimulation != null)
    {
      circuitSimulation = subcircuitSimulation.getCircuitSimulation();
    }

    String description;
    if (circuitSimulation != null)
    {
      String name = circuitSimulation.getName();
      long id = circuitSimulation.getId();
      if (StringUtil.isEmptyOrNull(name))
      {
        description = "" + id;
      }
      else
      {
        description = name + " (" + id + ")";
      }
    }
    else
    {
      description = "";
    }
    return String.format(" Circuit Simulation: %s of %s", description, editor.getCircuitSimulationCount());
  }
}

