package net.logicim.ui.simulation.component.passive.power;

import net.common.type.Float2D;
import net.common.type.Int2D;
import net.logicim.data.passive.power.GroundData;
import net.logicim.data.passive.power.GroundProperties;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.power.PowerPinNames;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.line.LineView;

import java.awt.*;

public class GroundView
    extends PowerSourceView<GroundProperties>
{
  protected LineView line1;
  protected LineView line2;
  protected LineView line3;
  protected LineView line4;

  public GroundView(SubcircuitView subcircuitView,
                    Int2D position,
                    Rotation rotation,
                    GroundProperties properties)
  {
    super(subcircuitView, position, rotation, properties);
    createGraphics();
    createPortViews();
    finaliseView();
  }

  protected void createGraphics()
  {
    float yOffset = 0.5f;
    line1 = new LineView(this, new Float2D(-0.9f, yOffset), new Float2D(0.9f, yOffset));
    line2 = new LineView(this, new Float2D(-0.6f, yOffset + 0.333f), new Float2D(0.6f, yOffset + 0.333f));
    line3 = new LineView(this, new Float2D(-0.3f, yOffset + 0.666f), new Float2D(0.3f, yOffset + 0.666f));
    line4 = new LineView(this, new Float2D(0, 0), new Float2D(0, yOffset));
  }

  @Override
  public float getVoltageOut()
  {
    return 0;
  }

  @Override
  protected void createPortViews()
  {
    new PortView(this, PowerPinNames.POWER, new Int2D(0, 0));
  }

  @Override
  public GroundData save(boolean selected)
  {
    return new GroundData(position,
                          rotation,
                          properties.name,
                          saveSimulationPassives(),
                          savePorts(),
                          id,
                          enabled,
                          selected);
  }

  @Override
  public void clampProperties(GroundProperties newProperties)
  {
  }

  @Override
  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    ViewPath viewPath,
                    CircuitSimulation circuitSimulation)
  {
    super.paint(graphics, viewport, viewPath, circuitSimulation);

    Stroke stroke = graphics.getStroke();
    Color color = graphics.getColor();

    if (line1 != null)
    {
      line1.paint(graphics, viewport);
      line2.paint(graphics, viewport);
      line3.paint(graphics, viewport);
      line4.paint(graphics, viewport);
    }
    paintPorts(graphics,
               viewport,
               viewPath,
               circuitSimulation);

    graphics.setStroke(stroke);
    graphics.setColor(color);
  }

  @Override
  public String getType()
  {
    return "Ground";
  }
}

