package net.logicim.ui.simulation.component.passive.pin;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.util.StringUtil;
import net.logicim.data.common.Radix;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.data.passive.wire.PinData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.domain.common.wire.TraceValue;
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

import static java.awt.Font.MONOSPACED;
import static java.awt.Font.SANS_SERIF;
import static net.logicim.domain.common.wire.TraceValue.*;

public class PinView
    extends PassiveView<Pin, PinProperties>
{
  public static int FONT_SIZE = 10;

  protected PortView port;
  protected Int2D relativeSubcircuitPosition;
  protected FamilyVoltageConfiguration familyVoltageConfiguration;

  protected CircleView circleView;
  protected FrameView frameView;
  protected TextView labelView;
  protected TextView dataView;
  protected int maxDigits;

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
    relativeSubcircuitPosition = new Int2D();
    maxDigits = calculateMaxDigits();

    createGraphics();
    finaliseView(circuit);
  }

  protected void createGraphics()
  {
    String text = StringUtil.pad(maxDigits, "X");

    dataView = new TextView(this,
                            new Float2D(),
                            text,
                            MONOSPACED,
                            FONT_SIZE,
                            false,
                            HorizontalAlignment.CENTER);
    Float2D topLeft = dataView.getTextOffset().clone();
    Float2D bottomRight = new Float2D(dataView.getTextDimension());
    float fontLength = (float) Math.ceil(bottomRight.x - topLeft.x + 0.5f);
    dataView.setPositionRelativeToIC(new Float2D(0.0f, -fontLength / 2.0f));

    labelView = new TextView(this,
                             new Float2D(0, -fontLength - 0.25f),
                             properties.name,
                             SANS_SERIF,
                             FONT_SIZE,
                             true,
                             HorizontalAlignment.LEFT);

    if (maxDigits == 1)
    {
      circleView = new CircleView(this, new Float2D(0, -1), 1, true, true);
      frameView = null;
    }
    else
    {
      frameView = new FrameView(this, Colours.getInstance().getShapeFill(), 1, -1, 1, -fontLength, 0);
      circleView = null;
    }
  }

  private int calculateMaxDigits()
  {
    int maxDigits = 0;
    if (properties.radix == Radix.BINARY)
    {
      maxDigits = properties.bitWidth;
    }
    else if (properties.radix == Radix.HEXADECIMAL)
    {
      maxDigits = (int) Math.ceil(properties.bitWidth / 4.0f);
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
    port = new PortView(this, passive.getTracePorts(), new Int2D());

    new PortView(this, passive.getVoltageCommon(), new Int2D((int) Math.floor(1), -1));
    new PortView(this, passive.getVoltageGround(), new Int2D((int) Math.ceil(-1), -1));
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
    if (properties.bitWidth < 1)
    {
      properties.bitWidth = 1;
    }
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

    FamilyVoltageConfiguration familyVoltageConfiguration = FamilyVoltageConfigurationStore.get(properties.family);

    TraceValue[] values = port.getValue(time, familyVoltageConfiguration, passive.getVCC(time));

    dataView.setText(getStringValue(values));
    dataView.paint(graphics, viewport);
    labelView.paint(graphics, viewport);

    paintPorts(graphics, viewport, time);
    graphics.setColor(color);
    graphics.setStroke(stroke);
    graphics.setFont(font);
  }

  private String getStringValue(TraceValue[] values)
  {
    if (properties.radix == Radix.BINARY)
    {
      return toBinaryString(values);
    }
    else if (properties.radix == Radix.HEXADECIMAL)
    {
      return toHexadecimalString(values);
    }
    else if (properties.radix == Radix.DECIMAL)
    {
      return toDecimalString(values);
    }
    else
    {
      return "";
    }
  }

  private String toDecimalString(TraceValue[] values)
  {
    long longValue = 0;
    for (int i = 0; i < values.length; i++)
    {
      TraceValue value = values[i];
      if (value == High)
      {
        longValue += 1 << i;
      }
      else if (value == Undriven)
      {
        return StringUtil.pad(maxDigits, Character.toString(getBinaryChar(Undriven)));
      }
      else if (value == Unsettled)
      {
        return StringUtil.pad(maxDigits, Character.toString(getBinaryChar(Unsettled)));
      }
    }

    return Long.toString(longValue);
  }

  private String toHexadecimalString(TraceValue[] values)
  {
    StringBuilder builder = new StringBuilder();
    int nybbleIndex = 0;
    boolean undriven = false;
    boolean unsettled = false;
    int nybble = 0;
    for (TraceValue value : values)
    {
      if (nybbleIndex == 0)
      {
        nybble = 0;
      }
      if (value == High)
      {
        nybble += 1 << nybbleIndex;
      }
      else if (value == Undriven)
      {
        undriven = true;
      }
      else if (value == Unsettled)
      {
        unsettled = true;
      }

      nybbleIndex++;
      if (nybbleIndex == 4)
      {
        nybbleIndex = 0;
        if (unsettled)
        {
          builder.append(getBinaryChar(Unsettled));
        }
        else if (undriven)
        {
          builder.append(getBinaryChar(Undriven));
        }
        else
        {
          builder.append(Integer.toHexString(nybble));
        }
        undriven = false;
        unsettled = false;
      }
    }
    builder.reverse();
    return builder.toString().toUpperCase();
  }

  private String toBinaryString(TraceValue[] values)
  {
    StringBuilder builder = new StringBuilder();
    for (int i = values.length - 1; i >= 0; i--)
    {
      builder.append(getBinaryChar(values[i]));
    }
    return builder.toString();
  }

  private char getBinaryChar(TraceValue value)
  {
    if (value == High)
    {
      return '1';
    }
    else if (value == Low)
    {
      return '0';
    }
    else if (value == Unsettled)
    {
      return 'X';
    }
    else if (value == Undriven)
    {
      return '.';
    }
    else
    {
      throw new SimulatorException("Cannot get binary value for unknown radix.");
    }
  }

  public Int2D getRelativeInstancePosition()
  {
    return relativeSubcircuitPosition;
  }

  @Override
  public void setRotation(Rotation rotation)
  {
    super.setRotation(rotation);
    updateTextViews();
  }

  protected void updateTextViews()
  {
  }
}

