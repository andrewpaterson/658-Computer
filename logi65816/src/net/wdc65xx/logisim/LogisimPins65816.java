package net.wdc65xx.logisim;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceState;
import net.util.EmulatorException;
import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Pins65816;

import static net.util.IntUtil.toByte;
import static net.wdc65xx.logisim.Logisim65816Factory.*;

public class LogisimPins65816
    implements Pins65816
{
  private InstanceState instanceState;
  private Cpu65816 cpu;

  public LogisimPins65816()
  {
    this.instanceState = null;
  }

  protected void setInstanceState(InstanceState instanceState)
  {
    this.instanceState = instanceState;
  }

  @Override
  public void setAddress(int address)
  {
    instanceState.setPort(PORT_AddressBus, Value.createKnown(BitWidth.create(16), address), 10);
  }

  @Override
  public int getData()
  {
    instanceState.setPort(PORT_Bank, Value.createUnknown(BitWidth.create(8)), 13);
    Value portValue = instanceState.getPortValue(PORT_DataBus);
    System.out.println("LogisimPins65816.getData - " + portValue.toHexString());
    if (portValue.isFullyDefined())
    {
      long longValue = portValue.toLongValue();
      return toByte((int) longValue);
    }
    else if (portValue.isErrorValue())
    {
      System.out.println("Error");
      setAllOutputsError();
      return 0;
    }
    else if (portValue.isUnknown())
    {
      return 0;
    }
    else
    {
      throw new EmulatorException("Impossible Value");
    }
  }

  @Override
  public void setData(int data)
  {
    instanceState.setPort(PORT_DataBus, Value.createKnown(BitWidth.create(8), data), 11);
    instanceState.setPort(PORT_Bank, Value.createUnknown(BitWidth.create(8)), 14);
  }

  @Override
  public void setBank(int data)
  {
    instanceState.setPort(PORT_DataBus, Value.createUnknown(BitWidth.create(8)), 11);
    instanceState.setPort(PORT_Bank, Value.createKnown(BitWidth.create(8), data), 14);
  }

  @Override
  public void setRwb(boolean rwB)
  {
    instanceState.setPort(PORT_RWB, rwB ? Value.TRUE : Value.FALSE, 10);
  }

  @Override
  public boolean getPhi2()
  {
    return getBooleanValue(PORT_PHI2);
  }

  @Override
  public void setEmulation(boolean emulation)
  {
    setBooleanValue(PORT_E, emulation, 10);
  }

  @Override
  public void setMemoryLockB(boolean memoryLockB)
  {
    setBooleanValue(PORT_MLB, memoryLockB, 10);
  }

  @Override
  public void setMX(boolean mx)
  {
    setBooleanValue(PORT_MX, mx, 10);
  }

  @Override
  public void setRdy(boolean rdy)
  {
    setBooleanValue(PORT_RDY, rdy, 10);
  }

  @Override
  public void setVectorPullB(boolean vectorPullB)
  {
    setBooleanValue(PORT_VPB, vectorPullB, 10);
  }

  @Override
  public void setValidProgramAddress(boolean validProgramAddress)
  {
    setBooleanValue(PORT_VPA, validProgramAddress, 10);
  }

  @Override
  public void setValidDataAddress(boolean validDataAddress)
  {
    setBooleanValue(PORT_VDA, validDataAddress, 10);
  }

  @Override
  public boolean isResetB()
  {
    return getBooleanValue(PORT_RESB);
  }

  @Override
  public void setCpu(Cpu65816 cpu)
  {
    this.cpu = cpu;
  }

  @Override
  public void setAllOutputsUnknown()
  {
    instanceState.setPort(PORT_AddressBus, Value.createUnknown(BitWidth.create(16)), 10);
    instanceState.setPort(PORT_DataBus, Value.createUnknown(BitWidth.create(8)), 11);
    instanceState.setPort(PORT_Bank, Value.createUnknown(BitWidth.create(8)), 14);
    instanceState.setPort(PORT_RWB, Value.UNKNOWN, 10);
    instanceState.setPort(PORT_RDY, Value.UNKNOWN, 10);
    instanceState.setPort(PORT_E, Value.UNKNOWN, 10);
    instanceState.setPort(PORT_MLB, Value.UNKNOWN, 10);
    instanceState.setPort(PORT_VPB, Value.UNKNOWN, 10);
    instanceState.setPort(PORT_VPA, Value.UNKNOWN, 10);
    instanceState.setPort(PORT_VDA, Value.UNKNOWN, 10);
    instanceState.setPort(PORT_MX, Value.UNKNOWN, 10);
  }

  @Override
  public void setAllOutputsError()
  {
    instanceState.setPort(PORT_AddressBus, Value.createError(BitWidth.create(16)), 10);
    instanceState.setPort(PORT_DataBus, Value.createError(BitWidth.create(8)), 11);
    instanceState.setPort(PORT_Bank, Value.createError(BitWidth.create(8)), 14);
    instanceState.setPort(PORT_RWB, Value.ERROR, 10);
    instanceState.setPort(PORT_RDY, Value.ERROR, 10);
    instanceState.setPort(PORT_E, Value.ERROR, 10);
    instanceState.setPort(PORT_MLB, Value.ERROR, 10);
    instanceState.setPort(PORT_VPB, Value.ERROR, 10);
    instanceState.setPort(PORT_VPA, Value.ERROR, 10);
    instanceState.setPort(PORT_VDA, Value.ERROR, 10);
    instanceState.setPort(PORT_MX, Value.ERROR, 10);
  }

  @Override
  public boolean isAbortB()
  {
    return getBooleanValue(PORT_ABORT);
  }

  @Override
  public boolean isBusEnable()
  {
    return getBooleanValue(PORT_BE);
  }

  @Override
  public boolean isIrqB()
  {
    return getBooleanValue(PORT_IRQB);
  }

  @Override
  public boolean isNmiB()
  {
    return getBooleanValue(PORT_NMIB);
  }

  private void setBooleanValue(int portIndex, boolean value, int delay)
  {
    instanceState.setPort(portIndex, value ? Value.TRUE : Value.FALSE, delay);
  }

  private boolean getBooleanValue(int portIndex)
  {
    Value portValue = instanceState.getPortValue(portIndex);
    if (portValue.isFullyDefined())
    {
      int width = portValue.getWidth();
      if (width == 1)
      {
        return portValue.toLongValue() != 0;
      }
    }
    return false;
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
}

