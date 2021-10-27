package net.simulation.gate;

import net.common.PinValue;
import net.common.Pins;
import net.common.Snapshot;
import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.common.Uniport;

import java.util.ArrayList;
import java.util.List;

public class NotGateTickablePins
    extends LogicGateTickablePins<Snapshot, NotGateTickablePins, NotGate>
    implements Pins<Snapshot, NotGateTickablePins, NotGate>
{
  protected Uniport in;
  protected Uniport out;

  public NotGateTickablePins(Tickables tickables, Trace inTrace, Trace outTrace)
  {
    super(tickables, outTrace);
    in = new Uniport(this, "In");
    out = new Uniport(this, "Out");

    in.connect(inTrace);
    out.connect(outTrace);
  }

  public PinValue getInValue()
  {
    return getPinValue(in);
  }

  @Override
  public List<Uniport> getInPorts()
  {
    ArrayList<Uniport> uniports = new ArrayList<>();
    uniports.add(in);
    return uniports;
  }
}

