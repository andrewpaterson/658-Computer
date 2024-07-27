package net.logicim.data.integratedcircuit.event;

import net.logicim.data.common.ReflectiveData;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimulationIntegratedCircuitEventData
    extends ReflectiveData
{
  public Map<Long, MultiIntegratedCircuitEventData> simulationIntegratedCircuitEventData;

  public SimulationIntegratedCircuitEventData()
  {
    this.simulationIntegratedCircuitEventData = new LinkedHashMap<>();
  }

  public void add(long simulationId, MultiIntegratedCircuitEventData eventDatas)
  {
    simulationIntegratedCircuitEventData.put(simulationId, eventDatas);
  }
}

