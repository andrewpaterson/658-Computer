package net.logisim.common;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceState;
import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;

public abstract class LogisimPins
    implements InstanceData
{
  protected InstanceState instanceState;

  @Override
  public Object clone()
  {
    try
    {
      return super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      return null;
    }
  }

  public void startPropagation()
  {
  }

  public void undoPropagation()
  {
  }

  public void donePropagation()
  {
  }

  public void setInstanceState(InstanceState instanceState)
  {
    this.instanceState = instanceState;
  }

  public boolean isClockHigh()
  {
    return false;
  }

  protected BusValue getBusValue(int portIndex, int width, int delay)
  {
    if (instanceState.isPortConnected(portIndex))
    {
      Value portValue = instanceState.getPortValue(portIndex);
      if (portValue.isErrorValue())
      {
        return BusValue.error();
      }
      else if (portValue.isUnknown())
      {
        return BusValue.unknown();
      }
      else
      {
        instanceState.setPort(portIndex, Value.createUnknown(BitWidth.create(width)), delay);
        return new BusValue(portValue.toLongValue());
      }
    }
    else
    {
      return BusValue.notConnected();
    }
  }

  public void propagate()
  {
    undoPropagation();
    getIntegratedCircuit().tick();
  }

  public abstract IntegratedCircuit getIntegratedCircuit();

  protected PinValue getPinValue(int portIndex)
  {
    Value value = instanceState.getPortValue(portIndex);
    if (value.isErrorValue())
    {
      return PinValue.Error;
    }
    else if (value == Value.TRUE)
    {
      return PinValue.High;
    }
    else if (value == Value.FALSE)
    {
      return PinValue.Low;
    }
    else
    {
      return PinValue.Unknown;
    }
  }
}

