package net.logicim.ui.integratedcircuit.standard.power;

import net.logicim.common.type.Int2D;
import net.logicim.domain.power.PowerSource;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;

public class PositivePowerView
    extends PowerSourceView
{
  protected float voltage;

  public PositivePowerView(CircuitEditor circuitEditor,
                           Int2D position,
                           Rotation rotation,
                           String name,
                           float voltage)
  {
    super(circuitEditor, position, rotation, name);
    this.voltage = voltage;
    finaliseView();
  }

  protected PowerSource createPowerSource()
  {
    return new PowerSource(circuitEditor.getCircuit(), name, voltage);
  }

  @Override
  public float getSourceVoltage()
  {
    return voltage;
  }

  @Override
  protected void createPortViews()
  {
    new PortView(this, powerSource.getPort("Power"), new Int2D(0, 1));
  }
}

