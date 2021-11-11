package net.logisim.integratedcircuits.wdc.w65c816;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import net.integratedcircuits.wdc.wdc65816.W65C816;
import net.integratedcircuits.wdc.wdc65816.W65C816Pins;
import net.integratedcircuits.wdc.wdc65816.W65C816Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.wdc.w65c816.W65C816Factory.*;
import static net.util.IntUtil.toByte;

public class W65C816LogisimPins
    extends LogisimPins<W65C816Snapshot, W65C816Pins, W65C816>
    implements W65C816Pins
{
  protected boolean clock;

  @Override
  public void setAddress(int address)
  {
    if (getCpu().isBusEnable())
    {
      instanceState.setPort(PORT_AddressBus, Value.createKnown(BitWidth.create(16), address), 12);
    }
  }

  @Override
  public int getData()
  {
    if (getCpu().isBusEnable())
    {
      int value = (int) instanceState.getPortValue(PORT_DataBus).toLongValue();
      instanceState.setPort(PORT_DataBus, Value.createUnknown(BitWidth.create(8)), 9);
      return toByte(value);
    }
    else
    {
      return 0;
    }
  }

  @Override
  public int peekData()
  {
    int value = (int) instanceState.getPortValue(PORT_DataBus).toLongValue();
    return toByte(value);
  }

  @Override
  public void setData(int data)
  {
    if (getCpu().isBusEnable())
    {
      instanceState.setPort(PORT_DataBus, Value.createKnown(BitWidth.create(8), data), 15);
    }
  }

  @Override
  public void setBank(int data)
  {
    if (getCpu().isBusEnable())
    {
      instanceState.setPort(PORT_DataBus, Value.createKnown(BitWidth.create(8), data), 15);
    }
  }

  @Override
  public void setRWB(boolean rwB)
  {
    if (getCpu().isBusEnable())
    {
      setPort(PORT_RWB, rwB, 9);
    }
  }

  @Override
  public void setEmulation(boolean emulation)
  {
    setPort(PORT_E, emulation, 10);
  }

  @Override
  public void setMemoryLockB(boolean memoryLockB)
  {
    setPort(PORT_MLB, memoryLockB, 10);
  }

  @Override
  public void setMX(boolean m)
  {
    setPort(PORT_MX, m, 10);
  }

  @Override
  public void setRdy(boolean rdy)
  {
    setPort(PORT_RDY, rdy, 10);
  }

  @Override
  public void setVectorPullB(boolean vectorPullB)
  {
    setPort(PORT_VPB, vectorPullB, 10);
  }

  @Override
  public void setValidProgramAddress(boolean validProgramAddress)
  {
    setPort(PORT_VPA, validProgramAddress, 10);
  }

  @Override
  public void setValidDataAddress(boolean validDataAddress)
  {
    setPort(PORT_VDA, validDataAddress, 10);
  }

  @Override
  public boolean isBusEnable()
  {
    return instanceState.getPortValue(PORT_BE) != Value.FALSE;
  }

  @Override
  public boolean isAbort()
  {
    return instanceState.getPortValue(PORT_ABORTB) == Value.FALSE;
  }

  @Override
  public boolean isReset()
  {
    return instanceState.getPortValue(PORT_RESB) == Value.FALSE;
  }

  @Override
  public boolean isIRQ()
  {
    return instanceState.getPortValue(PORT_IRQB) == Value.FALSE;
  }

  @Override
  public boolean isNMI()
  {
    return instanceState.getPortValue(PORT_NMIB) == Value.FALSE;
  }

  @Override
  public void disableBusses()
  {
    instanceState.setPort(PORT_AddressBus, Value.createUnknown(BitWidth.create(16)), 10);
    instanceState.setPort(PORT_DataBus, Value.createUnknown(BitWidth.create(8)), 11);
    instanceState.setPort(PORT_RWB, Value.UNKNOWN, 10);
  }

  @Override
  public boolean isClock()
  {
    return instanceState.getPortValue(PORT_PHI2) == Value.TRUE;
  }

  @Override
  public long getTimingValue()
  {
    Value portValue = instanceState.getPortValue(PORT_TimingBus);
    return portValue.toLongValue();
  }

  public W65C816 getCpu()
  {
    return getIntegratedCircuit();
  }

  private void setPort(int port, boolean value, int delay)
  {
    instanceState.setPort(port, value ? Value.TRUE : Value.FALSE, delay);
  }

  @Override
  public boolean isClockHigh()
  {
    return getCpu().isClock();
  }
}

