package net.logicim.data.integratedcircuit.standard.logic.common;

import net.logicim.data.common.Int2DData;
import net.logicim.data.common.RotationData;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.PortData;
import net.logicim.ui.integratedcircuit.standard.logic.common.LogicGateView;

import java.util.List;

public abstract class LogicGateData<ICV extends LogicGateView<?>>
    extends IntegratedCircuitData<ICV>
{
  protected int inputCount;

  public LogicGateData(Int2DData position,
                       RotationData rotation,
                       List<IntegratedCircuitEventData<?>> events,
                       List<PortData> portData,
                       int inputCount)
  {
    super(position, rotation, events, portData);
    this.inputCount = inputCount;
  }
}

