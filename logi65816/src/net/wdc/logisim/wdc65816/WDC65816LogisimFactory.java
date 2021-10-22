package net.wdc.logisim.wdc65816;

import com.cburch.logisim.util.GraphicsUtil;
import net.logisim.common.ComponentDescription;
import net.logisim.common.LogisimFactory;
import net.wdc.wdc65816.WDC65816;

import java.awt.*;

import static net.logisim.common.PortDescription.*;

public class WDC65816LogisimFactory
    extends LogisimFactory<WDC65816LogisimPins>
{
  // Left side, top to bottom
  protected static final int PORT_ABORTB = 0;
  protected static final int PORT_IRQB = 1;
  protected static final int PORT_NMIB = 2;
  protected static final int PORT_RESB = 3;
  protected static final int PORT_PHI2 = 4;
  protected static final int PORT_MX = 5;
  protected static final int PORT_E = 6;
  protected static final int PORT_MLB = 7;

  // Right side, bottom to top
  protected static final int PORT_BE = 8;
  protected static final int PORT_VPB = 9;
  protected static final int PORT_VPA = 10;
  protected static final int PORT_VDA = 11;
  protected static final int PORT_RDY = 12;
  protected static final int PORT_RWB = 13;
  protected static final int PORT_DataBus = 14;
  protected static final int PORT_AddressBus = 15;

  public WDC65816LogisimFactory()
  {
    super("W65C816S",
          new ComponentDescription(240, 240, 10, false,
                                   inputShared(PORT_ABORTB, "ABORTB").setTooltip("Abort current instruction (input: active low)"),
                                   inputShared(PORT_IRQB, "IRQB").setTooltip("Interrupt request (input: active low)"),
                                   inputShared(PORT_NMIB, "NMIB").setTooltip("Non-maskable interrupt (input: active low)"),
                                   inputShared(PORT_RESB, "RESB").setTooltip("Reset (input: active low)"),
                                   inputShared(PORT_PHI2, "PHI2").setTooltip("Clock (input)"),
                                   outputExclusive(PORT_MX, "M").setHighName("X").setTooltip("Memory width / Index width (8bit high, 16bit low)"),
                                   outputExclusive(PORT_E, "E").setTooltip("Emulation mode (output: emulation high, native low)"),
                                   outputExclusive(PORT_MLB, "MLB").setTooltip("Memory lock (output: read-modify-write low)"),

                                   inputShared(PORT_BE, "BE").setTooltip("Bus enable (input: A, D and RWB enabled high, A, D and RWB high impedance low"),
                                   outputExclusive(PORT_VPB, "VPB").setTooltip("Interrupt vector pull (output: fetching interrupt address low)"),
                                   outputExclusive(PORT_VPA, "VPA").setTooltip("Valid program address (output: valid high, invalid low)"),
                                   outputExclusive(PORT_VDA, "VDA").setTooltip("Valid data address (output: valid high, invalid low)"),
                                   inoutShared(PORT_RDY, "RDY").setTooltip("Ready (bi-directional - see data sheet)"),
                                   outputShared(PORT_RWB, "RWB").setTooltip("Read / Write (output: read high, write low)"),
                                   inoutShared(PORT_DataBus, "D", 8).setHighName("BA").setTooltip("Data / Bank address (bi-directional - see data sheet)"),
                                   outputShared(PORT_AddressBus, "A", 16).setTooltip("Address (output)")
          )
    );
  }

  @Override
  protected void paint(WDC65816LogisimPins instance, Graphics2D graphics2D)
  {
    int topOffset = 30;
    int width8Bit = 38;
    int width16Bit = 52;
    int width24Bit = 70;

    WDC65816 cpu = instance.getCpu();
    boolean isOpcodeValid = cpu.getCycle() != 0;
    drawInternal(graphics2D, topOffset, width8Bit, "Op-code:", cpu.getOpcodeMnemonicString(), isOpcodeValid);
    drawInternal(graphics2D, topOffset + 20, width8Bit, "Op-code:", cpu.getOpcodeValueHex(), isOpcodeValid);
    drawInternal(graphics2D, topOffset + 40, width8Bit, "Cycle:", Integer.toString(cpu.getCycle()), true);
    drawInternal(graphics2D, topOffset + 60, width16Bit, "Accumulator:", cpu.getAccumulatorValueHex(), true);
    drawInternal(graphics2D, topOffset + 80, width16Bit, "X Index:", cpu.getXValueHex(), true);
    drawInternal(graphics2D, topOffset + 100, width16Bit, "Y Index:", cpu.getYValueHex(), true);
    drawInternal(graphics2D, topOffset + 120, width16Bit, "Stack", cpu.getStackValueHex(), true);
    drawInternal(graphics2D, topOffset + 140, width24Bit, "P-Counter:", cpu.getProgramCounterValueHex(), true);
    drawInternal(graphics2D, topOffset + 160, width8Bit, "Data Bank:", cpu.getDataBankValueHex(), true);

    int processorStatusTopOffset = topOffset + 185;
    boolean emulationMode = cpu.isEmulation();
    drawProcessorStatus(graphics2D, processorStatusTopOffset, -80, "E", emulationMode);
    drawProcessorStatus(graphics2D, processorStatusTopOffset, -60, "C", cpu.isCarry());
    drawProcessorStatus(graphics2D, processorStatusTopOffset, -40, "Z", cpu.isZeroFlag());
    drawProcessorStatus(graphics2D, processorStatusTopOffset, -20, "I", cpu.isInterruptDisable());
    drawProcessorStatus(graphics2D, processorStatusTopOffset, 0, "D", cpu.isDecimal());
    if (emulationMode)
    {
      drawProcessorStatus(graphics2D, processorStatusTopOffset, 20, "B", cpu.isBreak());
    }
    else
    {
      drawProcessorStatus(graphics2D, processorStatusTopOffset, 20, "X", cpu.isIndex8Bit());
      drawProcessorStatus(graphics2D, processorStatusTopOffset, 40, "M", cpu.isMemory8Bit());
    }
    drawProcessorStatus(graphics2D, processorStatusTopOffset, 60, "V", cpu.isOverflowFlag());
    drawProcessorStatus(graphics2D, processorStatusTopOffset, 80, "N", cpu.isNegative());
  }

  private void drawProcessorStatus(Graphics g, int topOffset, int horizontalPosition, String flag, boolean black)
  {
    int top = description.getTopYPlusMargin() + topOffset;
    g.drawRect(horizontalPosition - 7, top - 5, 15, 15);
    Color oldColour = setColour(g, black);
    GraphicsUtil.drawText(g, flag, horizontalPosition, top, GraphicsUtil.H_CENTER, GraphicsUtil.V_CENTER);
    g.setColor(oldColour);
  }

  private void drawInternal(Graphics g, int topOffset, int rectangleWidth, String label, String value, boolean black)
  {
    int opcodeMnemonicTop = description.getTopYPlusMargin() + topOffset;
    g.drawRect(15, opcodeMnemonicTop - 5, rectangleWidth, 15);
    GraphicsUtil.drawText(g, label, 0, opcodeMnemonicTop, GraphicsUtil.H_RIGHT, GraphicsUtil.V_CENTER);
    Color oldColour = setColour(g, black);
    GraphicsUtil.drawText(g, value, 20, opcodeMnemonicTop, GraphicsUtil.H_LEFT, GraphicsUtil.V_CENTER);
    g.setColor(oldColour);
  }

  private Color setColour(Graphics g, boolean black)
  {
    Color oldColour = g.getColor();
    if (black)
    {
      g.setColor(Color.black);
    }
    else
    {
      g.setColor(Color.lightGray);
    }
    return oldColour;
  }

  @Override
  protected WDC65816LogisimPins createInstance()
  {
    return new WDC65816LogisimPins();
  }
}

