package net.logicim.ui.simulation.component.passive.pin;

import net.common.SimulatorException;
import net.common.type.Float2D;
import net.common.type.Int2D;
import net.common.util.StringUtil;
import net.logicim.data.circuit.SubcircuitPinAlignment;
import net.logicim.data.common.Radix;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.data.passive.wire.PinData;
import net.logicim.data.passive.wire.PinProperties;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.defaults.DefaultLogicLevels;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.common.wire.TraceValue;
import net.logicim.domain.passive.power.PowerPinNames;
import net.logicim.domain.passive.power.PowerSource;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.passive.wire.Pin;
import net.logicim.ui.circuit.SubcircuitInstanceViewFinder;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.common.integratedcircuit.PropertyClamp;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.circle.CircleView;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.component.common.InstanceView;
import net.logicim.ui.simulation.component.integratedcircuit.extra.FrameView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitPinView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Font.MONOSPACED;
import static java.awt.Font.SANS_SERIF;
import static net.logicim.domain.common.wire.TraceValue.*;

public class PinView
    extends PassiveView<Pin, PinProperties>
{
  public static int FONT_SIZE = 10;

  protected PortView port;
  protected SubcircuitInstanceViewFinder subcircuitInstanceViewFinder;
  protected Int2D relativeSubcircuitPosition;
  protected FamilyVoltageConfiguration familyVoltageConfiguration;

  protected CircleView circleView;
  protected FrameView frameView;
  protected TextView labelView;
  protected TextView dataView;
  protected int maxDigits;

  public PinView(SubcircuitView subcircuitView,
                 SubcircuitInstanceViewFinder subcircuitInstanceViewFinder,
                 Int2D position,
                 Rotation rotation,
                 PinProperties properties)
  {
    super(subcircuitView,
          position,
          rotation,
          properties);
    this.subcircuitInstanceViewFinder = subcircuitInstanceViewFinder;
    familyVoltageConfiguration = FamilyVoltageConfigurationStore.get(properties.family);
    relativeSubcircuitPosition = new Int2D();
    maxDigits = calculateMaxDigits();
    relativeRightRotations = 2;

    createGraphics();
    createPortViews();
    finaliseView();
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

  public List<String> getPortNames()
  {
    List<String> portNames = new ArrayList<>();
    for (int i = 0; i < properties.bitWidth; i++)
    {
      String portName = "Port " + i;
      portNames.add(portName);
    }
    return portNames;
  }

  @Override
  protected void createPortViews()
  {
    List<String> portNames = getPortNames();
    port = new PortView(this, portNames, new Int2D());

    if (mustIncludeExplicitPowerPorts(familyVoltageConfiguration))
    {
      new PortView(this, PowerPinNames.VCC, new Int2D((int) Math.floor(1), -1));
      new PortView(this, PowerPinNames.GND, new Int2D((int) Math.ceil(-1), -1));
    }
  }

  @Override
  public PassiveData<?> save(boolean selected)
  {
    return new PinData(position,
                       rotation,
                       getName(),
                       saveSimulationPassives(),
                       savePorts(),
                       id,
                       enabled,
                       selected,
                       properties.bitWidth,
                       properties.alignment,
                       properties.anchour,
                       properties.weight,
                       properties.inverting,
                       properties.clockNotch,
                       properties.family,
                       properties.explicitPowerPorts,
                       properties.radix);
  }

  @Override
  public void clampProperties(PinProperties newProperties)
  {
    newProperties.bitWidth = PropertyClamp.clamp(newProperties.bitWidth, 1, PropertyClamp.MAX_WIDTH);
    newProperties.weight = PropertyClamp.clamp(newProperties.weight, 0, 8191);
  }

  @Override
  protected Pin createPassive(ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
    SubcircuitSimulation containingSubcircuitSimulation = viewPath.getSubcircuitSimulation(circuitSimulation);
    Pin pin = new Pin(containingSubcircuitSimulation,
                      properties.name,
                      properties.bitWidth);
    if (!mustIncludeExplicitPowerPorts(familyVoltageConfiguration))
    {
      createPowerPorts(containingSubcircuitSimulation,
                       familyVoltageConfiguration,
                       pin);
    }
    return pin;
  }

  protected void createPowerPorts(SubcircuitSimulation containingSubcircuitSimulation, FamilyVoltageConfiguration familyVoltageConfiguration, Pin pin)
  {
    VoltageConfiguration voltageConfiguration = familyVoltageConfiguration.getDefaultVoltageConfiguration(DefaultLogicLevels.get());

    Trace vccTrace = new Trace();
    pin.getVoltageCommon().connect(vccTrace);

    PowerSource vccPowerSource = new PowerSource(containingSubcircuitSimulation, "", voltageConfiguration.getVcc());
    vccPowerSource.getPowerOutPort().connect(vccTrace);

    Trace gndTrace = new Trace();
    pin.getVoltageGround().connect(gndTrace);

    PowerSource gndPowerSource = new PowerSource(containingSubcircuitSimulation, "", 0);
    gndPowerSource.getPowerOutPort().connect(gndTrace);
  }

  protected boolean mustIncludeExplicitPowerPorts(FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    if (properties.explicitPowerPorts)
    {
      return true;
    }
    else
    {
      VoltageConfiguration voltageConfiguration = familyVoltageConfiguration.getDefaultVoltageConfiguration(DefaultLogicLevels.get());
      if (voltageConfiguration == null)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
  }

  @Override
  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    ViewPath viewPath,
                    CircuitSimulation circuitSimulation)
  {
    super.paint(graphics,
                viewport,
                viewPath,
                circuitSimulation);

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

    TraceValue[] values = null;
    long time = circuitSimulation.getTime();
    Pin passive = simulationPassives.get(viewPath, circuitSimulation);
    if (passive != null)
    {
      values = port.getValue(viewPath,
                             circuitSimulation,
                             familyVoltageConfiguration,
                             passive.getVCC(time));
    }

    dataView.setText(getStringValue(values));
    dataView.paint(graphics, viewport);
    labelView.paint(graphics, viewport);

    paintPorts(graphics,
               viewport,
               viewPath,
               circuitSimulation);
    graphics.setColor(color);
    graphics.setStroke(stroke);
    graphics.setFont(font);
  }

  private String getStringValue(TraceValue[] values)
  {
    if (values == null)
    {
      return "";
    }

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

  public PortView getPortView()
  {
    return port;
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

  @Override
  public int compareTo(InstanceView obj)
  {
    if (!(obj instanceof PinView))
    {
      return super.compareTo(obj);
    }

    PinView other = (PinView) obj;
    PinProperties otherProperties = other.properties;
    int result = Integer.compare(properties.weight, otherProperties.weight);
    if (result != 0)
    {
      return result;
    }

    result = properties.name.compareTo(otherProperties.name);
    if (result != 0)
    {
      return result;
    }

    result = super.compareTo(other);
    return result;
  }

  public TextView getLabelView()
  {
    return labelView;
  }

  @Override
  public void recalculatePropertiesAfterNew(SubcircuitView subcircuitView)
  {
    super.recalculatePropertiesAfterNew(subcircuitView);
    PinPropertyHelper helper = new PinPropertyHelper(subcircuitView.findAllPins());
    boolean updatedName = helper.ensureUniquePinName(this);
    helper.ensureNextWeight(properties);

    if (updatedName)
    {
      labelView.setText(properties.name);
    }

    if (rotation.isEast())
    {
      properties.alignment = SubcircuitPinAlignment.RIGHT;
    }
    else if (rotation.isWest())
    {
      properties.alignment = SubcircuitPinAlignment.LEFT;
    }
    else if (rotation.isNorth())
    {
      properties.alignment = SubcircuitPinAlignment.TOP;
    }
    else if (rotation.isSouth())
    {
      properties.alignment = SubcircuitPinAlignment.BOTTOM;
    }
  }

  public List<SubcircuitPinView> getSubcircuitPinViews()
  {
    ArrayList<SubcircuitPinView> subcircuitPinViews = new ArrayList<>();
    List<SubcircuitInstanceView> subcircuitInstanceViews = subcircuitInstanceViewFinder.getSubcircuitInstanceViews(getContainingSubcircuitView());
    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      SubcircuitPinView subcircuitPinView = subcircuitInstanceView.getSubcircuitPinView(this);
      subcircuitPinViews.add(subcircuitPinView);
    }
    return subcircuitPinViews;
  }

  @Override
  public String toDebugString()
  {
    return super.toDebugString() + String.format("Bit Width [%s]\nFamily [%s]\nExplicit Power [%s]\nPin Alignment [%s]\nPin Anchour [%s]\nWeight [%s]\nInverting [%s]\nClock Notch [%s]\nRadix [%s]\n",
                                                 properties.bitWidth,
                                                 properties.family.toString(),
                                                 properties.explicitPowerPorts,
                                                 properties.alignment,
                                                 properties.anchour,
                                                 properties.weight,
                                                 properties.inverting,
                                                 properties.clockNotch,
                                                 properties.radix);
  }
}

