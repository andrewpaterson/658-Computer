package net.logicim.data.integratedcircuit.standard.power;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.DiscreteData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.port.Port;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.power.PositivePowerView;

import java.util.List;

public class PositivePowerPortData
    extends DiscreteData
{
  private float voltage;

  public PositivePowerPortData()
  {
  }

  public PositivePowerPortData(Int2D position, Rotation rotation, String name, List<PortData> ports, float voltage)
  {
    super(position, rotation, name, ports);
    this.voltage = voltage;
  }

  @Override
  protected PositivePowerView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new PositivePowerView(circuitEditor, position, rotation, name, voltage);
  }

  @Override
  protected void loadPort(CircuitEditor circuitEditor, PortData portData, Port port)
  {
  }
}

