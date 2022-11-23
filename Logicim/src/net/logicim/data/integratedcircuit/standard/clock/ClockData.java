package net.logicim.data.integratedcircuit.standard.clock;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.port.PortData;
import net.logicim.ui.common.Rotation;

import java.util.List;

public class ClockData
    extends IntegratedCircuitData
{
  protected float frequency;

  protected boolean state;

  public ClockData(Int2D position, Rotation rotation, float frequency, boolean state, List<PortData> portData)
  {
    super(position, rotation, portData);
    this.frequency = frequency;
    this.state = state;
  }
}

