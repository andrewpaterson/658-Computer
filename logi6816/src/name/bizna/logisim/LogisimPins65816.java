package name.bizna.logisim;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceState;
import name.bizna.cpu.Pins65816;

import static name.bizna.logisim.Logisim65816Factory.*;

public class LogisimPins65816
    implements Pins65816
{
  private final InstanceState instanceState;

  private int data;                     //bi-directional
  private boolean rdy;                  //bi-directional.  Also.  Fuck-it I'm treating this an output.

  public LogisimPins65816(InstanceState state)
  {
    this.instanceState = state;
  }

  @Override
  public void setAddress(int address)
  {
    instanceState.setPort(PORT_AddressBus, Value.createKnown(BitWidth.create(16), address), 12);
  }

  @Override
  public int getData()
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

  @Override
  public void setData(int data)
  {
    this.data = data;
    instanceState.setPort(Logisim65816Factory.PORT_DataBus, Value.createKnown(BitWidth.create(8), data), 15);
  }

  @Override
  public void setRwb(boolean rwb)
  {
    instanceState.setPort(PORT_RWB, rwb ? Value.TRUE : Value.FALSE, 9);
  }

  @Override
  public boolean getPhi2()
  {
    return getBooleanValue(PORT_PHI2);
  }

  @Override
  public void setEmulation(boolean emulation)
  {
    setBooleanValue(PORT_E, emulation, 9);
  }

  @Override
  public void setMemoryLockB(boolean memoryLockB)
  {
    setBooleanValue(PORT_MLB, memoryLockB, 9);
  }

  @Override
  public void setMX(boolean mx)
  {
    setBooleanValue(PORT_MX, mx, 9);
  }

  @Override
  public void setRdy(boolean rdy)
  {
    setBooleanValue(PORT_RDY, rdy, 12);
  }

  @Override
  public void setVectorPullB(boolean vectorPullB)
  {
    setBooleanValue(PORT_VPB, vectorPullB, 9);
  }

  @Override
  public void setValidProgramAddress(boolean validProgramAddress)
  {
    setBooleanValue(PORT_VPA, validProgramAddress, 9);
  }

  @Override
  public void setValidDataAddress(boolean validDataAddress)
  {
    setBooleanValue(PORT_VDA, validDataAddress, 9);
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

