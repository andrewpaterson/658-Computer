package net.logicim.data.integratedcircuit.standard.logic.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.PortData;
import net.logicim.domain.common.state.State;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.logic.common.LogicGateView;

import java.util.List;

public abstract class LogicGateData<ICV extends LogicGateView<?>>
    extends StandardIntegratedCircuitData<ICV, State>
{
  protected int inputCount;

  public LogicGateData()
  {
  }

  public LogicGateData(Int2D position,
                       Rotation rotation,
                       String name,
                       String family,
                       List<IntegratedCircuitEventData<?>> events,
                       List<PortData> ports,
                       State state,
                       int inputCount,
                       boolean explicitPowerPorts)
  {
    super(position,
          rotation,
          name,
          family,
          events,
          ports,
          state,
          explicitPowerPorts);
    this.inputCount = inputCount;
  }
}

