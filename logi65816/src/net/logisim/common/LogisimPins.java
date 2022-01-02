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
    getIntegratedCircuit().doneTick();
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
    return LogiBus.getValue(logiBus, instanceState);
  }

  protected BusValue getValueWithoutSet(LogiBus logiBus)
  {
    return LogiBus.getValueWithoutSet(logiBus, instanceState);
  }

  protected PinValue getValue(LogiPin logiPin)
  {
    return LogiPin.getValue(logiPin, instanceState);
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
    LogiBus.setValue(logiBus, value, instanceState);
  }

  protected void setValue(LogiPin logiPin, boolean value)
  {
    LogiPin.setValue(logiPin, value, instanceState);
  }

  protected void setHighImpedance(LogiBus logiBus)
  {
    instanceState.setPort(logiBus.index,
                          Value.createUnknown(BitWidth.create(logiBus.width)),
                          logiBus.propagationDelay);
  }

  protected void setHighImpedance(LogiPin logiBus)
  {
    instanceState.setPort(logiBus.index,
                          Value.createUnknown(BitWidth.create(1)),
                          logiBus.propagationDelay);
  }

  public SNAPSHOT getPainterSnapshot()
  {
    return painterSnapshots.get(1);
  }
}

