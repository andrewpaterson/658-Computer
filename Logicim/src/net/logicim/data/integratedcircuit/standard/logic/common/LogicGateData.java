package net.logicim.data.integratedcircuit.standard.logic.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.PortData;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.logic.common.LogicGateView;

import java.util.List;

public abstract class LogicGateData<ICV extends LogicGateView<?>>
    extends IntegratedCircuitData<ICV>
{
  protected int inputCount;

  public LogicGateData()
  {
  }

  public LogicGateData(Int2D position,
                       Rotation rotation,
                       List<IntegratedCircuitEventData<?>> events,
                       List<PortData> portData,
                       int inputCount)
  {
    super(position, rotation, events, portData);
    this.inputCount = inputCount;
  }
}

