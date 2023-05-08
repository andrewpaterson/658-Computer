package net.logicim.data.integratedcircuit.event;

import java.util.List;

public class MultiIntegratedCircuitEventData
{
  public List<IntegratedCircuitEventData<?>> integratedCircuitEventData;

  public MultiIntegratedCircuitEventData(List<IntegratedCircuitEventData<?>> eventDatas)
  {
    integratedCircuitEventData = eventDatas;
  }
}

