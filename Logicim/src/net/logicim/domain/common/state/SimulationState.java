package net.logicim.domain.common.state;

import net.logicim.data.common.ReflectiveData;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimulationState<E extends State>
    extends ReflectiveData
{
  public Map<Long, E> simulationStateData;

  public SimulationState()
  {
    simulationStateData = new LinkedHashMap<>();
  }

  public void add(long simulationId, E state)
  {
    simulationStateData.put(simulationId, state);
  }
}

