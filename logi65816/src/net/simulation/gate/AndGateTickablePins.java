package net.simulation.gate;

import net.common.Pins;
import net.common.Snapshot;
import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.common.Uniport;

import java.util.ArrayList;
import java.util.List;

public class AndGateTickablePins
    extends LogicGateTickablePins<Snapshot, AndGateTickablePins, AndGate>
    implements Pins<Snapshot, AndGateTickablePins, AndGate>
{
  protected List<Uniport> ins;
  protected Uniport out;

  public AndGateTickablePins(Tickables tickables, Trace in1, Trace in2, Trace out)
  {
    super(tickables, out);
    this.ins = new ArrayList<>();

    this.ins.add(new Uniport(this, "In"));
    this.ins.add(new Uniport(this, "In"));
    this.out = new Uniport(this, "Out");

    this.ins.get(0).connect(in1);
    this.ins.get(1).connect(in2);
    this.out.connect(out);
  }

  @Override
  public List<Uniport> getInPorts()
  {
    return ins;
  }
}

