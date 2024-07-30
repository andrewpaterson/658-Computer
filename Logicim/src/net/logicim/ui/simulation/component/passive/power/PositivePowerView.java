package net.logicim.ui.simulation.component.passive.power;

import net.common.type.Float2D;
import net.common.type.Int2D;
import net.logicim.data.passive.power.PositivePowerData;
import net.logicim.data.passive.power.PositivePowerProperties;
import net.logicim.domain.common.voltage.Voltage;
import net.logicim.domain.passive.power.PowerPinNames;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.polygon.PolygonView;
import net.logicim.ui.shape.rectangle.RectangleView;

import java.awt.*;

import static net.logicim.ui.common.integratedcircuit.PropertyClamp.clamp;

public class PositivePowerView
    extends PowerSourceView<PositivePowerProperties>
{
  protected RectangleView rectangle;
  protected PolygonView polygonView;

  public PositivePowerView(SubcircuitView subcircuitView,
                           Int2D position,
                           Rotation rotation,
                           PositivePowerProperties properties)
  {
    super(subcircuitView, position, rotation, properties);
    createGraphics();
    createPortViews();
    finaliseView();
  }

  protected void createGraphics()
  {
    float yTop = 0.4f;
    float yBottom = -1f;
    float radius = 0.9f;
    rectangle = new RectangleView(this, new Float2D(-radius, yBottom + 0.4f), new Float2D(radius, yTop), true, true);
    polygonView = new PolygonView(this, getFillColour(), true, true, 2.0f, new Float2D(0, 1), new Float2D(radius, yTop), new Float2D(radius, yBottom), new Float2D(-radius, yBottom), new Float2D(-radius, yTop));
  }

  private Color getFillColour()
  {
    if (getVoltageOut() <= 4.0f)
    {
      return Colours.getInstance().getShapeFill();
    }
    else
    {
      return Colours.getInstance().getDarkShapeFill();
    }
  }

  @Override
  public float getVoltageOut()
  {
    return properties.voltage_V;
  }

  @Override
  protected void createPortViews()
  {
    new PortView(this, PowerPinNames.POWER, new Int2D(0, 1));
  }

  @Override
  public PositivePowerData save(boolean selected)
  {
    return new PositivePowerData(position,
                                 rotation,
                                 properties.name,
                                 saveSimulationPassives(),
                                 savePorts(),
                                 id,
                                 enabled,
                                 selected,
                                 properties.voltage_V);
  }

  @Override
  public void clampProperties(PositivePowerProperties newProperties)
  {
    newProperties.voltage_V = clamp(newProperties.voltage_V, 0.01f, 7.0f);
  }

  @Override
  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    SubcircuitSimulation subcircuitSimulation)
  {
    super.paint(graphics, viewport, subcircuitSimulation);

    Color color = graphics.getColor();
    Stroke stroke = graphics.getStroke();
    Font font = graphics.getFont();

    polygonView.paint(graphics, viewport);

    drawCenteredString(graphics, viewport, Voltage.toVoltageString(properties.voltage_V, false));

    paintPorts(graphics, viewport, subcircuitSimulation);
    graphics.setColor(color);
    graphics.setStroke(stroke);
    graphics.setFont(font);
  }

  public void drawCenteredString(Graphics graphics, Viewport viewport, String text)
  {
    rectangle.updateGridCache();  // Suspect to the max.

    Font font = viewport.getBoldFont(0, 11 * viewport.getZoom());
    Color voltageColour = Colours.getInstance().getTraceVoltage(properties.voltage_V);
    graphics.setColor(voltageColour);

    int x = viewport.transformGridToScreenSpaceX(rectangle.getTransformedPosition());
    int y = viewport.transformGridToScreenSpaceY(rectangle.getTransformedPosition());
    int width = viewport.transformGridToScreenWidth(rectangle.getTransformedDimension());
    int height = viewport.transformGridToScreenHeight(rectangle.getTransformedDimension());

    FontMetrics metrics = graphics.getFontMetrics(font);
    int sx = x + (width - metrics.stringWidth(text)) / 2;
    int sy = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();
    graphics.setFont(font);
    graphics.drawString(text, sx, sy);
  }

  @Override
  public String getType()
  {
    return "Positive Power";
  }

  @Override
  public String toDebugString()
  {
    return super.toDebugString() + String.format("Voltage [%.2fs]\n", properties.voltage_V);
  }
}

