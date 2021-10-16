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

  private int data;
  private int address;
  private int bank;
  private boolean rwB;
  private boolean emulation;
  private boolean memoryLockB;
  private boolean m;
  private boolean x;
  private boolean rdy;
  private boolean vectorPullB;
  private boolean validProgramAddress;
  private boolean validDataAddress;

  private boolean abortB;
  private boolean be;
  private boolean irqB;
  private boolean nmiB;

  public LogisimPins65816()
  {
  }

  @Override
  public void setAddress(int address)
  {
    this.address = address;
  }

  @Override
  public int getData()
  {
    return data;
  }

  @Override
  public void setData(int data)
  {
    this.data = toByte(data);
  }

  @Override
  public void setBank(int data)
  {
    this.bank = data;
  }

  @Override
  public void setRWB(boolean rwB)
  {
    this.rwB = rwB;
  }

  @Override
  public void setEmulation(boolean emulation)
  {
    this.emulation = emulation;
  }

  @Override
  public void setMemoryLockB(boolean memoryLockB)
  {
    this.memoryLockB = memoryLockB;
  }

  @Override
  public void setM(boolean m)
  {
    this.m = m;
  }

  @Override
  public void setX(boolean x)
  {
    this.x = x;
  }

  @Override
  public void setRdy(boolean rdy)
  {
    this.rdy = rdy;
  }

  @Override
  public void setVectorPullB(boolean vectorPullB)
  {
    this.vectorPullB = vectorPullB;
  }

  @Override
  public void setValidProgramAddress(boolean validProgramAddress)
  {
    this.validProgramAddress = validProgramAddress;
  }

  @Override
  public void setValidDataAddress(boolean validDataAddress)
  {
    this.validDataAddress = validDataAddress;
  }

  @Override
  public void setCpu(WDC65C816 cpu)
  {
    this.cpu = cpu;
  }
//
//  @Override
//  public void setAllOutputsUnknown()
//  {
//    instanceState.setPort(PORT_AddressBus, Value.createUnknown(BitWidth.create(16)), 10);
//    instanceState.setPort(PORT_DataBus, Value.createUnknown(BitWidth.create(8)), 11);
//    instanceState.setPort(PORT_Bank, Value.createUnknown(BitWidth.create(8)), 14);
//    instanceState.setPort(PORT_RWB, Value.UNKNOWN, 10);
//    instanceState.setPort(PORT_RDY, Value.UNKNOWN, 10);
//    instanceState.setPort(PORT_E, Value.UNKNOWN, 10);
//    instanceState.setPort(PORT_MLB, Value.UNKNOWN, 10);
//    instanceState.setPort(PORT_VPB, Value.UNKNOWN, 10);
//    instanceState.setPort(PORT_VPA, Value.UNKNOWN, 10);
//    instanceState.setPort(PORT_VDA, Value.UNKNOWN, 10);
//    instanceState.setPort(PORT_MX, Value.UNKNOWN, 10);
//  }
//
//  @Override
//  public void setAllOutputsError()
//  {
//    instanceState.setPort(PORT_AddressBus, Value.createError(BitWidth.create(16)), 10);
//    instanceState.setPort(PORT_DataBus, Value.createError(BitWidth.create(8)), 11);
//    instanceState.setPort(PORT_Bank, Value.createError(BitWidth.create(8)), 14);
//    instanceState.setPort(PORT_RWB, Value.ERROR, 10);
//    instanceState.setPort(PORT_RDY, Value.ERROR, 10);
//    instanceState.setPort(PORT_E, Value.ERROR, 10);
//    instanceState.setPort(PORT_MLB, Value.ERROR, 10);
//    instanceState.setPort(PORT_VPB, Value.ERROR, 10);
//    instanceState.setPort(PORT_VPA, Value.ERROR, 10);
//    instanceState.setPort(PORT_VDA, Value.ERROR, 10);
//    instanceState.setPort(PORT_MX, Value.ERROR, 10);
//  }

  @Override
  public boolean isAbortB()
  {
    return abortB;
  }

  @Override
  public boolean isBusEnable()
  {
    return be;
  }

  @Override
  public boolean isIrqB()
  {
    return irqB;
  }

  @Override
  public boolean isNmiB()
  {
    return nmiB;
  }

  public void setAbortB(boolean abortB)
  {
    this.abortB = abortB;
  }

  public void setBe(boolean be)
  {
    this.be = be;
  }

  public void setIrqB(boolean irqB)
  {
    this.irqB = irqB;
  }

  public void setNmiB(boolean nmiB)
  {
    this.nmiB = nmiB;
  }

  private boolean updateBussesEnabledFromNotBusEnabled(InstanceState instanceState)
  {
    if (isBusEnable())
    {
      instanceState.setPort(PORT_AddressBus, Value.createUnknown(BitWidth.create(16)), 10);
      instanceState.setPort(PORT_DataBus, Value.createUnknown(BitWidth.create(8)), 11);
      instanceState.setPort(PORT_Bank, Value.createUnknown(BitWidth.create(8)), 14);
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

  public void readInputs(InstanceState instanceState)
  {
    data = toByte((int) instanceState.getPortValue(PORT_DataBus).toLongValue());
    abortB = instanceState.getPortValue(PORT_ABORT) != Value.FALSE;
    be = instanceState.getPortValue(PORT_BE) != Value.FALSE;
    irqB = instanceState.getPortValue(PORT_IRQB) != Value.FALSE;
    nmiB = instanceState.getPortValue(PORT_NMIB) != Value.FALSE;
  }

  public void writeOutputs(InstanceState instanceState)
  {
    if (rwB)
    {
      doRead(instanceState);
    }
    else
    {
      doWrite(instanceState);
    }

    setPort(instanceState, PORT_E, emulation, 10);
    setPort(instanceState, PORT_MLB, memoryLockB, 10);
    setPort(instanceState, PORT_MX, m, 10);
    setPort(instanceState, PORT_RDY, rdy, 10);
    setPort(instanceState, PORT_VPB, vectorPullB, 10);
    setPort(instanceState, PORT_VPA, validProgramAddress, 10);
    setPort(instanceState, PORT_VDA, validDataAddress, 10);
//    instanceState.setPort(PORT_Bank, Value.createUnknown(BitWidth.create(8)), 15);
//    setPort(instanceState, PORT_MX, X, 10);
  }

  public void doRead(InstanceState instanceState)
  {
//    if (updateBussesEnabledFromNotBusEnabled(instanceState))
    {
      instanceState.setPort(PORT_AddressBus, Value.createKnown(BitWidth.create(16), address), 12);
      instanceState.setPort(PORT_DataBus, Value.createUnknown(BitWidth.create(8)), 9);
      setPort(instanceState, PORT_RWB, true, 9);
    }
  }

  public void doWrite(InstanceState instanceState)
  {
//    if (updateBussesEnabledFromNotBusEnabled(instanceState))
    {
      instanceState.setPort(PORT_AddressBus, Value.createKnown(BitWidth.create(16), address), 12);
      instanceState.setPort(PORT_DataBus, Value.createKnown(BitWidth.create(8), data), 15);
      setPort(instanceState, PORT_RWB, false, 9);
    }
  }

  private void setPort(InstanceState instanceState, int port, boolean value, int delay)
  {
    instanceState.setPort(port, value ? Value.TRUE : Value.FALSE, delay);
  }
}

