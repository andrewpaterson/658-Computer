package net.simulation.gate;

import net.common.Pins;
import net.common.Snapshot;
import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.common.Uniport;

import java.util.ArrayList;
import java.util.List;

public class OrGateTickablePins
    extends LogicGateTickablePins<Snapshot, OrGateTickablePins, OrGate>
    implements Pins<Snapshot, OrGateTickablePins, OrGate>
{
  protected List<Uniport> ins;

  public OrGateTickablePins(Tickables tickables, Trace in1, Trace in2, Trace out)
  {
    super(tickables, out);
    this.ins = new ArrayList<>();

    this.ins.add(new Uniport(this, "In"));
    this.ins.add(new Uniport(this, "In"));

    this.ins.get(0).connect(in1);
    this.ins.get(1).connect(in2);
  }

  @Override
  public List<Uniport> getInPorts()
  {
    return ins;
  }
}

