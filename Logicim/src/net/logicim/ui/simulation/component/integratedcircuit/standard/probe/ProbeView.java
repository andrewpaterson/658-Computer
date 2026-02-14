package net.logicim.ui.simulation.component.integratedcircuit.standard.probe;

import net.common.type.Float2D;
import net.common.type.Int2D;
import net.common.util.StringUtil;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.data.integratedcircuit.standard.probe.ProbeData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.domain.common.wire.TraceValue;
import net.logicim.domain.integratedcircuit.standard.probe.Probe;
import net.logicim.domain.integratedcircuit.standard.probe.ProbePins;
import net.logicim.domain.passive.power.PowerPinNames;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.circle.CircleView;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.component.common.RadixHelper;
import net.logicim.ui.simulation.component.integratedcircuit.extra.FrameView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.StandardIntegratedCircuitView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Font.MONOSPACED;

public class ProbeView
    extends StandardIntegratedCircuitView<Probe, ProbeProperties>
{
  protected static int FONT_SIZE = 10;

  protected PortView port;
  protected FamilyVoltageConfiguration familyVoltageConfiguration;
  protected CircleView circleView;
  protected FrameView frameView;
  protected TextView dataView;
  protected int maxDigits;

  public ProbeView(SubcircuitView containingSubcircuitView,
                   Int2D position,
                   Rotation rotation,
                   ProbeProperties properties)
  {
    super(containingSubcircuitView,
          position,
          rotation,
          properties);

    familyVoltageConfiguration = FamilyVoltageConfigurationStore.get(properties.family);
    maxDigits = RadixHelper.calculateMaxDigits(properties.radix, properties.bitWidth);
    relativeRightRotations = 2;

    createPortViews();
    createGraphics();
    finaliseView();
  }

  public List<String> getPortNames()
  {
    List<String> portNames = new ArrayList<>();
    for (int i = 0; i < properties.bitWidth; i++)
    {
      String portName = "Input " + i;
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

  @Override
  protected Probe createIntegratedCircuit(ViewPath viewPath,
                                          CircuitSimulation circuitSimulation,
                                          FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    SubcircuitSimulation containingSubcircuitSimulation = viewPath.getSubcircuitSimulation(circuitSimulation);
    return new Probe(containingSubcircuitSimulation,
                     properties.name,
                     new ProbePins(familyVoltageConfiguration,
                                   properties.bitWidth));
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
    Probe probe = simulationIntegratedCircuits.get(viewPath, circuitSimulation);
    if (probe != null)
    {
      values = port.getValue(viewPath,
                             circuitSimulation,
                             familyVoltageConfiguration,
                             probe.getVCC(time));
    }

    dataView.setText(RadixHelper.getStringValue(values, properties.radix, maxDigits));
    dataView.paint(graphics, viewport);

    paintPorts(graphics,
               viewport,
               viewPath,
               circuitSimulation);
    graphics.setColor(color);
    graphics.setStroke(stroke);
    graphics.setFont(font);
  }

  public ProbeData save(boolean selected)
  {
    return new ProbeData(position,
                         rotation,
                         properties.name,
                         properties.family,
                         saveEvents(),
                         savePorts(),
                         id,
                         enabled,
                         selected,
                         saveSimulationState(),
                         properties.explicitPowerPorts,
                         properties.bitWidth,
                         properties.radix);
  }

  @Override
  public String getType()
  {
    return "Probe";
  }

  @Override
  public void clampProperties(ProbeProperties newProperties)
  {
//    newProperties.frequency_Hz = clamp(newProperties.frequency_Hz, 0, 999 * GHz);
  }

  @Override
  public String toDebugString()
  {
    return super.toDebugString() + String.format("Input Width [%s]\n", properties.bitWidth);
  }
}

