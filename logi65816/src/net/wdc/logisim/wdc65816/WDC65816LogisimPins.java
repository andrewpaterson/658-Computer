package net.wdc.logisim.wdc65816;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceState;
import net.logisim.common.LogisimPins;
import net.wdc.wdc65816.WDC65816Snapshot;
import net.wdc.wdc65816.WDC65816;
import net.wdc.wdc65816.WDC65816Pins;

import static net.util.IntUtil.toByte;
import static net.wdc.logisim.wdc65816.WDC65816LogisimFactory.*;

public class WDC65816LogisimPins
    extends LogisimPins
    implements WDC65816Pins

{
  protected WDC65816 cpu;

  protected WDC65816Snapshot snapshot;
  protected boolean clock;

  public WDC65816LogisimPins()
  {
    new WDC65816(this);
    snapshot = null;
  }

  @Override
  public void setAddress(int address)
  {
    if (cpu.isBusEnable())
    {
      instanceState.setPort(PORT_AddressBus, Value.createKnown(BitWidth.create(16), address), 12);
    }
  }

  @Override
  public int getData()
  {
    if (cpu.isBusEnable())
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
  public void setData(int data)
  {
    if (cpu.isBusEnable())
    {
      instanceState.setPort(PORT_DataBus, Value.createKnown(BitWidth.create(8), data), 15);
    }
  }

  @Override
  public void setBank(int data)
  {
    if (cpu.isBusEnable())
    {
      instanceState.setPort(PORT_DataBus, Value.createKnown(BitWidth.create(8), data), 15);
    }
  }

  @Override
  public void setRWB(boolean rwB)
  {
    if (cpu.isBusEnable())
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
  public void setCpu(WDC65816 cpu)
  {
    this.cpu = cpu;
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

  public WDC65816 getCpu()
  {
    return cpu;
  }

  private void setPort(int port, boolean value, int delay)
  {
    instanceState.setPort(port, value ? Value.TRUE : Value.FALSE, delay);
  }

  @Override
  public void tick()
  {
    if (snapshot != null)
    {
      cpu.restoreCpuFromSnapshot(snapshot);
    }

    boolean clock = instanceState.getPortValue(PORT_PHI2) == Value.TRUE;

    cpu.preTick(clock);
    cpu.tick();
  }

  @Override
  public void startPropagation()
  {
    snapshot = cpu.createCpuSnapshot();
  }

  @Override
  public void donePropagation()
  {
    snapshot = null;
  }

  @Override
  public boolean isClockHigh()
  {
    return cpu.isClock();
  }
}

