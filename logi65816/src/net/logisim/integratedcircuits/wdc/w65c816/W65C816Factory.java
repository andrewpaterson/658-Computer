package net.logisim.integratedcircuits.wdc.w65c816;

import com.cburch.logisim.util.GraphicsUtil;
import net.integratedcircuits.wdc.wdc65816.W65C816;
import net.integratedcircuits.wdc.wdc65816.W65C816Snapshot;
import net.logisim.common.ComponentDescription;
import net.logisim.common.PortFactory;
import net.logisim.common.PropagatingInstanceFactory;
import net.logisim.common.SimpleInstancePainter;

import java.awt.*;

import static net.logisim.common.ComponentDescription.PIXELS_PER_PIN;
import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class W65C816Factory
    extends PropagatingInstanceFactory<W65C816LogisimPins>
{
  protected static int PORT_ABORTB;
  protected static int PORT_IRQB;
  protected static int PORT_NMIB;
  protected static int PORT_RESB;
  protected static int PORT_PHI2;
  protected static int PORT_MX;
  protected static int PORT_E;
  protected static int PORT_MLB;

  protected static int PORT_BE;
  protected static int PORT_VPB;
  protected static int PORT_VPA;
  protected static int PORT_VDA;
  protected static int PORT_RDY;
  protected static int PORT_RWB;
  protected static int PORT_DataBus;
  protected static int PORT_AddressBus;

  protected static int PORT_TimingBus1;
  protected static int PORT_TimingBus2;
  protected static int PORT_TimingClock;

  public static W65C816Factory create()
  {
    PortFactory factory = new PortFactory();

    factory.blank(LEFT);
    factory.blank(LEFT);
    PORT_ABORTB = factory.inputShared("ABORTB", LEFT).setInverting().setTooltip("Abort current instruction (input: active low)").index();
    PORT_IRQB = factory.inputShared("IRQB", LEFT).setInverting().setTooltip("Interrupt request (input: active low)").index();
    PORT_NMIB = factory.inputShared("NMIB", LEFT).setInverting().setTooltip("Non-maskable interrupt (input: active low)").index();
    PORT_RESB = factory.inputShared("RESB", LEFT).setInverting().setTooltip("Reset (input: active low)").index();
    PORT_PHI2 = factory.inputShared("PHI2", LEFT).setTooltip("Clock (input)").index();
    PORT_MX = factory.outputExclusive("M", LEFT).setHighName("X").setTooltip("Memory width / Index width (8bit high, 16bit low)").index();
    PORT_E = factory.outputExclusive("E", LEFT).setTooltip("Emulation mode (output: emulation high, native low)").index();
    PORT_MLB = factory.outputExclusive("MLB", LEFT).setInverting().setTooltip("Memory lock (output: read-modify-write low)").index();
    PORT_TimingBus1 = factory.inputShared("", 40, LEFT).setTooltip("Bus input defining MPU pin timings (only used when PHI2 != CLOCK).").index();
    PORT_TimingBus2 = factory.inputShared("", 40, LEFT).setTooltip("Bus input defining MPU pin timings (only used when PHI2 != CLOCK).").index();

    factory.blank(RIGHT);
    factory.blank(RIGHT);
    PORT_AddressBus = factory.outputShared("A", 16, RIGHT).setTooltip("Address (output)").index();
    PORT_DataBus = factory.inoutShared("D", 8, RIGHT).setHighName("BA").setTooltip("Data / Bank address (bi-directional - see data sheet)").index();
    PORT_RWB = factory.outputShared("RWB", RIGHT).setTooltip("Read / Write (output: read high, write low)").index();
    PORT_RDY = factory.inoutShared("RDY", RIGHT).setTooltip("Ready (bi-directional - see data sheet)").index();
    PORT_VDA = factory.outputExclusive("VDA", RIGHT).setTooltip("Valid data address (output: valid high, invalid low)").index();
    PORT_VPA = factory.outputExclusive("VPA", RIGHT).setTooltip("Valid program address (output: valid high, invalid low)").index();
    PORT_VPB = factory.outputExclusive("VPB`", RIGHT).setInverting().setTooltip("Interrupt vector pull (output: fetching interrupt address low)").index();
    PORT_BE = factory.inputShared("BE", RIGHT).setTooltip("Bus enable (input: A, D and RWB enabled high, A, D and RWB high impedance low").index();
    factory.blank(RIGHT);
    PORT_TimingClock = factory.inputShared("", RIGHT).setTooltip("Clock input to ensure MPU ticks even when PHI2 is not changing.").index();

    return new W65C816Factory(new ComponentDescription(W65C816.class.getSimpleName(),
                                                       W65C816.TYPE,
                                                       240,
                                                       240,
                                                       factory.getPorts(),
                                                       factory.getCommonPortNames()));
  }

  private W65C816Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(W65C816LogisimPins instance, Graphics2D graphics2D)
  {
    W65C816 cpu = instance.getCpu();
    W65C816Snapshot snapshot = instance.getPainterSnapshot();
    boolean isOpcodeValid = snapshot != null && snapshot.cycle != 0;

    int topOffset = PIXELS_PER_PIN / 2 + 2;
    drawField(graphics2D, getTopOffset(0) - topOffset, WIDTH_8BIT, "Op-code:", cpu.getOpcodeMnemonicString(snapshot), isOpcodeValid);
    drawField(graphics2D, getTopOffset(1) - topOffset, WIDTH_8BIT, "Op-code:", cpu.getOpcodeValueHex(snapshot), isOpcodeValid);
    drawField(graphics2D, getTopOffset(2) - topOffset, WIDTH_8BIT, "Cycle:", cpu.getCycleString(snapshot), true);
    drawField(graphics2D, getTopOffset(3) - topOffset, WIDTH_16BIT, "Accumulator:", cpu.getAccumulatorValueHex(), true);
    drawField(graphics2D, getTopOffset(4) - topOffset, WIDTH_16BIT, "X Index:", cpu.getXValueHex(), true);
    drawField(graphics2D, getTopOffset(5) - topOffset, WIDTH_16BIT, "Y Index:", cpu.getYValueHex(), true);
    drawField(graphics2D, getTopOffset(6) - topOffset, WIDTH_16BIT, "Stack", cpu.getStackValueHex(), true);
    drawField(graphics2D, getTopOffset(7) - topOffset, WIDTH_16BIT, "Direct Page", cpu.getDirectPageValueHex(), true);
    drawField(graphics2D, getTopOffset(8) - topOffset, WIDTH_24BIT, "P-Counter:", cpu.getProgramCounterValueHex(), true);
    drawField(graphics2D, getTopOffset(9) - topOffset, WIDTH_8BIT, "Data Bank:", cpu.getDataBankValueHex(), true);

    int processorStatusTopOffset = TOP_OFFSET + 185;
    boolean emulationMode = cpu.isEmulation();
    int xOffset = 0;
    if (cpu.isTimingConnected())
    {
      xOffset = -20;
      drawProcessorStatus(graphics2D, processorStatusTopOffset, 100, cpu.getTiming(), false);
    }
    drawProcessorStatus(graphics2D, processorStatusTopOffset, -80 + xOffset, "E", emulationMode);
    drawProcessorStatus(graphics2D, processorStatusTopOffset, -60 + xOffset, "C", cpu.isCarry());
    drawProcessorStatus(graphics2D, processorStatusTopOffset, -40 + xOffset, "Z", cpu.isZeroFlag());
    drawProcessorStatus(graphics2D, processorStatusTopOffset, -20 + xOffset, "I", cpu.isInterruptDisable());
    drawProcessorStatus(graphics2D, processorStatusTopOffset, xOffset, "D", cpu.isDecimal());
    if (emulationMode)
    {
      drawProcessorStatus(graphics2D, processorStatusTopOffset, 20 + xOffset, "B", cpu.isBreak());
    }
    else
    {
      drawProcessorStatus(graphics2D, processorStatusTopOffset, 20 + xOffset, "X", cpu.isIndex8Bit());
      drawProcessorStatus(graphics2D, processorStatusTopOffset, 40 + xOffset, "M", cpu.isMemory8Bit());
    }
    drawProcessorStatus(graphics2D, processorStatusTopOffset, 60 + xOffset, "V", cpu.isOverflowFlag());
    drawProcessorStatus(graphics2D, processorStatusTopOffset, 80 + xOffset, "N", cpu.isNegative());
  }

  private void drawProcessorStatus(Graphics g, int topOffset, int horizontalPosition, String flag, boolean black)
  {
    int top = description.getTopYPlusMargin() + topOffset;
    g.drawRect(horizontalPosition - 7, top - 5, 15, 15);
    Color oldColour = SimpleInstancePainter.setColour(g, black);
    GraphicsUtil.drawText(g, flag, horizontalPosition, top, GraphicsUtil.H_CENTER, GraphicsUtil.V_CENTER);
    g.setColor(oldColour);
  }

  @Override
  protected W65C816LogisimPins createInstance()
  {
    W65C816LogisimPins pins = new W65C816LogisimPins();
    new W65C816("", pins);
    return pins;
  }
}

