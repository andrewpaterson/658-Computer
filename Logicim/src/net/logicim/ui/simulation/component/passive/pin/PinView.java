package net.logicim.ui.simulation.component.passive.pin;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.util.StringUtil;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.data.passive.wire.PinData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.domain.passive.wire.Pin;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.circle.CircleView;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.component.integratedcircuit.extra.FrameView;

import java.awt.*;

public class PinView
    extends PassiveView<Pin, PinProperties>
{
  public static int FONT_SIZE = 10;
  public static long nextId = 1L;

  protected PortView port;
  protected long id;
  protected Int2D relativeSubcircuitPosition;
  protected FamilyVoltageConfiguration familyVoltageConfiguration;

  protected CircleView circleView;
  protected FrameView frameView;
  protected TextView labelView;
  protected TextView dataView;

  public PinView(SubcircuitView subcircuitView,
                 Circuit circuit,
                 Int2D position,
                 Rotation rotation,
                 PinProperties properties)
  {
    super(subcircuitView,
          circuit,
          position,
          rotation,
          properties);
    this.familyVoltageConfiguration = FamilyVoltageConfigurationStore.get(properties.family);
    id = nextId;
    nextId++;
    relativeSubcircuitPosition = new Int2D();

    createGraphics();
    finaliseView(circuit);
  }

  protected void createGraphics()
  {
    int maxDigits = maxDigits();
    String text = StringUtil.pad(maxDigits, "X");

    dataView = new TextView(this, new Float2D(0, -0.5f), text, FONT_SIZE, false, HorizontalAlignment.LEFT);
    Float2D topLeft = dataView.getTextOffset().clone();
    Float2D bottomRight = new Float2D(dataView.getTextDimension());
    float fontLength = (float) Math.ceil(bottomRight.x - topLeft.x);

    labelView = new TextView(this, new Float2D(0, -fontLength + 1), properties.name, FONT_SIZE, true, HorizontalAlignment.LEFT);

    if (maxDigits == 1)
    {
      circleView = new CircleView(this, new Float2D(0, 1), 1, true, true);
      frameView = null;
    }
    else
    {
      frameView = new FrameView(this, Colours.getInstance().getShapeFill(), 1, -1, 1, 0, fontLength);
      circleView = null;
    }
  }

  private int maxDigits()
  {
    int maxDigits = 0;
    if (properties.radix == Radix.BINARY)
    {
      maxDigits = properties.bitWidth;
    }
    else if (properties.radix == Radix.HEXADECIMAL)
    {
      maxDigits = properties.bitWidth / 16 + 1;
    }
    else if (properties.radix == Radix.DECIMAL)
    {
      if (properties.bitWidth < 63)
      {
        long maxInt = 1 << properties.bitWidth - 1;
        String s = Long.toString(maxInt);
        maxDigits = s.length();
      }
      else if (properties.bitWidth == 64)
      {
        maxDigits = 20;
      }
      else if (properties.bitWidth == 63)
      {
        maxDigits = 19;
      }
      else
      {
        throw new SimulatorException("Cannot calculate maximum decimal digits for bitwidth [%s].", properties.bitWidth);
      }
    }
    if (maxDigits < 1)
    {
      maxDigits = 1;
    }
    return maxDigits;
  }

  @Override
  public String getType()
  {
    return "Pin";
  }

  @Override
  protected void createPortViews()
  {
    port = new PortView(this, passive.getPorts(), new Int2D());
  }

  @Override
  public PassiveData<?> save(boolean selected)
  {
    return new PinData(position,
                       rotation,
                       getName(),
                       savePorts(),
                       selected,
                       properties.bitWidth,
                       properties.alignment,
                       properties.inverting,
                       properties.overline,
                       properties.clockNotch,
                       properties.family.getFamily(),
                       properties.radix);
  }

  @Override
  public void clampProperties(PinProperties newProperties)
  {
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  protected Pin createPassive(Circuit circuit)
  {
    return new Pin(circuit,
                   properties.name,
                   properties.bitWidth);
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);
    super.paint(graphics, viewport, time);

    Color color = graphics.getColor();
    Stroke stroke = graphics.getStroke();
    Font font = graphics.getFont();

    if (circleView != null)
    {
      circleView.paint(graphics, viewport);
    }
    if (frameView != null)
    {
      frameView.paint(graphics, viewport);
    }

    dataView.paint(graphics, viewport);
    labelView.paint(graphics, viewport);

    paintPorts(graphics, viewport, time);
    graphics.setColor(color);
    graphics.setStroke(stroke);
    graphics.setFont(font);
  }

  public Int2D getRelativeInstancePosition()
  {
    return relativeSubcircuitPosition;
  }
}

