package net.logicim.data.simulation;

import net.logicim.data.common.ReflectiveData;
import net.logicim.domain.common.state.State;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimulationStateData<E extends State>
    extends ReflectiveData
{
  public Map<Long, E> simulationStateData;

  public SimulationStateData()
  {
    simulationStateData = new LinkedHashMap<>();
  }

  public void add(long simulationId, E state)
  {
    simulationStateData.put(simulationId, state);
  }
}

