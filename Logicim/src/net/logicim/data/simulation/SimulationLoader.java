package net.logicim.data.simulation;

import net.common.SimulatorException;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitInstanceSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitTopSimulation;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

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

  public CircuitSimulation createCircuitSimulation(String name, long circuitSimulationId)
  {
    if (circuitSimulationId <= 0)
    {
      throw new SimulatorException();
    }

    CircuitSimulation circuitSimulation = circuitSimulationsById.get(circuitSimulationId);
    if (circuitSimulation == null)
    {
      circuitSimulation = new CircuitSimulation(circuitSimulationId, name);
      circuitSimulationsById.put(circuitSimulation.getId(), circuitSimulation);
    }
    return circuitSimulation;
  }

  public SubcircuitSimulation createSubcircuitInstanceSimulation(CircuitSimulation circuitSimulation,
                                                                 SubcircuitEditor subcircuitEditor,
                                                                 long subcircuitSimulationId)
  {
    if (subcircuitSimulationId <= 0)
    {
      throw new SimulatorException();
    }

    SubcircuitSimulation subcircuitSimulation = subcircuitSimulationsById.get(subcircuitSimulationId);
    if (subcircuitSimulation == null)
    {
      subcircuitSimulation = new SubcircuitInstanceSimulation(circuitSimulation, subcircuitSimulationId);
      addSubcircuitSimulation(subcircuitEditor, subcircuitSimulation);
    }
    return subcircuitSimulation;
  }

  public SubcircuitSimulation createSubcircuitTopSimulation(CircuitSimulation circuitSimulation,
                                                            SubcircuitEditor subcircuitEditor,
                                                            long subcircuitSimulationId)
  {
    if (subcircuitSimulationId <= 0)
    {
      throw new SimulatorException();
    }

    SubcircuitSimulation subcircuitSimulation = subcircuitSimulationsById.get(subcircuitSimulationId);
    if (subcircuitSimulation == null)
    {
      subcircuitSimulation = new SubcircuitTopSimulation(subcircuitEditor.getInstanceSubcircuitView(),
                                                         circuitSimulation,
                                                         subcircuitSimulationId);
      addSubcircuitSimulation(subcircuitEditor, subcircuitSimulation);
    }
    return subcircuitSimulation;
  }

  protected void addSubcircuitSimulation(SubcircuitEditor subcircuitEditor, SubcircuitSimulation subcircuitSimulation)
  {
    subcircuitEditor.getInstanceSubcircuitView().addSubcircuitSimulation(subcircuitSimulation);
    subcircuitSimulationsById.put(subcircuitSimulation.getId(), subcircuitSimulation);
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

