package net.logicim.ui.integratedcircuit.standard.power;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.DiscreteData;
import net.logicim.domain.power.PowerSource;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.rectangle.RectangleView;

import java.awt.*;

public class PositivePowerView
    extends PowerSourceView
{
  protected float voltage;
  protected RectangleView rectangle;

  public PositivePowerView(CircuitEditor circuitEditor,
                           Int2D position,
                           Rotation rotation,
                           String name,
                           float voltage)
  {
    super(circuitEditor, position, rotation, name);
    this.voltage = voltage;
    rectangle = new RectangleView(this, 2, 2, true, true);
    finaliseView();
  }

  protected void createPowerSource()
  {
    powerSource = new PowerSource(circuitEditor.getCircuit(), name, voltage);
    powerSource.disable();
  }

  @Override
  public float getVoltageOut()
  {
    return voltage;
  }

  @Override
  protected void createPortViews()
  {
    new PortView(this, powerSource.getPort("Power"), new Int2D(0, 1));
  }

  @Override
  public DiscreteData save()
  {
    return null;
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);
    if (rectangle != null)
    {
      rectangle.paint(graphics, viewport);
    }
    paintPorts(graphics, viewport, time);
  }
}

