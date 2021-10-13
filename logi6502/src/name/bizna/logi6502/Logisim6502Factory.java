package name.bizna.logi6502;

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

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import static name.bizna.logi6502.Logisim65C02Data.*;

public class Logisim6502Factory
    extends InstanceFactory
{
  private static final int PINS_PER_SIDE = 7;
  private static final int PIXELS_PER_PIN = 20;
  private static final int V_MARGIN = 10;
  private static final int LEFT_X = -120;
  private static final int RIGHT_X = 120;
  private static final int TOP_Y = -130 - V_MARGIN;
  private static final int BOT_Y = 130 + V_MARGIN;
  private static final int PIN_TOP_Y = ((PINS_PER_SIDE - 1) * PIXELS_PER_PIN / -2) - V_MARGIN;
  private static final int PIN_BOT_Y = ((PINS_PER_SIDE - 1) * PIXELS_PER_PIN / 2) + V_MARGIN;
  private static final int PIN_START_Y = PIN_TOP_Y + V_MARGIN + PIXELS_PER_PIN / 2;
  private static final int PIN_STOP_Y = PIN_BOT_Y - V_MARGIN;
  private static final PortInfo[] portInfos = new PortInfo[]
      {
          // Left side, top to bottom
          PortInfo.exclusiveOutput("VPB#"),
          PortInfo.sharedBidirectional("RDY"),
          PortInfo.sharedInput("IRQB#"),
          PortInfo.exclusiveOutput("MLB#"),
          PortInfo.sharedInput("NMIB#"),
          PortInfo.exclusiveOutput("SYNC"),
          null,
          // Right side, bottom to top
          PortInfo.sharedOutput("A", 16),
          PortInfo.sharedBidirectional("D", 8),
          PortInfo.sharedOutput("RWB"),
          PortInfo.sharedInput("BE"),
          PortInfo.sharedInput("PHI2"),
          PortInfo.sharedInput("SOB#"),
          PortInfo.sharedInput("RESB#")
      };
  public static final int PORT_VPB = 0;
  public static final int PORT_RDY = 1;
  public static final int PORT_IRQB = 2;
  public static final int PORT_MLB = 3;
  public static final int PORT_NMIB = 4;
  public static final int PORT_SYNC = 5;
  public static final int PORT_AddressBus = 6;
  public static final int PORT_DataBus = 7;
  public static final int PORT_RWB = 8;
  public static final int PORT_BE = 9;
  public static final int PORT_PHI2 = 10;
  public static final int PORT_SOB = 11;
  public static final int PORT_RESB = 12;

  public Logisim6502Factory()
  {
    super("W65C02S");
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
  public void propagate(InstanceState state)
  {
    Logisim65C02Data core = Logisim65C02Data.getOrCreate(state, this);
    core.tick(state, isReset(state), getPHI2(state));
  }

  protected void shred(InstanceState state)
  {
    Logisim65C02Data core = Logisim65C02Data.getOrCreate(state, this);
    core.shred();
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
      GraphicsUtil.drawCenteredText(g, "W65C02S", 0, TOP_Y + V_MARGIN);

      Logisim65C02Data core = Logisim65C02Data.getOrCreate(painter, this);
      int topOffset = 30;
      drawInternal(g, topOffset, 35, "Op-code:", core.getOpcodeMnemonicString(), core.isOpcodeValid());
      drawInternal(g, topOffset + 20, 35, "Op-code:", core.getOpcodeValueHex(), core.isOpcodeValid());
      drawInternal(g, topOffset + 40, 35, "Cycle:", core.getCycle(), true);
      drawInternal(g, topOffset + 60, 50, "Address:", core.getAddressValueHex(), core.isAddressValid());
      drawInternal(g, topOffset + 80, 35, "Accumulator:", core.getAccumulatorValueHex(), true);
      drawInternal(g, topOffset + 100, 35, "X Index:", core.getXValueHex(), true);
      drawInternal(g, topOffset + 120, 35, "Y Index:", core.getYValueHex(), true);
      drawInternal(g, topOffset + 140, 35, "Stack", core.getStackValueHex(), true);
      drawInternal(g, topOffset + 160, 50, "P-Counter:", core.getProgramCounterValueHex(), true);
      drawInternal(g, topOffset + 180, 35, "Data", core.getDataValueHex(), core.isDataValid());

      int processorStatusTopOffset = topOffset + 205;
      drawProcessorStatus(g, processorStatusTopOffset, -60, "C", core.isProcessorStatus(P_C_BIT));
      drawProcessorStatus(g, processorStatusTopOffset, -40, "Z", core.isProcessorStatus(P_Z_BIT));
      drawProcessorStatus(g, processorStatusTopOffset, -20, "I", core.isProcessorStatus(P_I_BIT));
      drawProcessorStatus(g, processorStatusTopOffset, 0, "D", core.isProcessorStatus(P_D_BIT));
      drawProcessorStatus(g, processorStatusTopOffset, 20, "B", core.isProcessorStatus(P_B_BIT));
      drawProcessorStatus(g, processorStatusTopOffset, 40, "V", core.isProcessorStatus(P_V_BIT));
      drawProcessorStatus(g, processorStatusTopOffset, 60, "N", core.isProcessorStatus(P_N_BIT));

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

  protected boolean isReset(InstanceState instanceState)
  {
    return instanceState.getPortValue(PORT_RESB) != Value.TRUE;
  }

  protected boolean getPHI2(InstanceState instanceState)
  {
    return instanceState.getPortValue(PORT_PHI2) != Value.FALSE;
  }

  public boolean isInterruptRequest(InstanceState instanceState)
  {
    return instanceState.getPortValue(PORT_IRQB) == Value.FALSE;
  }

  public boolean isNonMaskableInterrupt(InstanceState instanceState)
  {
    return instanceState.getPortValue(PORT_NMIB) == Value.FALSE;
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
      instanceState.setPort(PORT_DataBus, Value.createUnknown(BitWidth.create(8)), 9);
      setPort(instanceState, PORT_RWB, true, 9);
    }
  }

  public void doWrite(InstanceState instanceState, short address, byte data)
  {
    if (updateBussesEnabledFromNotBusEnabled(instanceState))
    {
      setAddressPort(instanceState, address);
      instanceState.setPort(PORT_DataBus, Value.createKnown(BitWidth.create(8), data), 15);
      setPort(instanceState, PORT_RWB, false, 9);
    }
  }

  public byte getDataFromPort(InstanceState instanceState)
  {
    return (byte) instanceState.getPortValue(PORT_DataBus).toLongValue();
  }

  public boolean isReady(InstanceState instanceState)
  {
    return instanceState.getPortValue(PORT_RDY) != Value.FALSE;
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

  public void setMLB(InstanceState instanceState, boolean mlb)
  {
    setPort(instanceState, PORT_MLB, mlb, 6);
  }
}

