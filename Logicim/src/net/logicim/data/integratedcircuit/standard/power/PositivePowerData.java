package net.logicim.data.integratedcircuit.standard.power;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.port.MultiPortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.passive.power.PositivePowerProperties;
import net.logicim.ui.integratedcircuit.standard.passive.power.PositivePowerView;

import java.util.List;

public class PositivePowerData
    extends PassiveData<PositivePowerView>
{
  private float voltage;

  public PositivePowerData()
  {
  }

  public PositivePowerData(Int2D position,
                           Rotation rotation,
                           String name,
                           List<MultiPortData> ports,
                           boolean selected,
                           float voltage)
  {
    super(position, rotation, name, ports, selected);
    this.voltage = voltage;
  }

  @Override
  protected PositivePowerView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new PositivePowerView(circuitEditor,
                                 position,
                                 rotation,
                                 new PositivePowerProperties(name, voltage));
  }
}

