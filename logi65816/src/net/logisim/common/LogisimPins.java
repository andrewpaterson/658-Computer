package net.logisim.common;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceState;
import net.common.*;

public abstract class LogisimPins<
    SNAPSHOT extends Snapshot,
    PINS extends Pins<SNAPSHOT, PINS, ? extends IntegratedCircuit<SNAPSHOT, PINS>>,
    INTEGRATED_CIRCUIT extends IntegratedCircuit<SNAPSHOT, PINS>>
    implements InstanceData
{
  protected INTEGRATED_CIRCUIT integratedCircuit;
  protected SNAPSHOT snapshot;

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
    snapshot = integratedCircuit.createSnapshot();
    getIntegratedCircuit().startTick();
  }

  public void propagate()
  {
    undoPropagation();
    getIntegratedCircuit().tick();
  }

  public void undoPropagation()
  {
    if (snapshot != null)
    {
      integratedCircuit.restoreFromSnapshot(snapshot);
    }
  }

  public void donePropagation()
  {
    snapshot = null;
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

  public boolean requiresPropagationListener()
  {
    return true;
  }

  public INTEGRATED_CIRCUIT getIntegratedCircuit()
  {
    return integratedCircuit;
  }

  public void setIntegratedCircuit(INTEGRATED_CIRCUIT integratedCircuit)
  {
    this.integratedCircuit = integratedCircuit;
  }

  public int getTickCount()
  {
    return instanceState.getTickCount();
  }
}

