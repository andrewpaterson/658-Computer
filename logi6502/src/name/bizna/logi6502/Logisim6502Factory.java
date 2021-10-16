package name.bizna.logi6502;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.util.GraphicsUtil;
import com.cburch.logisim.util.StringGetter;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import static name.bizna.logi6502.Logisim65C02Instance.*;

public class Logisim6502Factory
    extends InstanceFactory
{
  private static final int PINS_PER_SIDE = 7;
  private static final int PIXELS_PER_PIN = 20;
  private static final int V_MARGIN = 10;
  private static final int LEFT_X = -120;
  private static final int RIGHT_X = 120;
  private static final int TOP_Y = -120 - V_MARGIN;
  private static final int BOT_Y = 120 + V_MARGIN;
  private static final int PIN_TOP_Y = ((PINS_PER_SIDE - 1) * PIXELS_PER_PIN / -2) - V_MARGIN;
  private static final int PIN_BOT_Y = ((PINS_PER_SIDE - 1) * PIXELS_PER_PIN / 2) + V_MARGIN;
  private static final int PIN_START_Y = PIN_TOP_Y + V_MARGIN + PIXELS_PER_PIN / 2;
  private static final int PIN_STOP_Y = PIN_BOT_Y - V_MARGIN;
  private static final PortInfo[] portInfos = new PortInfo[]
      {
          // Left side, top to bottom
          PortInfo.sharedInput("IRQB"),
          PortInfo.sharedInput("NMIB#"),
          PortInfo.sharedInput("RESB"),
          PortInfo.sharedInput("PHI2"),
          PortInfo.exclusiveOutput("VPB"),
          PortInfo.sharedInput("SOB"),
          null,

          // Right side, bottom to top
          PortInfo.exclusiveOutput("SYNC"),
          PortInfo.exclusiveOutput("MLB"),
          PortInfo.sharedBidirectional("RDY"),
          PortInfo.sharedInput("BE"),
          PortInfo.sharedOutput("RWB"),
          PortInfo.sharedBidirectional("D", 8),
          PortInfo.sharedOutput("A", 16),
          };
  public static final int PORT_IRQB = 0;
  public static final int PORT_NMIB = 1;
  public static final int PORT_RESB = 2;
  public static final int PORT_PHI2 = 3;
  public static final int PORT_VPB = 4;
  public static final int PORT_SOB = 5;
  public static final int PORT_SYNC = 6;
  public static final int PORT_MLB = 7;
  public static final int PORT_RDY = 8;
  public static final int PORT_BE = 9;
  public static final int PORT_RWB = 10;
  public static final int PORT_DataBus = 11;
  public static final int PORT_AddressBus = 12;

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
  public void propagate(InstanceState instanceState)
  {
    Logisim65C02Instance instance = Logisim65C02Instance.getOrCreate(instanceState, this);
    instance.tick(instanceState);
  }

  protected void shred(InstanceState instanceState)
  {
    Logisim65C02Instance instance = Logisim65C02Instance.getOrCreate(instanceState, this);
    instance.shred();
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

      Logisim65C02Instance core = Logisim65C02Instance.getOrCreate(painter, this);
      int topOffset = 30;
      int width8Bit = 38;
      int width16Bit = 52;

      drawInternal(g, topOffset, 35, "Op-code:", core.getOpcodeMnemonicString(), core.isOpcodeValid());
      drawInternal(g, topOffset + 20, width8Bit, "Op-code:", core.getOpcodeValueHex(), core.isOpcodeValid());
      drawInternal(g, topOffset + 40, width8Bit, "Cycle:", core.getCycle(), true);
      drawInternal(g, topOffset + 60, width8Bit, "Accumulator:", core.getAccumulatorValueHex(), true);
      drawInternal(g, topOffset + 80, width8Bit, "X Index:", core.getXValueHex(), true);
      drawInternal(g, topOffset + 100, width8Bit, "Y Index:", core.getYValueHex(), true);
      drawInternal(g, topOffset + 120, width16Bit, "Stack", core.getStackValueHex(), true);
      drawInternal(g, topOffset + 140, width16Bit, "P-Counter:", core.getProgramCounterValueHex(), true);

      int processorStatusTopOffset = topOffset + 185;
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
}

