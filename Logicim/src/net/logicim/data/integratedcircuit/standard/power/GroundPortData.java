package net.logicim.data.integratedcircuit.standard.power;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.DiscreteData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.port.Port;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.power.GroundView;

import java.util.List;

public class GroundPortData
    extends DiscreteData
{
  public GroundPortData()
  {
  }

  public GroundPortData(Int2D position, Rotation rotation, String name, List<PortData> ports)
  {
    super(position, rotation, name, ports);
  }

  protected GroundView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new GroundView(circuitEditor, position, rotation, name);
  }

  @Override
  protected void loadPort(CircuitEditor circuitEditor, PortData portData, Port port)
  {
  }
}
