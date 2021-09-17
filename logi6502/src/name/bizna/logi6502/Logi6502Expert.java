package name.bizna.logi6502;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;

public class Logi6502Expert
    extends Logi6502
{
  private static final int PINS_PER_SIDE = 7;
  private static final int PIXELS_PER_PIN = 20;
  private static final int V_MARGIN = 10;
  private static final int LEFT_X = -80;
  private static final int RIGHT_X = 80;
  private static final int TOP_Y = ((PINS_PER_SIDE - 1) * PIXELS_PER_PIN / -2) - V_MARGIN;
  private static final int BOT_Y = ((PINS_PER_SIDE - 1) * PIXELS_PER_PIN / 2) + V_MARGIN;
  private static final int PIN_START_Y = TOP_Y + V_MARGIN + PIXELS_PER_PIN / 2;
  private static final int PIN_STOP_Y = BOT_Y - V_MARGIN;
  private static final PortInfo[] portInfos = new PortInfo[]{
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

  public Logi6502Expert()
  {
    super("W65C02S (Expert)");
    setOffsetBounds(Bounds.create(LEFT_X, TOP_Y, RIGHT_X - LEFT_X, BOT_Y - TOP_Y));
    addStandardPins(portInfos, LEFT_X, RIGHT_X, PIN_START_Y, PIN_STOP_Y, PIXELS_PER_PIN, PINS_PER_SIDE);
  }

  @Override
  public void paintInstance(InstancePainter painter)
  {
    painter.drawBounds();
    paintPorts(painter, portInfos, PINS_PER_SIDE);
    super.paintInstance(painter);
  }

  @Override
  protected boolean isReset(InstanceState instanceState)
  {
    return instanceState.getPortValue(PORT_RESB) != Value.TRUE;
  }

  @Override
  protected boolean getPHI2(InstanceState instanceState)
  {
    return instanceState.getPortValue(PORT_PHI2) != Value.FALSE;
  }

  @Override
  public boolean isInterruptRequest(InstanceState instanceState)
  {
    return instanceState.getPortValue(PORT_IRQB) == Value.FALSE;
  }

  @Override
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

  @Override
  public void doRead(InstanceState instanceState, short address)
  {
    if (updateBussesEnabledFromNotBusEnabled(instanceState))
    {
      setAddressPort(instanceState, address);
      instanceState.setPort(PORT_DataBus, Value.createUnknown(BitWidth.create(8)), 9);
      setPort(instanceState, PORT_RWB, true, 9);
    }
  }

  @Override
  public void doWrite(InstanceState instanceState, short address, byte data)
  {
    if (updateBussesEnabledFromNotBusEnabled(instanceState))
    {
      setAddressPort(instanceState, address);
      instanceState.setPort(PORT_DataBus, Value.createKnown(BitWidth.create(8), data), 15);
      setPort(instanceState, PORT_RWB, false, 9);
    }
  }

  @Override
  public byte getDataFromPort(InstanceState instanceState)
  {
    return (byte) instanceState.getPortValue(PORT_DataBus).toLongValue();
  }

  @Override
  public boolean isReady(InstanceState instanceState)
  {
    return instanceState.getPortValue(PORT_RDY) != Value.FALSE;
  }

  @Override
  public boolean isOverflow(InstanceState instanceState)
  {
    return instanceState.getPortValue(PORT_SOB) == Value.FALSE;
  }

  @Override
  public void setReady(InstanceState instanceState, boolean ready)
  {
    instanceState.setPort(PORT_RDY, ready ? Value.UNKNOWN : Value.FALSE, 9);
  }

  @Override
  public void setVPB(InstanceState instanceState, boolean vectorPull)
  {
    setPort(instanceState, PORT_VPB, !vectorPull, 6);
  }

  @Override
  public void setSync(InstanceState instanceState, boolean sync)
  {
    setPort(instanceState, PORT_SYNC, sync, 6);
  }

  @Override
  public void setMLB(InstanceState instanceState, boolean mlb)
  {
    setPort(instanceState, PORT_MLB, mlb, 6);
  }
}

