package net.logicim.data.integratedcircuit.event;

import net.logicim.data.common.ReflectiveData;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimulationIntegratedCircuitEventData
    extends ReflectiveData
{
  public Map<Long, List<IntegratedCircuitEventData<?>>> simulationIntegratedCircuitEventData;

  public SimulationIntegratedCircuitEventData()
  {
    this.simulationIntegratedCircuitEventData = new LinkedHashMap<>();
  }

  public void add(long simulationId, List<IntegratedCircuitEventData<?>> eventDatas)
  {
    simulationIntegratedCircuitEventData.put(simulationId, (List) eventDatas);
  }
}

