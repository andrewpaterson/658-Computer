package net.logicim.ui.integratedcircuit.standard.power;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.DiscreteData;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.rectangle.RectangleView;

import java.awt.*;

public class GroundView
    extends PowerSourceView
{
  protected RectangleView rectangle;

  public GroundView(CircuitEditor circuitEditor,
                    Int2D position,
                    Rotation rotation,
                    String name)
  {
    super(circuitEditor, position, rotation, name);
    rectangle = new RectangleView(this, 2, 2, true, true);
    finaliseView();
  }

  @Override
  public float getSourceVoltage()
  {
    return 0;
  }

  @Override
  protected void createPortViews()
  {
    new PortView(this, powerSource.getPort("Power"), new Int2D(0, -1));
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
    Stroke stroke = graphics.getStroke();
    Color color = graphics.getColor();
    if (rectangle != null)
    {
      rectangle.paint(graphics, viewport);
    }
    paintPorts(graphics, viewport, time);
    graphics.setStroke(stroke);
    graphics.setColor(color);
  }
}

