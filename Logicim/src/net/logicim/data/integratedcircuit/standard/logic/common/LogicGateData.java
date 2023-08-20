package net.logicim.data.integratedcircuit.standard.logic.common;

import net.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.simulation.SimulationStateData;
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
                       SimulationIntegratedCircuitEventData events,
                       List<SimulationMultiPortData> ports,
                       long id,
                       boolean enabled,
                       boolean selected,
                       SimulationStateData<State> simulationState,
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
          id,
          enabled,
          selected,
          simulationState,
          explicitPowerPorts);
    this.inputCount = inputCount;
    this.inputWidth = inputWidth;
  }
}

