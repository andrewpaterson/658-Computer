package net.wdc65xx.logisim;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.util.GraphicsUtil;
import com.cburch.logisim.util.StringGetter;
import net.wdc65xx.wdc65816.WDC65C816;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Logisim65816Factory
    extends InstanceFactory
{
  protected static final int V_MARGIN = 10;
  protected static final int PINS_PER_SIDE = 8;
  protected static final int PIXELS_PER_PIN = 20;
  protected static final int PIN_TOP_Y = ((PINS_PER_SIDE - 1) * PIXELS_PER_PIN / -2) - V_MARGIN;
  protected static final int PIN_BOT_Y = ((PINS_PER_SIDE - 1) * PIXELS_PER_PIN / 2) + V_MARGIN;

  protected static final int LEFT_X = -120;
  protected static final int RIGHT_X = 120;
  protected static final int TOP_Y = -120 - V_MARGIN;
  protected static final int BOT_Y = 120 + V_MARGIN;
  protected static final int PIN_START_Y = PIN_TOP_Y + V_MARGIN;
  protected static final int PIN_STOP_Y = PIN_BOT_Y - V_MARGIN;

  // Left side, top to bottom
  protected static final int PORT_ABORT = 0;
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
  protected static final int PORT_Bank = 15;
  protected static final int PORT_AddressBus = 16;

  protected final Map<Integer, PortInfo> portInfos;

  public Logisim65816Factory()
  {
    super("W65C816S");

    PortInfo[] portInfos = new PortInfo[]
        {
            PortInfo.inputShared(PORT_ABORT, "ABORT"),
            PortInfo.inputShared(PORT_IRQB, "IRQB"),
            PortInfo.inputShared(PORT_NMIB, "NMIB"),
            PortInfo.inputShared(PORT_RESB, "RESB"),
            PortInfo.inputShared(PORT_PHI2, "PHI2"),
            PortInfo.outputExclusive(PORT_MX, "M").setHighName("X"),
            PortInfo.outputExclusive(PORT_E, "E"),
            PortInfo.outputExclusive(PORT_MLB, "MLB"),

            PortInfo.inputShared(PORT_BE, "BE"),
            PortInfo.outputExclusive(PORT_VPB, "VPB"),
            PortInfo.outputExclusive(PORT_VPA, "VPA"),
            PortInfo.outputExclusive(PORT_VDA, "VDA"),
            PortInfo.inoutShared(PORT_RDY, "RDY"),
            PortInfo.outputShared(PORT_RWB, "RWB"),
            PortInfo.inoutShared(PORT_DataBus, "D", 8).setHighName(" "),
            PortInfo.inoutShared(PORT_Bank, "", 8).setHighName("BA"),
            PortInfo.outputShared(PORT_AddressBus, "A", 16)};

    this.portInfos = new LinkedHashMap<>();
    for (PortInfo portInfo : portInfos)
    {
      this.portInfos.put(portInfo.index, portInfo);
    }

    setOffsetBounds(Bounds.create(LEFT_X, TOP_Y, RIGHT_X - LEFT_X, BOT_Y - TOP_Y));
    addStandardPins(this.portInfos);
  }

  public Logisim65816Instance getOrCreateInstance(InstanceState instanceState)
  {
    Logisim65816Instance instance = (Logisim65816Instance) instanceState.getData();
    if (instance == null)
    {
      instance = new Logisim65816Instance();
      instanceState.setData(instance);
    }
    return instance;
  }

  public void paintInstance(InstancePainter painter)
  {
    Logisim65816Instance instance = getOrCreateInstance(painter);
    WDC65C816 cpu = instance.getPins().getCpu();
    boolean clockHigh = cpu.getClock();

    painter.drawBounds();
    for (Integer index : portInfos.keySet())
    {
      PortInfo portInfo = portInfos.get(index);
      if (portInfo != null)
      {
        Direction dir = index < Logisim65816Factory.PINS_PER_SIDE ? Direction.EAST : Direction.WEST;
        String name;
        if (!clockHigh)
        {
          name = portInfo.lowName;
        }
        else
        {
          name = portInfo.highName;
        }
        painter.drawPort(portInfo.index, name, dir);
      }
    }

    Graphics g = painter.getGraphics();
    if (g instanceof Graphics2D)
    {
      Font oldFont = g.getFont();
      g.setFont(oldFont.deriveFont(Font.BOLD));
      Bounds bds = painter.getBounds();
      Graphics2D g2 = (Graphics2D) g;

      AffineTransform oldTransform = g2.getTransform();
      AffineTransform newTransform = (AffineTransform) oldTransform.clone();
      newTransform.translate(bds.getX() + bds.getWidth() / 2.0, bds.getY() + bds.getHeight() / 2.0);
      g2.setTransform(newTransform);
      GraphicsUtil.drawCenteredText(g, "W65C816S", 0, TOP_Y + V_MARGIN);

      int topOffset = 30;
      int width8Bit = 38;
      int width16Bit = 52;
      int width24Bit = 70;

      boolean isOpcodeValid = cpu.getCycle() != 0;
      drawInternal(g, topOffset, width8Bit, "Op-code:", cpu.getOpcodeMnemonicString(), isOpcodeValid);
      drawInternal(g, topOffset + 20, width8Bit, "Op-code:", cpu.getOpcodeValueHex(), isOpcodeValid);
      drawInternal(g, topOffset + 40, width8Bit, "Cycle:", Integer.toString(cpu.getCycle()), true);
      drawInternal(g, topOffset + 60, width16Bit, "Accumulator:", cpu.getAccumulatorValueHex(), true);
      drawInternal(g, topOffset + 80, width16Bit, "X Index:", cpu.getXValueHex(), true);
      drawInternal(g, topOffset + 100, width16Bit, "Y Index:", cpu.getYValueHex(), true);
      drawInternal(g, topOffset + 120, width16Bit, "Stack", cpu.getStackValueHex(), true);
      drawInternal(g, topOffset + 140, width24Bit, "P-Counter:", cpu.getProgramCounterValueHex(), true);
      drawInternal(g, topOffset + 160, width8Bit, "Data Bank:", cpu.getDataBankValueHex(), true);

      int processorStatusTopOffset = topOffset + 185;
      boolean emulationMode = cpu.isEmulation();
      drawProcessorStatus(g, processorStatusTopOffset, -60, "C", cpu.isCarry());
      drawProcessorStatus(g, processorStatusTopOffset, -40, "Z", cpu.isZeroFlag());
      drawProcessorStatus(g, processorStatusTopOffset, -20, "I", cpu.isInterruptDisable());
      drawProcessorStatus(g, processorStatusTopOffset, 0, "D", cpu.isDecimal());
      if (emulationMode)
      {
        drawProcessorStatus(g, processorStatusTopOffset, 20, "B", cpu.isBreak());
      }
      else
      {
        drawProcessorStatus(g, processorStatusTopOffset, 20, "X", cpu.isIndex8Bit());
        drawProcessorStatus(g, processorStatusTopOffset, 40, "M", cpu.isMemory8Bit());
      }
      drawProcessorStatus(g, processorStatusTopOffset, 60, "V", cpu.isOverflowFlag());
      drawProcessorStatus(g, processorStatusTopOffset, 80, "N", cpu.isNegative());

      g2.setTransform(oldTransform);
      g.setFont(oldFont);
    }
  }

  private void drawProcessorStatus(Graphics g, int topOffset, int horizontalPosition, String flag, boolean black)
  {
    int top = TOP_Y + V_MARGIN + topOffset;
    g.drawRect(horizontalPosition - 7, top - 5, 15, 15);
    Color oldColour = setColour(g, black);
    GraphicsUtil.drawText(g, flag, horizontalPosition, top, GraphicsUtil.H_CENTER, GraphicsUtil.V_CENTER);
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

  private void drawInternal(Graphics g, int topOffset, int rectangleWidth, String label, String value, boolean black)
  {
    int opcodeMnemonicTop = TOP_Y + V_MARGIN + topOffset;
    g.drawRect(15, opcodeMnemonicTop - 5, rectangleWidth, 15);
    GraphicsUtil.drawText(g, label, 0, opcodeMnemonicTop, GraphicsUtil.H_RIGHT, GraphicsUtil.V_CENTER);
    Color oldColour = setColour(g, black);
    GraphicsUtil.drawText(g, value, 20, opcodeMnemonicTop, GraphicsUtil.H_LEFT, GraphicsUtil.V_CENTER);
    g.setColor(oldColour);
  }

  @Override
  public void propagate(InstanceState instanceState)
  {
    Logisim65816Instance instance = getOrCreateInstance(instanceState);
    instance.tick(instanceState);
  }

  protected void addStandardPins(Map<Integer, PortInfo> portInfos)
  {
    ArrayList<Port> ports = new ArrayList<>();
    for (Integer index : portInfos.keySet())
    {
      PortInfo info = portInfos.get(index);
      if (info == null)
      {
        continue;
      }
      boolean isRightSide = index >= Logisim65816Factory.PINS_PER_SIDE;
      int pinPerSide = isRightSide ? index - Logisim65816Factory.PINS_PER_SIDE : index;
      Port port;
      if (isRightSide)
      {
        port = new Port(Logisim65816Factory.RIGHT_X, Logisim65816Factory.PIN_STOP_Y - pinPerSide * Logisim65816Factory.PIXELS_PER_PIN, info.type, info.bitWidth, info.exclusive);
      }
      else
      {
        port = new Port(Logisim65816Factory.LEFT_X, Logisim65816Factory.PIN_START_Y + pinPerSide * Logisim65816Factory.PIXELS_PER_PIN, info.type, info.bitWidth, info.exclusive);
      }
      port.setToolTip(new StringGetter()
      {
        @Override
        public String toString()
        {
          return info.lowName;
        }
      });
      ports.add(port);
    }
    setPorts(ports);
  }
}

