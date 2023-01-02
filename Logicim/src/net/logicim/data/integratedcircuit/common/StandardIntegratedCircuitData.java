package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.PortData;
import net.logicim.domain.common.state.State;
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
                                       String family,
                                       List<IntegratedCircuitEventData<?>> events,
                                       List<PortData> ports,
                                       boolean selected,
                                       STATE state,
                                       boolean explicitPowerPorts)
  {
    super(position,
          rotation,
          name,
          family,
          events,
          ports,
          selected,
          state);
    this.explicitPowerPorts = explicitPowerPorts;
  }
}

