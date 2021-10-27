package net.simulation.gate;

import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.common.Pins;
import net.common.Snapshot;
import net.simulation.common.*;

import java.util.ArrayList;
import java.util.List;

public abstract class LogicGateTickablePins<
    SNAPSHOT extends Snapshot,
    PINS extends Pins<SNAPSHOT, PINS, ? extends IntegratedCircuit<SNAPSHOT, PINS>>,
    INTEGRATED_CIRCUIT extends IntegratedCircuit<SNAPSHOT, PINS>>
    extends Tickable<SNAPSHOT, PINS, INTEGRATED_CIRCUIT>
    implements Pins<SNAPSHOT, PINS, INTEGRATED_CIRCUIT>
{
  protected Uniport out;

  public LogicGateTickablePins(Tickables tickables, Trace out)
  {
    super(tickables);
    this.out = new Uniport(this, "Out");
    this.out.connect(out);
  }

  public List<PinValue> getInputValues()
  {
    List<Uniport> ins = getInPorts();
    List<PinValue> pinValues = new ArrayList<>(ins.size());
    for (Uniport uniport : ins)
    {
      pinValues.add(getPinValue(uniport));
    }
    return pinValues;
  }

  public PinValue getSingleInputValue()
  {
    TraceValue traceValue = Port.readStates(getInPorts());
    return getPinValue(traceValue);
  }

  public void setOutError()
  {
    out.error();
  }

  public void setOutUnsettled()
  {
    out.unset();
  }

  public void setOutValue(boolean value)
  {
    out.writeBool(value);
  }

  public abstract List<Uniport> getInPorts();
}

