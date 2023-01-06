package net.logicim.ui.integratedcircuit.standard.power;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.DiscreteData;
import net.logicim.data.integratedcircuit.standard.power.PositivePowerPortData;
import net.logicim.domain.common.voltage.Voltage;
import net.logicim.domain.power.PowerSource;
import net.logicim.ui.CircuitEditor;
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

  public PositivePowerView(CircuitEditor circuitEditor,
                           Int2D position,
                           Rotation rotation,
                           PositivePowerProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
    createGraphics();
    finaliseView();
  }

  protected void createGraphics()
  {
    float yTop = 0.4f;
    float yBottom = -1f;
    float radius = 0.9f;
    rectangle = new RectangleView(this, new Float2D(-radius, yBottom + 0.4f), new Float2D(radius, yTop), true, true);
    polygonView = new PolygonView(this, getFillColour(), true, true, new Float2D(0, 1), new Float2D(radius, yTop), new Float2D(radius, yBottom), new Float2D(-radius, yBottom), new Float2D(-radius, yTop));
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

  protected void createPowerSource()
  {
    powerSource = new PowerSource(circuitEditor.getCircuit(),
                                  properties.name,
                                  properties.voltage_V);
    powerSource.disable();
  }

  @Override
  public float getVoltageOut()
  {
    return properties.voltage_V;
  }

  @Override
  protected void createPortViews()
  {
    new PortView(this, powerSource.getPort("Power"), new Int2D(0, 1));
  }

  @Override
  public DiscreteData save(boolean selected)
  {
    return new PositivePowerPortData(position,
                                     rotation,
                                     properties.name,
                                     savePorts(),
                                     selected,
                                     properties.voltage_V);
  }

  @Override
  protected PositivePowerProperties createProperties()
  {
    return new PositivePowerProperties();
  }

  @Override
  public void clampProperties()
  {
    super.clampProperties();
    properties.voltage_V = clamp(properties.voltage_V, 0.01f, 7.0f);
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);

    Color color = graphics.getColor();
    Stroke stroke = graphics.getStroke();
    Font font = graphics.getFont();

    polygonView.paint(graphics, viewport);
    rectangle.updateGridCache();  // Suspect to the max.
    drawCenteredString(graphics, viewport, Voltage.toVoltageString(properties.voltage_V, false));

    paintPorts(graphics, viewport, time);
    graphics.setColor(color);
    graphics.setStroke(stroke);
    graphics.setFont(font);
  }

  public void drawCenteredString(Graphics graphics, Viewport viewport, String text)
  {
    Font font = graphics.getFont().deriveFont(Font.BOLD, (int) (11 * viewport.getZoom()));
    Color voltageColour = Colours.getInstance().getTraceVoltage(properties.voltage_V);
    graphics.setColor(voltageColour);

    FontMetrics metrics = graphics.getFontMetrics(font);
    int x = viewport.transformGridToScreenSpaceX(rectangle.getTransformedPosition());
    int y = viewport.transformGridToScreenSpaceY(rectangle.getTransformedPosition());
    int width = viewport.transformGridToScreenWidth(rectangle.getTransformedDimension());
    int height = viewport.transformGridToScreenHeight(rectangle.getTransformedDimension());

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
}

