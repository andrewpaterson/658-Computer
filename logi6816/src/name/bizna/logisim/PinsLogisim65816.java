package name.bizna.logisim;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceState;
import name.bizna.util.EmulatorException;
import name.bizna.cpu.Pins65816;

import static name.bizna.logisim.Logisim65816Factory.*;

public class PinsLogisim65816
    implements Pins65816
{
  private final InstanceState instanceState;

  private int address;                  //output
  private int data;                     //bi-directional
  private boolean read;                 //output
  private boolean emulation;            //output
  private boolean memoryLockB;           //output
  private boolean mx;                   //output
  private boolean ready;                //bi-directional.  Also.  Fuck-it I'm treating this an output.
  private boolean vectorPullB;          //output
  private boolean validProgramAddress;  //output
  private boolean validDataAddress;     //output

  public PinsLogisim65816(InstanceState state)
  {
    this.instanceState = state;
  }

  @Override
  public int getAddress()
  {
    return address;
  }

  @Override
  public void setAddress(int address)
  {
    this.address = address;
    instanceState.setPort(PORT_AddressBus, Value.createKnown(BitWidth.create(16), address), 12);
  }

  @Override
  public int getData()
  {
    if (read)
    {
      //instanceState.setPort(Logisim65816Factory.PORT_DataBus, Value.createUnknown(BitWidth.create(8)), 9);  //How the ever-loving-fuck does this line read data?
      Value portValue = instanceState.getPortValue(PORT_DataBus);
      if (portValue.isFullyDefined())
      {
        return (byte) portValue.toLongValue();
      }
      else
      {
        return 0;
      }
    }
    else
    {
      return (byte)data;
    }
  }

  @Override
  public void setData(int data)
  {
    this.data = data;
    instanceState.setPort(Logisim65816Factory.PORT_DataBus, Value.createKnown(BitWidth.create(8), data), 15);
  }

  @Override
  public void setRead(boolean read)
  {
    this.read = read;
    instanceState.setPort(PORT_RWB, read ? Value.TRUE : Value.FALSE, 9);
  }

  @Override
  public boolean isRead()
  {
    return read;
  }

  @Override
  public boolean getPhi2()
  {
    return getBooleanValue(PORT_PHI2);
  }

  @Override
  public void setPhi2(boolean phi2)
  {
    throw new EmulatorException("Cannot set PHI2 from within the CPU.");
  }

  @Override
  public void setEmulation(boolean emulation)
  {
    this.emulation = emulation;
    setBooleanValue(PORT_E, emulation, 9);
  }

  @Override
  public void setMemoryLockB(boolean memoryLockB)
  {
    this.memoryLockB = memoryLockB;
    setBooleanValue(PORT_MLB, memoryLockB, 9);
  }

  @Override
  public void setMX(boolean mx)
  {
    this.mx = mx;
    setBooleanValue(PORT_MX, mx, 9);
  }

  @Override
  public void setReady(boolean ready)
  {
    this.ready = ready;
    setBooleanValue(PORT_RDY, ready, 12);
  }

  @Override
  public void setVectorPullB(boolean vectorPullB)
  {
    this.vectorPullB = vectorPullB;
    setBooleanValue(PORT_VPB, mx, 9);
  }

  @Override
  public boolean isValidProgramAddress()
  {
    return validProgramAddress;
  }

  @Override
  public void setValidProgramAddress(boolean validProgramAddress)
  {
    this.validProgramAddress = validProgramAddress;
    setBooleanValue(PORT_MX, validProgramAddress, 9);
  }

  @Override
  public boolean isValidDataAddress()
  {
    return validDataAddress;
  }

  @Override
  public void setValidDataAddress(boolean validDataAddress)
  {
    this.validDataAddress = validDataAddress;
    setBooleanValue(PORT_MX, validDataAddress, 9);
  }

  @Override
  public boolean isEmulation()
  {
    return emulation;
  }

  @Override
  public boolean isMemoryLockB()
  {
    return memoryLockB;
  }

  @Override
  public boolean isMX()
  {
    return mx;
  }

  @Override
  public boolean isResetB()
  {
    return getBooleanValue(PORT_RESB);
  }

  @Override
  public boolean isAbortB()
  {
    return getBooleanValue(PORT_ABORT);
  }

  @Override
  public boolean isReady()
  {
    return ready;
  }

  @Override
  public boolean isVectorPullB()
  {
    return vectorPullB;
  }

  @Override
  public boolean isBusEnable()
  {
    return getBooleanValue(PORT_BE);
  }

  @Override
  public boolean isPhi2()
  {
    return getBooleanValue(PORT_PHI2);
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
}

