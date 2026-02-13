package net.logicim.ui.simulation.component.integratedcircuit.wdc;

import net.common.type.Float2D;
import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.wdc.W65C816Data;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816Pins;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816State;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.PortViewCreator;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.PortViewCreatorList;
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

  protected static int leftOffset = 11;
  protected static int rightOffset = -11;

  protected List<PortView> leftPorts;
  protected List<PortView> rightPorts;
  protected List<TextView> labels;
  protected List<RectangleView> rectangles;

  protected TextView opCodeName;
  protected TextView opCodeNumber;
  protected TextView cycle;
  protected TextView accumulator;
  protected TextView xIndex;
  protected TextView yIndex;
  protected TextView stack;
  protected TextView directPage;
  protected TextView programCounter;
  protected TextView dataBank;
  protected TextView emulationFlag;
  protected TextView carryFlag;
  protected TextView zeroFlag;
  protected TextView memoryWidthFlag;
  protected TextView indexWidthOrBreakFlag;
  protected TextView decimalModeFlag;
  protected TextView interruptDisableFlag;
  protected TextView negativeFlag;
  protected TextView overflowFlag;

  protected RectangleView memoryWidthRectangle;

  public W65C816View(SubcircuitView subcircuitView,
                     Int2D position,
                     Rotation rotation,
                     W65C816Properties properties)
  {
    super(subcircuitView, position, rotation, properties);

    leftPorts = new ArrayList<>();
    rightPorts = new ArrayList<>();
    labels = new ArrayList<>();

    createPortViews();
    createGraphics();
    finaliseView();
  }

  @SuppressWarnings("SuspiciousNameCombination")
  private void createGraphics()
  {
    rectangles = new ArrayList<>();
    rectangles.add(new RectangleView(this, 26, rightOffset - leftOffset, true, true));

    createPortLabels(labels,
                     FONT_SIZE,
                     leftPorts,
                     rightPorts,
                     leftOffset - 0.6f,
                     rightOffset + 0.6f);

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
    opCodeName = createDetail(y, 4, "Op-code", "RES");

    y += yStep;
    opCodeNumber = createDetail(y, 4, "Op-code", "###");

    y += yStep;
    cycle = createDetail(y, 2, "Cycle", "0");

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

  @SuppressWarnings("SuspiciousNameCombination")
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

  @SuppressWarnings("SuspiciousNameCombination")
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

  @SuppressWarnings("SuspiciousNameCombination")
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
    createLeftAndRightPortViews(leftPorts, rightPorts,
                                leftOffset, rightOffset,
                                -6, -8, 2,
                                new PortViewCreatorList(new PortViewCreator("PHI2", true, false),
                                                        new PortViewCreator("RESB", true),
                                                        new PortViewCreator("NMIB", true),
                                                        new PortViewCreator("IRQB", true),
                                                        new PortViewCreator("ABORTB", true),
                                                        new PortViewCreator("RDY"),
                                                        new PortViewCreator("BE")),
                                new PortViewCreatorList(new PortViewCreator("RWB"),
                                                        new PortViewCreator("VDA"),
                                                        new PortViewCreator("VPA"),
                                                        new PortViewCreator("VPB", true),
                                                        new PortViewCreator("Address", 16),
                                                        new PortViewCreator("MLB", true),
                                                        new PortViewCreator("E"),
                                                        new PortViewCreator("MX"),
                                                        new PortViewCreator("Data", 8)));

  }

  @Override
  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    ViewPath viewPath,
                    CircuitSimulation circuitSimulation)
  {
    super.paint(graphics, viewport, viewPath, circuitSimulation);
    Color color = graphics.getColor();
    Stroke stroke = graphics.getStroke();
    Font font = graphics.getFont();

    W65C816 w65c816 = simulationIntegratedCircuits.get(viewPath, circuitSimulation);
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

      W65C816State state = w65c816.getState();
      boolean emulation = state.isEmulation();
      emulationFlag.setColor(getFlagColour(emulation));
      carryFlag.setColor(getFlagColour(state.isCarrySet()));
      zeroFlag.setColor(getFlagColour(state.isZeroFlagSet()));
      decimalModeFlag.setColor(getFlagColour(state.isDecimal()));
      interruptDisableFlag.setColor(getFlagColour(state.isInterruptDisable()));
      negativeFlag.setColor(getFlagColour(state.isNegativeSet()));
      overflowFlag.setColor(getFlagColour(state.isOverflowFlag()));

      if (emulation)
      {
        indexWidthOrBreakFlag.setText("B");
        indexWidthOrBreakFlag.setColor(getFlagColour(state.isBreak()));

        memoryWidthFlag.setVisible(false);
        memoryWidthRectangle.setVisible(false);
      }
      else
      {
        indexWidthOrBreakFlag.setText("X");
        indexWidthOrBreakFlag.setColor(getFlagColour(state.isIndex8Bit()));

        memoryWidthFlag.setVisible(true);
        memoryWidthFlag.setColor(getFlagColour(state.isMemory8Bit()));
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

    paintPorts(graphics, viewport, viewPath, circuitSimulation);
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
  protected W65C816 createIntegratedCircuit(ViewPath viewPath,
                                            CircuitSimulation circuitSimulation,
                                            FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    SubcircuitSimulation containingSubcircuitSimulation = viewPath.getSubcircuitSimulation(circuitSimulation);
    return new W65C816(containingSubcircuitSimulation,
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

  @Override
  protected W65C816State saveState(W65C816 integratedCircuit)
  {
    W65C816State state = integratedCircuit.getState();
    return new W65C816State(state);
  }
}

