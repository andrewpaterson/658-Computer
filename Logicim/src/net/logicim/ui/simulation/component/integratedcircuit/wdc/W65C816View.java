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
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.StandardIntegratedCircuitView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Font.MONOSPACED;
import static java.awt.Font.SANS_SERIF;
import static net.logicim.data.integratedcircuit.decorative.HorizontalAlignment.*;

public class W65C816View
    extends StandardIntegratedCircuitView<W65C816, W65C816Properties>
{
  public static int FONT_SIZE = 10;

  protected int yOffset = -8;  //Remember the component is rotated 90 degrees.
  protected int left = 11;
  protected int right = -11;
  protected List<PortView> leftPorts;
  protected List<PortView> rightPorts;
  protected List<TextView> labels;
  protected List<RectangleView> rectangles;
  private TextView opCodeName;
  private TextView opCodeNumber;
  private TextView cycle;
  private TextView accumulator;
  private TextView xIndex;
  private TextView yIndex;
  private TextView stack;
  private TextView directPage;
  private TextView programCounter;
  private TextView dataBank;
  private TextView emulationFlag;
  private TextView carryFlag;
  private TextView zeroFlag;
  private TextView memoryWidthFlag;
  private TextView indexWidthOrBreakFlag;
  private TextView decimalModeFlag;
  private TextView interruptDisableFlag;
  private TextView negativeFlag;
  private TextView overflowFlag;
  private RectangleView memoryWidthRectangle;

  public W65C816View(SubcircuitView subcircuitView,
                     Int2D position,
                     Rotation rotation,
                     W65C816Properties properties)
  {
    super(subcircuitView, position, rotation, properties);

    leftPorts = new ArrayList<>();
    rightPorts = new ArrayList<>();

    createPortViews();
    createGraphics();
    finaliseView();
  }

  private void createGraphics()
  {
    rectangles = new ArrayList<>();
    rectangles.add(new RectangleView(this, 26, right - left, true, true));

    labels = new ArrayList<>();
    for (PortView portView : leftPorts)
    {
      labels.add(new TextView(this,
                              new Float2D(portView.getRelativePosition().x, left - 0.6f),
                              portView.getText(),
                              SANS_SERIF,
                              FONT_SIZE,
                              false,
                              HorizontalAlignment.LEFT));
    }

    for (PortView portView : rightPorts)
    {
      labels.add(new TextView(this,
                              new Float2D(portView.getRelativePosition().x, right + 0.6f),
                              portView.getText(),
                              SANS_SERIF,
                              FONT_SIZE,
                              false,
                              RIGHT));
    }

    float y = -12;
    float yStep = 2;
    labels.add(new TextView(this,
                            new Float2D(y, 0),
                            "Microprocessor",
                            SANS_SERIF,
                            FONT_SIZE,
                            true,
                            CENTER));

    y += yStep;
    opCodeName = createDetail(y, 3, "Op-code", "RES");

    y += yStep;
    opCodeNumber = createDetail(y, 3, "Op-code", "###");

    y += yStep;
    cycle = createDetail(y, 3, "Cycle", "0");

    y += yStep;
    accumulator = createDetail(y, 5, "Accumulator", "0x0000");

    y += yStep;
    xIndex = createDetail(y, 5, "X Index", "0x0000");

    y += yStep;
    yIndex = createDetail(y, 5, "Y Index", "0x0000");

    y += yStep;
    stack = createDetail(y, 5, "Stack", "0x01ff");

    y += yStep;
    directPage = createDetail(y, 5, "Direct page", "0x0000");

    y += yStep;
    programCounter = createDetail(y, 7, "P-Counter", "0x00:0000");

    y += yStep;
    dataBank = createDetail(y, 4, "Data Bank", "0x00");

    float x = 5.5f;
    float xStep = -1.4f;
    y += yStep;
    emulationFlag = createFlag("E", x, y, true, true);
    x += xStep;
    carryFlag = createFlag("C", x, y, false, true);
    x += xStep;
    zeroFlag = createFlag("Z", x, y, false, true);
    x += xStep;
    memoryWidthFlag = createFlag("M", x, y, false, false);
    memoryWidthRectangle = rectangles.get(rectangles.size() - 1);
    x += xStep;
    indexWidthOrBreakFlag = createFlag("B", x, y, false, true);
    x += xStep;
    decimalModeFlag = createFlag("D", x, y, false, true);
    x += xStep;
    interruptDisableFlag = createFlag("I", x, y, false, true);
    x += xStep;
    negativeFlag = createFlag("N", x, y, false, true);
    x += xStep;
    overflowFlag = createFlag("V", x, y, false, true);

    y += yStep;
    labels.add(new TextView(this,
                            new Float2D(y, 0),
                            "W65C816",
                            SANS_SERIF,
                            FONT_SIZE,
                            true,
                            CENTER));
  }

  private TextView createFlag(String name, float x, float y, boolean set, boolean visible)
  {
    TextView flag = new TextView(this,
                                 new Float2D(y, x),
                                 name,
                                 SANS_SERIF,
                                 FONT_SIZE,
                                 false,
                                 CENTER).setVisible(visible);
    if (set)
    {
      flag.setColor(Colours.getInstance().getText());
    }
    else
    {
      flag.setColor(Colours.getInstance().getHiddenText());
    }

    labels.add(flag);
    rectangles.add(new RectangleView(this,
                                     new Float2D(y - 0.6f, x - 0.6f),
                                     new Float2D(y + 0.6f, x + 0.6f),
                                     true,
                                     true)
                       .setLineWidth(1)
                       .setFillColour(Colours.getInstance().getBackground())
                       .setVisible(visible));
    return flag;
  }

  private TextView createDetail(float y, int width, String name, String value)
  {
    createDetailLabel(name, y);
    createDetailRectangle(y, width);
    return createDetailValue(value, y);
  }

  private RectangleView createDetailRectangle(float y, int width)
  {
    RectangleView rectangle = new RectangleView(this,
                                                new Float2D(y - 0.6f, 0),
                                                new Float2D(y + 0.6f, -width),
                                                true,
                                                true).setLineWidth(1).setFillColour(Colours.getInstance().getBackground());
    rectangles.add(rectangle);
    return rectangle;
  }

  private TextView createDetailLabel(String label, float y)
  {
    TextView textView = new TextView(this,
                                     new Float2D(y, 0),
                                     label + ": ",
                                     SANS_SERIF,
                                     FONT_SIZE,
                                     false,
                                     RIGHT);
    labels.add(textView);
    return textView;
  }

  private TextView createDetailValue(String value, float y)
  {
    TextView textView = new TextView(this,
                                     new Float2D(y, -0.6f),
                                     value,
                                     MONOSPACED,
                                     FONT_SIZE,
                                     false,
                                     LEFT).setColor(Colours.getInstance().getCommentText());
    labels.add(textView);
    return textView;
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
    int y = 2;
    PortView phi2PortView = createPortView(y, "PHI2", left).setDrawClock(true);
    leftPorts.add(phi2PortView);

    y += yStep;
    PortView resetPortView = createPortView(y, "RESB", left).setInverting(true, Rotation.South);
    leftPorts.add(resetPortView);

    y += yStep;
    PortView nonMaskableInterruptPortView = createPortView(y, "NMIB", left).setInverting(true, Rotation.South);
    leftPorts.add(nonMaskableInterruptPortView);

    y += yStep;
    PortView interruptRequestPortView = createPortView(y, "IRQB", left).setInverting(true, Rotation.South);
    leftPorts.add(interruptRequestPortView);

    y += yStep;
    PortView abortPortView = createPortView(y, "ABORTB", left).setInverting(true, Rotation.South);
    leftPorts.add(abortPortView);

    y += yStep;
    PortView readyPortView = createPortView(y, "RDY", left);
    leftPorts.add(readyPortView);

    y += yStep;
    PortView busEnablePortView = createPortView(y, "BE", left);
    leftPorts.add(busEnablePortView);

    //Outputs
    y = 0;
    PortView readWritePortView = createPortView(y, "RWB", right);
    rightPorts.add(readWritePortView);

    y += yStep;
    PortView validDataAddressPortView = createPortView(y, "VDA", right);
    rightPorts.add(validDataAddressPortView);

    y += yStep;
    PortView validProgramAddressPortView = createPortView(y, "VPA", right);
    rightPorts.add(validProgramAddressPortView);

    y += yStep;
    PortView vectorPullPortView = createPortView(y, "VPB", right).setInverting(true, Rotation.North);
    rightPorts.add(vectorPullPortView);

    y += yStep;
    PortView addressPortView = createPortView(y, "Address ", 16, right);
    rightPorts.add(addressPortView);

    y += yStep;
    PortView memoryLockPortView = createPortView(y, "MLB", right).setInverting(true, Rotation.North);
    rightPorts.add(memoryLockPortView);

    y += yStep;
    PortView emulationPortView = createPortView(y, "E", right);
    rightPorts.add(emulationPortView);

    y += yStep;
    PortView memoryIndexWidthPortView = createPortView(y, "MX", right);
    rightPorts.add(memoryIndexWidthPortView);

    //Bidirectional
    y += yStep;
    PortView dataPortView = createPortView(y, "Data ", 8, right);
    rightPorts.add(dataPortView);
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

    W65C816 w65c816 = getComponent(subcircuitSimulation);
    if (w65c816 != null)
    {
      opCodeName.setText(w65c816.getOpcodeMnemonicString());
      opCodeNumber.setText(w65c816.getOpcodeValueHex());
      cycle.setText(w65c816.getCycleString());
      accumulator.setText(w65c816.getAccumulatorValueHex());
      xIndex.setText(w65c816.getXValueHex());
      yIndex.setText(w65c816.getYValueHex());
      stack.setText(w65c816.getStackValueHex());
      directPage.setText(w65c816.getDirectPageValueHex());
      programCounter.setText(w65c816.getProgramCounterValueHex());
      dataBank.setText(w65c816.getDataBankValueHex());

      boolean emulation = w65c816.isEmulation();
      emulationFlag.setColor(getFlagColour(emulation));
      carryFlag.setColor(getFlagColour(w65c816.isCarrySet()));
      zeroFlag.setColor(getFlagColour(w65c816.isZeroFlagSet()));
      decimalModeFlag.setColor(getFlagColour(w65c816.isDecimal()));
      interruptDisableFlag.setColor(getFlagColour(w65c816.isInterruptDisable()));
      negativeFlag.setColor(getFlagColour(w65c816.isNegativeSet()));
      overflowFlag.setColor(getFlagColour(w65c816.isOverflowFlag()));

      if (emulation)
      {
        indexWidthOrBreakFlag.setText("B");
        indexWidthOrBreakFlag.setColor(getFlagColour(w65c816.isBreak()));

        memoryWidthFlag.setVisible(false);
        memoryWidthRectangle.setVisible(false);
      }
      else
      {
        indexWidthOrBreakFlag.setText("X");
        indexWidthOrBreakFlag.setColor(getFlagColour(w65c816.isIndex8Bit()));

        memoryWidthFlag.setVisible(true);
        memoryWidthFlag.setColor(getFlagColour(w65c816.isMemory8Bit()));
        memoryWidthRectangle.setVisible(true);
      }
    }

    for (RectangleView rectangle : rectangles)
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

  protected Color getFlagColour(boolean b)
  {
    Colours instance = Colours.getInstance();
    if (b)
    {
      return instance.getText();
    }
    else
    {
      return instance.getHiddenText();
    }
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

