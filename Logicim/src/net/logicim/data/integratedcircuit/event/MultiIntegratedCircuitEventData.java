package net.logicim.data.integratedcircuit.event;

import net.logicim.data.common.ReflectiveData;

import java.util.List;

public class MultiIntegratedCircuitEventData
    extends ReflectiveData
{
  public List<IntegratedCircuitEventData<?>> integratedCircuitEventData;

  public MultiIntegratedCircuitEventData()
  {
  }

  public MultiIntegratedCircuitEventData(List<IntegratedCircuitEventData<?>> eventDatas)
  {
    integratedCircuitEventData = eventDatas;
  }
}

