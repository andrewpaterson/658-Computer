package net.logicim.data.simulation;

import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitInstance;
import net.logicim.domain.passive.subcircuit.SubcircuitInstanceSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;
import net.logicim.domain.passive.subcircuit.SubcircuitTopSimulation;

import java.util.HashMap;
import java.util.Map;

public class SimulationLoader
{
  protected Map<Long, SubcircuitSimulation> subcircuitSimulationsById;
  protected Map<Long, CircuitSimulation> circuitSimulationsById;

  public SimulationLoader()
  {
    SubcircuitSimulation.resetNextId();
    CircuitSimulation.resetNextId();
    subcircuitSimulationsById = new HashMap<>();
    circuitSimulationsById = new HashMap<>();
  }

  public CircuitSimulation create(String name, long circuitSimulationId)
  {
    if (circuitSimulationId > 0)
    {
      CircuitSimulation circuitSimulation = circuitSimulationsById.get(circuitSimulationId);
      if (circuitSimulation == null)
      {
        circuitSimulation = new CircuitSimulation(circuitSimulationId, name);
        circuitSimulationsById.put(circuitSimulation.getId(), circuitSimulation);
      }
      return circuitSimulation;
    }
    else
    {
      return null;
    }
  }

  public SubcircuitInstanceSimulation create(CircuitSimulation circuitSimulation,
                                             SubcircuitInstance subcircuitInstance,
                                             long subcircuitSimulationId)
  {
    if (subcircuitSimulationId > 0)
    {
      SubcircuitInstanceSimulation subcircuitSimulation = (SubcircuitInstanceSimulation) subcircuitSimulationsById.get(subcircuitSimulationId);
      if (subcircuitSimulation == null)
      {
        subcircuitSimulation = new SubcircuitInstanceSimulation(circuitSimulation, subcircuitSimulationId, subcircuitInstance);
        //This needs to be put into the correct SubcircuitEditor.
        subcircuitSimulationsById.put(subcircuitSimulation.getId(), subcircuitSimulation);
      }
      return subcircuitSimulation;
    }
    else
    {
      return null;
    }
  }

  public SubcircuitTopSimulation create(CircuitSimulation circuitSimulation,
                                        SubcircuitEditor subcircuitEditor,
                                        long subcircuitSimulationId)
  {
    if (subcircuitSimulationId > 0)
    {
      SubcircuitTopSimulation subcircuitSimulation = (SubcircuitTopSimulation) subcircuitSimulationsById.get(subcircuitSimulationId);
      if (subcircuitSimulation == null)
      {
        subcircuitSimulation = new SubcircuitTopSimulation(circuitSimulation, subcircuitSimulationId);
        subcircuitEditor.addSubcircuitSimulation(subcircuitSimulation);
        subcircuitSimulationsById.put(subcircuitSimulationId, subcircuitSimulation);
      }
      return subcircuitSimulation;
    }
    else
    {
      return null;
    }
  }

  public CircuitSimulation getCircuitSimulation(long id)
  {
    return circuitSimulationsById.get(id);
  }

  public SubcircuitSimulation getSubcircuitSimulation(long id)
  {
    return subcircuitSimulationsById.get(id);
  }
}

