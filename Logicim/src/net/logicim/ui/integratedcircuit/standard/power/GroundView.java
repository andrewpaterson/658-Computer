package net.logicim.ui.integratedcircuit.standard.power;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.DiscreteData;
import net.logicim.data.integratedcircuit.standard.power.GroundPortData;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.line.LineView;

import java.awt.*;

public class GroundView
    extends PowerSourceView<GroundProperties>
{
  protected LineView line1;
  protected LineView line2;
  protected LineView line3;
  protected LineView line4;

  public GroundView(CircuitEditor circuitEditor,
                    Int2D position,
                    Rotation rotation,
                    String name)
  {
    super(circuitEditor, position, rotation, name);

    float yOffset = 0.5f;
    line1 = new LineView(this, new Float2D(-0.9f, yOffset), new Float2D(0.9f, yOffset));
    line2 = new LineView(this, new Float2D(-0.6f, yOffset + 0.333f), new Float2D(0.6f, yOffset + 0.333f));
    line3 = new LineView(this, new Float2D(-0.3f, yOffset + 0.666f), new Float2D(0.3f, yOffset + 0.666f));
    line4 = new LineView(this, new Float2D(0, 0), new Float2D(0, yOffset));
    finaliseView();
  }

  @Override
  public float getVoltageOut()
  {
    return 0;
  }

  @Override
  protected void createPortViews()
  {
    new PortView(this, powerSource.getPort("Power"), new Int2D(0, 0));
  }

  @Override
  public DiscreteData save()
  {
    return new GroundPortData(position,
                              rotation,
                              properties.name,
                              savePorts());
  }

  @Override
  protected GroundProperties createProperties()
  {
    return new GroundProperties();
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);
    Stroke stroke = graphics.getStroke();
    Color color = graphics.getColor();
    if (line1 != null)
    {
      line1.paint(graphics, viewport);
      line2.paint(graphics, viewport);
      line3.paint(graphics, viewport);
      line4.paint(graphics, viewport);
    }
    paintPorts(graphics, viewport, time);
    graphics.setStroke(stroke);
    graphics.setColor(color);
  }

  @Override
  public String getType()
  {
    return "Ground";
  }
}
