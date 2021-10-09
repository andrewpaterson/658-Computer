package name.bizna.logisim;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.util.GraphicsUtil;
import com.cburch.logisim.util.StringGetter;
import name.bizna.cpu.Cpu65816;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

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
  protected static final int TOP_Y = -130 - V_MARGIN;
  protected static final int BOT_Y = 130 + V_MARGIN;
  protected static final int PIN_START_Y = PIN_TOP_Y + V_MARGIN + PIXELS_PER_PIN / 2;
  protected static final int PIN_STOP_Y = PIN_BOT_Y - V_MARGIN;

  protected static final int PORT_VPB = 0;
  protected static final int PORT_RDY = 1;
  protected static final int PORT_ABORT = 2;
  protected static final int PORT_IRQB = 3;
  protected static final int PORT_MLB = 4;
  protected static final int PORT_NMIB = 5;
  protected static final int PORT_VPA = 6;
  protected static final int PORT_AddressBus = 7;

  protected static final int PORT_RESB = 8;
  protected static final int PORT_VDA = 9;
  protected static final int PORT_MX = 10;
  protected static final int PORT_PHI2 = 11;
  protected static final int PORT_BE = 12;
  protected static final int PORT_E = 13;
  protected static final int PORT_RWB = 14;
  protected static final int PORT_DataBus = 15;

  protected static final PortInfo[] portInfos = new PortInfo[]
      {
          // Left side, top to bottom
          PortInfo.sharedOutput("VPB"),
          PortInfo.sharedBidirectional("RDY"),
          PortInfo.sharedInput("ABORT"),
          PortInfo.sharedInput("IRQB"),
          PortInfo.sharedOutput("MLB"),
          PortInfo.sharedInput("NMIB"),
          PortInfo.sharedOutput("VPA"),
          PortInfo.sharedOutput("A", 16),

          // Right side, bottom to top
          PortInfo.sharedInput("RESB"),
          PortInfo.sharedOutput("VDA"),
          PortInfo.sharedOutput("MX"),
          PortInfo.sharedInput("PHI2"),
          PortInfo.sharedInput("BE"),
          PortInfo.sharedOutput("E"),
          PortInfo.sharedOutput("RWB"),
          PortInfo.sharedBidirectional("D", 8)
      };

  public Logisim65816Factory()
  {
    super("W65C816S");
    setOffsetBounds(Bounds.create(LEFT_X, TOP_Y, RIGHT_X - LEFT_X, BOT_Y - TOP_Y));
    addStandardPins(portInfos, LEFT_X, RIGHT_X, PIN_START_Y, PIN_STOP_Y, PIXELS_PER_PIN, PINS_PER_SIDE);
  }

  void paintPorts(InstancePainter painter)
  {
    int n = 0;
    for (int i = 0; i < Logisim65816Factory.portInfos.length; ++i)
    {
      if (Logisim65816Factory.portInfos[i] != null)
      {
        Direction dir = i < Logisim65816Factory.PINS_PER_SIDE ? Direction.EAST : Direction.WEST;
        painter.drawPort(n, Logisim65816Factory.portInfos[i].name, dir);
        n++;
      }
    }
  }

  @Override
  public void paintInstance(InstancePainter painter)
  {
    painter.drawBounds();
    paintPorts(painter);

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

      int topOffset = 30;
      int width8Bit = 35;
      int width16Bit = 50;
      int width24Bit = 65;

      drawInternal(g, topOffset, width8Bit, "Op-code:", cpu.getOpcodeMnemonicString(), core.isOpcodeValid());
      drawInternal(g, topOffset + 20, width8Bit, "Op-code:", cpu.getOpcodeValueHex(), core.isOpcodeValid());
      drawInternal(g, topOffset + 40, width8Bit, "Cycle:", Integer.toString(cpu.getCycle()), true);
      drawInternal(g, topOffset + 60, width16Bit, "Accumulator:", cpu.getAccumulatorValueHex(), true);
      drawInternal(g, topOffset + 80, width16Bit, "X Index:", cpu.getXValueHex(), true);
      drawInternal(g, topOffset + 100, width16Bit, "Y Index:", cpu.getYValueHex(), true);
      drawInternal(g, topOffset + 120, width16Bit, "Stack", cpu.getStackValueHex(), true);
      drawInternal(g, topOffset + 140, width24Bit, "P-Counter:", cpu.getProgramCounterValueHex(), true);
      drawInternal(g, topOffset + 160, width24Bit, "Address:", cpu.getAddressValueHex(), false);
      drawInternal(g, topOffset + 180, width16Bit, "Data", cpu.getDataValueHex(), false);

      int processorStatusTopOffset = topOffset + 205;
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
  public void propagate(InstanceState state)
  {
    Logisim65816Data core = Logisim65816Data.getOrCreateLogisim65816Data(state, this);
    core.propagate();
  }

  @SuppressWarnings("SameParameterValue")
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
}

