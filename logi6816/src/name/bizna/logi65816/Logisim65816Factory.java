package name.bizna.logi65816;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.util.GraphicsUtil;
import com.cburch.logisim.util.StringGetter;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.CpuStatus;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import static name.bizna.logi65816.W65C816.*;

public class Logisim65816Factory
    extends InstanceFactory
{
  protected static final int V_MARGIN = 10;
  protected static final int PINS_PER_SIDE = 7;
  protected static final int PIXELS_PER_PIN = 20;
  protected static final int PIN_TOP_Y = ((PINS_PER_SIDE - 1) * PIXELS_PER_PIN / -2) - V_MARGIN;
  protected static final int PIN_BOT_Y = ((PINS_PER_SIDE - 1) * PIXELS_PER_PIN / 2) + V_MARGIN;
  protected static final int PORT_VPB = 0;
  protected static final int PORT_RDY = 1;
  protected static final int PORT_IRQB = 2;
  protected static final int PORT_MLB = 3;
  protected static final int PORT_NMIB = 4;
  protected static final int PORT_SYNC = 5;
  protected static final int PORT_AddressBus = 6;
  protected static final int PORT_DataBus = 7;
  protected static final int PORT_RWB = 8;
  protected static final int PORT_BE = 9;
  protected static final int PORT_PHI2 = 10;
  protected static final int PORT_SOB = 11;
  protected static final int PORT_RESB = 12;

  protected static final int LEFT_X = -120;
  protected static final int RIGHT_X = 120;
  protected static final int TOP_Y = -130 - V_MARGIN;
  protected static final int BOT_Y = 130 + V_MARGIN;
  protected static final int PIN_START_Y = PIN_TOP_Y + V_MARGIN + PIXELS_PER_PIN / 2;
  protected static final int PIN_STOP_Y = PIN_BOT_Y - V_MARGIN;
  protected static final PortInfo[] portInfos = new PortInfo[]
      {
          // Left side, top to bottom
          PortInfo.exclusiveOutput("VPB#"),
          PortInfo.sharedBidirectional("RDY"),
          PortInfo.sharedInput("IRQB#"),
          PortInfo.exclusiveOutput("MLB#"),
          PortInfo.sharedInput("NMIB#"),
          PortInfo.exclusiveOutput("VPA"),
          PortInfo.exclusiveOutput("VDA"),
          // Right side, bottom to top
          PortInfo.sharedOutput("A", 16),
          PortInfo.sharedBidirectional("D", 8),
          PortInfo.sharedOutput("RWB"),
          PortInfo.sharedInput("BE"),
          PortInfo.sharedInput("PHI2"),
          PortInfo.sharedInput("SOB#"),
          PortInfo.sharedInput("RESB#")
      };

  public Logisim65816Factory()
  {
    super("W65C816S");
    setOffsetBounds(Bounds.create(LEFT_X, TOP_Y, RIGHT_X - LEFT_X, BOT_Y - TOP_Y));
    addStandardPins(portInfos, LEFT_X, RIGHT_X, PIN_START_Y, PIN_STOP_Y, PIXELS_PER_PIN, PINS_PER_SIDE);
  }

  void paintPorts(InstancePainter painter, PortInfo[] portInfos, int pinsPerSide)
  {
    int n = 0;
    for (int i = 0; i < portInfos.length; ++i)
    {
      if (portInfos[i] != null)
      {
        Direction dir = i < pinsPerSide ? Direction.EAST : Direction.WEST;
        painter.drawPort(n, portInfos[i].name, dir);
        ++n;
      }
    }
  }

  @Override
  public void paintInstance(InstancePainter painter)
  {
    painter.drawBounds();
    paintPorts(painter, portInfos, PINS_PER_SIDE);

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
      if (bds.getWidth() > bds.getHeight())
      {
        newTransform.quadrantRotate(-1, 0, 0);
      }
      g2.setTransform(newTransform);
      GraphicsUtil.drawCenteredText(g, "W65C816S", 0, TOP_Y + V_MARGIN);

      Logisim65816Data core = Logisim65816Data.getOrCreateLogisim65816Data(painter, this);
      Cpu65816 cpu = core.getCpu();
      CpuStatus status = cpu.getCpuStatus();

      int topOffset = 30;
      int width8Bit = 35;
      int width16Bit = 50;
      int width24Bit = 65;

      drawInternal(g, topOffset, width8Bit, "Op-code:", core.getOpcodeMnemonicString(), core.isOpcodeValid());
      drawInternal(g, topOffset + 20, width8Bit, "Op-code:", core.getOpcodeValueHex(), core.isOpcodeValid());
      drawInternal(g, topOffset + 40, width8Bit, "Cycle:", core.getCycle(), true);
      drawInternal(g, topOffset + 60, width24Bit, "Address:", core.getAddressValueHex(), false);
      drawInternal(g, topOffset + 80, width16Bit, "Accumulator:", core.getAccumulatorValueHex(), true);
      drawInternal(g, topOffset + 100, width16Bit, "X Index:", core.getXValueHex(), true);
      drawInternal(g, topOffset + 120, width16Bit, "Y Index:", core.getYValueHex(), true);
      drawInternal(g, topOffset + 140, width16Bit, "Stack", core.getStackValueHex(), true);
      drawInternal(g, topOffset + 160, width24Bit, "P-Counter:", core.getProgramCounterValueHex(), true);
      drawInternal(g, topOffset + 180, width16Bit, "Data", core.getDataValueHex(), false);

      int processorStatusTopOffset = topOffset + 205;
      boolean emulationMode = status.isEmulationMode();
      drawProcessorStatus(g, processorStatusTopOffset, -60, "C", status.carryFlag());
      drawProcessorStatus(g, processorStatusTopOffset, -40, "Z", status.zeroFlag());
      drawProcessorStatus(g, processorStatusTopOffset, -20, "I", status.interruptDisableFlag());
      drawProcessorStatus(g, processorStatusTopOffset, 0, "D", status.decimalFlag());
      if (emulationMode)
      {
        drawProcessorStatus(g, processorStatusTopOffset, 20, "B", status.breakFlag());
      }
      else
      {
        drawProcessorStatus(g, processorStatusTopOffset, 20, "X", status.isIndex8Bit());
        drawProcessorStatus(g, processorStatusTopOffset, 40, "M", status.isAccumulator8Bit());
      }
      drawProcessorStatus(g, processorStatusTopOffset, 60, "V", status.overflowFlag());
      drawProcessorStatus(g, processorStatusTopOffset, 80, "N", status.negativeFlag());

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

  private void setPort(InstanceState instanceState, int port, boolean value, int delay)
  {
    instanceState.setPort(port, value ? Value.TRUE : Value.FALSE, delay);
  }

  private void setAddressPort(InstanceState instanceState, short a)
  {
    instanceState.setPort(PORT_AddressBus, Value.createKnown(BitWidth.create(16), a), 12);
  }

  private boolean updateBussesEnabledFromNotBusEnabled(InstanceState instanceState)
  {
    if (instanceState.getPortValue(PORT_BE) == Value.FALSE)
    {
      instanceState.setPort(PORT_AddressBus, Value.createUnknown(BitWidth.create(16)), 12);
      instanceState.setPort(PORT_DataBus, Value.createUnknown(BitWidth.create(8)), 12);
      instanceState.setPort(PORT_RWB, Value.UNKNOWN, 12);
      return false;
    }
    else
    {
      return true;
    }
  }

  public void doRead(InstanceState instanceState, short address)
  {
    if (updateBussesEnabledFromNotBusEnabled(instanceState))
    {
      setAddressPort(instanceState, address);
      instanceState.setPort(Logisim65816Factory.PORT_DataBus, Value.createUnknown(BitWidth.create(8)), 9);
      setPort(instanceState, Logisim65816Factory.PORT_RWB, true, 9);
    }
  }

  public void doWrite(InstanceState instanceState, short address, byte data)
  {
    if (updateBussesEnabledFromNotBusEnabled(instanceState))
    {
      setAddressPort(instanceState, address);
      instanceState.setPort(Logisim65816Factory.PORT_DataBus, Value.createKnown(BitWidth.create(8), data), 15);
      setPort(instanceState, Logisim65816Factory.PORT_RWB, false, 9);
    }
  }

  public byte getDataFromPort(InstanceState instanceState)
  {
    return (byte) instanceState.getPortValue(Logisim65816Factory.PORT_DataBus).toLongValue();
  }

  public boolean isOverflow(InstanceState instanceState)
  {
    return instanceState.getPortValue(PORT_SOB) == Value.FALSE;
  }

  public void setReady(InstanceState instanceState, boolean ready)
  {
    instanceState.setPort(PORT_RDY, ready ? Value.UNKNOWN : Value.FALSE, 9);
  }

  public void setVPB(InstanceState instanceState, boolean vectorPull)
  {
    setPort(instanceState, PORT_VPB, !vectorPull, 6);
  }

  public void setSync(InstanceState instanceState, boolean sync)
  {
    setPort(instanceState, PORT_SYNC, sync, 6);
  }

  @Override
  public void propagate(InstanceState state)
  {
    Logisim65816Data core = Logisim65816Data.getOrCreateLogisim65816Data(state, this);
    core.propagate(state);
  }

  protected void addStandardPins(PortInfo[] portInfos, int LEFT_X, int RIGHT_X, int PIN_START_Y, int PIN_STOP_Y, int PIXELS_PER_PIN, int PINS_PER_SIDE)
  {
    ArrayList<Port> ports = new ArrayList<>(portInfos.length);
    for (int n = 0; n < portInfos.length; ++n)
    {
      PortInfo info = portInfos[n];
      if (info == null)
      {
        continue;
      }
      boolean isRightSide = n >= PINS_PER_SIDE;
      int pinPerSide = isRightSide ? n - PINS_PER_SIDE : n;
      Port port;
      if (isRightSide)
      {
        port = new Port(RIGHT_X, PIN_STOP_Y - pinPerSide * PIXELS_PER_PIN, info.type, info.bitWidth, info.exclusive);
      }
      else
      {
        port = new Port(LEFT_X, PIN_START_Y + pinPerSide * PIXELS_PER_PIN, info.type, info.bitWidth, info.exclusive);
      }
      port.setToolTip(new StringGetter()
      {
        @Override
        public String toString()
        {
          return info.name;
        }
      });
      ports.add(port);
    }
    setPorts(ports);
  }

  public void setMLB(InstanceState instanceState, boolean mlb)
  {
    setPort(instanceState, PORT_MLB, mlb, 6);
  }
}

