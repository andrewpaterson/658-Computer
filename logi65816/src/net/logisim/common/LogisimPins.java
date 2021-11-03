package net.logisim.common;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceState;
import net.common.*;

import java.util.ArrayList;
import java.util.List;

public abstract class LogisimPins<
    SNAPSHOT extends Snapshot,
    PINS extends Pins<SNAPSHOT, PINS, ? extends IntegratedCircuit<SNAPSHOT, PINS>>,
    INTEGRATED_CIRCUIT extends IntegratedCircuit<SNAPSHOT, PINS>>
    implements InstanceData
{
  protected INTEGRATED_CIRCUIT integratedCircuit;
  protected SNAPSHOT snapshot;
  protected List<SNAPSHOT> painterSnapshots;

  protected InstanceState instanceState;

  public LogisimPins()
  {
    painterSnapshots = new ArrayList<>(2);
    painterSnapshots.add(null);
    painterSnapshots.add(null);
  }

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
    painterSnapshots.set(1, painterSnapshots.get(0));
    painterSnapshots.set(0, snapshot);
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

  protected BusValue getValue(LogiBus logiBus)
  {
    if (instanceState.isPortConnected(logiBus.index))
    {
      Value portValue = instanceState.getPortValue(logiBus.index);
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
        instanceState.setPort(logiBus.index, Value.createUnknown(BitWidth.create(logiBus.width)), logiBus.propagationDelay);
        return new BusValue(portValue.toLongValue());
      }
    }
    else
    {
      return BusValue.notConnected();
    }
  }

  protected PinValue getValue(LogiPin logiPin)
  {
    Value value = instanceState.getPortValue(logiPin.index);
    if (value.isErrorValue())
    {
      return PinValue.Error;
    }
    else if (value == Value.TRUE)
    {
      //You should probably write unknown to this port.
//      instanceState.setPort(logiPin.index, Value.createUnknown(BitWidth.create(1)), logiPin.propagationDelay);
      return PinValue.High;
    }
    else if (value == Value.FALSE)
    {
      //You should probably write unknown to this port.
//      instanceState.setPort(logiPin.index, Value.createUnknown(BitWidth.create(1)), logiPin.propagationDelay);
      return PinValue.Low;
    }
    else
    {
      return PinValue.Unknown;
    }
  }

  protected void setError(LogiBus logiBus)
  {
    instanceState.setPort(logiBus.index,
                          Value.createError(BitWidth.create(logiBus.width)),
                          logiBus.propagationDelay);
  }

  protected void setError(LogiPin logiPin)
  {
    instanceState.setPort(logiPin.index,
                          Value.createError(BitWidth.create(1)),
                          logiPin.propagationDelay);
  }

  protected void setValue(LogiBus logiBus, long value)
  {
    instanceState.setPort(logiBus.index,
                          Value.createKnown(BitWidth.create(logiBus.width), value),
                          logiBus.propagationDelay);
  }

  protected void setValue(LogiPin logiPin, boolean value)
  {
    instanceState.setPort(logiPin.index,
                          Value.createKnown(BitWidth.create(1), value ? 1 : 0),
                          logiPin.propagationDelay);
  }

  protected void setHighImpedance(LogiBus logiBus)
  {
    instanceState.setPort(logiBus.index,
                          Value.createUnknown(BitWidth.create(logiBus.width)),
                          logiBus.propagationDelay);
  }

  public SNAPSHOT getPainterSnapshot()
  {
    return painterSnapshots.get(1);
  }
}

