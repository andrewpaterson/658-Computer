package net.logicim.data.integratedcircuit.standard.logic.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.common.MultiPortData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.domain.common.state.State;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.common.LogicGateView;

import java.util.List;

public abstract class LogicGateData<ICV extends LogicGateView<?>>
    extends StandardIntegratedCircuitData<ICV, State>
{
  protected int inputCount;
  protected int inputWidth;

  public LogicGateData()
  {
  }

  public LogicGateData(Int2D position,
                       Rotation rotation,
                       String name,
                       Family family,
                       List<IntegratedCircuitEventData<?>> events,
                       List<SimulationMultiPortData> ports,
                       boolean selected,
                       State state,
                       int inputCount,
                       int inputWidth,
                       boolean explicitPowerPorts)
  {
    super(position,
          rotation,
          name,
          family,
          events,
          ports,
          selected,
          state,
          explicitPowerPorts);
    this.inputCount = inputCount;
    this.inputWidth = inputWidth;
  }
}

