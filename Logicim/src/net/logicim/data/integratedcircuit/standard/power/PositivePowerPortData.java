package net.logicim.data.integratedcircuit.standard.power;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.DiscreteData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.DiscreteView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.power.PositivePowerView;

public class PositivePowerPortData
    extends DiscreteData
{
  private float voltage;

  public PositivePowerPortData()
  {
  }

  public PositivePowerPortData(Int2D position, Rotation rotation, String name, float voltage)
  {
    super(position, rotation, name);
    this.voltage = voltage;
  }

  @Override
  public DiscreteView createAndLoad(CircuitEditor circuitEditor,
                                    TraceLoader traceLoader)
  {
    return new PositivePowerView(circuitEditor, position, rotation, name, voltage);
  }
}

