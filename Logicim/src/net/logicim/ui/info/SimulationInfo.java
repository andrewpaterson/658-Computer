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
    SubcircuitSimulation subcircuitSimulation = editor.getSubcircuitSimulation();
    CircuitSimulation currentSimulation = null;
    if (subcircuitSimulation != null)
    {
      currentSimulation = subcircuitSimulation.getCircuitSimulation();
    }

    String description;
    if (currentSimulation != null)
    {
      String name = currentSimulation.getName();
      long id = currentSimulation.getId();
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
    return String.format(" Simulation %s of %s", description, editor.getSimulationCount());
  }
}

