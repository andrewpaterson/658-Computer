package net.logicim.ui.info;

import net.logicim.common.util.StringUtil;
import net.logicim.domain.CircuitSimulation;
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
    CircuitSimulation currentSimulation = editor.getCurrentSimulation();
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

