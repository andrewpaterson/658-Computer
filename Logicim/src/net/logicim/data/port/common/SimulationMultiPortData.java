package net.logicim.data.port.common;

import net.logicim.data.common.ReflectiveData;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimulationMultiPortData
    extends ReflectiveData
{
  public Map<Long, MultiPortData> simulationMultiPortData;

  public SimulationMultiPortData()
  {
    this.simulationMultiPortData = new LinkedHashMap<>();
  }

  public void add(long simulationId, MultiPortData multiPortData)
  {
    simulationMultiPortData.put(simulationId, multiPortData);
  }
}

