package net.wdc65xx.logisim;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceState;
import net.wdc65xx.wdc65816.Pins65816;
import net.wdc65xx.wdc65816.WDC65C816;

import static net.util.IntUtil.toByte;
import static net.wdc65xx.logisim.Logisim65816Factory.*;

public class LogisimPins65816
    implements Pins65816
{
  private WDC65C816 cpu;

  private InstanceState instanceState;

  public LogisimPins65816()
  {
  }

  @Override
  public void setAddress(int address)
  {
    instanceState.setPort(PORT_AddressBus, Value.createKnown(BitWidth.create(16), address), 12);
  }

  @Override
  public int getData()
  {
    int value = (int) instanceState.getPortValue(PORT_DataBus).toLongValue();
    instanceState.setPort(PORT_DataBus, Value.createUnknown(BitWidth.create(8)), 9);
    return toByte(value);
  }

  @Override
  public void setData(int data)
  {
    instanceState.setPort(PORT_DataBus, Value.createKnown(BitWidth.create(8), data), 15);
  }

  @Override
  public void setBank(int data)
  {
    instanceState.setPort(PORT_DataBus, Value.createKnown(BitWidth.create(8), data), 15);
  }

  @Override
  public void setRWB(boolean rwB)
  {
    setPort(PORT_RWB, rwB, 9);
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
  public void setCpu(WDC65C816 cpu)
  {
    this.cpu = cpu;
  }

  @Override
  public boolean isAbortB()
  {
    return instanceState.getPortValue(PORT_ABORT) != Value.FALSE;
  }

  @Override
  public boolean isBusEnable()
  {
    return instanceState.getPortValue(PORT_BE) != Value.FALSE;
  }

  @Override
  public boolean isIrqB()
  {
    return instanceState.getPortValue(PORT_IRQB) != Value.FALSE;
  }

  @Override
  public boolean isNmiB()
  {
    return instanceState.getPortValue(PORT_NMIB) != Value.FALSE;
  }

  private boolean updateBussesEnabledFromNotBusEnabled(InstanceState instanceState)
  {
    if (isBusEnable())
    {
      instanceState.setPort(PORT_AddressBus, Value.createUnknown(BitWidth.create(16)), 10);
      instanceState.setPort(PORT_DataBus, Value.createUnknown(BitWidth.create(8)), 11);
      instanceState.setPort(PORT_RWB, Value.UNKNOWN, 10);
      return false;
    }
    else
    {
      return true;
    }
  }

  public WDC65C816 getCpu()
  {
    return cpu;
  }

  private void setPort(int port, boolean value, int delay)
  {
    instanceState.setPort(port, value ? Value.TRUE : Value.FALSE, delay);
  }

  public void setInstanceState(InstanceState instanceState)
  {
    this.instanceState = instanceState;
  }
}

