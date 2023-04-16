package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.port.common.MultiPortData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.domain.common.state.SimulationState;
import net.logicim.domain.common.state.State;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorState;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.IntegratedCircuitView;

import java.util.List;

public abstract class StandardIntegratedCircuitData<ICV extends IntegratedCircuitView<?, ?>, STATE extends State>
    extends IntegratedCircuitData<ICV, STATE>
{
  protected boolean explicitPowerPorts;

  public StandardIntegratedCircuitData()
  {
  }

  public StandardIntegratedCircuitData(Int2D position,
                                       Rotation rotation,
                                       String name,
                                       Family family,
                                       SimulationIntegratedCircuitEventData events,
                                       List<SimulationMultiPortData> ports,
                                       long id,
                                       boolean selected,
                                       SimulationState<STATE> state,
                                       boolean explicitPowerPorts)
  {
    super(position,
          rotation,
          name,
          family,
          events,
          ports,
          id,
          selected,
          state);
    this.explicitPowerPorts = explicitPowerPorts;
  }
}

