package net.logicim.ui.simulation.component.integratedcircuit.wdc;

import net.common.type.Float2D;
import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.data.integratedcircuit.standard.logic.wdc.W65C816Data;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816Pins;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816State;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.StandardIntegratedCircuitView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Font.SANS_SERIF;

public class W65C816View
    extends StandardIntegratedCircuitView<W65C816, W65C816Properties>
{
  public static int FONT_SIZE = 10;

  protected int yOffset = -8;  //Remember the component is rotated 90 degrees.
  protected int left = 11;
  protected int right = -11;
  protected RectangleView rectangle;
  protected List<PortView> inputs;
  protected List<PortView> outputs;
  protected List<PortView> data;
  protected List<TextView> labels;

  public W65C816View(SubcircuitView subcircuitView,
                     Int2D position,
                     Rotation rotation,
                     W65C816Properties properties)
  {
    super(subcircuitView, position, rotation, properties);

    inputs = new ArrayList<>();
    outputs = new ArrayList<>();
    data = new ArrayList<>();

    createPortViews();
    createGraphics();
    finaliseView();
  }

  private void createGraphics()
  {
    rectangle = new RectangleView(this, 18, right - left, true, true);
    labels = new ArrayList<>();
    for (PortView portView : inputs)
    {
      labels.add(new TextView(this,
                              new Float2D(portView.getRelativePosition().x, left - 0.5f),
                              portView.getText(),
                              SANS_SERIF,
                              FONT_SIZE,
                              false,
                              HorizontalAlignment.LEFT));
    }

    for (PortView portView : outputs)
    {
      labels.add(new TextView(this,
                              new Float2D(portView.getRelativePosition().x, right + 0.5f),
                              portView.getText(),
                              SANS_SERIF,
                              FONT_SIZE,
                              false,
                              HorizontalAlignment.RIGHT));
    }

    for (PortView portView : data)
    {
      labels.add(new TextView(this,
                              new Float2D(portView.getRelativePosition().x - 0.75f, 0),
                              portView.getText(),
                              SANS_SERIF,
                              FONT_SIZE,
                              false,
                              HorizontalAlignment.CENTER));
    }
  }

  @Override
  public void clampProperties(W65C816Properties newProperties)
  {

  }

  @Override
  protected void createPortViews()
  {
    super.createPortViews();

    int yStep = 2;

    //Inputs
    int y = 1;
    PortView phi2PortView = createPortView(y, "PHI2", left).setDrawClock(true);
    inputs.add(phi2PortView);

    y += yStep;
    PortView resetPortView = createPortView(y, "RESB", left).setInverting(true, Rotation.South);
    inputs.add(resetPortView);

    y += yStep;
    PortView nonMaskableInterruptPortView = createPortView(y, "NMIB", left).setInverting(true, Rotation.South);
    inputs.add(nonMaskableInterruptPortView);

    y += yStep;
    PortView interruptRequestPortView = createPortView(y, "IRQB", left).setInverting(true, Rotation.South);
    inputs.add(interruptRequestPortView);

    y += yStep;
    PortView abortPortView = createPortView(y, "ABORTB", left).setInverting(true, Rotation.South);
    inputs.add(abortPortView);

    y += yStep;
    PortView readyPortView = createPortView(y, "RDY", left);
    inputs.add(readyPortView);

    y += yStep;
    PortView busEnablePortView = createPortView(y, "BE", left);
    inputs.add(busEnablePortView);

    //Outputs
    y = 1;
    PortView readWritePortView = createPortView(y, "RWB", right);
    outputs.add(readWritePortView);

    y += yStep;
    PortView validDataAddressPortView = createPortView(y, "VDA", right);
    outputs.add(validDataAddressPortView);

    y += yStep;
    PortView validProgramAddressPortView = createPortView(y, "VPA", right);
    outputs.add(validProgramAddressPortView);

    y += yStep;
    PortView vectorPullPortView = createPortView(y, "VPB", right).setInverting(true, Rotation.North);
    outputs.add(vectorPullPortView);

    y += yStep;
    PortView addressPortView = createPortView(y, "Address ", 16, right);
    outputs.add(addressPortView);

    y += yStep;
    PortView memoryLockPortView = createPortView(y, "MLB", right).setInverting(true, Rotation.North);
    outputs.add(memoryLockPortView);

    y += yStep;
    PortView emulationPortView = createPortView(y, "E", right);
    outputs.add(emulationPortView);

    y += yStep;
    PortView memoryIndexWidthPortView = createPortView(y, "MX", right);
    outputs.add(memoryIndexWidthPortView);

    //Bidirectional
    y += yStep;
    PortView dataPortView = createPortView(y, "Data ", 8, 0);
    data.add(dataPortView);
  }

  private PortView createPortView(int x, String portName, int i, int y)
  {
    return new PortView(this,
                        getPortNames(portName, 0, i),
                        new Int2D(yOffset + x, y)).setText(portName);
  }

  private PortView createPortView(int x, String portName, int y)
  {
    return new PortView(this,
                        portName,
                        new Int2D(yOffset + x, y)).setText(portName.trim());
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, SubcircuitSimulation subcircuitSimulation)
  {
    super.paint(graphics, viewport, subcircuitSimulation);
    Color color = graphics.getColor();
    Stroke stroke = graphics.getStroke();
    Font font = graphics.getFont();

    if (rectangle != null)
    {
      rectangle.paint(graphics, viewport);
    }

    for (TextView label : labels)
    {
      label.paint(graphics, viewport);
    }

    paintPorts(graphics, viewport, subcircuitSimulation);
    graphics.setColor(color);
    graphics.setStroke(stroke);
    graphics.setFont(font);
  }

  @Override
  public String getType()
  {
    return "W65C816 Microprocessor";
  }

  @Override
  protected W65C816 createIntegratedCircuit(SubcircuitSimulation subcircuitSimulation, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new W65C816(subcircuitSimulation.getCircuit(),
                       properties.name,
                       new W65C816Pins(familyVoltageConfiguration));
  }

  @Override
  public W65C816Data save(boolean selected)
  {
    return new W65C816Data(position,
                           rotation,
                           properties.name,
                           properties.family,
                           saveEvents(),
                           savePorts(),
                           id,
                           enabled,
                           selected,
                           saveSimulationState(),
                           properties.explicitPowerPorts);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected W65C816State saveState(W65C816 integratedCircuit)
  {
    W65C816State state = integratedCircuit.getState();
    return new W65C816State();
  }
}

